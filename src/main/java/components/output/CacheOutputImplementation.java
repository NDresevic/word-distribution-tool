package components.output;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class CacheOutputImplementation implements CacheOutput{

    private final ExecutorService threadPool;

    private BlockingQueue<OutputData> outputQueue;
    // [naziv rezultata -> brojevi pojavljivanja svih vreća te arnosti u toj datoteci]
    private Map<String, Map<String, Integer>> resultMap;

    private Map<String, Integer> aggregatedResultsMap;

    private ObservableList<String> observableFiles;

    public CacheOutputImplementation(ExecutorService threadPool) {
        this.threadPool = threadPool;
        this.outputQueue = new LinkedBlockingQueue<>();
        this.resultMap = new HashMap<>();
        this.aggregatedResultsMap = new ConcurrentHashMap<>();
        this.observableFiles = FXCollections.observableArrayList();

        this.threadPool.execute(this);
    }

    private void aggregateResults() {
        for (OutputData outputData: this.outputQueue) {
            try {
                Map<String, Integer> bagOfWordsOccurrence = outputData.getBagOfWordsOccurrenceMap().get();
                for (Map.Entry<String, Integer> entry: bagOfWordsOccurrence.entrySet()) {
                    String key = entry.getKey();
                    if (!this.aggregatedResultsMap.containsKey(key)) {
                        this.aggregatedResultsMap.put(key, entry.getValue());
                    } else {
                        int value = this.aggregatedResultsMap.get(key);
                        this.aggregatedResultsMap.put(key, value + entry.getValue());
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                OutputData outputData = outputQueue.take();
//                outputDa
                Map<String, Integer> bagOfWordsOccurrence = outputData.getBagOfWordsOccurrenceMap().get();
//                outputData.getBagOfWordsOccurrenceMap().is
                System.out.println("FINISHED: nOfKeys: " + bagOfWordsOccurrence.size() + " for file: " + outputData.getName());

                this.resultMap.put(outputData.getName(), bagOfWordsOccurrence);
                Platform.runLater(() -> {
                    this.observableFiles.remove("*" + outputData.getName());
                    this.observableFiles.add(outputData.getName());
                });
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void take(String fileName) {

    }

    @Override
    public void addOutputDataToQueue(OutputData outputData) {
        this.outputQueue.add(outputData);
        Platform.runLater(() -> this.observableFiles.add("*" + outputData.getName()));
    }

    @Override
    public Map<String, Integer> getResultMapForFile(String name) {
        return resultMap.get(name);
    }

    public ObservableList<String> getObservableFiles() {
        return observableFiles;
    }
}
