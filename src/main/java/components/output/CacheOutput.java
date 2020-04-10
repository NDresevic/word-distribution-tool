package components.output;

import gui.model.FileModel;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;

import java.util.List;
import java.util.Map;

public interface CacheOutput extends Runnable {

    void addOutputDataToQueue(OutputData outputData);
    ObservableList<FileModel> getObservableFiles();

    /**
     * Unblocking operation of reading that returns null if the result is not ready yet.
     * @return [bag of words -> number of occurrence]
     */
    Map<String, Integer> poll(String fileName);

    /**
     * Blocking operation that returns the result.
     * @return [bag of words -> number of occurrence]
     */
    Map<String, Integer> take(String fileName);

    /**
     * Sums bag of words occurrence results of all files into a new file.
     * @param fileName - aggregated file
     * @param files - list of files to be aggregated
     * @param progressBar - current progress shown in GUI
     */
    void aggregateResults(String fileName, List<FileModel> files, ProgressBar progressBar);
}
