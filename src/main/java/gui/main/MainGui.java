package gui.main;

import gui.view.MainStage;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainGui extends Application {

    private MainStage mainStage;

    @Override
    public void start(Stage stage) {
        this.mainStage = MainStage.getInstance();

        Scene scene = new Scene(this.mainStage.getBorderPane(), 1150, 750);

        this.mainStage.setTitle("Word distribution tool");
        this.mainStage.setScene(scene);
        this.mainStage.show();
        centerStage(this.mainStage);
    }

    public static void centerStage(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
