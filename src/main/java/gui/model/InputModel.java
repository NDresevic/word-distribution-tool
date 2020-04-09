package gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class InputModel {

    private String name;
    private DiscModel disc;
    private ObservableList<CruncherModel> crunchers;
    private ObservableList<File> directories;

    public InputModel(String name, DiscModel disc) {
        this.name = name;
        this.disc = disc;
        this.crunchers = FXCollections.observableArrayList();
        this.directories = FXCollections.observableArrayList();
    }

    public String getName() {
        return name;
    }

    public DiscModel getDisc() {
        return disc;
    }

    public ObservableList<CruncherModel> getCrunchers() {
        return crunchers;
    }

    public ObservableList<File> getDirectories() {
        return directories;
    }
}
