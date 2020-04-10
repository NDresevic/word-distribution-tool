import configuration.Configuration;
import gui.main.MainGui;
import javafx.application.Application;

public class AppCore {

    private AppCore() { }

    public static void main(String[] args) {
        Configuration.readConfiguration("config/app.properties");

        new Thread(() -> Application.launch(MainGui.class)).start();
    }
}
