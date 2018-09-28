package heroinedusk;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    
    Inner classes include:
    
    regionExit:  Stores region exit information.
    regionShop:  Stores information for a shop in the region.
    
    Methods include:
    
    addRegionExit:  Adds a region exit (based on the passed information) to the list.
    addRegionShop:  Adds a shop to the region (based on the passed information) to the list.
    addTiles:  Adds a horizontal set of tiles.
    populateHashMap:  Populates the hash map containing all region information.
    */
    
    // Declare object variables.
    private ArrayList<HeroineEnum.EnemyEnum> enemyList; // List of enemies within the region.
    private Map<String, Object> mapJSON; // Hash map containing key / value pairs covering all region data -- 
      // used with JSON.
    private final ArrayList<regionExit> regionExits; // List of exits within the region.
    private final ArrayList<regionShop> regionShops; // List of shops within the region.
    private final ArrayList<ArrayList<Integer>> regionTiles; // List of tiles composing the region.
    
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
        
        // Initialize array list.
        enemyList = new ArrayList<>();
        regionExits = new ArrayList<>();
        regionShops = new ArrayList<>();
        regionTiles = new ArrayList<>();
        
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
    
    public class regionExit
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
        private regionExit(int exit_x, int exit_y, int dest_map, int dest_x, int dest_y)
        {
            
            // The constructor stores the region exit information in the class-level variables.
            
            // Store the passed region exit information in the class-level variables.
            this.exit_x = exit_x;
            this.exit_y = exit_y;
            this.dest_map = dest_map;
            this.dest_x = dest_x;
            this.dest_y = dest_y;
            
        }
        
    }
    
    public class regionShop
    {
        
        // The inner class stores information for a shop in the region.
        
        // Declare regular variables.
        protected int exit_x; // X-coordinate of the exit (in the current region).
        protected int exit_y; // Y-cooridnate of the exit (in the current region).
        protected HeroineEnum.ShopEnum shop_id; // Shop identifier.
        protected int dest_x; // X-coordinate of the exit (in the destination region).
        protected int dest_y; // Y-coordinate of the exit (in the destination region).
        
        // exit_x = X-coordinate of the shop.
        // exit_y = Y-cooridnate of the shop.
        // shop_id = Shop identifier.
        // dest_x = X-coordinate of the shop exit.
        // dest_y = Y-coordinate of the shop exit.
        private regionShop(int exit_x, int exit_y, HeroineEnum.ShopEnum shop_id, int dest_x, int dest_y)
        {
            
            // The constructor populates the class-level variables with information for a shop in the region.
            
            // Store the passed shop information in the class-level variables.
            this.exit_x = exit_x;
            this.exit_y = exit_y;
            this.shop_id = shop_id;
            this.dest_x = dest_x;
            this.dest_y = dest_y;
            
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
        regionExits.add(new regionExit(exit_x, exit_y, dest_map, dest_x, dest_y));
        
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
        regionShops.add(new regionShop(exit_x, exit_y, shop_id, dest_x, dest_y));
        
        // Increment shop count.
        shopCount++;
        
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
        Map<String, Object> mapJSON_RegionTileRows; // Hash map containing key / value pairs covering all 
          // region tile row data -- used with JSON.
        
        // Set defaults.
        counter = 1;
        
        // Initialize hash map.
        mapJSON = new HashMap<>();
        mapJSON_RegionEnemies = new HashMap<>();
        mapJSON_RegionExits = new HashMap<>();
        mapJSON_RegionShops = new HashMap<>();
        mapJSON_RegionTileRow = new HashMap<>();
        mapJSON_RegionTileRows = new HashMap<>();
        
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
            for (regionExit currRegionExit : regionExits)
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
            for (regionShop currRegionShop : regionShops)
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
    public regionExit getRegionExit(int whichExit) {
        // The function returns information for the specified region exit.
        return regionExits.get(whichExit);
    }
    
    public ArrayList<regionExit> getRegionExits() {
        return regionExits;
    }
    
    // whichShop = Index number of region shop to return.  Base 0.
    public regionShop getRegionShop(int whichShop) {
        return regionShops.get(whichShop);
    }
    
    public ArrayList<regionShop> getRegionShops() {
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
    
    // posX = X-position of tile for which to get type number.
    // posY = Y-position of tile for which to get type number.
    // val = Type number to which to set tile at the passed position.
    public void setRegionTileNbr(Integer posX, Integer posY, Integer val) {
        // The function sets the type number of the tile at the passed position.
        regionTiles.get(posX).set(posY, val);
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