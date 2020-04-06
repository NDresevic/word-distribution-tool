package components.output;

import javafx.collections.ObservableList;

import java.util.Map;

public interface CacheOutput extends Runnable {

    void addOutputDataToQueue(OutputData outputData);
    Map<String, Integer> getResultMapForFile(String name);
    ObservableList<String> getObservableFiles();
}
