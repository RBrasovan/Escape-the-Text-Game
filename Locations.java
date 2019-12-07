package escape;

import java.util.List;
import java.util.Map;

public class Locations {
    String description;
    String name;
    List<Objects> objects;
    Map<String,String> directions;
    Locations[] neighbors;

    public Locations(String description, String name, List<Objects> objects, Map<String,String> directions) {
        this.description = description;
        this.name = name;
        this.objects = objects;
        this.directions = directions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Objects> getObjects() {
        return objects;
    }

    public void setObjects(List<Objects> objects) {
        this.objects = objects;
    }

    public Map<String, String> getDirections() {
        return directions;
    }

    public void setDirections(Map<String, String> directions) {
        this.directions = directions;
    }


    public Locations[] getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Locations[] neighbors) {
        this.neighbors = neighbors;
    }
   
}
