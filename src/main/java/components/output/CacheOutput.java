package components.output;

import javafx.collections.ObservableList;

import java.util.List;
import java.util.Map;

public interface CacheOutput extends Runnable {

    void addOutputDataToQueue(OutputData outputData);
    ObservableList<String> getObservableFiles();

    Map<String, Integer> poll(String fileName);
    Map<String, Integer> take(String fileName);
    void aggregateResults(String fileName, List<String> files);
}
