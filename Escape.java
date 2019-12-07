package escape;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Escape {

    static int OBJSIZE = 10;
    static int LOCSIZE = 18;
    static int index = 0;
    static int index2 = 0;

    public static void main(String[] args) {
        Objects[] objects = new Objects[OBJSIZE];
        Locations[] locations = new Locations[LOCSIZE];

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("Objects.json")) {
            Object obj = jsonParser.parse(reader);

            JSONArray objectsList = (JSONArray) obj;

            objectsList.forEach(emp -> parseJSONObjects((JSONObject) emp, objects));

        } catch (FileNotFoundException e) {
            //System.out.println(e);
        } catch (IOException | ParseException e) {
            //System.out.println(e);
        }

        JSONParser jsonParser2 = new JSONParser();
        try (FileReader reader = new FileReader("Locations.json")) {
            Object obj = jsonParser2.parse(reader);

            JSONArray locationsList = (JSONArray) obj;

            locationsList.forEach(emp -> parseJSONLocations((JSONObject) emp, locations, objects));

        } catch (FileNotFoundException e) {
            //System.out.println(e);
        } catch (IOException | ParseException e) {
            //System.out.println(e);
        }

        List<Objects> sack = new ArrayList<>();
        Objects[] bossWeapons = new Objects[2];
        bossWeapons[0] = new Objects("Scythe", true, 15, 5);
        bossWeapons[1] = new Objects("Dagger", true, 5, 2);

        String input = "";
        System.out.println("\t\t---------\tWelcome to the game of Escape!\t---------\n");
        System.out.println("You were teleported to this unknown area by a magical and evil being and you have no choice but to escape.\n"
                + "Pick up items as you go to each location to defeat the boss at the end of the maze!\n"
                + "How would you like to proceed?\n");
        rules(sack);
        MainCharacter main = new MainCharacter("Main", "human", 10, 50, locations[0]);
        Objects bossWeapon = new Objects("scythe", true, 10, 10);
        Boss boss = new Boss(bossWeapons, "Reaper", "demon", 50, 50, locations[LOCSIZE - 1]);
        Minions[] min = new Minions[3];
        Objects minWeapon = new Objects("dagger", true, 2, 2);
        int minNum = 1;
        for (int i = 0; i < min.length; i++) {
            min[i] = new Minions("Minion[" + minNum + "]", "mini-demon", 2, 10, locations[LOCSIZE - 1], minWeapon);
            minNum++;
        }

        while (!input.equals("quit")) {
            Scanner strings = new Scanner(System.in);
            if (boss.getHealth() <= 0 && min[0].getHealth() <= 0 && min[1].getHealth() <= 0 && min[2].getHealth() <= 0) {
                win();
            }
            System.out.printf("\nInput: ");
            input = strings.nextLine();
            applyAction(input, sack, objects, main.currentLoc, main, locations, min, boss);
        }
    }

    static void rules(List<Objects> sack) {
        System.out.println("\t---------\tHow to Play\t---------");
        System.out.println("Movement: 'go up', 'go down', 'go left', or 'go right'. Try each direction in the rooms to map out the maze!");
        System.out.println("Actions: 'pickup (item)', 'drop (item)', 'look', 'sack', and 'rules'.");
        System.out.println("Attacking: 'use (item)', 'trip', and 'punch'. Note: You can only attack while around enemies.");
        System.out.println("'Look' looks around the room you are in. 'Sack' looks at your current items in your sack. "
                + "If you forget how to play, 'rules' brings up these rules.");
        System.out.println("To save your game type 'save'. To load after quitting or later in the maze if you want to go back, type 'load'.");
        System.out.printf("Current carry weight: %.2f \\ 10.00\n", carryweight(sack));

    }

    static void applyAction(String input, List<Objects> sack, Objects[] objects, Locations loc, MainCharacter main, Locations[] allLocs, Minions[] min, Boss boss) {
        input = input.trim();
        String[] action = input.split(" +");
        if (action.length == 0 || action[0].equals("")) {
            System.out.println("Don't forget to have an input!");
            return;
        } else if (action.length > 2) {
            if (action[1].equals("health") && action[2].equals("potion")) {
                action[1] = "health potion";
            } else {
                System.out.println("Too many arguments.");
                return;
            }
        }
        action[0] = action[0].toLowerCase();
        if (action.length <= 1) {
            switch (action[0]) {
                case "pickup":
                    System.out.println("Pickup what?");
                    return;
                case "drop":
                    System.out.println("Drop what?");
                    return;
                case "use":
                    System.out.println("Use what?");
                    return;
                case "go":
                    System.out.println("Go where?");
                    return;
                case "sack":
                    sack(sack);
                    return;
                case "rules":
                    rules(sack);
                    return;
                case "look":
                    look(loc);
                    return;
                case "trip":
                    attack("trip", sack, min, boss, main);
                    return;
                case "punch":
                    attack("punch", sack, min, boss, main);
                    return;
                case "save":
                    save(main, allLocs, sack, objects);
                    return;
                case "load":
                    load(main, allLocs, sack, objects);
                    return;
                case "quit":
                    return;
                default:
                    System.out.println("You don't know how to do that.");
                    return;
            }
        }
        action[1] = action[1].toLowerCase();
        switch (action[0]) {
            case "pickup":
                pickup(action[1], sack, objects, loc);
                break;
            case "drop":
                drop(action[1], sack, loc);
                break;
            case "use":
                useObject(action[1], sack, main, min, boss);
                break;
            case "go":
                go(action[1], loc, main, allLocs);
                break;
            default:
                System.out.println("You don't know how to do that.");
        }

    }

    static void pickup(String item, List<Objects> sack, Objects[] objects, Locations loc) {
        for (int i = 0; i < loc.objects.size(); i++) {
            if (item.equals(loc.objects.get(i).getName())) {
                if (carryweight(sack) + loc.objects.get(i).getWeight() >= 10) {
                    System.out.println("You are carrying too much! Please drop items.");
                    return;
                }
                sack.add(loc.objects.get(i));
                loc.objects.remove(i);
                System.out.println("You have picked up " + item + ".");
                return;
            }
        }
        System.out.println("Invalid item.");
    }

    static void drop(String item, List<Objects> sack, Locations loc) {
        for (int i = 0; i < sack.size(); i++) {
            if (item.equals(sack.get(i).getName())) {
                loc.objects.add(sack.get(i));
                sack.remove(i);
                System.out.println("You have dropped " + item + ".");
                return;
            }
        }
        System.out.println("You do not have that item!");
    }

    static void useObject(String item, List<Objects> sack, MainCharacter main, Minions[] min, Boss boss) {
        for (int i = 0; i < sack.size(); i++) {
            if (item.equals(sack.get(i).getName())) {
                if (sack.get(i).isUsable() == true) {
                    if (sack.get(i).getDamage() > 0 && main.currentLoc.name.equals("boss room")) {
                        attack(item, sack, min, boss, main);
                        return;
                    } else if (sack.get(i).getDamage() == 0) {
                        if (sack.get(i).getName().equals("map")) {
                            System.out.println("The map is almost completely destroyed with only a little corner showing that says 'Danger!'");
                            return;
                        } else if (sack.get(i).getName().equals("health potion")) {
                            int newHealth = main.getHealth() + 10;
                            main.setHealth(newHealth);
                            System.out.println("+10 health!");
                            System.out.println("Your new health is: " + main.getHealth());
                            return;
                        } else if (sack.get(i).getName().equals("harmonica")) {
                            System.out.println("You play a little tune that boosts your spirits!");
                            return;
                        } else {
                            System.out.println("ERROR. ITEM SET TO USABLE WHEN UNUSABLE.");
                        }
                    } else {
                        System.out.println("You can't use that here!");
                        return;
                    }
                } else {
                    System.out.println("Item is not usable!");
                    return;
                }
            }

        }
        System.out.println("You do not have " + item + ".");
    }

    static void attack(String item, List<Objects> sack, Minions[] min, Boss boss, MainCharacter main) {
        Scanner string = new Scanner(System.in);
        System.out.println("Who do you want to attack?");

        for (Minions m : min) {
            if (m.getHealth() > 0) {
                System.out.println("Name: " + m.name + "     Health:" + m.health);
            }
        }
        if (boss.getHealth() > 0) {
            System.out.println("Name: " + boss.name + "    Health:" + boss.health);
        }
        System.out.print("Input: ");
        String who = string.nextLine();
        who = who.toLowerCase();
        for (Minions m : min) {
            if (who.equals(m.name.toLowerCase()) && m.getHealth() > 0) {
                boolean trip = damage(m, item, sack, main);
                enemyMove(boss, min, main, trip);
                return;
            }
        }
        if (who.equals(boss.name.toLowerCase()) && boss.getHealth() > 0) {
            boolean trip = damage(boss, item, sack, main);
            enemyMove(boss, min, main, trip);
            return;
        }
        System.out.println("Enemy does not exist!");

    }

    static boolean damage(Boss boss, String item, List<Objects> sack, MainCharacter main) {
        boolean trip = false;
        if (item.equals("punch")) {
            Random rollHit = new Random();
            int damage = main.punch();
            int toHitChance = rollHit.nextInt(20);
            if (toHitChance > 5) {
                int newHealth = boss.getHealth() - damage;
                if (newHealth <= 0) {
                    System.out.println("Reaper is dead!!");
                } else {
                    System.out.println("You did " + damage + " of punch damage to " + boss.getName() + "!");
                    boss.setHealth(newHealth);
                }
                return trip;
            } else {
                System.out.println("Your attack missed!");
                return trip;
            }

        } else if (item.equals("trip")) {
            trip = true;
            System.out.println("You have tripped the Reaper!");
            return trip;
        }
        for (Objects s : sack) {
            if (item.equals(s.getName())) {
                Random rollHit = new Random();
                int damage = (int) s.getDamage();
                int toHitChance = rollHit.nextInt(20);
                if (toHitChance > 9) {
                    int newHealth = boss.getHealth() - damage;
                    if (newHealth <= 0) {
                        System.out.println("Reaper is dead!!");
                        boss.setHealth(newHealth);
                    } else {
                        System.out.println("You did " + damage + " damage to " + boss.getName() + "!");
                        boss.setHealth(newHealth);
                    }
                } else {
                    System.out.println("Your attack missed!");
                }
            }
        }
        return trip;
    }

    static boolean damage(Minions min, String item, List<Objects> sack, MainCharacter main) {
        boolean trip = false;
        if (item.equals("punch")) {
            Random rollHit = new Random();
            int damage = main.punch();
            int toHitChance = rollHit.nextInt(20);
            if (toHitChance > 5) {
                int newHealth = min.getHealth() - damage;
                if (newHealth <= 0) {
                    System.out.println(min.getName() + " is dead!!");
                    min.setHealth(newHealth);
                } else {
                    System.out.println("You did " + damage + " of punch damage to " + min.getName() + "!");
                    min.setHealth(newHealth);
                }
                return trip;
            } else {
                System.out.println("Your attack missed!");
                return trip;
            }

        } else if (item.equals("trip")) {
            trip = true;
            System.out.println("You have tripped the Reaper!");
            return trip;
        }
        for (Objects s : sack) {
            if (item.equals(s.getName())) {
                Random rollHit = new Random();
                int damage = (int) s.getDamage();
                int toHitChance = rollHit.nextInt(20);
                if (toHitChance > 5) {
                    int newHealth = min.getHealth() - damage;
                    if (newHealth <= 0) {
                        System.out.println(min.getName() + " is dead!!");
                        min.setHealth(newHealth);
                        return trip;
                    } else {
                        System.out.println("You did " + damage + " damage to " + min.getName() + "!");
                        min.setHealth(newHealth);
                    }
                } else {
                    System.out.println("Your attack missed!");
                }
            }
        }
        return trip;
    }

    static void enemyMove(Boss boss, Minions[] min, MainCharacter main, boolean trip) {
        if (trip == false) {
            for (Minions m : min) {
                if (m.getHealth() > 0) {
                    Random rollHit = new Random();
                    int damage = (int) m.weapon.getDamage();
                    int toHitChance = rollHit.nextInt(20);
                    if (toHitChance >= 13) {
                        int newHealth = main.getHealth() - damage;
                        if (newHealth <= 0) {
                            System.out.println("You are dead!!");
                            death();
                            return;
                        } else {
                            System.out.println("You took " + damage + " damage from " + m.getName() + "!");
                            main.setHealth(newHealth);
                        }
                    } else {
                        System.out.println(m.getName() + "'s attack missed!");
                    }
                } else {

                }
            }
            if (boss.getHealth() > 0) {
                Random weaponChoice = new Random(10);
                int weap = weaponChoice.nextInt();
                if (weap == 1 || weap == 4 || weap == 10) {
                    Random rollHit = new Random();
                    int damage = (int) boss.weapons[1].getDamage();
                    int toHitChance = rollHit.nextInt(20);
                    if (toHitChance >= 10) {
                        int newHealth = main.getHealth() - damage;
                        if (newHealth <= 0) {
                            System.out.println("You are dead!!");
                            death();
                            return;
                        } else {
                            System.out.println("You took " + damage + " damage from " + boss.getName() + "!");
                            main.setHealth(newHealth);
                        }
                    } else {
                        System.out.println(boss.name + "'s attack missed!");
                    }
                } else {
                    Random rollHit = new Random();
                    int damage = (int) boss.weapons[0].getDamage();
                    int toHitChance = rollHit.nextInt(20);
                    if (toHitChance >= 10) {
                        int newHealth = main.getHealth() - damage;
                        if (newHealth <= 0) {
                            System.out.println("\nYou are dead!!");
                            death();
                            return;
                        } else {
                            System.out.println("You took " + damage + " damage from " + boss.getName() + "!");
                            main.setHealth(newHealth);
                        }
                    } else {
                        System.out.println(boss.getName() + "'s attack missed!");
                    }
                    if (toHitChance >= 10 && boss.getHealth() <= 25) {
                        int punchDamage = boss.punch();
                        int newHealth = main.getHealth() - punchDamage;
                        System.out.println("You took " + punchDamage + " of punch damage from " + boss.getName() + "!");
                        main.setHealth(newHealth);
                    }
                }
            } else {

            }
            System.out.println("\nYour health is: " + main.getHealth());
        } else {
            System.out.println("The enemy is reorganizing after being tripped!");
            System.out.println("\nYour health is: " + main.getHealth());
        }
    }

    static void death() {
        System.out.println("The game will now quit. Please restart and load your game and you will be in the last location saved.");
        exit(0);
    }

    static void win() {
        System.out.println("YOU WIN!\nCongrats on winning, champion! You have beat the boss and have been teleported back to your house. "
                + "While still stunned you realize you still have a sack on your person and you reach inside to find the items you collected "
                + "in your adventure. Congrats!\n"
                + "The game will now quit.\n");
        exit(0);
    }

    static void sack(List<Objects> sack) {
        System.out.println("Items in your sack: ");
        for (Objects obj : sack) {
            System.out.println(obj.getName());
        }
        System.out.printf("Current carry weight: %.2f \\ 10.00\n", carryweight(sack));
    }

    static void look(Locations loc) {
        System.out.println(loc.description);
        if (loc.objects.isEmpty() || (loc.objects.get(0).getName().equals("") && loc.objects.size() <= 1)) {
            return;
        }
        System.out.println("\nThe items you see are:");
        for (int i = 0; i < loc.objects.size(); i++) {
            System.out.println(loc.objects.get(i).getName());
        }
    }

    static void go(String item, Locations loc, MainCharacter main, Locations[] allLocs) {
        if (loc.directions.containsKey(item)) {
            for (String key : loc.directions.keySet()) {
                if (key.equals(item)) {
                    String value = loc.directions.get(key);
                    for (int i = 0; i < allLocs.length; i++) {
                        if (allLocs[i].name.equals(value)) {
                            look(allLocs[i]);
                            main.currentLoc = allLocs[i];
                            break;
                        }
                    }
                }
            }
        } else {
            System.out.println("You can't go that way!");
        }
    }

    static double carryweight(List<Objects> sack) {
        double weight = 0;
        for (Objects obj : sack) {
            weight += obj.getWeight();
        }
        return weight;
    }

    static void save(MainCharacter main, Locations[] allLocs, List<Objects> sack, Objects[] objects) {
        if (!main.currentLoc.getName().equals("boss room")) {
            try (PrintWriter save = new PrintWriter("saveData.txt", "UTF-8")) {
                save.println(main.getCurrentLoc().getName());
                save.println(main.getHealth());
                for (Objects s : sack) {
                    save.println(s);
                }
                save.println("Locations");
                for (Locations l : allLocs) {
                    save.println(l.getObjects());
                }
                save.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            System.out.println("File saved!\n");
        } else {
            System.out.println("You cannot save here!");
        }
    }

    static void load(MainCharacter main, Locations[] allLocs, List<Objects> sack, Objects[] objects) {
        try {
            try (BufferedReader in = new BufferedReader(new FileReader("saveData.txt"))) {
                String line;
                List<String> data = new ArrayList<>();
                while ((line = in.readLine()) != null) {
                    data.add(line);
                }
                String location = data.get(0);
                for (Locations l : allLocs) {
                    if (location.equals(l.name)) {
                        main.setCurrentLoc(l);
                    }
                }
                data.remove(0);
                int health = Integer.parseInt(data.get(0));
                main.setHealth(health);
                data.remove(0);
                sack.clear();
                String objSack = data.get(0);
                while (!objSack.equals("Locations")) {
                    for (Objects s : objects) {
                        if (objSack.equals(s.toString())) {
                            sack.add(s);
                        } else {

                        }
                    }
                    data.remove(0);
                    objSack = data.get(0);
                }
                data.remove(0);
                List<String> objLocs = new ArrayList<>();
                for (String d : data) {
                    objSack = d;
                    objLocs.add(objSack);
                }
                for (int i = 0; i < LOCSIZE; i++) {
                    List<Objects> locObjects = new ArrayList<>();
                    for (Objects o : objects) {
                        if (objLocs.get(i).contains(", O")) {
                            String[] addString = objLocs.get(i).split(", (?=O)");
                            for (String a : addString) {
                                if (a.startsWith("[") && !a.endsWith("]")) {
                                    a = a + "]";
                                }
                                if (!a.startsWith("[") && a.endsWith("]")) {
                                    a = "[" + a;
                                }
                                if (!a.startsWith("[") && !a.endsWith("]")) {
                                    a = "[" + a + "]";
                                }
                                String objString = "[" + o.toString() + "]";
                                if (a.equals(objString)) {
                                    locObjects.add(o);
                                } else if (objLocs.get(i).equals("[]")) {
                                }
                            }
                        } else {
                            String objString = "[" + o.toString() + "]";
                            if (objLocs.get(i).equals(objString)) {
                                locObjects.add(o);
                            } else if (objLocs.get(i).equals("[]")) {
                            }
                        }
                    }
                    allLocs[i].setObjects(locObjects);
                }
                System.out.println("File loaded!\n");
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private static void parseJSONObjects(JSONObject objects, Objects[] object) {
        JSONObject objsObject = (JSONObject) objects.get("objects");
        String name = (String) objsObject.get("name");
        boolean usable = (boolean) objsObject.get("usable");
        double weight = (double) objsObject.get("weight");
        long damage = (long) objsObject.get("damage");
        object[index] = new Objects(name, usable, weight, damage);
        index++;
    }

    private static void parseJSONLocations(JSONObject locations, Locations[] location, Objects[] object) {
        JSONObject locationObject = (JSONObject) locations.get("locations");
        String desc = (String) locationObject.get("description");
        String name = (String) locationObject.get("name");
        JSONArray objects = (JSONArray) locationObject.get("objects");

        Iterator<String> it = objects.iterator();
        List<Objects> objs = new ArrayList<>();
        while (it.hasNext()) {
            String mStr = it.next();
            for (Objects elm : object) {
                if (elm.getName().equals(mStr)) {
                    objs.add(elm);
                }
            }
        }

        JSONArray directions = (JSONArray) locationObject.get("directions");
        Map<String, String> map = new HashMap<>();
        Iterator<JSONObject> it2 = directions.iterator();
        while (it2.hasNext()) {
            JSONObject value = it2.next();
            map.put((String) value.get("direction"), (String) value.get("location"));
        }
        location[index2] = new Locations(desc, name, objs, map);
        index2++;
    }
}
