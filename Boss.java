package escape;

public class Boss extends Characters {

    Objects[] weapons;

    public Boss(Objects[] weapons, String name, String race, int level, int health, Locations location) {
        super(name, race, level, health, location);
        this.weapons = weapons;
    }

    public Objects[] getWeapons() {
        return weapons;
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

    public void setWeapons(Objects[] weapons) {
        this.weapons = weapons;
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
        System.out.println("Reaper plays a little tune that boosts his spirits!");
    }

    @Override
    public boolean trip() {
        return false;
    }

    @Override
    public int punch() {
        return 2;
    }
}
