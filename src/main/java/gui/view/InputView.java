package gui.view;

import components.ComponentManager;
import configuration.Configuration;
import gui.controller.input.AddInputController;
import gui.controller.input.LinkUnlinkCruncherController;
import gui.controller.input.RemoveInputController;
import gui.model.CruncherModel;
import gui.model.InputModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InputView extends VBox {

    public static InputView instance;

    private List<InputModel> inputs;
    private List<String> allCrunchers;

    private ComboBox<String> discComboBox;
    private ComboBox<String> crunchersComboBox = new ComboBox<>();
    private Button addFileInput;

    private InputView() {
        initElements();
        addElements();
    }

    private void initElements() {
        this.inputs = new ArrayList<>();
        this.allCrunchers = new ArrayList<>();//FXCollections.observableArrayList(ComponentManager.getInstance().getAllCrunchers());
        this.discComboBox = new ComboBox<>();
        this.discComboBox.getItems().addAll(Configuration.getParameter("disks").split(";"));
        this.discComboBox.getSelectionModel().selectFirst();
        this.crunchersComboBox.getItems().addAll(this.allCrunchers);
        this.addFileInput = new Button("Add File Input");

        setSpacing(10);
        setPadding(new Insets(10));
    }

    private void addElements() {
        getChildren().add(new Label("File inputs"));
        getChildren().add(discComboBox);
        getChildren().add(addFileInput);

        addFileInput.setOnAction(new AddInputController(this.discComboBox));
    }

    public void addFileInput(InputModel inputModel) {
        this.inputs.add(inputModel);

        ListView<String> crunchersListView = new ListView<>();
        crunchersListView.setItems(inputModel.getCrunchers());
        ListView<File> directoriesListView = new ListView<>();
        directoriesListView.setItems(inputModel.getDirectories());

        Button linkCruncherButton = new Button("Link cruncher");
        Button unlinkCruncherButton = new Button("Unlink cruncher");
        Button addDirButton = new Button("Add dir");
        Button removeDirButton = new Button("Remove dir");
        Button startPauseButton = new Button("Start");
        Button removeInputButton = new Button("Remove file input");

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

        removeInputButton.setOnAction(new RemoveInputController(inputModel, vBox));
        linkCruncherButton.setOnAction(new LinkUnlinkCruncherController("link", inputModel, this.crunchersComboBox));
        unlinkCruncherButton.setOnAction(new LinkUnlinkCruncherController("unlink", inputModel, this.crunchersComboBox));

        this.getChildren().add(vBox);
    }

    public List<InputModel> getInputs() {
        return inputs;
    }

    public List<String> getAllCrunchers() {
        System.out.println("fff");
        return allCrunchers;
    }

    public static InputView getInstance() {
        if (instance == null) {
            instance = new InputView();
        }
        return instance;
    }
}
