package gui.controller;

import gui.model.CruncherModel;
import gui.view.CruncherView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;

public class RemoveCruncherController implements EventHandler<ActionEvent> {

    private CruncherView cruncherView;
    private CruncherModel cruncherModel;
    private VBox vBox;

    public RemoveCruncherController(CruncherView cruncherView, CruncherModel cruncherModel, VBox vBox) {
        this.cruncherView = cruncherView;
        this.cruncherModel = cruncherModel;
        this.vBox = vBox;
    }

    @Override
    public void handle(ActionEvent event) {
        // TODO: razvezes sve input komponente od date output

        cruncherView.getCrunchers().remove(cruncherModel);
        cruncherView.getChildren().remove(vBox);
    }
}
