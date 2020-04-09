package gui.model;

public class DiscModel {

    private String path;

    public DiscModel(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }
}
