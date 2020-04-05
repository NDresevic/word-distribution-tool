package gui.controller.input;

import components.ComponentManager;
import gui.model.InputModel;
import gui.view.InputView;
import gui.view.MainStage;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;

public class RemoveInputController implements EventHandler<ActionEvent> {

    private InputView inputView;
    private InputModel inputModel;
    private VBox vBox;

    public RemoveInputController(InputView inputView, InputModel inputModel, VBox vBox) {
        this.inputView = inputView;
        this.inputModel = inputModel;
        this.vBox = vBox;
    }

    @Override
    public void handle(ActionEvent event) {
        ComponentManager.getInstance().removeInput(inputModel.getName());

        inputView.getInputs().remove(inputModel);
        inputView.getChildren().remove(vBox);
        Event.fireEvent(inputView.getDiscComboBox(), new ActionEvent());
    }
}
