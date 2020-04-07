package gui.view;

import components.ComponentManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainStage extends Stage {

    private static MainStage instance = null;

    private InputView inputView;
    private CruncherView cruncherView;
    private GraphDistributionView graphDistributionView;
    private OutputView outputView;

    private BorderPane borderPane;
    private Stage popupStage;

    private MainStage() {
        this.cruncherView = new CruncherView();
        this.inputView = new InputView(this.cruncherView.getCrunchers());
        this.graphDistributionView = new GraphDistributionView();
        this.outputView = new OutputView();
        this.borderPane = new BorderPane();
        this.popupStage = new Stage();

        StackPane stackPane = new StackPane(new Label("Stopping everything..."));
        Scene popupScene = new Scene(stackPane, 300, 70);
        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);

        this.addElementsToView();
    }

    private void addElementsToView() {
        borderPane.setPadding(new Insets(5, 5, 5, 5));

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(this.inputView, this.cruncherView);

        borderPane.setLeft(hBox);
        borderPane.setCenter(this.graphDistributionView);
        borderPane.setRight(this.outputView);

        this.setOnCloseRequest(windowEvent -> {
            new Thread(() -> ComponentManager.getInstance().shutdownApp(popupStage)).start();
            this.popupStage.showAndWait();
        });
    }

    public static MainStage getInstance() {
        if (instance == null) {
            synchronized (MainStage.class) {
                if (instance == null) {
                    instance = new MainStage();
                }
            }
        }
        return instance;
    }

    public InputView getInputView() {
        return inputView;
    }

    public CruncherView getCruncherView() {
        return cruncherView;
    }

    public GraphDistributionView getGraphDistributionView() {
        return graphDistributionView;
    }

    public OutputView getOutputView() {
        return outputView;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }
}
