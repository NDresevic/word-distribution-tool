package gui.controller.input;

import components.ComponentManager;
import gui.model.DiscModel;
import gui.model.InputModel;
import gui.view.InputView;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class AddInputController implements EventHandler<ActionEvent> {

    private InputView inputView;
    private ComboBox<DiscModel> discComboBox;

    public AddInputController(InputView inputView, ComboBox<DiscModel> discComboBox) {
        this.inputView = inputView;
        this.discComboBox = discComboBox;
    }

    @Override
    public void handle(ActionEvent event) {
        Label statusLabel = new Label("Idle");
        DiscModel disc = discComboBox.getSelectionModel().getSelectedItem();
        // add to backend and return File Input name
        String name = ComponentManager.getInstance().addInput(disc.getPath(), statusLabel);

        InputModel inputModel = new InputModel(name, disc);
        inputView.getInputs().add(inputModel);
        inputView.addFileInput(inputModel, statusLabel);
        Event.fireEvent(inputView.getDiscComboBox(), new ActionEvent());
    }
}
