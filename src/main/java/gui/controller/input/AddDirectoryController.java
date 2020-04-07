package gui.controller.input;

import components.ComponentManager;
import components.input.FileInput;
import gui.model.InputModel;
import gui.view.MainStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class AddDirectoryController implements EventHandler<ActionEvent> {

    private InputModel inputModel;

    public AddDirectoryController(InputModel inputModel) {
        this.inputModel = inputModel;
    }

    @Override
    public void handle(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select directory to scan");
        File directory = directoryChooser.showDialog(MainStage.getInstance());

        if (directory == null) {
            return;
        }
        inputModel.getDirectories().add(directory);

        FileInput fileInput = ComponentManager.getInstance().getInputs().get(inputModel.getName());
        fileInput.addDirectory(directory);
    }
}
