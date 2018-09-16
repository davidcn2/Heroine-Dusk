package heroinedusk;

public class MapLocation 
{

    /*
    The class stores information and logic related to a map location (map_id, x, y).
    
    Methods include:

    setMapLocation:  Stores information related to the passed map location.
    */
    
    private int map_id; // Map section identifier.
    private int x; // X-coordinate within the map section.
    private int y; // Y-coordinate within the map section.
    
    // map_id = Map section identifier.
    // x = X-coordinate within the map section.
    // y = Y-coordinate within the map section.
    public MapLocation(int map_id, int x, int y)
    {
        
        // The constructor stores information related to the map location.
        
        // Store map location information.
        setMapLocation(map_id, x, y);
        
    }
    
    // map_id = Map section identifier.
    // x = X-coordinate within the map section.
    // y = Y-coordinate within the map section.
    public final void setMapLocation(int map_id, int x, int y)
    {
        
        // The function stores information related to the passed map location.
        
        // Store map location information.
        this.map_id = map_id;
        this.x = x;
        this.y = y;
        
    }
    
    // Getters and setters below...
    
    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
