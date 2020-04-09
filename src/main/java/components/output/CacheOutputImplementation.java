package components.output;

import gui.model.FileModel;
import gui.view.MainStage;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CacheOutputImplementation implements CacheOutput{

    private final ExecutorService threadPool;

    private BlockingQueue<OutputData> outputQueue;
    /**
     * [name of the file -> occurrence number of all bags with given arity in the file]
     */
    private ConcurrentMap<String, Future<Map<String, Integer>>> resultMap;

    /**
     * List of files that are currently processing or have been processed shown in GUI output list view.
     */
    private ObservableList<FileModel> observableFiles;

    public CacheOutputImplementation(ExecutorService threadPool) {
        this.threadPool = threadPool;
        this.outputQueue = new LinkedBlockingQueue<>();
        this.resultMap = new ConcurrentHashMap<>();
        this.observableFiles = FXCollections.observableArrayList();

        this.threadPool.execute(this);
    }

    @Override
    public void aggregateResults(String fileName, List<FileModel> files, ProgressBar progressBar) {
        try {
            this.threadPool.execute(() -> {
                try {
                    Future<Map<String, Integer>> resultFuture = threadPool.submit(() ->
                            this.getAggregatedMap(files, progressBar, (double) 1 / (double) files.size()));
                    this.saveAndShowData(fileName, resultFuture);
                } catch (RejectedExecutionException e) {
                    return;
                }
            });
        } catch (RejectedExecutionException e) {
            return;
        }
    }

    private Map<String, Integer> getAggregatedMap(List<FileModel> files, ProgressBar progressBar, double updateValue) throws OutOfMemoryError {
        Map<String, Integer> aggregatedResultMap = new HashMap<>();

        for (FileModel fileModel: files) {
            Map<String, Integer> occurrenceMap = this.take(fileModel.getName());
            aggregatedResultMap = Stream.of(occurrenceMap, aggregatedResultMap)
                    .flatMap(map -> map.entrySet().stream())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            Integer::sum));


            Platform.runLater(() -> progressBar.setProgress(progressBar.getProgress() + updateValue));
        }
        Platform.runLater(() -> MainStage.getInstance().getOutputView().removeProgressBar(progressBar));

        return aggregatedResultMap;
    }

    @Override
    public void run() {
        try {
            while (true) {
                OutputData outputData = outputQueue.take();
                if (outputData.isPoisoned()) {
                    break;
                }
                this.threadPool.execute(() ->
                        this.saveAndShowData(outputData.getName(), outputData.getBagOfWordsOccurrenceMap()));
            }
        } catch (RejectedExecutionException e) {
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("CacheOutput is stopped..");
    }

    private void saveAndShowData(String name, Future<Map<String, Integer>> bagOfWordsOccurrenceMap) {
        try {
            this.resultMap.put(name, bagOfWordsOccurrenceMap);
            FileModel fileModel = new FileModel(name);
            Platform.runLater(() -> {
                if (!this.observableFiles.contains(fileModel)) {
                    this.observableFiles.add(fileModel);
                }
            });

            Map<String, Integer> bagOfWordsOccurrence = bagOfWordsOccurrenceMap.get();
            System.out.println("FINISHED: nOfKeys: " + bagOfWordsOccurrence.size() + " for file: " + name);
            fileModel.setComplete(true);
            Platform.runLater(() -> this.observableFiles.set(this.observableFiles.indexOf(fileModel), fileModel));
        } catch (OutOfMemoryError e) {
            Platform.runLater(() -> MainStage.getInstance().handleOutOfMemoryError());
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
        } catch (OutOfMemoryError e) {
            Platform.runLater(() -> MainStage.getInstance().handleOutOfMemoryError());
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
