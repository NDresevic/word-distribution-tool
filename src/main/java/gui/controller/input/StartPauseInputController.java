package gui.controller.input;

import components.ComponentManager;
import components.input.FileInput;
import gui.model.InputModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StartPauseInputController implements EventHandler<ActionEvent> {

    private Button button;
    private InputModel inputModel;
    private Label statusLabel;

    public StartPauseInputController(Button button, InputModel inputModel, Label statusLabel) {
        this.button = button;
        this.inputModel = inputModel;
        this.statusLabel = statusLabel;
    }

    @Override
    public void handle(ActionEvent event) {
        FileInput fileInput = ComponentManager.getInstance().getFileInputForName(inputModel.getName());
        if (button.getText().equals("Start")) {
            fileInput.startRunning();
            button.setText("Pause");
        } else {
            fileInput.pauseRunning();
            button.setText("Start");
        }
    }
}
