package gui.controller.input;

import components.ComponentManager;
import gui.model.InputModel;
import gui.view.InputView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

public class AddInputController implements EventHandler<ActionEvent> {

    private InputView inputView;
    private ComboBox<String> discComboBox;

    public AddInputController(ComboBox<String> discComboBox) {
        this.discComboBox = discComboBox;
        this.inputView = InputView.getInstance();
    }

    @Override
    public void handle(ActionEvent event) {
        String disc = this.discComboBox.getSelectionModel().getSelectedItem();
        String name = ComponentManager.getInstance().addInput(disc);

        InputModel inputModel = new InputModel(name, disc);
        this.inputView.addFileInput(inputModel);
    }
}
