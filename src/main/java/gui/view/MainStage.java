package gui.view;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainStage extends Stage {

    private static MainStage instance = null;

    private InputView inputView;
    private CruncherView cruncherView;
    private GraphDistributionView graphDistributionView;
    private OutputView outputView;

    private BorderPane borderPane;

    private MainStage() {
        this.cruncherView = new CruncherView();
        this.inputView = new InputView(this.cruncherView.getCrunchers());
        this.graphDistributionView = new GraphDistributionView();
        this.outputView = new OutputView();
        this.borderPane = new BorderPane();

        this.addElementsToView();
    }

    private void addElementsToView() {
        borderPane.setPadding(new Insets(30, 10, 20, 30));

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10));
        hBox.getChildren().addAll(this.inputView, this.cruncherView);

        borderPane.setLeft(hBox);
        borderPane.setCenter(this.graphDistributionView);
        borderPane.setRight(this.outputView);
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
