package components.output;

import java.util.Map;

public interface CacheOutput extends Runnable {

    void addOutputDataToQueue(OutputData outputData);
    Map<String, Integer> getAggregatedResults();
}
