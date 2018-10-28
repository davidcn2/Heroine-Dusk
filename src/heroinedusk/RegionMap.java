package heroinedusk;

// Java imports.
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

public class RegionMap 
{
    
    /* 
    The class stores map-related data, except for items.  One to many region maps exist in an atlas.
    Note:  The single wall tiles actually represent what occurs in the adjacent tile.
    
    Inner classes include:
    
    RegionExit:  Stores region exit information.
    RegionShop:  Stores information for a shop in the region.
    
    Methods include:
    
    addRegionExit:  Adds a region exit (based on the passed information) to the list.
    addRegionShop:  Adds a shop to the region (based on the passed information) to the list.
    addTile_East:  Adds an element to the array list with details about the east sides of map locations.
    addTile_North:  Adds an element to the array list with details about the north sides of map locations.
    addTile_South:  Adds an element to the array list with details about the south sides of map locations.
    addTile_West:  Adds an element to the array list with details about the west sides of map locations.
    addTiles:  Adds a horizontal set of tiles.
    populateHashMap:  Populates the hash map containing all region information.
    */
    
    // Declare object variables.
    private ArrayList<HeroineEnum.EnemyEnum> enemyList; // List of enemies within the region.
    private Map<String, Object> mapJSON; // Hash map containing key / value pairs covering all region data -- 
      // used with JSON.
    private final ArrayList<RegionExit> regionExits; // List of exits within the region.
    private final ArrayList<RegionShop> regionShops; // List of shops within the region.
    private final ArrayList<ArrayList<Integer>> regionTiles; // List of tiles composing the region.
    private final HashMap<String, RegionLocSide> sideTilesEast; // List of locations with view on east side.  Key = location (x, y).  Example:  3, 5.
    private final HashMap<String, RegionLocSide> sideTilesNorth; // List of locations with view on north side.  Key = location (x, y).  Example:  3, 5.
    private final HashMap<String, RegionLocSide> sideTilesSouth; // List of locations with view on south side.  Key = location (x, y).  Example:  3, 5.
    private final HashMap<String, RegionLocSide> sideTilesWest; // List of locations with view on west side.  Key = location (x, y).  Example:  3, 5.
    
    // Declare regular variables.
    private final int enemyCount; // Enemy count.
    private int exitCount; // Exit count.
    private HeroineEnum.ImgBackgroundEnum regionBackground; // Background image for the region.
    private int regionHeight; // Region height, in tiles.
    private HeroineEnum.MusicEnum regionMusic; // Region music.
    private String regionName; // Region name.
    private final int regionNbr; // Region number.  Base 0.
    private int regionWidth; // Region width, in tiles.
    private int shopCount; // Shop count.
    private int sideTilesEastCount; // Number of locations with view on east side.
    private int sideTilesNorthCount; // Number of locations with view on north side.
    private int sideTilesSouthCount; // Number of locations with view on south side.
    private int sideTilesWestCount; // Number of locations with view on east side.
    
    // Declare constants.
    private final String decFormatText00 = "00"; // Text used for decimal style used to format numbers as 00.
      // Examples:  1 > 01, 2 > 02, ...
    private final DecimalFormat decimalFormat00 = new DecimalFormat(decFormatText00); // Decimal style used to
      // format numbers as 00.  Examples:  1 > 01, 2 > 02, ...
    
    // Constructors below...
    
    // regionName = Region name.
    // regionMusic = Region music.
    // regionWidth = Region width, in tiles.
    // regionHeight = Region height, in tiles.
    // regionBackground = Background image for the region.
    // regionNbr = Region number, base 0.
    // enemies = List of enemies in region.
    public RegionMap(String regionName, HeroineEnum.MusicEnum regionMusic, int regionWidth, int regionHeight,
      HeroineEnum.ImgBackgroundEnum regionBackground, int regionNbr, HeroineEnum.EnemyEnum ... enemies)
    {
        
        // The constructor stores the name, music, width, height, background, and enemies for the region.
        
        // Set defaults.
        exitCount = 0;
        shopCount = 0;
        sideTilesNorthCount = 0;
        sideTilesSouthCount = 0;
        sideTilesEastCount = 0;
        sideTilesWestCount = 0;
        
        // Initialize array list.
        enemyList = new ArrayList<>();
        regionExits = new ArrayList<>();
        regionShops = new ArrayList<>();
        regionTiles = new ArrayList<>();
        sideTilesNorth = new HashMap<>();
        sideTilesSouth = new HashMap<>();
        sideTilesEast = new HashMap<>();
        sideTilesWest = new HashMap<>();
        
        // Store values for the region.
        this.regionName = regionName;
        this.regionMusic = regionMusic;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
        this.regionBackground = regionBackground;
        this.regionNbr = regionNbr;
        
        // Copy from passed array to array list.
        enemyList = new ArrayList<>(Arrays.asList(enemies));
        
        // Store enemy count.
        enemyCount = enemyList.size();
        
    }
    
    // Inner classes below...
    
    public class RegionExit
    {
        
        // The inner class stores region exit information.
        
        // Declare regular variables.
        protected int exit_x; // X-coordinate of the exit (in the current region).
        protected int exit_y; // Y-cooridnate of the exit (in the current region).
        protected int dest_map; // Destination map (region) number -- where the exit goes.
        protected int dest_x; // X-coordinate of the exit (in the destination region).
        protected int dest_y; // Y-coordinate of the exit (in the destination region).
        
        // exit_x = X-coordinate of the exit (in the current region).
        // exit_y = Y-cooridnate of the exit (in the current region).
        // dest_map = Destination map (region) number -- where the exit goes.
        // dest_x = X-coordinate of the exit (in the destination region).
        // dest_y = Y-coordinate of the exit (in the destination region).
        private RegionExit(int exit_x, int exit_y, int dest_map, int dest_x, int dest_y)
        {
            
            // The constructor stores the region exit information in the class-level variables.
            
            // Store the passed region exit information in the class-level variables.
            this.exit_x = exit_x;
            this.exit_y = exit_y;
            this.dest_map = dest_map;
            this.dest_x = dest_x;
            this.dest_y = dest_y;
            
        }
        
        public int getDest_map() {
            return dest_map;
        }

        public int getDest_x() {
            return dest_x;
        }

        public int getDest_y() {
            return dest_y;
        }
        
    }
    
    public class RegionLocSide
    {
        
        // The inner class stores information about the side of a location.
        
        // Declare regular variables.
        protected int x; // X-coordinate.
        protected int y; // Y-cooridnate.
        protected int tile; // Tile type.
        
        // x = X-coordinate.
        // y = Y-coordinate.
        // tile = Tile type.
        private RegionLocSide(int x, int y, int tile)
        {
            
            // The constructor stores the location side information in the class-level variables.
            
            // Store the passed location side information in the class-level variables.
            this.x = x;
            this.y = y;
            this.tile = tile;
            
        }
        
        public int getTile() {
            return tile;
        }

        public void setTile(int tile) {
            this.tile = tile;
        }
        
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
        
    }
    
    public class RegionShop
    {
        
        // The inner class stores information for a shop in the region.
        
        // Declare regular variables.
        protected int exit_x; // X-coordinate of the shop (in the current region).
        protected int exit_y; // Y-cooridnate of the shop (in the current region).
        protected HeroineEnum.ShopEnum shop_id; // Shop identifier.
        protected int dest_x; // X-coordinate of the shop exit (in the destination region).
        protected int dest_y; // Y-coordinate of the shop exit (in the destination region).
        
        // exit_x = X-coordinate of the shop.
        // exit_y = Y-cooridnate of the shop.
        // shop_id = Shop identifier.
        // dest_x = X-coordinate of the shop exit.
        // dest_y = Y-coordinate of the shop exit.
        private RegionShop(int exit_x, int exit_y, HeroineEnum.ShopEnum shop_id, int dest_x, int dest_y)
        {
            
            // The constructor populates the class-level variables with information for a shop in the region.
            
            // Store the passed shop information in the class-level variables.
            this.exit_x = exit_x;
            this.exit_y = exit_y;
            this.shop_id = shop_id;
            this.dest_x = dest_x;
            this.dest_y = dest_y;
            
        }
        
        public HeroineEnum.ShopEnum getShop_id() {
            return shop_id;
        }

        public int getDest_x() {
            return dest_x;
        }

        public int getDest_y() {
            return dest_y;
        }
        
    }
    
    // Methods below...
    
    // exit_x = X-coordinate of the exit (in the current region).
    // exit_y = Y-cooridnate of the exit (in the current region).
    // dest_map = Destination map (region) number -- where the exit goes.
    // dest_x = X-coordinate of the exit (in the destination region).
    // dest_y = Y-coordinate of the exit (in the destination region).
    public void addRegionExit(int exit_x, int exit_y, int dest_map, int dest_x, int dest_y)
    {
        
        // The function adds a region exit (based on the passed information) to the list.
        
        // Add region exit.
        regionExits.add(new RegionExit(exit_x, exit_y, dest_map, dest_x, dest_y));
        
        // Increment exit count.
        exitCount++;
        
    }
    
    // exit_x = X-coordinate of the shop.
    // exit_y = Y-cooridnate of the shop.
    // shop_id = Shop identifier.
    // dest_x = X-coordinate of the shop exit.
    // dest_y = Y-coordinate of the shop exit.
    public void addRegionShop(int exit_x, int exit_y, HeroineEnum.ShopEnum shop_id, int dest_x, int dest_y)
    {
        
        // The function adds a shop to the region (based on the passed information) to the list.
        
        // Add shop to the region.
        regionShops.add(new RegionShop(exit_x, exit_y, shop_id, dest_x, dest_y));
        
        // Increment shop count.
        shopCount++;
        
    }
    
    // x = X-coordinate.
    // y = Y-coordinate.
    // tile = Tile type to show on east side.
    public void addTile_East(int x, int y, int tile)
    {
        
        // The function adds an element to the array list with details about the east sides of map locations.
        
        String key; // Key to use with hash map.
        
        // Store key.
        key = Integer.toString(x) + ", " + Integer.toString(y);
        
        // Add information about east side of a map location.
        sideTilesEast.put(key, new RegionLocSide(x, y, tile));
        
        // Increment related count.
        sideTilesEastCount++;
        
    }
     
    // x = X-coordinate.
    // y = Y-coordinate.
    // tile = Tile type to show on east side.
    public void addTile_North(int x, int y, int tile)
    {
        
        // The function adds an element to the array list with details about the north sides of map locations.
        
        String key; // Key to use with hash map.
        
        // Store key.
        key = Integer.toString(x) + ", " + Integer.toString(y);
        
        // Add information about north side of a map location.
        sideTilesNorth.put(key, new RegionLocSide(x, y, tile));
        
        // Increment related count.
        sideTilesNorthCount++;
        
    }
    
    // x = X-coordinate.
    // y = Y-coordinate.
    // tile = Tile type to show on east side.
    public void addTile_South(int x, int y, int tile)
    {
        
        // The function adds an element to the array list with details about the south sides of map locations.
        
        String key; // Key to use with hash map.
        
        // Store key.
        key = Integer.toString(x) + ", " + Integer.toString(y);
        
        // Add information about south side of a map location.
        sideTilesSouth.put(key, new RegionLocSide(x, y, tile));
        
        // Increment related count.
        sideTilesSouthCount++;
        
    }
    
    // x = X-coordinate.
    // y = Y-coordinate.
    // tile = Tile type to show on east side.
    public void addTile_West(int x, int y, int tile)
    {
        
        // The function adds an element to the array list with details about the west sides of map locations.
        
        String key; // Key to use with hash map.
        
        // Store key.
        key = Integer.toString(x) + ", " + Integer.toString(y);
        
        // Add information about west side of a map location.
        sideTilesWest.put(key, new RegionLocSide(x, y, tile));
        
        // Increment related count.
        sideTilesWestCount++;
        
    }
    
    // tiles = Tiles to include in horizontal set.
    public void addTiles(Integer ... tiles)
    {
        
        // The function adds a horizontal set of tiles.
        
        ArrayList<Integer> tempTiles; // Temporarily holds set of tiles for addition to main array list.
        
        // Copy from passed array to array list.
        tempTiles = new ArrayList<>(Arrays.asList(tiles));
        
        // Add to complete tile array list.
        regionTiles.add(tempTiles);
        
    }
    
    // tiles = Tiles to include in horizontal set.
    public void addTiles(ArrayList tiles)
    {
        
        // The function adds a horizontal set of tiles.
        
        // Add to complete tile array list.
        regionTiles.add(tiles);
        
    }
    
    public void populateHashMap()
    {
        
        // The function populates the hash map containing all region information.
        
        int counter; // Used to iterate through regions.
        
        Set<Map.Entry<String, RegionLocSide>> entrySet; // Set view of the mappings in a hash map with tile 
          // side information.
        Map<String, Object> mapJSON_RegionEnemies; // Hash map containing key / value pairs covering all
          // region enemy data -- used with JSON.
        Map<String, Object> mapJSON_RegionExit; // Hash map containing key / value pairs covering current
          // region exit data (relative to loop) -- used with JSON.
        Map<String, Object> mapJSON_RegionExits; // Hash map containing key / value pairs covering all 
          // region exit data -- used with JSON.
        Map<String, Object> mapJSON_RegionShop; // Hash map containing key / value pairs covering current
          // region shop data (relative to loop) -- used with JSON.
        Map<String, Object> mapJSON_RegionShops; // Hash map containing key / value pairs covering all 
          // region shop data -- used with JSON.
        Map<String, Object> mapJSON_RegionTileRow; // Hash map containing key / value pairs covering current
          // region tile row data (relative to loop) -- used with JSON.
        Map<String, Object> mapJSON_SideTilesEast; // Hash map containing key / value pairs covering current
          // region tile row data (relative to loop) -- used with JSON.  For east sides of tiles.
        Map<String, Object> mapJSON_SideTilesEastAll; // Hash map containing key / value pairs covering all
          // region tile row data (relative to loop) -- used with JSON.  For east sides of tiles.
        Map<String, Object> mapJSON_SideTilesNorth; // Hash map containing key / value pairs covering current
          // region tile row data (relative to loop) -- used with JSON.  For north sides of tiles.
        Map<String, Object> mapJSON_SideTilesNorthAll; // Hash map containing key / value pairs covering all
          // region tile row data (relative to loop) -- used with JSON.  For north sides of tiles.
        Map<String, Object> mapJSON_SideTilesSouth; // Hash map containing key / value pairs covering current
          // region tile row data (relative to loop) -- used with JSON.  For south sides of tiles.
        Map<String, Object> mapJSON_SideTilesSouthAll; // Hash map containing key / value pairs covering all
          // region tile row data (relative to loop) -- used with JSON.  For south sides of tiles.
        Map<String, Object> mapJSON_SideTilesWest; // Hash map containing key / value pairs covering current
          // region tile row data (relative to loop) -- used with JSON.  For west sides of tiles.
        Map<String, Object> mapJSON_SideTilesWestAll; // Hash map containing key / value pairs covering all
          // region tile row data (relative to loop) -- used with JSON.  For west sides of tiles.
        Map<String, Object> mapJSON_RegionTileRows; // Hash map containing key / value pairs covering all 
          // region tile row data -- used with JSON.
        RegionLocSide currRegionLocSide; // Current side tile in loop.
          
        // Set defaults.
        counter = 1;
        
        // Initialize hash map.
        mapJSON = new HashMap<>();
        mapJSON_RegionEnemies = new HashMap<>();
        mapJSON_RegionExits = new HashMap<>();
        mapJSON_RegionShops = new HashMap<>();
        mapJSON_RegionTileRow = new HashMap<>();
        mapJSON_RegionTileRows = new HashMap<>();
        mapJSON_SideTilesNorthAll = new HashMap<>();
        mapJSON_SideTilesSouthAll = new HashMap<>();
        mapJSON_SideTilesEastAll = new HashMap<>();
        mapJSON_SideTilesWestAll = new HashMap<>();
        
        // If region contains enemies, then...
        if (enemyCount > 0)    
        {
            
            // Region contains one or more enemies.
            
            // Loop through region enemies.
            for (HeroineEnum.EnemyEnum enemy : enemyList)
            {

                // Add information to hash map containing data for all region enemies.
                mapJSON_RegionEnemies.put("ENEMY_" + decimalFormat00.format(counter), enemy.toString());

                // Increment counter.
                counter++;

            }

            // Reset counter.
            counter = 1;
            
        }
        
        // If region contains exits, then...
        if (exitCount > 0)    
        {
            
            // Region contains one or more exits.
        
            // Loop through region exits.
            for (RegionExit currRegionExit : regionExits)
            {

                // Reinitialize hash map for current region exit.  Necessary or same data written for each entry.
                mapJSON_RegionExit = new HashMap<>();
                
                // Add information to hash map containing data for a single region exit.
                mapJSON_RegionExit.put("EXIT_X", currRegionExit.exit_x);
                mapJSON_RegionExit.put("EXIT_Y", currRegionExit.exit_y);
                mapJSON_RegionExit.put("DEST_MAP", currRegionExit.dest_map);
                mapJSON_RegionExit.put("DEST_X", currRegionExit.dest_x);
                mapJSON_RegionExit.put("DEST_Y", currRegionExit.dest_y);

                // Add hash map for current to container.
                mapJSON_RegionExits.put("EXIT_" + decimalFormat00.format(counter), mapJSON_RegionExit);

                // Increment counter.
                counter++;

            }

            // Reset counter.
            counter = 1;
            
        }
        
        // If region contains shops, then...
        if (shopCount > 0)
        {
            
            // Region contains one or more shops.
        
            // Loop through region exits.
            for (RegionShop currRegionShop : regionShops)
            {

                // Reinitialize hash map for current region shop.  Necessary or same data written for each entry.
                mapJSON_RegionShop = new HashMap<>();
                
                // Add information to hash map containing data for a single region exit.
                mapJSON_RegionShop.put("EXIT_X", currRegionShop.exit_x);
                mapJSON_RegionShop.put("EXIT_Y", currRegionShop.exit_y);
                mapJSON_RegionShop.put("SHOP_ID", currRegionShop.shop_id);
                mapJSON_RegionShop.put("DEST_X", currRegionShop.dest_x);
                mapJSON_RegionShop.put("DEST_Y", currRegionShop.dest_y);

                // Add hash map for current to container.
                mapJSON_RegionShops.put("SHOP_" + decimalFormat00.format(counter), mapJSON_RegionShop);

                // Increment counter.
                counter++;

            }
            
            // Reset counter.
            counter = 1;
        
        }
        
        // Loop through region tile rows.
        for (ArrayList tileRow : regionTiles)
        {

            // Add information to hash map containing data for all region tile rows.
            mapJSON_RegionTileRows.put("ROW_" + decimalFormat00.format(counter), tileRow);

            // Increment counter.
            counter++;

        }
        
        // Reset counter.
        counter = 1;
        
        // If region contains north side tiles, then...
        if (sideTilesNorthCount > 0)    
        {
            
            // Region contains one or more north side tiles.
        
            // Store a set view of the mappings for the hash map.
            entrySet = sideTilesNorth.entrySet();
            
            // Loop through entries.
            for (Map.Entry entry : entrySet)
            {
                
                // Get a reference to the value of the current entry.
                currRegionLocSide = (RegionLocSide)entry.getValue();
                
                // Reinitialize hash map for current side tile.  Necessary or same data written for each entry.
                mapJSON_SideTilesNorth = new HashMap<>();
                
                // Add information to hash map containing data for a single side tile.
                mapJSON_SideTilesNorth.put("X", currRegionLocSide.x);
                mapJSON_SideTilesNorth.put("Y", currRegionLocSide.y);
                mapJSON_SideTilesNorth.put("TILE", currRegionLocSide.tile);

                // Add hash map for current to container.
                mapJSON_SideTilesNorthAll.put("SIDE_TILE_" + decimalFormat00.format(counter), 
                  mapJSON_SideTilesNorth);
                
                // Increment counter.
                counter++;
                
            }
            
            // Reset counter.
            counter = 1;
            
        }
        
        // If region contains south side tiles, then...
        if (sideTilesSouthCount > 0)    
        {
            
            // Region contains one or more south side tiles.
        
            // Store a set view of the mappings for the hash map.
            entrySet = sideTilesSouth.entrySet();
            
            // Loop through entries.
            for (Map.Entry entry : entrySet)
            {
                
                // Get a reference to the value of the current entry.
                currRegionLocSide = (RegionLocSide)entry.getValue();
                
                // Reinitialize hash map for current side tile.  Necessary or same data written for each entry.
                mapJSON_SideTilesSouth = new HashMap<>();
                
                // Add information to hash map containing data for a single side tile.
                mapJSON_SideTilesSouth.put("X", currRegionLocSide.x);
                mapJSON_SideTilesSouth.put("Y", currRegionLocSide.y);
                mapJSON_SideTilesSouth.put("TILE", currRegionLocSide.tile);

                // Add hash map for current to container.
                mapJSON_SideTilesSouthAll.put("SIDE_TILE_" + decimalFormat00.format(counter), 
                  mapJSON_SideTilesSouth);
                
                // Increment counter.
                counter++;
                
            }
            
            // Reset counter.
            counter = 1;
            
        }
        
        // If region contains east side tiles, then...
        if (sideTilesEastCount > 0)    
        {
            
            // Region contains one or more east side tiles.
        
            // Store a set view of the mappings for the hash map.
            entrySet = sideTilesEast.entrySet();
            
            // Loop through entries.
            for (Map.Entry entry : entrySet)
            {
                
                // Get a reference to the value of the current entry.
                currRegionLocSide = (RegionLocSide)entry.getValue();
                
                // Reinitialize hash map for current side tile.  Necessary or same data written for each entry.
                mapJSON_SideTilesEast = new HashMap<>();
                
                // Add information to hash map containing data for a single side tile.
                mapJSON_SideTilesEast.put("X", currRegionLocSide.x);
                mapJSON_SideTilesEast.put("Y", currRegionLocSide.y);
                mapJSON_SideTilesEast.put("TILE", currRegionLocSide.tile);

                // Add hash map for current to container.
                mapJSON_SideTilesEastAll.put("SIDE_TILE_" + decimalFormat00.format(counter), 
                  mapJSON_SideTilesEast);
                
                // Increment counter.
                counter++;
                
            }
            
            // Reset counter.
            counter = 1;
            
        }
        
        // If region contains west side tiles, then...
        if (sideTilesWestCount > 0)    
        {
            
            // Region contains one or more west side tiles.
        
            // Store a set view of the mappings for the hash map.
            entrySet = sideTilesWest.entrySet();
            
            // Loop through entries.
            for (Map.Entry entry : entrySet)
            {
                
                // Get a reference to the value of the current entry.
                currRegionLocSide = (RegionLocSide)entry.getValue();
                
                // Reinitialize hash map for current side tile.  Necessary or same data written for each entry.
                mapJSON_SideTilesWest = new HashMap<>();
                
                // Add information to hash map containing data for a single side tile.
                mapJSON_SideTilesWest.put("X", currRegionLocSide.x);
                mapJSON_SideTilesWest.put("Y", currRegionLocSide.y);
                mapJSON_SideTilesWest.put("TILE", currRegionLocSide.tile);

                // Add hash map for current to container.
                mapJSON_SideTilesWestAll.put("SIDE_TILE_" + decimalFormat00.format(counter), 
                  mapJSON_SideTilesWest);
                
                // Increment counter.
                counter++;
                
            }
            
            // Reset counter.
            counter = 1;
            
        }
        
        // Add values for the region to hash map.
        mapJSON.put("regionName", regionName);
        mapJSON.put("regionNbr", regionNbr);
        mapJSON.put("regionMusic", regionMusic.toString());
        mapJSON.put("regionWidth", regionWidth);
        mapJSON.put("regionHeight", regionHeight);
        mapJSON.put("regionBackground", regionBackground.toString());
        mapJSON.put("enemyCount", enemyCount);
        mapJSON.put("exitCount", exitCount);
        mapJSON.put("shopCount", shopCount);
        mapJSON.put("sideTileCountNorth", sideTilesNorthCount);
        mapJSON.put("sideTileCountSouth", sideTilesSouthCount);
        mapJSON.put("sideTileCountEast", sideTilesEastCount);
        mapJSON.put("sideTileCountWest", sideTilesWestCount);
        mapJSON.put("tiles", mapJSON_RegionTileRows);
        
        // If one or more enemies exist in region, then...
        if (enemyCount > 0)
            mapJSON.put("enemies", mapJSON_RegionEnemies);
        
        // If one or more exits exist in region, then...
        if (exitCount > 0)    
            mapJSON.put("exits", mapJSON_RegionExits);
        
        // If one or more shops exist in region, then...
        if (shopCount > 0)
            mapJSON.put("shops", mapJSON_RegionShops);
        
        // If one or more north side tiles exist in region, then...
        if (sideTilesNorthCount > 0)
            mapJSON.put("north_side_tiles", mapJSON_SideTilesNorthAll);
        
        // If one or more south side tiles exist in region, then...
        if (sideTilesSouthCount > 0)
            mapJSON.put("south_side_tiles", mapJSON_SideTilesSouthAll);
        
        // If one or more east side tiles exist in region, then...
        if (sideTilesEastCount > 0)
            mapJSON.put("east_side_tiles", mapJSON_SideTilesEastAll);
        
        // If one or more west side tiles exist in region, then...
        if (sideTilesWestCount > 0)
            mapJSON.put("west_side_tiles", mapJSON_SideTilesWestAll);
        
    }
    
    // Getters and setters below...
    
    // 1.  Array lists.
    
    public ArrayList<HeroineEnum.EnemyEnum> getEnemyList() {
        return enemyList;
    }
    
    public Map<String, Object> getMap() {
        return mapJSON;
    }
    
    // whichExit = Index number of region exit to return.  Base 0.
    public RegionExit getRegionExit(int whichExit) {
        // The function returns information for the specified region exit.
        return regionExits.get(whichExit);
    }
    
    // posX = X-coordinate at which to get exit information.
    // posY = Y-coordinate at which to get exit information.
    public RegionExit getRegionExit(int posX, int posY) {
        
        // The function looks for an exit at the passed map location.
        // The function returns an exit if one exists.
        // Otherwise, the function returns null.
        
        RegionExit temp; // Holder for the exit at the map location.
        
        // Set defaults.
        temp = null;
        
        // Loop through exits in current region.
        for (RegionExit regionExit : regionExits)
        {
            
            // If exit exists at passed location, then...
            if (regionExit.exit_x == posX && regionExit.exit_y == posY)
            {
                
                // Exit exists at passed location.
                
                // Copy exit.
                temp = regionExit;
                
                // Exit loop.
                break;
                
            }
            
        }
        
        // Return exit (or null).
        return temp;
        
    }
    
    public ArrayList<RegionExit> getRegionExits() {
        return regionExits;
    }
    
    // whichShop = Index number of region shop to return.  Base 0.
    public RegionShop getRegionShop(int whichShop) {
        return regionShops.get(whichShop);
    }
    
    // posX = X-coordinate at which to get shop information.
    // posY = Y-coordinate at which to get shop information.
    public RegionShop getRegionShop(int posX, int posY) {
        
        // The function looks for a shop at the passed map location.
        // The function returns a shop if one exists.
        // Otherwise, the function returns null.
        
        RegionShop temp; // Holder for the shop at the map location.
        
        // Set defaults.
        temp = null;
        
        // Loop through shops in current region.
        for (RegionShop regionShop : regionShops)
        {
            
            // If shop exists at passed location, then...
            if (regionShop.exit_x == posX && regionShop.exit_y == posY)
            {
                
                // Shop exists at passed location.
                
                // Copy shop.
                temp = regionShop;
                
                // Exit loop.
                break;
                
            }
            
        }
        
        // Return shop (or null).
        return temp;
        
    }
    
    public ArrayList<RegionShop> getRegionShops() {
        return regionShops;
    }
    
    // posX = X-position of tile for which to get type number.
    // posY = Y-position of tile for which to get type number.
    public int getRegionTileNbr(Integer posX, Integer posY) {
        // The function returns the type number of the tile at the passed position.
        return regionTiles.get(posX).get(posY);
    }
    
    public ArrayList<ArrayList<Integer>> getRegionTiles() {
        return regionTiles;
    }
    
    public HashMap<String, RegionLocSide> getSideTilesEast() {
        return sideTilesEast;
    }
    
    public Integer getSideTilesEast(Integer posX, Integer posY) {
        if (sideTilesEast.get(Integer.toString(posX) + ", " + Integer.toString(posY)) != null)
            return sideTilesEast.get(Integer.toString(posX) + ", " + Integer.toString(posY)).tile;
        else
            return null;
    }
    
    public Integer getSideTilesEast(String key) {
        if (sideTilesEast.get(key) != null)
            return sideTilesEast.get(key).tile;
        else
            return null;
    }
    
    public HashMap<String, RegionLocSide> getSideTilesNorth() {
        return sideTilesNorth;
    }
    
    public Integer getSideTilesNorth(Integer posX, Integer posY) {
        if (sideTilesNorth.get(Integer.toString(posX) + ", " + Integer.toString(posY)) != null)
            return sideTilesNorth.get(Integer.toString(posX) + ", " + Integer.toString(posY)).tile;
        else
            return null;
    }
    
    public Integer getSideTilesNorth(String key) {
        if (sideTilesNorth.get(key) != null)
            return sideTilesNorth.get(key).tile;
        else
            return null;
    }
    
    public HashMap<String, RegionLocSide> getSideTilesSouth() {
        return sideTilesSouth;
    }
    
    public Integer getSideTilesSouth(Integer posX, Integer posY) {
        if (sideTilesSouth.get(Integer.toString(posX) + ", " + Integer.toString(posY)) != null)
            return sideTilesSouth.get(Integer.toString(posX) + ", " + Integer.toString(posY)).tile;
        else
            return null;
    }
    
    public Integer getSideTilesSouth(String key) {
        if (sideTilesSouth.get(key) != null)
            return sideTilesSouth.get(key).tile;
        else
            return null;
    }
    
    public HashMap<String, RegionLocSide> getSideTilesWest() {
        return sideTilesWest;
    }
    
    public Integer getSideTilesWest(Integer posX, Integer posY) {
        if (sideTilesWest.get(Integer.toString(posX) + ", " + Integer.toString(posY)) != null)
            return sideTilesWest.get(Integer.toString(posX) + ", " + Integer.toString(posY)).tile;
        else
            return null;
    }
    
    public Integer getSideTilesWest(String key) {
        if (sideTilesWest.get(key) != null)
            return sideTilesWest.get(key).tile;
        else
            return null;
    }
    
    // posX = X-position of tile for which to get type number.
    // posY = Y-position of tile for which to get type number.
    // val = Type number to which to set tile at the passed position.
    public void setRegionTileNbr(Integer posX, Integer posY, Integer val) {
        // The function sets the type number of the tile at the passed position.
        regionTiles.get(posX).set(posY, val);
    }
    
    // posX = X-position of tile for which to get type number.
    // posY = Y-position of tile for which to get type number.
    // val = Type number to which to set tile at the passed position.
    public void setRegionTileNbrEast(Integer posX, Integer posY, Integer val) {
        
        // The function sets the type number of the tile at the passed position.
        // Used with the information about the east side of tiles.
        
        String key; // Key to use with hash map.
        
        // Specify key.
        key = Integer.toString(posX) + ", " + Integer.toString(posY);
        
        // Update tile.
        sideTilesEast.get(key).setTile(val);
        
    }
    
    // posX = X-position of tile for which to get type number.
    // posY = Y-position of tile for which to get type number.
    // val = Type number to which to set tile at the passed position.
    public void setRegionTileNbrNorth(Integer posX, Integer posY, Integer val) {
        
        // The function sets the type number of the tile at the passed position.
        // Used with the information about the north side of tiles.
        
        String key; // Key to use with hash map.
        
        // Specify key.
        key = Integer.toString(posX) + ", " + Integer.toString(posY);
        
        // Update tile.
        sideTilesNorth.get(key).setTile(val);
        
    }
    
    // posX = X-position of tile for which to get type number.
    // posY = Y-position of tile for which to get type number.
    // val = Type number to which to set tile at the passed position.
    public void setRegionTileNbrSouth(Integer posX, Integer posY, Integer val) {
        
        // The function sets the type number of the tile at the passed position.
        // Used with the information about the south side of tiles.
        
        String key; // Key to use with hash map.
        
        // Specify key.
        key = Integer.toString(posX) + ", " + Integer.toString(posY);
        
        // Update tile.
        sideTilesSouth.get(key).setTile(val);
        
    }
    
    // posX = X-position of tile for which to get type number.
    // posY = Y-position of tile for which to get type number.
    // val = Type number to which to set tile at the passed position.
    public void setRegionTileNbrWest(Integer posX, Integer posY, Integer val) {
        
        // The function sets the type number of the tile at the passed position.
        // Used with the information about the west side of tiles.
        
        String key; // Key to use with hash map.
        
        // Specify key.
        key = Integer.toString(posX) + ", " + Integer.toString(posY);
        
        // Update tile.
        sideTilesWest.get(key).setTile(val);
        
    }
    
    // 2.  Regular variables.
    
    public int getEnemyCount() {
        return enemyCount;
    }
    
    public int getExitCount() {
        return exitCount;
    }
    
    public HeroineEnum.ImgBackgroundEnum getRegionBackground() {
        return regionBackground;
    }

    public void setRegionBackground(HeroineEnum.ImgBackgroundEnum regionBackground) {
        this.regionBackground = regionBackground;
    }

    public int getRegionHeight() {
        return regionHeight;
    }

    public void setRegionHeight(int regionHeight) {
        this.regionHeight = regionHeight;
    }

    public HeroineEnum.MusicEnum getRegionMusic() {
        return regionMusic;
    }

    public void setRegionMusic(HeroineEnum.MusicEnum regionMusic) {
        this.regionMusic = regionMusic;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getRegionNbr() {
        return regionNbr;
    }
    
    public int getRegionWidth() {
        return regionWidth;
    }

    public void setRegionWidth(int regionWidth) {
        this.regionWidth = regionWidth;
    }
    
    public int getShopCount() {
        return shopCount;
    }
    
}