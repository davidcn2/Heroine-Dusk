package heroinedusk;

import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.ArrayList;

/*
Interface (implements) vs Sub-Class (extends)...

The distinction is that implements means that you're using the elements of a Java Interface in your 
class, and extends means that you are creating a subclass of the class you are extending. You can 
only extend one class in your new class, but you can implement as many interfaces as you would like.

Interface:  A Java interface is a bit like a class, except a Java interface can only contain method
signatures and fields. An Java interface cannot contain an implementation of the methods, only the 
signature (name, parameters and exceptions) of the method. You can use interfaces in Java as a way
to achieve polymorphism.

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use 
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.
*/

public class MazeMap 
{

    /* 
    The class stores data for the current active region / map.
    
    Methods include:
    
    ...
    */
    
    private Atlas atlas; // Reference to the atlas information.
    private AtlasItems atlasItems; // Reference to the atlas items information.
    private int current_id; // Current region / map location (number).
    private RegionMap currentRegion; // Reference to current region / map.
    private String regionName; // Name of current region / map location.
      // Maps to one of the integers in the mapIdentifiers object in the atlas.
    private HeroineEnum.MusicEnum current_song; // Song associated with the current region / map location.
    private int regionHeight; // Region height, in tiles.
    private final ArrayList<ArrayList<Integer>> regionTiles; // List of tiles composing the current region.
      // Example for use -- get(x).get(y):  Integer x = regionTiles.get(0).get(0);
    private int regionWidth; // Region width, in tiles.
    
    // Reference to the atlas information.
    // Reference to the atlas items information.
    public MazeMap(Atlas atlas, AtlasItems atlasItems)
    {
        
        /*
        The constructor performs the following actions:
        
        1.  Initializes array lists.
        2.  Stores references to atlas region and item information.
        3.  Sets starting region / map location.
        4.  Stores region name.
        5.  Stores reference to current region / map.
        6.  Copies tiles for current region to class-level variable.
        7.  Stores region width and height -- in tiles.
        */
        
        // Initialize array lists.
        regionTiles = new ArrayList<>();
        
        // Store reference to the atlas information.
        this.atlas = atlas;
        
        // Store reference to the atlas items information.
        this.atlasItems = atlasItems;
        
        // Set starting region / map location.
        current_id = 0;
        
        // Get region name.
        regionName = atlas.mapIdentifiersRev.get(current_id);
        
        // Store reference to current region / map.
        this.currentRegion = atlas.maps.get(regionName);
        
        System.out.println("Current region: " + regionName);
        
        // Copy tiles for current region. -- Actually, stores references.
        regionTiles.addAll(currentRegion.getRegionTiles());
        
        // Copy current region width and height -- in tiles.
        regionWidth = currentRegion.getRegionWidth();
        regionHeight = currentRegion.getRegionHeight();
        
        System.out.println("Region size: " + regionWidth + " by " + regionHeight);
        
    }
    
    // pos_x = X-coordinate to use in bounds check.
    // pos_y = Y-coordinate to use in bounds check.
    private boolean mazemap_bounds_check(int pos_x, int pos_y)
    {
        
        // The function checks to see if the passed location exists in the current region.
        
        // Return whether passed location exists in the current reigon.
        return pos_x >= 0 && pos_y >= 0 && pos_x < regionWidth && pos_y < regionHeight;
        
    }
    
    // x = X-coordinate within current region to render.
    // y = Y-coordinate within current region to render.
    // facing = Direction the player is facing.
    // whichStage = Stage to which to render.
    public void mazemap_render(int x, int y, HeroineEnum.FacingEnum facing, Stage whichStage)
    {
        
        /*
        The function renders the current location.
        
        The visibility cone is shaped like this:

        .........
        ..VVVVV..
        ..VVVVV..
        ...V@V...
        .........

        Drawing is done in this order (a=10, b=11, c=12)

        .........
        ..02431..
        ..57986..
        ...acb...
        .........
        */
        
        System.out.println("X: " + x + ", Y: " + y + ", Facing: " + facing);
        
        
        // Depending on direction facing, ...
        switch (facing) {
            
            case NORTH:
                
                // Exit selector.
                break;
                
            case SOUTH: // Starting direction.
                
                // Render back row.
                mazemap_render_tile( x + 2, y + 2, 0 );
                mazemap_render_tile( x - 2, y + 2, 1 );
                mazemap_render_tile( x + 1, y + 2, 2 );
                mazemap_render_tile( x - 1, y + 2, 3 );
                mazemap_render_tile( x, y + 2, 4 );
                
                // Render middle row.
                mazemap_render_tile( x + 2, y + 1, 5);
                mazemap_render_tile( x - 2, y + 1, 6);
                mazemap_render_tile( x + 1, y + 1, 7);
                mazemap_render_tile( x - 1, y + 1, 8);
                mazemap_render_tile( x, y + 1, 9);
                
                // Render front row.
                mazemap_render_tile(x + 1, y, 10);
                mazemap_render_tile(x - 1, y, 11);
                mazemap_render_tile(x, y, 12);
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Exit selector.
                break;
                
            default:
                
                // Display warning message.
                System.out.println("Warning:  Player facing in invalid direction!");
                
                // Exit selector.
                break;
                
            } // Depending on direction facing...
        
    }
    
    // pos_x = Y-coordinate of tile to render.
    // pos_y = X-coordinate of tile to render.
    // position = Position information for tile to render (offset within source graphic).
    public void mazemap_render_tile(int pos_x, int pos_y, int position)
    {
        // The function renders a tile.
        // Note: x, y flipped to ease map making.
        
        int tileNbr; // Tile number to render.
        
        // If tile exists in current region, then...
        if (mazemap_bounds_check(pos_x, pos_y))
        {
            
            // Tile exists in current region.
            
            // Determine tile number ot render.
            tileNbr = regionTiles.get(pos_y).get(pos_x);
            
            System.out.println("Rendering tile (tile, pos): " + tileNbr + ", " + position);
            
        }
        
    }
    
    // Getters and setters below...
    
    public RegionMap getCurrentRegion() {
        return currentRegion;
    }
    
}
