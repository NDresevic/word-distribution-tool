import components.ComponentManager;
import configuration.Configuration;
import gui.main.MainGui;
import javafx.application.Application;

import java.io.File;
import java.util.*;

public class AppCore {

    public static AppCore instance = null;

    private static final String ROOT_PATH = "/Users/ndresevic/RAF/8. semestar/KiDS/vezbe/word-distribution-tool/";

    private AppCore() { }

    public static void main(String[] args) {
        Configuration.loadConfiguration("config/app.properties");

        File B = new File(ROOT_PATH + "src/main/resources/data/disk1/B");
        File A = new File(ROOT_PATH + "src/main/resources/data/disk1/A");
        ArrayList<File> filesAB = new ArrayList<>();
        filesAB.add(A);
        filesAB.add(B);

//        File C = new File(ROOT_PATH + "src/main/resources/data/disk2/C");
//        File D = new File(ROOT_PATH + "src/main/resources/data/disk2/D");
//        ArrayList<File> filesCD = new ArrayList<>();
//        filesCD.add(C);
//        filesCD.add(D);

        File test = new File(ROOT_PATH + "src/main/resources/data/disk1/test");
        ArrayList<File> filesTest = new ArrayList<>();
        filesTest.add(test);

        Map<String, List<File>> discMap = new HashMap<>();
//        discMap.put(ROOT_PATH + "src/main/resources/data/disk1", filesTest);
        discMap.put(ROOT_PATH + "src/main/resources/data/disk1", filesAB);
//        discMap.put(ROOT_PATH + "src/main/resources/data/disk2", filesCD);

        ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.initializeComponents(discMap);

        new Thread(() -> Application.launch(MainGui.class)).start();

//        Thread t = new Thread(() -> {
//            MainGui gui = new MainGui();
//            gui.launch(args);
//        });
//        t.run();

//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        // Get current size of heap in bytes
//        long heapSize = Runtime.getRuntime().totalMemory();
//
//        // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
//        long heapMaxSize = Runtime.getRuntime().maxMemory();
//
//        // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
//        long heapFreeSize = Runtime.getRuntime().freeMemory();
//        System.out.println(heapSize);
//        System.out.println(heapMaxSize);
//        System.out.println(heapFreeSize);
    }

    public static AppCore getInstance() {
        if (instance == null) {
            instance = new AppCore();
        }
        return instance;
    }
}
