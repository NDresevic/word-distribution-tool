package components;

import components.cruncher.CounterCruncher;
import components.cruncher.CounterCruncherImplementation;
import components.input.FileInput;
import components.input.FileInputImplementation;
import components.output.CacheOutput;
import components.output.CacheOutputImplementation;
import configuration.Configuration;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ComponentManager {

    public static ComponentManager instance = null;

    public static int FILE_INPUT_SLEEP_TIME;
    public static int COUNTER_DATA_LIMIT;
    public static int SORT_PROGRESS_LIMIT;

    private static int CRUNCHER_COUNT = 0;
    private static int INPUT_COUNT = 0;

    private final ExecutorService inputThreadPool;
    private final ForkJoinPool cruncherThreadPool;
    private final ExecutorService outputThreadPool;

    private Map<String, CounterCruncher> crunchers;
    private Map<String, FileInput> inputs;

    private ComponentManager() {
        FILE_INPUT_SLEEP_TIME = Integer.parseInt(Configuration.getParameter("file_input_sleep_time"));
        COUNTER_DATA_LIMIT = Integer.parseInt(Configuration.getParameter("counter_data_limit"));
        SORT_PROGRESS_LIMIT = Integer.parseInt(Configuration.getParameter("sort_progress_limit"));

        this.inputThreadPool = Executors.newCachedThreadPool();
        this.cruncherThreadPool = ForkJoinPool.commonPool();
        this.outputThreadPool = Executors.newCachedThreadPool();

        this.crunchers = new HashMap<>();
        this.inputs = new HashMap<>();
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

    public String addInput(String disc) {
        FileInput fileInput = new FileInputImplementation(disc, this.inputThreadPool);
        String name = "File Input " + INPUT_COUNT;
        this.inputs.put(name, fileInput);
        INPUT_COUNT++;
        return name;
    }

    public void removeInput(String name) {
        FileInput fileInput = this.inputs.get(name);
        fileInput.stop();
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

    public Set<String> getAllCrunchers() {
        return this.crunchers.keySet();
    }

    public Map<String, CounterCruncher> getCrunchers() {
        return crunchers;
    }

    public Map<String, FileInput> getInputs() {
        return inputs;
    }
}
