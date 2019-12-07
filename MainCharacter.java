package escape;

public class MainCharacter extends Characters {
    
    public MainCharacter(String name, String race, int level, int health, Locations currentLoc) {
        super(name, race, level, health, currentLoc);
    }
    
    public MainCharacter(int health, Locations currentLoc) {
        super("Main", "human", 50, health, currentLoc);
    }


    public Locations getCurrentLoc() {
        return currentLoc;
    }

    public void setCurrentLoc(Locations currentLoc) {
        this.currentLoc = currentLoc;
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

    @Override
    void play() {
        System.out.println("You play a little tune that boosts your spirits!");
    }

    @Override
    public boolean trip() {
        return true;
    }

    @Override
    public int punch() {
        return 2;
    } 
}
