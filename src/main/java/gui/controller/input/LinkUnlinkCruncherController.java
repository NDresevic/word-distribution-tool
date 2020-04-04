package gui.controller.input;

import components.ComponentManager;
import components.cruncher.CounterCruncher;
import components.input.FileInput;
import gui.model.InputModel;
import gui.view.InputView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

public class LinkUnlinkCruncherController implements EventHandler<ActionEvent> {

    private InputView inputView;
    private String action;
    private InputModel inputModel;
    private ComboBox<String> comboBox;

    public LinkUnlinkCruncherController(String action, InputModel inputModel, ComboBox<String> comboBox) {
        this.action = action;
        this.inputModel = inputModel;
        this.comboBox = comboBox;
        this.inputView = InputView.getInstance();
    }

    @Override
    public void handle(ActionEvent event) {
        ComponentManager componentManager = ComponentManager.getInstance();
        FileInput fileInput = componentManager.getInputs().get(inputModel.getName());
        String cruncherName = comboBox.getSelectionModel().getSelectedItem();
        CounterCruncher counterCruncher = componentManager.getCrunchers().get(cruncherName);

        if (action.equals("link")) {
            componentManager.connectInputToCruncher(fileInput, counterCruncher);
            inputModel.getCrunchers().add(cruncherName);
        } else {
            componentManager.disconnectInputToCruncher(fileInput, counterCruncher);
            inputModel.getCrunchers().remove(cruncherName);
        }
    }
}
