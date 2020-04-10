package components.cruncher;

import components.input.InputData;
import components.output.CacheOutput;
import components.output.OutputData;
import gui.view.MainStage;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class CounterCruncherImplementation implements CounterCruncher {

    private final int arity;
    private final ForkJoinPool threadPool;
    private Label crunchingLabel;

    private BlockingQueue<InputData> inputQueue;
    private List<CacheOutput> outputQueues;

    public CounterCruncherImplementation(int arity, ForkJoinPool threadPool, Label crunchingLabel) {
        this.arity = arity;
        this.threadPool = threadPool;
        this.crunchingLabel = crunchingLabel;
        this.inputQueue = new LinkedBlockingQueue<>();
        this.outputQueues = new ArrayList<>();

        this.threadPool.execute(this);
    }

    @Override
    public void addInputDataToQueue(InputData inputData) {
        try {
            this.inputQueue.put(inputData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                InputData inputData = inputQueue.take();
                if (inputData.isPoisoned()) {
                    OutputData poison = new OutputData(true);
                    this.sendProcessedDataToOutput(poison);
                    break;
                }
                Platform.runLater(() -> crunchingLabel.setText(crunchingLabel.getText() + "\n" + inputData.getName()));

                String text = inputData.getContent();
                Future<Map<String, Integer>> resultFuture =
                        this.threadPool.submit((new CounterOccurrenceTask(0, text.length(), text, arity)));

                String name = inputData.getName() + "-arity" + this.arity;
                OutputData outputData = new OutputData(name, resultFuture);
                System.out.println("BEGAN: " + outputData.getName());
                this.sendProcessedDataToOutput(outputData);

                // updates GUI when crunching is finished
                this.threadPool.execute(() -> {
                    try {
                        Map<String, Integer> result = resultFuture.get();
                        Platform.runLater(() -> crunchingLabel.setText(crunchingLabel.getText().replace("\n" + inputData.getName(), "")));
                    } catch (OutOfMemoryError e) {
                        Platform.runLater(() -> MainStage.getInstance().handleOutOfMemoryError());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
            }
        }  catch (RejectedExecutionException e) {
            return;
        } catch (OutOfMemoryError e) {
            Platform.runLater(() -> MainStage.getInstance().handleOutOfMemoryError());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("CounterCruncher is stopped..");
    }

    @Override
    public void sendProcessedDataToOutput(OutputData outputData) {
        for (CacheOutput cacheOutput: this.outputQueues) {
            cacheOutput.addOutputDataToQueue(outputData);
        }
    }

    @Override
    public void addOutput(CacheOutput cacheOutput) {
        this.outputQueues.add(cacheOutput);
    }

    @Override
    public void removeOutput(CacheOutput cacheOutput) {
        this.outputQueues.remove(cacheOutput);
    }
}
