package gui.model;

public class CruncherModel {

    /**
     * Name of the cruncher, used as an ID.
     */
    private String name;
    /**
     * Arity that was given from the GUI by the user.
     */
    private int arity;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CruncherModel) {
            CruncherModel otherObj = (CruncherModel) obj;
            return this.name.equals(otherObj.getName());
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
