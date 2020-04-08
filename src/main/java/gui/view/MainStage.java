package gui.view;

import components.ComponentManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainStage extends Stage {

    private static MainStage instance = null;

    private InputView inputView;
    private CruncherView cruncherView;
    private GraphDistributionView graphDistributionView;
    private OutputView outputView;

    private BorderPane borderPane;
    private Stage popupStage;
    private Alert outOfMemoryAlert;

    private MainStage() {
        this.cruncherView = new CruncherView();
        this.inputView = new InputView(this.cruncherView.getCrunchers());
        this.graphDistributionView = new GraphDistributionView();
        this.outputView = new OutputView();
        this.borderPane = new BorderPane();
        this.popupStage = new Stage();
        this.outOfMemoryAlert = new Alert(Alert.AlertType.INFORMATION, "Out of memory, aborting.", ButtonType.OK);

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

    public void handleOutOfMemoryError() {
        this.outOfMemoryAlert.showAndWait();
        System.exit(0);
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
