package gui.view;

import components.ComponentManager;
import gui.controller.output.SingleResultController;
import gui.controller.output.SumResultController;
import gui.model.FileModel;
import gui.model.OutputModel;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class OutputView extends VBox {

    private OutputModel outputModel;

    private ListView<FileModel> filesListView;
    private Button singleResultButton;
    private Button sumResultButton;
    private ProgressBar sortingProgressBar;

    public OutputView() {
        super(5);
        initElements();
        addElements();
    }

    private void initElements() {
        this.filesListView = new ListView<>();
        this.singleResultButton = new Button("Single result");
        this.sumResultButton = new Button("Sum result");
        this.sortingProgressBar = new ProgressBar();
        this.outputModel = new OutputModel(ComponentManager.getInstance().getOutput().getObservableFiles());

        singleResultButton.setDisable(true);
        sumResultButton.setDisable(true);

        setPadding(new Insets(5));
    }

    private void addElements() {
        filesListView.setItems(outputModel.getFiles());
        filesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        filesListView.setOnMouseClicked(mouseEvent -> {
            int count = filesListView.getSelectionModel().getSelectedItems().size();
            if (count == 1) {
                singleResultButton.setDisable(false);
                sumResultButton.setDisable(true);
            } else if (count > 1) {
                singleResultButton.setDisable(true);
                sumResultButton.setDisable(false);
            } else {
                singleResultButton.setDisable(true);
                sumResultButton.setDisable(true);
            }
        });

        singleResultButton.setOnAction(new SingleResultController(filesListView, this));
        sumResultButton.setOnAction(new SumResultController(this));

        getChildren().add(filesListView);
        getChildren().add(singleResultButton);
        getChildren().add(sumResultButton);
    }

    public void updateProgressBar(double value) {
        if (!this.getChildren().contains(sortingProgressBar)) {
            this.getChildren().add(sortingProgressBar);
        }
        sortingProgressBar.setProgress(sortingProgressBar.getProgress() + value);
    }

    public void removeProgressBar(ProgressBar progressBar) {
        getChildren().remove(progressBar);
    }

    public void hideAndResetProgressBar() {
        getChildren().remove(sortingProgressBar);
        sortingProgressBar.setProgress(0);
    }

    public OutputModel getOutputModel() {
        return outputModel;
    }

    public ListView<FileModel> getFilesListView() {
        return filesListView;
    }
}
