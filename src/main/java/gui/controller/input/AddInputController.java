package gui.controller.input;

import components.ComponentManager;
import gui.model.InputModel;
import gui.view.InputView;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

public class AddInputController implements EventHandler<ActionEvent> {

    private InputView inputView;
    private ComboBox<String> discComboBox;

    public AddInputController(InputView inputView, ComboBox<String> discComboBox) {
        this.inputView = inputView;
        this.discComboBox = discComboBox;
    }

    @Override
    public void handle(ActionEvent event) {
        String disc = discComboBox.getSelectionModel().getSelectedItem();
        // add to back and return File Input name
        String name = ComponentManager.getInstance().addInput(disc);

        InputModel inputModel = new InputModel(name, disc);
        inputView.getInputs().add(inputModel);
        inputView.addFileInput(inputModel);
        Event.fireEvent(inputView.getDiscComboBox(), new ActionEvent());
    }
}
