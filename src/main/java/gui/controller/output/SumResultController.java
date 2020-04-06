package gui.controller.output;

import components.ComponentManager;
import gui.model.CruncherModel;
import gui.view.OutputView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;

import java.io.File;
import java.util.List;

public class SumResultController implements EventHandler<ActionEvent> {

    private OutputView outputView;

    public SumResultController(OutputView outputView) {
        this.outputView = outputView;
    }

    @Override
    public void handle(ActionEvent event) {
        TextInputDialog textInputDialog = new TextInputDialog("sum");
        textInputDialog.setHeaderText("Enter unique sum name");
        textInputDialog.initModality(Modality.APPLICATION_MODAL);
        textInputDialog.showAndWait().ifPresent(response -> {
            // if name is not unique error is reported
            if (this.outputView.getOutputModel().getFiles().contains(response)) {
                Alert newAlert = new Alert(Alert.AlertType.ERROR, "Sum name is not unique.", ButtonType.OK);
                newAlert.showAndWait();
                return;
            }

            List<String> sumFiles = this.outputView.getFilesListView().getSelectionModel().getSelectedItems();

            // TODO: do the sum


            this.outputView.getOutputModel().getFiles().add(response);
        });

    }
}
