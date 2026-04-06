package Models;

public abstract class Items {
    protected String name;

    public Items(String name) {
        this.name = name;
    }

    public abstract void use(Pokemon target);

    public String getName() {
        return name;
    }
}
