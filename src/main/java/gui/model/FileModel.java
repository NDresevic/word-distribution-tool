package gui.model;

public class FileModel {

    /**
     * Name of the file.
     */
    private String name;
    /**
     * Boolean that indicates if the file was processed.
     */
    private boolean complete;

    public FileModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
