package gui.view;

import gui.controller.cruncher.AddCruncherController;
import gui.controller.cruncher.RemoveCruncherController;
import gui.model.CruncherModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CruncherView extends VBox {

    private Button addCruncher;
    private ObservableList<CruncherModel> crunchers;

    public CruncherView() {
        super(5);
        initElements();
        addElements();
    }

    private void initElements() {
        this.addCruncher = new Button("Add Cruncher");
        this.crunchers = FXCollections.observableArrayList();

        setPadding(new Insets(5));
    }

    private void addElements() {
        getChildren().add(new Label("Crunchers"));
        getChildren().add(addCruncher);

        addCruncher.setOnAction(new AddCruncherController(this));
    }

    public void addCruncher(CruncherModel cruncher, Label crunchingLabel) {
        Button removeButton = new Button("Remove cruncher");

        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(5));
        vBox.setStyle("-fx-border-color: black;");

        vBox.getChildren().add(new Label("Name: " + cruncher.getName()));
        vBox.getChildren().add(new Label("Arity: " + cruncher.getArity()));
        vBox.getChildren().add(removeButton);
        vBox.getChildren().add(crunchingLabel);
        removeButton.setOnAction(new RemoveCruncherController(this, cruncher, vBox));
        if (cruncher.isCrunching()) {
            vBox.getChildren().add(new Label("Crunching: "));

            // TODO: ispisati listu fajlova koja se obradjuje
        }

        this.getChildren().add(vBox);
    }

    public ObservableList<CruncherModel> getCrunchers() {
        return crunchers;
    }
}
