package components;

import components.cruncher.CounterCruncher;
import components.cruncher.CounterCruncherImplementation;
import components.input.FileInput;
import components.input.FileInputImplementation;
import components.output.CacheOutput;
import components.output.CacheOutputImplementation;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ComponentManager {

    private final ExecutorService inputThreadPool;
    private final ForkJoinPool cruncherThreadPool;
    private final ExecutorService outputThreadPool;

    public static final int FILE_INPUT_SLEEP_TIME = 5000;
    public static int COUNTER_DATA_LIMIT = 10485760;

    public static int CRUNCHER_COUNT = 0;

    public static ComponentManager instance = null;

    private Map<String, CounterCruncher> crunchers;

    private ComponentManager() {
        this.inputThreadPool = Executors.newCachedThreadPool();
        this.cruncherThreadPool = ForkJoinPool.commonPool();
        this.outputThreadPool = Executors.newCachedThreadPool();

        this.crunchers = new HashMap<>();
    }

    public void initializeComponents(Map<String, List<File>> discFilesMap) {
        CacheOutput cacheOutput = new CacheOutputImplementation(this.outputThreadPool);
        for (Map.Entry<String, List<File>> entry : discFilesMap.entrySet()) {
            List<File> directories = new CopyOnWriteArrayList<>(entry.getValue());
            FileInputImplementation fileInput = new FileInputImplementation(entry.getKey(), directories, this.inputThreadPool);

            CounterCruncherImplementation counterCruncher = new CounterCruncherImplementation(1, this.cruncherThreadPool);
            this.connectInputToCruncher(fileInput, counterCruncher);
            this.connectCruncherToOutput(counterCruncher, cacheOutput);
            this.inputThreadPool.execute(fileInput);
        }

        System.out.println("AGGREGATED SIZE: " + cacheOutput.getAggregatedResults().size());
    }

    public String addCruncher(int arity) {
        CounterCruncher counterCruncher = new CounterCruncherImplementation(arity, this.cruncherThreadPool);
        String name = "Counter " + CRUNCHER_COUNT;
        this.crunchers.put(name, counterCruncher);
        CRUNCHER_COUNT++;
        return name;
    }

    public void connectInputToCruncher(FileInput fileInput, CounterCruncher counterCruncher) {
        fileInput.addCruncher(counterCruncher);
    }

    public void disconnectInputToCruncher(FileInput fileInput, CounterCruncher counterCruncher) {
        fileInput.removeCruncher(counterCruncher);
    }

    public void connectCruncherToOutput(CounterCruncher counterCruncher, CacheOutput cacheOutput) {
        counterCruncher.addOutput(cacheOutput);
    }

    public static ComponentManager getInstance() {
        if (instance == null) {
            instance = new ComponentManager();
        }
        return instance;
    }
}
