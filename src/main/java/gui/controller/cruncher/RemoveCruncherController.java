package gui.controller.cruncher;

import com.sun.javafx.scene.control.InputField;
import com.sun.tools.javac.Main;
import components.ComponentManager;
import gui.model.CruncherModel;
import gui.model.InputModel;
import gui.view.CruncherView;
import gui.view.MainStage;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;

import java.util.List;

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
        ComponentManager.getInstance().removeCruncher(cruncherModel.getName());

        List<InputModel> inputs = MainStage.getInstance().getInputView().getInputs();
        for (InputModel inputModel: inputs) {
            inputModel.getCrunchers().remove(cruncherModel);
        }
//        Event.fireEvent(MainStage.getInstance().getInputView().get, new ActionEvent());

        cruncherView.getCrunchers().remove(cruncherModel);
        cruncherView.getChildren().remove(vBox);
    }
}
