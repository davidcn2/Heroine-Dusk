package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import core.BaseActor;
import gui.CustomLabel;
import heroinedusk.AtlasItems.Chest;

// Java imports.
import java.util.ArrayList;
import java.text.DecimalFormat;
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
    
    mazemap_bounds_check:  Checks to see if the passed location exists in the current region.
    mazemap_render:  Returns the tiles used to render the passed location.
    mazemap_render_tile:  Returns a base actor representing the passed tile in the passed location.
    minimap_render:  Returns the icons used to render the minimap for the current map / region.
    */
    
    // Declare object variables.
    private final Atlas atlas; // Reference to the atlas information.
    private final AtlasItems atlasItems; // Reference to the atlas items information.
    private ArrayList<HeroineEnum.ItemEnum> chestOtherItems; // Additional items in chest(s).
    private ArrayList<Integer> chestOtherItemsQty; // Quantity of each additional item in chest(s).
    private final RegionMap currentRegion; // Reference to current region / map.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private ArrayList<BaseActor> goldPile; // List of gold actors.
    private final HashMap<HeroineEnum.MinimapCursorEnum, TextureRegion> minimapCursorRegions; // Unique set of
      // texture regions used with minimap cursors.
    private final HashMap<HeroineEnum.MinimapEnum, TextureRegion> minimapRegions; // Unique set of texture
      // regions used with minimap.
    private final ArrayList<ArrayList<Float>> minimapDestX; // X-coordinates for destination of icons in stage.
    private final ArrayList<ArrayList<Float>> minimapDestY; // Y-coordinates for destination of icons in stage.
    private final ArrayList<ArrayList<Integer>> regionTiles; // List of tiles composing the current region.
      // Example for use -- get(x).get(y):  Integer x = regionTiles.get(0).get(0);
    private HashMap<Integer, Integer> tileMap; // Cross reference with key = position (related to tile image 
      // offset) and value = array index (in array list with tile actors).  Also, includes special key values
      // for chest, treasure, and skull pile.
    
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
    private final int map_id; // Current region / map location (number).
    private final float minimapIconSize; // Size of an icon in minimap (width and height match).
    private final float minimapOffsetX; // X-coordinate of lower left corner of minimap.
    private final float minimapOffsetY; // Y-coordinate of lower left corner of minimap.
    private float minimapHeight; // Height of the minimap, including the background.
    private float minimapWidth; // Width of the minimap, including the background.
    private final String regionName; // Name of current region / map location.
      // Maps to one of the integers in the mapIdentifiers object in the atlas.
    private HeroineEnum.MusicEnum current_song; // Song associated with the current region / map location.
    private final int regionHeight; // Region height, in tiles.
    private final int regionWidth; // Region width, in tiles.
    
    // Declare constants.
    private final Color COLOR_MED_GRAY = new Color(0.50f, 0.50f, 0.50f, 1);
    //private static final Integer[] GOLD_BASE_POS_X_LIST =  new Integer[]{65, 56, 74, 74, 50, 63, 41, 90, 87, 29};
    private static final Integer[] GOLD_BASE_POS_X_LIST =  new Integer[]{36, 27, 45, 45, 21, 34, 12, 61, 58, 0};
    //private static final Integer[] GOLD_BASE_POS_Y_LIST =  new Integer[]{93, 96, 95, 86, 80, 78, 92, 76, 94, 77};
    private static final Integer[] GOLD_BASE_POS_Y_LIST =  new Integer[]{17, 20, 19, 10, 4, 2, 16, 0, 18, 1};
    private final String decFormatText000 = "000"; // Text used for decimal style used to format numbers as 000.
      // Examples:  1 > 001, 2 > 002, ...
    private final DecimalFormat decimalFormat000 = new DecimalFormat(decFormatText000); // Decimal style used to
      // format numbers as 000.  Examples:  1 > 001, 2 > 002, ...
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
        
        // 1.  Set defauls and initialize array lists and hash maps.
        
        // Disable events for chest in square in front of player (position 9).
        chestActiveInd = false;
        
        // Initialize array lists.
        regionTiles = new ArrayList<>();
        minimapDestX = new ArrayList<>();
        minimapDestY = new ArrayList<>();
        chestOtherItems = new ArrayList<>();
        chestOtherItemsQty = new ArrayList<>();
        
        // Initialize hash maps.
        minimapRegions = new HashMap<>();
        minimapCursorRegions = new HashMap<>();
        tileMap = new HashMap<>();
        
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
        this.map_id = map_id; // 0;
        
        // 5.  Get region name.
        regionName = atlas.mapIdentifiersRev.get(this.map_id);
        
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
    
    // posX = X-coordinate in map / region of chest(s).
    // posY = Y-coordinate in map / region of chest(s).
    // heroineWeapon = BaseActor object that acts as the player weapon.
    // weaponLabel = Label showing current player weapon.
    // heroineArmor = BaseActor object that acts as the player armor.
    // armorLabel = Label showing current player armor.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // goldLabel = Label showing player gold.
    private ArrayList<Chest> acquireChestContents(int posX, int posY, BaseActor heroineWeapon, CustomLabel weaponLabel, 
      BaseActor heroineArmor, CustomLabel armorLabel, CustomLabel hpLabel, CustomLabel mpLabel,
      CustomLabel goldLabel)
    {
        
        // The function gives the contents of the chest(s) at the passed location to the player.
        
        int chestCount; // Number of chests.
        ArrayList<Chest> chestList; // List of chests at the passed position.
        
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
    
    // bonePileActor = Reference to BaseActor for the bone pile tile.
    // viewWidth = Width of the stage.
    // mapActionButtonMagic = Hash map containing BaseActor objects that act as the spell action buttons.
    private void addEvent_BonePileActor(BaseActor bonePileActor, int viewWidth, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtonMagic)
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

                        System.out.println("Click on bone pile occurred");
                        
                    }
                    
                } // End ... touchUp event.
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                @Override
                public boolean mouseMoved (InputEvent event, float x, float y)
                {
                
                    // The function occurs when the mouse cursor or a finger touch is moved over the actor and
                    // a button is not down.  The event only occurs on the desktop.
                    
                    // If bone pile active, then...
                    if (bonePileActiveInd)
                    {
                        
                        // Bone pile active.
                    
                        // Set position of burn button center on current mouse position.
                        mapActionButtonMagic.get(
                          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setX(bonePileActor.getX() + 
                          x - mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).getWidth() 
                          / 2 );
                        mapActionButtonMagic.get(
                          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setY(bonePileActor.getY() + y - 
                          mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).getHeight() 
                          / 2 );

                        // If player has no magic left, then...
                        if (gameHD.getAvatar().getMp() == 0)
                        {

                            // Player has no magic left.

                            // Apply a dark shade to the button to signify not currently enabled.
                            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setColor( 
                              COLOR_MED_GRAY );

                        }

                        else
                        {

                            // Player has magic left.

                            // Removing shading from button to signify enabled.
                            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setColor(
                              Color.WHITE );

                        }

                        // Set burn button to visible.
                        mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setVisible(true);
                    
                    } // End ... If bone pile active.
                        
                    // Do not handle event.
                    return false;
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // fromActor = Reference to actor losing focus.
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
                {
                    
                    // The function occurs when the mouse cursor or a finger touch is moved over the actor.
                    
                    // Notes:  On the desktop, the event occurs even when no mouse buttons are pressed 
                    // (pointer will be -1).
                    // Notes:  Occurs when mouse cursor or finger touch is moved over the label.
                        
                    // If bone pile active, then...
                    if (bonePileActiveInd)
                    {
                        
                        // Bone pile active.
                    
                        // Apply a dark shade to the bone pile.
                        bonePileActor.setColor(Color.LIGHT_GRAY);
                        
                    }
                    
                } // End ... enter event.
                
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
                    
                    // If bone pile active, then...
                    if (bonePileActiveInd)
                    {
                        
                        // Bone pile active.
                    
                        // If ignoring next exit event, then...
                        if (ignoreNextExitEvent)

                            // Ignoring next exit event.
                            
                            // Flag to process next exit event.
                            ignoreNextExitEvent = false;

                        // Otherwise, ...
                        else
                        {
                            
                            // Process exit event.
                            
                            // If fade not occurring, then...
                            if (bonePileActor.getActionMapCount() == 0)
                            {
                                
                                // Fade not occurring yet.

                                // Return bone pile to normal color.
                                bonePileActor.setColor(Color.WHITE);
                                
                            }
                            
                            // Set burn button to not visible.
                            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setVisible(false);
                        
                        } // Processing exit event.
                            
                    } // End ... If bone pile active.
                        
                } // End ... exit event.
                
            }; // End ... InputListener.
        
        // Add event to tile actor.
        bonePileActor.addListener(tileEvent);
        
    }
    
    // chestActor = Reference to BaseActor for the chest tile.
    // treasureActor = Reference to BaseActor for the treasure tile.
    // treasureGroup = Reference to BaseActor for the treasure group tile.  Used for groups, like with gold.
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
    private void addEvent_ChestActor(BaseActor chestActor, BaseActor treasureActor, BaseActor treasureGroup,
      int viewWidth, CustomLabel treasureLabel, BaseActor heroineWeapon, CustomLabel weaponLabel, 
      BaseActor heroineArmor, CustomLabel armorLabel, CustomLabel hpLabel, CustomLabel mpLabel,
      CustomLabel goldLabel, CustomLabel regionLabel)
    {
        
        // The function adds events to the passed chest-related tile (BaseActor).
        // Events include touchDown, touchUp, enter, and exit.
        
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
                    
                    ArrayList<Chest> chestList; // List of chests at the tile.
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
                        
                        // 1.  Perform preliminary actions.
                    
                        // Play coin sound.
                        gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_COIN);

                        // 2.  Perform actions on chest actor.

                        // Set up an action to fade out the chest.
                        chestActor.addAction_FadeOut(0.25f, 0.75f);

                        System.out.println("Chest metadata: " + chestActor.getVirtualString());

                        // Get position of first comma in virtual text.
                        index1 = chestActor.getVirtualString().indexOf(",");

                        // Get position of second comma in virtual text.
                        index2 = chestActor.getVirtualString().indexOf(",", index1 + 1);

                        // Get x and y coordinates associated with the tile.
                        posX = Integer.valueOf( chestActor.getVirtualString().substring(index1 + 1, index2) );
                        posY = Integer.valueOf( chestActor.getVirtualString().substring(index2 + 1, 
                          chestActor.getVirtualString().length()) );

                        // 3.  Give chest contents to the player.

                        // Take chest contents.
                        chestList = acquireChestContents(posX, posY, heroineWeapon, weaponLabel, heroineArmor, 
                          armorLabel, hpLabel, mpLabel, goldLabel);
                        
                        // 4.  Perform actions on treasure actor and label.

                        // Display the first chest / item.
                        treasureImageInd = render_treasure( chestList.get(0).getPrimaryItem(), chestActor, 
                          treasureActor, treasureGroup, viewWidth, treasureLabel, 
                          chestList.get(0).getPrimaryItemCount() );

                        // If image available, draw treasure.
                        // Always show associated label.
                        draw_treasure(treasureImageInd, chestActor, treasureActor, treasureGroup, 
                          treasureLabel, viewWidth, 1.00f, regionLabel );
                        
                        // 5.  Remove chests from array list and hash map in atlas items.

                        // Remove chests from array list and hash map associated with current location.
                        gameHD.getAtlasItems().removeChests(chestList, map_id, posX, posY);

                        // 6.  Transform tile associated with chest.

                        // Transform tile with chest to show similar image to background -- removing chest.
                        transformChestTile(posX, posY);
                        
                    } // End ... If chest active.
                    
                } // End ... touchUp event.
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // fromActor = Reference to actor losing focus.
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
                {
                    
                    // The function occurs when the mouse cursor or a finger touch is moved over the actor.
                    
                    // Notes:  On the desktop, the event occurs even when no mouse buttons are pressed 
                    // (pointer will be -1).
                    // Notes:  Occurs when mouse cursor or finger touch is moved over the label.
                    
                    // If chest active, then...
                    if (chestActiveInd)
                    {
                        
                        // Chest active.
                        
                        // Apply a dark shade to the chest.
                        chestActor.setColor(Color.LIGHT_GRAY);
                        
                    }
                    
                } // End ... enter event.
                
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
                    if (chestActiveInd)
                    {
                        
                        // Chest active.
                    
                        // If ignoring next exit event, then...
                        if (ignoreNextExitEvent)

                            // Ignoring next exit event.

                            // Flag to process next exit event.
                            ignoreNextExitEvent = false;

                        // Otherwise, ...
                        else

                            // Process exit event.

                            // If fade not occurring, then...
                            if (chestActor.getActionMapCount() == 0)

                                // Fade not occurring yet.

                                // Return chest to normal color.
                                chestActor.setColor(Color.WHITE);
                        
                    } // End ... If chest active.
                        
                } // End ... exit event.
                
            }; // End ... InputListener.
        
        // Add event to tile actor.
        chestActor.addListener(tileEvent);
        
    }
    
    // treasureActor = Reference to actor acting as the treasure.
    // treasureLabel = Reference to label with the treasure description.
    // chestActor = Reference to BaseActor for the chest tile.
    // treasureGroup = Reference to BaseActor for the treasure group tile.  Used for groups, like with gold.
    // viewWidth = Width of the stage.
    private void addEvent_TreasureActor(BaseActor treasureActor, CustomLabel treasureLabel,
      BaseActor chestActor, BaseActor treasureGroup, int viewWidth)
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
                    float treasureHeight; // Height to which to resize treasure.
                    float treasureWidth; // Width to which to resize treasure.
                    boolean switchFromGold; // Whether switching from gold to non-gold.
                    boolean switchToGold; // Whether switching from non-gold to gold.
                    boolean treasureImageInd; // Whether treasure image exists for current item.
                    
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
                        treasureImageInd = false;
                        
                        // If gold pile exists, then...
                        if (goldPile != null)
                        {
                            // Gold pile exists.

                            // Remove actors in gold pile from treasure group.
                            goldPile.forEach((actor) -> {

                                // Remove current actor in loop from group.
                                treasureGroup.removeActor(actor);

                            });

                            // Clear out gold pile.
                            goldPile = null;
                            
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
                            
                            // Update actor information for gold pile and treasure group.
                            render_gold(chestActor, treasureGroup, viewWidth, 
                              chestOtherItemsQty.get(chestOtherItemIndex));
                            
                            // Remove existing actions on treasure group.
                            treasureGroup.removeActions();
                            
                            // Flag image of treasure as existing.
                            treasureImageInd = true;
                            
                            // If switching from treasure actor to gold pile, then...
                            if (switchToGold)
                            {
                                
                                // Switching from treasure actor to gold pile.
                                
                                // If texture for treasure actor initialized, then...
                                if (treasureActor.getRegion().getTexture() != null)
                                {
                                    // Texture for treasure actor initialized.
                                
                                    // Fade out treasure actor.
                                    treasureActor.addAction_FadeOut( 0.00f, 0.50f );
                                }
                                
                            }
                            
                            // Set up an action to fade in the treasure group.
                            treasureGroup.addAction_FadeIn( 0.50f, 0.50f );
                            
                            // Update the treasure description in the associated label and center horizontally.
                            render_treasure_label( treasureLabel, treasureImageInd, chestActor, treasureActor, 
                              treasureGroup );
                            
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
                                
                                // Switch treasure actor to more than a group.
                                treasureActor.setGroupOnlyInd(false);
                                
                                // Flag image of treasure as existing.
                                treasureImageInd = true;
                                
                                // Determine width and height to resize treasure.

                                // Magnify by scale factor.
                                treasureWidth = gameHD.getConfig().getScale() * 
                                  gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()).getRegionWidth();
                                treasureHeight = gameHD.getConfig().getScale() * 
                                  gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()).getRegionHeight();

                                // If chest actor "passed" (not null), then...
                                if (chestActor != null)
                                {

                                    // Chest actor "passed" (not null).
                                
                                    // If treasure width greater than chest, then... > Limit to chest width.
                                    if (treasureWidth > chestActor.getWidth())
                                        treasureWidth = chestActor.getWidth();

                                    // If treasure height greater than chest, then... > Limit to chest height.
                                    if (treasureHeight > chestActor.getHeight())
                                        treasureHeight = chestActor.getHeight();

                                    // Take the lower of the treasure width and height.
                                    treasureWidth = Math.min(treasureWidth, treasureHeight);
                                    treasureHeight = treasureWidth;
                                    
                                }
                                
                                // Remove existing treasure actor actions.
                                treasureActor.removeActions();
                                
                                // If switching from gold, then...
                                if (switchFromGold)
                                {
                                    
                                    // Switching from gold.
                                    
                                    // Fade out gold pile.
                                    treasureGroup.addAction_FadeOut( 0.00f, 0.50f );
                                
                                    // Update image for treasure actor.
                                    treasureActor.setTextureRegion( 
                                      gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()) );
                                    
                                    // Set dimensions of treasure actor.
                                    treasureActor.setWidth( treasureWidth );
                                    treasureActor.setHeight( treasureHeight );
                                    
                                    // Set treasure origin to center to allow for proper rotation.
                                    treasureActor.setOriginCenter();

                                    // If chest actor "passed" (not null), then...
                                    if (chestActor != null)
                                    {

                                        // Chest actor "passed" (not null).
                                    
                                        // Position treasure actor at center of stage horizontally and chest 
                                        // vertically.
                                        treasureActor.setPosition( (viewWidth - treasureWidth) / 2, 
                                          chestActor.getOriginY() );
                                        
                                    }
                                    
                                    else
                                    {
                                        
                                        // Chest actor NOT "passed" (null).
                                        
                                        // Position treasure actor at center of stage horizontally and 
                                        // vertically slightly above the bottom.
                                        treasureActor.setPosition( (viewWidth - treasureWidth) / 2, 
                                          TREASURE_POS_Y * gameHD.getConfig().getScale() );
                                        
                                    }
                                    
                                    // Set treasure actor to not visible to allow for fade in effect.
                                    treasureActor.setVisible( false );
                                    
                                    // Remove existing actions on treasure actor.
                                    treasureActor.removeActions();
                                    
                                    // Add fade in effect to treasure actor.
                                    treasureActor.addAction_FadeIn( 0.50f, 0.50f );
                                    
                                }
                                
                                else
                                {
                                    // Displaying another non-gold item.
                                    
                                    // If texture for treasure actor initialized, then...
                                    if (treasureActor.getRegion().getTexture() != null)
                                    {
                                        // Texture for treasure actor initialized.
                                        
                                        // Remove existing actions on treasure actor.
                                        treasureActor.removeActions();
                                        
                                        // Fade in the next treasure actor / image.
                                        treasureActor.addAction_FadeIn( 0.50f, 0.50f, 
                                          gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()), 
                                          imgTreasureEnum.toString(), treasureWidth, treasureHeight );
                                    }
                                    
                                    else
                                    {
                                        // Texture for treasure actor NOT initialized.
                                        
                                        // Update image for treasure actor.
                                        treasureActor.setTextureRegion( 
                                          gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()) );
                                        
                                        // If chest actor "passed" (not null), then...
                                        if (chestActor != null)
                                        {
                                            // Chest actor "passed" (not null).
                                            
                                            // Position treasure actor at center of stage horizontally and 
                                            // chest vertically.
                                            treasureActor.setPosition( (viewWidth - treasureWidth) / 2, 
                                              chestActor.getOriginY() );
                                        }
                                        
                                        else
                                        {
                                            // Chest actor NOT "passed" (null).
                                            
                                            // Position treasure actor at center of stage horizontally and 
                                            // vertically slightly above the bottom.
                                            treasureActor.setPosition( (viewWidth - treasureWidth) / 2, 
                                              TREASURE_POS_Y * gameHD.getConfig().getScale() );
                                        }
                                        
                                        // Resize treasure actor based on scale.
                                        treasureActor.setWidth( treasureWidth );
                                        treasureActor.setHeight( treasureHeight );

                                        // Set treasure origin to center to allow for proper rotation.
                                        treasureActor.setOriginCenter();
                                        
                                        // Set up an action to fade in the treasure.
                                        treasureActor.addAction_FadeIn( 0.50f, 0.50f );
                                    } // End ... If texture for treasure actor NOT initialized.
                                    
                                } // End ... If displaying another non-gold item.
                                
                                // Update the treasure description in the associated label and center horizontally.
                                render_treasure_label( treasureLabel, treasureImageInd, chestActor, 
                                  treasureActor, treasureGroup );
                                
                                // Move to the next item.
                                chestOtherItemIndex++;
                                
                            } // End ... If enumeration value points at an image.
                            
                            else
                            {
                                
                                // Enumeration value lacks an image.
                                
                                // If texture for treasure actor initialized, then...
                                if (treasureActor.getRegion().getTexture() != null)
                                {
                                    // Texture for treasure actor initialized.
                                
                                    // Fade out the treasure actor.
                                    treasureActor.addAction_FadeOut( 0.50f, 0.50f );
                                }
                                
                                // If gold pile exists, then...
                                if (goldPile != null)
                                {
                                    // Gold pile exists.
                                
                                    // Fade out the treasure group.
                                    treasureGroup.addAction_FadeOut( 0.50f, 0.50f );
                                }
                                
                                // Update the treasure description in the associated label and center 
                                // horizontally.
                                render_treasure_label( treasureLabel, treasureImageInd, chestActor, 
                                  treasureActor, treasureGroup );
                                
                                // Move to the next item.
                                chestOtherItemIndex++;
                                
                            } // End ... If enumeration value lacks an image.
                            
                        } // End ... If item NOT gold.
                        
                    } // End ... If additional items remain to display.
                    
                    else
                    {
                        
                        // At last item to display.
                        
                        // Set up an action to fade out the treasure actor and label.
                        treasureActor.addAction_FadeOut(0f, 1f);
                        treasureLabel.addAction_FadeOut(0f, 1f);
                        
                    }
                    
                }
                
                /*
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // fromActor = Reference to actor losing focus.
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
                {
                    
                    // The function occurs when the mouse cursor or a finger touch is moved over the actor.
                    
                    // Notes:  On the desktop, the event occurs even when no mouse buttons are pressed 
                    // (pointer will be -1).
                    // Notes:  Occurs when mouse cursor or finger touch is moved over the label.
                    
                    // Apply a dark shade to the treasure.
                    treasureActor.setColor(Color.LIGHT_GRAY);
                    
                }
                
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
                    
                    // If ignoring next exit event, then...
                    if (ignoreNextExitEvent)
                        
                        // Ignoring next exit event.
                        
                        // Flag to process next exit event.
                        ignoreNextExitEvent = false;
                    
                    // Otherwise, ...
                    else 
                    {
                        
                        // Process exit event.
                        
                        // If fade out not occurring, then...
                        if (!treasureLabel.getActionMapKeyInd("FadeOut"))
                            
                            // Fade not occurring yet.
                            
                            // Return treasure to normal color.
                            treasureActor.setColor(Color.WHITE);
                        
                    }
                        
                }
                
                */
                
            }; // End ... InputListener.
        
        // Add event to actor and label.
        treasureActor.addListener(treasureEvent);
        treasureGroup.addListener(treasureEvent);
        treasureLabel.getLabel().addListener(treasureEvent);
        
    }
    
    // treasureImageInd = Whether treasure image exists for current item.
    // chestActor = Reference to BaseActor for the chest tile.
    // treasureActor = Reference to BaseActor for the treasure tile.
    // treasureGroup = Reference to BaseActor for the treasure group tile.  Used for groups, like with gold.
    // treasureLabel = Reference to label with the treasure description.
    private float determine_treasure_label_pos_y(boolean treasureImageInd, BaseActor chestActor, 
      BaseActor treasureActor, BaseActor treasureGroup, CustomLabel treasureLabel)
    {
        
        // The function places the treasure label.
        
        float pos_y; // Y-coordinate at which to set bottom of label.
        
        // If showing treasure, then...
        if (treasureImageInd)
        {

            // Showing treasure.

            // If rendering group, then...
            if (goldPile != null)
            {

                // Rendering group.
                
                // If chest actor "passed" (not null), then...
                if (chestActor != null)
                {

                    // Chest actor "passed" (not null).
                
                    // Set the vertical position of the label to center between the treasure group 
                    // and the bottom of the stage.
                    pos_y = (treasureGroup.getY() - treasureLabel.getLabelHeight()) / 2 ;
                    
                }
                
                else
                {
                    
                    // Chest actor NOT "passed" (null).
                    
                    // Set the vertical position of the label to be slightly above the treasure group.
                    pos_y = treasureGroup.getY() + treasureGroup.getGroupHeight() + 
                      TREASURE_LABEL_POS_Y * gameHD.getConfig().getScale();
                    
                }

            }

            else
            {

                // Rendering non-group.

                // If chest actor "passed" (not null), then...
                if (chestActor != null)
                {

                    // Chest actor "passed" (not null).
                
                    // Set the vertical position of the label to center between the treasure and 
                    // bottom of the stage.
                    pos_y = (treasureActor.getY() - treasureLabel.getLabelHeight()) / 2;
                    
                }
                
                else
                {
                    
                    // Chest actor NOT "passed" (null).
                    
                    // Set the vertical position of the label to be slightly above the treasure.
                    pos_y = treasureActor.getY() + treasureActor.getHeight() + 
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
    
    // treasureImageInd = Whether treasure image exists for primary item in first chest.
    // chestActor = Reference to BaseActor for the chest tile.
    // treasureActor = Reference to BaseActor for the treasure tile.
    // treasureGroup = Reference to BaseActor for the treasure group tile.  Used for groups, like with gold.
    // treasureLabel = Reference to label with the treasure description.
    // viewWidth = Width of the stage.
    // delay = Time, in seconds, before fade occurs.
    // regionLabel = Label showing the current region name.
    private void draw_treasure(boolean treasureImageInd, BaseActor chestActor, BaseActor treasureActor, 
      BaseActor treasureGroup, CustomLabel treasureLabel, int viewWidth, float delay, CustomLabel regionLabel)
    {
        
        // The function (conditionally) displays the treaure.
        // The function always displays the associated label.
        // NOTE:  The function is only called when rendering the first item in the chest.
        
        float pos_y; // Y-coordinate at which to set bottom of label.
        
        // Store y-coordinate at which to set bottom of label.
        pos_y = determine_treasure_label_pos_y( treasureImageInd, chestActor, treasureActor, treasureGroup, 
          treasureLabel );
        
        // Update the vertical position of the label.
        treasureLabel.setPosY( pos_y );
        
        // If showing treasure, then...
        if (treasureImageInd)
        {

            // Showing treasure.

            // If rendering group, then...
            if (goldPile != null)
            {

                // Rendering group.

                // Remove existing actions on treasure group.
                treasureGroup.removeActions();

                // Set up an action to fade in the treasure group.
                treasureGroup.addAction_FadeIn( delay, 0.50f );

            }

            else
            {

                // Rendering non-group.

                // Remove existing actions on treasure actor.
                treasureActor.removeActions();

                // Set up an action to fade in the treasure group.
                treasureActor.addAction_FadeIn( delay, 0.50f );

            } // End ... If Rendering non-group.
            
        } // End ... If showing treasure.
        
        // Add events to treasure actor / group / label to display next item (or fade) upon click.
        addEvent_TreasureActor( treasureActor, treasureLabel, chestActor, treasureGroup, viewWidth );
        
        // Clear the label with the region name.
        regionLabel.setLabelText("");
        
        // Remove actions on treasure label.
        treasureLabel.removeActions();
        
        // Set up an action to fade in the label.
        treasureLabel.addAction_FadeIn( delay, 0.50f );
        
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
    // mapActionButtonMagic = Hash map containing BaseActor objects that act as the spell action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of spell action buttons.
    public ArrayList<BaseActor> mazemap_render(int x, int y, HeroineEnum.FacingEnum facing, int viewWidth,
      CustomLabel treasureLabel, BaseActor heroineWeapon, CustomLabel weaponLabel, BaseActor heroineArmor, 
      CustomLabel armorLabel, CustomLabel hpLabel, CustomLabel mpLabel, CustomLabel goldLabel,
      CustomLabel regionLabel, CustomLabel statusLabel, boolean turnInd, boolean redrawInd,
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtonMagic, 
      Map<String, Boolean> mapActionButtonEnabled)
    {
        
        /*
        The function returns the tiles used to render the passed location.
        The function also populates the hash map used as a cross reference between the positions and 
        array indices.
        
        Beyond building the tile array list, logic includes:
        
        1.  Adding event-supported chest when in square in front of player.
        2.  Handling chest in same square as player.
        3.  Handling hay bale in same square as player.
        4.  Adding event-supported skull pile in square in front of player.
        
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
        
        String actorName; // Name of current actor in loop.
        boolean bonePileInd; // Whether bone pile exists immediately in front of player.
        boolean chestInd; // Whether chest exists immediately in front of player.
        ArrayList<Chest> chestList; // List of chests at the passed position.
        boolean chestNearbyInd; // Whether chest exists either in current location or immediately in front 
          // of player.
        int counter; // Used to increment through array list of BaseActor objects associated with tiles.
        Integer key; // Key to use when adding value to hash map.
        ArrayList<BaseActor> removeList; // List of actors to remove.
        BaseActor tempBonePile; // Holder for the (bone pile) actor.
        BaseActor tempChest; // Holder for the (chest) actor.
        BaseActor tempTreasure; // Holder for the (treasure) actor.  For non-group functionality.
        BaseActor tempTreasureGroup; // Holder for the (treasure) actor.  For group functionality.
        int tileNbr; // Tile number at current (exact) location.
        ArrayList<BaseActor> tiles; // BaseActor objects associated with tiles.
        boolean treasureImageInd; // Whether treasure image exists for primary item in first chest.
        String virtualString; // Virtual field (string) for the actor with the chest(s).
        
        // 1.  Set defaults.
        chestInd = false;
        bonePileInd = false;
        chestNearbyInd = false;
        chestActiveInd = false;
        bonePileActiveInd = false;
        virtualString = "";
        goldPile = null;
        tempTreasure = null;
        tempTreasureGroup = null;
        
        // 2.  Initialize array lists and hash maps.
        tiles = new ArrayList<>();
        removeList = new ArrayList<>();
        tileMap = new HashMap<>();
        
        // 3.  Prepare tiles for rendering.
        
        System.out.println("X: " + x + ", Y: " + y + ", Facing: " + facing);
        
        // Depending on direction facing, ...
        switch (facing) {
            
            case NORTH:
                
                // Render back row.
                tiles.add(mazemap_render_tile( x - 2, y - 2, 0, -2,  0 ));
                tiles.add(mazemap_render_tile( x + 2, y - 2, 1, +2, -2 ));
                tiles.add(mazemap_render_tile( x - 1, y - 2, 2, -1, -2 ));
                tiles.add(mazemap_render_tile( x + 1, y - 2, 3, +1, -2 ));
                tiles.add(mazemap_render_tile( x    , y - 2, 4,  0, -2 ));
                
                // Render middle row.
                tiles.add(mazemap_render_tile( x - 2, y - 1, 5, -2, -1 ));
                tiles.add(mazemap_render_tile( x + 2, y - 1, 6, +2, -1 ));
                tiles.add(mazemap_render_tile( x - 1, y - 1, 7, -1, -1 ));
                tiles.add(mazemap_render_tile( x + 1, y - 1, 8, +1, -1 ));
                tiles.add(mazemap_render_tile( x    , y - 1, 9,  0, -1 ));
                
                // Render front row.
                
                tiles.add(mazemap_render_tile( x - 1, y, 10, -1, 0 ));
                tiles.add(mazemap_render_tile( x + 1, y, 11, +1, 0 ));
                tiles.add(mazemap_render_tile( x    , y, 12,  0, 0 ));
                
                // Exit selector.
                break;
                
            case SOUTH: // Starting direction.
                
                // Render back row.
                tiles.add(mazemap_render_tile( x + 2, y + 2, 0, +2, +2 ));
                tiles.add(mazemap_render_tile( x - 2, y + 2, 1, -2, +2 ));
                tiles.add(mazemap_render_tile( x + 1, y + 2, 2, +1, +2 ));
                tiles.add(mazemap_render_tile( x - 1, y + 2, 3, -1, +2 ));
                tiles.add(mazemap_render_tile( x    , y + 2, 4,  0, +2 ));
                
                // Render middle row.
                tiles.add(mazemap_render_tile( x + 2, y + 1, 5, +2, +1 ));
                tiles.add(mazemap_render_tile( x - 2, y + 1, 6, -2, +1 ));
                tiles.add(mazemap_render_tile( x + 1, y + 1, 7, +1, +1 ));
                tiles.add(mazemap_render_tile( x - 1, y + 1, 8, -1, +1 ));
                tiles.add(mazemap_render_tile( x    , y + 1, 9,  0, +1 ));
                
                // Render front row.
                tiles.add(mazemap_render_tile( x + 1, y, 10, +1, 0 ));
                tiles.add(mazemap_render_tile( x - 1, y, 11, -1, 0 ));
                tiles.add(mazemap_render_tile( x    , y, 12,  0, 0 ));
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Render back row.
                tiles.add(mazemap_render_tile( x + 2, y - 2, 0, +2, -2 ));
                tiles.add(mazemap_render_tile( x + 2, y + 2, 1, +2, +2 ));
                tiles.add(mazemap_render_tile( x + 2, y - 1, 2, +2, -1 ));
                tiles.add(mazemap_render_tile( x + 2, y + 1, 3, +2, +1 ));
                tiles.add(mazemap_render_tile( x + 2, y    , 4, +2,  0 ));
                
                // Render middle row.
                tiles.add(mazemap_render_tile( x + 1, y - 2, 5, +1, -2 ));
                tiles.add(mazemap_render_tile( x + 1, y + 2, 6, +1, +2 ));
                tiles.add(mazemap_render_tile( x + 1, y - 1, 7, +1, -1 ));
                tiles.add(mazemap_render_tile( x + 1, y + 1, 8, +1, +1 ));
                tiles.add(mazemap_render_tile( x + 1, y    , 9, +1,  0 ));
                
                // Render front row.
                tiles.add(mazemap_render_tile( x, y - 1, 10, 0, -1 ));
                tiles.add(mazemap_render_tile( x, y + 1, 11, 0, +1 ));
                tiles.add(mazemap_render_tile( x,     y, 12, 0,  0 ));
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Render back row.
                tiles.add(mazemap_render_tile( x - 2, y + 2, 0, -2, +2 ));
                tiles.add(mazemap_render_tile( x - 2, y - 2, 1, -2, -2 ));
                tiles.add(mazemap_render_tile( x - 2, y + 1, 2, -2, +1 ));
                tiles.add(mazemap_render_tile( x - 2, y - 1, 3, -2, -1 ));
                tiles.add(mazemap_render_tile( x - 2, y    , 4, -2,  0 ));
                
                // Render middle row.
                tiles.add(mazemap_render_tile( x - 1, y + 2, 5, -1, +2 ));
                tiles.add(mazemap_render_tile( x - 1, y - 2, 6, -1, -2 ));
                tiles.add(mazemap_render_tile( x - 1, y + 1, 7, -1, +1 ));
                tiles.add(mazemap_render_tile( x - 1, y - 1, 8, -1, -1 ));
                tiles.add(mazemap_render_tile( x - 1, y    , 9, -1,  0 ));
                
                // Render front row.
                tiles.add(mazemap_render_tile( x, y + 1, 10, 0, +1 ));
                tiles.add(mazemap_render_tile( x, y - 1, 11, 0, -1 ));
                tiles.add(mazemap_render_tile( x, y    , 12, 0,  0 ));  
                
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
        
        // 4.  Populate tile cross reference hash map.
        
        // Set starting counter value.
        counter = 0;
        
        // Key = Position (related to tile image offset -- where to crop picture).
        // Value = Index in array list of base actors returned (non-null tiles).
        
        // Loop through tiles.
        for (BaseActor tile : tiles)
        {
            
            // Store name of current actor in loop.
            actorName = tile.getActorName();
            
            // Determine key to use when adding entry to hash map.
            key = Integer.valueOf(actorName.substring(tile.getActorName().lastIndexOf("_") + 1));
            
            // Add entry to hash map.
            tileMap.put(key, counter);
            
            // Increment counter.
            counter++;
            
        }
        
        // 5.  Check whether chest or bone pile exists immediately in front of player.
        
        // Loop through tiles.
        for (BaseActor tile : tiles)
        {
            
            // If tile metadata five characters or more, then...
            if (tile.getVirtualString().length() >= 5)
            {
                
                // Tile metadata five characters or more.
                
                // If tile associated with chest, then...
                if (tile.getVirtualString().substring(0, 5).equalsIgnoreCase("Chest"))
                {

                    // Tile associated with chest.

                    // Flag chest.
                    chestInd = true;

                    // Copy virtual string.
                    virtualString = tile.getVirtualString();
                    
                    // Exit loop.
                    break;

                }
                
                // Otherwise, if tile associated with bone pile, then...
                else if (tile.getVirtualString().substring(0, 9).equalsIgnoreCase("Bone Pile"))
                {
                    
                    // Tile associated with bone pile.
                    
                    // Flag bone pile.
                    bonePileInd = true;
                    
                    // Copy virtual string.
                    virtualString = tile.getVirtualString();
                    
                    // Exit loop.
                    break;
                    
                }
                
            } // End ... If tile metadata five characters or more.
            
        } // End ... Loop through tiles.
        
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
            tempTreasure = new BaseActor();
            tempTreasureGroup = new BaseActor();

            // Name treasure actor and group.
            tempTreasure.setActorName( "Treasure" );
            tempTreasureGroup.setActorName( "Treasure Group" );

            // Set treasure actor and group to hidden.
            tempTreasure.setVisible( false );
            tempTreasureGroup.setVisible( false );

            // Add treasure actor to the list.
            tiles.add( tempTreasure );

            // Add entry to cross reference hash map.
            tileMap.put( HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_TREASURE.getValue(), counter );

            // Incement hash map counter.
            counter++;

            // Add treasure group to the list.
            tiles.add( tempTreasureGroup );

            // Add entry to cross reference hash map.
            tileMap.put( HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_TREASURE_GROUP.getValue(), counter );

            // Incement hash map counter.
            counter++;
            
        }
        
        // If adding actor for chest -- immediately in front of player, then...
        if (chestInd)
        {
            
            // Adding actor for chest -- immediately in front of player.
            
            // Create actor for the chest, setting position in the next step.
            tempChest = new BaseActor( "Chest", 
              gameHD.getAssetMgr().getImage_xRef(HeroineEnum.ImgOtherEnum.IMG_OTHER_CHEST.getValue_Key()), 
              0f, 0f );
            
            // Set position of actor -- centered horizontally and up about 1/4 from the bottom.
            tempChest.setPosition( (float)((viewWidth - tempChest.getWidth()) / 2), 
              28f * gameHD.getConfig().getScale() );
            
            // Set origin of actor to center of associated image.
            tempChest.setOriginCenter();
            
            // Set virtual string of actor.
            tempChest.setVirtualString( virtualString );
            
            // Add events to actor.
            addEvent_ChestActor( tempChest, tempTreasure, tempTreasureGroup, viewWidth, treasureLabel, 
              heroineWeapon, weaponLabel, heroineArmor, armorLabel, hpLabel, mpLabel, goldLabel,
              regionLabel );
            
            // Add chest actor to the list.
            tiles.add( tempChest );
            
            // Add entry to cross reference hash map.
            tileMap.put( HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_CHEST.getValue(), counter );
            
            // Incement hash map counter.
            counter++;
            
        } // End ... If adding actor for chest -- immediately in front of player.
        
        // 7.  If necessary, add actor for bone pile.
        //     If no bone pile exists, disable burn button.
        
        // If bone pile exists immediately in front of player, then...
        if ( bonePileInd )
        {
            
            // Bone pile exists immediately in front of player.
            
            System.out.println("Bone pile exists immediately in front of player.");
            
            // Create actor for the bone pile, setting position in the next step.
            tempBonePile = new BaseActor( "Bone Pile", 
              gameHD.getAssetMgr().getImage_xRef(HeroineEnum.ImgOtherEnum.IMG_OTHER_SKULL_PILE.getValue_Key()), 
              0f, 0f );
            
            // Set position of actor -- centered horizontally and up about 1/4 from the bottom.
            tempBonePile.setPosition( (float)((viewWidth - tempBonePile.getWidth()) / 2), 
              28f * gameHD.getConfig().getScale() );
            
            // Set origin of actor to center of associated image.
            tempBonePile.setOriginCenter();
            
            // Set virtual string of actor.
            tempBonePile.setVirtualString( virtualString );
            
            // Add events to actor.
            addEvent_BonePileActor( tempBonePile, viewWidth, mapActionButtonMagic );
            
            // Add bone pile actor to the list.
            tiles.add( tempBonePile );
            
            // Add entry to cross reference hash map.
            tileMap.put( HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_BONE_PILE.getValue(), counter );
            
            // Enable burn button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN.toString(), true);
            
            // Incement hash map counter.
            counter++;
            
        } // End ... If bone pile exists immediately in front of player.
        
        else
        {
            
            // Bone pile DOES NOT exist immediately in front of player.
            
            // Disable burn button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN.toString(), false);
            
        }
            
        
        // 8.  Handle objects that exist in current location (at feet of player).
        
        // If player has already moved, is headed forward or backward, and a redraw is not occurring, then...
        if ( gameHD.getAvatar().movedInd() && !turnInd && !redrawInd )
        {
            
            // Player has already moved, is headed forward or backward, and a redraw is not occurring.
            
            // Depending on tile in current location...
            switch ( HeroineEnum.ImgTileEnum.valueOf(tileNbr) ) {
                
                case IMG_TILE_CHEST_EXTERIOR:
                case IMG_TILE_CHEST_INTERIOR:

                    // Chest exists in current location.

                    // Disable chest events -- related to square in front of player (not current location).
                    chestActiveInd = false;

                    // Play coin sound.
                    gameHD.getSounds().playSound( HeroineEnum.SoundEnum.SOUND_COIN );

                    // Take chest contents.
                    chestList = acquireChestContents( x, y, heroineWeapon, weaponLabel, heroineArmor, 
                      armorLabel, hpLabel, mpLabel, goldLabel );

                    // Display the first chest / item.
                    treasureImageInd = render_treasure( chestList.get(0).getPrimaryItem(), null, 
                      tempTreasure, tempTreasureGroup, viewWidth, treasureLabel, 
                      chestList.get(0).getPrimaryItemCount() );

                    // If image available, draw treasure.
                    // Always show associated label.
                    draw_treasure( treasureImageInd, null, tempTreasure, tempTreasureGroup, treasureLabel, 
                      viewWidth, 0.00f, regionLabel );

                    // Remove chests from array list and hash map associated with current location.
                    gameHD.getAtlasItems().removeChests( chestList, map_id, x, y );

                    // Transform tile with chest to show similar image to background -- removing chest.
                    transformChestTile( x, y );

                    // Exit selector.
                    break;

                case IMG_TILE_HAY_PILE:

                    // Hay pile exists in current location.

                    // Play coin sound.
                    gameHD.getSounds().playSound( HeroineEnum.SoundEnum.SOUND_COIN );
                    
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
        
        // 9.  Return the array list with the base actors for the tiles.
        return tiles;
        
    }
    
    // pos_x = Y-coordinate of tile to render.
    // pos_y = X-coordinate of tile to render.
    // position = Position information for tile to render (offset within source graphic).
    // adj_x = Adjustment to X-coordinate used to reach rendering location.
    // adj_y = Adjustment to Y-coordinate used to reach rendering location.
    public BaseActor mazemap_render_tile(int pos_x, int pos_y, int position, int adj_x, int adj_y)
    {
        // The function returns a base actor representing the passed tile in the passed location.
        // Note: x, y flipped to ease map making.
        
        BaseActor temp; // Holder for the BaseActor to return.
        int tileNbr; // Tile number to render.
        String key; // Key for the tile to display -- in the asset manager.
        String virtualString; // Virtual text to associate with BaseActor.
        
        // Set defaults.
        temp = null;
        virtualString = "";
        
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
                //return null;
            }
            
            else
            {
                
                // Tile exists in current region and NOT a placeholder (empty location).
                
                //System.out.println("tileNbr = " + tileNbr);
                //System.out.println("position = " + position);
                
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
                
                // Return the base actor for the tile (possibly null if out of bounds).
                temp = new BaseActor("TilePos_" + Integer.toString(position), 
                  gameHD.getAssetMgr().getTextureRegion(key), dest_x[position], dest_y[position] );
                
                // Store tile type in actor.
                temp.setVirtualInt(tileNbr);
                
                // Store virtual text in actor.
                temp.setVirtualString(virtualString);
                
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
          minimapCursorRegions.get(keyCursor), 
          minimapDestX.get(gameHD.getAvatar().getY()).get(gameHD.getAvatar().getX()), 
          minimapDestY.get(gameHD.getAvatar().getY()).get(gameHD.getAvatar().getX()) ) );
        
        // Return the array list with the base actors for the icons.
        return icons;

    }
    
    // tileMapKeyEnum = Enumerated value for tile type to remove -- chest, treasure, bone pile, ...
    public void removeTileMap_Value(HeroineEnum.TileMapKeyEnum tileMapKeyEnum) 
    {
        // The function removes the value from the tile map hash map related to the passed key.
        // The key is limited to one of the values in the enumerated list -- chest, treasure, ...
        tileMap.remove(tileMapKeyEnum.getValue()); 
    }
    
    // chestActor = Reference to BaseActor for the chest tile.
    // treasureGroup = Reference to BaseActor for the treasure group tile.  Used for groups, like with gold.
    // viewWidth = Width of the stage.
    // quantity = Number of items to render.
    private void render_gold(BaseActor chestActor, BaseActor treasureGroup, int viewWidth, int quantity)
    {
        
        // The function populates the gold pile (array list of actors) and related treasure group.
        
        ArrayList<Integer> bitwiseList; // List of numbers that add up to gold amount.
        int destIndex; // Index in arrays, GOLD_BASE_POS_X_LIST and GOLD_BASE_POS_Y_LIST, of positions to 
          // place gold.
        float goldHeight; // Height of current gold actor (in group).
        float goldMaxX; // Maximum x-coordinate for placement of gold (in group).
        float goldMaxY; // Maximum y-coordinate for placement of gold (in group).
        float goldMinX; // Minimum x-coordinate for placement of gold (in group).
        float goldMinY; // Minimum y-coordinate for placement of gold (in group).
        float goldPosX; // X-coordinate to place current gold actor (in group).
        float goldPosY; // Y-coordinate to place current gold actor (in group).
        float goldWidth; // Width of current gold actor (in group).
        HeroineEnum.ImgTreasureEnum imgTreasureEnum; // Enumerated value for treasure.
        BaseActor tempGold; // Temporary actor for displaying current gold in loop.
        float treasureHeight; // Height to which to resize treasure.
        float treasureWidth; // Width to which to resize treasure.
        
        // Set defaults for gold.
        goldMinX = 999999;
        goldMinY = 999999;
        goldMaxX = 0;
        goldMaxY = 0;

        // Populate list of numbers that add up to gold amount.
        bitwiseList = routines.UtilityRoutines.bitwiseList(quantity);

        // Initialize array list for gold pile.
        goldPile = new ArrayList<>();

        // Loop through gold amounts to show.
        for (Integer gold : bitwiseList)
        {

            //System.out.println("Gold image: " + HeroineEnum.ImgTreasureEnum.getValue_AtlasKey_Gold(gold));

            // Get treasure enumeration value.
            imgTreasureEnum = HeroineEnum.ImgTreasureEnum.valueOf("IMG_TREASURE_GOLD_" + Integer.toString(gold));

            // Determine index in position lists with location to place gold.
            destIndex = imgTreasureEnum.getValue();

            // Determine x and y coordinates to place current gold actor (in group / loop).
            goldPosX = GOLD_BASE_POS_X_LIST[destIndex] * gameHD.getConfig().getScale();
            goldPosY = GOLD_BASE_POS_Y_LIST[destIndex] * gameHD.getConfig().getScale();

            // Determine dimensions of current gold actor (in group / loop).
            goldWidth = gameHD.getConfig().getScale() * 
              gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()).getRegionWidth();
            goldHeight = gameHD.getConfig().getScale() * 
              gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()).getRegionHeight();

            // Configure and add actor to gold pile.

            // Create new actor.
            tempGold = new BaseActor("GOLD" + Integer.toString(gold), imgTreasureEnum.getValue_AtlasKey(), 
              goldPosX, goldPosY, gameHD.getAssetMgr());

            // Scale actor.
            tempGold.setWidth(goldWidth);
            tempGold.setHeight(goldHeight);

            // Add image to gold pile.
            goldPile.add(tempGold);

            // Store minimum and maximum x and y to calculate dimensions and adjust base location to 0, 0.
            goldMinX = Math.min(goldMinX, goldPosX);
            goldMinY = Math.min(goldMinY, goldPosY);
            goldMaxX = Math.max(goldMaxX, goldPosX + goldWidth);
            goldMaxY = Math.max(goldMaxY, goldPosY + goldHeight);

        }

        // Loop through gold actors in group.
        for (BaseActor gold : goldPile)
        {
            // Adjust base location to 0, 0 -- allows easy positioning of group and proper centering.
            gold.setX(gold.getX() - goldMinX);
            gold.setY(gold.getY() - goldMinY);

            // Add actor to group.
            treasureGroup.addActor(gold);
        }

        // Set treasure actor to function / draw as group only.
        treasureGroup.setGroupOnlyInd(true);

        // Determine width and height of treasure.
        treasureWidth = goldMaxX - goldMinX;
        treasureHeight = goldMaxY - goldMinY;

        // Store width and height of treasure (group) in actor.
        treasureGroup.setGroupWidth(treasureWidth);
        treasureGroup.setGroupHeight(treasureHeight);
        
        // Set treasure origin to center to allow for proper rotation.
        treasureGroup.setOriginCenter_Group();

        // If chest actor "passed" (not null), then...
        if (chestActor != null)
        {

            // Chest actor "passed" (not null).
        
            // Position treasure actor (group) at center of stage horizontally and chest vertically.
            treasureGroup.setPosition( (viewWidth - treasureWidth) / 2, chestActor.getOriginY() );
        
        }
        
        else
        {
            
            // Chest actor NOT "passed" (null).
            
            // Position treasure actor (group) at center of stage horizontally and vertically slightly above 
            // the bottom.
            treasureGroup.setPosition( (viewWidth - treasureWidth) / 2, 
              TREASURE_POS_Y * gameHD.getConfig().getScale() );
            
        }
        
    }
    
    // itemEnum = Enumerated value for the item to render the associated treasure image.
    // chestActor = Reference to BaseActor for the chest tile.
    // treasureActor = Reference to BaseActor for the treasure tile.
    // treasureGroup = Reference to BaseActor for the treasure group tile.  Used for groups, like with gold.
    // viewWidth = Width of the stage.
    // treasureLabel = Reference to label with the treasure description.
    // quantity = Number of items to render.
    private boolean render_treasure(HeroineEnum.ItemEnum itemEnum, BaseActor chestActor, 
      BaseActor treasureActor, BaseActor treasureGroup, int viewWidth, CustomLabel treasureLabel, 
      int quantity)
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
            
            // Populate actor information for gold pile and treasure group.
            render_gold(chestActor, treasureGroup, viewWidth, quantity);
            
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
                treasureActor.setTextureRegion( gameHD.getAssetMgr().getTextureRegion(treasureKey) );
                
                // Determine width and height to resize treasure.
                
                // Magnify by scale factor.
                treasureWidth = gameHD.getConfig().getScale() * treasureActor.getWidth();
                treasureHeight = gameHD.getConfig().getScale() * treasureActor.getHeight();

                // If chest actor "passed" (not null), then...
                if (chestActor != null)
                {
                    
                    // Chest actor "passed" (not null).
                    
                    // Constrain treasure dimensions to those of chest.
                    
                    // If treasure width greater than chest, then... > Limit to chest width.
                    if (treasureWidth > chestActor.getWidth())
                        treasureWidth = chestActor.getWidth();

                    // If treasure height greater than chest, then... > Limit to chest height.
                    if (treasureHeight > chestActor.getHeight())
                        treasureHeight = chestActor.getHeight();

                    // Take the lower of the treasure width and height.
                    treasureWidth = Math.min(treasureWidth, treasureHeight);
                    treasureHeight = treasureWidth;
                    
                    // Position treasure actor at center of stage horizontally and chest vertically.
                    treasureActor.setPosition( (viewWidth - treasureWidth) / 2, chestActor.getOriginY() );
                    
                }
                
                else
                {
                    
                    // Chest actor NOT "passed" (null).
                    
                    // Position treasure actor at center of stage horizontally and vertically slightly above 
                    // the bottom.
                    treasureActor.setPosition( (viewWidth - treasureWidth) / 2, 
                      TREASURE_POS_Y * gameHD.getConfig().getScale() );
                    
                }
                    
                
                // Resize treasure actor based on scale.
                treasureActor.setWidth( treasureWidth );
                treasureActor.setHeight( treasureHeight );

                // Set treasure origin to center to allow for proper rotation.
                treasureActor.setOriginCenter();
                
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
    // chestActor = Reference to BaseActor for the chest tile.
    // treasureActor = Reference to BaseActor for the treasure tile.
    // treasureGroup = Reference to BaseActor for the treasure group tile.  Used for groups, like with gold.
    private void render_treasure_label(CustomLabel treasureLabel, boolean treasureImageInd, 
      BaseActor chestActor, BaseActor treasureActor, BaseActor treasureGroup)
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
        pos_y = determine_treasure_label_pos_y(treasureImageInd, chestActor, treasureActor, treasureGroup, 
          treasureLabel);
        
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
    
    public void setBonePileActiveInd(boolean bonePileActiveInd) {
        this.bonePileActiveInd = bonePileActiveInd;
    }
    
    public RegionMap getCurrentRegion() {
        return currentRegion;
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
    
    public HashMap<Integer, Integer> getTileMap() {
        return tileMap;
    }
    
    public Integer getTileMap_Value(HeroineEnum.TileMapKeyEnum tileMapKeyEnum) {
        // The function returns the value from the tile map hash map related to the passed key.
        // The key is limited to one of the values in the enumerated list -- chest, treasure, ...
        return tileMap.get(tileMapKeyEnum.getValue());
    }
    
}