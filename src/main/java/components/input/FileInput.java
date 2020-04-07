package components.input;

import components.cruncher.CounterCruncher;

import java.io.File;
import java.util.List;

public interface FileInput extends Runnable {

    void startRunning();
    void pauseRunning();

    void addDirectory(File directory);
    void removeDirectory(File directory);

    void addCruncher(CounterCruncher cruncher);
    void removeCruncher(CounterCruncher cruncher);
    List<CounterCruncher> getCrunchers();

    void remove();
    void stop();
}
