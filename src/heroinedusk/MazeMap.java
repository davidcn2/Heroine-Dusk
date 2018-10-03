package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import core.BaseActor;

// Java imports.
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;

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
    
    mazemap_bounds_check:  Checks to see if the passed location exists in the current region.
    mazemap_render:  Returns the tiles used to render the passed location.
    mazemap_render_tile:  Returns a base actor representing the passed tile in the passed location.
    minimap_render:  Returns the icons used to render the minimap for the current map / region.
    */
    
    // Declare object variables.
    private final Atlas atlas; // Reference to the atlas information.
    private final AtlasItems atlasItems; // Reference to the atlas items information.
    private RegionMap currentRegion; // Reference to current region / map.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private final HashMap<HeroineEnum.MinimapCursorEnum, TextureRegion> minimapCursorRegions; // Unique set of
      // texture regions used with minimap cursors.
    private final HashMap<HeroineEnum.MinimapEnum, TextureRegion> minimapRegions; // Unique set of texture
      // regions used with minimap.
    private ArrayList<ArrayList<Float>> minimapDestX; // X-coordinates for destination of icons in stage.
    private ArrayList<ArrayList<Float>> minimapDestY; // Y-coordinates for destination of icons in stage.
    private final ArrayList<ArrayList<Integer>> regionTiles; // List of tiles composing the current region.
      // Example for use -- get(x).get(y):  Integer x = regionTiles.get(0).get(0);
    
    // Declare regular variables.
    private int current_id; // Current region / map location (number).
    private float dest_x[]; // Array holding X-coordinate (relative to the bottom left corner) to display
      // tile texture region in stage -- adjusted by scale factor.  Excludes render offset.
    private float dest_y[]; // Array holding Y-coordinate (relative to the bottom left corner) to display
      // tile texture region in stage -- adjusted by scale factor.  Excludes render offset.
    private float minimapIconSize; // Size of an icon in minimap (width and height match).
    private float minimapOffsetX; // X-coordinate of lower left corner of minimap.
    private float minimapOffsetY; // Y-coordinate of lower left corner of minimap.
    private float minimapHeight; // Height of the minimap, including the background.
    private float minimapWidth; // Width of the minimap, including the background.
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
    // map_id = Current map section in which player resides.
    // viewHeight = Height of the stage.
    public MazeMap(HeroineDuskGame hdg, int map_id, int viewHeight)
    {
        
        /*
        The constructor performs the following actions:
        
        1.  Initializes array lists and hash maps and allocate space for arrays.
        2.  Store reference to main game class.
        3.  Stores references to atlas region and item information.
        4.  Sets starting region / map location.
        5.  Stores region name.
        6.  Stores reference to current region / map.
        7.  Copies tiles for current region to class-level variable.
        8.  Stores region width and height -- in tiles.
        9.  Calculate and store destinations for tiles.
        10.  Populate hash maps with unique texture regions used with minimap.
        11.  Store minimap icon size.
        12.  Calculate minimap offset (lower left corner) based on scale.
        13.  Calculate destination coordinates for minimap icons.
        */
        
        ArrayList<Float> tempMinimapDestX; // Holder for x-coordinate values for current row (for placing minimap icons).
        ArrayList<Float> tempMinimapDestY; // Holder for y-coordinate values for current row (for placing minimap icons).
        float minimapDestPosX; // X-coordinate at which to place minimap icon.
        float minimapDestPosY; // Y-coordinate at which to place minimap icon.
        int counter; // Used to increment through tile region enumerations.
        
        // 1.  Initialize array lists.
        regionTiles = new ArrayList<>();
        minimapDestX = new ArrayList<>();
        minimapDestY = new ArrayList<>();
        
        // Initialize hash maps.
        minimapRegions = new HashMap<>();
        minimapCursorRegions = new HashMap<>();
        
        // Allocate space for arrays and array lists.
        dest_x = new float[HeroineEnum.TileRegionEnum.values().length];
        dest_y = new float[HeroineEnum.TileRegionEnum.values().length];
        
        // 2.  Store reference to main game class.
        gameHD = hdg;
        
        // 3.  Store reference to atlas region and items information.
        
        // Store reference to the atlas region information.
        this.atlas = gameHD.getAtlas();
        
        // Store reference to the atlas items information.
        this.atlasItems = gameHD.getAtlasItems();
        
        // 4.  Set starting region / map location.
        current_id = map_id; // 0;
        
        // 5.  Get region name.
        regionName = atlas.mapIdentifiersRev.get(current_id);
        
        // 6.  Store reference to current region / map.
        this.currentRegion = atlas.maps.get(regionName);
        
        System.out.println("Current region: " + regionName);
        
        // 7.  Copy tiles for current region. -- Actually, stores references.
        regionTiles.addAll(currentRegion.getRegionTiles());
        
        // 8.  Copy current region width and height -- in tiles.
        regionWidth = currentRegion.getRegionWidth();
        regionHeight = currentRegion.getRegionHeight();
        
        System.out.println("Region size: " + regionWidth + " by " + regionHeight);
        
        // 9.  Calculate and store destinations for tiles.
        
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
        
        // 10.  Populate hash maps with unique texture regions used with minimap.
        
        // Loop through minimap enumerated values.
        for (HeroineEnum.MinimapEnum minimapEnum : HeroineEnum.MinimapEnum.values())
        {
            
            // Add texture region to hash map.
            minimapRegions.put(minimapEnum, gameHD.getAssetMgr().getTextureRegion(minimapEnum.getValue_Key()));
            
        }
        
        // Loop through minimap cursor enumerated values.
        for (HeroineEnum.MinimapCursorEnum minimapCursorEnum : HeroineEnum.MinimapCursorEnum.values())
        {
            
            // Add texture region to hash map.
            minimapCursorRegions.put(minimapCursorEnum, 
              gameHD.getAssetMgr().getTextureRegion(minimapCursorEnum.getValue_Key()));
            
        }
        
        // 11.  Store minimap icon size.
        minimapIconSize = minimapRegions.get(HeroineEnum.MinimapEnum.MINIMAP_BLOCK_BLACK).getRegionWidth();
        
        // 12.  Calculate minimap offset (lower left corner) based on scale.
        minimapOffsetX = 2f * gameHD.getConfig().getScale();
        minimapOffsetY = viewHeight - (regionHeight * minimapIconSize) - (2f * gameHD.getConfig().getScale()); // 106f * gameHD.getConfig().getScale();
        
        // 13.  Calculate destination coordinates for minimap icons.
        
        // Set starting y-coordinate at which to place minimap icon (bottom of lowest icon).
        minimapDestPosY = 0f; // minimapOffsetY;
        
        // Loop through vertical tiles.
        for (int counterY = regionHeight - 1; counterY >= 0; counterY--)
        {
            
            // Reset x-coordinate at which to place minimap icon (left edge).
            minimapDestPosX = 0f; // minimapOffsetX;
            
            // Reinitialize array lists.
            tempMinimapDestX = new ArrayList<>();
            tempMinimapDestY = new ArrayList<>();
            
            // Loop through horizontal tiles.
            for (int counterX = 0; counterX < regionWidth; counterX++)
            {
                
                // Add location to array lists for current row.
                tempMinimapDestX.add(minimapDestPosX);
                tempMinimapDestY.add(minimapDestPosY);
                
                // Move to the right one icon.
                minimapDestPosX += minimapIconSize;
                
            } // End ... Loop through horizontal tiles.
            
            // Add array lists for current row to sets for all rows.
            minimapDestX.add(tempMinimapDestX);
            minimapDestY.add(tempMinimapDestY);
            
            // Move up one icon.
            minimapDestPosY += minimapIconSize;
            
        } // End ... Loop through vertical tiles.
        
        // Reverse rows in top-level array lists.
        Collections.reverse(minimapDestX);
        Collections.reverse(minimapDestY);
        
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
    public ArrayList<BaseActor> mazemap_render(int x, int y, HeroineEnum.FacingEnum facing)
    {
        
        /*
        The function returns the tiles used to render the passed location.
        
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
        // The function returns a base actor representing the passed tile in the passed location.
        // Note: x, y flipped to ease map making.
        
        int tileNbr; // Tile number to render.
        String key; // Key for the tile to display -- in the asset manager.
        
        // If tile exists in current region, then...
        if (mazemap_bounds_check(pos_x, pos_y))
        {
            
            // Tile exists in current region.
            
            // Determine tile number ot render.
            tileNbr = regionTiles.get(pos_y).get(pos_x);
            
            // If tile represents a placeholder, then...
            if (tileNbr == HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE.getValue())
            {
                
                // Tile represents a placeholder (empty location).
                
                // Return null.
                return null;
            }
            
            else
            {
                
                // Tile exists in current region and NOT a placeholder (empty location).
                
                //System.out.println("tileNbr = " + tileNbr);
                //System.out.println("position = " + position);
                
                // Store asset manager key for the tile to display.
                key = HeroineEnum.ImgTileEnum.valueOf(tileNbr).getValue_Key() + "_" + 
                  decimalFormat000.format(position);
                
                //System.out.println("Rendering tile (tile, pos): " + tileNbr + ", " + position);

                //System.out.println("Tile_" + Integer.toString(tileNbr));
                //System.out.println("key = " + key);
                //System.out.println("tile nbr = " + tileNbr);
                //System.out.println("dest_x = " + dest_x[position]);

                // Return the base actor for the tile (possibly null if out of bounds).
                return new BaseActor("Tile_" + Integer.toString(tileNbr), 
                  gameHD.getAssetMgr().getTextureRegion(key), dest_x[position], dest_y[position] );
                
            }
            
        }
        
        else
        {
            
            // Tile outside current region.
            
            // Return null.
            return null;
            
        }
        
    }
    
    public ArrayList<BaseActor> minimap_render()
    {
        
        /*
        The function returns the icons used to render the minimap for the current map / region.
        
        The setting up the minimap for rendering (populating the array list) involves the following:
        
        1.  Initialize array lists.
        2.  Add minimap background (included in icon list for simplicity).
        3.  Render (base) map.
        4.  Render exits.
        5.  Render shops.
        6.  Render minimap cursor.
        */
        
        int counter; // Used to increment through exits and shops.
        ArrayList<BaseActor> icons; // BaseActor objects associated with icons.
        int tileNbr; // Tile number for which to render icon  in minimap.
        boolean walkable; // Whether tile walkable.
        BaseActor minimapBackground; // BaseActor serving as the minimap background.
        HeroineEnum.MinimapEnum key; // Key to texture region in hash map for icon to display.
        HeroineEnum.MinimapCursorEnum keyCursor; // Key to texture region in hash map for cursor icon to display.
        
        // 1.  Initialize array lists.
        icons = new ArrayList<>();
        Color tempColor;
        tempColor = new Color(Color.LIGHT_GRAY.r, Color.LIGHT_GRAY.g, Color.LIGHT_GRAY.b, 0.50f);
        
        // 2. Add minimap background (included in icon list for simplicity).
        
        // Create and configure base actor for minimap background.
        minimapBackground = new BaseActor("Minimap_Background", 
          minimapRegions.get(HeroineEnum.MinimapEnum.MINIMAP_BLOCK_BLACK), 
          -3f, -3f );
        
        // Set width and height of minimap background.
        minimapWidth = (regionWidth * minimapIconSize) + 6f;
        minimapHeight = (regionHeight * minimapIconSize) + 6f;
        minimapBackground.setWidth( minimapWidth );
        minimapBackground.setHeight( minimapHeight );
        
        // Apply a partially transparent light gray shade to the background.
        minimapBackground.setColor(tempColor);
        
        // Add the base actor for the background to the array list.
        icons.add( minimapBackground );
        
        // 3.  Render (base) map.
        
        // Loop through vertical tiles.
        for (int counterY = 0; counterY < regionHeight; counterY++)
        {
            
            // Loop through horizontal tiles.
            for (int counterX = 0; counterX < regionWidth; counterX++)
            {
                
                // Determine tile number for which to render icon.
                tileNbr = regionTiles.get(counterY).get(counterX);
                
                // If NOT a placeholder tile, then...
                if (tileNbr != HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE.getValue())
                {
                    
                    // NOT a placeholder tile.
                    
                    // Determine while tile walkable.
                    walkable = HeroineEnum.ImgTileEnum.valueOf(tileNbr).getValue_Walkable();

                    /*
                    Walkable (key) = minimap_white
                    Not walkable (key) = minimap_black
                    */

                    // Determine key containing texture region for icon.

                    // If walkable tile, then...
                    if (walkable)
                        // Walkable tile.
                        key = HeroineEnum.MinimapEnum.MINIMAP_BLOCK_WHITE;
                    else
                        // Non-walkable tile.
                        key = HeroineEnum.MinimapEnum.MINIMAP_BLOCK_BLACK;

                    // Add base actor for current icon in loop.
                    icons.add( new BaseActor("Minimap_(" + Integer.toString(counterX) + "," + 
                      Integer.toString(counterY) + ")", minimapRegions.get(key), 
                      minimapDestX.get(counterY).get(counterX), minimapDestY.get(counterY).get(counterX) ) );
                    
                }
                
            }
                
        }
        
        // 4.  Render exits.
        
        // Set starting value for counter.
        counter = 0;
        
        // Determine key containing texture region for icon.      
        key = HeroineEnum.MinimapEnum.MINIMAP_BLOCK_BLUE;
        
        // Loop through exits in current region.
        for (RegionMap.RegionExit exit : currentRegion.getRegionExits())
        {
            
            // Add base actor for current exit icon in loop.
            icons.add( new BaseActor("Minimap_Exit_" + Integer.toString(counter), 
              minimapRegions.get(key), minimapDestX.get(exit.exit_y).get(exit.exit_x), 
              minimapDestY.get(exit.exit_y).get(exit.exit_x) ) );
            
        }
        
        // 5.  Render shops.
        
        // Set starting value for counter.
        counter = 0;
        
        // Determine key containing texture region for icon.      
        key = HeroineEnum.MinimapEnum.MINIMAP_BLOCK_DARK_BLUE;
        
        // Loop through shops in current region.
        for (RegionMap.RegionShop shop : currentRegion.getRegionShops())
        {
            
            // Add base actor for current shop icon in loop.
            icons.add( new BaseActor("Minimap_Shop_" + Integer.toString(counter), 
              minimapRegions.get(key), minimapDestX.get(shop.exit_y).get(shop.exit_x), 
              minimapDestY.get(shop.exit_y).get(shop.exit_x) ) );
            
        }
        
        // 6.  Render minimap cursor.
        
        // Determine key containing texture region for icon.      
        
        // Depending on direction avatar is facing...
        switch (gameHD.getAvatar().getFacing())    
        {
            
            case NORTH:
                
                // Facing north.
                
                // Set key containing texture region for icon to represent player facing north.      
                keyCursor = HeroineEnum.MinimapCursorEnum.MINIMAP_CURSOR_BLOCK_UP;
                
                // Exit selector.
                break;
                
            case SOUTH:
                
                // Facing south.
                
                // Set key containing texture region for icon to represent player facing south.      
                keyCursor = HeroineEnum.MinimapCursorEnum.MINIMAP_CURSOR_BLOCK_DOWN;
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Facing east.
                
                // Set key containing texture region for icon to represent player facing east.      
                keyCursor = HeroineEnum.MinimapCursorEnum.MINIMAP_CURSOR_BLOCK_RIGHT;
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Facing west.
                
                // Set key containing texture region for icon to represent player facing west.      
                keyCursor = HeroineEnum.MinimapCursorEnum.MINIMAP_CURSOR_BLOCK_LEFT;
                
                // Exit selector.
                break;
                
            default:
                
                // Set key to default.
                keyCursor = HeroineEnum.MinimapCursorEnum.MINIMAP_CURSOR_BLOCK_LEFT;
                
                // Display warning.
                System.out.println("Warning:  Facing unknown direction while rendering minimap!");
                
                // Exit selector.
                break;
                
        }
        
        // Add base actor for player icon.
        icons.add( new BaseActor("Minimap_Avatar", 
          minimapCursorRegions.get(keyCursor), minimapDestX.get(gameHD.getAvatar().getY()).get(gameHD.getAvatar().getX()), 
          minimapDestY.get(gameHD.getAvatar().getY()).get(gameHD.getAvatar().getX()) ) );
        
        // Return the array list with the base actors for the icons.
        return icons;

    }
    
    // Getters and setters below...
    
    public RegionMap getCurrentRegion() {
        return currentRegion;
    }
    
    public float getMinimapHeight() {
        return minimapHeight;
    }
    
    public float getMinimapOffsetX() {
        return minimapOffsetX;
    }

    public float getMinimapOffsetY() {
        return minimapOffsetY;
    }
    
    public float getMinimapWidth() {
        return minimapWidth;
    }
    
    public int getRegionHeight() {
        return regionHeight;
    }

    public int getRegionWidth() {
        return regionWidth;
    }
    
    // posX = X-coordinate at which to get tile information.
    // posY = Y-coordinate at which to get tile information.
    public HeroineEnum.ImgTileEnum getImgTileEnum(int posX, int posY) {
        // Returns the enumerated value for the tile at the passed location.
        // Reverses y and x to ease working with tiles.
        return HeroineEnum.ImgTileEnum.valueOf(regionTiles.get(posY).get(posX));
    }
    
}
