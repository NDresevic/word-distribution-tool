package components.input;

public class InputData {

    private String name;
    private String content;
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
