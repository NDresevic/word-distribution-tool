package components.output;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class CacheOutputImplementation implements CacheOutput{

    private final ExecutorService threadPool;

    private BlockingQueue<OutputData> outputQueue;
    // [naziv rezultata -> brojevi pojavljivanja svih vreća te arnosti u toj datoteci]
    private ConcurrentMap<String, Future<Map<String, Integer>>> resultMap;

    private ObservableList<String> observableFiles;

    public CacheOutputImplementation(ExecutorService threadPool) {
        this.threadPool = threadPool;
        this.outputQueue = new LinkedBlockingQueue<>();
        this.resultMap = new ConcurrentHashMap<>();
        this.observableFiles = FXCollections.observableArrayList();

        this.threadPool.execute(this);
    }

    // TODO: zvezda, progress bar
    @Override
    public void aggregateResults(String fileName, List<String> files) {
        System.out.println("agg uso");
        int time = (int) System.currentTimeMillis();
        Future<Map<String, Integer>> resultFuture = threadPool.submit(() -> {
            Map<String, Integer> aggregatedResultMap = new HashMap<>();
            for (String file: files) {
                for (Map.Entry<String, Integer> entry: this.take(file).entrySet()) {
                    String key = entry.getKey();
                    if (!aggregatedResultMap.containsKey(key)) {
                        aggregatedResultMap.put(key, entry.getValue());
                    } else {
                        int value = aggregatedResultMap.get(key);
                        aggregatedResultMap.put(key, value + entry.getValue());
                    }
                }


                // progres bar
            }

            return aggregatedResultMap;
        });

        this.resultMap.put(fileName, resultFuture);
        Platform.runLater(() -> this.observableFiles.add("*" + fileName));

        try {
            Map<String, Integer> result = this.resultMap.get(fileName).get();
            Platform.runLater(() ->
                    this.observableFiles.set(this.observableFiles.indexOf("*" + fileName), fileName));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(System.currentTimeMillis() - time);
        System.out.println("agg izaso");
    }

    @Override
    public void run() {
        try {
            while (true) {
                OutputData outputData = outputQueue.take();
                this.threadPool.execute(() -> this.storeETC(outputData));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void storeETC(OutputData outputData) {
        this.resultMap.put(outputData.getName(), outputData.getBagOfWordsOccurrenceMap());
        Platform.runLater(() -> this.observableFiles.add("*" + outputData.getName()));

        try {
            Map<String, Integer> bagOfWordsOccurrence = outputData.getBagOfWordsOccurrenceMap().get();

            System.out.println("FINISHED: nOfKeys: " + bagOfWordsOccurrence.size() + " for file: " + outputData.getName());
            Platform.runLater(() ->
                    this.observableFiles.set(this.observableFiles.indexOf("*" + outputData.getName()), outputData.getName()));
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

    public ObservableList<String> getObservableFiles() {
        return observableFiles;
    }
}
