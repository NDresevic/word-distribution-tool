import components.ComponentManager;
import configuration.Configuration;
import gui.main.MainGui;
import javafx.application.Application;

public class AppCore {

    private AppCore() { }

    public static void main(String[] args) {
        Configuration.loadConfiguration("config/app.properties");
//        ComponentManager.getInstance();

        new Thread(() -> Application.launch(MainGui.class)).start();

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
}
