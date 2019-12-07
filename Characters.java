package escape;

public abstract class Characters implements oddAttacks {

    String name;
    String race;
    int level;
    int health;
    Locations currentLoc;

    public Characters(String name, String race, int level, int health, Locations currentLoc) {
        this.name = name;
        this.race = race;
        this.level = level;
        this.health = health;
        this.currentLoc = currentLoc;
    }
    
    abstract void play();
}
