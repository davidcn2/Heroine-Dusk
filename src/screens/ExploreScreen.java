package screens;

// LibGDX imports.
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

// Local project imports.
import core.BaseActor;
import core.BaseScreen;
import core.CoreEnum;
import gui.CustomLabel;
import heroinedusk.HeroineDuskGame;
import heroinedusk.HeroineEnum;
import heroinedusk.MazeMap;
import heroinedusk.RegionMap;

// Java imports.
import java.util.ArrayList;
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

ArrayList supports dynamic arrays that can grow as needed.
*/

public class ExploreScreen extends BaseScreen { // Extends the BaseScreen class.

    /*
    The class extends the basic functionality of a BaseScreen class and sets up the introduction screen.

    Methods include:

    action_render:  Configures and adds the actors for the action buttons (excluding information).
    addEventBasics:  Adds basic events to the passed action button / BaseActor.
    addEvent_Information:  Adds events to the passed (information) button (BaseActor).
    addEvent_Minimap:  Adds events related to dragging the minimap.
    addEvent_Touch_Burn:  Adds events to the passed button (BaseActor) -- used for the burn (spell).
    addEvent_Touch_Freeze:  Adds events to the passed button (BaseActor) -- used for the freeze (spell).
    addEvent_Touch_Heal:  Adds events to the passed button (BaseActor) -- used for the heal (spell).
    addEvent_Touch_Light:  Adds events to the passed button (BaseActor) -- used for the light (spell).
    addEvent_Touch_Reflect:  Adds events to the passed button (BaseActor) -- used for the reflect (spell).
    addEvent_Touch_Unlock:  Adds events to the passed button (BaseActor) -- used for the unlock (spell).
    dispose:  Called when removing the screen and allows for clearing of related resources from memory.
    info_clear_messages:  Clears the text and actions from the power action and result lines.
    info_render:  Handles displaying the information view or switching back to the standard display.
    info_render_equipment:  Configures and adds the actors for the player, including the weapon and armor.
    info_render_gold:  Configures and adds the label for the current player gold.
    info_render_hpmp:  Configures and adds the labels for the player hit and magic points.
    info_render_itemlist:  Configures and adds the labels for the current player armor and weapon.
    info_render_no_target:  Updates the power action and result labels to display the "(NO TARGET)" message.
    info_render_powerResponseLines:  Configures and adds the labels for the power action and result lines.
    processExit:  Encapsulates logic related to the player moving to an exit.
    processShop:  Encapsulates logic related to the player entering a shop.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    wakeScreen:  Called when redisplaying the already initialized screen.
    */
    
    // Declare object variables.
    private Map<String, Boolean> mapActionButtonEnabled; // Hash map containing
      // enabled status of spell action buttons.
    private Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtonMagic; // Hash map containing
      // BaseActor objects that will act as the spell action buttons.
    private Map<String, Boolean> mapIgnoreNextExitEvent_ActionButtons; // Hash map containing
      // whether to ignore next exit event (used with touchUp / exit) -- for action buttons.
    private CustomLabel armorLabel; // Label showing current player armor.
    private BaseActor background; // BaseActor object that will act as the background.
    private CustomLabel facingLabel; // Label showing direction player is facing.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private CustomLabel goldLabel; // Label showing current player gold.
    private BaseActor heroineArmor; // BaseActor object that will act as the player armor.
    private BaseActor heroineBase; // BaseActor object that will act as the base player.
    private BaseActor heroineWeapon; // BaseActor object that will act as the player weapon.
    private CustomLabel hpLabel; // Label showing player hit points.
    private BaseActor infoButton; // BaseActor object that will act as the information button.
    private BaseActor infoButtonSelector; // BaseActor object that will act as the selector for the
      // information button.
    private CustomLabel infoLabel; // Label showing "INFO" text.
    private MazeMap mazemap; // Stores data for the current active region / map.
    private Group minimapGroup; // Group containing minimap actors.
    private ArrayList<BaseActor> minimapIcons; // BaseActor objects associated with minimap.
    private CustomLabel mpLabel; // Label showing player magic points.
    private CustomLabel powerActionLabel; // Label showing the first line -- power action.
    private CustomLabel powerResultLabel; // Label showing the second line -- power result.
    private CustomLabel regionLabel; // Label showing the current region name.
    private CustomLabel spellsLabel; // Label showing "SPELLS" text.
    private ArrayList<BaseActor> tiles; // BaseActor objects associated with tiles.
    private CustomLabel treasureLabel; // Label showing treasure description.
    private Array<Actor> uiStageActors; // List of actors in ui stage used when waking screen.
    private CustomLabel weaponLabel; // Label showing current player weapon.
    
    // Declare regular variables.
    private HeroineEnum.SelectPosEnum buttonSelected; // Selected button.
    private boolean infoButtonSelected; // Whether information button selected and clicked.
    private int gold_value; // Amount of gold found by player in current location.
    private float minimapOffsetX; // X-coordinate of the point where the player first touched the minimap.
    private float minimapOffsetY; // Y-coordinate of the point where the player first touched the minimap.
    private float minimapOriginalX; // Original position (x-coordinate) of the minimap on the stage before drag operation.
    private float minimapOriginalY; // Original position (y-coordinate) of the minimap on the stage before drag operation.
    private boolean minimapRenderInd; // Whether minimap rendered for current location yet.
    private HeroineEnum.ItemEnum treasure_id; // Treasure (item) found by player in current location.
    
    // Declare constants.
    private final Color colorMedGray = new Color(0.50f, 0.50f, 0.50f, 1);
    
    // hdg = Reference to Heroine Dusk (main) game.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    public ExploreScreen(HeroineDuskGame hdg, int windowWidth, int windowHeight)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Stores reference to main game class.
        // 3.  Calls the create() function to perform remaining startup logic.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(hdg, windowWidth, windowHeight);
        
        // Store reference to main game class.
        gameHD = hdg;
        
        // Perform additional logic related to startup / create phase, including configuration and addition
        // of actors to stage.
        create();
        
    }
    
    public final void create()
    {
        
        /* 
        The function occurs during the startup / create phase and accomplishes the following:
        
        1.  Set defaults.
        2.  Initialize arrays, array lists, and hash maps.
        3.  Initialize the maze map.
        4.  Configure and add the background Actor.
        5.  Render current map location / tiles.
        6.  Configure and add the label with the direction the player is facing.
        7.  Configure and add the information button Actor.
        8.  Configure and add the label with the "INFO" text.  Hidden at start.
        9.  Configure and add the selector Actor for the information button.  Hidden at start.
        10.  Configure and add the label with the "SPELLS" text.  Hidden at start.
        11.  Configure and add the actors for the base player, weapon, and armor.  Hidden at start.
        12.  Configure and add the labels for the current player armor and weapon.  Hidden at start.
        13.  Configure and add the labels for the player hit and magic points.  Hidden at start.
        14.  Configure and add the label for the current player gold.  Hidden at start.
        15.  Populate hash map with starting enabled statuses for action buttons.
        16.  Populate hash map with starting ignore next exit event flags for action buttons.
        17.  Configure and add the actors for the action buttons (excluding information).  Hidden at start.
        18.  Configure and add the labels for the two lines for responses to spells / powers / actions.
             Hidden at start.
        19.  Configure and add the label showing the current region name.  Hidden at start.
        20.  Configure and add the label showing the treasure description.  Hidden at start.
        */
        
        // 1.  Set defaults.
        infoButtonSelected = false;
        minimapRenderInd = false;
        buttonSelected = HeroineEnum.SelectPosEnum.BUTTON_POS_INFO;
        
        // 2.  Initialize arrays, array lists, and hash maps.
        uiStageActors = new Array<>();
        tiles = new ArrayList<>();
        minimapIcons = new ArrayList<>();
        mapActionButtonMagic = new HashMap<>();
        mapActionButtonEnabled = new HashMap<>();
        mapIgnoreNextExitEvent_ActionButtons = new HashMap<>();
        
        // 3.  Initialize the maze map.
        mazemap = new MazeMap(gameHD, gameHD.getAvatar().getMap_id(), viewHeightMain);
        
        // 4.  Configure and add the background Actor.
        // Note:  The application uses a starting background of tempest, which the foreground objects completely hide.
        info_render_background();
        
        // 5.  Render current map location / tiles.
        
        // Store array list with base actors for tiles to display.
        tiles = mazemap.mazemap_render(gameHD.getAvatar().getX(), gameHD.getAvatar().getY(), 
          gameHD.getAvatar().getFacing(), viewWidthMain, treasureLabel, heroineWeapon, weaponLabel,
          heroineArmor, armorLabel, hpLabel, mpLabel, goldLabel, regionLabel);
        
        // Loop through base actors in array list.
        tiles.forEach((actor) -> {
            
            // Add the tile Actor to the scene graph.
            mainStage.addActor( actor );
        
        });
        
        // 6.  Configure and add the label with the direction the player is facing.
        
        // Initialize and add label with facing text.
        facingLabel = new CustomLabel(game.skin, gameHD.getAvatar().getFacing().toString(), "uiLabelStyle", 
          1.0f, gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage, null, 
          (float)(gameHD.getConfig().getScale() * -2), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( facingLabel.getLabel() );
        
        // 7.  Configure and add the information button Actor.
        
        // Create and configure new BaseActor for the information button.
        
        // Pos X = (140 + 2) * scale factor.  2 = Offset.
        // Pos Y = (0 - 2) * scale factor.  -2 = Offset.  -- Relative to top of screen.
        infoButton = new BaseActor( "info button", HeroineEnum.InfoButtonEnum.INFO_BUTTON.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage.getWidth(), 
          uiStage.getHeight(), (140f + 2f) * gameHD.getConfig().getScale(), 
          -2f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // Add events to information button.
        addEvent_Information( infoButton );
        
        // Add the information button Actor to the scene graph.
        uiStage.addActor( infoButton );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( infoButton );
        
        // 8.  Configure and add the label with the "INFO" text.
        
        // Initialize and add label with the "INFO" text.
        infoLabel = new CustomLabel(game.skin, "INFO", "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage, null, 
          (float)(gameHD.getConfig().getScale() * -2), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        infoLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( infoLabel.getLabel() );
        
        // Configure and add the selector Actor for the selector for the information button.
        
        // Create and configure new BaseActor for the selector for the information button.
        
        // Pos X = 140 * scale factor.  0 = Offset.
        // Pos Y = 0 * scale factor.  -2 = Offset.  -- Relative to top of screen.
        infoButtonSelector = new BaseActor( "info button selector", 
          HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_SELECT.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage.getWidth(), 
          uiStage.getHeight(), 140f * gameHD.getConfig().getScale(), 
          0f, CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE );
        
        // Add events to information button selector.
        addEvent_Information( infoButtonSelector );
        
        // Set information button selector as invisible.
        infoButtonSelector.setVisible(false);
        
        // Add the information button selector Actor to the scene graph.
        uiStage.addActor( infoButtonSelector );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( infoButtonSelector );
        
        // 10.  Configure and add the label with the "SPELLS" text.  Hidden at start.
        
        // Initialize and add label with the "SPELLS" text.
        spellsLabel = new CustomLabel(game.skin, "SPELLS", "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_RIGHT, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_RIGHT, uiStage, (float)(gameHD.getConfig().getScale() * -2), 
          (float)(gameHD.getConfig().getScale() * -30), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        spellsLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( spellsLabel.getLabel() );
        
        // 11.  Configure and add the actors for the base player, weapon, and armor.  Hidden at start.
        info_render_equipment();
        
        // 12.  Configure and adds the labels for the player armor and weapon.  Hidden at start.
        info_render_itemlist();
        
        // 13.  Configure and add the labels for the player hit and magic points.  Hidden at start.
        info_render_hpmp();
        
        // 14.  Configure and add the label for the current player gold.  Hidden at start.
        info_render_gold();
    
        // 15.  Populate hash map with starting enabled statuses for action buttons.
        // 16.  Populate hash map with starting ignore next exit event flags for action buttons.
        
        // Loop through action button enumerations.
        for (HeroineEnum.ActionButtonEnum actionButtonEnum : HeroineEnum.ActionButtonEnum.values())
        {
            
            // Add default value of not enabled for current enumeration to hash map.
            mapActionButtonEnabled.put(actionButtonEnum.toString(), false);
            
            // Add default value of do not exit for current enumation to hash map.
            mapIgnoreNextExitEvent_ActionButtons.put(actionButtonEnum.toString(), false);
            
        }
        
        // If necessary, override status of heal action button.
        
        // If player NOT at maximum hit points, then...
        if (!gameHD.getAvatar().getHp_AtMax())
        {
            
            // Player NOT at maximum hit points.
            
            // Enable heal button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL.toString(), true);
            
        }
        
        // 17.  Configure and add the actors for the action buttons (excluding information).
        action_render();
        
        // 18.  Configure and add the labels for the two lines for responses to spells / powers / actions.
        //      Hidden at start.
        info_render_powerResponseLines();
        
        // 19.  Configure and add the label showing the current region name.  Hidden at start.
        
        // Initialize and add label with the "SPELLS" text.
        regionLabel = new CustomLabel(game.skin, mazemap.getCurrentRegion().getRegionName(), "uiLabelStyle", 
          1.0f, gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage, 0f, 
          (float)(gameHD.getConfig().getScale() * 12), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        regionLabel.applyVisible(false);
        
        // 20.  Configure and add the label showing the treasure description.  Hidden at start.
        treasureLabel = new CustomLabel(game.skin, "NO TREASURE HERE", "uiLabelStyle", 
          0.8f, gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, uiStage, 0f, 
          (float)(gameHD.getConfig().getScale() * 12), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        treasureLabel.applyVisible(false);
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    private void addEventBasics(BaseActor buttonActor)
    {
        
        // The function adds basic events to the passed action button / BaseActor.
        // Events include touchDown, enter, and exit.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
            {
                
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
                    
                    // If action button enabled, then...
                    if (mapActionButtonEnabled.get(buttonActor.getActorName()))
                    {
                        
                        // Action button enabled.
                        
                        // Apply a dark shade to the button.
                        buttonActor.setColor(Color.LIGHT_GRAY);
                        
                    }
                    
                    else
                    {
                        
                        // Action button disabled.
                        
                        // Apply a dark shade to the button.
                        buttonActor.setColor(Color.DARK_GRAY);
                        
                    }
                    
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
                    if (mapIgnoreNextExitEvent_ActionButtons.get(buttonActor.getActorName()))
                        
                        // Ignoring next exit event.
                        
                        // Flag to process next exit event.
                        mapIgnoreNextExitEvent_ActionButtons.put(buttonActor.getActorName(), false);
                    
                    // Otherwise, ...
                    else
                        
                        // Process exit event.
                        
                        // If action button enabled, then...
                        if (mapActionButtonEnabled.get(buttonActor.getActorName()))
                        {

                            // Action button enabled.
                            
                            // Return button to normal color.
                            buttonActor.setColor(Color.WHITE);
                        
                        }
                    
                        else
                        {
                            
                            // Action button disabled.
                            
                            // Return button to disabled color.
                            buttonActor.setColor(colorMedGray);
                            
                        }
                    
                }
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    private void addEvent_Information(BaseActor buttonActor)
    {
        
        // The function adds events to the passed (information) button (BaseActor).
        // Events include touchDown, touchUp, enter, and exit.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
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
                    
                    // Flag to ignore next exit event.
                    ignoreNextExitEvent = true;
                    
                    // Toggle information button.
                    infoButtonSelected = !infoButtonSelected;     
                    
                    // If selected, then...
                    if (infoButtonSelected)
                    {
                        
                        // Information button selected.
                    
                        // Switch to information state.
                        gameHD.setGameState(HeroineEnum.GameState.STATE_INFO);
                        
                        // Draw in selected state.
                        infoButton.setTextureRegion(gameHD.getAssetMgr().getTextureRegion(HeroineEnum.InfoButtonEnum.INFO_BUTTON_SEL.getValue_Key()));
                        
                    }
                    
                    else
                    {
                    
                        // Information button deselected.
                        
                        // Switch to explore state.
                        gameHD.setGameState(HeroineEnum.GameState.STATE_EXPLORE);
                        
                        // Draw in unselected state.
                        infoButton.setTextureRegion(gameHD.getAssetMgr().getTextureRegion(HeroineEnum.InfoButtonEnum.INFO_BUTTON.getValue_Key()));
                        
                    }
                    
                    // Play click sound.
                    gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_CLICK);
                    
                    // Update screen to either display information or standard view.
                    info_render();
                    
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
                    
                    // Apply a dark shade to the information button.
                    infoButton.setColor(Color.LIGHT_GRAY);
                    
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
                        
                        // Return information button to normal color.
                        infoButton.setColor(Color.WHITE);
                        
                }
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    private void addEvent_Minimap()
    {
        
        // The function adds events related to dragging the minimap.
        // Events include touchDown, touchUp, and touchDragged.
        
        InputListener minimapEvent; // Events to add to minimap group.
        
        // Craft event logic to add to minimap group.
        minimapEvent = new InputListener()
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
                    
                    // The function stores the related movement data: the position on the minimap that
                    // was touched, as well as the original location of the minimap on the Stage.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS. 
                    // Notes:  Occurs when user press down mouse on button.
                    // Notes:  Event (touchDown) necessary to reach touchUp.
                    
                    minimapOffsetX = x; // Store x-coordinate of the point where the player first touched the minimap.
                    minimapOffsetY = y; // Store y-coordinate of the point where the player first touched the minimap.
                    minimapOriginalX = event.getStageX(); // Store original (x) location of the minimap on the Stage.
                    minimapOriginalY = event.getStageY(); // Store original (y) location of the minimap on the Stage.
                    
                    // Return a value.
                    return true;
                    
                }
                
                // event = Type of input event.
                // x = The x coordinate of the mouse click -- origin is in the upper left corner.
                // y = The y coordinate of the mouse click -- origin is in the upper left corner.
                // pointer = Index of the pointer used in the drag operation.
                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) 
                {

                    /*
                    The event occurs when a finger is being dragged over the screen or the mouse is dragged
                    while a button is pressed.
                    When the player drags the minimap (handled by the touchDragged method), move to a new 
                    position.  However, instead of moving the lower-left corner of the minimap
                    to the touch position, use the position on the minimap that was initially touched 
                    (stored in offsetX and offsetY).  Therefore, take the offsetX and offsetY values
                    into account when using the moveBy method of the minimap group.
                    */
                    
                    // Move the minimap by adding / subtracting from current position.
                    minimapGroup.moveBy(x - minimapOffsetX, y - minimapOffsetY);
                    
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
                    // The function move the minimap to the left of the spells label, when necessary.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
                    // Notes:  Occurs when user releases mouse on button.
                    
                    // If right portion of minimap past "SPELLS" label, then...
                    if ( minimapGroup.getX() + mazemap.getMinimapWidth() > spellsLabel.getLabel().getX() )
                        // Right portion of minimap past "SPELLS" label.
                        // Adjust minimap position so that right edge aligns with left of spells label.
                        minimapGroup.setX(spellsLabel.getLabel().getX() - mazemap.getMinimapWidth());
                    
                }
                
            }; // End ... InputListener.
        
        // Add event to minimap group.
        minimapGroup.addListener(minimapEvent);
        
    }
            
    // buttonActor = Reference to BaseActor for the button.
    private void addEvent_Touch_Burn(BaseActor buttonActor)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Provides functionality for the burn (spell) button.
        // Events include touchDown and touchUp.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
            {
                
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
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(buttonActor.getActorName(), true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    // Hide the minimap.
                    minimapGroup.setVisible(false);
                    
                    // If player in combat, then...
                    if (gameHD.getGameState() == HeroineEnum.GameState.STATE_COMBAT)    
                    {
                        
                        // Player in combat.
                        
                        System.out.println("Burn!");
                        
                        // Play fire sound.
                        gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_FIRE);
                        
                    }
                    
                    else
                    {
                        
                        // Player NOT in combat.
                        
                        // Render "(NO TARGET)" message.
                        info_render_no_target();
                        
                    }
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    private void addEvent_Touch_Freeze(BaseActor buttonActor)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Provides functionality for the freeze (spell) button.
        // Events include touchDown and touchUp.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
            {
                
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
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(buttonActor.getActorName(), true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    // Hide the minimap.
                    minimapGroup.setVisible(false);
                    
                    // If player in combat, then...
                    if (gameHD.getGameState() == HeroineEnum.GameState.STATE_COMBAT)    
                    {
                        
                        // Player in combat.
                        
                        System.out.println("Freeze!");
                    
                        // Play freeze sound.
                        gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_FREEZE);
                        
                    }
                    
                    else
                    {
                        
                        // Player NOT in combat.
                        
                        // Render "(NO TARGET)" message.
                        info_render_no_target();
                        
                    }
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    private void addEvent_Touch_Heal(BaseActor buttonActor)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Provides functionality for the heal (spell) button.
        // Events include touchDown and touchUp.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
            {
                
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
                    
                    int heal_amount; // Number of hit points to heal.
                    String hpText; // Text to display for hit points.
                    String mpText; // Text to display for magic points.
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(buttonActor.getActorName(), true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    // Hide the minimap.
                    minimapGroup.setVisible(false);
                    
                    // If player at maximum hit points, then...
                    if (gameHD.getAvatar().getHp_AtMax())    
                    {
                        
                        // Player at maximum hit points.
                        
                        // Update power action labels.
                        powerActionLabel.setLabelText("HP AT MAX!");
                        
                        // Display power action  labels.
                        powerActionLabel.applyVisible(true);
                        
                        // Set up fade effect for power action label.
                        powerActionLabel.addAction_Fade();
                        
                        // Play error sound.
                        gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_ERROR);
                        
                    }
                    
                    // Otherwise, if player lacks sufficient magic points, then...
                    else if (gameHD.getAvatar().getMp() == 0)
                    {
                        
                        // Player lacks sufficient magic points.
                        
                        // Update power action labels.
                        powerActionLabel.setLabelText("INSUFFICIENT MP!");
                        
                        // Display power action  labels.
                        powerActionLabel.applyVisible(true);
                        
                        // Set up fade effect for power action label.
                        powerActionLabel.addAction_Fade();
                        
                        // Play error sound.
                        gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_ERROR);
                        
                    }  
                        
                    else
                    {
                        
                        // Player below maximum hit points.
                        
                        // Play heal sound.
                        gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_HEAL);
                        
                        // Calculate number of hit points to heal.
                        heal_amount = (int)Math.floor(gameHD.getAvatar().getMax_hp() / 2) +
                          (int)Math.floor(Math.random() * gameHD.getAvatar().getMax_hp() / 2);
                        heal_amount = Math.min(heal_amount, gameHD.getAvatar().getMax_hp() - 
                          gameHD.getAvatar().getHp());
                        
                        // Reduce magic points by one.
                        gameHD.getAvatar().decrement_mp();
                        
                        // Increase player hit points by calculated amount.
                        gameHD.getAvatar().setHp(gameHD.getAvatar().getHp() + heal_amount);
                        
                        // Store text to display for hit points.
                        hpText = "HP " + Integer.toString(gameHD.getAvatar().getHp()) + "/" + 
                          Integer.toString(gameHD.getAvatar().getMax_hp());
                        
                        // Update hit points label.
                        hpLabel.setLabelText(hpText);
                        
                        // Store text to display for magic points.
                        mpText = "MP " + Integer.toString(gameHD.getAvatar().getMp()) + "/" +
                          Integer.toString(gameHD.getAvatar().getMax_mp());
                        
                        // Update magic points label.
                        mpLabel.setLabelText(mpText);
                        
                        // If player now at maximum hit points or no magic points, then...
                        if (gameHD.getAvatar().getHp_AtMax() || gameHD.getAvatar().getMp() == 0)
                        {
                            
                            // Player now at maximum hit points or no magic points.
                            
                            // Apply a dark shade to the button to signify not currently enabled.
                            buttonActor.setColor( Color.DARK_GRAY );
                            
                            // Disable heal button.
                            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL.toString(), 
                              false);
                            
                        }
                        
                        System.out.println("Heal - " + heal_amount + " hp!");
                        
                        // Update power action and result labels.
                        powerActionLabel.setLabelText("HEAL!");
                        powerResultLabel.setLabelText("+" + Integer.toString(heal_amount) + " HP");
                        
                        // Display power action and result labels.
                        powerActionLabel.applyVisible(true);
                        powerResultLabel.applyVisible(true);
                        
                        // Set up fade effects for power action and result labels.
                        powerActionLabel.addAction_Fade();
                        powerResultLabel.addAction_Fade();
                        
                    } // End ... If player at maximum hit points.
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    private void addEvent_Touch_Reflect(BaseActor buttonActor)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Provides functionality for the reflect (spell) button.
        // Events include touchDown and touchUp.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
            {
                
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
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(buttonActor.getActorName(), true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    // Hide the minimap.
                    minimapGroup.setVisible(false);
                    
                    // If player in combat, then...
                    if (gameHD.getGameState() == HeroineEnum.GameState.STATE_COMBAT)    
                    {
                        
                        // Player in combat.
                        
                        System.out.println("Reflect!");
                    
                        // Play reflect sound.
                        gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_REFLECT);
                        
                    }
                    
                    else
                    {
                        
                        // Player NOT in combat.
                        
                        // Render "(NO TARGET)" message.
                        info_render_no_target();
                        
                    }
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    private void addEvent_Touch_Unlock(BaseActor buttonActor)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Provides functionality for the unlock (spell) button.
        // Events include touchDown and touchUp.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
            {
                
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
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(buttonActor.getActorName(), true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    // Hide the minimap.
                    minimapGroup.setVisible(false);
                    
                    System.out.println("Unlock!");
                    
                    // Play unlock sound.
                    gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_UNLOCK);
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    private void addEvent_Touch_Light(BaseActor buttonActor)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Provides functionality for the light (spell) button.
        // Events include touchDown and touchUp.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
            {
                
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
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(buttonActor.getActorName(), true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    // Hide the minimap.
                    minimapGroup.setVisible(false);
                    
                    System.out.println("Light!");
                    
                    // Play light sound.
                    gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_LIGHT);
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    private void action_render()
    {
        
        // The function configures and adds the actors for the action buttons (excluding information).
        // The function only displays available actions.
        // Note:  Actor hidden at start.
        
        BaseActor temp; // Used as placeholder for actor for a spell action button, for adding to hash map.
        
        // 1.  Configure and add the heal (spell) button Actor.
        
        // Create and configure new BaseActor for the heal (spell) button.
        
        // Pos X = -38 * scale factor.  -- Relative to right of screen.
        // Pos Y = +62 * scale factor.  -- Relative to bottom of screen.
        temp = new BaseActor( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL.toString(), 
          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
          uiStage.getHeight(), -38f * gameHD.getConfig().getScale(), 
          62f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // If player at maximum hit points or has no magic left, then...
        if (gameHD.getAvatar().getHp_AtMax() || gameHD.getAvatar().getMp() == 0)
        {
            
            // Player at maximum hit points.
            
            // Apply a dark shade to the button to signify not currently enabled.
            temp.setColor( colorMedGray );
            
        }
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Heal( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the heal (spell) button to the hash map.
        mapActionButtonMagic.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL, temp );
        
        // Add the heal (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL) );
        
        // 2.  Configure and add the burn (spell) button Actor.
        
        // Create and configure new BaseActor for the burn (spell) button.
        
        // Pos X = -18 * scale factor.  -- Relative to right of screen.
        // Pos Y = +62 * scale factor.  -- Relative to bottom of screen.
        temp = new BaseActor( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN.toString(), 
          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
          uiStage.getHeight(), -18f * gameHD.getConfig().getScale(), 
          62f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // Apply a dark shade to the button to signify not currently enabled.
        temp.setColor( colorMedGray );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Burn( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the burn (spell) button to the hash map.
        mapActionButtonMagic.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN, temp );
        
        // Add the burn (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN) );
        
        // 3.  Configure and add the unlock (spell) button Actor.
        
        // Create and configure new BaseActor for the unlock (spell) button.
        
        // Pos X = -38 * scale factor.  -- Relative to right of screen.
        // Pos Y = +42 * scale factor.  -- Relative to bottom of screen.
        temp = new BaseActor( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK.toString(), 
          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
          uiStage.getHeight(), -38f * gameHD.getConfig().getScale(), 
          42f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // Apply a dark shade to the button to signify not currently enabled.
        temp.setColor( colorMedGray );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Unlock( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the unlock (spell) button to the hash map.
        mapActionButtonMagic.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, temp );
        
        // Add the unlock (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK) );
        
        // 4.  Configure and add the light (spell) button Actor.
        
        // Create and configure new BaseActor for the light (spell) button.
        
        // Pos X = -18 * scale factor.  -- Relative to right of screen.
        // Pos Y = +42 * scale factor.  -- Relative to bottom of screen.
        temp = new BaseActor( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT.toString(), 
          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
          uiStage.getHeight(), -18f * gameHD.getConfig().getScale(), 
          42f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // Apply a dark shade to the button to signify not currently enabled.
        temp.setColor( colorMedGray );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Light( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the light (spell) button to the hash map.
        mapActionButtonMagic.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT, temp );
        
        // Add the light (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT) );
        
        // 5.  Configure and add the freeze (spell) button Actor.
        
        // Create and configure new BaseActor for the freeze (spell) button.
        
        // Pos X = -38 * scale factor.  -- Relative to right of screen.
        // Pos Y = +22 * scale factor.  -- Relative to bottom of screen.
        temp = new BaseActor( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE.toString(), 
          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
          uiStage.getHeight(), -38f * gameHD.getConfig().getScale(), 
          22f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // Apply a dark shade to the button to signify not currently enabled.
        temp.setColor( colorMedGray );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Freeze( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the freeze (spell) button to the hash map.
        mapActionButtonMagic.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE, temp );
        
        // Add the freeze (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE) );
        
        // 6.  Configure and add the reflect (spell) button Actor.
        
        // Create and configure new BaseActor for the reflect (spell) button.
        
        // Pos X = -18 * scale factor.  -- Relative to right of screen.
        // Pos Y = +22 * scale factor.  -- Relative to bottom of screen.
        temp = new BaseActor( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT.toString(), 
          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
          uiStage.getHeight(), -18f * gameHD.getConfig().getScale(), 
          22f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // Apply a dark shade to the button to signify not currently enabled.
        temp.setColor( colorMedGray );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Reflect( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the reflect (spell) button to the hash map.
        mapActionButtonMagic.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT, temp );
        
        // Add the reflect (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT) );
        
        /*
        // if in combat, show fight and run
        if (gamestate == STATE_COMBAT) {
          action_render_button(0, BUTTON_POS_ATTACK);
          action_render_button(1, BUTTON_POS_RUN);
        }


        // show spells
        if (avatar.spellbook >= 1) action_render_button(2, BUTTON_POS_HEAL);
        if (avatar.spellbook >= 2) action_render_button(3, BUTTON_POS_BURN);
        if (avatar.spellbook >= 3) action_render_button(4, BUTTON_POS_UNLOCK);
        if (avatar.spellbook >= 4) action_render_button(5, BUTTON_POS_LIGHT);
        if (avatar.spellbook >= 5) action_render_button(6, BUTTON_POS_FREEZE);
        if (avatar.spellbook >= 6) action_render_button(7, BUTTON_POS_REFLECT);

        action_render_select(action.select_pos);
        */
        
    }
    
    @Override
    public void dispose()
    {
        
        // The method is called when removing the screen and allows for clearing of related resources 
        // from memory.
        
        // Call manual dispose method in superclass.
        super.disposeManual();
        
    }
    
    private void info_clear_messages()
    {
        
        // The function clears the text and actions from the power action and result lines.
        
        // Clear text from the power action and result labels.
        powerActionLabel.setLabelText("");
        powerResultLabel.setLabelText("");
        
        // Remove any existing actions on the power action and result labels.
        powerActionLabel.removeActions();
        powerResultLabel.removeActions();
        
    }
    
    private void info_render()
    {
        
        // The function handles displaying the information view or switching back to the standard display.
        
        // Clear text from and hide power action and result labels.
        info_clear_messages();
        
        // If switching to information view, then...
        if (infoButtonSelected)
        {
            
            // Switch to information view.
            
            // Hide the direction facing label.
            facingLabel.applyVisible(false);
            
            // Display the "INFO" text label.
            infoLabel.applyVisible(true);
            
            // Set information button selector as visible.
            infoButtonSelector.setVisible(true);
            
            // Display the "SPELLS" text label.
            spellsLabel.applyVisible(true);
            
            // Display the player base.
            heroineBase.setVisible(true);
            
            // Display the player armor.
            heroineArmor.setVisible(true);
            
            // Display the player weapon.
            heroineWeapon.setVisible(true);
            
            // Display the player armor label.
            armorLabel.applyVisible(true);
            
            // Display the player weapon label.
            weaponLabel.applyVisible(true);
            
            // Display the player hit points label.
            hpLabel.applyVisible(true);
            
            // Display the player magic points label.
            mpLabel.applyVisible(true);
            
            // Display the player gold label.
            goldLabel.applyVisible(true);
            
            // Display the (available) action buttons.
            
            // Loop through player spell(s).
            gameHD.getAvatar().getSpellList().forEach((spellEnum) -> {
                
                // Display current player spell in loop.
                mapActionButtonMagic.get(spellEnum.getValue_ActionButtonEnum()).setVisible(true);
            });
            
            /*
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL).setVisible(true);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setVisible(true);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setVisible(true);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT).setVisible(true);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE).setVisible(true);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT).setVisible(true);
            */
            
            // If minimap already rendered, then...
            if (minimapRenderInd)
            {
                
                // Minimap already rendered.
                
                // Display minimap group.
                minimapGroup.setVisible(true);
                
            }
            
            else
            {
            
                // Minimap NOT rendered yet.
                
                // Render minimap.
                info_render_minimap();
                
                // Flag minimap as rendered.
                minimapRenderInd = true;
                
            }
            
            // Hide any visible treasure.
            
            // If treasure actor exists, then...
            if (mazemap.getTileMap_Value(HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_TREASURE) != null)    
            {
                // Treasure actor exists.
                
                // Hide treasure actor.
                tiles.get(mazemap.getTileMap_Value(HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_TREASURE)).setVisible(false);
                
                // Hide treasure label.
                treasureLabel.applyVisible(false);
            }
            
            // If treasure actor (group) exists, then...
            if (mazemap.getTileMap_Value(HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_TREASURE_GROUP) != null)    
            {
                // Treasure actor (group) exists.
                
                // Hide treasure actor (group).
                tiles.get(mazemap.getTileMap_Value(HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_TREASURE_GROUP)).setVisible(false);
            }
                
        } // End ... If information view selected.
        
        // Otherwise -- switching from information to regular view.
        else
        {
            
            // Switch to regular view.
            
            // Display the direction facing label.
            facingLabel.applyVisible(true);
            
            // Hide the "INFO" text label.
            infoLabel.applyVisible(false);
            
            // Set information button selector as invisible.
            infoButtonSelector.setVisible(false);
            
            // Hide the "SPELLS" text label.
            spellsLabel.applyVisible(false);
            
            // Hide the player base.
            heroineBase.setVisible(false);
            
            // Hide the player armor.
            heroineArmor.setVisible(false);
            
            // Hide the player weapon.
            heroineWeapon.setVisible(false);
            
            // Hide the player armor label.
            armorLabel.applyVisible(false);
            
            // Hide the player weapon label.
            weaponLabel.applyVisible(false);
            
            // Hide the player hit points label.
            hpLabel.applyVisible(false);
            
            // Hide the player magic points label.
            mpLabel.applyVisible(false);
            
            // Hide the player gold label.
            goldLabel.applyVisible(false);
            
            // Hide the (available) action buttons.
            
            // Loop through player spell(s).
            gameHD.getAvatar().getSpellList().forEach((spellEnum) -> {
                
                // Hide current player spell in loop.
                mapActionButtonMagic.get(spellEnum.getValue_ActionButtonEnum()).setVisible(false);
            });
            
            /*
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL).setVisible(false);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setVisible(false);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setVisible(false);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT).setVisible(false);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE).setVisible(false);
            mapActionButtonMagic.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT).setVisible(false);
            */
            
            // Hide minimap group.
            minimapGroup.setVisible(false);
            
        }
        
    }
    
    private void info_render_background()
    {
        
        // The function configures and add the actor for the background.
        
        // Create new BaseActor for the background.
        background = new BaseActor();
        
        // Name background actor.
        background.setActorName("Background");
        
        // Assign the Texture to the background Actor.
        background.setTexture(gameHD.getAssetMgr().getImage_xRef(mazemap.getCurrentRegion().getRegionBackground().getValue_Key()));
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        background.setPosition( 0, 0 );
        
        // Add the background Actor to the scene graph.
        mainStage.addActor( background );
        
    }
    
    private void info_render_equipment()
    {
        
        // The function configures and adds the actors for the player, including the weapon and armor.
        // Note:  Actors hidden at start.
        
        String armorKey; // Asset manager key related to armor texture region.
        String weaponKey; // Asset manager key related to weapon texture region.
        
        // 1.  Draw the player base.
        
        // Create and configure new BaseActor for the player base.
        
        // Pos X = Center.
        // Pos Y = 2 * scale factor.  -- Relative to bottom of screen.
        heroineBase = new BaseActor( "heroineBase", 
          HeroineEnum.HeroinePlayerEnum.HEROINE_PLAYER_BASE.getValue_Key(), 
          gameHD.getAssetMgr(), CoreEnum.AlignEnum.ALIGN_CENTER, CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, 
          uiStage.getWidth(), uiStage.getHeight(), null, 2f * gameHD.getConfig().getScale(), 
          CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION, 0 );
        
        // Set player base as invisible.
        heroineBase.setVisible(false);
        
        // Add the player base Actor to the scene graph.
        uiStage.addActor( heroineBase );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( heroineBase );
        
        // 2.  Render the armor worn.
        
        // Create and configure new BaseActor for the player armor.
        
        // Determine asset manager key related to armor.
        armorKey = gameHD.getAvatar().getArmor().getValue_PlayerEnum().toString();
        armorKey = HeroineEnum.HeroinePlayerEnum.valueOf(armorKey).getValue_Key();
        
        // Pos X = Center.
        // Pos Y = 2 * scale factor.  -- Relative to bottom of screen.
        heroineArmor = new BaseActor( "heroineArmor", 
          armorKey, gameHD.getAssetMgr(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, uiStage.getWidth(), uiStage.getHeight(), null, 
          2f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION, 0 );
        
        // Set player armor as invisible.
        heroineArmor.setVisible(false);
        
        // Add the player armor Actor to the scene graph.
        uiStage.addActor( heroineArmor );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( heroineArmor );
        
        // 3.  Render the weapon.
        
        // Create and configure new BaseActor for the player weapon.
        
        // Determine asset manager key related to weapon.
        weaponKey = gameHD.getAvatar().getWeapon().getValue_PlayerEnum().toString();
        weaponKey = HeroineEnum.HeroinePlayerEnum.valueOf(weaponKey).getValue_Key();
        
        // Pos X = Center.
        // Pos Y = 2 * scale factor.  -- Relative to bottom of screen.
        heroineWeapon = new BaseActor( "heroineWeapon", 
          weaponKey, gameHD.getAssetMgr(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, uiStage.getWidth(), uiStage.getHeight(), 
          null, 2f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION, 0 );
        
        // Set player weapon as invisible.
        heroineWeapon.setVisible(false);
        
        // Add the player weapon Actor to the scene graph.
        uiStage.addActor( heroineWeapon );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( heroineWeapon );
        
    }
    
    private void info_render_gold()
    {
        
        // The function configures and adds the label for the current player gold.
        // Note:  Actor hidden at start.
        
        String goldText; // Text to display for player gold.
        
        // 1.  Configure and add the label with the player gold.  Hidden at start.
        
        // Store text to display for player gold.
        goldText = Integer.toString(gameHD.getAvatar().getGold()) + " GOLD";
        
        // Initialize and add label with the player gold.
        goldLabel = new CustomLabel(game.skin, goldText, "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_RIGHT, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage, 
          (float)(gameHD.getConfig().getScale() * -2), (float)(gameHD.getConfig().getScale() * 2), 
          HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        goldLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( goldLabel.getLabel() );
        
    }
    
    private void info_render_hpmp()
    {
        
        // The function configures and adds the labels for the player hit and magic points.
        // Note:  Actors hidden at start.
        
        String hpText; // Text to display for hit points.
        String mpText; // Text to display for magic points.
        
        // 1.  Configure and add the label with the player hit points.  Hidden at start.
        
        // Store text to display for hit points.
        hpText = gameHD.getAvatar().getHpText();
        
        // Initialize and add label with the player hit points.
        hpLabel = new CustomLabel(game.skin, hpText, "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), null, CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, 
          uiStage, (float)(gameHD.getConfig().getScale() * 2), (float)(gameHD.getConfig().getScale() * 12), 
          HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        hpLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( hpLabel.getLabel() );
        
        // 2.  Configure and add the label with the player magic points.  Hidden at start.
        
        // Store text to display for magic points.
        mpText = gameHD.getAvatar().getMpText();
        
        // Initialize and add label with the player magic points.
        mpLabel = new CustomLabel(game.skin, mpText, "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), null, CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, 
          uiStage, (float)(gameHD.getConfig().getScale() * 2), (float)(gameHD.getConfig().getScale() * 2), 
          HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        mpLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mpLabel.getLabel() );
        
    }
    
    private void info_render_itemlist()
    {
        
        // The function configures and adds the labels for the current player armor and weapon.
        // Note:  Actors hidden at start.
        
        // 1.  Configure and add the label with the current player armor.  Hidden at start.
        
        // Initialize and add the label with the current player armor.
        armorLabel = new CustomLabel(game.skin, gameHD.getAvatar().getArmorText(), 
          "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), null, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, uiStage, (float)(gameHD.getConfig().getScale() * 2), 
          (float)(gameHD.getConfig().getScale() * 47), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        armorLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( armorLabel.getLabel() );
        
        // 2.  Configure and add the label with the current player weapon.  Hidden at start.
        
        // Initialize and add label with the current player armor.
        weaponLabel = new CustomLabel(game.skin, gameHD.getAvatar().getWeaponText(), 
          "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), null, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, uiStage, (float)(gameHD.getConfig().getScale() * 2), 
          (float)(gameHD.getConfig().getScale() * 37), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        weaponLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( weaponLabel.getLabel() );
        
    }
    
    private void info_render_minimap()
    {
        
        // The function encapsulates logic used to render the minimap.
        // The minimap comprises a group containing "icon" actors.
        
        // Initialize minimap group.
        minimapGroup = new Group();

        // Render minimap.

        // Store array list with base actors for minimap icons to display.
        minimapIcons = mazemap.minimap_render();

        // Loop through base actors in array list.
        minimapIcons.forEach((actor) -> {

            // Add the minimap icon Actor to the group.
            minimapGroup.addActor(actor);

        });
        
        // Set position of minimap group (lower left corner).
        minimapGroup.setPosition(mazemap.getMinimapOffsetX(), mazemap.getMinimapOffsetY());
        
        // Add events to minimap group.
        addEvent_Minimap();
        
        // Add the minimap group to the scene graph.
        uiStage.addActor(minimapGroup);
        
        /*
        // Update position of power-related labels.
        
        // Place power-related labels slightly below minimap.
        powerActionLabel.displayLabel((float)(gameHD.getConfig().getScale() * 2), 
          mazemap.getMinimapOffsetY() - (float)(gameHD.getConfig().getScale() * 2) - 
          powerActionLabel.getLabel().getHeight());
        powerResultLabel.displayLabel((float)(gameHD.getConfig().getScale() * 2), 
          mazemap.getMinimapOffsetY() - (float)(gameHD.getConfig().getScale() * 12) - 
          powerActionLabel.getLabel().getHeight());
        */
        
    }
    
    private void info_render_no_target()
    {
        
        // The function updates the power action and result labels to display the "(NO TARGET)" message.
        // Associated with attempting to execute an action at an invalid time.
                        
        // Update power action labels.
        powerActionLabel.setLabelText("(NO TARGET)");
        
        // Display power action  labels.
        powerActionLabel.applyVisible(true);
        
        // Set up fade effect for power action label.
        powerActionLabel.addAction_Fade();

        // Play error sound.
        gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_ERROR);
        
    }
    
    private void info_render_powerResponseLines()
    {
        
        // The function configures and adds the labels for the power action and result lines.
        // Note:  Actors hidden at start.
        
        // 1.  Configure and add the label with the power action text.  Hidden at start.
        
        // Initialize and add the label with the power action.
        powerActionLabel = new CustomLabel(game.skin, "", 
          "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), null, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage, (float)(gameHD.getConfig().getScale() * 2), 
          (float)(gameHD.getConfig().getScale() * -30), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        powerActionLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( powerActionLabel.getLabel() );
        
        // 2.  Configure and add the label with the power result text.  Hidden at start.
        
        // Initialize and add label with the power result.
        powerResultLabel = new CustomLabel(game.skin, "", 
          "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), null, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage, (float)(gameHD.getConfig().getScale() * 2), 
          (float)(gameHD.getConfig().getScale() * -40), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        powerResultLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( powerResultLabel.getLabel() );
        
    }
    
    // regionExit = Reference to the exit to process.
    private void processExit(RegionMap.RegionExit regionExit)
    {
        
        // The function encapsulates logic related to the player moving to an exit.
        // The same functionality could support moving the player to a location in another region.
        
        // Set new location of player.
        gameHD.getAvatar().setX(regionExit.getDest_x());
        gameHD.getAvatar().setY(regionExit.getDest_y());
        gameHD.getAvatar().setMap_id(regionExit.getDest_map());
        
        // Update object storing data for the current active region / map.
        
        // Reinitialize the maze map.
        mazemap = new MazeMap(gameHD, regionExit.getDest_map(), viewHeightMain);
        
        // Update and display region label.
        regionLabel.setLabelText_Center(mazemap.getCurrentRegion().getRegionName().toUpperCase());
        regionLabel.applyVisible(true);
        
        // Set up fade effect for region label.
        regionLabel.removeActions();
        regionLabel.addAction_Fade();
        
        // Render updated view.
        renderCurrentView();
        
    }
    
    // regionExit = Reference to the shop to process.
    private void processShop(RegionMap.RegionShop regionShop)
    {
        
        // The function encapsulates logic related to the player entering a shop.
        
        // Reset most aspects of the dialog.
        gameHD.getDialog().resetDialog();
        
        // Build the dialog -- title and buttons and associated text -- associated with the current shop.
        gameHD.getShopInfo().shop_set(regionShop.getShop_id());
        
        // Set new location of player.
        gameHD.getAvatar().setX(regionShop.getDest_x());
        gameHD.getAvatar().setY(regionShop.getDest_y());
        
        // Switch to the dialog screen.
        gameHD.setDialogScreen();
        
    }
    
    private void renderCurrentView()
    {
        
        // The function renders the current exploration view.
        
        ArrayList<BaseActor> removeList; // List of actors to remove.
        
        // 1.  Initialize array lists.
        removeList = new ArrayList<>();
        
        // 2.  Update the background Actor.
        
        // Assign the Texture to the background Actor.
        background.setTexture(gameHD.getAssetMgr().getImage_xRef(mazemap.getCurrentRegion().getRegionBackground().getValue_Key()));
        
        // 3.  Remove existing actors related to current map location / tiles.
        
        // Loop through base actors in array list.
        tiles.forEach((actor) -> {
            
            // Add the tile Actor to the removalList.
            removeList.add( actor );
        
        });
        
        // Clear actors in removal list -- remove them from the array list with the tiles.
        // Loop through actors in removal list.
        removeList.forEach((ba) -> {
            
            // Remove actor from stage.
            ba.destroy();
            
            // Remove actor from array list.
            //tiles.remove(ba);
        
        });
        
        // Reinitialize array list for tiles.
        tiles = new ArrayList<>();
        
        // 4.  Hide any treasure text.
        treasureLabel.applyVisible(false);
        
        // 5.  Render current map location / tiles.
        
        // Store array list with base actors for tiles to display.
        tiles = mazemap.mazemap_render(gameHD.getAvatar().getX(), gameHD.getAvatar().getY(), 
          gameHD.getAvatar().getFacing(), viewWidthMain, treasureLabel, heroineWeapon, weaponLabel,
          heroineArmor, armorLabel, hpLabel, mpLabel, goldLabel, regionLabel);
        
        // Loop through base actors in array list.
        tiles.forEach((actor) -> {
            
            // Add the tile Actor to the scene graph.
            mainStage.addActor( actor );
        
        });
        
        // 6.  Reset flag for minimap rendering, indicating regeneration necessary.
        minimapRenderInd = false;
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt) 
    {   
        
        /*
        The function occurs during the update phase (render method).
        */
        
    }
    
    public void wakeScreen()
    {
        
        /*
        The method gets called when redisplaying the already initialized screen.
        
        The following actions occur:
        
        1.  Wakes up the base screen.
        2.  Adds the background actor to the scene graph.
        3.  Renders the current view -- tiles and minimap.
        4.  Adds back the actors in the ui stage (based on the list, uiStageActors).
        5.  Updates the text for the labels.
        6.  Renders the updated armor and weapon.
        */
        
        String armorKey; // Asset manager key related to armor texture region.
        String weaponKey; // Asset manager key related to weapon texture region.
        
        // Wake up the base screen, setting up viewports, input multiplexer, ....
        wakeBaseScreen();
        
        // Add the background Actor to the scene graph.
        mainStage.addActor( background );
        
        // Render updated view.
        // Note:  Causes a redrawing of the minimap.
        renderCurrentView();
        
        // Loop through actors required for ui stage.
        for (Actor actor : uiStageActors)
        {
            // Add actor to ui stage.
            uiStage.addActor(actor);
        }
        
        // Update labels.
        armorLabel.setLabelText( gameHD.getAvatar().getArmorText() );
        weaponLabel.setLabelText( gameHD.getAvatar().getWeaponText() );
        goldLabel.setLabelText( Integer.toString(gameHD.getAvatar().getGold()) + " GOLD" );
        hpLabel.setLabelText( "HP " + Integer.toString(gameHD.getAvatar().getHp()) + "/" + 
          Integer.toString(gameHD.getAvatar().getMax_hp()) );
        mpLabel.setLabelText( "MP " + Integer.toString(gameHD.getAvatar().getMp()) + "/" + 
          Integer.toString(gameHD.getAvatar().getMax_mp()) );
        
        // Determine asset manager key related to armor.
        armorKey = gameHD.getAvatar().getArmor().getValue_PlayerEnum().toString();
        armorKey = HeroineEnum.HeroinePlayerEnum.valueOf(armorKey).getValue_Key();
        
        // Determine asset manager key related to weapon.
        weaponKey = gameHD.getAvatar().getWeapon().getValue_PlayerEnum().toString();
        weaponKey = HeroineEnum.HeroinePlayerEnum.valueOf(weaponKey).getValue_Key();
        
        // Render updated armor.
        heroineArmor.setTextureRegion( gameHD.getAssetMgr().getTextureRegion(armorKey) );
        
        // Render updated weapon.
        heroineWeapon.setTextureRegion( gameHD.getAssetMgr().getTextureRegion(weaponKey) );
        
    }
    
    // InputProcessor methods for handling discrete input.
    
    @Override
    public boolean keyDown(int keycode)
    {

        // The function gets called when the user presses a key.
        
        boolean movementInd; // Whether movement occurred.  Includes turning left to right.
        boolean movementNewPosInd; // Whether movement occurred that involved a new position.
        RegionMap.RegionExit regionExit; // Exit occurring at location to which player moves.
        RegionMap.RegionShop regionShop; // Shop occurring at location to which player moves.
        
        // Set defaults.
        movementInd = false;
        movementNewPosInd = false;
        
        // 1.  Handle player movement -- when in explore mode.
        
        // Start with player not moving.
        gameHD.getAvatar().setMoved(false);
        
        // If in explore mode, then...
        if (gameHD.getGameState() == HeroineEnum.GameState.STATE_EXPLORE)
        {
            
            // In explore mode.
            
            // If the user pressed the up arrow key, then...
            if (keycode == Input.Keys.UP)
            {
                // The user pressed the up arrow key.
                
                // Try to move forward / up.
                movementInd = gameHD.getAvatar().avatar_move(true, mazemap, gameHD);
                movementNewPosInd = movementInd;
            }

            // If the user pressed the down arrow key, then...
            if (keycode == Input.Keys.DOWN)
            {
                // The user pressed the down arrow key.
                
                // Try to move backward / down.
                movementInd = gameHD.getAvatar().avatar_move(false, mazemap, gameHD);
                movementNewPosInd = movementInd;
            }
            
            // If the user pressed the left arrow key, then...
            if (keycode == Input.Keys.LEFT)
            {
                // The user pressed the left arrow key.
                
                // Turn the player to the left.
                gameHD.getAvatar().avatar_turn_left();
                
                // Update direction label.
                facingLabel.setLabelText(gameHD.getAvatar().getFacing().toString());
                
                // Flag "movement" as occurring.
                movementInd = true;
                
                // Flag that player stayed in same location.
                movementNewPosInd = false;
            }

            // If the user pressed the right arrow key, then...
            if (keycode == Input.Keys.RIGHT)
            {
                // The user pressed the right arrow key.
                
                // Turn the player to the right.
                gameHD.getAvatar().avatar_turn_right();
                
                // Update direction label.
                facingLabel.setLabelText(gameHD.getAvatar().getFacing().toString());
                
                // Flag "movement" as occurring.
                movementInd = true;
                
                // Flag that player stayed in same location.
                movementNewPosInd = false;
            }
            
            // If movement occurred, then...
            if (movementInd)
            {
                
                // Movement occurred.
                // Render updated view.
                
                /*
                System.out.println("Movement occurred.");
                System.out.println("New loc: (" + gameHD.getAvatar().getX() + ", " + 
                  gameHD.getAvatar().getY() + ")");
                System.out.println("Facing: " + gameHD.getAvatar().getFacing());
                */

                // Flag player as moved.
                gameHD.getAvatar().setMoved(true);
                
                // If movement to new location occurred, then...
                if (movementNewPosInd)
                {
                    
                    // Movement to new location occurred.
                    
                    // Check for an exit.
                    regionExit = mazemap.getCurrentRegion().getRegionExit(gameHD.getAvatar().getX(), 
                      gameHD.getAvatar().getY());
                    
                    // If exit exists, then...
                    if (regionExit != null)
                    {
                        
                        // Exit exists.
                        
                        // Process exit.
                        processExit(regionExit);
                        
                    } // End ... Exit exists at current location.
                    
                    else
                    {
                        
                        // No exit exists.
                        
                        // Check for a shop.
                        regionShop = mazemap.getCurrentRegion().getRegionShop(gameHD.getAvatar().getX(), 
                          gameHD.getAvatar().getY());

                        // If shop exists, then...
                        if (regionShop != null)
                        {

                            // Shop exists.

                            // Process shop.
                            processShop(regionShop);

                        } // End ... Shop exists at current location.
                        
                        else
                        {
                            
                            // Neither an exit nor a shop exists at current location.
                            
                            // Render updated view.
                            renderCurrentView();
                            
                        } // End ... If NO exit or shop exists at current location.
                           
                    } // End ... If NO exit exists at current location.
                    
                } // End ... If movement to new location occurred.
                
                else
                {
                    
                    // Stayed in same location (turns, ...).
                    
                    // Render updated view.
                    renderCurrentView();
                    
                }
                
            } // End ... If movement occurred.
            
        }
        
        // Return a value.
        return false;
    }
    
}
