package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
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
    private ArrayList<HeroineEnum.ItemEnum> chestOtherItems; // Additional items in current chest.  Also, contains any items in secondary chests.
    private final RegionMap currentRegion; // Reference to current region / map.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
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
      // for chest and treasure.
    
    // Declare regular variables.
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
        chestOtherItems = new ArrayList<>();
        
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
    
    // chestActor = Reference to BaseActor for the chest tile.
    // treasureActor = Reference to BaseActor for the treasure tile.
    // viewWidth = Width of the stage.
    // treasureLabel = Reference to label with the treasure description.
    // heroineWeapon = BaseActor object that acts as the player weapon.
    // weaponLabel = Label showing current player weapon.
    // heroineArmor = BaseActor object that acts as the player armor.
    // armorLabel = Label showing current player armor.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    private void addEvent_ChestActor(BaseActor chestActor, BaseActor treasureActor, int viewWidth,
      CustomLabel treasureLabel, BaseActor heroineWeapon, CustomLabel weaponLabel, BaseActor heroineArmor, 
      CustomLabel armorLabel, CustomLabel hpLabel, CustomLabel mpLabel)
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
                    HeroineEnum.ImgTreasureEnum imgTreasureEnum; // Enumerated value for treasure.
                    int chestCount; // Number of chests.
                    int index1; // Position of first comma in virtual text.
                    int index2; // Position of second comma in virtual text.
                    int posX; // X-coordinate associated with the tile.
                    int posY; // Y-coordinate associated with the tile.
                    float treasureHeight; // Height to which to resize treasure.
                    boolean treasureImageInd; // Whether treasure image exists for primary item in first chest.
                    String treasureKey; // Key to texture region for treasure in asset manager.
                    float treasureWidth; // Width to which to resize treasure.
                    
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
                    
                    System.out.println("posX: " + posX);
                    System.out.println("posY: " + posY);
                    
                    // 3.  Give chest contents to the player.
                    
                    // Get list of chests at the tile.
                    chestList = gameHD.getAtlasItems().getChestList( map_id, posX, posY );
                    
                    // Reinitialize list of extra chest items.
                    chestOtherItems = new ArrayList<>();
                    
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
                          hpLabel, mpLabel);
                        
                        // If in second or later chest, then...
                        if (chestCount > 0)
                            // In second or later chest.
                            // Add to list of other chest items.
                            chestOtherItems.add(chest.getPrimaryItem());
                        
                        // Give additional items to the player.
                        System.out.println("Addl item count: " + chest.getAddlItemList().size());
                        // Loop through additional items in chest.
                        for (HeroineEnum.ItemEnum itemEnum : chest.getAddlItemList())
                        {
                            
                            // Give the additional item to the player.
                            gameHD.getAvatar().takeItem(itemEnum, heroineWeapon, weaponLabel, 
                              heroineArmor, armorLabel, 1, gameHD.getAssetMgr(), hpLabel, mpLabel);
                            
                            // Add to list of other chest items.
                            chestOtherItems.add(itemEnum);
                            
                        }
                        
                        // Increment chest count.
                        chestCount++;
                        
                    }
                    
                    // 4.  Perform actions on treasure actor and label.
                    
                    // Display the first chest / item.
                    treasureImageInd = render_treasure( chestList.get(0).getPrimaryItem(), chestActor, 
                      treasureActor, viewWidth, treasureLabel );
                    
                    /*
                    // Get treasure enumeration value.
                    imgTreasureEnum = chestList.get(0).getPrimaryItem().getValue_ImgTreasureEnum();
                    
                    // If enumeration value points at an image, then...
                    if (imgTreasureEnum != null)
                    {
                        // Enumeration value points at an image.
                        
                        // Get key for texture region associated with treasure in asset manager.
                        treasureKey = imgTreasureEnum.getValue_AtlasKey();
                        
                        // Update image for treasure actor.
                        treasureActor.setTextureRegion( gameHD.getAssetMgr().getTextureRegion(treasureKey) );

                        // Determine width and height to resize treasure.

                        // Magnify by scale factor.
                        treasureWidth = gameHD.getConfig().getScale() * treasureActor.getWidth();
                        treasureHeight = gameHD.getConfig().getScale() * treasureActor.getHeight();

                        // If treasure width greater than chest, then... > Limit to chest width.
                        if (treasureWidth > chestActor.getWidth())
                            treasureWidth = chestActor.getWidth();

                        // If treasure height greater than chest, then... > Limit to chest height.
                        if (treasureHeight > chestActor.getHeight())
                            treasureHeight = chestActor.getHeight();

                        // Take the lower of the treasure width and height.
                        treasureWidth = Math.min(treasureWidth, treasureHeight);
                        treasureHeight = treasureWidth;

                        // Resize treasure actor based on scale.
                        treasureActor.setWidth( treasureWidth );
                        treasureActor.setHeight( treasureHeight );

                        // Set treasure origin to center to allow for proper rotation.
                        treasureActor.setOriginCenter();

                        // Position treasure actor at center of stage horizontally and chest vertically.
                        treasureActor.setPosition( (viewWidth - treasureWidth) / 2, chestActor.getOriginY() );

                        // Set up an action to fade in the treasure.
                        treasureActor.addAction_FadeIn( 1.00f, 0.50f );

                        // Add event to treasure actor to fade out upon click.
                        addEvent_TreasureActor(treasureActor, treasureLabel);
                        
                    }
                    
                    else
                    {
                        // Enumeration value empty.
                        // Only display label.
                    }
                    
                    // 4.  Perform actions on treasure label.
                    
                    // Update the treasure description in the associated label and center horizontally.
                    treasureLabel.setLabelText_Center( 
                      "FOUND " + chestList.get(0).getPrimaryItem().getValue_TreasureText() +
                      ((chestList.size() > 1 || chestList.get(0).getAddlItemCount() > 0) ? "++" : "") + 
                      "!" );
                    */
                    
                    
                    // If showing treasure, then...
                    if (treasureImageInd)
                    {
                        
                        // Showing treasure.
                        
                        // Set up an action to fade in the treasure.
                        treasureActor.addAction_FadeIn( 1.00f, 0.50f );

                        // Add event to treasure actor to fade out upon click.
                        addEvent_TreasureActor( treasureActor, treasureLabel, chestActor );
                        
                        // Update the vertical position of the label to center between the treasure and 
                        // bottom of the stage.
                        treasureLabel.setPosY( (treasureActor.getY() - treasureLabel.getLabelHeight()) / 2 );
                        
                    }
                        
                    else
                    {
                        
                        // NOT showing treasure.
                        
                        // Update the vertical position of the label to be slightly above the bottom of the stage.
                        treasureLabel.setPosY( gameHD.getConfig().getScale() * 4.125f );
                        
                    }
                    
                    // Remove existing actions on label.
                    treasureLabel.removeActions();
                    
                    // Set up an action to fade in the label.
                    treasureLabel.addAction_FadeIn( 1.00f, 0.50f );
                    
                    
                    
                    // 5.  Remove chests from array list and hash map in atlas items.
                    
                    // Remove chests from array list and hash map associated with current location.
                    gameHD.getAtlasItems().removeChests(chestList, map_id, posX, posY);
                    
                    // 6.  Transform tile associated with chest.
                    
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
                    
                    // Apply a dark shade to the chest.
                    chestActor.setColor(Color.LIGHT_GRAY);
                    
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
                        
                        // Process exit event.
                        
                        // If fade not occurring, then...
                        if (chestActor.getActionMapCount() == 0)
                            
                            // Fade not occurring yet.
                            
                            // Return chest to normal color.
                            chestActor.setColor(Color.WHITE);
                        
                }
                
            }; // End ... InputListener.
        
        // Add event to tile actor.
        chestActor.addListener(tileEvent);
        
    }
    
    // treasureActor = Reference to actor acting as the treasure.
    // treasureLabel = Reference to label with the treasure description.
    // chestActor = Reference to BaseActor for the chest tile.
    private void addEvent_TreasureActor(BaseActor treasureActor, CustomLabel treasureLabel,
      BaseActor chestActor)
    {
        
        // The function adds events to the passed treasure actor and label.
        // Clicking the tresaure actor causes the actor and label to fade out.
        // Events include touchDown, touchUp, enter, and exit.
        
        InputListener labelEvent; // Events to add to passed label.
        
        // Craft event logic to add to passed label.
        labelEvent = new InputListener()
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
                    String otherItemsText; // Text to show when more items exist to take -> ++.
                    String treasureText; // Text to display for treasure.
                    
                    // Flag to ignore next exit event.
                    ignoreNextExitEvent = true;
                    
                    // Play click sound.
                    gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_CLICK);
                    
                    // If additional items remain to display, then...
                    if (chestOtherItemIndex < chestOtherItems.size())
                    {
                        
                        // Additional items remain to display.
                        
                        // Get treasure enumeration value.
                        imgTreasureEnum = chestOtherItems.get(chestOtherItemIndex).getValue_ImgTreasureEnum();
                        
                        // If enumeration value points at an image, then...
                        if (imgTreasureEnum != null)
                        {
                            // Enumeration value points at an image.
                            
                            // Determine width and height to resize treasure.
            
                            // Magnify by scale factor.
                            treasureWidth = gameHD.getConfig().getScale() * 
                              gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()).getRegionWidth();
                            treasureHeight = gameHD.getConfig().getScale() * 
                              gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()).getRegionHeight();
                            
                            // If treasure width greater than chest, then... > Limit to chest width.
                            if (treasureWidth > chestActor.getWidth())
                                treasureWidth = chestActor.getWidth();

                            // If treasure height greater than chest, then... > Limit to chest height.
                            if (treasureHeight > chestActor.getHeight())
                                treasureHeight = chestActor.getHeight();

                            // Take the lower of the treasure width and height.
                            treasureWidth = Math.min(treasureWidth, treasureHeight);
                            treasureHeight = treasureWidth;
                            
                            // Fade in the next treasure actor / image.
                            treasureActor.addAction_FadeIn( 0.50f, 0.50f, 
                              gameHD.getAssetMgr().getTextureRegion(imgTreasureEnum.getValue_AtlasKey()), 
                              imgTreasureEnum.toString(), treasureWidth, treasureHeight );
                            
                            // Update the treasure description in the associated label and center horizontally.
                            
                            // Remove existing treasure label actions.
                            treasureLabel.removeActions();
                            
                            // Populate other items text.
                            otherItemsText = (chestOtherItemIndex + 1) < chestOtherItems.size() ? "++" : "";
                            
                            // Store text to display for treasure.
                            treasureText = "FOUND " + 
                              chestOtherItems.get(chestOtherItemIndex).getValue_TreasureText() + 
                              otherItemsText + "!";
                            
                            // Fade in the next treasure label text.
                            treasureLabel.addAction_FadeIn_Center( 0.50f, 0.50f, treasureText );
                            
                            // Move to the next item.
                            chestOtherItemIndex++;
                            
                        }
                        
                    }
                    
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
        
        // Add event to label.
        treasureActor.addListener(labelEvent);
        
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
    public ArrayList<BaseActor> mazemap_render(int x, int y, HeroineEnum.FacingEnum facing, int viewWidth,
      CustomLabel treasureLabel, BaseActor heroineWeapon, CustomLabel weaponLabel, BaseActor heroineArmor, 
      CustomLabel armorLabel, CustomLabel hpLabel, CustomLabel mpLabel)
    {
        
        /*
        The function returns the tiles used to render the passed location.
        The function also populates the hash map used as a cross reference between the positions and 
        array indices.
        
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
        boolean chestInd; // Whether chest exists immediately in front of player.
        int counter; // Used to increment through array list of BaseActor objects associated with tiles.
        Integer key; // Key to use when adding value to hash map.
        ArrayList<BaseActor> removeList; // List of actors to remove.
        BaseActor tempChest; // Holder for the additional (chest) actor to add.
        BaseActor tempTreasure; // Holder for the additional (treasure) actor to add.
        ArrayList<BaseActor> tiles; // BaseActor objects associated with tiles.
        String virtualString; // Virtual field (string) for the actor with the chest(s).
        
        // 1.  Set defaults.
        chestInd = false;
        virtualString = "";
        
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
        
        // Key = Position (related to tile image offset).
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
        
        // 5.  Check whether chest exists immediately in front of player.
        
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
                
            }
            
        } // End ... Loop through tiles.
        
        // 6.  If necessary, add actors for chest and treasure.
        
        // If adding actor for chest, then...
        if (chestInd)
        {
            
            // Adding actors for chest (and treasure).
            
            // A.  Set up treasure...
            
            // Create new BaseActor for the treasure, setting most properties when opening the chest.
            tempTreasure = new BaseActor();
            
            // Name treasure actor.
            tempTreasure.setActorName( "Treasure" );
            
            // Set treasure actor to hidden.
            tempTreasure.setVisible( false );
            
            // Add treasure actor to the list.
            tiles.add( tempTreasure );
            
            // Add entry to cross reference hash map.
            tileMap.put( HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_TREASURE.getValue(), counter );
            
            // Incement hash map counter.
            counter++;
            
            // B.  Set up chest...
            
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
            addEvent_ChestActor( tempChest, tempTreasure, viewWidth, treasureLabel, heroineWeapon, 
              weaponLabel, heroineArmor, armorLabel, hpLabel, mpLabel );
            
            // Add chest actor to the list.
            tiles.add( tempChest );
            
            // Add entry to cross reference hash map.
            tileMap.put( HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_CHEST.getValue(), counter );
            
            // Incement hash map counter.
            counter++;
            
        }
        
        // 7.  Return the array list with the base actors for the tiles.
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
                
                // If looking at position immediately in front of player, then...
                if (position == 9)
                {
                    
                    // Looking at position immediately in front of player.
                    
                    // If tile represents a chest in an exterior environment, then...
                    if (tileNbr == HeroineEnum.ImgTileEnum.IMG_TILE_CHEST_EXTERIOR.getValue())
                    {
                        // Tile represents a chest in an exterior environment.
                        
                        // Switch out the chest for the floor.
                        tileNbr = HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_FLOOR.getValue();
                        
                        // Store metadata about the chest.
                        virtualString = "Chest|" + Integer.toString(map_id) + "," + Integer.toString(pos_x) + 
                          "," + Integer.toString(pos_y);
                    }
                    
                    // Otherwise, if tile presents a chest in an interior environment, then...
                    else if (tileNbr == HeroineEnum.ImgTileEnum.IMG_TILE_CHEST_INTERIOR.getValue())
                    {
                        // Tile represents a chest in an interior environment.
                        
                        // Switch out the chest for the ceiling.
                        tileNbr = HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_CEILING.getValue();
                        
                        // Store metadata about the chest.
                        virtualString = "Chest|" + Integer.toString(map_id) + "," + Integer.toString(pos_x) + 
                          "," + Integer.toString(pos_y);
                        
                    }
                    
                }
                
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
          minimapCursorRegions.get(keyCursor), minimapDestX.get(gameHD.getAvatar().getY()).get(gameHD.getAvatar().getX()), 
          minimapDestY.get(gameHD.getAvatar().getY()).get(gameHD.getAvatar().getX()) ) );
        
        // Return the array list with the base actors for the icons.
        return icons;

    }
    
    // itemEnum = Enumerated value for the item to render the associated treasure image.
    // chestActor = Reference to BaseActor for the chest tile.
    // treasureActor = Reference to BaseActor for the treasure tile.
    // viewWidth = Width of the stage.
    // treasureLabel = Reference to label with the treasure description.
    private boolean render_treasure(HeroineEnum.ItemEnum itemEnum, BaseActor chestActor, 
      BaseActor treasureActor, int viewWidth, CustomLabel treasureLabel)
    {
        
        // The function updates the image and placement for the treasure actor based on the passed item.
        // The function also updates and recenters the text, based on the passed item.
        // The function returns whether a treasure image exists for the passed item.
        
        HeroineEnum.ImgTreasureEnum imgTreasureEnum; // Enumerated value for treasure.
        float treasureHeight; // Height to which to resize treasure.
        String treasureKey; // Key to texture region for treasure in asset manager.
        boolean treasureImageInd; // Whether treasure image exists for passed actor.
        float treasureWidth; // Width to which to resize treasure.    
        
        // 1.  Set defaults.
        treasureImageInd = false;
        
        // 2.  If image available, update treasure image and position.
        
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
            
            // If treasure width greater than chest, then... > Limit to chest width.
            if (treasureWidth > chestActor.getWidth())
                treasureWidth = chestActor.getWidth();

            // If treasure height greater than chest, then... > Limit to chest height.
            if (treasureHeight > chestActor.getHeight())
                treasureHeight = chestActor.getHeight();

            // Take the lower of the treasure width and height.
            treasureWidth = Math.min(treasureWidth, treasureHeight);
            treasureHeight = treasureWidth;

            // Resize treasure actor based on scale.
            treasureActor.setWidth( treasureWidth );
            treasureActor.setHeight( treasureHeight );

            // Set treasure origin to center to allow for proper rotation.
            treasureActor.setOriginCenter();

            // Position treasure actor at center of stage horizontally and chest vertically.
            treasureActor.setPosition( (viewWidth - treasureWidth) / 2, chestActor.getOriginY() );

        } // End ... If enumeration value points at an image.

        else
        {
            // Enumeration value empty.
            // Only display label.
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
    
    // Getters and setters below...
    
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
