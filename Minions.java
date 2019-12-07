package escape;

public class Minions extends Characters {

    Objects weapon;

    public Minions(String name, String race, int level, int health, Locations currentLoc, Objects weapon) {
        super(name, race, level, health, currentLoc);
        this.weapon = weapon;
    }

    public void setWeapon(Objects weapon) {
        this.weapon = weapon;
    }

    public Objects getWeapon() {
        return weapon;
    }

    public String getName() {
        return name;
    }

    public String getRace() {
        return race;
    }

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public Locations getCurrentLoc() {
        return currentLoc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setCurrentLoc(Locations currentLoc) {
        this.currentLoc = currentLoc;
    }
    
    @Override
    void play() {
        System.out.println(getName() + " plays a little tune that boosts his spirits!");
    }

    @Override
    public boolean trip() {
        return false;
    }

    @Override
    public int punch() {
        return 1;
    }
}
