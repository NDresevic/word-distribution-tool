package gui.controller.input;

import components.ComponentManager;
import gui.model.InputModel;
import gui.view.InputView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;

public class RemoveInputController implements EventHandler<ActionEvent> {

    private InputView inputView;
    private InputModel inputModel;
    private VBox vBox;

    public RemoveInputController(InputModel inputModel, VBox vBox) {
        this.inputModel = inputModel;
        this.vBox = vBox;
        this.inputView = InputView.getInstance();
    }

    @Override
    public void handle(ActionEvent event) {
        ComponentManager.getInstance().removeInput(inputModel.getName());

        inputView.getInputs().remove(inputModel);
        inputView.getChildren().remove(vBox);
    }
}
