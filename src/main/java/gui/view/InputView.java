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
    private Button addFileInput;

    public InputView(ObservableList<CruncherModel> allCrunchers) {
        super(5);
        this.allCrunchers = allCrunchers;
        initElements();
        addElements();
    }

    private void initElements() {
        this.inputs = new ArrayList<>();
        this.discComboBox = new ComboBox<>();
        this.discComboBox.getItems().addAll(Configuration.getParameter("disks").split(";"));
        this.discComboBox.getSelectionModel().selectFirst();
        this.addFileInput = new Button("Add File Input");

        setPadding(new Insets(5));
    }

    private void addElements() {
        getChildren().add(new Label("File inputs"));
        getChildren().add(discComboBox);
        getChildren().add(addFileInput);

        // if selected disc was already used to create FileInput then "Add File Input" button is disabled
        discComboBox.setOnAction(e -> {
            boolean valid = true;
            for (InputModel inputModel: this.inputs) {
                if (inputModel.getDisc().equals(discComboBox.getValue())) {
                    valid = false;
                    break;
                }
            }
            addFileInput.setDisable(!valid);
        });

        addFileInput.setOnAction(new AddInputController(this, this.discComboBox));
    }

    public void addFileInput(InputModel inputModel, Label statusLabel) {
        Button linkCruncherButton = new Button("Link cruncher");
        Button unlinkCruncherButton = new Button("Unlink cruncher");
        Button addDirButton = new Button("Add dir");
        Button removeDirButton = new Button("Remove dir");
        Button startPauseButton = new Button("Start");
        Button removeInputButton = new Button("Remove file input");

        ObservableList<CruncherModel> observableCrunchers = inputModel.getCrunchers();
        ListView<CruncherModel> crunchersListView = new ListView<>();
        crunchersListView.setItems(observableCrunchers);
        crunchersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ListView<File> directoriesListView = new ListView<>();
        directoriesListView.setItems(inputModel.getDirectories());
        directoriesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ComboBox<CruncherModel> crunchersComboBox  = new ComboBox<>(this.allCrunchers);
        crunchersComboBox.getSelectionModel().selectFirst();
        // if selected CruncherModel is already linked to InputModel then "Link Cruncher" button is disabled
        crunchersComboBox.setOnAction(e -> {
            if (crunchersComboBox.getItems().isEmpty() ||
                    observableCrunchers.contains(crunchersComboBox.getValue())) {
                linkCruncherButton.setDisable(true);
            } else {
                linkCruncherButton.setDisable(false);
            }
        });

        allCrunchers.addListener((ListChangeListener<CruncherModel>) change
                -> crunchersComboBox.getSelectionModel().selectFirst());
        // TODO: fix bug - kad dodas prvo crunchera link je disabled
        linkCruncherButton.setDisable(true);
        unlinkCruncherButton.disableProperty().bind(crunchersListView.getSelectionModel().selectedItemProperty().isNull());
        removeDirButton.disableProperty().bind(directoriesListView.getSelectionModel().selectedItemProperty().isNull());

        VBox linkUnlinkBox = new VBox(5);
        linkUnlinkBox.getChildren().addAll(linkCruncherButton, unlinkCruncherButton);
        HBox linkingBox = new HBox(5);
        linkingBox.getChildren().addAll(crunchersComboBox, linkUnlinkBox);

        HBox addRemoveDirBox = new HBox(5);
        addRemoveDirBox.getChildren().addAll(addDirButton, removeDirButton);

        HBox inputsBox = new HBox(5);
        inputsBox.getChildren().addAll(startPauseButton, removeInputButton);

        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(5));
//        vBox.setStyle("-fx-border-color: black;");

        vBox.getChildren().add(new Label(inputModel.getName() + ": " + inputModel.getDisc()));
        vBox.getChildren().add(new Label("Crunchers:" ));
        vBox.getChildren().add(crunchersListView);
        vBox.getChildren().add(linkingBox);
        vBox.getChildren().add(new Label("Directories:"));
        vBox.getChildren().add(directoriesListView);
        vBox.getChildren().add(addRemoveDirBox);
        vBox.getChildren().add(inputsBox);
        vBox.getChildren().add(statusLabel);

        removeInputButton.setOnAction(new RemoveInputController(this, inputModel, vBox));
        linkCruncherButton.setOnAction(new LinkCruncherController(inputModel, crunchersComboBox));
        unlinkCruncherButton.setOnAction(new UnlinkCruncherController(inputModel, crunchersListView, crunchersComboBox));
        addDirButton.setOnAction(new AddDirectoryController(inputModel));
        removeDirButton.setOnAction(new RemoveDirectoryController(inputModel, directoriesListView));
        startPauseButton.setOnAction(new StartPauseInputController(startPauseButton, inputModel, statusLabel));

        this.getChildren().addAll(vBox);
    }

    public ComboBox<String> getDiscComboBox() {
        return discComboBox;
    }

    public List<InputModel> getInputs() {
        return inputs;
    }
}
