package components.output;

import gui.model.FileModel;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;

import java.util.List;
import java.util.Map;

public interface CacheOutput extends Runnable {

    void addOutputDataToQueue(OutputData outputData);
    ObservableList<FileModel> getObservableFiles();

    Map<String, Integer> poll(String fileName);
    Map<String, Integer> take(String fileName);
    void aggregateResults(String fileName, List<FileModel> files, ProgressBar progressBar);
}
