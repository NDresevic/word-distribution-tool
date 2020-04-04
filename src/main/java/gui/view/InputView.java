package gui.view;

import configuration.Configuration;
import gui.controller.input.*;
import gui.model.CruncherModel;
import gui.model.InputModel;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InputView extends VBox {

    private List<InputModel> inputs;
    private ObservableList<CruncherModel> allCrunchers;

    private ComboBox<String> discComboBox;
    private ComboBox<CruncherModel> crunchersComboBox;
    private Button addFileInput;

    public InputView(ObservableList<CruncherModel> allCrunchers) {
        this.allCrunchers = allCrunchers;
        initElements();
        addElements();
    }

    private void initElements() {
        this.inputs = new ArrayList<>();
        this.discComboBox = new ComboBox<>();
        this.crunchersComboBox = new ComboBox<>(this.allCrunchers);
        this.addFileInput = new Button("Add File Input");

        this.discComboBox.getItems().addAll(Configuration.getParameter("disks").split(";"));
        this.discComboBox.getSelectionModel().selectFirst();

        setSpacing(10);
        setPadding(new Insets(10));
    }

    private void addElements() {
        getChildren().add(new Label("File inputs"));
        getChildren().add(discComboBox);
        getChildren().add(addFileInput);

        addFileInput.setOnAction(new AddInputController(this, this.discComboBox));
    }

    public void addFileInput(InputModel inputModel) {
        this.inputs.add(inputModel);

        Button linkCruncherButton = new Button("Link cruncher");
        Button unlinkCruncherButton = new Button("Unlink cruncher");
        Button addDirButton = new Button("Add dir");
        Button removeDirButton = new Button("Remove dir");
        Button startPauseButton = new Button("Start");
        Button removeInputButton = new Button("Remove file input");

        ObservableList<CruncherModel> observableCrunchers = inputModel.getCrunchers();
//        observableCrunchers.addListener(new ListChangeListener<CruncherModel>() {
//            @Override
//            public void onChanged(Change<? extends CruncherModel> change) {
//                if (observableCrunchers.isEmpty()) {
//                    linkCruncherButton.setDisable(true);
//                    unlinkCruncherButton.setDisable(true);
//                } else {
//                    linkCruncherButton.setDisable(false);
//                    unlinkCruncherButton.setDisable(false);
//                }
//            }
//        });


        ListView<CruncherModel> crunchersListView = new ListView<>();
        crunchersListView.setItems(observableCrunchers);
        crunchersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        unlinkCruncherButton.disableProperty().bind(crunchersListView.getSelectionModel().selectedItemProperty().isNull());


        ListView<File> directoriesListView = new ListView<>();
        directoriesListView.setItems(inputModel.getDirectories());
        directoriesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        HBox addRemoveDirBox = new HBox();
        addRemoveDirBox.setSpacing(10);
        addRemoveDirBox.setPadding(new Insets(10));
        addRemoveDirBox.getChildren().addAll(addDirButton, removeDirButton);

        HBox inputsBox = new HBox();
        inputsBox.setSpacing(10);
        inputsBox.setPadding(new Insets(10));
        inputsBox.getChildren().addAll(startPauseButton, removeInputButton);

        Label workingLabel = new Label("Idle");

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.setStyle("-fx-border-color: black;");

        vBox.getChildren().add(new Label(inputModel.getName() + ": " + inputModel.getDisc()));
        vBox.getChildren().add(new Label("Crunchers:" ));
        vBox.getChildren().add(crunchersListView);
        vBox.getChildren().add(this.crunchersComboBox);
        vBox.getChildren().add(linkCruncherButton);
        vBox.getChildren().add(unlinkCruncherButton);
        vBox.getChildren().add(new Label("Directories:"));
        vBox.getChildren().add(directoriesListView);
        vBox.getChildren().add(addRemoveDirBox);
        vBox.getChildren().add(inputsBox);
        vBox.getChildren().add(workingLabel);

        removeInputButton.setOnAction(new RemoveInputController(this, inputModel, vBox));
        linkCruncherButton.setOnAction(new LinkCruncherController(inputModel, this.crunchersComboBox));
        unlinkCruncherButton.setOnAction(new UnlinkCruncherController(inputModel, crunchersListView));
        addDirButton.setOnAction(new AddDirectoryController(inputModel));
        removeDirButton.setOnAction(new RemoveDirectoryController(inputModel, directoriesListView));

        this.getChildren().add(vBox);
    }

    public List<InputModel> getInputs() {
        return inputs;
    }
}
