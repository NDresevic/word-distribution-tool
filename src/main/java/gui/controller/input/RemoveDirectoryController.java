package gui.controller.input;

import components.ComponentManager;
import components.input.FileInput;
import gui.model.InputModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;

import java.io.File;

public class RemoveDirectoryController implements EventHandler<ActionEvent> {

    private InputModel inputModel;
    private ListView<File> directoriesListView;

    public RemoveDirectoryController(InputModel inputModel, ListView<File> directoriesListView) {
        this.inputModel = inputModel;
        this.directoriesListView = directoriesListView;
    }

    @Override
    public void handle(ActionEvent event) {
        File directory = directoriesListView.getSelectionModel().getSelectedItem();
        inputModel.getDirectories().remove(directory);

        FileInput fileInput = ComponentManager.getInstance().getInputs().get(inputModel.getName());
        fileInput.removeDirectory(directory);
    }
}
