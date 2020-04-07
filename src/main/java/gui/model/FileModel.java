package gui.model;

public class FileModel {

    private String name;
    private boolean complete;

    public FileModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        if (!complete) {
            return "*" + name;
        }
        return name;
    }
}
