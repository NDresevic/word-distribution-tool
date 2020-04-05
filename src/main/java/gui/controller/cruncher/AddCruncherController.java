package gui.controller.cruncher;

import components.ComponentManager;
import gui.model.CruncherModel;
import gui.view.CruncherView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;

public class AddCruncherController implements EventHandler<ActionEvent> {

    private CruncherView cruncherView;

    public AddCruncherController(CruncherView cruncherView) {
        this.cruncherView = cruncherView;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        TextInputDialog textInputDialog = new TextInputDialog("1");
        textInputDialog.setHeaderText("Enter cruncher arity");
        textInputDialog.initModality(Modality.APPLICATION_MODAL);
        textInputDialog.showAndWait().ifPresent(response -> {
            int arity = Integer.parseInt(response);
            Label crunchingLabel = new Label("Crunching:");
            String name = ComponentManager.getInstance().addCruncher(arity, crunchingLabel);

            CruncherModel cruncherModel = new CruncherModel(name, arity);
            this.cruncherView.addCruncher(cruncherModel, crunchingLabel);
            this.cruncherView.getCrunchers().add(cruncherModel);
        });
    }
}
