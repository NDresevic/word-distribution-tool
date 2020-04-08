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
    public boolean equals(Object obj) {
        if (obj instanceof FileModel) {
            FileModel otherObj = (FileModel) obj;
            return this.name.equals(otherObj.getName());
        }
        return false;
    }

    @Override
    public String toString() {
        if (!complete) {
            return "*" + name;
        }
        return name;
    }
}
