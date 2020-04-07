package components.input;

import components.ComponentManager;
import components.cruncher.CounterCruncher;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class FileInputImplementation implements FileInput {

    private volatile boolean exit = false;
    private volatile boolean started = false;
    private volatile boolean running = false;
    private Label statusLabel;

    private final String disc;
    private List<File> directories;
    private final ExecutorService threadPool;

    private ConcurrentMap<File, Long> lastModifiedMap;

    private List<CounterCruncher> crunchers;

    public FileInputImplementation(String disc, ExecutorService threadPool, Label statusLabel) {
        this.disc = disc;
        this.threadPool = threadPool;
        this.statusLabel = statusLabel;
        this.directories = new ArrayList<>();
        this.lastModifiedMap = new ConcurrentHashMap<>();
        this.crunchers = new CopyOnWriteArrayList<>();
    }

    @Override
    public void run() {
        while (!exit) {
            if (running) {
                this.scanDirectories();
                this.sleep(ComponentManager.FILE_INPUT_SLEEP_TIME);
            } else {
                this.sleep();
            }
        }
        System.out.println("FileInput is stopped....");
    }

    /**
     * Komponenta treba da radi tako što rekurzivno obilazi direktorijume koji su joj navedeni kao input.
     * Kada u nekom od njih pronađe bilo koju .txt datoteku, ona se dodaje na spisak za čitanje.
     */
    private synchronized void scanDirectories() {
        for (File directory: this.directories) {
            this.insertFiles(directory);
        }
        System.out.println(this.lastModifiedMap);
    }

    private void insertFiles(File directory) {
        List<File> files = Collections.synchronizedList(new ArrayList<>());
        files = this.getFilesFromDirectory(directory, files);
        for (File file: files) {
            Long lastModified = this.lastModifiedMap.get(file);
            if (lastModified == null || !lastModified.equals(file.lastModified())) {
                this.lastModifiedMap.put(file, file.lastModified());
                this.threadPool.execute(() -> readFile(file));
            }
        }
    }

    private List<File> getFilesFromDirectory(File directory, List<File> files) {
        for (File fileEntry: directory.listFiles()) {
            if (fileEntry.isDirectory()) {
                files = getFilesFromDirectory(fileEntry, files);
            } else {
                if (fileEntry.getName().toLowerCase().endsWith(".txt")) {
                    files.add(fileEntry);
                }
//                if (Files.getFileExtension(fileEntry.getName()).equals("txt")) {
//                    files.add(fileEntry);
//                }
            }
        }
        return files;
    }

    private synchronized void sleep(int time) {
        try {
            wait(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sleep() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void readFile(File file) {
        synchronized (this.disc) {
            try {
                Platform.runLater(() -> statusLabel.setText("Reading: " + file.getName()));

                // novo citanje - manje memorije zauzima (3.91GB -> 3.76GB)
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String content = new String(data, StandardCharsets.US_ASCII);
                //
//                String content = Files.asCharSource(file, Charsets.US_ASCII).read();
                InputData inputData = new InputData(file.getName(), content);
                this.sendInputDataToCrunchers(inputData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> statusLabel.setText("Idle"));
        }
    }

    private void sendInputDataToCrunchers(InputData inputData) {
        for (CounterCruncher cruncher : this.crunchers) {
            cruncher.addInputDataToQueue(inputData);
        }
    }

    @Override
    public synchronized void startRunning() {
        if (!started) {
            started = true;
            threadPool.execute(this);
        }
        running = true;
        notify();
    }

    @Override
    public synchronized void pauseRunning() {
        running = false;
        // because thread might already sleep
        notify();
    }

    @Override
    public void addDirectory(File directory) {
        this.directories.add(directory);
    }

    /**
     * Ako se neki direktorijum izbaci iz spiska za skeniranje, sve datoteke unutar tog direktorijuma se brišu iz
     * “last modified” spiska. Ako se isti direktorijum ponovo doda, sve datoteke unutar tog direktorijuma će biti
     * ponovo pročitane.
     * @param directory
     */
    @Override
    public void removeDirectory(File directory) {
        List<File> files = Collections.synchronizedList(new ArrayList<>());
        files = this.getFilesFromDirectory(directory, files);
        for (File file: files) {
            this.lastModifiedMap.remove(file);
        }
        this.directories.remove(directory);
    }

    @Override
    public void addCruncher(CounterCruncher cruncher) {
        this.crunchers.add(cruncher);
    }

    @Override
    public void removeCruncher(CounterCruncher cruncher) {
        this.crunchers.remove(cruncher);
    }

    @Override
    public List<CounterCruncher> getCrunchers() {
        return this.crunchers;
    }

    @Override
    public void remove() {
        this.exit = true;
    }

    @Override
    public void stop() {
        this.exit = true;
        InputData poison = new InputData(true);
        this.sendInputDataToCrunchers(poison);
    }
}
