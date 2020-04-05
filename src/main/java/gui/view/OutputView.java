package gui.view;

import gui.controller.output.SingleResultController;
import gui.controller.output.SumResultController;
import gui.model.OutputModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;

public class OutputView extends VBox {

    private OutputModel outputModel;

    private ListView<String> filesListView;
    private Button singleResultButton;
    private Button sumResultButton;

    public OutputView() {
        super(5);
        initElements();
        addElements();
    }

    private void initElements() {
        this.filesListView = new ListView<>();
        this.singleResultButton = new Button("Single result");
        this.sumResultButton = new Button("Sum result");
        this.outputModel = new OutputModel();

        setPadding(new Insets(5));
    }

    private void addElements() {
        filesListView.setItems(outputModel.getFiles());
        filesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        singleResultButton.disableProperty().bind(filesListView.getSelectionModel().selectedItemProperty().isNull());
        sumResultButton.disableProperty().bind(filesListView.getSelectionModel().selectedItemProperty().isNull());

        singleResultButton.setOnAction(new SingleResultController());
        sumResultButton.setOnAction(new SumResultController());

        getChildren().add(filesListView);
        getChildren().add(singleResultButton);
        getChildren().add(sumResultButton);
    }

    public OutputModel getOutputModel() {
        return outputModel;
    }
}
