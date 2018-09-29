package heroinedusk;

// LibGDX imports.
import core.BaseActor;

// Java imports.
import java.util.ArrayList;
import java.text.DecimalFormat;

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
    
    // Declare object variables.
    private final Atlas atlas; // Reference to the atlas information.
    private final AtlasItems atlasItems; // Reference to the atlas items information.
    private RegionMap currentRegion; // Reference to current region / map.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private final ArrayList<ArrayList<Integer>> regionTiles; // List of tiles composing the current region.
      // Example for use -- get(x).get(y):  Integer x = regionTiles.get(0).get(0);
    
    // Declare regular variables.
    private int current_id; // Current region / map location (number).
    private float dest_x[]; // Array holding X-coordinate (relative to the bottom left corner) to display
      // tile texture region in stage -- adjusted by scale factor.  Excludes render offset.
    private float dest_y[]; // Array holding Y-coordinate (relative to the bottom left corner) to display
      // tile texture region in stage -- adjusted by scale factor.  Excludes render offset.
    private String regionName; // Name of current region / map location.
      // Maps to one of the integers in the mapIdentifiers object in the atlas.
    private HeroineEnum.MusicEnum current_song; // Song associated with the current region / map location.
    private int regionHeight; // Region height, in tiles.
    private int regionWidth; // Region width, in tiles.
    
    // Declare constants.
    private final String decFormatText000 = "000"; // Text used for decimal style used to format numbers as 000.
      // Examples:  1 > 001, 2 > 002, ...
    private final DecimalFormat decimalFormat000 = new DecimalFormat(decFormatText000); // Decimal style used to
      // format numbers as 000.  Examples:  1 > 001, 2 > 002, ...
    
    // hdg = Reference to Heroine Dusk (main) game.
    public MazeMap(HeroineDuskGame hdg)
    {
        
        /*
        The constructor performs the following actions:
        
        1.  Initializes array lists and allocate space for arrays.
        2.  Stores references to atlas region and item information.
        3.  Store reference to main game class.
        4.  Sets starting region / map location.
        5.  Stores region name.
        6.  Stores reference to current region / map.
        7.  Copies tiles for current region to class-level variable.
        8.  Stores region width and height -- in tiles.
        9.  Calculate and store destinations for tiles.
        */
        
        int counter; // Used to increment through tile region enumerations.
        
        // Initialize array lists.
        regionTiles = new ArrayList<>();
        
        // Allocate space for arrays and array lists.
        dest_x = new float[HeroineEnum.TileRegionEnum.values().length];
        dest_y = new float[HeroineEnum.TileRegionEnum.values().length];
        
        // Store reference to main game class.
        gameHD = hdg;
        
        // Store reference to the atlas information.
        this.atlas = gameHD.getAtlas();
        
        // Store reference to the atlas items information.
        this.atlasItems = gameHD.getAtlasItems();
        
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
        
        // Calculate and store destinations for tiles.
        
        // Set starting value for counter.
        counter = 0;
        
        // Loop through tile region enumerations.
        for (HeroineEnum.TileRegionEnum tileRegionEnum : HeroineEnum.TileRegionEnum.values())
        {
            
            // Calculate and store destination coordinates, applying scale factor.
            dest_x[counter] = tileRegionEnum.getValue_Dest_X() * gameHD.getConfig().getScale();
            dest_y[counter] = tileRegionEnum.getValue_Dest_Y() * gameHD.getConfig().getScale();
            
            // Increment counter.
            counter++;
            
        }
        
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
    // tiles = BaseActor objects associated with tiles.
    public ArrayList<BaseActor> mazemap_render(int x, int y, HeroineEnum.FacingEnum facing)
    {
        
        /*
        The function returns the tiles used to render the current location.
        
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
        
        ArrayList<BaseActor> removeList; // List of actors to remove.
        ArrayList<BaseActor> tiles; // BaseActor objects associated with tiles.
        
        // 1.  Initialize array lists.
        tiles = new ArrayList<>();
        removeList = new ArrayList<>();
        
        // 2.  Prepare tiles for rendering.
        
        System.out.println("X: " + x + ", Y: " + y + ", Facing: " + facing);
        
        // Depending on direction facing, ...
        switch (facing) {
            
            case NORTH:
                
                // Render back row.
                tiles.add(mazemap_render_tile( x - 2, y - 2, 0 ));
                tiles.add(mazemap_render_tile( x + 2, y - 2, 1 ));
                tiles.add(mazemap_render_tile( x - 1, y - 2, 2 ));
                tiles.add(mazemap_render_tile( x + 1, y - 2, 3 ));
                tiles.add(mazemap_render_tile( x, y - 2, 4 ));
                
                // Render middle row.
                tiles.add(mazemap_render_tile( x - 2, y - 1, 5 ));
                tiles.add(mazemap_render_tile( x + 2, y - 1, 6 ));
                tiles.add(mazemap_render_tile( x - 1, y - 1, 7 ));
                tiles.add(mazemap_render_tile( x + 1, y - 1, 8 ));
                tiles.add(mazemap_render_tile( x, y - 1, 9 ));
                
                // Render front row.
                
                tiles.add(mazemap_render_tile( x - 1, y, 10 ));
                tiles.add(mazemap_render_tile( x + 1, y, 11 ));
                tiles.add(mazemap_render_tile( x, y, 12 ));
                
                // Exit selector.
                break;
                
            case SOUTH: // Starting direction.
                
                // Render back row.
                tiles.add(mazemap_render_tile( x + 2, y + 2, 0 ));
                tiles.add(mazemap_render_tile( x - 2, y + 2, 1 ));
                tiles.add(mazemap_render_tile( x + 1, y + 2, 2 ));
                tiles.add(mazemap_render_tile( x - 1, y + 2, 3 ));
                tiles.add(mazemap_render_tile( x, y + 2, 4 ));
                
                // Render middle row.
                tiles.add(mazemap_render_tile( x + 2, y + 1, 5 ));
                tiles.add(mazemap_render_tile( x - 2, y + 1, 6 ));
                tiles.add(mazemap_render_tile( x + 1, y + 1, 7 ));
                tiles.add(mazemap_render_tile( x - 1, y + 1, 8 ));
                tiles.add(mazemap_render_tile( x, y + 1, 9 ));
                
                // Render front row.
                tiles.add(mazemap_render_tile( x + 1, y, 10 ));
                tiles.add(mazemap_render_tile( x - 1, y, 11 ));
                tiles.add(mazemap_render_tile( x, y, 12 ));
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Render back row.
                tiles.add(mazemap_render_tile( x + 2, y - 2, 0 ));
                tiles.add(mazemap_render_tile( x + 2, y + 2, 1 ));
                tiles.add(mazemap_render_tile( x + 2, y - 1, 2 ));
                tiles.add(mazemap_render_tile( x + 2, y + 1, 3 ));
                tiles.add(mazemap_render_tile( x + 2, y, 4 ));
                
                // Render middle row.
                tiles.add(mazemap_render_tile( x + 1, y - 2, 5 ));
                tiles.add(mazemap_render_tile( x + 1, y + 2, 6 ));
                tiles.add(mazemap_render_tile( x + 1, y - 1, 7 ));
                tiles.add(mazemap_render_tile( x + 1, y + 1, 8 ));
                tiles.add(mazemap_render_tile( x + 1, y, 9 ));
                
                // Render front row.
                tiles.add(mazemap_render_tile( x, y - 1, 10 ));
                tiles.add(mazemap_render_tile( x, y + 1, 11 ));
                tiles.add(mazemap_render_tile( x, y, 12 ));
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Render back row.
                tiles.add(mazemap_render_tile( x - 2, y + 2, 0 ));
                tiles.add(mazemap_render_tile( x - 2, y - 2, 1 ));
                tiles.add(mazemap_render_tile( x - 2, y + 1, 2 ));
                tiles.add(mazemap_render_tile( x - 2, y - 1, 3 ));
                tiles.add(mazemap_render_tile( x - 2, y, 4 ));
                
                // Render middle row.
                tiles.add(mazemap_render_tile( x - 1, y + 2, 5 ));
                tiles.add(mazemap_render_tile( x - 1, y - 2, 6 ));
                tiles.add(mazemap_render_tile( x - 1, y + 1, 7 ));
                tiles.add(mazemap_render_tile( x - 1, y - 1, 8 ));
                tiles.add(mazemap_render_tile( x - 1, y, 9 ));
                
                // Render front row.
                tiles.add(mazemap_render_tile( x, y + 1, 10 ));
                tiles.add(mazemap_render_tile( x, y - 1, 11 ));
                tiles.add(mazemap_render_tile( x, y, 12 ));  
                
                // Exit selector.
                break;
                
            default:
                
                // Display warning message.
                System.out.println("Warning:  Player facing in invalid direction!");
                
                // Exit selector.
                break;
                
            } // Depending on direction facing...
        
        // Loop through and remove empty (null) tiles -- out of bounds entries.
        tiles.stream().filter((tile) -> (tile == null)).forEachOrdered((tile) -> {
            
            // Tile empty (null).
            
            // Add tile to removal list.
            removeList.add(tile);
        
        });
        
        // Clear actors in removal list -- remove them from the array list with the tiles.
        // Loop through actors in removal list.
        removeList.forEach((ba) -> {
            
            // Remove actor from array list.
            tiles.remove(ba);
        
        });
        
        // Return the array list with the base actors for the tiles.
        return tiles;
        
    }
    
    // pos_x = Y-coordinate of tile to render.
    // pos_y = X-coordinate of tile to render.
    // position = Position information for tile to render (offset within source graphic).
    // tiles = BaseActor objects associated with tiles.
    public BaseActor mazemap_render_tile(int pos_x, int pos_y, int position)
    {
        // The function returns a base actor representing the passed tile.
        // Note: x, y flipped to ease map making.
        
        int tileNbr; // Tile number to render.
        String key; // Key for the tile to display -- in the asset manager.
        
        // If tile exists in current region, then...
        if (mazemap_bounds_check(pos_x, pos_y))
        {
            
            // Tile exists in current region.
            
            // Determine tile number ot render.
            tileNbr = regionTiles.get(pos_y).get(pos_x);
            
            // Store asset manager key for the tile to display.
            key = HeroineEnum.ImgTileEnum.valueOf(tileNbr).getValue_Key() + "_" + decimalFormat000.format(position);
            
            System.out.println("Rendering tile (tile, pos): " + tileNbr + ", " + position);
            
            //System.out.println("Tile_" + Integer.toString(tileNbr));
            //System.out.println("key = " + key);
            //System.out.println("tile nbr = " + tileNbr);
            //System.out.println("dest_x = " + dest_x[position]);
            
            // Return the base actor for the tile (possibly null if out of bounds).
            return new BaseActor("Tile_" + Integer.toString(tileNbr), 
              gameHD.getAssetMgr().getTextureRegion(key), dest_x[position], dest_y[position] );
            
        }
        
        else
        {
            
            // Tile outside current region.
            
            // Return null.
            return null;
            
        }
        
    }
    
    // Getters and setters below...
    
    public RegionMap getCurrentRegion() {
        return currentRegion;
    }
    
}
