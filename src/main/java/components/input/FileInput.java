package components.input;

import components.cruncher.CounterCruncher;

import java.io.File;

public interface FileInput extends Runnable {

    void startScan();
    void pauseScan();

    void addDirectory(File directory);
    void removeDirectory(File directory);

    void addCruncher(CounterCruncher cruncher);
    void removeCruncher(CounterCruncher cruncher);

    void stop();
}
