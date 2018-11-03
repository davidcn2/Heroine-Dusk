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
import core.ShakyActor;
import gui.CustomLabel;
import heroinedusk.Combat;
import heroinedusk.HeroineDuskGame;
import heroinedusk.HeroineEnum;
import heroinedusk.MazeMap;
import heroinedusk.RegionMap;
import heroinedusk.Sounds;
import heroinedusk.Spells;

// Java imports.
import java.security.SecureRandom;
import java.util.ArrayList;
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

ArrayList supports dynamic arrays that can grow as needed.
*/

public class ExploreScreen extends BaseScreen { // Extends the BaseScreen class.

    /*
    The class extends the basic functionality of a BaseScreen class and sets up the introduction screen.

    Methods include:

    action_render:  Configures and adds the actors for the action buttons (excluding information).
    addEventBasics:  Adds basic events to the passed action button / BaseActor.
    addEvent_Attack:  Adds events to the passed button (BaseActor) -- used for attacking in combat.
    addEvent_Information:  Adds events to the passed (information) button (BaseActor).
    addEvent_Minimap:  Adds events related to dragging the minimap.
    addEvent_Run:  Adds events to the passed button (BaseActor) -- used for running in combat.
    addEvent_Touch_Burn:  Adds events to the passed button (BaseActor) -- used for the burn (spell).
    addEvent_Touch_Freeze:  Adds events to the passed button (BaseActor) -- used for the freeze (spell).
    addEvent_Touch_Heal:  Adds events to the passed button (BaseActor) -- used for the heal (spell).
    addEvent_Touch_Light:  Adds events to the passed button (BaseActor) -- used for the light (spell).
    addEvent_Touch_Reflect:  Adds events to the passed button (BaseActor) -- used for the reflect (spell).
    addEvent_Touch_Unlock:  Adds events to the passed button (BaseActor) -- used for the unlock (spell).
    create:  Calls constructor for BaseScreen and configures and adds objects in the explore screen.
    disableButton:  Disables a button based on the passed parameters.
    dispose:  Called when removing the screen and allows for clearing of related resources from memory.
    info_clear_messages:  Clears the text and actions from the power action and result lines.
    info_render:  Handles displaying the information view or switching back to the standard display.
    info_render_background:  Configures and add the actor for the background.
    info_render_equipment:  Configures and adds the actors for the player, including the weapon and armor.
    info_render_gold:  Configures and adds the label for the current player gold.
    info_render_hpmp:  Configures and adds the labels for the player hit and magic points.
    info_render_insufficient_mp:  Updates power action label to display "INSUFFICIENT MP!)" message.
    info_render_itemlist:  Configures and adds the labels for the current player armor and weapon.
    info_render_minimap:  Encapsulates logic used to render the minimap.
    info_render_no_target:  Updates the power action and result labels to display the "(NO TARGET)" message.
    info_render_powerResponseLines:  Configures and adds the labels for the power action and result lines.
    prepareGoldPile:  Configures properties for gold pile (array list of actors) and related treasure group.
    processExit:  Encapsulates logic related to the player moving to an exit.
    processShop:  Encapsulates logic related to the player entering a shop.
    renderCurrentView:  Renders the current exploration view.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    wakeScreen:  Called when redisplaying the already initialized screen.
    */
    
    // Declare object variables.
    private CustomLabel armorLabel; // Label showing current player armor.
    private BaseActor background; // BaseActor object that will act as the background.
    private Combat combat; // Reference to the combat engine.
    private ShakyActor enemy; // ShakyActor object that will act as the enemy.
    private CustomLabel enemyLabel; // Label showing enemy type.
    private CustomLabel facingLabel; // Label showing direction player is facing.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private CustomLabel goldLabel; // Label showing current player gold.
    private ArrayList<BaseActor> goldPile; // List of gold actors.
    private BaseActor heroineArmor; // BaseActor object that will act as the player armor.
    private BaseActor heroineBase; // BaseActor object that will act as the base player.
    private BaseActor heroineWeapon; // BaseActor object that will act as the player weapon.
    private CustomLabel hpLabel; // Label showing player hit points.
    private BaseActor infoButton; // BaseActor object that will act as the information button.
    private BaseActor infoButtonSelector; // BaseActor object that will act as the selector for the
      // information and other buttons.
    private CustomLabel infoLabel; // Label showing "INFO" text.
    private Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled; // Hash map containing enabled 
      // status of action buttons.
    private Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosX; // X-coordinate of each action button.
    private Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosY; // Y-coordinate of bottom of each action button.
    private Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons; // Hash map containing
      // BaseActor objects that will act as the action buttons.
    private Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonVisible; // Hash map containing visible 
      // status of action buttons.
    private Map<HeroineEnum.ActionButtonEnum, Boolean> mapIgnoreNextExitEvent_ActionButtons; // Hash map containing
      // whether to ignore next exit event (used with touchUp / exit) -- for action buttons.
    private MazeMap mazemap; // Stores data for the current active region / map.
    private Array<Actor> middleStageActors; // List of actors in middle stage used when waking screen.
    private Group minimapGroup; // Group containing minimap actors.
    private ArrayList<BaseActor> minimapIcons; // BaseActor objects associated with minimap.
    private CustomLabel mpLabel; // Label showing player magic points.
    private CustomLabel powerActionLabel; // Label showing the first line -- power action.
    private CustomLabel powerResultLabel; // Label showing the second line -- power result.
    private CustomLabel regionLabel; // Label showing the current region name.
    private CustomLabel spellsLabel; // Label showing "SPELLS" text.
    private CustomLabel statusLabel; // General status label.
    private ShakyActor tileGroup; // ShakyActor object that will act as the group containing the tiles.
    private ArrayList<BaseActor> tiles; // BaseActor objects associated with tiles.
      // 0 to 12 = Background tiles.  13 = Treasure.  14 = Treasure group.  15 = Chest.  16 = Bone pile.
      // 17 = Lock.
    private CustomLabel treasureLabel; // Label showing treasure description.
    private Array<Actor> uiStageActors; // List of actors in ui stage used when waking screen.
    private CustomLabel weaponLabel; // Label showing current player weapon.
    
    // Declare regular variables.
    private HeroineEnum.SelectPosEnum buttonSelected; // Selected button.
    private boolean infoButtonSelected; // Whether information button selected and clicked.
    private float minimapOffsetX; // X-coordinate of the point where the player first touched the minimap.
    private float minimapOffsetY; // Y-coordinate of the point where the player first touched the minimap.
    private float minimapOriginalX; // Original position (x-coordinate) of the minimap on the stage before drag operation.
    private float minimapOriginalY; // Original position (y-coordinate) of the minimap on the stage before drag operation.
    private boolean minimapRenderInd; // Whether minimap rendered for current location yet.
    private final SecureRandom number; // Used for generating random numbers.
    private HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosX; // List of x-positions to place selector -- related to buttons.
    private HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosY; // List of y-positions to place selector -- related to buttons.
    
    // Declare constants.
    private final Color COLOR_MED_GRAY = new Color(0.50f, 0.50f, 0.50f, 1); // Disabled color.
    private static final Integer[] GOLD_BASE_POS_X_LIST = new Integer[]{36, 27, 45, 45, 21, 34, 12, 61, 58, 0};
    private static final Integer[] GOLD_BASE_POS_Y_LIST = new Integer[]{17, 20, 19, 10, 4, 2, 16, 0, 18, 1};
    private final int SPELL_SUCCESSFUL = 1;
    private final int TILE_COUNT = 25; // Number of tile actors.
    private final int TILE_POS_TREASURE = 13; // Tile position of treasure (actor).
    private final int TILE_POS_TREASURE_GROUP = 14; // Tile position of treasure (group).
    private final int TILE_POS_CHEST = 15; // Tile position of chest.
    private final int TILE_POS_BONE_PILE = 16; // Tile position of bone pile.
    private final int TILE_POS_LOCK = 17; // Tile position of lock.
    
    // hdg = Reference to Heroine Dusk (main) game.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    public ExploreScreen(HeroineDuskGame hdg, int windowWidth, int windowHeight)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Stores reference to main game class.
        // 3.  Start random number generator.
        // 4.  Calls the create() function to perform remaining startup logic.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(hdg, windowWidth, windowHeight);
        
        // Store reference to main game class.
        gameHD = hdg;
        
        // Start random number generator.
        number = new SecureRandom();
        
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
        5.  Configure and add the label showing the general status.  Hidden at start.
        6.  Configure and add the labels for the player hit and magic points.  Hidden at start.
        7.  Configure and add shaky actor (group) that will contain tiles.
        8.  Add unconfigured tiles to array -- creating actors.
        9.  Configure special tiles -- chest, bone pile, lock, ...
        10.  Add and configure the actors for the gold pile.
        11.  Render current map location / tiles.
        12.  Configure and add the label with the direction the player is facing.
        13.  Configure and add the information button Actor.
        14.  Configure and add the label with the "INFO" text.  Hidden at start.
        15.  Configure and add the selector actor for the information button.  Hidden at start.
        16.  Configure and add the label with the "SPELLS" text.  Hidden at start.
        17.  Configure and add the actors for the action buttons (excluding information).  Hidden at start.
        18.  Configure and add the actors for the base player, weapon, and armor.  Hidden at start.
        19.  Configure and add the labels for the current player armor and weapon.  Hidden at start.
        20.  Configure and add the label for the current player gold.  Hidden at start.
        21.  Populate hash map with starting enabled statuses for action buttons.
        22.  Populate hash map with starting ignore next exit event flags for action buttons. 
        23.  Configure and add the labels for the two lines for responses to spells / powers / actions.
            Hidden at start.
        24.  Configure and add the label showing the current region name.  Hidden at start.
        25.  Configure and add the label showing the treasure description.  Hidden at start.
        26.  Configure and add the label showing the enemy type.  Hidden at start.
        27.  Configure and add the enemy actor to the middle stage.  Hidden at start.
        28.  Initialize combat engine.
        
        Notes:
        
        A.  Labels use only the name property, while actors use actorName and name.
        B.  Main stage contains background and tiles.
        C.  Middle stage contains enemies.
        D.  UI stage contains additional actors.
        */
        
        // 1.  Set defaults.
        infoButtonSelected = false;
        minimapRenderInd = false;
        buttonSelected = HeroineEnum.SelectPosEnum.BUTTON_POS_INFO;
        
        // 2.  Initialize arrays, array lists, and hash maps.
        middleStageActors = new Array<>();
        uiStageActors = new Array<>();
        tiles = new ArrayList<>();
        minimapIcons = new ArrayList<>();
        mapActionButtonPosX = new HashMap<>();
        mapActionButtonPosY = new HashMap<>();
        mapActionButtons = new HashMap<>();
        mapActionButtonEnabled = new HashMap<>();
        mapActionButtonVisible = new HashMap<>();
        mapIgnoreNextExitEvent_ActionButtons = new HashMap<>();
        mapSelectorPosX = new HashMap<>();
        mapSelectorPosY = new HashMap<>();
        
        // 3.  Initialize the maze map.
        mazemap = new MazeMap(gameHD, gameHD.getAvatar().getMap_id(), viewHeightMain);
        
        // 4.  Configure and add the background Actor.
        // Note:  The application uses a starting background of tempest, which the foreground objects completely 
        // hide.
        info_render_background();
        
        // 5.  Configure and add the label showing the general status.  Hidden at start.
        statusLabel = new CustomLabel(game.skin, "NO STATUS HERE", "status label", "uiLabelStyle", 
          1.0f, gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, uiStage, 0f, 
          (float)(gameHD.getConfig().getScale() * 12), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( statusLabel.getLabel() );
        
        // 6.  Configure and add the labels for the player hit and magic points.  Hidden at start.
        info_render_hpmp();
        
        // Hide the general status label.
        statusLabel.applyVisible(false);
        
        // 7.  Configure and add shaky actor (group) that will contain tiles.
        // The group allows for shaking the tiles when the player is hit by the enemy in combat.
        
        // Initialize tile group.
        tileGroup = new ShakyActor(viewWidthMain, viewHeightMain);
        
        // Set position of tile group -- to lower left corner of stage.
        tileGroup.setPosition(0f, 0f);
        
        // Set name of tile group.
        tileGroup.setActorName("Tile Group");
        
        // Set as group.
        tileGroup.setGroupOnlyInd(true);
        
        // Add tile group to stage.
        mainStage.addActor(tileGroup);
        
        // 8.  Add unconfigured tiles to array list -- creating actors.
        for (int tileCounter = 1; tileCounter <= TILE_COUNT; tileCounter++)
        {
            
            // Add tile-related array to array list.
            tiles.add( new BaseActor() );
            
            // Add the tile Actor to the scene graph.
            //mainStage.addActor( tiles.get(tileCounter - 1) );
            tileGroup.addActor( tiles.get(tileCounter - 1) );
            
        }
        
        // 9.  Configure special tiles -- chest, bone pile, lock, ...
        mazemap.prepareSpecialTiles( tiles, viewWidthMain );
        
        // 10.  Add and configure the actors for the gold pile.
        prepareGoldPile(tiles);
        
        // 11.  Render current map location / tiles.
        mazemap.mazemap_render(tiles, goldPile, gameHD.getAvatar().getX(), gameHD.getAvatar().getY(), 
          gameHD.getAvatar().getFacing(), viewWidthMain, treasureLabel, heroineWeapon, weaponLabel,
          heroineArmor, armorLabel, hpLabel, mpLabel, goldLabel, regionLabel, statusLabel, false, false,
          mapActionButtons, mapActionButtonEnabled);
        
        // 12.  Configure and add the label with the direction the player is facing.
        
        // Initialize and add label with facing text.
        facingLabel = new CustomLabel(game.skin, gameHD.getAvatar().getFacing().toString(), 
          "player facing label", "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), 
          CoreEnum.AlignEnum.ALIGN_CENTER, CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage, null, 
          (float)(gameHD.getConfig().getScale() * -2), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( facingLabel.getLabel() );
        
        // 13.  Configure and add the information button Actor.
        
        // Create and configure new BaseActor for the information button.
        
        // Pos X = (140 + 2) * scale factor.  2 = Offset.
        // Pos Y = (0 - 2) * scale factor.  -2 = Offset.  -- Relative to top of screen.
        infoButton = new BaseActor( "info button", HeroineEnum.InfoButtonEnum.INFO_BUTTON.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage.getWidth(), 
          uiStage.getHeight(), (140f + 2f) * gameHD.getConfig().getScale(), 
          -2f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // Add events to information button.
        addEvent_Information( infoButton, true );
        
        // Add the information button Actor to the scene graph.
        uiStage.addActor( infoButton );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( infoButton );
        
        // 14.  Configure and add the label with the "INFO" text.
        
        // Initialize and add label with the "INFO" text.
        infoLabel = new CustomLabel(game.skin, "INFO", "info label", "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage, null, 
          (float)(gameHD.getConfig().getScale() * -2), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        infoLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( infoLabel.getLabel() );
        
        // 15.  Configure and add the selector Actor for the selector for the information button.
        
        // Create and configure new BaseActor for the selector for the information button.
        
        // Pos X = 140 * scale factor.  0 = Offset.
        // Pos Y = 0 * scale factor.  -2 = Offset.  -- Relative to top of screen.
        infoButtonSelector = new BaseActor( "info button selector", 
          HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_SELECT.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage.getWidth(), 
          uiStage.getHeight(), 140f * gameHD.getConfig().getScale(), 
          0f, CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE );
        
        // Add events to information button selector.
        addEvent_Information( infoButtonSelector, false );
        
        // Set information button selector as invisible.
        infoButtonSelector.setVisible(false);
        
        // Add the information button selector Actor to the scene graph.
        uiStage.addActor( infoButtonSelector );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( infoButtonSelector );
        
        // Add position information related to the selector for the information button.
        mapSelectorPosX.put( HeroineEnum.SelectPosEnum.BUTTON_POS_INFO, infoButtonSelector.getX() );
        mapSelectorPosY.put( HeroineEnum.SelectPosEnum.BUTTON_POS_INFO, infoButtonSelector.getY() );
        
        // 16.  Configure and add the label with the "SPELLS" text.  Hidden at start.
        
        // Initialize and add label with the "SPELLS" text.
        spellsLabel = new CustomLabel(game.skin, "SPELLS", "spells label", "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_RIGHT, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_RIGHT, uiStage, (float)(gameHD.getConfig().getScale() * -2), 
          (float)(gameHD.getConfig().getScale() * -30), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        spellsLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( spellsLabel.getLabel() );
        
        // 17.  Configure and add the actors for the action buttons (excluding information).
        action_render();
        
        // 18.  Configure and add the actors for the base player, weapon, and armor.  Hidden at start.
        info_render_equipment();
        
        // 19.  Configure and adds the labels for the player armor and weapon.  Hidden at start.
        info_render_itemlist();
        
        // 20.  Configure and add the label for the current player gold.  Hidden at start.
        info_render_gold();
        
        // 21.  Populate hash map with starting enabled statuses for action buttons.
        // 22.  Populate hash map with starting ignore next exit event flags for action buttons.
        
        // Loop through action button enumerations.
        for (HeroineEnum.ActionButtonEnum actionButtonEnum : HeroineEnum.ActionButtonEnum.values())
        {
            
            // Add default value of not enabled for current enumeration to hash map.
            mapActionButtonEnabled.put(actionButtonEnum, false);
            
            // Add default value of do not exit for current enumation to hash map.
            mapIgnoreNextExitEvent_ActionButtons.put(actionButtonEnum, false);
            
        }
        
        // If necessary, override status of heal action button.
        
        // If player NOT at maximum hit points, then...
        if (!gameHD.getAvatar().getHp_AtMax())
        {
            
            // Player NOT at maximum hit points.
            
            // Enable heal button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL, true);
            
        }
        
        // 23.  Configure and add the labels for the two lines for responses to spells / powers / actions.
        //      Hidden at start.
        info_render_powerResponseLines();
        
        // 24.  Configure and add the label showing the current region name.  Hidden at start.
        
        // Initialize and add label with the "SPELLS" text.
        regionLabel = new CustomLabel(game.skin, mazemap.getCurrentRegion().getRegionName(), 
          "region name label", "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), 
          CoreEnum.AlignEnum.ALIGN_CENTER, CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage, 0f, 
          (float)(gameHD.getConfig().getScale() * 12), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        regionLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( regionLabel.getLabel() );
        
        // 25.  Configure and add the label showing the treasure description.  Hidden at start.
        treasureLabel = new CustomLabel(game.skin, "NO TREASURE HERE", "treasure label", "uiLabelStyle", 
          0.8f, gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, uiStage, 0f, 
          (float)(gameHD.getConfig().getScale() * 12), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        treasureLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( treasureLabel.getLabel() );
        
        // 26.  Configure and add the label showing the enemy type.  Hidden at start.
        enemyLabel = new CustomLabel(game.skin, "NO ENEMY HERE", "enemy name label", "uiLabelStyle", 
          1.0f, gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage, 0f, 
          (float)(gameHD.getConfig().getScale() * -2), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        enemyLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( enemyLabel.getLabel() );
        
        // 27.  Configure and add the enemy actor to the middle stage.  Hidden at start.
        enemy = new ShakyActor(viewWidthMain, viewHeightMain);
        
        // Name the actor.
        enemy.setName("enemy actor");
        
        // Hide the actor.
        enemy.setVisible(false);
        
        // Add the enemy Actor to the scene graph.
        middleStage.addActor(enemy);
        
        // Add the actor to the list for use when waking the screen.
        middleStageActors.add(enemy);
        
        // 28.  Initialize combat engine.
        combat = new Combat(gameHD.getAvatar(), gameHD.getConfig().getScale());
        
        // 29.  As necessary, shade action buttons to indicate enabled.
        
        // If bone pile in front of player, then...
        if (mazemap.isBonePileActiveInd())
        {
            
            // Bone pile exists in front of player.
            
            // If sufficient magic points, set burn button shading to indicate enabled.
            if (gameHD.getAvatar().getMp() > 0)
            {
                
                // Player has sufficient magic points to cast burn spell.
                
                // Shade burn button to indicate enabled.
                mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setColor( Color.WHITE );
                
            }
            
        } // End ... If bone pile in front of player.
        
        // If lock in front of player, then...
        if (mazemap.isLockedDoorActiveInd())
        {
            
            // Lock exists in front of player.
            
            // If sufficient magic points, set unlock button shading to indicate enabled.
            if (gameHD.getAvatar().getMp() > 0)
            {
                
                // Player has sufficient magic points to cast unlock spell.
                
                // Shade unlock button to indicate enabled.
                mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setColor( Color.WHITE );
                
            }
            
        } // End ... If lock in front of player.
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    private void addEventBasics(BaseActor buttonActor)
    {
        
        // The function adds basic events to the passed action button / BaseActor.
        // Events include enter and exit.
        
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
                    
                    HeroineEnum.ActionButtonEnum actionButtonEnum; // Enumerated value for current action button.
                    
                    // Get enumerated value for current action button.
                    actionButtonEnum = HeroineEnum.ActionButtonEnum.valueOf(buttonActor.getActorName());
                    
                    // If action button enabled, then...
                    if (mapActionButtonEnabled.get(actionButtonEnum))
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
                    
                    HeroineEnum.ActionButtonEnum actionButtonEnum; // Enumerated value for current action button.
                    
                    // Get enumerated value for current action button.
                    actionButtonEnum = HeroineEnum.ActionButtonEnum.valueOf(buttonActor.getActorName());
                    
                    // If ignoring next exit event, then...
                    if (mapIgnoreNextExitEvent_ActionButtons.get(actionButtonEnum))
                    {
                        // Ignoring next exit event.
                        
                        // Flag to process next exit event.
                        mapIgnoreNextExitEvent_ActionButtons.put(actionButtonEnum, false);
                    }
                    
                    // Otherwise, ...
                    else
                        
                        // Process exit event.
                        
                        // If action button enabled, then...
                        if (mapActionButtonEnabled.get(actionButtonEnum))
                        {

                            // Action button enabled.
                            
                            // Return button to normal color.
                            buttonActor.setColor(Color.WHITE);
                        
                        }
                    
                        else
                        {
                            
                            // Action button disabled.
                            
                            // Return button to disabled color.
                            buttonActor.setColor(COLOR_MED_GRAY);
                            
                        }
                    
                }
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    // infoButtonInd = Whether adding event for information button (vs selector).
    private void addEvent_Information(BaseActor buttonActor, boolean infoButtonInd)
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
                    
                    // If information button selected, then...
                    if (buttonSelected == HeroineEnum.SelectPosEnum.BUTTON_POS_INFO || infoButtonInd)
                    {
                        
                        // Information button selected.
                        
                        // Apply a dark shade to the information button.
                        infoButton.setColor(Color.LIGHT_GRAY);
                        
                    }
                    
                    else
                    {
                        
                        // Button other than information selected.
                        
                        // Apply a dark shade to the selected button.
                        mapActionButtons.get(
                          HeroineEnum.ActionButtonExploreEnum.valueOf_xRef(
                          buttonSelected).getValue_ActionButtonEnum()).setColor(Color.LIGHT_GRAY);
                        
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
                    if (ignoreNextExitEvent)
                        
                        // Ignoring next exit event.
                        
                        // Flag to process next exit event.
                        ignoreNextExitEvent = false;
                    
                    // Otherwise, ...
                    else
                        
                        // Process exit event.
                        
                        // If information button selected, then...
                        if (buttonSelected == HeroineEnum.SelectPosEnum.BUTTON_POS_INFO || infoButtonInd)
                        {

                            // Information button selected.
                        
                            // Return information button to normal color.
                            infoButton.setColor(Color.WHITE);
                            
                        }
                    
                        else
                        {

                            // Button other than information selected.

                            // Return selected button to normal color.
                            mapActionButtons.get(
                              HeroineEnum.ActionButtonExploreEnum.valueOf_xRef(
                              buttonSelected).getValue_ActionButtonEnum()).setColor(Color.WHITE);

                        }
                        
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
    private void addEvent_Attack(BaseActor buttonActor)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Provides functionality for the attack (combat) button.
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
                    mapIgnoreNextExitEvent_ActionButtons.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK, 
                      true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    System.out.println("Attack!");
                    
                    // Perform attack action using combat engine.
                    combat.fight(HeroineEnum.FightEnum.FIGHT_ATTACK, gameHD.getSounds(), gameHD.getAvatar(),
                      powerActionLabel, powerResultLabel, hpLabel, mpLabel, buttonActor, 
                      mapActionButtonEnabled, null);
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
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
                    
                    int burn_amount; // Either amount of damage to enemy, -1 when unable to cast spell, or 
                      // 1 when succesfully burning an object.
                    HeroineEnum.ImgTileEnum imgTileNumForward; // Tile image enumeration value for location
                      // in front of player.  Null in combat.
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN, 
                      true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    // If player in combat, then...
                    if (gameHD.getGameState() == HeroineEnum.GameState.STATE_COMBAT)
                    {
                        
                        // Player in combat.
                        
                        // Cast burn spell and return base damage.
                        burn_amount = Spells.cast_burn(gameHD.getAvatar(), gameHD.getAtlasItems(), true, false, 
                          null, powerActionLabel, powerResultLabel, mpLabel, tiles, mazemap, buttonActor, 
                          mapActionButtonEnabled, number, gameHD.getSounds());
                        
                        // Perform burn action using combat engine.
                        combat.fight(HeroineEnum.FightEnum.FIGHT_BURN, gameHD.getSounds(), gameHD.getAvatar(),
                          powerActionLabel, powerResultLabel, hpLabel, mpLabel, buttonActor, 
                          mapActionButtonEnabled, burn_amount);
                        
                    }
                    
                    else
                    {
                        
                        // Player NOT in combat.
                        
                        // If minimap group initialized, then...
                        if (minimapGroup != null)
                        {

                            // Minimap group initialized.

                            // Hide the minimap.
                            minimapGroup.setVisible(false);

                        }
                        
                        // Store tile image enumeration value for location in front of player.
                        imgTileNumForward = mazemap.getImgTileEnum_ForwardLoc(gameHD.getAvatar());
                        
                        // Cast burn spell.
                        burn_amount = Spells.cast_burn(gameHD.getAvatar(), gameHD.getAtlasItems(), false, 
                          infoButtonSelected, imgTileNumForward, powerActionLabel, powerResultLabel, mpLabel, tiles, 
                          mazemap, buttonActor, mapActionButtonEnabled, number, gameHD.getSounds());
                        
                        // If player successfully cast burn spell, then...
                        if (burn_amount == SPELL_SUCCESSFUL)
                        {

                            // Player successfully cast burn spell.
                        
                            // Move burn button to original location (in spell list / area).

                            // Pos X = -18 * scale factor.  -- Relative to right of screen.
                            // Pos Y = +62 * scale factor.  -- Relative to bottom of screen.
                            mapActionButtons.get( 
                              HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setRelativePosition(null, 
                              CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
                              uiStage.getHeight(), -18f * gameHD.getConfig().getScale(), 
                              62f * gameHD.getConfig().getScale() );
                            
                            // Reset flag for minimap rendering, indicating regeneration necessary.
                            minimapRenderInd = false;
                            
                        } // End ... If player successfully cast burn spell.
                        
                    } // End ... If player NOT in combat.
                    
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
                    mapIgnoreNextExitEvent_ActionButtons.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE, 
                      true);
                    
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
                        info_render_no_target(null);
                        
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
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL, 
                      true);
                    
                    // Process heal action.
                    processAction_Heal();
                    
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
                    mapIgnoreNextExitEvent_ActionButtons.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT, 
                      true);
                    
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
                        info_render_no_target(null);
                        
                    }
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    // buttonActor = Reference to BaseActor for the button.
    private void addEvent_Run(BaseActor buttonActor)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Provides functionality for the run (combat) button.
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
                    mapIgnoreNextExitEvent_ActionButtons.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN, 
                      true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    System.out.println("Run!");
                    
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
                    
                    int unlock_amount; // Either amount of damage to enemy, -1 when unable to cast spell, or 
                      // 1 when succesfully unlocking an object.
                    HeroineEnum.ImgTileEnum imgTileNumForward; // Tile image enumeration value for location
                      // in front of player.  Null in combat.
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, 
                      true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    // If player in combat, then...
                    if (gameHD.getGameState() == HeroineEnum.GameState.STATE_COMBAT)
                    {
                        
                        // Player in combat.
                        
                    }
                    
                    else
                    {
                        
                        // Player NOT in combat.
                        
                        // If minimap group initialized, then...
                        if (minimapGroup != null)
                        {

                            // Minimap group initialized.

                            // Hide the minimap.
                            minimapGroup.setVisible(false);
                            
                        }
                        
                        // Store tile image enumeration value for location in front of player.
                        //imgTileNumForward = mazemap.getImgTileEnum_ForwardLoc(gameHD.getAvatar());
                        imgTileNumForward = mazemap.getImgTileEnum_Side(gameHD.getAvatar(), true);
                        
                        // Cast unlock spell.
                        unlock_amount = Spells.cast_unlock(gameHD.getAvatar(), gameHD.getAtlasItems(), false, 
                          infoButtonSelected, imgTileNumForward, powerActionLabel, powerResultLabel, mpLabel, 
                          tiles, mazemap, buttonActor, mapActionButtonEnabled, number, gameHD.getSounds());
                        
                        // If player successfully cast unlock spell, then...
                        if (unlock_amount == SPELL_SUCCESSFUL)
                        {
                            
                            // Player successfully cast unlock spell.
                            
                            // Move unlock button to original location (in spell list / area).
                            
                            // Pos X = -38 * scale factor.  -- Relative to right of screen.
                            // Pos Y = +42 * scale factor.  -- Relative to bottom of screen.
                            mapActionButtons.get( 
                              HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setRelativePosition(null, 
                              CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
                              uiStage.getHeight(), -38f * gameHD.getConfig().getScale(), 
                              42f * gameHD.getConfig().getScale() );
                            
                            // Reset flag for minimap rendering, indicating regeneration necessary.
                            minimapRenderInd = false;
                            
                        } // End ... If player successfully cast unlock spell.
                        
                    } // End ... If player NOT in combat.
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    
    /*
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
                    
                    String mpText; // Text to display for magic points.
                    
                    // Flag to ignore next exit event.
                    mapIgnoreNextExitEvent_ActionButtons.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, 
                      true);
                    
                    // Remove any existing text and actions on the power action and result labels.
                    info_clear_messages();
                    
                    // If minimap group initialized, then...
                    if (minimapGroup != null)
                    {
                        
                        // Minimap group initialized.
                        
                        // Hide the minimap.
                        minimapGroup.setVisible(false);
                        
                    }
                    
                    // If player lacks sufficient magic points, then...
                    if (gameHD.getAvatar().getMp() == 0)
                    {
                        
                        // Player lacks sufficient magic points.
                        
                        // If square in front of player NOT a locked door and NOT in combat mode, then...
                        if (mazemap.getImgTileEnum_ForwardLoc(gameHD.getAvatar()) != 
                          HeroineEnum.ImgTileEnum.IMG_TILE_LOCKED_DOOR && 
                          gameHD.getGameState() != HeroineEnum.GameState.STATE_COMBAT)
                        {
                            
                            // Square in front of player NOT a locked door and NOT in combat mode.
                            
                            // Render "(NO TARGET)" message.
                            info_render_no_target(HeroineEnum.SoundEnum.SOUND_BLOCKED);
                            
                        }
                        
                        else
                        {
                            
                            // Square in front of player is a locked door and NOT in combat mode.
                            
                            // Update power action labels.
                            powerActionLabel.setLabelText("INSUFFICIENT MP!");
                            
                            // Display power action  labels.
                            powerActionLabel.applyVisible(true);

                            // Set up fade effect for power action label.
                            powerActionLabel.addAction_Fade();

                            // Play error sound.
                            gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_ERROR);
                            
                        }
                        
                    } // End ... If player lacks sufficient magic points.
                    
                    // Otherwise, if player in combat, then...
                    else if (gameHD.getGameState() == HeroineEnum.GameState.STATE_COMBAT)    
                    {
                        
                        // Player in combat and has sufficient magic points.
                        
                        System.out.println("Unlock!");
                        
                        // Play unlock sound.
                        gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_UNLOCK);
                        
                    }
                    
                    else
                    {
                        
                        // Player NOT in combat and has sufficient magic points.
                        
                        // System.out.println("Tile: " + mazemap.getImgTileEnum_ForwardLoc(gameHD.getAvatar()));
                        
                        // If tile in front of player is a locked door, then...
                        if (mazemap.getImgTileEnum_ForwardLoc(gameHD.getAvatar()) == 
                          HeroineEnum.ImgTileEnum.IMG_TILE_LOCKED_DOOR)
                        {
                            
                            // Unlock the door in the tile in front of the player.
                            
                            // Play unlock sound.
                            gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_UNLOCK);
                            
                            // Set up an action to fade out the lock.
                            tiles.get(TILE_POS_LOCK).addAction_FadeOut(0.25f, 0.75f);
                            
                            // Disable lock events.
                            mazemap.setLockedDoorActiveInd(false);
                            
                            // Set locked door in array list inactive and remove from hash map entry associated 
                            // with current location.
                            gameHD.getAtlasItems().removeLockedDoorFirst(
                              gameHD.getAvatar().getMapLocation_ForwardLoc());
                            
                            // Remove tile showing locked door.
                            //mazemap.removeTileMap_Value(HeroineEnum.TileMapKeyEnum.TILE_MAP_KEY_LOCK);
                            
                            // Change tile in front of player to regular (unlocked) door.
                            mazemap.setImgTileEnum_ForwardLoc(gameHD.getAvatar(), 
                              HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_DOOR);
                            
                            // Move unlock button to original location (in spell list / area).
                            
                            // Pos X = -38 * scale factor.  -- Relative to right of screen.
                            // Pos Y = +42 * scale factor.  -- Relative to bottom of screen.
                            mapActionButtons.get( 
                              HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setRelativePosition(null, 
                              CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
                              uiStage.getHeight(), -38f * gameHD.getConfig().getScale(), 
                              42f * gameHD.getConfig().getScale() );
                            
                            // Decrement player magic points.
                            gameHD.getAvatar().decrement_mp();
                            
                            // Store text to display for magic points.
                            mpText = "MP " + Integer.toString(gameHD.getAvatar().getMp()) + "/" +
                              Integer.toString(gameHD.getAvatar().getMax_mp());

                            // Update magic points label.
                            mpLabel.setLabelText(mpText);
                            
                            // Display action-related text.
                            info_update_powerResponseLines("UNLOCK!", "DOOR OPENED!");
                            
                            // Set the unlock button to not visible.
                            mapActionButtons.get(
                              HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setVisible(false);
                            
                            // Apply a dark shade to the unlock button to signify not currently enabled.
                            mapActionButtons.get(
                              HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setColor( COLOR_MED_GRAY );
                            
                            // Disable unlock button.
                            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, false);
                            
                            // TO DO:  Add avatar save.
                            
                        } // End ... If tile in front of player is a locked door.
                        
                        else
                        {
                            
                            // Tile in front of player not a locked door.
                            
                            // Render "(NO TARGET)" message.
                            info_render_no_target(HeroineEnum.SoundEnum.SOUND_BLOCKED);
                            
                        }
                        
                    } // End ... If NOT in combat.
                    
                } // End ... touchUp.
                
            }; // End ... InputListener.
        
        // Add event to button.
        buttonActor.addListener(buttonEvent);
        
    }
    */
    
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
                    mapIgnoreNextExitEvent_ActionButtons.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT, 
                      true);
                    
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
        
        /*
        The function configures and adds the actors for the action buttons (excluding information).
        
        Note:  Actors hidden at start.
        
        Action buttons include:
        
        1.  Heal (spell)
        2.  Burn (spell)
        3.  Unlock (spell)
        4.  Light (spell)
        5.  Freeze (spell)
        6.  Reflect (spell)
        7.  Attack (combat)
        8.  Run (combat)
        */
        
        BaseActor temp; // Used as placeholder for actor for an action button, for adding to hash map.
        
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
            
            // Player at maximum hit points or has no magic left.
            
            // Apply a dark shade to the button to signify not currently enabled.
            temp.setColor( COLOR_MED_GRAY );
            
        }
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Heal( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the heal (spell) button to the hash map.
        mapActionButtons.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL, temp );
        
        // Add the heal (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL) );
        
        // Store stage location of button.
        mapActionButtonPosX.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL, temp.getX());
        mapActionButtonPosY.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL, temp.getY());
        
        // Store stage location of button selector.
        mapSelectorPosX.put( HeroineEnum.SelectPosEnum.BUTTON_POS_HEAL, temp.getX() - 12f );
        mapSelectorPosY.put( HeroineEnum.SelectPosEnum.BUTTON_POS_HEAL, temp.getY() - 12f );
        
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
        temp.setColor( COLOR_MED_GRAY );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Burn( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the burn (spell) button to the hash map.
        mapActionButtons.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN, temp );
        
        // Add the burn (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN) );
        
        // Store stage location of button.
        mapActionButtonPosX.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN, temp.getX());
        mapActionButtonPosY.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN, temp.getY());
        
        // Store stage location of button selector.
        mapSelectorPosX.put( HeroineEnum.SelectPosEnum.BUTTON_POS_BURN, temp.getX() - 12f );
        mapSelectorPosY.put( HeroineEnum.SelectPosEnum.BUTTON_POS_BURN, temp.getY() - 12f );
        
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
        temp.setColor( COLOR_MED_GRAY );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Unlock( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the unlock (spell) button to the hash map.
        mapActionButtons.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, temp );
        
        // Add the unlock (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK) );
        
        // Store stage location of button.
        mapActionButtonPosX.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, temp.getX());
        mapActionButtonPosY.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, temp.getY());
        
        // Store stage location of button selector.
        mapSelectorPosX.put( HeroineEnum.SelectPosEnum.BUTTON_POS_UNLOCK, temp.getX() - 12f );
        mapSelectorPosY.put( HeroineEnum.SelectPosEnum.BUTTON_POS_UNLOCK, temp.getY() - 12f );
        
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
        temp.setColor( COLOR_MED_GRAY );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Light( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the light (spell) button to the hash map.
        mapActionButtons.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT, temp );
        
        // Add the light (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT) );
        
        // Store stage location of button.
        mapActionButtonPosX.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT, temp.getX());
        mapActionButtonPosY.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT, temp.getY());
        
        // Store stage location of button selector.
        mapSelectorPosX.put( HeroineEnum.SelectPosEnum.BUTTON_POS_LIGHT, temp.getX() - 12f );
        mapSelectorPosY.put( HeroineEnum.SelectPosEnum.BUTTON_POS_LIGHT, temp.getY() - 12f );
        
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
        temp.setColor( COLOR_MED_GRAY );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Freeze( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the freeze (spell) button to the hash map.
        mapActionButtons.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE, temp );
        
        // Add the freeze (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE) );
        
        // Store stage location of button.
        mapActionButtonPosX.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE, temp.getX());
        mapActionButtonPosY.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE, temp.getY());
        
        // Store stage location of button selector.
        mapSelectorPosX.put( HeroineEnum.SelectPosEnum.BUTTON_POS_FREEZE, temp.getX() - 12f );
        mapSelectorPosY.put( HeroineEnum.SelectPosEnum.BUTTON_POS_FREEZE, temp.getY() - 12f );
        
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
        temp.setColor( COLOR_MED_GRAY );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Touch_Reflect( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the reflect (spell) button to the hash map.
        mapActionButtons.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT, temp );
        
        // Add the reflect (spell) button Actor to the scene graph.
        uiStage.addActor( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT) );
        
        // Store stage location of button.
        mapActionButtonPosX.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT, temp.getX());
        mapActionButtonPosY.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT, temp.getY());
        
        // Store stage location of button selector.
        mapSelectorPosX.put( HeroineEnum.SelectPosEnum.BUTTON_POS_REFLECT, temp.getX() - 12f );
        mapSelectorPosY.put( HeroineEnum.SelectPosEnum.BUTTON_POS_REFLECT, temp.getY() - 12f );
        
        // 7.  Configure and add the attack (combat) button Actor.
        
        // Create and configure new BaseActor for the attack (combat) button.
        
        // Pos X = -38 * scale factor.  -- Relative to right of screen.
        // Pos Y = -22 * scale factor.  -- Relative to top of screen.
        temp = new BaseActor( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK.toString(), 
          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_UPPER_RIGHT, uiStage.getWidth(), 
          uiStage.getHeight(), -38f * gameHD.getConfig().getScale(), 
          -22f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Attack( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the attack (combat) button to the hash map.
        mapActionButtons.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK, temp );
        
        // Add the attack (combat) button Actor to the scene graph.
        uiStage.addActor( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK) );
        
        // Store stage location of button.
        mapActionButtonPosX.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK, temp.getX());
        mapActionButtonPosY.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK, temp.getY());
        
        // Store stage location of button selector.
        mapSelectorPosX.put( HeroineEnum.SelectPosEnum.BUTTON_POS_ATTACK, temp.getX() - 12f );
        mapSelectorPosY.put( HeroineEnum.SelectPosEnum.BUTTON_POS_ATTACK, temp.getY() - 12f );
        
        // 8.  Configure and add the run (combat) button Actor.
        
        // Create and configure new BaseActor for the run (combat) button.
        
        // Pos X = -18 * scale factor.  -- Relative to right of screen.
        // Pos Y = -22 * scale factor.  -- Relative to top of screen.
        temp = new BaseActor( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN.toString(), 
          HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN.getValue_Key(), 
          gameHD.getAssetMgr(), null, CoreEnum.PosRelativeEnum.REL_POS_UPPER_RIGHT, uiStage.getWidth(), 
          uiStage.getHeight(), -18f * gameHD.getConfig().getScale(), 
          -22f * gameHD.getConfig().getScale(), CoreEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION );
        
        // Add basic events (enter, exit) to button.
        addEventBasics( temp );
        
        // Add click-related events to button.
        addEvent_Run( temp );
        
        // Hide the button.
        temp.setVisible( false );
        
        // Add the actor for the run (combat) button to the hash map.
        mapActionButtons.put( HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN, temp );
        
        // Add the run (combat) button Actor to the scene graph.
        uiStage.addActor( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN) );
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN) );
        
        // Store stage location of button.
        mapActionButtonPosX.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN, temp.getX());
        mapActionButtonPosY.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN, temp.getY());
        
        // Store stage location of button selector.
        mapSelectorPosX.put( HeroineEnum.SelectPosEnum.BUTTON_POS_RUN, temp.getX() - 12f );
        mapSelectorPosY.put( HeroineEnum.SelectPosEnum.BUTTON_POS_RUN, temp.getY() - 12f );
        
    }
    
    // buttonActor = Reference to BaseActor for the button to disable.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    public static void disableButton(BaseActor buttonActor, HeroineEnum.ActionButtonEnum actionButtonEnum,
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled)
    {
        
        // The function disables a button based on the passed parameters.
        // The function is commonly called when running out of magic points due to casting a spell.
        
        // Disable button with passed enumeration value.
        mapActionButtonEnabled.put(actionButtonEnum, false);
        
        // If button (actor) passed, then...
        if (buttonActor != null)
        {

            // Button (actor) passed.

            // Apply a dark shade to the button to signify not currently enabled.
            buttonActor.setColor( Color.DARK_GRAY );

        }
        
    }
    
    @Override
    public void dispose()
    {
        
        // The method is called when removing the screen and allows for clearing of related resources 
        // from memory.
        
        // Call manual dispose method in superclass.
        super.disposeManual();
        
    }
    
    // keycode = Code for key pressed.
    private void handle_key_explore(int keycode)
    {
        
        // The function encapsulates logic related to key presses when in explore mode.
        
        boolean movementInd; // Whether movement occurred.  Includes turning left to right.
        boolean movementNewPosInd; // Whether movement occurred that involved a new position.
        RegionMap.RegionExit regionExit; // Exit occurring at location to which player moves.
        RegionMap.RegionShop regionShop; // Shop occurring at location to which player moves.
        boolean turnInd; // Whether movement involved turning.
        
        // Set defaults.
        movementInd = false;
        movementNewPosInd = false;
        turnInd = false;
        
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

            // Flag that player turned.
            turnInd = true;
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

            // Flag that player turned.
            turnInd = true;
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
                    processExit(regionExit, turnInd);

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
                        renderCurrentView(turnInd, false);

                        // Check for encounter.
                        mazemap.check_random_encounter(combat, enemyLabel, infoButtonSelector, hpLabel, 
                          mpLabel, infoButton, facingLabel, regionLabel, enemy, gameHD.getAssetMgr(), 
                          mapActionButtons, mapActionButtonEnabled, mapActionButtonPosX, 
                          mapActionButtonPosY);

                    } // End ... If NO exit or shop exists at current location.

                } // End ... If NO exit exists at current location.

            } // End ... If movement to new location occurred.

            else
            {

                // Stayed in same location (turns, ...).

                // Render updated view.
                renderCurrentView(turnInd, false);

            }

        } // End ... If movement occurred.
        
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
        
        // Declare object variables.
        Set<Map.Entry<HeroineEnum.SpellEnum, Boolean>> entrySetSpellList; // Set view of the spellbook mappings in the hash map.
        
        // Declare regular variables.
        HeroineEnum.SpellEnum entryKey; // Hash map key related to current entry when looping through set.
        
        // Clear text from and hide power action and result labels.
        info_clear_messages();
        
        // If switching to information view, then...
        if (infoButtonSelected)
        {
            
            // Switch to information view.
            
            // Select the information button.
            buttonSelected = HeroineEnum.SelectPosEnum.BUTTON_POS_INFO;

            // Position the selector around the information button.
            infoButtonSelector.setPosition( mapSelectorPosX.get(HeroineEnum.SelectPosEnum.BUTTON_POS_INFO), 
              mapSelectorPosY.get(HeroineEnum.SelectPosEnum.BUTTON_POS_INFO) );
            
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
            
            // Clear hash map with visible status of action buttons.
            mapActionButtonVisible.clear();
            
            // Display the (available) action buttons.
            
            // Store a set view of the spellbook mappings for the hash map.
            entrySetSpellList = gameHD.getAvatar().getSpellList().entrySet();
            
            // Loop through entries for player spells.
            for (Map.Entry entrySpell : entrySetSpellList)
            {
                // Store the key for the current entry.
                entryKey = (HeroineEnum.SpellEnum)entrySpell.getKey();
                
                // Display current player spell in loop.
                mapActionButtons.get(entryKey.getValue_ActionButtonEnum()).setVisible(true);
                
                // Add spell as visible in related hash map.
                mapActionButtonVisible.put(entryKey.getValue_ActionButtonEnum(), true);
            }
            
            /*
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL).setVisible(true);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setVisible(true);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setVisible(true);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT).setVisible(true);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE).setVisible(true);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT).setVisible(true);
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
            
            // Hide any visible secondary items.
            
            // If treasure actor active, then...
            if (mazemap.getTileActiveInd(TILE_POS_TREASURE))   
            {
                // Treasure actor active.
                
                // Hide treasure actor.
                tiles.get(TILE_POS_TREASURE).setVisible(false);
                
                // Hide treasure label.
                treasureLabel.applyVisible(false);
            }
            
            // If treasure actor (group) active, then...
            if (mazemap.getTileActiveInd(TILE_POS_TREASURE_GROUP))  
            {
                // Treasure actor (group) active.
                
                // Hide treasure actor (group).
                tiles.get(TILE_POS_TREASURE_GROUP).setVisible(false);
                
                // Hide treasure label.
                treasureLabel.applyVisible(false);
            }
            
            // If bone pile actor active, then...
            if (mazemap.getTileActiveInd(TILE_POS_BONE_PILE))   
            {
                // Bone pile actor active.
                
                // Move burn button to original location (in spell list / area).
                
                // Pos X = -18 * scale factor.  -- Relative to right of screen.
                // Pos Y = +62 * scale factor.  -- Relative to bottom of screen.
                mapActionButtons.get( 
                  HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setRelativePosition(null, 
                  CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
                  uiStage.getHeight(), -18f * gameHD.getConfig().getScale(), 
                  62f * gameHD.getConfig().getScale() );
                
                // Disable bone pile actor events.
                mazemap.setBonePileActiveInd(false);
                
            }
            
            // If lock actor active, then...
            if (mazemap.getTileActiveInd(TILE_POS_LOCK))   
            {
                // Lock actor active.
                
                // Move unlock button to original location (in spell list / area).
                
                // Pos X = -38 * scale factor.  -- Relative to right of screen.
                // Pos Y = +42 * scale factor.  -- Relative to bottom of screen.
                mapActionButtons.get( 
                  HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setRelativePosition(null, 
                  CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, uiStage.getWidth(), 
                  uiStage.getHeight(), -38f * gameHD.getConfig().getScale(), 
                  42f * gameHD.getConfig().getScale() );
                
                // Disable lock actor events.
                mazemap.setLockedDoorActiveInd(false);
                
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
            
            // Store a set view of the spellbook mappings for the hash map.
            entrySetSpellList = gameHD.getAvatar().getSpellList().entrySet();
            
            // Loop through entries for player spells.
            for (Map.Entry entrySpell : entrySetSpellList)
            {
                // Store the key for the current entry.
                entryKey = (HeroineEnum.SpellEnum)entrySpell.getKey();
                
                // Display current player spell in loop.
                mapActionButtons.get(entryKey.getValue_ActionButtonEnum()).setVisible(false);
            }
            
            /*
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL).setVisible(false);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setVisible(false);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setVisible(false);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_LIGHT).setVisible(false);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_FREEZE).setVisible(false);
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT).setVisible(false);
            */
            
            // Hide minimap group.
            minimapGroup.setVisible(false);
            
            // If bone pile actor active, then...
            if (mazemap.getTileActiveInd(TILE_POS_BONE_PILE))   
            {
                // Bone pile actor active.
                
                // Enable bone pile actor events.
                mazemap.setBonePileActiveInd(true);
                
            }
            
            // If lock actor active, then...
            if (mazemap.getTileActiveInd(TILE_POS_LOCK))   
            {
                // Lock actor active.
                
                // Enable lock actor events.
                mazemap.setLockedDoorActiveInd(true);
                
            }
            
        }
        
        // Hide the general status label and remove associated actions.
        statusLabel.removeActions();
        statusLabel.applyVisible(false);
        
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
        goldLabel = new CustomLabel(game.skin, goldText, "player gold label", "uiLabelStyle", 1.0f, 
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
        hpLabel = new CustomLabel(game.skin, hpText, "player hp label", "uiLabelStyle", 1.0f, 
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
        mpLabel = new CustomLabel(game.skin, mpText, "player mp label" , "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), null, CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, 
          uiStage, (float)(gameHD.getConfig().getScale() * 2), (float)(gameHD.getConfig().getScale() * 2), 
          HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        mpLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( mpLabel.getLabel() );
        
    }
    
    // soundEnum = Sound to play.
    // powerActionLabel = Label showing the first line -- power action.
    // sounds = Reference to the sounds class.
    public static void info_render_insufficient_mp(CustomLabel powerActionLabel, Sounds sounds)
    {
        
        // The function updates the power action label to display the "INSUFFICIENT MP!)" message.
        // Associated with attempting to execute a spell action with insufficient magic points.
        
        // Update power action labels.
        powerActionLabel.setLabelText("INSUFFICIENT MP!");

        // Display power action  labels.
        powerActionLabel.applyVisible(true);

        // Set up fade effect for power action label.
        powerActionLabel.addAction_Fade();

        // Play error sound.
        sounds.playSound(HeroineEnum.SoundEnum.SOUND_ERROR);
        
    }
    
    private void info_render_itemlist()
    {
        
        // The function configures and adds the labels for the current player armor and weapon.
        // Note:  Actors hidden at start.
        
        // 1.  Configure and add the label with the current player armor.  Hidden at start.
        
        // Initialize and add the label with the current player armor.
        armorLabel = new CustomLabel(game.skin, gameHD.getAvatar().getArmorText(), "player armor label",
          "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), null, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_LEFT, uiStage, (float)(gameHD.getConfig().getScale() * 2), 
          (float)(gameHD.getConfig().getScale() * 47), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        armorLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( armorLabel.getLabel() );
        
        // 2.  Configure and add the label with the current player weapon.  Hidden at start.
        
        // Initialize and add label with the current player armor.
        weaponLabel = new CustomLabel(game.skin, gameHD.getAvatar().getWeaponText(), "player weapon label",
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
    
    // soundEnum = Sound to play.
    public void info_render_no_target(HeroineEnum.SoundEnum soundEnum)
    {
        
        // The function updates the power action label to display the "(NO TARGET)" message.
        // Associated with attempting to execute an action at an invalid time.
        
        HeroineEnum.SoundEnum sound; // Sound to play.
        
        // Update power action labels.
        powerActionLabel.setLabelText("(NO TARGET)");
        
        // Display power action labels.
        powerActionLabel.applyVisible(true);
        
        // Set up fade effect for power action label.
        powerActionLabel.addAction_Fade();

        // If null passed for sound to play, then...
        if (soundEnum == null)
            // Null passed for sound to play.
            // Default to error sound.
            sound = HeroineEnum.SoundEnum.SOUND_ERROR;
        else
            // Use sound passed in parameter.
            sound = soundEnum;
        
        // Play error sound.
        gameHD.getSounds().playSound(sound);
        
    }
    
    // soundEnum = Sound to play.
    // powerActionLabel = Label showing the first line -- power action.
    // sounds = Reference to the sounds class.
    public static void info_render_no_target(HeroineEnum.SoundEnum soundEnum, CustomLabel powerActionLabel,
      Sounds sounds)
    {
        
        // The function updates the power action and result labels to display the "(NO TARGET)" message.
        // Associated with attempting to execute an action at an invalid time.
        
        HeroineEnum.SoundEnum sound; // Sound to play.
        
        // Update power action labels.
        powerActionLabel.setLabelText("(NO TARGET)");
        
        // Display power action labels.
        powerActionLabel.applyVisible(true);
        
        // Set up fade effect for power action label.
        powerActionLabel.addAction_Fade();

        // If null passed for sound to play, then...
        if (soundEnum == null)
            // Null passed for sound to play.
            // Default to error sound.
            sound = HeroineEnum.SoundEnum.SOUND_ERROR;
        else
            // Use sound passed in parameter.
            sound = soundEnum;
        
        // Play error sound.
        sounds.playSound(sound);
        
    }
    
    private void info_render_powerResponseLines()
    {
        
        // The function configures and adds the labels for the power action and result lines.
        // Note:  Actors hidden at start.
        
        // 1.  Configure and add the label with the power action text.  Hidden at start.
        
        // Initialize and add the label with the power action.
        powerActionLabel = new CustomLabel(game.skin, "", "power action label",
          "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), null, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage, (float)(gameHD.getConfig().getScale() * 2), 
          (float)(gameHD.getConfig().getScale() * -30), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        powerActionLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( powerActionLabel.getLabel() );
        
        // 2.  Configure and add the label with the power result text.  Hidden at start.
        
        // Initialize and add label with the power result.
        powerResultLabel = new CustomLabel(game.skin, "", "power result label",
          "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), null, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, uiStage, (float)(gameHD.getConfig().getScale() * 2), 
          (float)(gameHD.getConfig().getScale() * -40), HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // Hide the label.
        powerResultLabel.applyVisible(false);
        
        // Add the actor to the list for use when waking the screen.
        uiStageActors.add( powerResultLabel.getLabel() );
        
    }
    
    // powerAction = Text to show for the first line -- power action.
    // powerResult = Text to show for the second line -- power result.
    private void info_update_powerResponseLines(String powerAction, String powerResult)
    {
        
        // The function updates the power action and result labels to display the passed messages.
        // The function fades the messages after displaying them.
        
        // Update power action and result labels.
        powerActionLabel.setLabelText(powerAction);
        powerResultLabel.setLabelText(powerResult);
        
        // Display power action and result labels.
        powerActionLabel.applyVisible(true);
        powerResultLabel.applyVisible(true);
        
        // Set up fade effect for power action and result labels.
        powerActionLabel.addAction_Fade();
        powerResultLabel.addAction_Fade();
        
    }
    
    // powerAction = Text to show for the first line -- power action.
    // powerResult = Text to show for the second line -- power result.
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    public static void info_update_powerResponseLines(String powerAction, String powerResult,
      CustomLabel powerActionLabel, CustomLabel powerResultLabel)
    {
        
        // The function updates the power action and result labels to display the passed messages.
        // The function fades the messages after displaying them.
        
        // Update power action and result labels.
        powerActionLabel.setLabelText(powerAction);
        powerResultLabel.setLabelText(powerResult);
        
        // Display power action and result labels.
        powerActionLabel.applyVisible(true);
        powerResultLabel.applyVisible(true);
        
        // Set up fade effect for power action and result labels.
        powerActionLabel.addAction_Fade();
        powerResultLabel.addAction_Fade();
        
    }
    
    private HeroineEnum.SelectPosEnum moveDown()
    {
        
        // The function moves down an item in the action buttons.
        // The function does not accomplish any movement to the right or left.
        
        HeroineEnum.ActionButtonExploreEnum actionButtonNavEnumPre; // Action button navigation enumerated
          // value related to the selection.  Before movement.
        HeroineEnum.SelectPosEnum selectPosEnumPost; // Enumeration value related to explore screen button 
          // selection -- after movement.
          
        // Set defaults.
        selectPosEnumPost = null;
        
        // Get the action button navigation enumerated value related to the current selection.
        actionButtonNavEnumPre = HeroineEnum.ActionButtonExploreEnum.valueOf_xRef(buttonSelected);
        
        // If enumerated value exists, then...
        if (actionButtonNavEnumPre != null)
        {
            
            // Enumerated value exists.
        
            // Get the selected button enumerated value after applying the movement.
            selectPosEnumPost = actionButtonNavEnumPre.getValue_MoveDown();
            
            // Move the selector.
            moveSelector(selectPosEnumPost);
            
        }
        
        // Return (enumerated value for) new position.
        return selectPosEnumPost;
        
    }
    
    private HeroineEnum.SelectPosEnum moveLeft()
    {
        
        // The function moves left an item in the action buttons.
        // The function does not accomplish any movement up or down.
        
        HeroineEnum.SelectPosEnum selectPosEnumPost; // Enumeration value related to explore screen button 
          // selection -- after movement.
        HeroineEnum.ActionButtonExploreEnum actionButtonNavEnumPre; // Action button navigation enumerated
          // value related to the selection.  Before movement.
        
        // Set defaults.
        selectPosEnumPost = null;
        
        // Get the action button navigation enumerated value related to the current selection.
        actionButtonNavEnumPre = HeroineEnum.ActionButtonExploreEnum.valueOf_xRef(buttonSelected);
        
        // If enumerated value exists, then...
        if (actionButtonNavEnumPre != null)
        {
            
            // Enumerated value exists.
        
            // Get the selected button enumerated value after applying the movement.
            selectPosEnumPost = actionButtonNavEnumPre.getValue_MoveLeft();
            
            // Move the selector.
            moveSelector(selectPosEnumPost);
            
        }
        
        // Return (enumerated value for) new position.
        return selectPosEnumPost;
        
    }
    
    private HeroineEnum.SelectPosEnum moveRight()
    {
        
        // The function moves right an item in the action buttons.
        // The function does not accomplish any movement up or down.
        
        HeroineEnum.SelectPosEnum selectPosEnumPost; // Enumeration value related to explore screen button 
          // selection -- after movement.
        HeroineEnum.ActionButtonExploreEnum actionButtonNavEnumPre; // Action button navigation enumerated
          // value related to the selection.  Before movement.
        
        // Set defaults.
        selectPosEnumPost = null;
        
        // Get the action button navigation enumerated value related to the current selection.
        actionButtonNavEnumPre = HeroineEnum.ActionButtonExploreEnum.valueOf_xRef(buttonSelected);
        
        // If enumerated value exists, then...
        if (actionButtonNavEnumPre != null)
        {
            
            // Enumerated value exists.
        
            // Get the selected button enumerated value after applying the movement.
            selectPosEnumPost = actionButtonNavEnumPre.getValue_MoveRight();
            
            // Move the selector.
            moveSelector(selectPosEnumPost);
            
        }
        
        // Return (enumerated value for) new position.
        return selectPosEnumPost;
        
    }
    
    // selectPosEnumPost = Enumeration value related to explore screen button selection -- after movement.
    private Boolean moveSelector(HeroineEnum.SelectPosEnum selectPosEnumPost)
    {
        
        // The function moves the selector to the new location specified in the passed parameter.
        // No movement occurs when the parameter is null (occurs when trying to move to invalid location).
        
        Boolean enabled; // Whether the action button to which to move is enabled.
        HeroineEnum.ActionButtonEnum actionButtonEnumPost; // Enumeration value related to action button --
          // after movement.
        
        // Set defaults.
        enabled = null;
        
        // If enumerated value exists after applying movement, then...
        if (selectPosEnumPost != null)
        {

            // Enumerated value exists after applying movement.

            // If moving to information button, then...
            if (selectPosEnumPost == HeroineEnum.SelectPosEnum.BUTTON_POS_INFO)
            {
                // Moving to information button.

                // Mark as enabled.
                enabled = true;
            }

            // Otherwise, check hash map whether enabled.
            else
            {

                // Moving to a button other than information.

                // Determine action button after movement.
                actionButtonEnumPost = HeroineEnum.ActionButtonExploreEnum.valueOf_xRef(
                  selectPosEnumPost).getValue_ActionButtonEnum();

                // Base enabled status on visibility of action button.
                enabled = mapActionButtonVisible.get(actionButtonEnumPost) != null;

            } // End ... If NOT moving to information button.

            // If action button enabled, then...
            if (enabled)
            {

                // Action button enabled.

                // Update selected button.
                buttonSelected = selectPosEnumPost;

                // Move selector.
                infoButtonSelector.setPosition( mapSelectorPosX.get(buttonSelected), 
                  mapSelectorPosY.get(buttonSelected) );

            } // End ... If action button enabled.

        } // End ... If enumerated value exists after applying movement.
        
        // Return whether enabled -- or null.
        return enabled;
        
    }
    
    private HeroineEnum.SelectPosEnum moveUp()
    {
        
        // The function moves up an item in the action buttons.
        // The function does not accomplish any movement to the right or left.
        
        HeroineEnum.SelectPosEnum selectPosEnumPost; // Enumeration value related to explore screen button 
          // selection -- after movement.
        HeroineEnum.ActionButtonExploreEnum actionButtonNavEnumPre; // Action button navigation enumerated
          // value related to the selection.  Before movement.
        
        // Set defaults.
        selectPosEnumPost = null;
        
        // Get the action button navigation enumerated value related to the current selection.
        actionButtonNavEnumPre = HeroineEnum.ActionButtonExploreEnum.valueOf_xRef(buttonSelected);
        
        // If enumerated value exists, then...
        if (actionButtonNavEnumPre != null)
        {
            
            // Enumerated value exists.
        
            // Get the selected button enumerated value after applying the movement.
            selectPosEnumPost = actionButtonNavEnumPre.getValue_MoveUp();
            
            // Move the selector.
            moveSelector(selectPosEnumPost);
            
        }
        
        // Return (enumerated value for) new position.
        return selectPosEnumPost;
        
    }
    
    // tiles = BaseActor objects associated with tiles.  0 to 12 = Background tiles.  13 and beyond for others.
    private void prepareGoldPile(ArrayList<BaseActor> tiles)
    {
        
        // The function configures properties for the gold pile (array list of actors) and related treasure 
        // group.
        // The function supports displaying up to 1023 gold.
        
        ArrayList<Integer> bitwiseList; // List of numbers that add up to gold amount.
        int counter; // Used to iterate through gold actors.
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
        float treasureHeight; // Height to which to resize treasure.
        float treasureWidth; // Width to which to resize treasure.
        
        // Set defaults.
        counter = 0;
        goldMinX = 999999;
        goldMinY = 999999;
        goldMaxX = 0;
        goldMaxY = 0;
        
        // Populate list of numbers that add up to gold amount.
        bitwiseList = routines.UtilityRoutines.bitwiseList(1023);
        
        // Initialize array list for gold pile.
        goldPile = new ArrayList<>();

        // Loop through gold amounts to show.
        for (Integer gold : bitwiseList)
        {

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
            
            // Add image to gold pile.
            goldPile.add(new BaseActor("GOLD" + Integer.toString(gold), imgTreasureEnum.getValue_AtlasKey(), 
              goldPosX, goldPosY, gameHD.getAssetMgr()));
            
            // Scale actor.
            goldPile.get(counter).setWidth(goldWidth);
            goldPile.get(counter).setHeight(goldHeight);

            // Set actor as not visible.
            goldPile.get(counter).setVisible(false);
            
            // Store minimum and maximum x and y to calculate dimensions and adjust base location to 0, 0.
            goldMinX = Math.min(goldMinX, goldPosX);
            goldMinY = Math.min(goldMinY, goldPosY);
            goldMaxX = Math.max(goldMaxX, goldPosX + goldWidth);
            goldMaxY = Math.max(goldMaxY, goldPosY + goldHeight);

            // Increment counter.
            counter++;
            
        }

        // Loop through gold actors in group.
        for (BaseActor gold : goldPile)
        {
            // Adjust base location to 0, 0 -- allows easy positioning of group and proper centering.
            gold.setX(gold.getX() - goldMinX);
            gold.setY(gold.getY() - goldMinY);

            // Add actor to group.
            tiles.get(TILE_POS_TREASURE_GROUP).addActor(gold);
        }

        // Set treasure actor to function / draw as group only.
        tiles.get(TILE_POS_TREASURE_GROUP).setGroupOnlyInd(true);

        // Determine width and height of treasure.
        treasureWidth = goldMaxX - goldMinX;
        treasureHeight = goldMaxY - goldMinY;

        // Store width and height of treasure (group) in actor.
        tiles.get(TILE_POS_TREASURE_GROUP).setGroupWidth(treasureWidth);
        tiles.get(TILE_POS_TREASURE_GROUP).setGroupHeight(treasureHeight);
        
        // Set treasure origin to center to allow for proper rotation.
        tiles.get(TILE_POS_TREASURE_GROUP).setOriginCenter_Group();
        
        // Set position of treasure group.
        
        // Position treasure actor (group) at center of stage horizontally and chest vertically.
        tiles.get(TILE_POS_TREASURE_GROUP).setPosition( (viewWidthMain - treasureWidth) / 2, 
          tiles.get(TILE_POS_CHEST).getOriginY() );
        
    }
    
    private void processAction_Heal()
    {
        
        // The function encapsulates logic related to performing the heal action.
        
        // Remove any existing text and actions on the power action and result labels.
        info_clear_messages();
        
        System.out.println("Heal!");

        // If player in combat, then...
        if (gameHD.getGameState() == HeroineEnum.GameState.STATE_COMBAT)
        {

            // Player in combat.

            // Perform heal action using combat engine.
            combat.fight(HeroineEnum.FightEnum.FIGHT_HEAL, gameHD.getSounds(), gameHD.getAvatar(),
              powerActionLabel, powerResultLabel, hpLabel, mpLabel, 
              mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL), mapActionButtonEnabled, null);

        }

        else
        {

            // Player NOT in combat.

            // Hide the minimap.
            minimapGroup.setVisible(false);

            // Cast heal spell.
            Spells.cast_heal(gameHD.getAvatar(), powerActionLabel, powerResultLabel, hpLabel, 
              mpLabel, mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL), 
              mapActionButtonEnabled, gameHD.getSounds());
            
        }
        
    }
    
    // regionExit = Reference to the exit to process.
    // turnInd = Whether movement involved turning.
    private void processExit(RegionMap.RegionExit regionExit, boolean turnInd)
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
        renderCurrentView(turnInd, false);
        
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
    
    // turnInd = Whether movement involved turning.
    // redrawInd = Whether redraw occurring -- like when waking screen or loading a game.
    private void renderCurrentView(boolean turnInd, boolean redrawInd)
    {
        
        /*
        The function renders the current exploration view.
        
        Beyond rendering, logic includes:
        
        1.  Adding event-supported chest when in square in front of player.
        2.  Handling chest in same square as player.
        3.  Remove existing actors related to current map location / tiles.
        4.  Hide any treasure text.
        5.  Render current map location / tiles.
        6.  Reset flag for minimap rendering, indicating regeneration necessary.
        */
        
        // 1.  Initialize array lists.
        
        
        // 2.  Update the background Actor.
        
        // Assign the Texture to the background Actor.
        background.setTexture(
          gameHD.getAssetMgr().getImage_xRef(mazemap.getCurrentRegion().getRegionBackground().getValue_Key()));
        
        // 4.  Hide any treasure text.
        treasureLabel.applyVisible(false);
        
        // 5.  Render current map location / tiles.
        
        // Store array list with base actors for tiles to display.
        mazemap.mazemap_render(tiles, goldPile, gameHD.getAvatar().getX(), gameHD.getAvatar().getY(), 
          gameHD.getAvatar().getFacing(), viewWidthMain, treasureLabel, heroineWeapon, weaponLabel,
          heroineArmor, armorLabel, hpLabel, mpLabel, goldLabel, regionLabel, statusLabel, turnInd, redrawInd,
          mapActionButtons, mapActionButtonEnabled);
        
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
        3.  Adds the tile group actor to the scene graph.
        4.  Renders the current view -- tiles and minimap.
        5.  Adds back the actors in the ui stage (based on the list, uiStageActors).
        6.  Adds back the actors in the middle stage (based on the list, middleStageActors).
        7.  Updates the text for the labels.
        8.  Renders the updated armor and weapon.
        
        Notes:
        
        A.  Labels use only the name property, while actors use actorName and name.
        B.  Main stage contains background and tiles.
        C.  Middle stage contains enemies.
        D.  UI stage contains additional actors.
        */
        
        String armorKey; // Asset manager key related to armor texture region.
        String weaponKey; // Asset manager key related to weapon texture region.
        
        // Wake up the base screen, setting up viewports, input multiplexer, ....
        wakeBaseScreen();
        
        // Add the background Actor to the scene graph.
        mainStage.addActor( background );
        
        // Add the tile group Actor to the scene graph.
        mainStage.addActor( tileGroup );
        
        // Render updated view.
        // Note:  Causes a redrawing of the minimap.
        renderCurrentView( false, true );
        
        // Loop through actors required for ui stage.
        for (Actor actor : uiStageActors)
        {
            
            // Add actor to ui stage.
            uiStage.addActor( actor );
            
        }
        
         // Loop through actors required for middle stage.
        for (Actor actor : middleStageActors)
        {
            
            // Add actor to ui stage.
            middleStage.addActor( actor );
            
        }
        
        // Update labels.
        armorLabel.setLabelText( gameHD.getAvatar().getArmorText() );
        weaponLabel.setLabelText( gameHD.getAvatar().getWeaponText() );
        goldLabel.setLabelText( Integer.toString(gameHD.getAvatar().getGold()) + " GOLD" );
        hpLabel.setLabelText( "HP " + Integer.toString(gameHD.getAvatar().getHp()) + "/" + 
          Integer.toString(gameHD.getAvatar().getMax_hp()) );
        mpLabel.setLabelText( "MP " + Integer.toString(gameHD.getAvatar().getMp()) + "/" + 
          Integer.toString(gameHD.getAvatar().getMax_mp()) );
        statusLabel.setLabelText( "" );
        treasureLabel.setLabelText( "" );
        
        // Hide status label and remove all associated actions.
        statusLabel.removeActions();
        statusLabel.applyVisible( false );
        
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
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {

        // The function gets called when the user presses a key.
        
        // 1.  Handle player movement -- when in explore mode.
        // 2.  Handle navigating through action buttons -- when in information or combat mode.
        
        // Start with player not moving.
        gameHD.getAvatar().setMoved(false);
        
        // TESTING
            
        if (keycode == Input.Keys.S)
        {
            // Shake the enemy.
            System.out.println("Shake the enemy");
            enemy.startShake(5);
        }

        if (keycode == Input.Keys.T)
        {
            // Shake the tiles.
            System.out.println("Shake the tiles");
            tileGroup.startShake(5);
        }
        
        if (keycode == Input.Keys.M)
        {
            // Display map / tile information.
            System.out.println("Tiles: " + mazemap.getCurrentRegion().getRegionTiles());
        }

        if (keycode == Input.Keys.P)
        {
            // Display position.
            System.out.println("Position: (" + gameHD.getAvatar().getX() + ", " + 
              gameHD.getAvatar().getY() + ").");
        }
        
        // TESTING
        
        // If in explore mode, then...
        if (gameHD.getGameState() == HeroineEnum.GameState.STATE_EXPLORE)
        {
            
            // In explore mode.
            
            // Process key event while in explore mode.
            handle_key_explore(keycode);
            
        } // End ... If in explore mode.
        
        // If in information mode, then...
        else if (gameHD.getGameState() == HeroineEnum.GameState.STATE_INFO)
        {
            
            // In information mode.
            
            System.out.println("In information mode");
            
            // Depending on key pressed, ...
            switch (keycode) {
                
                // If the user pressed the up arrow key, then...
                case Input.Keys.UP:
                    
                    // The user pressed the up arrow key.
                    
                    // Try to move up an action button.
                    moveUp();
                    
                    // Exit selector.
                    break;
                    
                // Otherwise, if the user pressed the down arrow key, then...
                case Input.Keys.DOWN:
                    
                    // The user pressed the down arrow key.
                    
                    // Try to move down an action button.
                    moveDown();
                    
                    // Exit selector.
                    break;
                
                // Otherwise, if the user pressed the left arrow key, then...
                case Input.Keys.LEFT:
                    
                    // The user pressed the left arrow key.
                    
                    // Try to move left an action button.
                    moveLeft();
                    
                    // Exit selector.
                    break;
                    
                // Otherwise, if the user pressed the right arrow key, then...
                case Input.Keys.RIGHT:
                    
                    // The user pressed the right arrow key.
                    
                    // Try to move right an action button.
                    moveRight();
                    
                    // Exit selector.
                    break;
                    
                // Otherwise, if the user pressed the enter / return key, then...
                case Input.Keys.ENTER:
                    
                    // The user pressed the enter / return key.
                    
                    // Depending on active action button, ...
                    switch (buttonSelected) {
                        
                        case BUTTON_POS_HEAL:
                            
                            // The user pressed enter on the heal button.
                            
                            // Process heal action.
                            processAction_Heal();
                            
                            // Exit selector.
                            break;
                            
                        default:
                            
                            // The user pressed enter on an unknown button.
                            
                            // Display warning.
                            System.out.println("Warning:  Pressing enter with unknown button selected.");
                            
                            // Exit selector.
                            break;
                        
                    } // End ... Depending on active action button.
                        
                    
                    // Exit selector.
                    break;
                    
                // Otherwise, ...
                default:
                
                    // Otherwise...
                    
                    // Exit selector.
                    break;
                    
            } // End ... Depending on key pressed.
            
        } // End ... If in information mode.
        
        // Return a value.
        return false;
        
    }
    
}
