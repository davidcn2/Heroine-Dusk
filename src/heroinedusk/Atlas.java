package heroinedusk;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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

public class Atlas 
{
    
    /* 
    The class stores map-related data.  An atlas contains one to many region / maps.
    
    Methods include:
    
    addMap:  Adds a region / map to the atlas.
    addRegionExit:  Adds a region exit (based on the passed information) to a region / map.
    addRegionShop:  Adds a shop (based on the passed information) to a region / map.
    addTiles:  Adds a horizontal set of tiles to a region / map.
    populateHashMap:  Populates the hash map containing all the atlas information.
    readHashMap:  Builds the entire atlas using the passed hash map.
    removeMap:  Removes the passed region / map from the atlas.
    */
    
    // Declare object variables.
    private RegionMap currRegion; // Reference to current region.  Simplifies addition of tiles, shops, exits, ...
    protected HashMap<String, Integer> mapIdentifiers; // Region name and identifier (number) cross reference.
    protected HashMap<Integer, String> mapIdentifiersRev; // Identifier (number) and region name cross reference.
    protected Map<String, Object> mapJSON; // Hash map containing key / value pairs covering all atlas data -- 
      // used with JSON.  Key = region name.
    protected HashMap<String, RegionMap> maps; // Information about each region.  Key = region name.
    
    // Declare regular variables.
    protected int mapCount; // Number of maps / regions.  Base 1.
    
    // Declare constants.
    private final String decFormatText00 = "00"; // Text used for decimal style used to format numbers as 00.
      // Examples:  1 > 01, 2 > 02, ...
    private final DecimalFormat decimalFormat00 = new DecimalFormat(decFormatText00); // Decimal style used to
      // format numbers as 00.  Examples:  1 > 01, 2 > 02, ...
    
    public Atlas()
    {
        
        // The constructor sets defaults and performs initialization.
        
        // Set defaults.
        mapCount = 0;
        
        // Initialize the hash maps.
        mapIdentifiers = new HashMap<>();
        mapIdentifiersRev = new HashMap<>();
        mapJSON = new HashMap<>();
        maps = new HashMap<>();
        
    }
    
    // regionName = Region name.
    // regionMusic = Region music.
    // regionWidth = Region width, in tiles.
    // regionHeight = Region height, in tiles.
    // regionBackground = Background image for the region.
    // enemies = List of enemies in region.
    public RegionMap addMap(String regionName, HeroineEnum.MusicEnum regionMusic, int regionWidth, 
      int regionHeight, HeroineEnum.ImgBackgroundEnum regionBackground, HeroineEnum.EnemyEnum ... enemies)
    {
        
        // The function adds a region / map to the atlas.
        
        // Create region / map.
        maps.put(regionName, new RegionMap(regionName, regionMusic, regionWidth, regionHeight, 
          regionBackground, mapCount, enemies));
        
        // Add cross reference entry.
        mapIdentifiers.put(regionName, mapCount);
        mapIdentifiersRev.put(mapCount, regionName);
        
        // Increment map count.
        mapCount++;
        
        // Store reference to region / map.
        currRegion = maps.get(regionName);
        
        // Return newly created region / map.
        return maps.get(regionName);
        
    }
    
    // regionName = Region name.
    public void removeMap(String regionName)
    {
        
        // The function removes the passed region / map from the atlas.
        // The function does not change the existing region numbers.
        
        // Remove map from atlas.
        maps.remove(regionName);
        mapIdentifiers.remove(regionName);
        
    }
    
    // regionName = Region name.
    // exit_x = X-coordinate of the exit (in the current region).
    // exit_y = Y-cooridnate of the exit (in the current region).
    // dest_map = Destination map (region) number -- where the exit goes.
    // dest_x = X-coordinate of the exit (in the destination region).
    // dest_y = Y-coordinate of the exit (in the destination region).
    public void addRegionExit(String regionName, int exit_x, int exit_y, int dest_map, int dest_x, int dest_y)
    {
        
        // The function adds a region exit (based on the passed information) to the specified region / map.
        
        // Add region exit to specified region / map.
        maps.get(regionName).addRegionExit(exit_x, exit_y, dest_map, dest_x, dest_y);
        
    }
    
    // exit_x = X-coordinate of the exit (in the current region).
    // exit_y = Y-cooridnate of the exit (in the current region).
    // dest_map = Destination map (region) number -- where the exit goes.
    // dest_x = X-coordinate of the exit (in the destination region).
    // dest_y = Y-coordinate of the exit (in the destination region).
    public void addRegionExit(int exit_x, int exit_y, int dest_map, int dest_x, int dest_y)
    {
        
        // The function adds a region exit (based on the passed information) to the current region / map.
        
        // Add region exit to current region / map.
        currRegion.addRegionExit(exit_x, exit_y, dest_map, dest_x, dest_y);
        
    }
    
    // regionName = Region name.
    // exit_x = X-coordinate of the shop.
    // exit_y = Y-cooridnate of the shop.
    // shop_id = Shop identifier.
    // dest_x = X-coordinate of the shop exit.
    // dest_y = Y-coordinate of the shop exit.
    public void addRegionShop(String regionName, int exit_x, int exit_y, HeroineEnum.ShopEnum shop_id, int dest_x, 
      int dest_y)
    {
        
        // The function adds a shop (based on the passed information) to the specified region / map.
        
        // Add shop to specified region / map.
        maps.get(regionName).addRegionShop(exit_x, exit_y, shop_id, dest_x, dest_y);
        
    }
    
    // exit_x = X-coordinate of the exit (in the current region).
    // exit_y = Y-cooridnate of the exit (in the current region).
    // shop_id = Shop identifier.
    // dest_x = X-coordinate of the exit (in the destination region).
    // dest_y = Y-coordinate of the exit (in the destination region).
    public void addRegionShop(int exit_x, int exit_y, HeroineEnum.ShopEnum shop_id, int dest_x, int dest_y)
    {
        
        // The function adds a shop (based on the passed information) to the current region / map.
        
        // Add shop to current region / map.
        currRegion.addRegionShop(exit_x, exit_y, shop_id, dest_x, dest_y);
        
    }
    
    // regionName = Region name.
    // tiles = Tiles to include in horizontal set.
    public void addTiles(String regionName, Integer ... tiles)
    {
        
        // The function adds a horizontal set of tiles to the specified region / map.
        
        // Add passed tiles to specified region / map.
        maps.get(regionName).addTiles(tiles);
        
    }
    
    // tiles = Tiles to include in horizontal set.
    public void addTiles(Integer ... tiles)
    {
        
        // The function adds a horizontal set of tiles to the current region / map.
        
        // Add passed tiles to current region / map.
        currRegion.addTiles(tiles);
        
    }
    
    // tiles = Tiles to include in horizontal set.
    public void addTiles(ArrayList tiles)
    {
        
        // The function adds a horizontal set of tiles to the current region / map.
        
        // Add passed tiles to current region / map.
        currRegion.addTiles(tiles);
        
    }
    
    public void populateHashMap()
    {
        
        // The function populates the hash map containing all the atlas information.
        
        int counter; // Used to iterate through regions.
        RegionMap currentRegion; // Current region in loop.
        Set<Map.Entry<String, RegionMap>> entrySet; // Set view of the mappings in the hash map for all regions.
        
        // Set defaults.
        counter = 1;
        
        // Store a set view of the mappings for the hash map for all regions.
        entrySet = maps.entrySet();
        
        // Loop through regions.
        for (Entry entry : entrySet)
        {
            // Get a reference to the current region.
            currentRegion = (RegionMap)entry.getValue();
            
            // Populate the hash map for the current region.
            currentRegion.populateHashMap();
            
            // Add the hash map for the current region to the object containing all.
            mapJSON.put("MAP_" + decimalFormat00.format(currentRegion.getRegionNbr()), currentRegion.getMap());
            
            // Increment region counter.
            counter++;
        }
        
    }
    
    // mapJSONBuild = Hash map containing data for the entire atlas.
    public void readHashMap(Map<String, Object> mapJSONBuild)
    {
        
        // The function builds the entire atlas using the passed hash map.
        
        // Declare object variables.
        HeroineEnum.EnemyEnum[] enemyEnums; // Array containing enemies for current region in loop.
        Set<Map.Entry<String, Object>> entrySet; // Set view of all of the mappings in the hash map for all regions.
        Set<Map.Entry<String, String>> entrySetEnemies; // Set view of the enemy mappings in the hash map for current region in loop.
        Set<Map.Entry<String, Object>> entrySetExits; // Set view of the exit mappings in the hash map for current region in loop.
        Set<Map.Entry<String, Object>> entrySetShops; // Set view of the exit mappings in the hash map for current region in loop.
        HashMap<String, Integer> exitData; // Hash map containing information for a single exit.
        HashMap<String, Object> exitMap; // Hash map containing information for exits in current region in loop.
        HashMap<String, Object> shopData; // Hash map containing information for a single shop.
        HashMap<String, Object> shopMap; // Hash map containing information for shops in current region in loop.
        HashMap<String, Object> temp; // Hash map containing information for current region in loop.
        ArrayList tileRow; // Array list containing information for all tiles in current row and region in loops.
        
        // Declare regular variables.
        int enemyCount; // Number of enemies in current region.
        int enemyCounter; // Used to increment through enemies.
        int exitCount; // Number of exits in current region.
        int mapCountFile; // Map count read from file.
        int regionHeight; // Region height, in tiles.  Same as tile row count.
        String regionName; // Name of current region in loop.
        int shopCount; // Number of shops in current region.
        String tileRowKey; // Key in hash map for current tile row in loop.
        
        // Reset defaults.
        mapCount = 0;
        
        // Reinitialize atlas.
        mapIdentifiers = new HashMap<>();
        mapIdentifiersRev = new HashMap<>();
        mapJSON = new HashMap<>();
        maps = new HashMap<>();
        
        // Populate the region count.
        mapCountFile = (int)mapJSONBuild.get("mapCount");
        
        // Populate region name and identifier cross reference.
        //mapIdentifiers.putAll((HashMap)mapJSONBuild.get("mapIdentifiers"));
        
        // Store a set view of all of the mappings for the hash map for all regions.
        entrySet = ((HashMap)mapJSONBuild.get("maps")).entrySet();
        
        // Loop through regions.
        for (Entry entry : entrySet)
        {
            
            // Reinitialize hash maps containing information for current region.
            temp = new HashMap<>();
            exitMap = new HashMap<>();
            shopMap = new HashMap<>();
            
            // Get a reference to the current region.
            
            // Copy all data for current region into hash map. 
            temp.putAll((HashMap)entry.getValue());
            
            // Store name of current region in loop.
            regionName = (String)temp.get("regionName");
            
            // Store number of tile rows in current region in loop -- same as region height.
            regionHeight = (int)temp.get("regionHeight");
            
            // Store number of enemies in current region in loop.
            enemyCount = (int)temp.get("enemyCount");
            
            // Initialize enemy enumeration array.
            enemyEnums = new HeroineEnum.EnemyEnum[enemyCount];
            
            // If one or more enemies exist in current region, then...
            if (enemyCount > 0)
            {
                
                // One or more enemies exist in current region.
                
                // Reset counter.
                enemyCounter = 0;
                
                // Store a set view of the enemy mappings for the hash map for the current regions.
                entrySetEnemies = ((HashMap)temp.get("enemies")).entrySet();
                
                // Loop through enemies.
                for (Entry entryEnemy : entrySetEnemies)
                {
                
                    // Add the enemy to the array.
                    enemyEnums[enemyCounter] = HeroineEnum.EnemyEnum.valueOf((String)entryEnemy.getValue());
                    
                    // Increment counter.
                    enemyCounter++;
                    
                }
                
            } // End ... If one or more enemies exist in current region.
            
            // Add current region to atlas.
            addMap(regionName, 
              HeroineEnum.MusicEnum.valueOf((String)temp.get("regionMusic")), 
              (int)temp.get("regionWidth"), regionHeight, 
              HeroineEnum.ImgBackgroundEnum.valueOf((String)temp.get("regionBackground")), enemyEnums);
            
            // Store number of exits in current region in loop.
            exitCount = (int)temp.get("exitCount");
            
            // If one or more exits exist in current region, then...
            if (exitCount > 0)
            {
                
                // One or more exits exist in current region.
                
                // Copy all data for exits in current region into hash map. 
                exitMap.putAll((HashMap)temp.get("exits"));
                
                // Store a set view of the exit mappings for the hash map for the current regions.
                entrySetExits = exitMap.entrySet();
                
                // Loop through exits in current region.
                for (Entry entryExit : entrySetExits)
                {
                    
                    // Reinitialize hash map for single exit.
                    exitData = new HashMap<>();
                    
                    // Copy all data for current exit in loop to hash map.
                    exitData.putAll((HashMap)entryExit.getValue());
                    
                    // Add the exit to the region.
                    addRegionExit((int)exitData.get("EXIT_X"), (int)exitData.get("EXIT_Y"), 
                      (int)exitData.get("DEST_MAP"), (int)exitData.get("DEST_X"), (int)exitData.get("DEST_Y"));
                    
                }
                
            } // End ... If one or more exits exist in current region.
            
            // Store number of shops in current region in loop.
            shopCount = (int)temp.get("shopCount");
            
            // If one or more shops exist in current region, then...
            if (shopCount > 0)
            {
                
                // One or more shops exist in current region.
                
                // Copy all data for shops in current region into hash map. 
                shopMap.putAll((HashMap)temp.get("shops"));
                
                // Store a set view of the shop mappings for the hash map for the current regions.
                entrySetShops = shopMap.entrySet();
                
                // Loop through shops in current region.
                for (Entry entryShop : entrySetShops)
                {
                    
                    // Reinitialize hash map for single shop.
                    shopData = new HashMap<>();
                    
                    // Copy all data for current shop in loop to hash map.
                    shopData.putAll((HashMap)entryShop.getValue());
                    
                    // Add the shop to the region.
                    addRegionShop((int)shopData.get("EXIT_X"), (int)shopData.get("EXIT_Y"), 
                      HeroineEnum.ShopEnum.valueOf((String)shopData.get("SHOP_ID")), 
                      (int)shopData.get("DEST_X"), (int)shopData.get("DEST_Y"));
                    
                }
                
            } // End ... If one or more shops exist in current region.
            
            // Loop through tile rows in current region.
            for (int tileRowCounter = 1; tileRowCounter <= regionHeight; tileRowCounter++)
            {
                
                // Store key to use with hash map to get data for current tile row data.
                tileRowKey = "ROW_" + decimalFormat00.format(tileRowCounter);
                
                // Store information for current row / region in the array list.
                tileRow = (ArrayList)((HashMap)temp.get("tiles")).get(tileRowKey);
                
                // Add row of tiles to current region data.
                addTiles(tileRow);
                
            }
            
            /*
            System.out.println("\nRegion Name: " + maps.get(regionName).getRegionName());
            System.out.println("Region Music: " + maps.get(regionName).getRegionMusic());
            System.out.println("Region Width: " + maps.get(regionName).getRegionWidth());
            System.out.println("Region Height: " + maps.get(regionName).getRegionHeight());
            System.out.println("Region Background: " + maps.get(regionName).getRegionBackground());
            System.out.println("Enemy Count: " + maps.get(regionName).getEnemyCount());
            System.out.println("Exit Count (File): " + exitCount);
            System.out.println("Exit Count (Atlas): " + maps.get(regionName).getExitCount());
            System.out.println("Exit Dest 1: " + maps.get(regionName).getRegionExit(0).dest_map);
            System.out.println("Shop Count (File): " + shopCount);
            System.out.println("Shop Count (Atlas): " + maps.get(regionName).getShopCount());
            System.out.println("Tile row count: " + maps.get(regionName).getRegionTiles().size());
            if (maps.get(regionName).getShopCount() > 0)
                System.out.println("Shop Dest 1: " + maps.get(regionName).getRegionShop(0).shop_id);
            
            if (enemyEnums.length > 0)
            {
                System.out.println("Enemy 1: " + enemyEnums[0]);
            }
            System.out.println("Tile 1: " + maps.get(regionName).getRegionTiles().get(0));
            */
            
        } // End ... Loop through regions.
        
    }
    
    // Getters and setters below...
    
    public int getMapCount() {
        return mapCount;
    }
    
    public Map<String, Object> getMapJSON() {
        return mapJSON;
    }
    
}
