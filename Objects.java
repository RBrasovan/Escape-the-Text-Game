package escape;

public class Objects {

    final private String name;
    final private boolean usable;
    final private double weight;
    final private long damage;

    public Objects(String name, boolean usable, double weight, long damage) {
        this.name = name;
        this.usable = usable;
        this.weight = weight;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public boolean isUsable() {
        return usable;
    }

    public double getWeight() {
        return weight;
    }

    public long getDamage() {
        return damage;
    }


    @Override
    public String toString() {
        return "Objects{" + "name=" + name + ", usable=" + usable + ", weight=" + weight + ", damage=" + damage + "}";
    }
}
