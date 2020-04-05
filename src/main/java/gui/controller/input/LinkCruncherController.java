package gui.controller.input;

import components.ComponentManager;
import components.cruncher.CounterCruncher;
import components.input.FileInput;
import gui.model.CruncherModel;
import gui.model.InputModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

public class LinkCruncherController implements EventHandler<ActionEvent> {

    private InputModel inputModel;
    private ComboBox<CruncherModel> comboBox;

    public LinkCruncherController(InputModel inputModel, ComboBox<CruncherModel> comboBox) {
        this.inputModel = inputModel;
        this.comboBox = comboBox;
    }

    @Override
    public void handle(ActionEvent event) {
        ComponentManager componentManager = ComponentManager.getInstance();
        FileInput fileInput = componentManager.getInputs().get(inputModel.getName());
        CruncherModel cruncherModel = comboBox.getSelectionModel().getSelectedItem();
        CounterCruncher counterCruncher = componentManager.getCrunchers().get(cruncherModel.getName());

        componentManager.connectInputToCruncher(fileInput, counterCruncher);
        inputModel.getCrunchers().add(cruncherModel);
        Event.fireEvent(comboBox, new ActionEvent());
    }
}
