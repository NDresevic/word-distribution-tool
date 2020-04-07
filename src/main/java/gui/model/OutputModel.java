package gui.model;

import javafx.collections.ObservableList;

public class OutputModel {

    private ObservableList<FileModel> files;

    public OutputModel(ObservableList<FileModel> files) {
        this.files = files;
    }

    public ObservableList<FileModel> getFiles() {
        return files;
    }
}
