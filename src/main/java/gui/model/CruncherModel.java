package gui.model;

public class CruncherModel {

    private String name;
    private int arity;
    private boolean crunching;

    public CruncherModel(String name, int arity) {
        this.name = name;
        this.arity = arity;
    }

    public String getName() {
        return name;
    }

    public int getArity() {
        return arity;
    }

    public boolean isCrunching() {
        return crunching;
    }

    public void setCrunching(boolean crunching) {
        this.crunching = crunching;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
