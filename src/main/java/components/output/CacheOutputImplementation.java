package components.output;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class CacheOutputImplementation implements CacheOutput{

    private final ExecutorService threadPool;

    private BlockingQueue<OutputData> outputQueue;
    // [naziv rezultata -> brojevi pojavljivanja svih vreća te arnosti u toj datoteci]
    private Map<String, Map<String, Integer>> resultMap;

    private Map<String, Integer> aggregatedResultsMap;

    public CacheOutputImplementation(ExecutorService threadPool) {
        this.threadPool = threadPool;
        this.outputQueue = new LinkedBlockingQueue<>();
        this.resultMap = new HashMap<>();
        this.aggregatedResultsMap = new ConcurrentHashMap<>();

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
            OutputData outputData = outputQueue.take();
            outputData.setName(outputData.getName().substring(1));
//            System.out.println("FINISHED: " + outputData.getName());
            Map<String, Integer> bagOfWordsOccurrence = outputData.getBagOfWordsOccurrenceMap().get();
            System.out.println("FINISHED: nOfKeys: " + bagOfWordsOccurrence.size() + " for file: " + outputData.getName());
            this.resultMap.put(outputData.getName(), bagOfWordsOccurrence);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOutputDataToQueue(OutputData outputData) {
        this.outputQueue.add(outputData);
    }

    @Override
    public Map<String, Integer> getAggregatedResults() {
//        this.threadPool.execute(() -> this.aggregateResults());
        this.aggregateResults();
        return this.aggregatedResultsMap;
    }
}
