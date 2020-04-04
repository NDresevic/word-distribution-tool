package gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OutputModel {

    private ObservableList<String> files;

    public OutputModel() {
        this.files = FXCollections.observableArrayList();
    }

    public ObservableList<String> getFiles() {
        return files;
    }
}
