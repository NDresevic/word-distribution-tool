package components.output;

import gui.model.FileModel;
import gui.view.MainStage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class CacheOutputImplementation implements CacheOutput{

    private final ExecutorService threadPool;

    private BlockingQueue<OutputData> outputQueue;
    // [naziv rezultata -> brojevi pojavljivanja svih vreća te arnosti u toj datoteci]
    private ConcurrentMap<String, Future<Map<String, Integer>>> resultMap;

    private ObservableList<FileModel> observableFiles;

    public CacheOutputImplementation(ExecutorService threadPool) {
        this.threadPool = threadPool;
        this.outputQueue = new LinkedBlockingQueue<>();
        this.resultMap = new ConcurrentHashMap<>();
        this.observableFiles = FXCollections.observableArrayList();

        this.threadPool.execute(this);
    }

    // TODO: zvezda, progress bar
    @Override
    public void aggregateResults(String fileName, List<FileModel> files, ProgressBar progressBar) {

        this.threadPool.execute(() -> {
            System.out.println("agg uso");
            int time = (int) System.currentTimeMillis();
            final double updateValue = files.size() / 100;

            Future<Map<String, Integer>> resultFuture = threadPool.submit(() -> {
                Map<String, Integer> aggregatedResultMap = new HashMap<>();
                for (FileModel fileModel: files) {
                    progressBar.setProgress(progressBar.getProgress() + updateValue);
                    for (Map.Entry<String, Integer> entry: this.take(fileModel.getName()).entrySet()) {
                        String key = entry.getKey();
                        if (!aggregatedResultMap.containsKey(key)) {
                            aggregatedResultMap.put(key, entry.getValue());
                        } else {
                            int value = aggregatedResultMap.get(key);
                            aggregatedResultMap.put(key, value + entry.getValue());
                        }
                    }

                    Platform.runLater(() -> MainStage.getInstance().getOutputView().removeProgressBar(progressBar));
                }

                return aggregatedResultMap;
            });

            this.resultMap.put(fileName, resultFuture);
            FileModel fileModel = new FileModel(fileName);
            Platform.runLater(() -> this.observableFiles.add(fileModel));

            try {
                Map<String, Integer> result = this.resultMap.get(fileName).get();
                fileModel.setComplete(true);
//            this.observableFiles.re
//            Platform.runLater(() -> fileModel.setComplete(true));

//            Platform.runLater(() -> this.observableFiles.get(fileModel).setComplete(true));
//            this.observableFiles.get(fileModel).setComplete(true);
//            Platform.runLater(() ->
//                    this.observableFiles.set(this.observableFiles.indexOf("*" + fileName), fileName));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            System.out.println(System.currentTimeMillis() - time);
            System.out.println("agg izaso");
        });

    }

    @Override
    public void run() {
        try {
            while (true) {
                OutputData outputData = outputQueue.take();
                if (outputData.isPoisoned()) {
                    break;
                }
                this.threadPool.execute(() -> this.saveAndProcessData(outputData));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("CacheOutput is stopped..");
    }

    // TODO: bag kad promenis fajl pa opet dodas
    private void saveAndProcessData(OutputData outputData) {
        this.resultMap.put(outputData.getName(), outputData.getBagOfWordsOccurrenceMap());
        FileModel fileModel = new FileModel(outputData.getName());
        Platform.runLater(() -> this.observableFiles.add(fileModel));
//        Platform.runLater(() -> this.observableFiles.add("*" + outputData.getName()));

        try {
            Map<String, Integer> bagOfWordsOccurrence = outputData.getBagOfWordsOccurrenceMap().get();

            System.out.println("FINISHED: nOfKeys: " + bagOfWordsOccurrence.size() + " for file: " + outputData.getName());
            fileModel.setComplete(true);
//            Platform.runLater(() ->
//                    this.observableFiles.set(this.observableFiles.indexOf("*" + outputData.getName()), outputData.getName()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Integer> poll(String fileName) {
        if (this.resultMap.get(fileName) != null && this.resultMap.get(fileName).isDone()) {
            return this.take(fileName);
        }
        return null;
    }

    @Override
    public Map<String, Integer> take(String fileName) {
        try {
            return this.resultMap.get(fileName).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addOutputDataToQueue(OutputData outputData) {
        this.outputQueue.add(outputData);
    }

    public ObservableList<FileModel> getObservableFiles() {
        return observableFiles;
    }
}
