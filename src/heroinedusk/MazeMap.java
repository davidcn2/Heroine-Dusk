package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

// Local project imports.
import core.AssetMgr;
import core.BaseActor;
import core.CoreEnum;
import core.ShakyActor;
import gui.CustomLabel;
import routines.UtilityRoutines;

// Java imports.
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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

public class MazeMap 
{

    /* 
    The class stores data for the current active region / map.
    
    Methods include:
    
    acquireChestContents:  Gives the contents of the chest(s) at the passed location to the player.
    addEvent_BonePileActor:  Adds events to the passed bone pile related tile (BaseActor).
    addEvent_ChestActor:  Adds events to the passed chest-related tile (BaseActor).
    addEvent_LockActor:  Adds events to the passed lock related tile (BaseActor).
    addEvent_TreasureActor:  Adds events to the passed treasure actor and label.
    check_random_encounter:  Checks and returns whether a random encounter occurs.  Performs
	  initialization related to any random encounter that occurs.
    determine_treasure_label_pos_y:  Places the treasure label.
    display_gold:  Encapsulates logic related to drawing a gold pile and the related label,
	  including fade effects.
    draw_treasure_first:  (Conditionally) displays the treasure and always shows the associated
	  label.
    draw_treasure_next:  Displays the treasure and always shows the associated label.
    draw_treasure_no_image:  Displays the treasure description when no image exists.
    eventExitMagic:  Encapsulates exit event logic related to items with associated action buttons.
    eventMouseMoveAction:  Encapsulates mouse move event logic related to items with associated
	  action buttons.
    fade_gold_pile:  Fades out the treasure group and gold pile, including associated actors.
    fade_treasure:  Fades all treasure / gold actors (including the label) and is called when 
      clicking on the last item.
    mazemap_bounds_check:  Checks to see if the passed location exists in the current region.
    mazemap_render:  Returns the tiles used to render the passed location.
    mazemap_render_tile:  Returns a base actor representing the passed tile in the passed location.
    mazemap_render_tile_side:  Returns an enumerated value (integer) representing the (side) tile 
      in the passed location.  Actually shows the side of the adjacent tile facing the player.
    minimap_render:  Returns the icons used to render the minimap for the current map / region.
    prepareSpecialTiles:  Configures properties for the special tile actors, such as the chest, 
	  bone pile, and lock.
    render_gold:  Displays the current gold pile (only the actors necessary to add up to the 
	  desired quantity).
    render_treasure_first:  Updates the image and placement for the treasure actor and label.
    render_treasure_label:  Updates the treasure description and position in the associated label.
    transformChestTile:  Transforms the tile associated with the chest at the passed location.
      Effectively replaces the tile with its equivalent in the background -- removing the chest.
    */
    
    // Declare object variables.
    private final Atlas atlas; // Reference to the atlas information.
    private final AtlasItems atlasItems; // Reference to the atlas items information.
    private ArrayList<HeroineEnum.ItemEnum> chestOtherItems; // Additional items in chest(s).
    private ArrayList<Integer> chestOtherItemsQty; // Quantity of each additional item in chest(s).
    private final RegionMap currentRegion; // Reference to current region / map.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private final ArrayList<Action> goldActions; // List of actions to apply to gold actors.
    private final HashMap<Integer, Integer> goldXRef; // Cross reference between amount of gold (key) and 
      // index in array list (value).  Array list = goldPile.
    private final HashMap<HeroineEnum.MinimapCursorEnum, TextureRegion> minimapCursorRegions; // Unique set of
      // texture regions used with minimap cursors.
    private final HashMap<HeroineEnum.MinimapEnum, TextureRegion> minimapRegions; // Unique set of texture
      // regions used with minimap.
    private final ArrayList<ArrayList<Float>> minimapDestX; // X-coordinates for destination of icons in stage.
    private final ArrayList<ArrayList<Float>> minimapDestY; // Y-coordinates for destination of icons in stage.
    private final ArrayList<ArrayList<Integer>> regionTiles; // List of tiles composing the current region.
      // Example for use -- get(x).get(y):  Integer x = regionTiles.get(0).get(0);
    private final ArrayList<Boolean> tileActiveInd; // Whether each tile is active.
    
    // Declare regular variables.
    private boolean bonePileActiveInd; // Whehter bone pile in square in front of player enabled.
      // Disabled in information view and after burning.
    private boolean chestActiveInd; // Whether chest in square in front of (NOT UNDER) player enabled.
    private int chestOtherItemIndex; // Array index of current additional chest item to show -- in 
      // chestOtherItems.
    private final float dest_x[]; // Array holding X-coordinate (relative to the bottom left corner) to display
      // tile texture region in stage -- adjusted by scale factor.  Excludes render offset.
    private final float dest_y[]; // Array holding Y-coordinate (relative to the bottom left corner) to display
      // tile texture region in stage -- adjusted by scale factor.  Excludes render offset.
    private int encounter_chance; // Current encounter chance -- increases (to a point) until combat occurs.
    private boolean goldPileActiveInd; // Whether gold pile active.
    private final boolean[] goldVisibleList; // Whether each gold actor visible.
    private boolean lockedDoorActiveInd; // Whether locked door in square in front of (NOT UNDER) player enabled.
    private final int map_id; // Current region / map location (number).
    private final float minimapIconSize; // Size of an icon in minimap (width and height match).
    private final float minimapOffsetX; // X-coordinate of lower left corner of minimap.
    private final float minimapOffsetY; // Y-coordinate of lower left corner of minimap.
    private float minimapHeight; // Height of the minimap, including the background.
    private float minimapWidth; // Width of the minimap, including the background.
    private final SecureRandom number; // Used for generating random numbers.
    private final String regionName; // Name of current region / map location.
      // Maps to one of the integers in the mapIdentifiers object in the atlas.
    private HeroineEnum.MusicEnum current_song; // Song associated with the current region / map location.
    private final int regionHeight; // Region height, in tiles.
    private final int regionWidth; // Region width, in tiles.
    
    // Declare constants.
    private final Color COLOR_MED_GRAY = new Color(0.50f, 0.50f, 0.50f, 1);
    private final int ENCOUNTER_INCREMENT = 5; // Amount by which encounter chance increases.
    private final int ENCOUNTER_MAX = 30; // Maximum encounter chance.
    private final String decFormatText000 = "000"; // Text used for decimal style used to format numbers as 000.
      // Examples:  1 > 001, 2 > 002, ...
    private final DecimalFormat decimalFormat000 = new DecimalFormat(decFormatText000); // Decimal style used to
      // format numbers as 000.  Examples:  1 > 001, 2 > 002, ...
    private final int TILE_COUNT = 25; // Number of tile actors.
    private final int TILE_COUNT_BASE_0 = 24; // Number of tile actors, base 0.
    private final int TILE_POS_TREASURE = 13; // Tile position of treasure (actor).
    private final int TILE_POS_TREASURE_GROUP = 14; // Tile position of treasure (group).
    private final int TILE_POS_CHEST = 15; // Tile position of chest.
    private final int TILE_POS_BONE_PILE = 16; // Tile position of bone pile.
    private final int TILE_POS_LOCK = 17; // Tile position of lock.
    private final int TREASURE_LABEL_POS_Y = 2; // How many pixels (adjusted by scale) to place label above
      // treasure.
    private final int TREASURE_POS_Y = 1; // Y-coordinate at which to place treasure when in same square 
      // as player.  Adjusted by scale factor.
    
    // hdg = Reference to Heroine Dusk (main) game.
    // map_id = Current map section in which player resides.
    // viewHeight = Height of the stage.
    public MazeMap(HeroineDuskGame hdg, int map_id, int viewHeight)
    {
        
        /*
        The constructor performs the following actions:
        
        1.  Initializes array lists and hash maps and allocate space for arrays.
        2.  Initializes combat engine.
        3.  Stores reference to main game class.
        4.  Populates hash map with gold pile cross reference.
        5.  Stores references to atlas region and item information.
        6.  Sets starting region / map location.
        7.  Stores region name.
        8.  Stores reference to current region / map.
        9.  Copies tiles for current region to class-level variable.
        10.  Stores region width and height -- in tiles.
        11.  Calculates and store destinations for tiles.
        12.  Populates hash maps with unique texture regions used with minimap.
        13.  Stores minimap icon size.
        14.  Calculates minimap offset (lower left corner) based on scale.
        15.  Calculates destination coordinates for minimap icons.
        16.  Adds elements to tile active array list.
        */
        
        ArrayList<Float> tempMinimapDestX; // Holder for x-coordinate values for current row (for placing minimap icons).
        ArrayList<Float> tempMinimapDestY; // Holder for y-coordinate values for current row (for placing minimap icons).
        float minimapDestPosX; // X-coordinate at which to place minimap icon.
        float minimapDestPosY; // Y-coordinate at which to place minimap icon.
        int counter; // Used to increment through tile region enumerations.
        
        // 1.  Set defaults and initialize array lists and hash maps.
        
        // Set basic defaults.
        encounter_chance = 0;
        goldPileActiveInd = false;
        goldVisibleList =  new boolean[]{false, false, false, false, false, false, false, false, false, false};
        
        // Start random number generator.
        number = new SecureRandom();
        
        // Disable events for objects in square in front of player (position 9).
        bonePileActiveInd = false;
        chestActiveInd = false;
        lockedDoorActiveInd = false;
        
        // Initialize array lists.
        goldActions = new ArrayList<>();
        regionTiles = new ArrayList<>();
        minimapDestX = new ArrayList<>();
        minimapDestY = new ArrayList<>();
        chestOtherItems = new ArrayList<>();
        chestOtherItemsQty = new ArrayList<>();
        tileActiveInd = new ArrayList<>();
        
        // Initialize hash maps.
        goldXRef = new HashMap<>();
        minimapRegions = new HashMap<>();
        minimapCursorRegions = new HashMap<>();
        
        // Allocate space for arrays and array lists.
        dest_x = new float[HeroineEnum.TileRegionEnum.values().length];
        dest_y = new float[HeroineEnum.TileRegionEnum.values().length];
        
        // 2.  Store reference to main game class.
        gameHD = hdg;
        
        // 3.  Populate hash map with gold pile cross reference.
        goldXRef.put(1, 0);
        goldXRef.put(2, 1);
        goldXRef.put(4, 2);
        goldXRef.put(8, 3);
        goldXRef.put(16, 4);
        goldXRef.put(32, 5);
        goldXRef.put(64, 6);
        goldXRef.put(128, 7);
        goldXRef.put(256, 8);
        goldXRef.put(512, 9);
        
        // 4.  Store reference to atlas region and items information.
        
        // Store reference to the atlas region information.
        this.atlas = gameHD.getAtlas();
        
        // Store reference to the atlas items information.
        this.atlasItems = gameHD.getAtlasItems();
        
        // 5.  Set starting region / map location.
        this.map_id = map_id; // 0;
        
        // 6.  Get region name.
        regionName = atlas.mapIdentifiersRev.get(this.map_id);
        
        // 7.  Store reference to current region / map.
        this.currentRegion = atlas.maps.get(regionName);
        
        System.out.println("Current region: " + regionName);
        
        // 8.  Copy tiles for current region. -- Actually, stores references.
        regionTiles.addAll(currentRegion.getRegionTiles());
        
        // 9.  Copy current region width and height -- in tiles.
        regionWidth = currentRegion.getRegionWidth();
        regionHeight = currentRegion.getRegionHeight();
        
        System.out.println("Region size: " + regionWidth + " by " + regionHeight);
        
        // 10.  Calculate and store destinations for tiles.
        
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
        
        // 11.  Populate hash maps with unique texture regions used with minimap.
        
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
        
        // 12.  Store minimap icon size.
        minimapIconSize = minimapRegions.get(HeroineEnum.MinimapEnum.MINIMAP_BLOCK_BLACK).getRegionWidth();
        
        // 13.  Calculate minimap offset (lower left corner) based on scale.
        minimapOffsetX = 2f * gameHD.getConfig().getScale();
        minimapOffsetY = viewHeight - (regionHeight * minimapIconSize) - (2f * gameHD.getConfig().getScale()); // 106f * gameHD.getConfig().getScale();
        
        // 14.  Calculate destination coordinates for minimap icons.
        
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
        
        // 15.  Add elements to active tile list.
        
        // Loop through and add elements to active tile list, defaulting to false.
        for (int tileCounter = 0; tileCounter <= TILE_COUNT_BASE_0; tileCounter++)
        {
            // Add tile active indicator -- default to false.
            tileActiveInd.add(false);
        }
        
    }
    
    // posX = X-coordinate in map / region of chest(s).
    // posY = Y-coordinate in map / region of chest(s).
    // heroineWeapon = BaseActor object that acts as the player weapon.
    // weaponLabel = Label showing current player weapon.
    // heroineArmor = BaseActor object that acts as the player armor.
    // armorLabel = Label showing current player armor.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // goldLabel = Label showing player gold.
    private ArrayList<AtlasItems.Chest> acquireChestContents(int posX, int posY, BaseActor heroineWeapon, 
      CustomLabel weaponLabel, BaseActor heroineArmor, CustomLabel armorLabel, CustomLabel hpLabel, 
      CustomLabel mpLabel, CustomLabel goldLabel)
    {
        
        // The function gives the contents of the chest(s) at the passed location to the player.
        
        int chestCount; // Number of chests.
        ArrayList<AtlasItems.Chest> chestList; // List of chests at the passed position.
        
        // Get list of chests at the passed position.
        chestList = gameHD.getAtlasItems().getChestList( map_id, posX, posY );

        // Reinitialize lists of extra chest items.
        chestOtherItems = new ArrayList<>();
        chestOtherItemsQty = new ArrayList<>();

        // Reset index for current element in extra chest items.
        chestOtherItemIndex = 0;

        // Set chest count starting value.  Base 0.
        chestCount = 0;

        // Loop through chests in list.
        for (AtlasItems.Chest chest : chestList)
        {

            // Give the primary item to the player.
            gameHD.getAvatar().takeItem(chest.getPrimaryItem(), heroineWeapon, weaponLabel, 
              heroineArmor, armorLabel, chest.getPrimaryItemCount(), gameHD.getAssetMgr(),
              hpLabel, mpLabel, goldLabel);

            // If in second or later chest, then...
            if (chestCount > 0)
            {

                // In second or later chest.

                // Add to lists of other chest items.
                chestOtherItems.add(chest.getPrimaryItem());
                chestOtherItemsQty.add(chest.getPrimaryItemCount());

            }

            // Give additional items to the player.
            
            // Loop through additional items in chest.
            for (HeroineEnum.ItemEnum itemEnum : chest.getAddlItemList())
            {

                // Give the additional item to the player.
                gameHD.getAvatar().takeItem(itemEnum, heroineWeapon, weaponLabel, 
                  heroineArmor, armorLabel, 1, gameHD.getAssetMgr(), hpLabel, mpLabel,
                  goldLabel);

                // Add to list of other chest items.
                chestOtherItems.add(itemEnum);
                chestOtherItemsQty.add(1);
                
            }

            // Increment chest count.
            chestCount++;

        } // End ... Loop through chests in list.
        
        // Return the list of chests at the passed position.
        return chestList;
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // mapActionButton = Hash map containing BaseActor objects that act as the action buttons.
    private void addEvent_BonePileActor(ArrayList<BaseActor> tiles, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButton)
    {
        
        // The function adds events to the passed bone pile related tile (BaseActor).
        // Events include touchDown, touchUp, mouseMoved, enter, and exit.
        
        InputListener tileEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        tileEvent = new InputListener()
            {
                
                boolean ignoreNextExitEvent; // Whether to ignore next exit event (used with touchUp / exit).
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user touches the screen or presses a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS. 
                    // Notes:  Occurs when user press down mouse on button.
                    // Notes:  Event (touchDown) necessary to reach touchUp.
                    
                    // Return a value.
                    return true;
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user lifts a finger or released a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
                    // Notes:  Occurs when user releases mouse on button.
                        
                    // If bone pile active, then...
                    if (bonePileActiveInd)
                    {
                        
                        // Bone pile active.
                    
                        // Flag to ignore next exit event.
                        ignoreNextExitEvent = true;
                        
                    }
                    
                } // End ... touchUp event.
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                @Override
                public boolean mouseMoved (InputEvent event, float x, float y)
                {
                
                    /*
                    The function occurs when the mouse cursor or a finger touch is moved over the actor and
                    a button is not down.  The event only occurs on the desktop.
                    
                    1.  When active and rolling over transparent pixel in image, return actor to normal 
                        color and set action button to not visible.
                         
                    2.  When active and rolling over a non-transparent pixel in image, show action button
                        centered on current mouse position.  If player has magic points remaining, set action
                        button to normal color.  Otherwise, if player has no magic points, set action button to
                        dark shade.
                    */
                    
                    // Call function to handle mouse move event logic.
                    return eventMouseMoveAction(bonePileActiveInd, 
                      HeroineEnum.ImgOtherEnum.IMG_OTHER_SKULL_PILE, x, y, tiles.get(TILE_POS_BONE_PILE), 
                      mapActionButton, HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN);
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // toActor = Reference to actor gaining focus.
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
                {
                    
                    /*
                    The function occurs when the mouse cursor or a finger touch is moved out of the actor.
                    
                    Notes:  On the desktop, the event occurs even when no mouse buttons are pressed 
                            (pointer will be -1).
                    Notes:  Occurs when mouse cursor or finger touch is moved out of the label.
                    
                    1.  When active and ignoring current exit, flag to process next event.
                    2.  When active and processing current exit, return item to no color if no active actions 
                        (fade).
                    3.  When active and processing current exit, set action button to not visible.
                    */
                    
                    // Call function to handle exit event logic.
                    ignoreNextExitEvent = eventExitMagic(ignoreNextExitEvent, bonePileActiveInd, 
                      tiles.get(TILE_POS_BONE_PILE), 
                      mapActionButton.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN));
                      
                } // End ... exit event.
                
            }; // End ... InputListener.
        
        // Add event to tile actor.
        tiles.get(TILE_POS_BONE_PILE).addListener(tileEvent);
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // viewWidth = Width of the stage.
    // treasureLabel = Reference to label with the treasure description.
    // heroineWeapon = BaseActor object that acts as the player weapon.
    // weaponLabel = Label showing current player weapon.
    // heroineArmor = BaseActor object that acts as the player armor.
    // armorLabel = Label showing current player armor.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // goldLabel = Label showing player gold.
    // regionLabel = Label showing the current region name.
    private void addEvent_ChestActor(ArrayList<BaseActor> tiles, ArrayList<BaseActor> goldPile, int viewWidth, 
      CustomLabel treasureLabel, BaseActor heroineWeapon, CustomLabel weaponLabel, BaseActor heroineArmor, 
      CustomLabel armorLabel, CustomLabel hpLabel, CustomLabel mpLabel, CustomLabel goldLabel, 
      CustomLabel regionLabel)
    {
        
        // The function adds events to the passed chest-related tile (BaseActor).
        // Events include touchDown, touchUp, mouseMoved, and exit.
        
        InputListener tileEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        tileEvent = new InputListener()
            {
                
                boolean ignoreNextExitEvent; // Whether to ignore next exit event (used with touchUp / exit).
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user touches the screen or presses a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS. 
                    // Notes:  Occurs when user press down mouse on button.
                    // Notes:  Event (touchDown) necessary to reach touchUp.
                    
                    // Return a value.
                    return true;
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user lifts a finger or released a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
                    // Notes:  Occurs when user releases mouse on button.
                    
                    ArrayList<AtlasItems.Chest> chestList; // List of chests at the tile.
                    int index1; // Position of first comma in virtual text.
                    int index2; // Position of second comma in virtual text.
                    int posX; // X-coordinate associated with the tile.
                    int posY; // Y-coordinate associated with the tile.
                    boolean treasureImageInd; // Whether treasure image exists for primary item in first chest.
                    
                    // If chest active, then...
                    if (chestActiveInd)
                    {
                        
                        // Chest active.
                        
                        // Flag to ignore next exit event.
                        ignoreNextExitEvent = true;
                        
                        // If clicking on a non-transparent pixel in the image, then...
                        if ( !gameHD.getAssetMgr().getPixmapTransparentInd(
                          HeroineEnum.ImgOtherEnum.IMG_OTHER_CHEST.getValue_Key(), x, y) )
                        {
                           
                            // Clicking on a non-transparent pixel in the image.
                            
                            System.out.println("Chest metadata: " + 
                              tiles.get(TILE_POS_CHEST).getVirtualString());
                            
                            // 1.  Get virtual information related to chest.
                            
                            // Get position of first comma in virtual text.
                            index1 = tiles.get(TILE_POS_CHEST).getVirtualString().indexOf(",");

                            // Get position of second comma in virtual text.
                            index2 = tiles.get(TILE_POS_CHEST).getVirtualString().indexOf(",", index1 + 1);

                            // Get x and y coordinates associated with the tile.
                            posX = Integer.valueOf( tiles.get(TILE_POS_CHEST).getVirtualString().substring(index1 + 1, 
                              index2) );
                            posY = Integer.valueOf( tiles.get(TILE_POS_CHEST).getVirtualString().substring(index2 + 1, 
                              tiles.get(TILE_POS_CHEST).getVirtualString().length()) );
                            
                            // 2.  Check whether chest or mimic.
                            
                            // If mimic, then...
                            if ( gameHD.getAtlasItems().getSpecificEnemyInd( map_id, posX, posY ) )
                            {
                                
                                // Encountered mimic(s).
                                
                                // 3.  Fade out the chest.
                            
                                // Set up an action to fade out the chest.
                                tiles.get(TILE_POS_CHEST).addAction_FadeOut(0.25f, 0.25f);
                                
                                System.out.println("Encountered mimic!");
                                
                            } // End ... If encountered mimic.
                            
                            else
                            {
                            
                                // Encountered chest(s).
                                
                                // 3.  Play sound.
                                
                                // Play coin sound.
                                gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_COIN);

                                // 4.  Fade out the chest.
                            
                                // Set up an action to fade out the chest.
                                tiles.get(TILE_POS_CHEST).addAction_FadeOut(0.25f, 0.75f);
                                
                                // 5.  Give chest contents to the player.

                                // Take chest contents.
                                chestList = acquireChestContents(posX, posY, heroineWeapon, weaponLabel, 
                                  heroineArmor, armorLabel, hpLabel, mpLabel, goldLabel);

                                // 6.  Perform actions on treasure actor and label.

                                // Display the first chest / item.
                                treasureImageInd = render_treasure_first( chestList.get(0).getPrimaryItem(), tiles, 
                                  goldPile, viewWidth, treasureLabel, chestList.get(0).getPrimaryItemCount() );

                                // If image available, draw treasure.
                                // Always show associated label.
                                draw_treasure_first(treasureImageInd, tiles, goldPile, treasureLabel, viewWidth, 1.00f, 
                                  regionLabel );

                                // 7.  Remove chests from array list and hash map in atlas items.

                                // Set chests in array list inactive and remove entry hash map associated with
                                // current location.
                                gameHD.getAtlasItems().removeChests(chestList, map_id, posX, posY);

                                // 8.  Transform tile associated with chest.

                                // Transform tile with chest to show similar image to background -- removing chest.
                                transformChestTile(posX, posY);
                                
                            } // End ... If encountered chest(s).
                            
                        } // End ... If clicking on a non-transparent pixel in the image.
                        
                    } // End ... If chest active.
                    
                } // End ... touchUp event.
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                @Override
                public boolean mouseMoved (InputEvent event, float x, float y)
                {
                
                    // The function occurs when the mouse cursor or a finger touch is moved over the actor and
                    // a button is not down.  The event only occurs on the desktop.
                    
                    // If chest active, then...
                    if ( chestActiveInd )
                    {
                        
                        // Chest active.
                        
                        // If fade not occurring, then...
                        if (tiles.get(TILE_POS_CHEST).getActionMapCount() == 0)
                        {
                            // Fade not occurring yet.
                
                            // If rolling over a transparent pixel in image, then...
                            if ( gameHD.getAssetMgr().getPixmapTransparentInd(
                              HeroineEnum.ImgOtherEnum.IMG_OTHER_CHEST.getValue_Key(), x, y) )
                            {

                                // Rolling over a transparent pixel in image.

                                // Return chest to normal color.
                                tiles.get(TILE_POS_CHEST).setColor(Color.WHITE);

                            }

                            else
                            {

                                // Rolling over a non-transparent pixel in image.

                                // Apply a light shade to the chest.
                                tiles.get(TILE_POS_CHEST).setColor(Color.LIGHT_GRAY);

                            }
                        
                        } // End ... If fade not occurring.
                        
                    } // End ... If chest active.
                    
                    // Return a value.
                    return true;
                    
                } // End ... mouseMoved event.
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // toActor = Reference to actor gaining focus.
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
                {
                    
                    // The function occurs when the mouse cursor or a finger touch is moved out of the actor.
                    
                    // Notes:  On the desktop, the event occurs even when no mouse buttons are pressed 
                    // (pointer will be -1).
                    // Notes:  Occurs when mouse cursor or finger touch is moved out of the label.
                    
                    // If chest active, then...
                    if ( chestActiveInd )
                    {
                        
                        // Chest active.
                    
                        // If ignoring next exit event, then...
                        if ( ignoreNextExitEvent )
                        {
                            // Ignoring next exit event.
                            
                            // Flag to process next exit event.
                            ignoreNextExitEvent = false;
                        }

                        // Otherwise, ...
                        else

                            // Process exit event.

                            // If fade not occurring, then...
                            if (tiles.get(TILE_POS_CHEST).getActionMapCount() == 0)
                            {
                                // Fade not occurring yet.
                                
                                // Return chest to normal color.
                                tiles.get(TILE_POS_CHEST).setColor(Color.WHITE);
                            }
                        
                    } // End ... If chest active.
                        
                } // End ... exit event.
                
            }; // End ... InputListener.
        
        // Add event to tile actor.
        tiles.get(TILE_POS_CHEST).addListener(tileEvent);
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // mapActionButton = Hash map containing BaseActor objects that act as the action buttons.
    private void addEvent_LockActor(ArrayList<BaseActor> tiles, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButton)
    {
        
        // The function adds events to the passed lock related tile (BaseActor).
        // Events include touchDown, touchUp, mouseMoved, and exit.
        
        InputListener tileEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        tileEvent = new InputListener()
            {
                
                boolean ignoreNextExitEvent; // Whether to ignore next exit event (used with touchUp / exit).
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user touches the screen or presses a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS. 
                    // Notes:  Occurs when user press down mouse on button.
                    // Notes:  Event (touchDown) necessary to reach touchUp.
                    
                    // Return a value.
                    return true;
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user lifts a finger or released a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
                    // Notes:  Occurs when user releases mouse on button.
                        
                    // If locked door active, then...
                    if (lockedDoorActiveInd)
                    {
                        
                        // Locked door active.
                    
                        // Flag to ignore next exit event.
                        ignoreNextExitEvent = true;
                        
                    }
                    
                } // End ... touchUp event.
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                @Override
                public boolean mouseMoved (InputEvent event, float x, float y)
                {
                
                    /*
                    The function occurs when the mouse cursor or a finger touch is moved over the actor and
                    a button is not down.  The event only occurs on the desktop.
                    
                    1.  When active and rolling over transparent pixel in image, return actor to normal 
                        color and set action button to not visible.
                         
                    2.  When active and rolling over a non-transparent pixel in image, show action button
                        centered on current mouse position.  If player has magic points remaining, set action
                        button to normal color.  Otherwise, if player has no magic points, set action button to
                        dark shade.
                    */
                    
                    // Call function to handle mouse move logic.
                    return eventMouseMoveAction(lockedDoorActiveInd, HeroineEnum.ImgOtherEnum.IMG_OTHER_LOCK, 
                      x, y, tiles.get(TILE_POS_LOCK), mapActionButton, 
                      HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK);
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // toActor = Reference to actor gaining focus.
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
                {
                    
                    /*
                    The function occurs when the mouse cursor or a finger touch is moved out of the actor.
                    
                    Notes:  On the desktop, the event occurs even when no mouse buttons are pressed 
                            (pointer will be -1).
                    Notes:  Occurs when mouse cursor or finger touch is moved out of the label.
                    
                    1.  When active and ignoring current exit, flag to process next event.
                    2.  When active and processing current exit, return item to no color if no active actions 
                        (fade).
                    3.  When active and processing current exit, set action button to not visible.
                    */
                    
                    // Call function to handle exit event logic.
                    ignoreNextExitEvent = eventExitMagic(ignoreNextExitEvent, lockedDoorActiveInd, 
                      tiles.get(TILE_POS_LOCK), 
                      mapActionButton.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK));
                    
                } // End ... exit event.
                
            }; // End ... InputListener.
        
        // Add event to tile actor.
        tiles.get(TILE_POS_LOCK).addListener(tileEvent);
        //lockActor.addListener(tileEvent);
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // treasureLabel = Reference to label with the treasure description.
    // viewWidth = Width of the stage.
    private void addEvent_TreasureActor(ArrayList<BaseActor> tiles, ArrayList<BaseActor> goldPile, 
      CustomLabel treasureLabel, int viewWidth)
    {
        
        // The function adds events to the passed treasure actor and label.
        // Clicking the tresaure actor causes the actor and label to fade out.
        // Events include touchDown, touchUp, enter, and exit.
        
        InputListener treasureEvent; // Events to add to passed objects.
        
        // Craft event logic to add to passed label.
        treasureEvent = new InputListener()
            {
                
                boolean ignoreNextExitEvent; // Whether to ignore next exit event (used with touchUp / exit).
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user touches the screen or presses a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS. 
                    // Notes:  Occurs when user press down mouse on button.
                    // Notes:  Event (touchDown) necessary to reach touchUp.
                    
                    // Return a value.
                    return true;
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user lifts a finger or released a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
                    // Notes:  Occurs when user releases mouse on button.
                    
                    HeroineEnum.ImgTreasureEnum imgTreasureEnum; // Enumerated value for treasure.
                    boolean switchFromGold; // Whether switching from gold to non-gold.
                    boolean switchToGold; // Whether switching from non-gold to gold.
                    
                    // Flag to ignore next exit event.
                    ignoreNextExitEvent = true;
                    
                    // Play click sound.
                    gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_CLICK);
                    
                    // If additional items remain to display, then...
                    if (chestOtherItemIndex < chestOtherItems.size())
                    {
                        
                        // Additional items remain to display.
                        
                        // Set defaults.
                        switchFromGold = false;
                        switchToGold = false;
                        
                        // If gold pile active, then...
                        if (goldPileActiveInd)
                        {
                            // Gold pile active.
                            
                            // Store whether switching from gold to non-gold.
                            switchFromGold = chestOtherItems.get(chestOtherItemIndex) != 
                              HeroineEnum.ItemEnum.ITEM_GOLD;
                            
                        }
                        
                        else
                        {
                            
                            // No gold pile exists.
                            
                            // If switching from non-gold to gold, then...
                            if (chestOtherItems.get(chestOtherItemIndex) == HeroineEnum.ItemEnum.ITEM_GOLD)
                                switchToGold = true;
                            
                        }
                        
                        // If item represents gold, then...
                        if (chestOtherItems.get(chestOtherItemIndex) == HeroineEnum.ItemEnum.ITEM_GOLD)
                        {
                            
                            // Item represents gold.
                            
                            // Display gold pile and label.
                            display_gold(tiles, goldPile, treasureLabel, viewWidth, switchToGold, true);
                            
                            // Move to the next item.
                            chestOtherItemIndex++;
                            
                        } // End ... If item represents gold.
                        
                        else
                        {
                            
                            // Item NOT gold.
                            
                            // Get treasure enumeration value.
                            imgTreasureEnum = 
                              chestOtherItems.get(chestOtherItemIndex).getValue_ImgTreasureEnum();
                            
                            // If enumeration value points at an image, then...
                            if (imgTreasureEnum != null)
                            {
                                // Enumeration value points at an image.
                                
                                // Display treasure (non-gold) and associated label.
                                draw_treasure_next(tiles, goldPile, treasureLabel, viewWidth, imgTreasureEnum, 
                                  switchFromGold, true);
                                
                                // Move to the next item.
                                chestOtherItemIndex++;
                                
                            } // End ... If enumeration value points at an image.
                            
                            else
                            {
                                
                                // Enumeration value lacks an image.
                                
                                // Display label associated with treasure.  No image exists.
                                draw_treasure_no_image(tiles, goldPile, treasureLabel);
                                
                                // Move to the next item.
                                chestOtherItemIndex++;
                                
                            } // End ... If enumeration value lacks an image.
                            
                        } // End ... If item NOT gold.
                        
                    } // End ... If additional items remain to display.
                    
                    else
                    {
                        
                        // At last item to display.
                        
                        // Fade all treasure-related actors (regular, gold, and label).
                        fade_treasure( tiles, goldPile, treasureLabel );
                        
                    } // End ... If no additional items remain to display.
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to actors and label.
        tiles.get(TILE_POS_TREASURE).addListener(treasureEvent);
        tiles.get(TILE_POS_TREASURE_GROUP).addListener(treasureEvent);
        treasureLabel.getLabel().addListener(treasureEvent);
        
    }
    
    // combat = Reference to combat engine.
    // enemyLabel = Reference to label showing enemy type.
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // infoButton = BaseActor object that acts as the information button.
    // facingLabel = Label showing direction player is facing.
    // regionLabel = Label showing the current region name.
    // enemy = ShakyActor object that acts as the enemy.
    // assetMgr = Reference to the asset manager class.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapActionButtonPosX = X-coordinate of each action button.
    // mapActionButtonPosY = Y-coordinate of bottom of each action button.
    public boolean check_random_encounter(Combat combat, CustomLabel enemyLabel, BaseActor infoButtonSelector, 
      CustomLabel hpLabel, CustomLabel mpLabel, BaseActor infoButton, CustomLabel facingLabel, 
      CustomLabel regionLabel, ShakyActor enemy, AssetMgr assetMgr, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled,
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosX,
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosY)
    {
        
        // The function checks and returns whether a random encounter occurs.
        // The function performs initialization related to any random encounter that occurs.
        
        boolean encounterInd; // Whether encounter occurs.
        HeroineEnum.EnemyEnum enemyEnum; // Enemy type encountered.
        int random; // Random number between 1 and 100.
        
        // Set defaults.
        encounterInd = false;
        
        // If one or more enemies exist in current region, then...
        if ( currentRegion.getEnemyCount() > 990 ) // 99990 )
        {
            
            // One or more enemies exist in current region.
            
            // Generate random number between one and one hundred.
            random = UtilityRoutines.generateStandardRnd(number, 1, 100);
            
            // If random number in range of encounter, then...
            if (random < encounter_chance)
            {
                
                // Random number in range of encounter.
                
                // Reset encounter chance.
                encounter_chance = 0;
                
                // Set game state as in combat.
                gameHD.setGameState(HeroineEnum.GameState.STATE_COMBAT);
                
                // Generate random number between one and number of enemies in current region.
                random = UtilityRoutines.generateStandardRnd(number, 0, 1);
                
                System.out.println("Enemy count: " + currentRegion.getEnemyCount());
                System.out.println("Random: " + random);
                
                // Store type of enemy encountered.
                enemyEnum = currentRegion.getEnemyList().get(random);
                
                // Initiate combat.
                combat.initiate_combat(enemyEnum, enemyLabel, infoButtonSelector, hpLabel, mpLabel, 
                  infoButton, facingLabel, regionLabel, enemy, assetMgr, mapActionButtons, 
                  mapActionButtonEnabled, mapActionButtonPosX, mapActionButtonPosY);
                
                // Temp.
                //gameHD.setGameState(HeroineEnum.GameState.STATE_EXPLORE);
                
            } // End ... If random number in range of encounter.
            
            else
            {
                
                // Random number resulted in NO encounter.
                
                // Increase encounter chance.
                encounter_chance += ENCOUNTER_INCREMENT;
                
                // If necessary, reduce encounter chance to maximum.
                encounter_chance = Math.min(encounter_chance, ENCOUNTER_MAX);
                
            } // End... If random number resulted in NO encounter.
            
        } // End ... If one or more enemies exist in current region.
        
        // Return whether encounter occurs.
        return encounterInd;
        
    }
    
    // treasureImageInd = Whether treasure image exists for current item.
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // treasureLabel = Reference to label with the treasure description.
    private float determine_treasure_label_pos_y(boolean treasureImageInd, ArrayList<BaseActor> tiles, 
      CustomLabel treasureLabel)
    {
        
        // The function places the treasure label.
        
        float pos_y; // Y-coordinate at which to set bottom of label.
        
        // If showing treasure, then...
        if (treasureImageInd)
        {

            // Showing treasure.

            // If rendering group, then...
            if (goldPileActiveInd)
            {

                // Rendering group.
                
                // If chest actor active, then...
                if (tileActiveInd.get(TILE_POS_CHEST))
                {

                    // Chest actor active.
                
                    // Set the vertical position of the label to center between the treasure group 
                    // and the bottom of the stage.
                    pos_y = (tiles.get(TILE_POS_TREASURE_GROUP).getY() - treasureLabel.getLabelHeight()) / 2 ;
                    
                }
                
                else
                {
                    
                    // Chest actor NOT "passed" (null).
                    
                    // Set the vertical position of the label to be slightly above the treasure group.
                    pos_y = tiles.get(TILE_POS_TREASURE_GROUP).getY() + 
                      tiles.get(TILE_POS_TREASURE_GROUP).getGroupHeight() + 
                      treasureLabel.getLabelHeight() + (gameHD.getConfig().getScale() * 1); //TREASURE_LABEL_POS_Y * gameHD.getConfig().getScale();
                    
                }

            }

            else
            {

                // Rendering non-group.

                // If chest actor active, then...
                if (tileActiveInd.get(TILE_POS_CHEST))
                {

                    // Chest actor active.
                
                    // Set the vertical position of the label to center between the treasure and 
                    // bottom of the stage.
                    pos_y = (tiles.get(TILE_POS_TREASURE).getY() - treasureLabel.getLabelHeight()) / 2;
                    
                }
                
                else
                {
                    
                    // Chest actor NOT "passed" (null).
                    
                    // Set the vertical position of the label to be slightly above the treasure.
                    pos_y = tiles.get(TILE_POS_TREASURE).getY() + tiles.get(TILE_POS_TREASURE).getHeight() + 
                      TREASURE_LABEL_POS_Y * gameHD.getConfig().getScale();
                    
                }

            } // End ... If rendering non-group.
            
        } // End ... If showing treasure.

        else
        {

            // NOT showing treasure.

            // Set the vertical position of the label to be slightly above the bottom of the stage.
            pos_y = gameHD.getConfig().getScale() * 4.125f;

        }
        
        // Return the vertical position of the label.
        return pos_y;
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // treasureLabel = Reference to label with the treasure description.
    // viewWidth = Width of the stage.
    // switchToGold = Whether switching from non-gold to gold.
    // treasureImageInd = Whether treasure image exists for current item.
    private void display_gold(ArrayList<BaseActor> tiles, ArrayList<BaseActor> goldPile, 
      CustomLabel treasureLabel, int viewWidth, boolean switchToGold, boolean treasureImageInd)
    {
        
        // The function encapsulates logic related to drawing a gold pile and the related label, including
        // fade effects.
        
        // Set vertical position of treasure group.
        
        // If chest actor active, then...
        if (tileActiveInd.get(TILE_POS_CHEST))
        {

            // Chest actor active.

            // Position treasure actor (group) at center of chest vertically.
            tiles.get(TILE_POS_TREASURE_GROUP).setY( tiles.get(TILE_POS_CHEST).getOriginY() );

        }

        else
        {

            // Chest actor NOT active.

            // Position treasure actor (group) vertically slightly above the bottom.
            tiles.get(TILE_POS_TREASURE_GROUP).setY( TREASURE_POS_Y * gameHD.getConfig().getScale() );

        }
        
        // Redraw / update gold pile and treasure group.
        render_gold(tiles, goldPile, viewWidth, chestOtherItemsQty.get(chestOtherItemIndex));

        // Remove existing actions on treasure group.
        tiles.get(TILE_POS_TREASURE_GROUP).removeActions();
        
        // If switching from treasure actor to gold pile, then...
        if (switchToGold)
        {

            // Switching from treasure actor to gold pile.

            // If texture for treasure actor initialized, then...
            if (tiles.get(TILE_POS_TREASURE).getRegion().getTexture() != null)
            {
                // Texture for treasure actor initialized.

                // Fade out treasure actor.
                tiles.get(TILE_POS_TREASURE).addAction_FadeOut( 0.00f, 0.50f );
            }

        }

        // Set up an action to fade in the treasure group.
        tiles.get(TILE_POS_TREASURE_GROUP).addAction_FadeIn( 0.50f, 0.50f );

        // Update the treasure description in the associated label and center horizontally.
        render_treasure_label( treasureLabel, treasureImageInd, tiles );
        
    }
    
    // treasureImageInd = Whether treasure image exists for primary item in first chest.
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // treasureLabel = Reference to label with the treasure description.
    // viewWidth = Width of the stage.
    // delay = Time, in seconds, before fade occurs.
    // regionLabel = Label showing the current region name.
    private void draw_treasure_first(boolean treasureImageInd, ArrayList<BaseActor> tiles, 
      ArrayList<BaseActor> goldPile, CustomLabel treasureLabel, int viewWidth, float delay, 
      CustomLabel regionLabel)
    {
        
        // The function (conditionally) displays the treasure.
        // The function always displays the associated label.
        // NOTE:  The function is only called when rendering the first item in the chest.
        
        float pos_y; // Y-coordinate at which to set bottom of label.
        
        // Store y-coordinate at which to set bottom of label.
        pos_y = determine_treasure_label_pos_y( treasureImageInd, tiles, treasureLabel );
        
        // Update the vertical position of the label.
        treasureLabel.setPosY( pos_y );
        
        // If showing treasure, then...
        if (treasureImageInd)
        {

            // Showing treasure.

            // If rendering group, then...
            if (goldPileActiveInd)
            {

                // Rendering group.

                // Remove existing actions on treasure group.
                tiles.get(TILE_POS_TREASURE_GROUP).removeActions();

                // Set up an action to fade in the treasure group.
                tiles.get(TILE_POS_TREASURE_GROUP).addAction_FadeIn( delay, 0.50f );

            }

            else
            {

                // Rendering non-group.

                // Remove existing actions on treasure actor.
                tiles.get(TILE_POS_TREASURE).removeActions();

                // Set up an action to fade in the treasure group.
                tiles.get(TILE_POS_TREASURE).addAction_FadeIn( delay, 0.50f );

            } // End ... If Rendering non-group.
            
        } // End ... If showing treasure.
        
        // Add events to treasure actor / group / label to display next item (or fade) upon click.
        addEvent_TreasureActor( tiles, goldPile, treasureLabel, viewWidth );
        
        // Clear the label with the region name.
        regionLabel.setLabelText("");
        
        // Remove actions on treasure label.
        treasureLabel.removeActions();
        
        // Set up an action to fade in the label.
        treasureLabel.addAction_FadeIn( delay, 0.50f );
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // treasureLabel = Reference to label with the treasure description.
    // viewWidth = Width of the stage.
    // imgTreasureEnum = Enumerated value for treasure.
    // switchFromGold = Whether switching from gold to non-gold.
    // treasureImageInd = Whether treasure image (regular or gold) exists for current item.
    private void draw_treasure_next(ArrayList<BaseActor> tiles, ArrayList<BaseActor> goldPile,
      CustomLabel treasureLabel, int viewWidth, HeroineEnum.ImgTreasureEnum imgTreasureEnum,
      boolean switchFromGold, boolean treasureImageInd)
    {
        
        // The function displays the treasure.
        // The function always displays the associated label.
        // NOTE:  The function is only called when rendering the second or later item in the chest.
        
        float treasureHeight; // Height to which to resize treasure.
        float treasureWidth; // Width to which to resize treasure.
        
        // Switch treasure actor to more than a group.
        tiles.get(TILE_POS_TREASURE).setGroupOnlyInd(false);

        // Determine width and height to resize treasure.

        // Magnify by scale factor.
        treasureWidth = gameHD.getConfig().getScale() * 
          gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()).getRegionWidth();
        treasureHeight = gameHD.getConfig().getScale() * 
          gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()).getRegionHeight();

        // If chest actor active, then...
        if (tileActiveInd.get(TILE_POS_CHEST))
        {

            // Chest actor active.

            // If treasure width greater than chest, then... > Limit to chest width.
            if (treasureWidth > tiles.get(TILE_POS_CHEST).getWidth())
                treasureWidth = tiles.get(TILE_POS_CHEST).getWidth();

            // If treasure height greater than chest, then... > Limit to chest height.
            if (treasureHeight > tiles.get(TILE_POS_CHEST).getHeight())
                treasureHeight = tiles.get(TILE_POS_CHEST).getHeight();

            // Take the lower of the treasure width and height.
            treasureWidth = Math.min(treasureWidth, treasureHeight);
            treasureHeight = treasureWidth;

        }

        // Remove existing treasure actor actions.
        tiles.get(TILE_POS_TREASURE).removeActions();

        // If switching from gold, then...
        if (switchFromGold)
        {

            // Switching from gold.

            // Flag gold pile as not active.
            goldPileActiveInd = false;
            
            // Fade out gold pile.
            fade_gold_pile( tiles, goldPile, 0.00f, 0.50f );
            
            // Update image for treasure actor.
            tiles.get(TILE_POS_TREASURE).setTextureRegion( 
              gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()) );

            // Set dimensions of treasure actor.
            tiles.get(TILE_POS_TREASURE).setWidth( treasureWidth );
            tiles.get(TILE_POS_TREASURE).setHeight( treasureHeight );

            // Set treasure origin to center to allow for proper rotation.
            tiles.get(TILE_POS_TREASURE).setOriginCenter();

            // If chest actor active, then...
            if (tileActiveInd.get(TILE_POS_CHEST))
            {

                // Chest actor active.

                // Position treasure actor at center of stage horizontally and chest 
                // vertically.
                tiles.get(TILE_POS_TREASURE).setPosition( (viewWidth - treasureWidth) / 2, 
                  tiles.get(TILE_POS_CHEST).getOriginY() );

            }

            else
            {

                // Chest actor NOT active.

                // Position treasure actor at center of stage horizontally and 
                // vertically slightly above the bottom.
                tiles.get(TILE_POS_TREASURE).setPosition( (viewWidth - treasureWidth) / 2, 
                  TREASURE_POS_Y * gameHD.getConfig().getScale() );

            }

            // Set treasure actor to not visible to allow for fade in effect.
            tiles.get(TILE_POS_TREASURE).setVisible( false );

            // Remove existing actions on treasure actor.
            tiles.get(TILE_POS_TREASURE).removeActions();

            // Add fade in effect to treasure actor.
            tiles.get(TILE_POS_TREASURE).addAction_FadeIn( 0.50f, 0.50f );

        }

        else
        {
            // Displaying another non-gold item.

            // If texture for treasure actor initialized, then...
            if (tiles.get(TILE_POS_TREASURE).getRegion().getTexture() != null)
            {
                // Texture for treasure actor initialized.

                // Remove existing actions on treasure actor.
                tiles.get(TILE_POS_TREASURE).removeActions();

                // Fade in the next treasure actor / image.
                /*
                tiles.get(TILE_POS_TREASURE).addAction_FadeIn( 0.50f, 0.50f, 
                  gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()), 
                  imgTreasureEnum.toString(), treasureWidth, treasureHeight );
                */
                tiles.get(TILE_POS_TREASURE).addAction_FadeIn( 3.50f, 3.50f, 
                  gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()), 
                  imgTreasureEnum.toString(), treasureWidth, treasureHeight );
            }

            else
            {

                // Texture for treasure actor NOT initialized.

                // Update image for treasure actor.
                tiles.get(TILE_POS_TREASURE).setTextureRegion( 
                  gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()) );

                // If chest actor active, then...
                if (tileActiveInd.get(TILE_POS_CHEST))
                {
                    // Chest actor active.

                    // Position treasure actor at center of stage horizontally and 
                    // chest vertically.
                    tiles.get(TILE_POS_TREASURE).setPosition( (viewWidth - treasureWidth) / 2, 
                      tiles.get(TILE_POS_CHEST).getOriginY() );
                }

                else
                {
                    // Chest actor not active.

                    // Position treasure actor at center of stage horizontally and 
                    // vertically slightly above the bottom.
                    tiles.get(TILE_POS_TREASURE).setPosition( (viewWidth - treasureWidth) / 2, 
                      TREASURE_POS_Y * gameHD.getConfig().getScale() );
                }

                // Resize treasure actor based on scale.
                tiles.get(TILE_POS_TREASURE).setWidth( treasureWidth );
                tiles.get(TILE_POS_TREASURE).setHeight( treasureHeight );

                // Set treasure origin to center to allow for proper rotation.
                tiles.get(TILE_POS_TREASURE).setOriginCenter();

                // Set up an action to fade in the treasure.
                tiles.get(TILE_POS_TREASURE).addAction_FadeIn( 0.50f, 0.50f );

            } // End ... If texture for treasure actor NOT initialized.

        } // End ... If displaying another non-gold item.

        // Update the treasure description in the associated label and center horizontally.
        render_treasure_label( treasureLabel, treasureImageInd, tiles);
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // treasureLabel = Reference to label with the treasure description.
    private void draw_treasure_no_image(ArrayList<BaseActor> tiles, ArrayList<BaseActor> goldPile,
      CustomLabel treasureLabel)
    {
        
        // The function displays the treasure description when no image exists.
        // Fades out any active treasure / gold.
        // The function always displays the label associated with the treasure.
        // NOTE:  The function is only called when rendering the second or later item in the chest.
        
        // If texture for treasure actor initialized, then...
        if (tiles.get(TILE_POS_TREASURE).getRegion().getTexture() != null)
        {
            // Texture for treasure actor initialized.

            // Fade out the treasure actor.
            tiles.get(TILE_POS_TREASURE).addAction_FadeOut( 0.50f, 0.50f );
        }

        // If gold pile active, then...
        if (goldPileActiveInd)
        {
            // Gold pile active.

            // Fade out gold pile.
            fade_gold_pile( tiles, goldPile, 0f, 0.5f );

            // Flag gold pile as inactive.
            goldPileActiveInd = false;
        }

        // Update the treasure description in the associated label and center 
        // horizontally.
        render_treasure_label( treasureLabel, false, tiles );
        
    }
    
    // ignoreNextExitEvent = Whether to ignore next exit event (used with touchUp / exit).
    // activeInd = Active indicator related to item (bone pile, lock, ...).
    // actor = Reference to BaseActor related to the item.
    // actionButton = Reference to the BaseActor for the action button associated with the item.
    private boolean eventExitMagic(boolean ignoreNextExitEvent, boolean activeInd, BaseActor actor,
      BaseActor actionButton)
    {
        
        /*
        The function encapsulates exit event logic related to items with associated action buttons.
        
        1.  When active and ignoring current exit, flag to process next event.
        2.  When active and processing current exit, return item to no color if no active actions (fade).
        3.  When active and processing current exit, set action button to not visible.
        */
        
        boolean ignore; // Return value -- whether to ignore next exit event (used with touchUp / exit).
        
        // Set defaults.
        ignore = ignoreNextExitEvent;
        
        // If item (bone pile, lock, ...) active, then...
        if (activeInd)
        {

            // Item (bone pile, lock, ...) active.
            
            // If ignoring next exit event, then...
            if (ignoreNextExitEvent)

                // Ignoring next exit event.

                // Flag to process next exit event.
                ignore = false;

            // Otherwise, ...
            else
            {

                // Process exit event.

                // If fade not occurring, then...
                if (actor.getActionMapCount() == 0)
                {

                    // Fade not occurring yet.

                    // Return item to normal color.
                    actor.setColor(Color.WHITE);

                }

                // Set action button to not visible.
                actionButton.setVisible(false);

            } // Processing exit event.

        } // End ... If item (bone pile, lock, ...) active.
        
        // Return whether to ignore next exit event (used with touchUp / exit).
        return ignore;
        
    }
    
    // activeInd = Active indicator related to item (bone pile, lock, ...).
    // imgOtherEnum = Enumerated value for item image.
    // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    // actor = Reference to BaseActor related to the item.
    // mapActionButton = Hash map containing BaseActor objects that act as the action buttons.
    // actionButtonEnum = Enumerated value for the action button related to the item.
    private boolean eventMouseMoveAction(boolean activeInd, HeroineEnum.ImgOtherEnum imgOtherEnum, float x,
      float y, BaseActor actor, Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButton,
      HeroineEnum.ActionButtonEnum actionButtonEnum)
    {
        
        /*
        The function encapsulates mouse move event logic related to items with associated action buttons.
        
        1.  When active and rolling over transparent pixel in image, return actor to normal color and 
            set action button to not visible.
        2.  When active and rolling over a non-transparent pixel in image, show action button centered
            on current mouse position.  If player has magic points remaining, set action button to
            normal color.  Otherwise, if player has no magic points, set action button to dark shade.
        */
        
        // If item (bone pile, lock, ...) active, then...
        if (activeInd)
        {

            // Item (bone pile, lock, ...) active.

            // If rolling over a transparent pixel in the image, then...
            if ( gameHD.getAssetMgr().getPixmapTransparentInd( imgOtherEnum.getValue_Key(), x, y) )
            {

                // Rolling over a transparent pixel in the image.

                // If fade not occurring, then...
                if (actor.getActionMapCount() == 0)
                {

                    // Fade not occurring yet.

                    // Return actor to normal color.
                    actor.setColor(Color.WHITE);

                }

                // Set related magic / action button to not visible.
                mapActionButton.get(actionButtonEnum).setVisible(false);
                
            }

            else
            {

                // Rolling over a non-transparent pixel in the image.

                // Set position of related magic / action button center on current mouse position.
                mapActionButton.get(actionButtonEnum).setX(actor.getX() + x - 
                  mapActionButton.get(actionButtonEnum).getWidth() / 2 );
                mapActionButton.get(actionButtonEnum).setY(actor.getY() + y - 
                  mapActionButton.get(actionButtonEnum).getHeight() / 2 );
                
                // If player has no magic left, then...
                if (gameHD.getAvatar().getMp() == 0)
                {

                    // Player has no magic left.

                    // Apply a dark shade to the button to signify not currently enabled.
                    mapActionButton.get(actionButtonEnum).setColor( COLOR_MED_GRAY );

                }

                else
                {

                    // Player has magic left.

                    // Remove shading from button to signify enabled.
                    mapActionButton.get(actionButtonEnum).setColor( Color.WHITE );

                }
                
                // Set button to visible.
                mapActionButton.get(actionButtonEnum).setVisible( true );
                
            } // End ... If rolling over a non-transparent pixel in the image.

        } // End ... If item (bone pile, lock, ...) active.

        // Tell calling function to not handle event.
        return false;
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // delay = Time, in seconds, before fade occurs.
    // fadeOut = Duration, in seconds, of fade.
    private void fade_gold_pile(ArrayList<BaseActor> tiles, ArrayList<BaseActor> goldPile, float delay, 
      float fadeOut)
    {
        
        // The function fades out the treasure group and gold pile, including associated actors.
        
        // Fade out visible actors in gold pile.
        goldPile.forEach((actor) -> {

            // If actor visible, then...
            if (actor.isVisible())
            {
                
                // Actor is vislble.
                
                // Fade out the gold actor.
                actor.addAction_FadeOut( delay, fadeOut );
                
            }
            
        });
        
        // Fade out the treasure group.
        tiles.get(TILE_POS_TREASURE_GROUP).addAction_FadeOut( delay, fadeOut );
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // treasureLabel = Reference to label with the treasure description.
    private void fade_treasure(ArrayList<BaseActor> tiles, ArrayList<BaseActor> goldPile, 
      CustomLabel treasureLabel)
    {
        
        // The function fades all treasure / gold actors (including the label) and is called when 
        // clicking on the last item.
        
        // If gold pile active, then...
        if (goldPileActiveInd)
        {
            // Gold pile active.

            // Fade out the gold pile, including all associated actors.
            fade_gold_pile( tiles, goldPile, 0f, 1f );

            // Flag gold pile as inactive.
            goldPileActiveInd = false;

        }

        // Otherwise, If texture for treasure actor initialized, then...
        else if (tiles.get(TILE_POS_TREASURE).getRegion().getTexture() != null)
        {
            // Texture for treasure actor initialized.

            // Fade out the treasure actor.
            tiles.get(TILE_POS_TREASURE).addAction_FadeOut( 0f, 1f );
        }

        // Set up an action to fade out the treasure label.
        treasureLabel.addAction_FadeOut(0f, 1f);
        
    }
    
    // pos_x = X-coordinate to use in bounds check.
    // pos_y = Y-coordinate to use in bounds check.
    private boolean mazemap_bounds_check(int pos_x, int pos_y)
    {
        
        // The function checks to see if the passed location exists in the current region.
        
        // Return whether passed location exists in the current reigon.
        return pos_x >= 0 && pos_y >= 0 && pos_x < regionWidth && pos_y < regionHeight;
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // x = X-coordinate within current region to render.
    // y = Y-coordinate within current region to render.
    // facing = Direction the player is facing.
    // viewWidth = Width of the stage.
    // treasureLabel = Reference to label with the treasure description.
    // heroineWeapon = BaseActor object that acts as the player weapon.
    // weaponLabel = Label showing current player weapon.
    // heroineArmor = BaseActor object that acts as the player armor.
    // armorLabel = Label showing current player armor.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // goldLabel = Label showing player gold.
    // regionLabel = Label showing the current region name.
    // statusLabel = General status label.
    // turnInd = Whether movement involved turning.
    // redrawInd = Whether redrawing screen -- like when waking screen or loading game.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    public ArrayList<BaseActor> mazemap_render(ArrayList<BaseActor> tiles, ArrayList<BaseActor> goldPile, 
      int x, int y, HeroineEnum.FacingEnum facing, int viewWidth, CustomLabel treasureLabel, 
      BaseActor heroineWeapon, CustomLabel weaponLabel, BaseActor heroineArmor, CustomLabel armorLabel, 
      CustomLabel hpLabel, CustomLabel mpLabel, CustomLabel goldLabel, CustomLabel regionLabel, 
      CustomLabel statusLabel, boolean turnInd, boolean redrawInd, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled)
    {
        
        /*
        The function returns the tiles used to render the passed location.
        The function also populates the hash map used as a cross reference between the positions and 
        array indices.
        
        Beyond building the tile array list, logic includes:
        
        1.  Adding event-supported chest when in square in front of player.
        2.  Adding event-supported skull pile in square in front of player.
        3.  Adding event-supported locked door in square in front of player.
        4.  Handling chest in same square as player.
        5.  Handling hay bale in same square as player.
        
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
        
        boolean adjInd; // Whether position adjustment occurred.
        boolean bonePileInd; // Whether bone pile exists immediately in front of player.
        boolean chestInd; // Whether chest exists immediately in front of player.
        ArrayList<AtlasItems.Chest> chestList; // List of chests at the passed position.
        boolean chestNearbyInd; // Whether chest exists either in current location or immediately in front 
          // of player.
        boolean lockedDoorInd; // Whether locked door exists immediately in front of player.
        int tileNbr; // Tile number at current (exact) location.
        boolean treasureImageInd; // Whether treasure image exists for primary item in first chest.
        String virtualString; // Virtual field (string) for the actor with the chest(s), bone pile(s), ...
        String virtualStringObjType; // Object type parsed from virtual field (string).
        
        // 1.  Set defaults.
        adjInd = false;
        chestInd = false;
        bonePileInd = false;
        lockedDoorInd = false;
        chestNearbyInd = false;
        chestActiveInd = false;
        bonePileActiveInd = false;
        
        // 2.  Reset properties of actors in array list.
        for (BaseActor actor : tiles)
        {
            
            actor.setActorName(null);
            actor.setVirtualInt(null);
            actor.setVirtualString(null);
            actor.setVisible(false);
            actor.removeActions();
            
            // Return actor to normal color.
            actor.setColor(Color.WHITE);
            
            // Remove transparency (results from actions).
            actor.getColor().a = 1.0f;
            
            // Remove events.
            for (EventListener temp : actor.getListeners())
            {
                actor.removeListener(temp);
            }
            
        }
        
        // 3.  Reset active indicators for tiles.
        for (int tileCounter = 0; tileCounter <= TILE_COUNT_BASE_0; tileCounter++)
        {
            // Reset active indicator for current tile in loop.
            tileActiveInd.set(tileCounter, false);
        }
        
        // 4.  Prepare tiles for rendering.
        
        System.out.println("\nX: " + x + ", Y: " + y + ", Facing: " + facing);
        
        // Depending on direction facing, ...
        switch (facing) {
            
            case NORTH:
                
                // Render back row.
                mazemap_render_tile( x - 2, y - 2, 0, -2, -2, tiles, x, y, facing );
                mazemap_render_tile( x + 2, y - 2, 1, +2, -2, tiles, x, y, facing );
                mazemap_render_tile( x - 1, y - 2, 2, -1, -2, tiles, x, y, facing );
                mazemap_render_tile( x + 1, y - 2, 3, +1, -2, tiles, x, y, facing );
                mazemap_render_tile( x    , y - 2, 4,  0, -2, tiles, x, y, facing );
                
                // Render middle row.
                mazemap_render_tile( x - 2, y - 1, 5, -2, -1, tiles, x, y, facing );
                mazemap_render_tile( x + 2, y - 1, 6, +2, -1, tiles, x, y, facing );
                mazemap_render_tile( x - 1, y - 1, 7, -1, -1, tiles, x, y, facing );
                mazemap_render_tile( x + 1, y - 1, 8, +1, -1, tiles, x, y, facing );
                mazemap_render_tile( x    , y - 1, 9,  0, -1, tiles, x, y, facing );
                
                // Render front row.
                mazemap_render_tile( x - 1, y, 10, -1, 0, tiles, x, y, facing );
                mazemap_render_tile( x + 1, y, 11, +1, 0, tiles, x, y, facing );
                mazemap_render_tile( x    , y, 12,  0, 0, tiles, x, y, facing );
                
                // Exit selector.
                break;
                
            case SOUTH: // Starting direction.
                
                // Render back row.
                mazemap_render_tile( x + 2, y + 2, 0, +2, +2, tiles, x, y, facing );
                mazemap_render_tile( x - 2, y + 2, 1, -2, +2, tiles, x, y, facing );
                mazemap_render_tile( x + 1, y + 2, 2, +1, +2, tiles, x, y, facing );
                mazemap_render_tile( x - 1, y + 2, 3, -1, +2, tiles, x, y, facing );
                mazemap_render_tile( x    , y + 2, 4,  0, +2, tiles, x, y, facing );
                
                // Render middle row.
                mazemap_render_tile( x + 2, y + 1, 5, +2, +1, tiles, x, y, facing );
                mazemap_render_tile( x - 2, y + 1, 6, -2, +1, tiles, x, y, facing );
                mazemap_render_tile( x + 1, y + 1, 7, +1, +1, tiles, x, y, facing );
                mazemap_render_tile( x - 1, y + 1, 8, -1, +1, tiles, x, y, facing );
                mazemap_render_tile( x    , y + 1, 9,  0, +1, tiles, x, y, facing );
                
                // Render front row.
                mazemap_render_tile( x + 1, y, 10, +1, 0, tiles, x, y, facing );
                mazemap_render_tile( x - 1, y, 11, -1, 0, tiles, x, y, facing );
                mazemap_render_tile( x    , y, 12,  0, 0, tiles, x, y, facing );
                
                /*
                
                // Check for rendering portal in position 12, then...
                
                // If tile (in position 12) exists in current region, then...
                if (mazemap_bounds_check(x, y))
                {

                    // Tile exists in current region.
                    
                    // If current location contains portal, then...
                    if ( HeroineEnum.ImgTileEnum.valueOf(regionTiles.get(y).get(x)) == 
                      HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_DOOR )
                    {
                        
                        // Current location contains portal.
                        
                        // Switch positions 9 and 12.
                        mazemap_render_tile( x, y    ,  9, 0,  0, tiles ); // Middle row.
                        mazemap_render_tile( x, y + 1, 12, 0, +1, tiles ); // Front row.
                        
                        // Flag adjustment as occurring.
                        adjInd = true;
                        
                    }
                    
                } // End ... If tile (in position 12) exists in current region.
                
                // If no adjustments yet and tiles (in positions 9, 10, and 11) exist in current region, then...
                if (!adjInd && mazemap_bounds_check(x, y + 1) && mazemap_bounds_check(x - 1, y) && 
                    mazemap_bounds_check(x + 1, y))
                {
                    
                    // No adjustments yet and tiles (in positions 9, 10, and 11) exist in current region.
                    
                    // If position one square ahead is a door and those to left and right are walls, then...
                    if (HeroineEnum.ImgTileEnum.valueOf(regionTiles.get(y + 1).get(x)) == 
                      HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_DOOR && 
                      HeroineEnum.ImgTileEnum.valueOf(regionTiles.get(y).get(x - 1)) == 
                      HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_WALL &&
                      HeroineEnum.ImgTileEnum.valueOf(regionTiles.get(y).get(x + 1)) == 
                      HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_WALL) 
                    {
                        
                        // Position one square ahead of current location contains portal and those to left and
                        // right are walls.
                        
                        // Adjust positions 4 and 9.
                        mazemap_render_tile( x, y + 1, 4, 0, +1, tiles ); // Switch to show position 9.
                        mazemap_render_tile( x, y    , 9, 0, +0, tiles ); // Switch to show position 12.
                        
                        // Flag adjustment as occurring.
                        adjInd = true;
                        
                    } // End ... If position one square ahead of current location contains portal and those to 
                    // left and right are walls.
                    
                } // End ... No adjustments yet and tiles (in positions 9, 10, and 11) exist in current region.
                
                */
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Render back row.
                mazemap_render_tile( x + 2, y - 2, 0, +2, -2, tiles, x, y, facing );
                mazemap_render_tile( x + 2, y + 2, 1, +2, +2, tiles, x, y, facing );
                mazemap_render_tile( x + 2, y - 1, 2, +2, -1, tiles, x, y, facing );
                mazemap_render_tile( x + 2, y + 1, 3, +2, +1, tiles, x, y, facing );
                mazemap_render_tile( x + 2, y    , 4, +2,  0, tiles, x, y, facing );
                
                // Render middle row.
                mazemap_render_tile( x + 1, y - 2, 5, +1, -2, tiles, x, y, facing );
                mazemap_render_tile( x + 1, y + 2, 6, +1, +2, tiles, x, y, facing );
                mazemap_render_tile( x + 1, y - 1, 7, +1, -1, tiles, x, y, facing );
                mazemap_render_tile( x + 1, y + 1, 8, +1, +1, tiles, x, y, facing );
                mazemap_render_tile( x + 1, y    , 9, +1,  0, tiles, x, y, facing );
                
                // Render front row.
                mazemap_render_tile( x, y - 1, 10, 0, -1, tiles, x, y, facing );
                mazemap_render_tile( x, y + 1, 11, 0, +1, tiles, x, y, facing );
                mazemap_render_tile( x,     y, 12, 0,  0, tiles, x, y, facing );
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Render back row.
                mazemap_render_tile( x - 2, y + 2, 0, -2, +2, tiles, x, y, facing );
                mazemap_render_tile( x - 2, y - 2, 1, -2, -2, tiles, x, y, facing );
                mazemap_render_tile( x - 2, y + 1, 2, -2, +1, tiles, x, y, facing );
                mazemap_render_tile( x - 2, y - 1, 3, -2, -1, tiles, x, y, facing );
                mazemap_render_tile( x - 2, y    , 4, -2,  0, tiles, x, y, facing );
                
                // Render middle row.
                mazemap_render_tile( x - 1, y + 2, 5, -1, +2, tiles, x, y, facing );
                mazemap_render_tile( x - 1, y - 2, 6, -1, -2, tiles, x, y, facing );
                mazemap_render_tile( x - 1, y + 1, 7, -1, +1, tiles, x, y, facing );
                mazemap_render_tile( x - 1, y - 1, 8, -1, -1, tiles, x, y, facing );
                mazemap_render_tile( x - 1, y    , 9, -1,  0, tiles, x, y, facing );
                
                // Render front row.
                mazemap_render_tile( x, y + 1, 10, 0, +1, tiles, x, y, facing );
                mazemap_render_tile( x, y - 1, 11, 0, -1, tiles, x, y, facing );
                mazemap_render_tile( x, y    , 12, 0,  0, tiles, x, y, facing );  
                
                // Exit selector.
                break;
                
            default:
                
                // Display warning message.
                System.out.println("Warning:  Player facing in invalid direction!");
                
                // Exit selector.
                break;
                
            } // Depending on direction facing...
        
        // 5.  Check whether objects exist immediately in front of player.
        
        // Key = Position (related to tile image offset -- where to crop picture).
        // Value = Index in array list of base actors returned (non-null tiles).
        
        // Loop through background tiles.
        for (int tileCounter = 0; tileCounter <= 12; tileCounter++)
        {
            
            // If tile active, then...
            if (tileActiveInd.get(tileCounter))
            {
                
                // Tile active.
        
                // Check whether objects exist immediately in front of player.

                // Copy virtual string.
                virtualString = tiles.get(tileCounter).getVirtualString();
                
                // If tile metadata five characters or more, then...
                if (virtualString.length() >= 5)
                {

                    // Tile metadata five characters or more.

                    // Store object type -- extracting information from virtual string associated with tile.
                    virtualStringObjType = virtualString.substring(0, virtualString.indexOf("|"));

                    // Depending on object type...
                    switch (virtualStringObjType) {

                        case "Chest":

                            // Tile associated with chest.

                            // Flag chest.
                            chestInd = true;

                            // Copy virtual string.
                            tiles.get(TILE_POS_CHEST).setVirtualString(virtualString);
                            
                            // Set chest tile as active.
                            tileActiveInd.set(TILE_POS_CHEST, true);

                            // Exit loop.
                            break;

                        case "Bone Pile":

                            // Tile associated with bone pile.

                            // Flag bone pile.
                            bonePileInd = true;

                            // Copy virtual string.
                            tiles.get(TILE_POS_BONE_PILE).setVirtualString(virtualString);
                            
                            // Set bone pile tile as active.
                            tileActiveInd.set(TILE_POS_BONE_PILE, true);

                            // Exit loop.
                            break;

                        case "Locked Door":

                            // Tile associated with locked door.

                            // Flag locked door.
                            lockedDoorInd = true;

                            // Copy virtual string.
                            tiles.get(TILE_POS_LOCK).setVirtualString(virtualString);
                            
                            // Set lock tile as active.
                            tileActiveInd.set(TILE_POS_LOCK, true);

                            // Exit loop.
                            break;

                        default:

                            // Display warning message.
                            System.out.println("Warning:  Found an unknown object!");

                            // Exit loop.
                            break;

                    } // End ... Depending on object type.

                } // End ... If tile metadata five characters or more.
            
            } // End ... If tile active.
            
        } // End ... Loop through background tiles.
        
        // 6.  If necessary, add actors for chest and treasure.
        
        // Get tile number at current location.
        tileNbr = regionTiles.get(y).get(x);
        
        // If chest exists in current location or immediately in front of player, then flag chest as nearby.
        if (tileNbr == HeroineEnum.ImgTileEnum.IMG_TILE_CHEST_EXTERIOR.getValue() ||
          tileNbr == HeroineEnum.ImgTileEnum.IMG_TILE_CHEST_INTERIOR.getValue() || chestInd)
            chestNearbyInd = true;
        
        // If chest nearby, then...
        if (chestNearbyInd)
        {
            
            // Chest nearby -- in current location or immediately in front of player.
            
            // Create new BaseActor objects for the treasure and group, setting most properties when opening 
            // the chest.
            
            // Set treasure tile as active.
            tileActiveInd.set(TILE_POS_TREASURE, true);

            // Set treasure group tile as active.
            tileActiveInd.set(TILE_POS_TREASURE_GROUP, true);
            
        } // End ... If chest nearby.
        
        // If adding actor for chest -- immediately in front of player, then...
        if (chestInd)
        {
            
            // Adding actor for chest -- immediately in front of player.
            
            // Add events to actor.
            addEvent_ChestActor( tiles, goldPile, viewWidth, treasureLabel, heroineWeapon, weaponLabel, 
              heroineArmor, armorLabel, hpLabel, mpLabel, goldLabel, regionLabel );
            
            // Set chest actor as visible.
            tiles.get(TILE_POS_CHEST).setVisible(true);
            
        } // End ... If adding actor for chest -- immediately in front of player.
        
        // 7.  If necessary, add actor for bone pile.
        //     If no bone pile exists, disable burn button.
        
        // If bone pile exists immediately in front of player, then...
        if ( bonePileInd )
        {
            
            // Bone pile exists immediately in front of player.
            
            System.out.println("Bone pile exists immediately in front of player.");
            
            // If player knows burn spell and has sufficient magic points for casting, then...
            if (gameHD.getAvatar().getSpellCastInd(HeroineEnum.SpellEnum.SPELL_BURN))
            {
                
                // Player knows burn spell and has sufficient magic points for casting.
            
                // Add events to actor.
                addEvent_BonePileActor(tiles, mapActionButtons );
                
            }
            
            // Set bone pile actor as visible.
            tiles.get(TILE_POS_BONE_PILE).setVisible(true);
            
            // If sufficient magic points, set burn button shading to indicate enabled.
            if (gameHD.getAvatar().getMp() > 0 && mapActionButtons.size() > 0)
            {
                
                // Player has sufficient magic points to cast burn spell.
                // Include check for elements in hash map (empty when application starts).
                
                // Shade burn button to indicate enabled.
                mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setColor( Color.WHITE );
                
            }
            
            // Enable burn button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN, true);
            
        } // End ... If bone pile exists immediately in front of player.
        
        else
        {
            
            // Bone pile DOES NOT exist immediately in front of player.
            
            // If elements exist in hash map (empty when application starts), then...
            if (mapActionButtons.size() > 0)
            {
                
                // Shade burn button to indicate disabled.
                mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setColor( Color.DARK_GRAY );
                
            }
            
            // Disable burn button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN, false);
            
        }
        
        // 8.  If necessary, add actor for lock.
        //     If no locked door exists, disable unlock button.
        
        // If locked door exists immediately in front of player, then...
        if ( lockedDoorInd )
        {
            
            // Locked door exists immediately in front of player.
            
            System.out.println("Locked door exists immediately in front of player.");
            
            // If player knows unlock spell and has sufficient magic points for casting, then...
            if (gameHD.getAvatar().getSpellCastInd(HeroineEnum.SpellEnum.SPELL_UNLOCK))
            {
                
                // Player knows unlock spell and has sufficient magic points for casting.
                
                // Add events to actor.
                addEvent_LockActor(tiles, mapActionButtons );
                
            }
            
            // Set lock actor as visible.
            tiles.get(TILE_POS_LOCK).setVisible(true);
            
            // If sufficient magic points, set unlock button shading to indicate enabled.
            // Include check for elements in hash map (empty when application starts).
            if (gameHD.getAvatar().getMp() > 0 && mapActionButtons.size() > 0)
            {
                
                // Player has sufficient magic points to cast unlock spell.
                
                // Shade unlock button to indicate enabled.
                mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setColor( Color.WHITE );
                
            }
            
            // Enable unlock button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, true);
            
        } // End ... If locked door exists immediately in front of player.
        
        else
        {
            
            // Locked door DOES NOT exist immediately in front of player.
            
            // If elements exist in hash map (empty when application starts), then...
            if (mapActionButtons.size() > 0)
            {
                
                // Shade unlock button to indicate disabled.
                mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setColor( Color.DARK_GRAY );
                
            }
            
            // Disable unlock button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, false);
            
        }
        
        // 9.  Handle objects that exist in current location (at feet of player).
        
        // If player has already moved, is headed forward or backward, and a redraw is not occurring, then...
        if ( gameHD.getAvatar().getMovedInd() && !turnInd && !redrawInd )
        {
            
            // Player has already moved, is headed forward or backward, and a redraw is not occurring.
            
            // Depending on tile in current location...
            switch ( HeroineEnum.ImgTileEnum.valueOf(tileNbr) ) {
                
                case IMG_TILE_CHEST_EXTERIOR:
                case IMG_TILE_CHEST_INTERIOR:

                    // Chest exists in current location.

                    // Disable chest events -- related to square in front of player (not current location).
                    chestActiveInd = false;
                    
                    // If player encountered mimic(s), then...
                    if ( gameHD.getAtlasItems().getSpecificEnemyInd( map_id, x, y ) )
                    {
                        
                        // Player encountered mimic(s).
                        
                        System.out.println("Encountered mimic!");
                        
                    }
                    
                    else
                    {
                        
                        // Player encounter regular chest(s).
                        
                        // Play coin sound.
                        gameHD.getSounds().playSound( HeroineEnum.SoundEnum.SOUND_COIN );

                        // Take chest contents.
                        chestList = acquireChestContents( x, y, heroineWeapon, weaponLabel, heroineArmor, 
                          armorLabel, hpLabel, mpLabel, goldLabel );

                        // Display the first chest / item.
                        treasureImageInd = render_treasure_first( chestList.get(0).getPrimaryItem(), tiles, 
                          goldPile, viewWidth, treasureLabel, chestList.get(0).getPrimaryItemCount() );

                        // If image available, draw treasure.
                        // Always show associated label.
                        draw_treasure_first( treasureImageInd, tiles, goldPile, treasureLabel, viewWidth, 
                          0.00f, regionLabel );

                        // Set chests in array list inactive and remove entry from hash map associated with 
                        // current location.
                        gameHD.getAtlasItems().removeChests( chestList, map_id, x, y );

                        // Transform tile with chest to show similar image to background -- removing chest.
                        transformChestTile( x, y );
                        
                    }
                    
                    // Exit selector.
                    break;

                case IMG_TILE_HAY_PILE:

                    // Hay pile exists in current location.

                    // Play coin sound.
                    gameHD.getSounds().playSound( HeroineEnum.SoundEnum.SOUND_COIN );
                    
                    // Hide region name label.
                    regionLabel.applyVisible(false);
                    
                    // Display the "rest" text.
                    statusLabel.setLabelText_Center( "YOU REST FOR AWHILE." );

                    // Show status label and remove associated actions.
                    statusLabel.removeActions();
                    statusLabel.applyVisible( true );
                    
                    // Set up fade effect for status label.
                    statusLabel.addAction_Fade();
                    
                    // Cause the player to sleep -- restore hp and mp and set new respawn point.
                    gameHD.getAvatar().avatar_sleep( hpLabel, mpLabel );
                    
                    // Exit selector.
                    break;

                default:

                    // No special logic.

                    // Hide the general status label.
                    statusLabel.applyVisible(false);

                    // Exit selector.
                    break;

            } // End ... Depending on tile in current location.
            
        } // End ... If player has already moved.
        
        // 10.  Return the array list with the base actors for the tiles.
        return tiles;
        
    }
    
    // pos_x = Y-coordinate of tile to render.
    // pos_y = X-coordinate of tile to render.
    // position = Position information for tile to render (offset within source graphic).
    // adj_x = Adjustment to X-coordinate used to reach rendering location.
    // adj_y = Adjustment to Y-coordinate used to reach rendering location.
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // x = X-coordinate within current region to render.  Usually x-coordinate of player position.
    // y = Y-coordinate within current region to render.  Usually y-coordinate of player position.
    // facing = Direction the player is facing.
    public BaseActor mazemap_render_tile(int pos_x, int pos_y, int position, int adj_x, int adj_y,
      ArrayList<BaseActor> tiles, int x, int y, HeroineEnum.FacingEnum facing)
    {
        // The function returns a base actor representing the tile in the passed location.
        // Note: x, y flipped to ease map making.
        
        BaseActor temp; // Holder for the BaseActor to return.
        int tileNbr; // Tile number to render.
        Integer tileNbr_Side; // Tile number coming from side-related override.
        String key; // Key for the tile to display -- in the asset manager.
        String virtualString; // Virtual text to associate with BaseActor.
        
        // Set defaults.
        temp = tiles.get(position);
        virtualString = "";
        
        // Check for side-related tile override.
        tileNbr_Side = mazemap_render_tile_side(pos_x, pos_y, x, y, facing, true);
        
        // If tile exists in current region (or override exists), then...
        if (mazemap_bounds_check(pos_x, pos_y) || tileNbr_Side != null)
        {
            
            // Tile exists in current region or override exists.
            
            // If override exists, then...
            if (tileNbr_Side != null)
            {
                
                // Override exists.
                
                // Copy overriden tile.
                tileNbr = tileNbr_Side;
                
            }
            
            else
            {
                
                // No override.
                
                // Determine tile number to render.
                tileNbr = regionTiles.get(pos_y).get(pos_x);
                
            }
            
            //System.out.println("Y: " + pos_y + ", X: " + pos_x + ", pos: " + position + " -- " + tileNbr);
            
            // If tile represents a placeholder, then...
            if (tileNbr == HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE.getValue())
            {
                
                // Tile represents a placeholder (empty location).
                
                // Return null.
                //return null;
            }
            
            else
            {
                
                // Tile exists in current region and NOT a placeholder (empty location).
                
                //System.out.println("tileNbr = " + tileNbr);
                //System.out.println("position = " + position);
                
                // Flag tile as active.
                tileActiveInd.set(position, true);
                
                // 9 = Immediately in front of player.
                // 12 = At feet of player.
                
                // If looking at position immediately in front of player, then...
                if (position == 9)
                {
                    
                    // Looking at position immediately in front of player.
                    
                    // Depending on what exists in tile in front of player, ...
                    switch (HeroineEnum.ImgTileEnum.valueOf(tileNbr)) {
                        
                        case IMG_TILE_CHEST_EXTERIOR:
                            
                            // Tile represents a chest in an exterior environment.
                            
                            // Switch out the chest for the floor.
                            tileNbr = HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_FLOOR.getValue();
                            
                            // Store metadata about the chest.
                            virtualString = "Chest|" + Integer.toString(map_id) + "," + 
                              Integer.toString(pos_x) + "," + Integer.toString(pos_y);
                            
                            // Enable chest events -- disable later if chest exists both immediately in 
                            // front of player and at feet.
                            chestActiveInd = true;
                            
                            // Exit selector.
                            break;
                     
                        case IMG_TILE_CHEST_INTERIOR:
                            
                            // Tile represents a chest in an interior environment.
                            
                            // Switch out the chest for the ceiling.
                            tileNbr = HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_CEILING.getValue();

                            // Store metadata about the chest.
                            virtualString = "Chest|" + Integer.toString(map_id) + "," + 
                              Integer.toString(pos_x) + "," + Integer.toString(pos_y);
                            
                            // Enable chest events -- disable later if chest exists both immediately in 
                            // front of player and at feet.
                            chestActiveInd = true;
                            
                            // Exit selector.
                            break;
                            
                        case IMG_TILE_SKULL_PILE:
                            
                            // Store metadata about the skull pile.
                            virtualString = "Bone Pile|" + Integer.toString(map_id) + "," + 
                              Integer.toString(pos_x) + "," + Integer.toString(pos_y);
                            
                            // Burning will replace tile with dungeon ceiling.
                            
                            // For now, display the dungeon ceiling with an added tile with the skull pile.
                            // Make the tile replacement temporary until the burning occurs.
                            tileNbr = HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_CEILING.getValue();
                            
                            // Enable bone pile events -- disable later if displaying information view.
                            bonePileActiveInd = true;
                            
                            // Exit selector.
                            break;
                            
                        case IMG_TILE_LOCKED_DOOR:
                            
                            // Store metadata about the skull pile.
                            virtualString = "Locked Door|" + Integer.toString(map_id) + "," + 
                              Integer.toString(pos_x) + "," + Integer.toString(pos_y);
                            
                            // Unlocking will replace locked with regular (unlocked) door.
                            
                            // For now, display the regular door with an added tile with the lock.
                            // Make the tile replacement with the regular door temporary until the
                            // unlocking occurs.
                            tileNbr = HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_DOOR.getValue();
                            
                            // Enable locked door events -- disable later if displaying information view.
                            lockedDoorActiveInd = true;
                            
                            // Exit selector.
                            break;
                            
                        default:
                            
                            // Exit selector.
                            break;
                            
                    }
                    
                } // End ... If looking at position immediately in front of player.
                
                // Store asset manager key for the tile to display.
                key = HeroineEnum.ImgTileEnum.valueOf(tileNbr).getValue_Key() + "_" + 
                  decimalFormat000.format(position);
                
                //System.out.println("Rendering tile (tile, pos): " + tileNbr + ", " + position);

                //System.out.println("Tile_" + Integer.toString(tileNbr));
                //System.out.println("key = " + key);
                //System.out.println("tile nbr = " + tileNbr);
                //System.out.println("dest_x = " + dest_x[position]);
                
                // Configure the base actor for the tile (possibly null if out of bounds).
                //temp = new BaseActor("TilePos_" + Integer.toString(position), 
                //  gameHD.getAssetMgr().getTextureRegion(key), dest_x[position], dest_y[position] );
                
                // Configure the actor representing the tile.
                
                // Store name for the actor.
                temp.setActorName("TilePos_" + Integer.toString(position));
                
                // Set the texture region for the actor.
                temp.setTextureRegion(gameHD.getAssetMgr().getTextureRegion(key));
                
                // Position the lower left corner of the actor.
                temp.setPosition( dest_x[position], dest_y[position] );
                
                // Store tile type in actor.
                temp.setVirtualInt(tileNbr);
                
                // Store virtual text in actor.
                temp.setVirtualString(virtualString);
                
                // Set actor as visible.
                temp.setVisible(true);
                
                // Update the 
                tiles.set(position, temp);
                
            } // End ... If tile exists in current region and NOT a placeholder (empty location).
            
        } // End ... If tile exists in current region.
        
        // Otherwise -- tile outside current region.
        else
        {
            
            // Tile outside current region.
            
            // Return null.
            //return null;
            
        }
        
        // Return the base actor for the tile.
        return temp;
        
    }
    
    // pos_x = Y-coordinate of tile to render.
    // pos_y = X-coordinate of tile to render.
    // x = X-coordinate within current region to render.  Usually x-coordinate of player position.
    // y = Y-coordinate within current region to render.  Usually y-coordinate of player position.
    // facing = Direction the player is facing.
    // sideInd = Whether to look at sides of player, in addition to straight ahead.
    public Integer mazemap_render_tile_side(int pos_x, int pos_y, int x, int y, 
      HeroineEnum.FacingEnum facing, boolean sideInd)
    {
        // The function returns an enumerated value (integer) representing the (side) tile in the 
        // passed location.
        // The function actually shows the side of the adjacent tile facing the player.
        // Note: x, y flipped to ease map making.
        
        Integer tileSide; // Tile number to render for side adjustment.
        String key; // Key for the tile to display -- in the asset manager.
        
        // Set defaults.
        tileSide = null;
        
        // Specify key to use when checking hash maps.
        key = Integer.toString(pos_x) + ", " + Integer.toString(pos_y);
        
        // Depending on direction player is facing, ...
        switch (facing) {
            
            case NORTH:
                
                // Player facing north.
                
                // If tile north of player, then...
                if (y > pos_y)
                {
                    
                    // Tile north of player.  Store south side.
                    tileSide = currentRegion.getSideTilesSouth(key);
                    
                }
                
                // If side tile not found yet and looking at sides of player, then...
                if (tileSide == null && sideInd)
                {
                    
                    // Side tile not found yet and looking at sides of player.
                    
                    // If tile east of player, then...
                    if (x < pos_x)
                    {
                        
                        // Tile east of player.  Store west side.
                        tileSide = currentRegion.getSideTilesWest(key);

                    }

                    // Otherwise, if tile west of player, then...
                    else if (x > pos_x)
                    {
                        
                        // Tile west of player.  Store east side.
                        tileSide = currentRegion.getSideTilesEast(key);

                    }
                    
                } // End ... If side tile not found yet and looking at sides of player.
                
                // Exit processing.
                break;
                
            case SOUTH:
                
                // Player facing south.
                
                // If tile south of player, then...
                if (y < pos_y)
                {
                    
                    // Tile south of player.  Store north side.
                    tileSide = currentRegion.getSideTilesNorth(key);
                    
                }
                
                // If side tile not found yet and looking at sides of player, then...
                if (tileSide == null && sideInd)
                {
                    
                    // Side tile not found yet and looking at sides of player.
                    
                    // If tile east of player, then...
                    if (x < pos_x)
                    {
                        
                        // Tile east of player.  Store west side.
                        tileSide = currentRegion.getSideTilesWest(key);

                    }

                    // Otherwise, if tile west of player, then...
                    else if (x > pos_x)
                    {
                        
                        // Tile west of player.  Store east side.
                        tileSide = currentRegion.getSideTilesEast(key);

                    }
                    
                } // End ... If side tile not found yet and looking at sides of player.
                
                // Exit processing.
                break;
                
            case EAST:
                
                // Player facing east.
                
                // If tile east of player, then...
                if (x < pos_x)
                {
                    
                    // Tile east of player.  Store west side.
                    tileSide = currentRegion.getSideTilesWest(key);
                    
                }
                
                // If side tile not found yet and looking at sides of player, then...
                if (tileSide == null && sideInd)
                {
                    
                    // Side tile not found yet and looking at sides of player.
                    
                    // If tile north of player, then...
                    if (y > pos_y)
                    {
                        
                        // Tile north of player.  Store south side.
                        tileSide = currentRegion.getSideTilesSouth(key);

                    }

                    // Otherwise, if tile south of player, then...
                    else if (y < pos_y)
                    {
                        
                        // Tile south of player.  Store north side.
                        tileSide = currentRegion.getSideTilesNorth(key);

                    }
                    
                } // End ... If side tile not found yet and looking at sides of player.
                
                // Exit processing.
                break;
                
            case WEST:
                
                // Player facing west.
                
                // If tile west of player, then...
                if (x > pos_x)
                {
                    
                    // Tile west of player.  Store east side.
                    tileSide = currentRegion.getSideTilesEast(key);
                    
                }
                
                // If side tile not found yet and looking at sides of player, then...
                if (tileSide == null && sideInd)
                {
                
                    // Side tile not found yet and looking at sides of player.
                    
                    // If tile north of player, then...
                    if (y > pos_y)
                    {
                        
                        // Tile north of player.  Store south side.
                        tileSide = currentRegion.getSideTilesSouth(key);

                    }

                    // Otherwise, if tile south of player, then...
                    else if (y < pos_y)
                    {
                        
                        // Tile south of player.  Store north side.
                        tileSide = currentRegion.getSideTilesNorth(key);

                    }
                    
                } // End ... If side tile not found yet and looking at sides of player.
                
                // Exit processing.
                break;
                
            default:
                
                // Unknown direction.
                
                // Display warning.
                System.out.println("Warning:  Player is facing in unknown direction.");
                
                // Exit processing.
                break;
            
        }
        
        // Return the side tile.
        return tileSide;
        
    }
    
    public ArrayList<BaseActor> minimap_render()
    {
        
        /*
        The function returns the icons used to render the minimap for the current map / region.
        
        The setting up the minimap for rendering (populating the array list) involves the following:
        
        1.  Set defaults.
        2.  Initialize array lists.
        3.  Store color to use for transparency effect.
        4.  Store icon width and height for use when placing side tiles.
        5. Add minimap background (included in icon list for simplicity).
        6.  Render (base) map.
        7.  Render exits.
        8.  Render shops.
        9.  Render side tiles.
        10.  Render minimap cursor.
        11.  Add base actor for player icon.
        12.  Return the array list with the base actors for the icons.
        */
        
        int counter; // Used to increment through exits and shops.
        float dest_pos_x; // X-coordinate at which to place lower-left corner of actor used for door.
        float dest_pos_y; // Y-coordinate at which to place lower-left corner of actor used for door.
        int doorEastCount; // Number of doors on east side.
        int doorNorthCount; // Number of doors on north side.
        int doorSouthCount; // Number of doors on south side.
        int doorWestCount; // Number of doors on west side.
        float iconHeight; // Height of each icon in minimap.
        ArrayList<BaseActor> icons; // BaseActor objects associated with icons.
        float iconWidth; // Width of each icon in minimap.
        HeroineEnum.MinimapEnum key; // Key to texture region in hash map for icon to display.
        HeroineEnum.MinimapCursorEnum keyCursor; // Key to texture region in hash map for cursor icon to display.
        BaseActor minimapBackground; // BaseActor serving as the minimap background.
        Color tempColor; // Color used to apply partial transparency.
        int tileNbr; // Tile number for which to render icon  in minimap.
        boolean walkable; // Whether tile walkable.
        
        // 1.  Set defaults.
        doorNorthCount = 0;
        doorSouthCount = 0;
        doorEastCount = 0;
        doorWestCount = 0;
        
        // 2.  Initialize array lists.
        icons = new ArrayList<>();
        
        // 3.  Store color to use for transparency effect.
        tempColor = new Color(Color.LIGHT_GRAY.r, Color.LIGHT_GRAY.g, Color.LIGHT_GRAY.b, 0.50f);
        
        // 4.  Store icon width and height for use when placing side tiles.
        iconWidth = minimapRegions.get(HeroineEnum.MinimapEnum.MINIMAP_BLOCK_BLACK).getRegionWidth();
        iconHeight = minimapRegions.get(HeroineEnum.MinimapEnum.MINIMAP_BLOCK_BLACK).getRegionHeight();
        
        // 5. Add minimap background (included in icon list for simplicity).
        
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
        
        // 6.  Render (base) map.
        
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
                        
                } // End ... If NOT a placeholder tile.
                
            } // End ... Loop through horizontal tiles.
                
        } // End ... Loop through vertical tiles.
        
        // 7.  Render exits.
        
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
        
        // 8.  Render shops.
        
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
        
        // 9.  Render side tiles.
        
        // Loop through vertical tiles.
        for (int counterY = 0; counterY < regionHeight; counterY++)
        {
            
            // Loop through horizontal tiles.
            for (int counterX = 0; counterX < regionWidth; counterX++)
            {
                
                // If side tile exists on north side, then...
                if (getImgTileEnum_Side(counterX, counterY, HeroineEnum.FacingEnum.NORTH, false) != null )
                {

                    // Side tile exists on north side.

                    // System.out.println("Side tile exists on north side.");
                    
                    // Increment north side tile count.
                    doorNorthCount++;
                    
                    // Calculate placement of actor.
                    dest_pos_x = minimapDestX.get(counterY).get(counterX);
                    dest_pos_y = minimapDestY.get(counterY).get(counterX) + iconHeight;
                    dest_pos_x++;
                    dest_pos_y--;
                    
                    /*
                    System.out.println("Key: " + 
                      HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_MINIMAP_DOOR_HORZ.getValue_Key());
                    System.out.println("Dest X: " + dest_pos_x);
                    System.out.println("Dest Y: " + dest_pos_y);
                    */
                    
                    // Add base actor for current side tile.
                    icons.add( new BaseActor("Minimap_North_Side(" + Integer.toString(doorNorthCount) + ")", 
                      HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_MINIMAP_DOOR_HORZ.getValue_Key(), 
                      gameHD.getAssetMgr(), dest_pos_x, dest_pos_y, CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE, 
                      tempColor) );

                } // End ... If side tile exists on north side.
                
                // If side tile exists on south side, then...
                if (getImgTileEnum_Side(counterX, counterY, HeroineEnum.FacingEnum.SOUTH, false) != null )
                {

                    // Side tile exists on south side.

                    // System.out.println("Side tile exists on south side.");
                    
                    // Increment south side tile count.
                    doorSouthCount++;
                    
                    // Calculate placement of actor.
                    dest_pos_x = minimapDestX.get(counterY).get(counterX);
                    dest_pos_y = minimapDestY.get(counterY).get(counterX); // + iconHeight;
                    dest_pos_x++;
                    dest_pos_y--;
                    
                    /*
                    System.out.println("Key: " + 
                      HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_MINIMAP_DOOR_HORZ.getValue_Key());
                    System.out.println("Dest X: " + dest_pos_x);
                    System.out.println("Dest Y: " + dest_pos_y);
                    */
                    
                    // Add base actor for current side tile.
                    icons.add( new BaseActor("Minimap_South_Side(" + Integer.toString(doorSouthCount) + ")", 
                      HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_MINIMAP_DOOR_HORZ.getValue_Key(), 
                      gameHD.getAssetMgr(), dest_pos_x, dest_pos_y, CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE, 
                      tempColor) );

                } // End ... If side tile exists on south side.
                
                // If side tile exists on east side, then...
                if (getImgTileEnum_Side(counterX, counterY, HeroineEnum.FacingEnum.EAST, false) != null )
                {

                    // Side tile exists on east side.

                    // System.out.println("Side tile exists on east side.");
                    
                    // Increment east side tile count.
                    doorEastCount++;
                    
                    // Calculate placement of actor.
                    dest_pos_x = minimapDestX.get(counterY).get(counterX) + iconWidth;
                    dest_pos_y = minimapDestY.get(counterY).get(counterX);
                    dest_pos_x--;
                    dest_pos_y++;
                    
                    /*
                    System.out.println("Key: " + 
                      HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_MINIMAP_DOOR_VERT.getValue_Key());
                    System.out.println("Dest X: " + dest_pos_x);
                    System.out.println("Dest Y: " + dest_pos_y);
                    */
                    
                    // Add base actor for current side tile.
                    icons.add( new BaseActor("Minimap_East_Side(" + Integer.toString(doorEastCount) + ")", 
                      HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_MINIMAP_DOOR_VERT.getValue_Key(), 
                      gameHD.getAssetMgr(), dest_pos_x, dest_pos_y, CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE, 
                      tempColor) );

                } // End ... If side tile exists on east side.

                // If side tile exists on west side, then...
                if (getImgTileEnum_Side(counterX, counterY, HeroineEnum.FacingEnum.WEST, false) != null )
                {

                    // Side tile exists on west side.

                    // System.out.println("Side tile exists on west side.");

                    // Increment west side tile count.
                    doorWestCount++;
                    
                    // Calculate placement of actor.
                    dest_pos_x = minimapDestX.get(counterY).get(counterX);
                    dest_pos_y = minimapDestY.get(counterY).get(counterX);
                    dest_pos_x--;
                    dest_pos_y++;
                    
                    /*
                    System.out.println("Key: " + 
                      HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_MINIMAP_DOOR_VERT.getValue_Key());
                    System.out.println("Dest X: " + dest_pos_x);
                    System.out.println("Dest Y: " + dest_pos_y);
                    */
                    
                    // Add base actor for current side tile.
                    icons.add( new BaseActor("Minimap_West_Side(" + Integer.toString(doorWestCount) + ")", 
                      HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_MINIMAP_DOOR_VERT.getValue_Key(), 
                      gameHD.getAssetMgr(), dest_pos_x, dest_pos_y, CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE, 
                      tempColor) );
                    
                } // End ... If side tile exists on west side.
                
            } // End ... Loop through horizontal tiles.
        
        } // End ... Loop through vertical tiles.
        
        // 10.  Render minimap cursor.
        
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
        
        // 11.  Add base actor for player icon.
        icons.add( new BaseActor("Minimap_Avatar", 
          minimapCursorRegions.get(keyCursor), 
          minimapDestX.get(gameHD.getAvatar().getY()).get(gameHD.getAvatar().getX()), 
          minimapDestY.get(gameHD.getAvatar().getY()).get(gameHD.getAvatar().getX()) ) );
        
        // 12.  Return the array list with the base actors for the icons.
        return icons;

    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // viewWidth = Width of the stage.
    public void prepareSpecialTiles(ArrayList<BaseActor> tiles, int viewWidth)
    {
        
        // The function configures properties for the special tile actors, such as the chest, bone pile, and 
        // lock.
        
        // 1.  Configure tile (actor) for chest.
        
        // Set actor name.
        tiles.get(TILE_POS_CHEST).setActorName( "Chest" );
            
        // Set texture of actor.
        tiles.get(TILE_POS_CHEST).setTexture(
          gameHD.getAssetMgr().getImage_xRef(HeroineEnum.ImgOtherEnum.IMG_OTHER_CHEST.getValue_Key()) );

        // Set position of actor -- centered horizontally and up about 1/4 from the bottom.
        tiles.get(TILE_POS_CHEST).setPosition( (float)((viewWidth - tiles.get(TILE_POS_CHEST).getWidth()) / 2), 
          28f * gameHD.getConfig().getScale() );

        // Set origin of actor to center of associated image.
        tiles.get(TILE_POS_CHEST).setOriginCenter();
        
        // 2.  Configure tile (actor) for treasure and related group.
        
        // Name treasure actor and group.
        tiles.get(TILE_POS_TREASURE).setActorName( "Treasure" );
        tiles.get(TILE_POS_TREASURE_GROUP).setActorName( "Treasure Group" );
        
        // 3.  Configure tile (actor) for bone pile.
        
        // Set actor name.
        tiles.get(TILE_POS_BONE_PILE).setActorName( "Bone Pile" );

        // Set texture of actor.
        tiles.get(TILE_POS_BONE_PILE).setTexture(
          gameHD.getAssetMgr().getImage_xRef(HeroineEnum.ImgOtherEnum.IMG_OTHER_SKULL_PILE.getValue_Key()) );

        // Set position of actor -- centered horizontally and up about 1/4 from the bottom.
        tiles.get(TILE_POS_BONE_PILE).setPosition( (float)((viewWidth - tiles.get(TILE_POS_BONE_PILE).getWidth()) / 2), 
          28f * gameHD.getConfig().getScale() );

        // Set origin of actor to center of associated image.
        tiles.get(TILE_POS_BONE_PILE).setOriginCenter();
        
        // 4.  Configure tile (actor) for lock.
        
        // Set actor name.
        tiles.get(TILE_POS_LOCK).setActorName( "Lock" );

        // Set texture of actor.
        tiles.get(TILE_POS_LOCK).setTexture(
          gameHD.getAssetMgr().getImage_xRef(HeroineEnum.ImgOtherEnum.IMG_OTHER_LOCK.getValue_Key()) );

        // Set position of actor -- centered horizontally and up about 1/4 from the bottom.
        tiles.get(TILE_POS_LOCK).setPosition( 74f * gameHD.getConfig().getScale(), 
          41f * gameHD.getConfig().getScale() );

        // Set origin of actor to center of associated image.
        tiles.get(TILE_POS_LOCK).setOriginCenter();

        // Set lock actor as visible.
        tiles.get(TILE_POS_LOCK).setVisible(true);
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // viewWidth = Width of the stage.
    // quantity = Number of items to render.
    private void render_gold(ArrayList<BaseActor> tiles, ArrayList<BaseActor> goldPile, int viewWidth, 
      int quantity)
    {
        
        // The function displays the current gold pile (only the actors necessary to add up to the desired
        // quantity).
        
        int counter; // Used to increment through actors.
        ArrayList<Integer> bitwiseList; // List of numbers that add up to gold amount.
    
        // Flag gold pile as active.
        goldPileActiveInd = true;
    
        // Clear array list with actions for gold actors.
        goldActions.clear();
        
        // Populate list of numbers that add up to gold amount.
        bitwiseList = routines.UtilityRoutines.bitwiseList(quantity);
        
        // 1.  Flag all actors as invisible.
        
        // Loop through and flag all actors as invisible.
        for (int counterFlag = 0; counterFlag <= 9; counterFlag++)
        {
            // Flag current actor in loop as invisible.
            this.goldVisibleList[counterFlag] = false;
        }
        
        // 2.  Flag relevant actors as visible, based upon bitwise list.
        
        // Loop through gold actors to show.
        bitwiseList.forEach((gold) -> {
            
            // Flag current gold actor as visible.
            this.goldVisibleList[goldXRef.get(gold)] = true;
            
        });
        
        // 3.  Set up actions related to fading.
        
        // Reset counter.
        counter = 0;
        
        // Set up actions...
        
        // Loop through actors.
        for (BaseActor actor : goldPile)
        {
            
            // If actor visible, then...
            if (actor.isVisible())  
            {
                
                // Actor visible.
                
                // Route 1:  Visible.  Fade out first.
                
                // If rendering current gold actor, then...
                if (this.goldVisibleList[counter])
                {
                    
                    // Rendering current gold actor.
                    
                    // Route 1A:  Visible (before and after).  Fade out.  Then, fade in.
                    
                    // Add action to fade out and in the actor.
                    goldActions.add(
                        Actions.sequence
                            (
                            //Actions.fadeIn(0.0f), // First step of fade out.
                            //Actions.visible(true),
                            Actions.delay(0.0f), 
                            Actions.fadeOut(0.5f), 
                            Actions.visible(false), // Last step of fade out.
                            Actions.delay(0.0f), // First step of fade in.
                            Actions.alpha(0),
                            Actions.visible(true),
                            Actions.fadeIn(0.5f) // Last step of fade in.
                            )
                            
                    ); // End ... Add action to fade out and in the actor.
                    
                } // End ... If rendering current gold actor
                
                else
                {
                    
                    // Actor no longer visible.
                    
                    // Route 1B:  Visible (before).  Fade out.  Do not fade in.
                    
                    // Add action to fade out the actor.
                    goldActions.add(
                        Actions.sequence
                            (
                            //Actions.fadeIn(0.0f), // First step of fade out.
                            //Actions.visible(true),
                            Actions.delay(0.0f), 
                            Actions.fadeOut(0.5f), 
                            Actions.visible(false) // Last step of fade out.
                            )
                    ); // End ... Add action to fade out the actor.
                    
                } // End ... If actor no longer visible.
                
            } // End ... If actor visible.
            
            else
            {
                
                // Actor not visible.
                
                // Route 2:  Not visible (before).  Do NOT fade out first.  Fade in.
                
                // If rendering current gold actor, then...
                if (this.goldVisibleList[counter])
                {
                    
                    // Rendering current gold actor.
                    
                    // Add action to fade in the actor.
                    goldActions.add(
                        Actions.sequence
                            (
                            Actions.delay(0.0f), // First step of fade in.
                            Actions.alpha(0),
                            Actions.visible(true),
                            Actions.fadeIn(0.5f) // Last step of fade in.
                            )
                    ); // End ... Add action to fade in the actor.
                    
                } // End ... If rendering current gold actor.
                
                else
                {
                    
                    // NOT rendering current gold actor.
                    
                    // Add an empty entry to maintain proper positioning.
                    goldActions.add(null);
                    
                }
                
            } // End ... If actor not visible.
            
            // Increment counter.
            counter++;
            
        } // Loop through actors.
        
        // 4.  Clear and add actions related to fading to the actors.
        
        // Reset counter.
        counter = 0;
        
        // Clear and add actions.
        
        // Loop through actors.
        for (BaseActor actor : goldPile)
        {
            
            // Clear actions from actor.
            actor.clearActions();
            
            // If new action exist for actor, then...
            if (goldActions.get(counter) != null)
            {
                // New action exist for actor.
                
                // Add action to actor.
                actor.addAction(goldActions.get(counter));
            }
            
            // Increment counter.
            counter++;
            
        } // End ... Loop through actors.
        
    }
    
    // itemEnum = Enumerated value for the item to render the associated treasure image.
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    // goldPile = List of gold actors.
    // viewWidth = Width of the stage.
    // treasureLabel = Reference to label with the treasure description.
    // quantity = Number of items to render.
    private boolean render_treasure_first(HeroineEnum.ItemEnum itemEnum, ArrayList<BaseActor> tiles, 
      ArrayList<BaseActor> goldPile, int viewWidth, CustomLabel treasureLabel, int quantity)
    {
        
        // The function updates the image and placement for the treasure actor based on the passed item.
        // The function also updates and recenters the text, based on the passed item.
        // The function returns whether a treasure image exists for the passed item.
        // NOTE:  The function is only called when rendering the first item in the chest.
        
        HeroineEnum.ImgTreasureEnum imgTreasureEnum; // Enumerated value for treasure.
        float treasureHeight; // Height to which to resize treasure.
        String treasureKey; // Key to texture region for treasure in asset manager.
        boolean treasureImageInd; // Whether treasure image exists for passed actor.
        float treasureWidth; // Width to which to resize treasure.
        
        // 1.  Set defaults.
        treasureImageInd = false;
        
        // 2.  If available, update treasure image and position.
        
        // If item represents gold, then...
        if (itemEnum == HeroineEnum.ItemEnum.ITEM_GOLD)
        {
            
            // Item represents gold.
            
            // Set vertical position of treasure group.
            
            // If chest actor active, then...
            if (tileActiveInd.get(TILE_POS_CHEST))
            {

                // Chest actor active.

                // Position treasure actor (group) at center of chest vertically.
                tiles.get(TILE_POS_TREASURE_GROUP).setY( tiles.get(TILE_POS_CHEST).getOriginY() );

            }

            else
            {

                // Chest actor NOT active.

                // Position treasure actor (group) vertically slightly above the bottom.
                tiles.get(TILE_POS_TREASURE_GROUP).setY( TREASURE_POS_Y * gameHD.getConfig().getScale() );

            }
            
            // Redraw / update gold pile and treasure group.
            render_gold(tiles, goldPile, viewWidth, quantity);
            
            // Flag treasure image as existing.
            treasureImageInd = true;
            
        } // End ... If item represents gold.
        
        else
        {
            
            // Item NOT gold.
            
            // Get treasure enumeration value.
            imgTreasureEnum = itemEnum.getValue_ImgTreasureEnum();

            // If enumeration value points at an image, then...
            if (imgTreasureEnum != null)
            {
                // Enumeration value points at an image.
                
                // Flag treasure image as existing.
                treasureImageInd = true;
                
                // Get key for texture region associated with treasure in asset manager.
                treasureKey = imgTreasureEnum.getValue_AtlasKey();

                // Update image for treasure actor.
                tiles.get(TILE_POS_TREASURE).setTextureRegion( 
                  gameHD.getAssetMgr().getTextureRegion(treasureKey) );
                
                // Determine width and height to resize treasure.
                
                // Magnify by scale factor.
                treasureWidth = gameHD.getConfig().getScale() * tiles.get(TILE_POS_TREASURE).getWidth();
                treasureHeight = gameHD.getConfig().getScale() * tiles.get(TILE_POS_TREASURE).getHeight();

                // If chest actor active, then...
                if (tileActiveInd.get(TILE_POS_CHEST))
                {
                    
                    // Chest actor active.
                    
                    // Constrain treasure dimensions to those of chest.
                    
                    // If treasure width greater than chest, then... > Limit to chest width.
                    if (treasureWidth > tiles.get(TILE_POS_CHEST).getWidth())
                        treasureWidth = tiles.get(TILE_POS_CHEST).getWidth();

                    // If treasure height greater than chest, then... > Limit to chest height.
                    if (treasureHeight > tiles.get(TILE_POS_CHEST).getHeight())
                        treasureHeight = tiles.get(TILE_POS_CHEST).getHeight();

                    // Take the lower of the treasure width and height.
                    treasureWidth = Math.min(treasureWidth, treasureHeight);
                    treasureHeight = treasureWidth;
                    
                    // Position treasure actor at center of stage horizontally and chest vertically.
                    tiles.get(TILE_POS_TREASURE).setPosition( (viewWidth - treasureWidth) / 2, 
                      tiles.get(TILE_POS_CHEST).getOriginY() );
                    
                }
                
                else
                {
                    
                    // Chest actor active.
                    
                    // Position treasure actor at center of stage horizontally and vertically slightly above 
                    // the bottom.
                    tiles.get(TILE_POS_TREASURE).setPosition( (viewWidth - treasureWidth) / 2, 
                      TREASURE_POS_Y * gameHD.getConfig().getScale() );
                    
                }
                    
                
                // Resize treasure actor based on scale.
                tiles.get(TILE_POS_TREASURE).setWidth( treasureWidth );
                tiles.get(TILE_POS_TREASURE).setHeight( treasureHeight );

                // Set treasure origin to center to allow for proper rotation.
                tiles.get(TILE_POS_TREASURE).setOriginCenter();
                
            } // End ... If enumeration value points at an image.

            else
            {
                // Enumeration value empty.
                // Only display label.
            }
            
        }
        
        // 3.  Perform actions on treasure label.
        
        // Update the treasure description in the associated label and center horizontally.
        treasureLabel.setLabelText_Center( 
          "FOUND " + itemEnum.getValue_TreasureText() +
          ((chestOtherItemIndex < chestOtherItems.size()) ? "++" : "") + 
          "!" );
        
        // Return whether treasure image exists.
        return treasureImageInd;
        
    }
    
    // treasureLabel = Reference to label with the treasure description.
    // treasureImageInd = Whether treasure image exists for current item.
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    private void render_treasure_label(CustomLabel treasureLabel, boolean treasureImageInd, 
      ArrayList<BaseActor> tiles)
    {
    
        // The function updates the treasure description and position in the associated label.
        // Positioning includes centering horizontally and vertical placement that depends on the treasure.
        
        String otherItemsText; // Text to show when more items exist to take -> ++.
        float pos_y; // Y-coordinate at which to set bottom of label.
        String treasureText; // Text to display for treasure.
        
        // Populate other items text.
        otherItemsText = (chestOtherItemIndex + 1) < chestOtherItems.size() ? "++" : "";

        // Store text to display for treasure.
        treasureText = "FOUND " + 
          chestOtherItems.get(chestOtherItemIndex).getValue_TreasureText() + 
          otherItemsText + "!";
        
        // Determine vertical placement of treasure label.
        pos_y = determine_treasure_label_pos_y(treasureImageInd, tiles, treasureLabel);
        
        // Remove actions from label.
        treasureLabel.removeActions();
        
        // Fade in the next treasure label text.
        treasureLabel.addAction_FadeOutIn_Center_AdjY( 0.00f, 0.50f, 0.50f, treasureText, pos_y );
        
    }
    
    // posX = Y-coordinate of tile to render.
    // posY = X-coordinate of tile to render.
    private void transformChestTile(int posX, int posY)
    {
        
        // The function transforms the tile associated with the chest at the passed location.
        // The function effectively replaces the tile with its equivalent in the background -- 
        // removing the chest.
        
        // While the parameters passed in represent the x and y coordinates of the player (in that order),
        // the adjustments require reversing the two.

        // If working with an interior tile, then...
        if (currentRegion.getRegionTileNbr(posY, posX) == 
          HeroineEnum.ImgTileEnum.IMG_TILE_CHEST_INTERIOR.getValue())
            // Working with an interior tile.
            // Change to dungeon ceiling.
            currentRegion.setRegionTileNbr(posY, posX, 
              HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_CEILING.getValue());

        // Otherwise, if working with an exterior tile, then...
        else if (currentRegion.getRegionTileNbr(posY, posX) == 
          HeroineEnum.ImgTileEnum.IMG_TILE_CHEST_EXTERIOR.getValue())
            // Working with an exterior tile.
            // Change to dungeon floor.
            currentRegion.setRegionTileNbr(posY, posX, 
              HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_FLOOR.getValue());

        else
            // Problem finding tile to transform.
            // Display warning.
            System.out.println("Warning:  Unable to find file to transform at (" + posX + 
              ", " + posY + ")!");
        
    }
    
    // Getters and setters below...
    
    public boolean isBonePileActiveInd() {
        return bonePileActiveInd;
    }
    
    public void setBonePileActiveInd(boolean bonePileActiveInd) {
        this.bonePileActiveInd = bonePileActiveInd;
    }
    
    public RegionMap getCurrentRegion() {
        return currentRegion;
    }
    
    // posX = X-coordinate of location at which to get forward position (key).
    // posY = Y-coordinate of location at which to get forward position (key).
    // facingEnum = Enumerated value related to current direction player is facing.
    public String getForwardPosKey(int posX, int posY, HeroineEnum.FacingEnum facingEnum) {
        // Returns the location of the tile in passed location / facing direction in the form of a key (x, y).
        // Example:  10, 5.
        
        String key; // Key value related to location to return.
        
        // Set defaults.
        key = null;
        
        // Depending on direction player facing, ...
        switch (facingEnum) {
            
            case NORTH:
                
                // Facing north.
                
                // Return key for tile one square north of the one passed.
                key = Integer.toString(posX) + ", " + Integer.toString(posY - 1);
                
                // Exit selector.
                break;
                
            case SOUTH:
                
                // Facing south.
                
                // Return key for tile one square south of the one passed.
                key = Integer.toString(posX) + ", " + Integer.toString(posY + 1);
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Facing east.
                
                // Return key for tile one square east of the one passed.
                key = Integer.toString(posX + 1) + ", " + Integer.toString(posY);
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Facing west.
                
                // Return key for tile one square west of the one passed.
                key = Integer.toString(posX - 1) + ", " + Integer.toString(posY);
                
                // Exit selector.
                break;
                
            default:
                
                // Unknown direction.
                
                // Display warning.
                System.out.println("Warning:  Player is facing in unknown direction when getting forward position.");
                
        }
        
        // Return location key.
        return key;
        
    }
    
    // posX = X-coordinate at which to get tile information.
    // posY = Y-coordinate at which to get tile information.
    public HeroineEnum.ImgTileEnum getImgTileEnum(int posX, int posY) {
        // Returns the enumerated value for the tile at the passed location.
        // Reverses y and x to ease working with tiles.
        return HeroineEnum.ImgTileEnum.valueOf(regionTiles.get(posY).get(posX));
    }
    
    // avatar = Reference to player information.
    public HeroineEnum.ImgTileEnum getImgTileEnum_CurrentLoc(Avatar avatar) {
        // Returns the enumerated value for the tile at the current player location.
        // Reverses y and x to ease working with tiles.
        return HeroineEnum.ImgTileEnum.valueOf(regionTiles.get(avatar.getY()).get(avatar.getX()));
    }
    
    // avatar = Reference to player information.
    // imgTileEnum = Enumerated value to which to set tile.
    public void setImgTileEnum_CurrentLoc(Avatar avatar, HeroineEnum.ImgTileEnum imgTileEnum) {
        
        // Sets the enumerated value for the tile at the current player location.
        // Reverses y and x to ease working with tiles.
        
        // Alter tile.
        currentRegion.setRegionTileNbr( avatar.getY(), avatar.getX(), imgTileEnum.getValue() );
        
    }
    
    // avatar = Reference to player information.
    public HeroineEnum.ImgTileEnum getImgTileEnum_ForwardLoc(Avatar avatar) {
        
        // Returns the enumerated value for the tile in front of the current player location.
        // Reverses y and x to ease working with tiles.
        
        HeroineEnum.ImgTileEnum imgTileEnum; // Tile in front of the current player location.
        
        // Depending on direction player facing, ...
        switch (avatar.getFacing()) {
            
            case NORTH:
                
                // Facing north.
                
                // Set value of tile to return.
                imgTileEnum = 
                  HeroineEnum.ImgTileEnum.valueOf( regionTiles.get(avatar.getY() - 1).get(avatar.getX()) );
                
                // Exit selector.
                break;
                
            case SOUTH:
                
                // Facing south.
                
                // Set value of tile to return.
                imgTileEnum = 
                  HeroineEnum.ImgTileEnum.valueOf( regionTiles.get(avatar.getY() + 1).get(avatar.getX()) );
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Facing east.
                
                // Set value of tile to return.
                imgTileEnum = 
                  HeroineEnum.ImgTileEnum.valueOf( regionTiles.get(avatar.getY()).get(avatar.getX() + 1) );
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Facing west.
                
                // Set value of tile to return.
                imgTileEnum = 
                  HeroineEnum.ImgTileEnum.valueOf( regionTiles.get(avatar.getY()).get(avatar.getX() - 1) );
                
                // Exit selector.
                break;
                
            default:
                
                // Facing invalid direction.
            
                // Set value to return to null.
                imgTileEnum = null;
                
                // Display warning.
                System.out.println("Warning:  Player facing invalid direction while checking tile at forward location.");
                
                // Exit selector.
                break;
                
        }
        
        // Return value.
        return imgTileEnum;
    
    }
    
    // avatar = Reference to player information.
    // forwardInd = Whether moving (or looking) foward.  true = Forward, false = Backward.  Examples:  true, false.
    public HeroineEnum.ImgTileEnum getImgTileEnum_Side(Avatar avatar, boolean forwardInd) {
        
        // Returns the enumerated value for the tile facing (or immediately behind) the player.
        // Refers actually to the side tile in the adjacent location.
        // Eases moving through and working with portals.
        
        Integer tileNbr; // Number of tile at side facing (or immediately behind) current player location (or null).
        HeroineEnum.ImgTileEnum imgTileEnum; // Tile at side facing (or immediately behind) current player location.
        
        // Depending on direction player facing, ...
        switch (avatar.getFacing()) {
            
            case NORTH:
                
                // Facing north.
                
                // If moving forward, then...
                if (forwardInd)
                {
                    
                    // Moving forward.
                    
                    // Store side tile (possibly none -- null).
                    tileNbr = currentRegion.getSideTilesSouth( avatar.getX(), avatar.getY() - 1 );
                    
                    /*
                    System.out.println("Trying to move forward (" + avatar.getX() + ", " +
                      (avatar.getY() - 1) + ") - south: " + tileNbr);
                    */
                    
                }
                
                else
                {
                    
                    // Moving backward.
                    
                    // Store side tile (possibly none -- null).
                    tileNbr = currentRegion.getSideTilesNorth( avatar.getX(), avatar.getY() + 1 );
                    
                    /*
                    System.out.println("Trying to move backward (" + avatar.getX() + ", " +
                      (avatar.getY() + 1) + ") - north: " + tileNbr);
                    */

                }
                
                // Exit selector.
                break;
                
            case SOUTH:
                
                // Facing south.
                
                // If moving forward, then...
                if (forwardInd)
                {
                    
                    // Moving forward.
                    
                    // Store side tile (possibly none -- null).
                    tileNbr = currentRegion.getSideTilesNorth( avatar.getX(), avatar.getY() + 1 );
                    
                    /*
                    System.out.println("Trying to move forward (" + avatar.getX() + ", " +
                      (avatar.getY() + 1) + ") - north: " + tileNbr);
                    */
                    
                }
                
                else
                {
                    
                    // Moving backward.
                    
                    // Store side tile (possibly none -- null).
                    tileNbr = currentRegion.getSideTilesSouth( avatar.getX(), avatar.getY() - 1 );
                    
                    /*
                    System.out.println("Trying to move backward (" + avatar.getX() + ", " +
                      (avatar.getY() - 1) + ") - south: " + tileNbr);
                    */
                    
                }
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Facing east.
                
                // If moving forward, then...
                if (forwardInd)
                {
                    
                    // Moving forward.
                    
                    // Store side tile (possibly none -- null).
                    tileNbr = currentRegion.getSideTilesWest( avatar.getX() + 1, avatar.getY() );
                    
                    /*
                    System.out.println("Trying to move forward (" + (avatar.getX() + 1) + ", " +
                      avatar.getY() + ") - west: " + tileNbr);
                    */
                    
                }
                
                else
                {
                    
                    // Moving backward.
                    
                    // Store side tile (possibly none -- null).
                    tileNbr = currentRegion.getSideTilesEast( avatar.getX() - 1, avatar.getY() );
                    
                    /*
                    System.out.println("Trying to move backward (" + (avatar.getX() - 1) + ", " +
                      avatar.getY() + ") - east: " + tileNbr);
                    */
                    
                }
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Facing west.
                
                // If moving forward, then...
                if (forwardInd)
                {
                    
                    // Moving forward.
                    
                    // Store side tile (possibly none -- null).
                    tileNbr = currentRegion.getSideTilesEast( avatar.getX() - 1, avatar.getY() );
                    
                    /*
                    System.out.println("Trying to move forward (" + (avatar.getX() - 1) + ", " +
                      avatar.getY() + ") - east: " + tileNbr);
                    */
                    
                }
                
                else
                {
                    
                    // Moving backward.
                    
                    // Store side tile (possibly none -- null).
                    tileNbr = currentRegion.getSideTilesWest( avatar.getX() + 1, avatar.getY() );
                    
                    /*
                    System.out.println("Trying to move backward (" + (avatar.getX() + 1) + ", " +
                      avatar.getY() + ") - west: " + tileNbr);
                    */
                    
                }
                
                // Exit selector.
                break;
                
            default:
                
                // Facing invalid direction.
            
                // Set value to return to null.
                tileNbr = null;
                
                // Display warning.
                System.out.println("Warning:  Player facing invalid direction while trying to determine side tile at current location.");
                
                // Exit selector.
                break;
                
        }
        
        // If side tile not null, then...
        if (tileNbr != null)
        {

            // Side tile not null.
            imgTileEnum = HeroineEnum.ImgTileEnum.valueOf( tileNbr );

        }
        
        else
        {
            
            // Side tile is null.
            imgTileEnum = HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE_SIDE;
            
        }
        
        // Return tile at side facing current player location (or ignore when none present).
        return imgTileEnum;
        
    }
    
    // pos_x = X-coordinate of location for which to check adjacent tile side.
    // pos_y = Y-coordinate of location for which to check adjacent tile side.
    // facingEnum = Direction to use when checking adjacent tile side.
    // convertNull = Whether to convert null to IMG_TILE_IGNORE.
    public HeroineEnum.ImgTileEnum getImgTileEnum_Side(int pos_x, int pos_y, 
      HeroineEnum.FacingEnum facingEnum, boolean convertNull) {
        
        // Returns the enumerated value for the (side) tile one away from the location passed, using the 
        // specified direction.
        
        Integer tileNbr; // Number of tile at side facing (or immediately behind) current player location (or null).
        HeroineEnum.ImgTileEnum imgTileEnum; // Tile at side facing (or immediately behind) current player location.
        
        // Set defaults.
        imgTileEnum = null;
        
        // Depending on direction, ...
        switch (facingEnum) {
            
            case NORTH:
                
                // Facing north.
                    
                // Store side tile (possibly none -- null).
                tileNbr = currentRegion.getSideTilesSouth( pos_x, pos_y - 1 );
                
                // Exit selector.
                break;
                
            case SOUTH:
                
                // Facing south.
                    
                // Store side tile (possibly none -- null).
                tileNbr = currentRegion.getSideTilesNorth( pos_x, pos_y + 1 );
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Facing east.
                    
                // Store side tile (possibly none -- null).
                tileNbr = currentRegion.getSideTilesWest( pos_x + 1, pos_y );
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Facing west.
                    
                // Store side tile (possibly none -- null).
                tileNbr = currentRegion.getSideTilesEast( pos_x - 1, pos_y );
                
                // Exit selector.
                break;
                
            default:
                
                // Facing invalid direction.
            
                // Set value to return to null.
                tileNbr = null;
                
                // Display warning.
                System.out.println("Warning:  Player facing invalid direction while trying to determine side tile at passed location.");
                
                // Exit selector.
                break;
                
        }
        
        // If side tile not null, then...
        if (tileNbr != null)
        {

            // Side tile not null.
            imgTileEnum = HeroineEnum.ImgTileEnum.valueOf( tileNbr );

        }
        
        // Otherwise, if converting null, then...
        else if (convertNull)
        {
            
            // Convert null to IMG_TILE_IGNORE_SIDE.
            
            // Side tile is null.
            imgTileEnum = HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE_SIDE;
            
        }
        
        // Return tile at side facing current player location (or ignore when none present).
        return imgTileEnum;
        
    }
    
    // avatar = Reference to player information.
    // imgTileEnum = Enumerated value to which to set tile.
    public void setImgTileEnum_ForwardLoc(Avatar avatar, HeroineEnum.ImgTileEnum imgTileEnum) {
        
        // Sets the enumerated value for the tile in front of the current player location to the passed value.
        // Reverses y and x to ease working with tiles.
        
        // Depending on direction player facing, ...
        switch (avatar.getFacing()) {
            
            case NORTH:
                
                // Facing north.
                
                // Alter tile.
                currentRegion.setRegionTileNbr( avatar.getY() - 1, avatar.getX(), imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            case SOUTH:
                
                // Facing south.
                
                // Alter tile.
                currentRegion.setRegionTileNbr( avatar.getY() + 1, avatar.getX(), imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Facing east.
                
                // Alter tile.
                currentRegion.setRegionTileNbr( avatar.getY(), avatar.getX() + 1, imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Facing west.
                
                // Alter tile.
                currentRegion.setRegionTileNbr( avatar.getY(), avatar.getX() - 1, imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            default:
                
                // Facing invalid direction.
                
                // Display warning.
                System.out.println("Warning:  Player facing invalid direction while checking tile at forward location.");
                
                // Exit selector.
                break;
                
        }
    
    }
    
    // avatar = Reference to player information.
    // imgTileEnum = Enumerated value to which to set tile.
    public void setImgTileEnum_SideLoc(Avatar avatar, HeroineEnum.ImgTileEnum imgTileEnum) {
        
        // Sets the enumerated value for the side tile in front of the current player location to the
        // passed value.
        
        // Depending on direction player facing, ...
        switch (avatar.getFacing()) {
            
            case NORTH:
                
                // Facing north.
                
                // Alter tile.
                currentRegion.setRegionTileNbrNorth( avatar.getX(), avatar.getY(), imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            case SOUTH:
                
                // Facing south.
                
                // Alter tile.
                currentRegion.setRegionTileNbrSouth( avatar.getX(), avatar.getY(), imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Facing east.
                
                // Alter tile.
                currentRegion.setRegionTileNbrEast( avatar.getX(), avatar.getY(), imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Facing west.
                
                // Alter tile.
                currentRegion.setRegionTileNbrWest( avatar.getX(), avatar.getY(), imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            default:
                
                // Facing invalid direction.
                
                // Display warning.
                System.out.println("Warning:  Player facing invalid direction while setting side tile at current location.");
                
                // Exit selector.
                break;
                
        }
    
    }
    
    // avatar = Reference to player information.
    // imgTileEnum = Enumerated value to which to set tile.
    public void setImgTileEnum_SideLoc_Forward(Avatar avatar, HeroineEnum.ImgTileEnum imgTileEnum) {
        
        // Sets the enumerated value for the side in the tile in front of (adjacent to) the current 
        // player location to the passed value.
        
        // Depending on direction player facing, ...
        switch (avatar.getFacing()) {
            
            case NORTH:
                
                // Facing north.
                
                // Alter tile.
                currentRegion.setRegionTileNbrSouth( avatar.getX(), (avatar.getY() - 1), 
                  imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            case SOUTH:
                
                // Facing south.
                
                // Alter tile.
                currentRegion.setRegionTileNbrNorth( avatar.getX(), (avatar.getY() + 1), 
                  imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Facing east.
                
                // Alter tile.
                currentRegion.setRegionTileNbrWest( (avatar.getX() + 1), avatar.getY(), 
                  imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Facing west.
                
                // Alter tile.
                currentRegion.setRegionTileNbrEast( (avatar.getX() - 1), avatar.getY(), 
                  imgTileEnum.getValue() );
                
                // Exit selector.
                break;
                
            default:
                
                // Facing invalid direction.
                
                // Display warning.
                System.out.println("Warning:  Player facing invalid direction while setting side tile at current location.");
                
                // Exit selector.
                break;
                
        }
    
    }
    
    public boolean isLockedDoorActiveInd() {
        return lockedDoorActiveInd;
    }
    
    public void setLockedDoorActiveInd(boolean lockedDoorActiveInd) {
        this.lockedDoorActiveInd = lockedDoorActiveInd;
    }
    
    public int getMap_id() {
        return map_id;
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
    
    public boolean getTileActiveInd(int tile) {
        // Return whether passed tile active.  Only active tiles are rendered.
        return tileActiveInd.get(tile);
    }
    
}