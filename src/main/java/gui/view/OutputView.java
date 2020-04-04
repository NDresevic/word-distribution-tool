package gui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class OutputView extends VBox {

    private ListView<String> filesListView;
    private ObservableList<String> files;
    private Button singleResultButton;
    private Button sumResultButton;

    public OutputView() {
        initElements();
        addElements();
    }

    private void initElements() {
        this.filesListView = new ListView<>();
        this.files = FXCollections.observableArrayList();
        this.singleResultButton = new Button("Single result");
        this.sumResultButton = new Button("Sum result");

        setSpacing(10);
        setPadding(new Insets(10));
    }

    private void addElements() {
        filesListView.setItems(files);
        getChildren().add(filesListView);
        getChildren().add(singleResultButton);
        getChildren().add(sumResultButton);
    }
}
