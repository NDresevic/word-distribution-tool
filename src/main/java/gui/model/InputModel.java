package gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class InputModel {

    private String name;
    private String disc;
    private ObservableList<String> crunchers;
    private ObservableList<File> directories;

    public InputModel(String name, String disc) {
        this.name = name;
        this.disc = disc;
        this.crunchers = FXCollections.observableArrayList();
        this.directories = FXCollections.observableArrayList();
    }

    public String getName() {
        return name;
    }

    public String getDisc() {
        return disc;
    }

    public ObservableList<String> getCrunchers() {
        return crunchers;
    }

    public ObservableList<File> getDirectories() {
        return directories;
    }
}
