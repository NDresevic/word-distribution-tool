package components.input;

public class InputData {

    /**
     * Name of the file.
     */
    private String name;
    /**
     * Content of the file.
     */
    private String content;
    /**
     * Boolean that indicates if this object is used as a poison pill.
     */
    private boolean poisoned;

    public InputData(String name, String content) {
        this.name = name;
        this.content = content;
        this.poisoned = false;
    }

    public InputData(boolean poisoned) {
        this.name = "";
        this.content = "";
        this.poisoned = poisoned;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public boolean isPoisoned() {
        return poisoned;
    }
}
