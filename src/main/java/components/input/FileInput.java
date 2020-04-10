package components.input;

import components.cruncher.CounterCruncher;

import java.io.File;
import java.util.List;

public interface FileInput extends Runnable {

    /**
     * Starts scanning the directories.
     */
    void startRunning();

    /**
     * Pause scanning of directories.
     */
    void pauseRunning();

    void addDirectory(File directory);
    void removeDirectory(File directory);

    void addCruncher(CounterCruncher cruncher);
    void removeCruncher(CounterCruncher cruncher);

    /**
     * List of crunchers this FileInput sends data to.
     */
    List<CounterCruncher> getCrunchers();

    /**
     * Gets called from GUI, stops its thread.
     */
    void remove();

    /**
     * Stops FileInput and sends poison pills to its connected CounterCrunchers.
     */
    void stop();
}
