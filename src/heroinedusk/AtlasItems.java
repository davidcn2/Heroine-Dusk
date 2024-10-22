package heroinedusk;

// Java imports.
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

public class AtlasItems 
{
    
    /* 
    The class stores item-related data for specific (region, x, y) locations.
    
    Inner classes include:
    
    AlterMap:  Store map alteration information (actually changes a tile).
    BonePile:  Stores bone pile information.
    Chest:  Stores chest information.
    HayBale:  Stores hay bale information.
    ItemCounts:  Stores the counts of each item type for a single region.
    LockedDoor:  Stores locked door information.
    SpecificEnemy:  Stores information about specific enemies and their locations.
    
    Methods include:
    
    addAlterMap:  Adds a map alteration event (based on the passed information) to the list.
    addBonePile:  Adds a bone pile (based on the passed information) to the list.
    addChest:  Adds a chest (based on the passed information) to the list.
    addHayBale:  Adds a hay bale (based on the passed information) to the list.
    addLockedDoor:  Adds a locked door (based on the passed information) to the list.
    addSpecificEnemy:  Adds an enemy at a specific location (based on the passed information) to the list.
    adjChestCount:  Adjusts chest quantity in variable, chestCount, by the passed parameter.
    adjItemCountChest:  Adjusts chest quantity in list variable, itemCountList, by the passed parameter.
    adjItemTotal:  Adjusts total item quantity in list variable, itemTotalList, by the passed parameter.
    populateHashMap:  Populates the hash map containing all region (specific location) information.
    readHashMap:  Builds the entire atlas using the passed hash map.
    removeBonePile:  Encapsulates logic for setting a bone pile (based on map / region and index) as 
      inactive in the array list.
    removeBonePileEntry:  Removes the bone pile based on the passed information from the hash map,
      mapRegionItems.
    removeBonePileFirst:  Encapsulates logic for removing the first bone pile from the passed map location.
    removeChest:  Encapsulates logic for setting a chest (based on map / region and index) as inactive in 
      the array list.
    removeChestEntry:  Removes the chest based on the passed information from the hash map, mapRegionItems.
    removeChests:  Encapsulates logic for removing (setting as inactive) the passed chests from the 
      specified region, including for the hash map.
    removeLockedDoor:  Encapsulates logic for setting a locked door (based on map / region and index) as 
      inactive in the array list.
    removeLockedDoorEntry:  Removes the locked door based on the passed information from the hash map, 
      mapRegionItems.
    removeLockedDoorFirst:  Encapsulates logic for removing the first locked door from the passed map
      location.
    storeRegionInfo:  Stores the hash maps and array lists with the information by region / location.
    */
    
    // Declare object variables.
    private ArrayList<AlterMap> alterMapList; // List of map alteration events.
    private ArrayList<BonePile> bonePileList; // List of bone piles.
    private ArrayList<Chest> chestList; // List of chests.
    private ArrayList<HayBale> hayBaleList; // List of chests.
    private ArrayList<ItemCounts> itemCountList; // List of counts by item for each region.
    private ArrayList<Integer> itemTotalList; // Total item count for each region.
    private ArrayList<LockedDoor> lockedDoorList; // List of locked doors.
    private Map<String, Object> mapJSON; // Hash map containing key / value pairs covering all atlas item 
      // data -- used with JSON.
    private HashMap<String, ArrayList<Object>> mapRegionItems; // Hash map containing item lists based on 
      // locations.
      // Key:  region,x,y,type.  Type matches one of the ItemCategoryEnum values.  Example:  4,1,14,ITEM_CTGY_BONE_PILE.
      // Value:  ArrayList of objects matching the item type in the key.
    public ArrayList<SpecificEnemy> specificEnemyList; // List of enemies at specific locations.
    
    // Declare regular variables.
    private int alterMapCount; // Map alteration event count.
    private int bonePileCount; // Bone pile count.
    private int chestCount; // Chest count.
    private int hayBaleCount; // Hay bale count.
    private int lockedDoorCount; // Locked door count.
    private int specificEnemyCount; // Number of enemies at specific locations.
    
    // Declare constants.
    private final String decFormatText00 = "00"; // Text used for decimal style used to format numbers as 00.
      // Examples:  1 > 01, 2 > 02, ...
    private final DecimalFormat decimalFormat00 = new DecimalFormat(decFormatText00); // Decimal style used to
      // format numbers as 00.  Examples:  1 > 01, 2 > 02, ...
    
    public AtlasItems()
    {
        
        // The constructor sets default values.
        
        // Set defaults.
        alterMapCount = 0;
        bonePileCount = 0;
        chestCount = 0;
        hayBaleCount = 0;
        lockedDoorCount = 0;
        specificEnemyCount = 0;
        
        // Initialize arrays and array lists.
        alterMapList = new ArrayList<>();
        bonePileList = new ArrayList<>();
        chestList = new ArrayList<>();
        hayBaleList = new ArrayList<>();
        itemCountList = new ArrayList<>();
        itemTotalList = new ArrayList<>();
        lockedDoorList = new ArrayList<>();
        specificEnemyList = new ArrayList<>();
        
    }
    
    // Inner classes below...
    
    public class AlterMap
    {
        
        // The inner class stores information about a map alteration event.
        // The event changes a tile at a specified location, with an optional condition.
        
        // Declare regular variables.
        private String campaignEvent; // Campaign event to check for in list.
        private boolean campaignEventInd; // Whether campaign event involved.
        private boolean campaignEventType; // If true, changes tile when event exists in list.
          // If false, changes tile when event does NOT exist in list.
        private final int pos_x; // X-coordinate of the tile to change (the array index).
        private final int pos_y; // Y-coordinate of the tile to change (the array index).
        private final int regionNbr; // Region number.  Base 0.
        private final int tileNbr; // New tile type.
        
        // pos_x = X-coordinate of the tile to change (the array index).
        // pos_y = Y-coordinate of the tile to change (the array index).
        // regionNbr = Region number.  Base 0.
        // tileNbr = New tile type.
        private AlterMap(int pos_x, int pos_y, int regionNbr, int tileNbr)
        {
            
            // The constructor stores the map alteration event, without a condition.
            
            // Store the passed map alteration event information in the class-level variables.
            this.pos_x = pos_x;
            this.pos_y = pos_y;
            this.regionNbr = regionNbr;
            this.tileNbr = tileNbr;
            this.campaignEventInd = false;
            
        }
        
        // pos_x = X-coordinate of the tile to change (the array index).
        // pos_y = Y-coordinate of the tile to change (the array index).
        // regionNbr = Region number.  Base 0.
        // tileNbr = New tile type.
        // campaignEvent = Campaign event to check for in list.
        // campaignEventType = If true, changes tile when event exists in list.
        //   If false, changes tile when event does NOT exist in list.
        private AlterMap(int pos_x, int pos_y, int regionNbr, int tileNbr, String campaignEvent, 
          boolean campaignEventType)
        {
            
            // The constructor stores the map alteration event, with a condition.
            
            // Call other constructor.
            this(pos_x, pos_y, regionNbr, tileNbr);
            
            // Store the passed map alteration event information in the class-level variables.
            this.campaignEvent = campaignEvent;
            this.campaignEventType = campaignEventType;
            this.campaignEventInd = true;
            
        }
        
        public String getCampaignEvent() {
            return campaignEvent;
        }

        public boolean isCampaignEventInd() {
            return campaignEventInd;
        }

        public boolean isCampaignEventType() {
            return campaignEventType;
        }

        public int getPos_x() {
            return pos_x;
        }

        public int getPos_y() {
            return pos_y;
        }

        public int getRegionNbr() {
            return regionNbr;
        }

        public int getTileNbr() {
            return tileNbr;
        }
        
    }
    
    public class BonePile
    {
        
        // The inner class stores information about a bone pile.
        // Burning a bone pile will remove the object from its map location.
        
        // Declare regular variables.
        private boolean activeInd; // Whether bone pile active (used in place of removal simplify array list).
        private int bonePileIndex; // Index in array -- bonePileList.
        private final int pos_x; // X-coordinate of the tile with the bone pile (the array index).
        private final int pos_y; // Y-coordinate of the tile with the bone pile (the array index).
        private final int regionNbr; // Region number.  Base 0.
        
        // pos_x = X-coordinate of the tile with the bone pile (the array index).
        // pos_y = Y-coordinate of the tile with the bone pile (the array index).
        // regionNbr = Region number.  Base 0.
        private BonePile(int pos_x, int pos_y, int regionNbr)
        {
            
            // The constructor stores the bone pile information.
            
            // Store the passed bone pile information in the class-level variables.
            this.activeInd = true;
            this.pos_x = pos_x;
            this.pos_y = pos_y;
            this.regionNbr = regionNbr;
            
        }

        public boolean isActiveInd() {
            return activeInd;
        }

        public void setActiveInd(boolean activeInd) {
            this.activeInd = activeInd;
        }
        
        public int getBonePileIndex() {
            return bonePileIndex;
        }
        
        public void setBonePileIndex(int index) {
            this.bonePileIndex = index;
        }
        
        public int getPos_x() {
            return pos_x;
        }

        public int getPos_y() {
            return pos_y;
        }

        public int getRegionNbr() {
            return regionNbr;
        }
        
    }
    
    public class Chest
    {
        
        // The inner class stores information about a chest.
        // A chest contains one to many items.
        
        // Declare regular variables.
        private boolean activeInd; // Whether bone pile active (used in place of removal simplify array list).
        private final int addlItemCount; // Number of additional items in chest.
        private final ArrayList<HeroineEnum.ItemEnum> addlItemList; // List of additional items in chest.
        private int chestIndex; // Index in array -- chestList.
        private final int pos_x; // X-coordinate of the tile with the chest (the array index).
        private final int pos_y; // Y-coordinate of the tile with the chest (the array index).
        private final HeroineEnum.ItemEnum primaryItem; // Primary item in chest.
        private final int primaryItemCount; // Number of primary item in chest.
        private final int regionNbr; // Region number.  Base 0.
        
        // pos_x = X-coordinate of the tile with the chest (the array index).
        // pos_y = Y-coordinate of the tile with the chest (the array index).
        // regionNbr = Region number.  Base 0.
        // primaryItem = Primary item in chest.
        // primaryItemCount = Quantity of primary item in chest.
        // otherItems = Additional items in chest.
        private Chest(int pos_x, int pos_y, int regionNbr, HeroineEnum.ItemEnum primaryItem, 
          int primaryItemCount, HeroineEnum.ItemEnum ... otherItems)
        {
            
            // The constructor stores the chest information.
            
            // Store the passed chest information in the class-level variables.
            this.activeInd = true;
            this.pos_x = pos_x;
            this.pos_y = pos_y;
            this.regionNbr = regionNbr;
            this.primaryItem = primaryItem;
            this.primaryItemCount = primaryItemCount;
            this.addlItemList = new ArrayList<>(Arrays.asList(otherItems));
            this.addlItemCount = otherItems.length;
            
        }
        
        // pos_x = X-coordinate of the tile with the chest (the array index).
        // pos_y = Y-coordinate of the tile with the chest (the array index).
        // regionNbr = Region number.  Base 0.
        // primaryItem = Primary item in chest.
        // primaryItemCount = Quantity of primary item in chest.
        // otherItems = Additional items in chest.
        private Chest(int pos_x, int pos_y, int regionNbr, HeroineEnum.ItemEnum primaryItem, 
          int primaryItemCount, ArrayList otherItems)
        {
            
            // The constructor stores the chest information.
            
            // Store the passed chest information in the class-level variables.
            this.activeInd = true;
            this.pos_x = pos_x;
            this.pos_y = pos_y;
            this.regionNbr = regionNbr;
            this.primaryItem = primaryItem;
            this.primaryItemCount = primaryItemCount;
            this.addlItemList = new ArrayList<>(otherItems);
            this.addlItemCount = otherItems.size();
            
        }
        
        public boolean isActiveInd() {
            return activeInd;
        }

        public void setActiveInd(boolean activeInd) {
            this.activeInd = activeInd;
        }
        
        public int getAddlItemCount() {
            return addlItemCount;
        }

        public ArrayList<HeroineEnum.ItemEnum> getAddlItemList() {
            return addlItemList;
        }

        public int getChestIndex() {
            return chestIndex;
        }
        
        public void setChestIndex(int index) {
            this.chestIndex = index;
        }
        
        public int getPos_x() {
            return pos_x;
        }

        public int getPos_y() {
            return pos_y;
        }

        public HeroineEnum.ItemEnum getPrimaryItem() {
            return primaryItem;
        }

        public int getPrimaryItemCount() {
            return primaryItemCount;
        }

        public int getRegionNbr() {
            return regionNbr;
        }
        
    }
    
    public class HayBale
    {
        
        // The inner class stores information about a hay bale.
        // Going to a tile with a hay bale results in the player resting and restoring hit and magic points.
        
        // Declare regular variables.
        private final int pos_x; // X-coordinate of the tile with the hay bale (the array index).
        private final int pos_y; // Y-coordinate of the tile with the hay bale (the array index).
        private final int regionNbr; // Region number.  Base 0.
        
        // pos_x = X-coordinate of the tile with the hay bale (the array index).
        // pos_y = Y-coordinate of the tile with the hay bale (the array index).
        // regionNbr = Region number.  Base 0.
        private HayBale(int pos_x, int pos_y, int regionNbr)
        {
            
            // The constructor stores the hay bale information.
            
            // Store the passed hay bale information in the class-level variables.
            this.pos_x = pos_x;
            this.pos_y = pos_y;
            this.regionNbr = regionNbr;
            
        }
        
        public int getPos_x() {
            return pos_x;
        }

        public int getPos_y() {
            return pos_y;
        }

        public int getRegionNbr() {
            return regionNbr;
        }
        
    }
    
    public class ItemCounts
    {
        
        // The inner class stores the counts of each item type for a single region.
        
        // Declare regular variables.
        protected int itemCount_AlterMap; // Number of map alteration events in region.
        protected int itemCount_BonePile; // Number of bone piles in region.
        protected int itemCount_Chest; // Number of chests in region.
        protected int itemCount_HayBale; // Number of hay bales in region.
        protected int itemCount_LockedDoor; // Number of locked doors in region.
        protected int itemCount_SpecificEnemy; // Number of specific enemies in region.
        
        // itemCount_AlterMap = Number of map alteration events in region.
        // itemCount_BonePile = Number of bone piles in region.
        // itemCount_Chest = Number of chests in region.
        // itemCount_HayBale = Number of hay bales in region.
        // itemCount_LockedDoor = Number of locked doors in region.
        // itemCount_SpecificEnemy = Number of specific enemies in region.
        private ItemCounts(int itemCount_AlterMap, int itemCount_BonePile, int itemCount_Chest, 
          int itemCount_HayBale, int itemCount_LockedDoor, int itemCount_SpecificEnemy)
        {

            // The constructor stores the item counts.
            
            // Store the passed item counts in the class-level variables.
            this.itemCount_AlterMap = itemCount_AlterMap;
            this.itemCount_BonePile = itemCount_BonePile;
            this.itemCount_Chest = itemCount_Chest;
            this.itemCount_HayBale = itemCount_HayBale;
            this.itemCount_LockedDoor = itemCount_LockedDoor;
            this.itemCount_SpecificEnemy = itemCount_SpecificEnemy;
            
        }
        
    }
    
    public class LockedDoor
    {
        
        // The inner class stores information about a locked door.
        
        // Declare regular variables.
        private boolean activeInd; // Whether locked door active (used in place of removal simplify array list).
        private int lockedDoorIndex; // Index in array -- lockedDoorList.
        private final int pos_x; // X-coordinate of the tile with the locked door (the array index).
        private final int pos_y; // Y-coordinate of the tile with the locked door (the array index).
        private final int regionNbr; // Region number.  Base 0.
        
        // pos_x = X-coordinate of the tile with the locked door (the array index).
        // pos_y = Y-coordinate of the tile with the locked door (the array index).
        // regionNbr = Region number.  Base 0.
        private LockedDoor(int pos_x, int pos_y, int regionNbr)
        {
            
            // The constructor stores the locked door information.
            
            // Store the passed locked door information in the class-level variables.
            this.activeInd = true;
            this.pos_x = pos_x;
            this.pos_y = pos_y;
            this.regionNbr = regionNbr;
            
        }
        
        public boolean isActiveInd() {
            return activeInd;
        }

        public void setActiveInd(boolean activeInd) {
            this.activeInd = activeInd;
        }
        
        public int getLockedDoorIndex() {
            return lockedDoorIndex;
        }
        
        public void setLockedDoorIndex(int index) {
            this.lockedDoorIndex = index;
        }
        
        public int getPos_x() {
            return pos_x;
        }

        public int getPos_y() {
            return pos_y;
        }

        public int getRegionNbr() {
            return regionNbr;
        }
        
    }
    
    public class SpecificEnemy
    {
        
        // The inner class stores information about an enemy at a specific tile.
        
        // Declare regular variables.
        private final HeroineEnum.EnemyEnum enemyType; // Type of enemy at specific tile.
        private final int pos_x; // X-coordinate of the tile with the enemy (the array index).
        private final int pos_y; // Y-coordinate of the tile with the enemy (the array index).
        private final int regionNbr; // Region number.  Base 0.
        
        // pos_x = X-coordinate of the tile with the specific enemy (the array index).
        // pos_y = Y-coordinate of the tile with the specific enemy (the array index).
        // regionNbr = Region number.  Base 0.
        // enemyType = Type of enemy at specific tile.
        private SpecificEnemy(int pos_x, int pos_y, int regionNbr, HeroineEnum.EnemyEnum enemyType)
        {
            
            // The constructor stores the enemy information.
            
            // Store the passed enemy information in the class-level variables.
            this.pos_x = pos_x;
            this.pos_y = pos_y;
            this.regionNbr = regionNbr;
            this.enemyType = enemyType;
            
        }
        
        public HeroineEnum.EnemyEnum getEnemyType() {
            return enemyType;
        }

        public int getPos_x() {
            return pos_x;
        }

        public int getPos_y() {
            return pos_y;
        }

        public int getRegionNbr() {
            return regionNbr;
        }
        
    }
    
    // Methods below...
    
    // pos_x = X-coordinate of the tile to change (the array index).
    // pos_y = Y-coordinate of the tile to change (the array index).
    // regionNbr = Region number.  Base 0.
    // tileNbr = New tile type.
    public void addAlterMap(int pos_x, int pos_y, int regionNbr, int tileNbr)
    {
        
        // The function adds a map alteration event, without a condition, to the list.
        
        // Add map alteration event.
        alterMapList.add(new AlterMap(pos_x, pos_y, regionNbr, tileNbr));
        
        // Increment map alteration event count.
        alterMapCount++;
        
    }
    
    // pos_x = X-coordinate of the tile to change (the array index).
    // pos_y = Y-coordinate of the tile to change (the array index).
    // regionNbr = Region number.  Base 0.
    // tileNbr = New tile type.
    // campaignEvent = Campaign event to check for in list.
    // campaignEventType = If true, changes tile when event exists in list.
    //   If false, changes tile when event does NOT exist in list.
    public void addAlterMap(int pos_x, int pos_y, int regionNbr, int tileNbr, String campaignEvent, 
      boolean campaignEventType)
    {
        
        // The function adds a map alteration event, with a condition, to the list.
        
        // Add map alteration event.
        alterMapList.add(new AlterMap(pos_x, pos_y, regionNbr, tileNbr, campaignEvent, campaignEventType));
        
        // Increment map alteration event count.
        alterMapCount++;
        
    }
    
    // pos_x = X-coordinate of the tile with the bone pile (the array index).
    // pos_y = Y-coordinate of the tile with the bone pile (the array index).
    // regionNbr = Region number.  Base 0.
    public void addBonePile(int pos_x, int pos_y, int regionNbr)
    {
        
        // The function adds a bone pile to the list.
        // Burning a bone pile will remove the object from its map location.
        
        // Add bone pile.
        bonePileList.add(new BonePile(pos_x, pos_y, regionNbr));
        
        // Increment bone pile count.
        bonePileCount++;
        
    }
    
    // pos_x = X-coordinate of the tile with the chest (the array index).
    // pos_y = Y-coordinate of the tile with the chest (the array index).
    // regionNbr = Region number.  Base 0.
    // primaryItem = Primary item in chest.
    // primaryItemCount = Quantity of primary item in chest.
    // otherItems = Additional items in chest.
    public void addChest(int pos_x, int pos_y, int regionNbr, HeroineEnum.ItemEnum primaryItem, 
      int primaryItemCount, HeroineEnum.ItemEnum ... otherItems)
    {
        
        // The function adds a chest to the list.
        // A chest contains one to many items.
        
        // If one or more additional item(s) passed, then...
        if (otherItems.length > 0)
        {
            
            // One or more additional item(s) passed.
            
            // Add chest.
            chestList.add(new Chest(pos_x, pos_y, regionNbr, primaryItem, primaryItemCount, otherItems));
            
        }
        
        else
        {
            
            // No additional items passed.
            chestList.add(new Chest(pos_x, pos_y, regionNbr, primaryItem, primaryItemCount));
            
        }
        
        // Increment chest count.
        chestCount++;
        
    }
    
    // pos_x = X-coordinate of the tile with the chest (the array index).
    // pos_y = Y-coordinate of the tile with the chest (the array index).
    // regionNbr = Region number.  Base 0.
    // primaryItem = Primary item in chest.
    // primaryItemCount = Quantity of primary item in chest.
    // otherItems = Additional items in chest.
    public void addChest(int pos_x, int pos_y, int regionNbr, HeroineEnum.ItemEnum primaryItem, 
      int primaryItemCount, ArrayList otherItems)
    {
        
        // The function adds a chest to the list.
        // A chest contains one to many items.
        
        // Add chest.
        chestList.add(new Chest(pos_x, pos_y, regionNbr, primaryItem, primaryItemCount, otherItems));
        
        // Increment chest count.
        chestCount++;
        
    }
    
    // pos_x = X-coordinate of the tile with the hay bale (the array index).
    // pos_y = Y-coordinate of the tile with the hay bale (the array index).
    // regionNbr = Region number.  Base 0.
    public void addHayBale(int pos_x, int pos_y, int regionNbr)
    {
        
        // The function adds a hay bale to the list.
        // Going to a tile with a hay bale results in the player resting and restoring hit and magic points.
        
        // Add hay bale.
        hayBaleList.add(new HayBale(pos_x, pos_y, regionNbr));
        
        // Increment hay bale count.
        hayBaleCount++;
        
    }
    
    // pos_x = X-coordinate of the tile with the locked door (the array index).
    // pos_y = Y-coordinate of the tile with the locked door (the array index).
    // regionNbr = Region number.  Base 0.
    public void addLockedDoor(int pos_x, int pos_y, int regionNbr)
    {
        
        // The function adds a locked door to the list.
        
        // Add locked door.
        lockedDoorList.add(new LockedDoor(pos_x, pos_y, regionNbr));
        
        // Increment locked door count.
        lockedDoorCount++;
        
    }
    
    // pos_x = X-coordinate of the tile with the bone pile (the array index).
    // pos_y = Y-coordinate of the tile with the bone pile (the array index).
    // regionNbr = Region number.  Base 0.
    // enemyType = Type of enemy at specific tile.
    public void addSpecificEnemy(int pos_x, int pos_y, int regionNbr, HeroineEnum.EnemyEnum enemyType)
    {
        
        // The function adds a specific enemy to the list.
        
        // Add enemy.
        specificEnemyList.add(new SpecificEnemy(pos_x, pos_y, regionNbr, enemyType));
        
        // Increment enemy count.
        specificEnemyCount++;
        
    }
    
    public void populateHashMap()
    {
        
        // The function populates the hash map containing all atlas item information.
        
        int counter; // Used to iterate through items.
        
        Map<String, Object> mapJSON_AlterMap; // Hash map containing key / value pairs covering current
          // map alteration event data -- used with JSON.
        Map<String, Object> mapJSON_AlterMaps; // Hash map containing key / value pairs covering all
          // map alteration event data -- used with JSON.
        Map<String, Object> mapJSON_BonePile; // Hash map containing key / value pairs covering current bone
          // pile data -- used with JSON.
        Map<String, Object> mapJSON_BonePiles; // Hash map containing key / value pairs covering all bone
          // pile data -- used with JSON.
        Map<String, Object> mapJSON_Chest; // Hash map containing key / value pairs covering current 
          // chest data -- used with JSON.
        Map<String, Object> mapJSON_Chests; // Hash map containing key / value pairs covering all 
          // chest data -- used with JSON.
        Map<String, Object> mapJSON_HayBale; // Hash map containing key / value pairs covering current
          // hay bale data -- used with JSON.
        Map<String, Object> mapJSON_HayBales; // Hash map containing key / value pairs covering all
          // hay bale data -- used with JSON.
        Map<String, Object> mapJSON_LockedDoor; // Hash map containing key / value pairs covering current 
          // locked door data -- used with JSON.
        Map<String, Object> mapJSON_LockedDoors; // Hash map containing key / value pairs covering all 
          // locked door data -- used with JSON.
        Map<String, Object> mapJSON_SpecificEnemy; // Hash map containing key / value pairs covering current
          // specific enemy data -- used with JSON.
        Map<String, Object> mapJSON_SpecificEnemies; // Hash map containing key / value pairs covering all
          // specific enemy data -- used with JSON.
        
        // Set defaults.
        counter = 1;
        
        // Initialize hash map.
        mapJSON = new HashMap<>();
        mapJSON_AlterMaps = new HashMap<>();
        mapJSON_BonePiles = new HashMap<>();
        mapJSON_Chests = new HashMap<>();
        mapJSON_HayBales = new HashMap<>();
        mapJSON_LockedDoors = new HashMap<>();
        mapJSON_SpecificEnemies = new HashMap<>();
        
        // If atlas contains map alteration event(s), then...
        if (alterMapCount > 0)    
        {
            
            // Atlas contains one or more map alteration events.
            
            // Loop through map alteration events.
            for (AlterMap currAlterMap : alterMapList)
            {

                // Reinitialize hash map for current map alteration event.
                // Necessary or same data written for each entry.
                mapJSON_AlterMap = new HashMap<>();
                
                // Add information to hash map containing data for a single map alteration event.
                mapJSON_AlterMap.put("POS_X", currAlterMap.pos_x);
                mapJSON_AlterMap.put("POS_Y", currAlterMap.pos_y);
                mapJSON_AlterMap.put("REGION_NBR", currAlterMap.regionNbr);
                mapJSON_AlterMap.put("TILE_NBR", currAlterMap.tileNbr);
                
                // If a campaign event, then...
                if (currAlterMap.campaignEventInd)
                {
                    
                    // Campaign event.
                    
                    // Add campaign event information.
                    mapJSON_AlterMap.put("CAMPAIGN_EVENT_TYPE", currAlterMap.campaignEventType);
                    mapJSON_AlterMap.put("CAMPAIGN_EVENT", currAlterMap.campaignEvent);
                    mapJSON_AlterMap.put("CAMPAIGN_EVENT_IND", true);
                    
                }
                
                else
                {
                    
                    // NOT a campaign event.
                    
                    // Add campaign event information.
                    mapJSON_AlterMap.put("CAMPAIGN_EVENT_IND", false);
                    
                }
                
                // Add hash map for current to container.
                mapJSON_AlterMaps.put("ALTER_MAP_" + decimalFormat00.format(counter), mapJSON_AlterMap);

                // Increment counter.
                counter++;

            }
            
            // Reset counter.
            counter = 1;
            
        }
        
        // If atlas contains bone pile(s), then...
        if (bonePileCount > 0)    
        {
            
            // Atlas contains one or more bone piles.
            
            // Loop through bone piles.
            for (BonePile currBonePile : bonePileList)
            {

                // Reinitialize hash map for current bone pile.  Necessary or same data written for each entry.
                mapJSON_BonePile = new HashMap<>();
                
                // Add information to hash map containing data for a single bone pile.
                mapJSON_BonePile.put("POS_X", currBonePile.pos_x);
                mapJSON_BonePile.put("POS_Y", currBonePile.pos_y);
                mapJSON_BonePile.put("REGION_NBR", currBonePile.regionNbr);
                
                // Add hash map for current to container.
                mapJSON_BonePiles.put("BONE_PILE_" + decimalFormat00.format(counter), mapJSON_BonePile);

                // Increment counter.
                counter++;

            }

            // Reset counter.
            counter = 1;
            
        }
        
        // If atlas contains chest(s), then...
        if (chestCount > 0)    
        {
            
            // Atlas contains one or more chests.
            
            // Loop through chests.
            for (Chest currChest : chestList)
            {
                
                // Reinitialize hash map for current chest.  Necessary or same data written for each entry.
                mapJSON_Chest = new HashMap<>();
                
                // Add information to hash map containing data for a single chest.
                mapJSON_Chest.put("POS_X", currChest.pos_x);
                mapJSON_Chest.put("POS_Y", currChest.pos_y);
                mapJSON_Chest.put("REGION_NBR", currChest.regionNbr);
                mapJSON_Chest.put("PRIMARY_ITEM", currChest.primaryItem);
                mapJSON_Chest.put("PRIMARY_ITEM_CNT", currChest.primaryItemCount);
                mapJSON_Chest.put("ADDL_ITEM_CNT", currChest.addlItemCount);
                
                // If additional items exist in chest, then...
                if (currChest.addlItemCount > 0)
                {
                    
                    // Additional items exist in chest.
                    
                    // Store additional items data.
                    mapJSON_Chest.put("ADDL_ITEMS", currChest.addlItemList);
                    
                }
                
                // Add hash map for current to container.
                mapJSON_Chests.put("CHEST_" + decimalFormat00.format(counter), mapJSON_Chest);

                // Increment counter.
                counter++;

            }

            // Reset counter.
            counter = 1;
            
        }
        
        // If atlas contains hay bale(s), then...
        if (hayBaleCount > 0)    
        {
            
            // Atlas contains one or more hay bales.
            
            // Loop through hay bales.
            for (HayBale currHayBale : hayBaleList)
            {

                // Reinitialize hash map for current hay bale.  Necessary or same data written for each entry.
                mapJSON_HayBale = new HashMap<>();
                
                // Add information to hash map containing data for a single hay bale.
                mapJSON_HayBale.put("POS_X", currHayBale.pos_x);
                mapJSON_HayBale.put("POS_Y", currHayBale.pos_y);
                mapJSON_HayBale.put("REGION_NBR", currHayBale.regionNbr);
                
                // Add hash map for current to container.
                mapJSON_HayBales.put("HAY_BALE_" + decimalFormat00.format(counter), mapJSON_HayBale);

                // Increment counter.
                counter++;

            }

            // Reset counter.
            counter = 1;
            
        }
        
        // If atlas contains locked door(s), then...
        if (lockedDoorCount > 0)    
        {
            
            // Atlas contains one or more locked doors.
            
            // Loop through locked doors.
            for (LockedDoor currLockedDoor : lockedDoorList)
            {

                // Reinitialize hash map for current locked door.  Necessary or same data written for each entry.
                mapJSON_LockedDoor = new HashMap<>();
                
                // Add information to hash map containing data for a single locked door.
                mapJSON_LockedDoor.put("POS_X", currLockedDoor.pos_x);
                mapJSON_LockedDoor.put("POS_Y", currLockedDoor.pos_y);
                mapJSON_LockedDoor.put("REGION_NBR", currLockedDoor.regionNbr);
                
                // Add hash map for current to container.
                mapJSON_LockedDoors.put("LOCKED_DOOR_" + decimalFormat00.format(counter), mapJSON_LockedDoor);

                // Increment counter.
                counter++;

            }

            // Reset counter.
            counter = 1;
            
        }
        
        // If atlas contains specific enem(ies), then...
        if (specificEnemyCount > 0)    
        {
            
            // Atlas contains one or more specific enemies.
            
            // Loop through specific enemies.
            for (SpecificEnemy currSpecificEnemy : specificEnemyList)
            {

                // Reinitialize hash map for current specific enemy.  Necessary or same data written for each entry.
                mapJSON_SpecificEnemy = new HashMap<>();
                
                // Add information to hash map containing data for a single specific enemy.
                mapJSON_SpecificEnemy.put("POS_X", currSpecificEnemy.pos_x);
                mapJSON_SpecificEnemy.put("POS_Y", currSpecificEnemy.pos_y);
                mapJSON_SpecificEnemy.put("REGION_NBR", currSpecificEnemy.regionNbr);
                mapJSON_SpecificEnemy.put("ENEMY_TYPE", currSpecificEnemy.enemyType);
                
                // Add hash map for current to container.
                mapJSON_SpecificEnemies.put("SPECIFIC_ENEMY_" + decimalFormat00.format(counter), 
                  mapJSON_SpecificEnemy);

                // Increment counter.
                counter++;

            }

            // Reset counter.
            counter = 1;
            
        }
        
        // Add values for the atlas to hash map.
        
        // Start with counts.
        mapJSON.put("ALTER_MAP_CNT", alterMapCount);
        mapJSON.put("BONE_PILE_CNT", bonePileCount);
        mapJSON.put("CHEST_CNT", chestCount);
        mapJSON.put("HAY_BALE_CNT", hayBaleCount);
        mapJSON.put("LOCKED_DOOR_CNT", lockedDoorCount);
        mapJSON.put("SPECIFIC_ENEMY_CNT", specificEnemyCount);
        
        // Next, add item information (as necessary).
        
        // If one or more map alteration events exist, then...
        if (alterMapCount > 0)
            mapJSON.put("ALTER_MAP_EVENTS", mapJSON_AlterMaps);
        
        // If one or more bone piles exist, then...
        if (bonePileCount > 0)
            mapJSON.put("BONE_PILES", mapJSON_BonePiles);
        
        // If one or more chests exist, then...
        if (chestCount > 0)
            mapJSON.put("CHESTS", mapJSON_Chests);
        
        // If one or more hay bales exist, then...
        if (hayBaleCount > 0)
            mapJSON.put("HAY_BALES", mapJSON_HayBales);
        
        // If one or more locked doors exist, then...
        if (lockedDoorCount > 0)
            mapJSON.put("LOCKED_DOORS", mapJSON_LockedDoors);
        
        // If one or more specific enemies exist, then...
        if (specificEnemyCount > 0)
            mapJSON.put("SPECIFIC_ENEMIES", mapJSON_SpecificEnemies);
        
    }
    
    // mapJSONBuild = Hash map containing item data for the atlas.
    // regionCount = Number of regions.
    public void readHashMap(Map<String, Object> mapJSONBuild, int regionCount)
    {
        
        // The function builds the item data for the atlas using the passed hash map.
        
        // Declare object variables.
        Set<Map.Entry<String, Object>> entrySetAlterMap; // Set view of the map alteration event mappings in the hash map.
        Set<Map.Entry<String, Object>> entrySetBonePile; // Set view of the bone pile mappings in the hash map.
        Set<Map.Entry<String, Object>> entrySetChest; // Set view of the chest mappings in the hash map.
        Set<Map.Entry<String, Object>> entrySetHayBale; // Set view of the hay bale mappings in the hash map.
        Set<Map.Entry<String, Object>> entrySetLockedDoor; // Set view of the locked door mappings in the hash map.
        Set<Map.Entry<String, Object>> entrySetSpecificEnemy; // Set view of the specific enemy mappings in the hash map.
        HashMap<String, Object> alterMapData; // Hash map containing information for a single map alteration event.
        HashMap<String, Object> alterMapMap; // Hash map containing information for map alteration events in loop.
        HashMap<String, Object> atlasItemsMap; // Hash map containing top-level object / parent.
          // The children include the counts and other hash maps.
        HashMap<String, Object> bonePileData; // Hash map containing information for a single bone pile.
        HashMap<String, Object> bonePileMap; // Hash map containing information for bone piles in loop.
        HashMap<String, Object> chestData; // Hash map containing information for a single chest.
        HashMap<String, Object> chestMap; // Hash map containing information for chests in loop.
        HashMap<String, Object> hayBaleData; // Hash map containing information for a single hay bale.
        HashMap<String, Object> hayBaleMap; // Hash map containing information for hay bales in loop.
        HashMap<String, Object> lockedDoorData; // Hash map containing information for a single locked door.
        HashMap<String, Object> lockedDoorMap; // Hash map containing information for locked doors in loop.
        HashMap<String, Object> specificEnemyData; // Hash map containing information for a single specific enemy.
        HashMap<String, Object> specificEnemyMap; // Hash map containing information for specific enemies in loop.
        
        // Declare regular variables.
        int chestAddlItemCount; // Number of additional items in the current chest.
        int fileAlterMapCount; // Number of map alteration events in the file.  Used for validation.
        int fileBonePileCount; // Number of bone piles in the file.  Used for validation.
        int fileChestCount; // Number of chests in the file.  Used for validation.
        int fileHayBaleCount; // Number of hay bales in the file.  Used for validation.
        int fileLockedDoorCount; // Number of locked doors in the file.  Used for validation.
        int fileSpecificEnemyCount; // Number of specific enemies in the file.  Used for validation.
        
        // Reinitialize class-level lists.
        alterMapList = new ArrayList<>();
        bonePileList = new ArrayList<>();
        chestList = new ArrayList<>();
        hayBaleList = new ArrayList<>();
        lockedDoorList = new ArrayList<>();
        specificEnemyList = new ArrayList<>();
        
        // Initialize hash maps containing item information.
        alterMapMap = new HashMap<>();
        atlasItemsMap = new HashMap<>();
        bonePileMap = new HashMap<>();
        chestMap = new HashMap<>();
        hayBaleMap = new HashMap<>();
        lockedDoorMap = new HashMap<>();
        specificEnemyMap = new HashMap<>();
        
        // Get the top-level / parent hash map.
        atlasItemsMap.putAll((HashMap)mapJSONBuild.get("items"));
        
        // Populate the counts -- used for validation.
        fileAlterMapCount = (int)atlasItemsMap.get("ALTER_MAP_CNT");
        fileBonePileCount = (int)atlasItemsMap.get("BONE_PILE_CNT");
        fileChestCount = (int)atlasItemsMap.get("CHEST_CNT");
        fileHayBaleCount = (int)atlasItemsMap.get("HAY_BALE_CNT");
        fileLockedDoorCount = (int)atlasItemsMap.get("LOCKED_DOOR_CNT");
        fileSpecificEnemyCount = (int)atlasItemsMap.get("SPECIFIC_ENEMY_CNT");
        
        // If one or more map alteration event(s) exist, then...
        if (fileAlterMapCount > 0)
        {

            // One or more map alteration event(s) exist.

            // Copy all data for map alteration events into hash map. 
            alterMapMap.putAll((HashMap)atlasItemsMap.get("ALTER_MAP_EVENTS"));

            // Store a set view of the map alteration event mappings for the hash map.
            entrySetAlterMap = alterMapMap.entrySet();

            // Loop through entries for map alteration events.
            for (Entry entryAlterMap : entrySetAlterMap)
            {

                // Reinitialize hash map for single map alteration event.
                alterMapData = new HashMap<>();

                // Copy all data for current map alteration event in loop to hash map.
                alterMapData.putAll((HashMap)entryAlterMap.getValue());

                // Add the map alteration event.
                
                // If a campaign event, then...
                if (((boolean)alterMapData.get("CAMPAIGN_EVENT_IND")))    
                {
                    
                    // Campaign event.
                    
                    // Add map alteration event.
                    addAlterMap((int)alterMapData.get("POS_X"), (int)alterMapData.get("POS_Y"), 
                      (int)alterMapData.get("REGION_NBR"), (int)alterMapData.get("TILE_NBR"), 
                      (String)alterMapData.get("CAMPAIGN_EVENT"), (boolean)alterMapData.get("CAMPAIGN_EVENT_TYPE"));
                
                }
                
                else
                {
                    
                    // NOT a campaign event.
                    
                    // Add map alteration event.
                    addAlterMap((int)alterMapData.get("POS_X"), (int)alterMapData.get("POS_Y"), 
                      (int)alterMapData.get("REGION_NBR"), (int)alterMapData.get("TILE_NBR"));
                    
                }
            
            } // End ... Loop through entries for map alteration event(s).

        } // End ... If one or more map alteration event(s) exist.
        
        // If one or more bone pile(s) exist, then...
        if (fileBonePileCount > 0)
        {

            // One or more bone pile(s) exist.

            // Copy all data for bone piles into hash map. 
            bonePileMap.putAll((HashMap)atlasItemsMap.get("BONE_PILES"));

            // Store a set view of the bone pile mappings for the hash map.
            entrySetBonePile = bonePileMap.entrySet();

            // Loop through entries for bone piles.
            for (Entry entryBonePile : entrySetBonePile)
            {

                // Reinitialize hash map for single bone pile.
                bonePileData = new HashMap<>();

                // Copy all data for current bone pile in loop to hash map.
                bonePileData.putAll((HashMap)entryBonePile.getValue());

                // Add the bone pile.
                addBonePile((int)bonePileData.get("POS_X"), (int)bonePileData.get("POS_Y"), 
                  (int)bonePileData.get("REGION_NBR"));
            
            } // End ... Loop through entries for bone pile(s).

        } // End ... If one or more bone pile(s) exist.
        
        // If one or more chest(s) exist, then...
        if (fileChestCount > 0)
        {

            // One or more chest(s) exist.

            // Copy all data for chests into hash map. 
            chestMap.putAll((HashMap)atlasItemsMap.get("CHESTS"));

            // Store a set view of the chest mappings for the hash map.
            entrySetChest = chestMap.entrySet();

            // Loop through entries for chests.
            for (Entry entryChest : entrySetChest)
            {

                // Reinitialize hash map for single chest.
                chestData = new HashMap<>();

                // Copy all data for current chest in loop to hash map.
                chestData.putAll((HashMap)entryChest.getValue());

                // Store the number of additional item(s) in the chest.
                chestAddlItemCount = (int)chestData.get("ADDL_ITEM_CNT");
                
                // If the chest contains one or more additional item(s), then...
                if (chestAddlItemCount > 0)
                {
                    
                    // One or more additional item(s) exist in the chest.
                    
                    // Add the chest.
                    addChest((int)chestData.get("POS_X"), (int)chestData.get("POS_Y"), 
                      (int)chestData.get("REGION_NBR"), 
                      HeroineEnum.ItemEnum.valueOf((String)chestData.get("PRIMARY_ITEM")), 
                      (int)chestData.get("PRIMARY_ITEM_CNT"), (ArrayList)chestData.get("ADDL_ITEMS"));
                    
                }
                
                else
                {
                    
                    // No additional item(s) exist in the chest.
                    
                    // Add the chest.
                    addChest((int)chestData.get("POS_X"), (int)chestData.get("POS_Y"), 
                      (int)chestData.get("REGION_NBR"), 
                      HeroineEnum.ItemEnum.valueOf((String)chestData.get("PRIMARY_ITEM")), 
                      (int)chestData.get("PRIMARY_ITEM_CNT"));
                    
                }
            
            } // End ... Loop through entries for chest(s).

        } // End ... If one or more chest(s) exist.
        
        // If one or more hay bale(s) exist, then...
        if (fileHayBaleCount > 0)
        {

            // One or more hay bale(s) exist.

            // Copy all data for hay bales into hash map. 
            hayBaleMap.putAll((HashMap)atlasItemsMap.get("HAY_BALES"));

            // Store a set view of the hay bale mappings for the hash map.
            entrySetHayBale = hayBaleMap.entrySet();

            // Loop through entries for hay bales.
            for (Entry entryHayBale : entrySetHayBale)
            {

                // Reinitialize hash map for single hay bale.
                hayBaleData = new HashMap<>();

                // Copy all data for current hay bale in loop to hash map.
                hayBaleData.putAll((HashMap)entryHayBale.getValue());
                    
                // Add the hay bale.
                addHayBale((int)hayBaleData.get("POS_X"), (int)hayBaleData.get("POS_Y"), 
                  (int)hayBaleData.get("REGION_NBR"));
            
            } // End ... Loop through entries for hay bale(s).

        } // End ... If one or more hay bale(s) exist.
        
        // If one or more locked door(s) exist, then...
        if (fileLockedDoorCount > 0)
        {

            // One or more locked door(s) exist.

            // Copy all data for locked doors into hash map. 
            lockedDoorMap.putAll((HashMap)atlasItemsMap.get("LOCKED_DOORS"));

            // Store a set view of the locked door mappings for the hash map.
            entrySetLockedDoor = lockedDoorMap.entrySet();

            // Loop through entries for locked doors.
            for (Entry entryLockedDoor : entrySetLockedDoor)
            {

                // Reinitialize hash map for single locked door.
                lockedDoorData = new HashMap<>();

                // Copy all data for current locked door in loop to hash map.
                lockedDoorData.putAll((HashMap)entryLockedDoor.getValue());
                
                // Add the locked door.
                addLockedDoor((int)lockedDoorData.get("POS_X"), (int)lockedDoorData.get("POS_Y"), 
                  (int)lockedDoorData.get("REGION_NBR"));
            
            } // End ... Loop through entries for locked door(s).

        } // End ... If one or more locked door(s) exist.
        
        // If one or more specific enem(ies) exist, then...
        if (fileSpecificEnemyCount > 0)
        {
            
            // One or more specific enem(ies) exist.

            // Copy all data for specific enemies into hash map. 
            specificEnemyMap.putAll((HashMap)atlasItemsMap.get("SPECIFIC_ENEMIES"));

            // Store a set view of the specific enemy mappings for the hash map.
            entrySetSpecificEnemy = specificEnemyMap.entrySet();

            // Loop through entries for specific enemies.
            for (Entry entrySpecificEnemy : entrySetSpecificEnemy)
            {

                // Reinitialize hash map for single specific enemy.
                specificEnemyData = new HashMap<>();

                // Copy all data for current specific enemy in loop to hash map.
                specificEnemyData.putAll((HashMap)entrySpecificEnemy.getValue());
                
                // Add the specific enemy.
                addSpecificEnemy((int)specificEnemyData.get("POS_X"), (int)specificEnemyData.get("POS_Y"), 
                  (int)specificEnemyData.get("REGION_NBR"), 
                  HeroineEnum.EnemyEnum.valueOf((String)specificEnemyData.get("ENEMY_TYPE")));
            
            } // End ... Loop through entries for specific enem(ies).

        } // End ... If one or more specific(ies) exist.
        
        // Store item information by region.
        storeRegionInfo(regionCount);
        
        // Validate counts in class vs read from file.
        
        // If counts match, then...
        if (fileAlterMapCount == alterMapCount && fileBonePileCount == bonePileCount && 
          fileChestCount == chestCount && fileHayBaleCount == hayBaleCount && 
          fileLockedDoorCount == lockedDoorCount && fileSpecificEnemyCount == specificEnemyCount)
        {}
        
        else
        {
            // Counts differ.
            System.out.println("Warning:  Counts between file and class differ!");
        }
        
    }
    
    // regionCount = Number of regions.
    private void storeRegionInfo(int regionCount)
    {
        
        // The function stores the hash maps and array lists with the information by region / location.
        
        int counterIndex; // Counter used to increment through list indices.
        ArrayList<Object> temp; // Used when first adding an array list to the hash map with items by location.
        String key; // Key value for populating hash map with items by location.
        
        // First, initialize hash maps.
        mapRegionItems = new HashMap<>();
        
        // Second, store item counts for each region.
        
        // Loop through and initialize counts for each region as zero.
        for (int counter = 0; counter < regionCount; counter++)
            itemCountList.add(new ItemCounts(0, 0, 0, 0, 0, 0));
        
        // Loop through map alteration events and increment counts for appropriate regions.
        alterMapList.forEach((alterMap) -> {
            itemCountList.get(alterMap.regionNbr).itemCount_AlterMap++;
        });
        
        // Loop through bone piles and increment counts for appropriate regions.
        bonePileList.forEach((bonePile) -> {
            itemCountList.get(bonePile.regionNbr).itemCount_BonePile++;
        });
        
        // Loop through chests and increment counts for appropriate regions.
        chestList.forEach((chest) -> {
            itemCountList.get(chest.regionNbr).itemCount_Chest++;
        });
        
        // Loop through hay bales and increment counts for appropriate regions.
        hayBaleList.forEach((hayBale) -> {
            itemCountList.get(hayBale.regionNbr).itemCount_HayBale++;
        });
        
        // Loop through locked doors and increment counts for appropriate regions.
        lockedDoorList.forEach((lockedDoor) -> {
            itemCountList.get(lockedDoor.regionNbr).itemCount_LockedDoor++;
        });
        
        // Loop through specific enemies and increment counts for appropriate regions.
        specificEnemyList.forEach((specificEnemy) -> {
            itemCountList.get(specificEnemy.regionNbr).itemCount_SpecificEnemy++;
        });
        
        // Loop through and calculate total item counts by region.
        for (int counter = 0; counter < regionCount; counter++)
            itemTotalList.add(itemCountList.get(counter).itemCount_AlterMap + 
            itemCountList.get(counter).itemCount_BonePile + itemCountList.get(counter).itemCount_Chest + 
            itemCountList.get(counter).itemCount_HayBale + itemCountList.get(counter).itemCount_LockedDoor + 
            itemCountList.get(counter).itemCount_SpecificEnemy);
        
        // Last, populate hash maps for locations with items.
        
        // Loop through map alteration events.
        for (AlterMap alterMap : alterMapList)
        {
            
            // Determine key.
            key = Integer.toString(alterMap.regionNbr) + "," + Integer.toString(alterMap.pos_x) + "," + 
              Integer.toString(alterMap.pos_y) + "," + HeroineEnum.ItemCategoryEnum.ITEM_CTGY_ALTER_MAP;
            
            // If necessary, initialize array list for map / region items for the current key.
            
            // If item type exists in hash map for location, then...
            if (mapRegionItems.get(key) != null)
            {
                // Item type DOES exist in hash map for location.
                
                // Get reference to existing array list.
                temp = mapRegionItems.get(key);
            }
            
            else
            {
                // Item type DOES NOT exist in hash map for location.
                
                // Create new array list (reinitialize).
                temp = new ArrayList<>();
                
            }
            
            // Add item to array list.
            temp.add(alterMap);
            
            // Add item to hash map.
            mapRegionItems.put(key, temp);
            
        }
        
        // Reset index counter.
        counterIndex = 0;
        
        // Loop through bone pile events.
        for (BonePile bonePile : bonePileList)
        {
            
            // Determine key.
            key = Integer.toString(bonePile.regionNbr) + "," + Integer.toString(bonePile.pos_x) + "," + 
              Integer.toString(bonePile.pos_y) + "," + HeroineEnum.ItemCategoryEnum.ITEM_CTGY_BONE_PILE;
            
            // If necessary, initialize array list for map / region items for the current key.
            
            // If item type exists in hash map for location, then...
            if (mapRegionItems.get(key) != null)
            {
                // Item type DOES exist in hash map for location.
                
                // Get reference to existing array list.
                temp = mapRegionItems.get(key);
            }
            
            else
            {
                // Item type DOES NOT exist in hash map for location.
                
                // Create new array list (reinitialize).
                temp = new ArrayList<>();
                
            }
            
            // Store index.
            bonePile.setBonePileIndex(counterIndex);
            
            // Add item to array list.
            temp.add(bonePile);
            
            // Add item to hash map.
            mapRegionItems.put(key, temp);
            
            // Increment counter.
            counterIndex++;
            
        }
        
        // Reset index counter.
        counterIndex = 0;
        
        // Loop through chests.
        for (Chest chest : chestList)
        {
            
            // Determine key.
            key = Integer.toString(chest.regionNbr) + "," + Integer.toString(chest.pos_x) + "," + 
              Integer.toString(chest.pos_y) + "," + HeroineEnum.ItemCategoryEnum.ITEM_CTGY_CHEST;
            
            // If necessary, initialize array list for map / region items for the current key.
            
            // If item type exists in hash map for location, then...
            if (mapRegionItems.get(key) != null)
            {
                // Item type DOES exist in hash map for location.
                
                // Get reference to existing array list.
                temp = mapRegionItems.get(key);
            }
            
            else
            {
                // Item type DOES NOT exist in hash map for location.
                
                // Create new array list (reinitialize).
                temp = new ArrayList<>();
                
            }
            
            // Store index.
            chest.setChestIndex(counterIndex);
            
            // Add item to array list.
            temp.add(chest);
            
            // Add item to hash map.
            mapRegionItems.put(key, temp);
            
            // Increment counter.
            counterIndex++;
            
        }
        
        // Loop through hay bales.
        for (HayBale hayBale : hayBaleList)
        {
            
            // Determine key.
            key = Integer.toString(hayBale.regionNbr) + "," + Integer.toString(hayBale.pos_x) + "," + 
              Integer.toString(hayBale.pos_y) + "," + HeroineEnum.ItemCategoryEnum.ITEM_CTGY_HAY_BALE;
            
            // If necessary, initialize array list for map / region items for the current key.
            
            // If item type exists in hash map for location, then...
            if (mapRegionItems.get(key) != null)
            {
                // Item type DOES exist in hash map for location.
                
                // Get reference to existing array list.
                temp = mapRegionItems.get(key);
            }
            
            else
            {
                // Item type DOES NOT exist in hash map for location.
                
                // Create new array list (reinitialize).
                temp = new ArrayList<>();
                
            }
            
            // Add item to array list.
            temp.add(hayBale);
            
            // Add item to hash map.
            mapRegionItems.put(key, temp);
            
        }
        
        // Reset index counter.
        counterIndex = 0;
        
        // Loop through locked doors.
        for (LockedDoor lockedDoor : lockedDoorList)
        {
            
            // Determine key.
            key = Integer.toString(lockedDoor.regionNbr) + "," + Integer.toString(lockedDoor.pos_x) + "," + 
              Integer.toString(lockedDoor.pos_y) + "," + HeroineEnum.ItemCategoryEnum.ITEM_CTGY_LOCKED_DOOR;
            
            // If necessary, initialize array list for map / region items for the current key.
            
            // If item type exists in hash map for location, then...
            if (mapRegionItems.get(key) != null)
            {
                // Item type DOES exist in hash map for location.
                
                // Get reference to existing array list.
                temp = mapRegionItems.get(key);
            }
            
            else
            {
                // Item type DOES NOT exist in hash map for location.
                
                // Create new array list (reinitialize).
                temp = new ArrayList<>();
                
            }
            
            // Store index.
            lockedDoor.setLockedDoorIndex(counterIndex);
            
            // Add item to array list.
            temp.add(lockedDoor);
            
            // Add item to hash map.
            mapRegionItems.put(key, temp);
            
            // Increment counter.
            counterIndex++;
            
        }
        
        // Loop through enemies at specific locations.
        for (SpecificEnemy specificEnemy : specificEnemyList)
        {
            
            // Determine key.
            key = Integer.toString(specificEnemy.regionNbr) + "," + Integer.toString(specificEnemy.pos_x) + "," + 
              Integer.toString(specificEnemy.pos_y) + "," + HeroineEnum.ItemCategoryEnum.ITEM_CTGY_SPECIFIC_ENEMY;
            
            // If necessary, initialize array list for map / region items for the current key.
            
            // If item type exists in hash map for location, then...
            if (mapRegionItems.get(key) != null)
            {
                // Item type DOES exist in hash map for location.
                
                // Get reference to existing array list.
                temp = mapRegionItems.get(key);
            }
            
            else
            {
                // Item type DOES NOT exist in hash map for location.
                
                // Create new array list (reinitialize).
                temp = new ArrayList<>();
                
            }
            
            // Add item to array list.
            temp.add(specificEnemy);
            
            // Add item to hash map.
            mapRegionItems.put(key, temp);
            
        }
        
    }
    
    // index = Array index of bone pile to "remove" -- set inactive -- (in the array list, bonePileList).
    // map_id = Map / region number in which to remove the bone pile.
    public void removeBonePile(int index, int map_id) {
        
        /*
        The function encapsulates logic for setting a bone pile (based on map / region and index) as 
        inactive in the array list.  The function also reduces the bone pile counts in the related lists.
        
        Notes:
        1.  The hash map, mapRegionItems, stores a list of objects, by type, at each location.
        2.  Keys in the hash map use the format, map_id,x,y,*ITEM CATEGORY*.
        3.  *ITEM CATEGORY* = One of the enumerated values in HeroineEnum.ItemCategoryEnum.
        4.  The "value" in the hash map contains an array list of the specified item category.
        5.  For example, the "value" would contain one to many bone piles if category = ITEM_CTGY_BONE_PILE.
        7.  The function does NOT remove the bone pile from the hash map.
        */
        
        // Set the bone pile inactive to simulate removing from the array list.
        bonePileList.get(index).setActiveInd(false);
        //bonePileList.remove(index);
        
        // Reduce bone pile count.
        bonePileCount--;
        
        // For the passed map / region, reduce number of bone piles stored in array list with quantities.
        itemCountList.get(map_id).itemCount_BonePile--;
        
        // Reduce the total item count for the current region.
        itemTotalList.set(map_id, itemTotalList.get(map_id) - 1);
        
    }
    
    // map_id = Map / region number in which to remove the bone pile.
    // posX = X-coordinate associated with the bone pile.
    // posY = Y-coordinate associated with the bone pile.
    public void removeBonePileEntry(int map_id, int posX, int posY)
    {
        
        // The function removes the bone pile based on the passed information from the hash map, mapRegionItems.
        
        // Remove bone pile from hash map.
        mapRegionItems.remove(getMapRegionItemKey(map_id, posX, posY, 
          HeroineEnum.ItemCategoryEnum.ITEM_CTGY_BONE_PILE));
        
    }
    
    // mapLocation = Location information associated with the bone pile to remove.  Includes map_id, x, and y.
    public void removeBonePileFirst(MapLocation mapLocation)
    {
        
        /*
        The function encapsulates logic for removing the first bone pile from the passed map location.
        The function actually assumes that only one bone pile exists at a given map location.
        
        Actions include setting the array-related value inactive and removing the entry from the
        hash map.
        */
        
        BonePile bonePile; // First bone pile at passed location.
        ArrayList<Object> objBonePileList; // List of bone piles at passed location.
        String key; // Key to use in get call to hash map with atlas items.
        
        // Specify key to use in get call to hash map with atlas items.
        key = getMapRegionItemKey(mapLocation.getMap_id(), mapLocation.getX(), mapLocation.getY(), 
          HeroineEnum.ItemCategoryEnum.ITEM_CTGY_BONE_PILE);
        
        // Get list of bone piles at passed location.
        objBonePileList = (ArrayList<Object>)mapRegionItems.get(key);
        
        // Get reference to first bone pile at passed location.
        bonePile = (BonePile)objBonePileList.get(0);
        
        // Set bone pile in array list as inactive.
        removeBonePile(bonePile.getBonePileIndex(), mapLocation.getMap_id());
            
        // Remove entry from mapRegionItems hash map.
        removeBonePileEntry(mapLocation.getMap_id(), mapLocation.getX(), mapLocation.getY());
        
    }
    
    // index = Array index of chest to "remove" -- set inactive -- (in the array list, chestList).
    // map_id = Map / region number in which to remove the chest.
    public void removeChest(int index, int map_id) {
        
        /*
        The function encapsulates logic for setting a chest (based on map / region and index) as 
        inactive in the array list.  The function also reduces the chest counts in the related lists.
        
        Notes:
        1.  A list of chests for a specific location can be retrieved with getChestList( map_id, posX, posY );
        2.  The hash map, mapRegionItems, stores a list of objects, by type, at each location.
        3.  Keys in the hash map use the format, map_id,x,y,*ITEM CATEGORY*.
        4.  *ITEM CATEGORY* = One of the enumerated values in HeroineEnum.ItemCategoryEnum.
        5.  The "value" in the hash map contains an array list of the specified item category.
        6.  For example, the "value" would contain one to many chests if category = ITEM_CTGY_CHEST.
        7.  The function does NOT remove the chest from the hash map, as one to many chests exist 
            for each entry.
        */
        
        // Set the chest inactive to simulate removing from the array list.
        chestList.get(index).setActiveInd(false);
        //chestList.remove(index);
        
        // Reduce chest count.
        chestCount--;
        
        // For the passed map / region, reduce number of chests stored in array list with quantities.
        itemCountList.get(map_id).itemCount_Chest--;
        
        // Reduce the total item count for the current region.
        itemTotalList.set(map_id, itemTotalList.get(map_id) - 1);
        
    }
    
    // tempChests = List of chests to remove.
    // map_id = Map / region number in which to remove the chests.
    // posX = X-coordinate associated with the chest(s).
    // posY = Y-coordinate associated with the chest(s).
    public void removeChests(ArrayList<Chest> tempChests, int map_id, int posX, int posY)
    {
        
        /*
        The function encapsulates logic for removing the passed chests from the 
        specified region, including for the hash map.
        The function actually sets the chests in the array list as inactive and removes
        the entry in the hash map.
        */
        
        ArrayList<Integer> removeList; // List of indices to remove (for chests).
        
        // Initialize array list.
        removeList = new ArrayList<>();
        
        /*
        System.out.println("\nBefore...");
        System.out.println("Chest list size: " + chestList.size());
        System.out.println("Chest count: " + chestCount);
        System.out.println("Item count: " + itemCountList.get(map_id).itemCount_Chest);
        System.out.println("Item total: " + itemTotalList.get(map_id));
        */
        
        // Loop through chests.
        tempChests.forEach((chest) -> {
            
            // Add chest to removal list.
            removeList.add(chest.getChestIndex());
        
        });
        
        // Loop through and set chests as inactive in array list.
        removeList.forEach((index) -> {

            // Remove chest from array list.
            removeChest((int)index, map_id);

        });
        
        // Remove entry from mapRegionItems hash map.
        removeChestEntry(map_id, posX, posY);
        
        /*
        System.out.println("\nAfter...");
        System.out.println("Chest list size: " + chestList.size());
        System.out.println("Chest count: " + chestCount);
        System.out.println("Item count: " + itemCountList.get(map_id).itemCount_Chest);
        System.out.println("Item total: " + itemTotalList.get(map_id));
        */
        
    }
    
    // map_id = Map / region number in which to remove the chest.
    // posX = X-coordinate associated with the chest(s).
    // posY = Y-coordinate associated with the chest(s).
    public void removeChestEntry(int map_id, int posX, int posY)
    {
        
        // The function removes the chest based on the passed information from the hash map, mapRegionItems.
        
        // Remove chest from hash map.
        mapRegionItems.remove(Integer.toString(map_id) + "," + Integer.toString(posX) + "," + 
          Integer.toString(posY) + "," + HeroineEnum.ItemCategoryEnum.ITEM_CTGY_CHEST);
        
    }
    
    // index = Array index of locked door to "remove" -- set inactive -- (in the array list, lockedDoorList).
    // map_id = Map / region number in which to remove the locked door.
    public void removeLockedDoor(int index, int map_id) {
        
        /*
        The function encapsulates logic for setting a locked door (based on map / region and index) as 
        inactive in the array list.  The function also reduces the locked door counts in the related lists.
        
        Notes:
        1.  The hash map, mapRegionItems, stores a list of objects, by type, at each location.
        2.  Keys in the hash map use the format, map_id,x,y,*ITEM CATEGORY*.
        3.  *ITEM CATEGORY* = One of the enumerated values in HeroineEnum.ItemCategoryEnum.
        4.  The "value" in the hash map contains an array list of the specified item category.
        5.  For example, the "value" would contain one to many locked doors if category = ITEM_CTGY_LOCKED_DOOR.
        7.  The function does NOT remove the locked door from the hash map.
        */
        
        // Set the locked door inactive to simulate removing from the array list.
        lockedDoorList.get(index).setActiveInd(false);
        //lockedDoorList.remove(index);
        
        // Reduce locked door count.
        lockedDoorCount--;
        
        // For the passed map / region, reduce number of locked doors stored in array list with quantities.
        itemCountList.get(map_id).itemCount_LockedDoor--;
        
        // Reduce the total item count for the current region.
        itemTotalList.set(map_id, itemTotalList.get(map_id) - 1);
        
    }
    
    // map_id = Map / region number in which to remove the locked door.
    // posX = X-coordinate associated with the locked door.
    // posY = Y-coordinate associated with the locked door.
    public void removeLockedDoorEntry(int map_id, int posX, int posY)
    {
        
        // The function removes the locked door based on the passed information from the hash map, 
        // mapRegionItems.
        
        // Remove locked door from hash map.
        mapRegionItems.remove(getMapRegionItemKey(map_id, posX, posY, 
          HeroineEnum.ItemCategoryEnum.ITEM_CTGY_LOCKED_DOOR));
        
    }
    
    // mapLocation = Location information associated with the locked door to remove.  Includes map_id, x, and y.
    public void removeLockedDoorFirst(MapLocation mapLocation)
    {
        
        /*
        The function encapsulates logic for removing the first locked door from the passed map location.
        The function actually assumes that only one locked door exists at a given map location.
        
        Actions include setting the array-related value inactive and removing the entry from the
        hash map.
        */
        
        LockedDoor lockedDoor; // First locked door at passed location.
        ArrayList<Object> objLockedDoorList; // List of locked doors at passed location.
        String key; // Key to use in get call to hash map with atlas items.
        
        // Specify key to use in get call to hash map with atlas items.
        key = getMapRegionItemKey(mapLocation.getMap_id(), mapLocation.getX(), mapLocation.getY(), 
          HeroineEnum.ItemCategoryEnum.ITEM_CTGY_LOCKED_DOOR);
        
        // Get list of locked doors at passed location.
        objLockedDoorList = (ArrayList<Object>)mapRegionItems.get(key);
        
        // Get reference to first locked door at passed location.
        lockedDoor = (LockedDoor)objLockedDoorList.get(0);
        
        // Set bone pile in array list as inactive.
        removeLockedDoor(lockedDoor.getLockedDoorIndex(), mapLocation.getMap_id());
        
        // Remove entry from mapRegionItems hash map.
        removeLockedDoorEntry(mapLocation.getMap_id(), mapLocation.getX(), mapLocation.getY());
        
    }
    
    // Getters and setters below...
    
    // whichAlterMap = Index number of map alteration event to return.  Base 0.
    public AlterMap getAlterMap(int whichAlterMap) {
        // The function returns information for the specified map alteration event.
        return alterMapList.get(whichAlterMap);
    }
    
    public ArrayList<AlterMap> getAlterMapList() {
        return alterMapList;
    }

    // whichBonePile = Index number of bone pile to return.  Base 0.
    public BonePile getBonePile(int whichBonePile) {
        // The function returns information for the specified bone pile.
        return bonePileList.get(whichBonePile);
    }
    
    public ArrayList<BonePile> getBonePileList() {
        return bonePileList;
    }

    // whichChest = Index number of chest to return.  Base 0.
    public Chest getChest(int whichChest) {
        // The function returns information for the specified chest.
        return chestList.get(whichChest);
    }
    
    public void adjChestCount(int chestCount) {
        this.chestCount += chestCount;
    }
    
    public void setChestCount(int chestCount) {
        this.chestCount = chestCount;
    }
    
    // map_id = Region / map location (number) for which to return chest(s).
    // x = X-coordinate within map for which to return chest(s).
    // y = Y-coordinate within map for which to return chest(s).
    public boolean getChestInd(int map_id, int x, int y) {
        
        // The function returns whether chest(s) exist at the passed location.
        
        String key; // Key to use when getting chest list.
        
        // Determine key.
        key = Integer.toString(map_id) + "," + Integer.toString(x) + "," + Integer.toString(y) + "," + 
          HeroineEnum.ItemCategoryEnum.ITEM_CTGY_CHEST;
        
        // Return whether chest(s) exist at passed location.
        return mapRegionItems.get(key) != null;
        
    }
    
    // map_id = Region / map location (number) for which to return chest(s).
    // x = X-coordinate within map for which to return chest(s).
    // y = Y-coordinate within map for which to return chest(s).
    public ArrayList<Chest> getChestList(int map_id, int x, int y) {
        
        /*
        The function returns the list of chests at the passed location.
        
        The function retrieves the chest list from the hash map, mapRegionItems.
        The hash map stores a list of objects, by type, at each location.
        Keys in the hash map use the format, map_id,x,y,*ITEM CATEGORY*.
        *ITEM CATEGORY* = One of the enumerated values in HeroineEnum.ItemCategoryEnum.
        The "value" in the hash map contains an array list of the specified item category.
        For example, the "value" would contain one to many chests if category = ITEM_CTGY_CHEST.
        */
        
        int count; // Number of chests at passed location.
        String key; // Key to use when getting chest list.
        ArrayList<Chest> temp; // Holder for list of chests at passed location.
        
        // Determine key.
        key = Integer.toString(map_id) + "," + Integer.toString(x) + "," + Integer.toString(y) + "," + 
          HeroineEnum.ItemCategoryEnum.ITEM_CTGY_CHEST;
        
        // Initialize array list.
        temp = new ArrayList<>();
        
        // If chests exist at passed location, then...
        if (mapRegionItems.get(key) != null)
        {
            
            // Chests exist at passed location.
            
            // Store number of chests at passed location.
            count = mapRegionItems.get(key).size();
            
            // Loop through chests at passed location.
            for (int counter = 0; counter < count; counter++)
            {
                // Add chest to list to pass back.
                temp.add((Chest)mapRegionItems.get(key).get(counter));
            }
        }
        
        // Pass back list of chests at specified location.
        return temp;
    
    }
    
    public ArrayList<Chest> getChestList() {
        return chestList;
    }
    
    // whichHayBale = Index number of hay bale to return.  Base 0.
    public HayBale getHayBale(int whichHayBale) {
        // The function returns information for the specified hay bale.
        return hayBaleList.get(whichHayBale);
    }
    
    public ArrayList<HayBale> getHayBaleList() {
        return hayBaleList;
    }
    
    public ArrayList<ItemCounts> getItemCountList() {
        return itemCountList;
    }

    public void adjItemCountChest(int map_id, int countAdj) {
        itemCountList.get(map_id).itemCount_Chest += countAdj;
    }
    
    public void adjItemTotal(int map_id, int countAdj) {
        itemTotalList.set(map_id, itemTotalList.get(map_id) + countAdj);
    }
    
    // whichLockedDoor = Index number of locked door to return.  Base 0.
    public LockedDoor getLockedDoor(int whichLockedDoor) {
        // The function returns information for the specified locked door.
        return lockedDoorList.get(whichLockedDoor);
    }
    
    public ArrayList<LockedDoor> getLockedDoorList() {
        return lockedDoorList;
    }

    public Map<String, Object> getMapJSON() {
        return mapJSON;
    }
    
    // map_id = Map / region number of item.
    // posX = X-coordinate associated with the item.
    // posY = Y-coordinate associated with the item.
    // itemCategoryEnum = Enumerated value for the item -- a value from the ItemCategoryEnum list.
    public String getMapRegionItemKey(int map_id, int posX, int posY, 
      HeroineEnum.ItemCategoryEnum itemCategoryEnum) {
        return Integer.toString(map_id) + "," + Integer.toString(posX) + "," + Integer.toString(posY) + "," + 
          itemCategoryEnum;
    }
    
    public HashMap<String, ArrayList<Object>> getMapRegionItems() {
        return mapRegionItems;
    }
    
    // whichSpecifiedEnemy = Index number of specific enemy to return.  Base 0.
    public SpecificEnemy getSpecifiedEnemy(int whichSpecificEnemy) {
        // The function returns information for the specific enemy.
        return specificEnemyList.get(whichSpecificEnemy);
    }
    
    // map_id = Region / map location (number) for which to return chest(s).
    // x = X-coordinate within map for which to return chest(s).
    // y = Y-coordinate within map for which to return chest(s).
    public boolean getSpecificEnemyInd(int map_id, int x, int y) {
        
        // The function returns whether specific enem(ies) exist at the passed location.
        
        String key; // Key to use when getting specific enemy list.
        
        // Determine key.
        key = Integer.toString(map_id) + "," + Integer.toString(x) + "," + Integer.toString(y) + "," + 
          HeroineEnum.ItemCategoryEnum.ITEM_CTGY_SPECIFIC_ENEMY;
        
        // Return whether speicifc enem(ies) exist at passed location.
        return mapRegionItems.get(key) != null;
        
    }
    
    // map_id = Region / map location (number) for which to return chest(s).
    // x = X-coordinate within map for which to return chest(s).
    // y = Y-coordinate within map for which to return chest(s).
    public ArrayList<SpecificEnemy> getSpecificEnemyList(int map_id, int x, int y) {
        
        /*
        The function returns the list of specific enemies at the passed location.
        
        The function retrieves the specific enemy list from the hash map, mapRegionItems.
        The hash map stores a list of objects, by type, at each location.
        Keys in the hash map use the format, map_id,x,y,*ITEM CATEGORY*.
        *ITEM CATEGORY* = One of the enumerated values in HeroineEnum.ItemCategoryEnum.
        The "value" in the hash map contains an array list of the specified item category.
        For example, the "value" would contain one to many specific enemies if 
        category = ITEM_CTGY_SPECIFIC_ENEMY.
        */
        
        int count; // Number of chests at passed location.
        String key; // Key to use when getting specific enemy list.
        ArrayList<SpecificEnemy> temp; // Holder for list of specific enemies at passed location.
        
        // Determine key.
        key = Integer.toString(map_id) + "," + Integer.toString(x) + "," + Integer.toString(y) + "," + 
          HeroineEnum.ItemCategoryEnum.ITEM_CTGY_SPECIFIC_ENEMY;
        
        // Initialize array list.
        temp = new ArrayList<>();
        
        // If specific enemies exist at passed location, then...
        if (mapRegionItems.get(key) != null)
        {
            
            // Specific enemies exist at passed location.
            
            // Store number of specific enemies at passed location.
            count = mapRegionItems.get(key).size();
            
            // Loop through specific enemies at passed location.
            for (int counter = 0; counter < count; counter++)
            {
                // Add specific enemies to list to pass back.
                temp.add((SpecificEnemy)mapRegionItems.get(key).get(counter));
            }
        }
        
        // Pass back list of specific enemies at specified location.
        return temp;
    
    }
    
    public ArrayList<SpecificEnemy> getSpecificEnemyList() {
        return specificEnemyList;
    }
    
}