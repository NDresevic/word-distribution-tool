package gui.model;

import javafx.collections.ObservableList;

public class OutputModel {

    private ObservableList<String> files;

    public OutputModel(ObservableList<String> files) {
        this.files = files;
    }

    public ObservableList<String> getFiles() {
        return files;
    }
}
