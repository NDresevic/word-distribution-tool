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

    // TODO: zvezda, progress bar
    @Override
    public void aggregateResults(String fileName, List<FileModel> files, ProgressBar progressBar) {
        this.threadPool.execute(() -> {
            try {
                final double updateValue = files.size() / 100;
                Future<Map<String, Integer>> resultFuture = threadPool.submit(() ->
                        this.getAggregatedMap(files, progressBar, updateValue));
                this.resultMap.put(fileName, resultFuture);

                FileModel fileModel = new FileModel(fileName);
                Platform.runLater(() -> this.observableFiles.add(fileModel));

                Map<String, Integer> result = this.resultMap.get(fileName).get();
                int index = this.observableFiles.indexOf(fileModel);
                Platform.runLater(() -> this.observableFiles.get(index).setComplete(true));
            } catch (OutOfMemoryError e) {
                Platform.runLater(() -> MainStage.getInstance().handleOutOfMemoryError());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private Map<String, Integer> getAggregatedMap(List<FileModel> files, ProgressBar progressBar, double updateValue) throws OutOfMemoryError {
        Map<String, Integer> aggregatedResultMap = new HashMap<>();

        for (FileModel fileModel: files) {
            Platform.runLater(() -> progressBar.setProgress(progressBar.getProgress() + updateValue));

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

    private void saveAndProcessData(OutputData outputData) {
        this.resultMap.put(outputData.getName(), outputData.getBagOfWordsOccurrenceMap());
        FileModel fileModel = new FileModel(outputData.getName());
        Platform.runLater(() -> {
            if (!this.observableFiles.contains(fileModel)) {
                this.observableFiles.add(fileModel);
            }
        });
//        Platform.runLater(() -> this.observableFiles.add("*" + outputData.getName()));

        try {
            Map<String, Integer> bagOfWordsOccurrence = outputData.getBagOfWordsOccurrenceMap().get();

            System.out.println("FINISHED: nOfKeys: " + bagOfWordsOccurrence.size() + " for file: " + outputData.getName());
            fileModel.setComplete(true);
//            Platform.runLater(() ->
//                    this.observableFiles.set(this.observableFiles.indexOf("*" + outputData.getName()), outputData.getName()));
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
