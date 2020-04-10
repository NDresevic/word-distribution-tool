package gui.main;

import gui.view.MainStage;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainGui extends Application {

    @Override
    public void start(Stage stage) {
        MainStage mainStage = MainStage.getInstance();

        Scene scene = new Scene(mainStage.getBorderPane(), 1250, 850);

        mainStage.setTitle("Word distribution tool");
        mainStage.setScene(scene);
        mainStage.show();
        centerStage(mainStage);
    }

    private static void centerStage(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
