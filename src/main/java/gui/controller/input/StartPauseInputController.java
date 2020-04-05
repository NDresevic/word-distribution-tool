package gui.controller.input;

import gui.model.InputModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class StartPauseInputController implements EventHandler<ActionEvent> {

    private InputModel inputModel;
    private Label statusLabel;

    public StartPauseInputController(InputModel inputModel, Label statusLabel) {
        this.inputModel = inputModel;
        this.statusLabel = statusLabel;
    }

    @Override
    public void handle(ActionEvent event) {

    }
}
