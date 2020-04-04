package gui.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class InputView extends VBox {

    private ComboBox<String> discComboBox;
    private Button addFileInput;

    public InputView() {
        initElements();
        addElements();
    }

    private void initElements() {
        this.discComboBox = new ComboBox<>();
        this.discComboBox.getItems().addAll("Rastuce", "Opadajuce");
//        this.discComboBox.getSelectionModel().selectFirst();
        this.addFileInput = new Button("Add File Input");

        setSpacing(10);
        setPadding(new Insets(10));
    }

    private void addElements() {
        getChildren().add(new Label("File inputs"));
        getChildren().add(discComboBox);
        getChildren().add(addFileInput);
//        getChildren().add(sumResultButton);
    }
}
