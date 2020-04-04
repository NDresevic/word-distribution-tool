package gui.controller.input;

import components.ComponentManager;
import components.cruncher.CounterCruncher;
import components.input.FileInput;
import gui.model.CruncherModel;
import gui.model.InputModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;

public class UnlinkCruncherController implements EventHandler<ActionEvent> {

    private InputModel inputModel;
    private ListView<CruncherModel> crunchersListView;

    public UnlinkCruncherController(InputModel inputModel, ListView<CruncherModel> crunchersListView) {
        this.inputModel = inputModel;
        this.crunchersListView = crunchersListView;
    }

    @Override
    public void handle(ActionEvent event) {
        ComponentManager componentManager = ComponentManager.getInstance();
        FileInput fileInput = componentManager.getInputs().get(inputModel.getName());
        CruncherModel cruncherModel = crunchersListView.getSelectionModel().getSelectedItem();
        CounterCruncher counterCruncher = componentManager.getCrunchers().get(cruncherModel.getName());

        componentManager.disconnectInputToCruncher(fileInput, counterCruncher);
        inputModel.getCrunchers().remove(cruncherModel);
    }
}
