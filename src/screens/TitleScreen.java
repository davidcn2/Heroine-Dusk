package screens;

// LibGDX imports.
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

// Local project imports.
import core.BaseActor;
import core.BaseGame;
import core.BaseScreen;
import gui.CustomLabel;
import gui.CustomProgressBar;
import heroinedusk.HeroineDuskGame;
import heroinedusk.HeroineEnum;
import routines.ArrayRoutines;

// Java imports.
import java.util.ArrayList;

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

public class TitleScreen extends BaseScreen { // Extends the BaseScreen class.
    
    /*
    The class extends the basic functionality of a BaseScreen class and sets up the main menu screen.

    Methods include:

    addEvent:  Adds events to the passed custom label.
    buildMenu:  Builds the current menu and adds its objects to the scene graph.
    create:  Calls constructor for BaseScreen and configures and adds objects in the title screen.
    dispose:  Called when removing the screen and allows for clearing of related resources from memory.
    titleLogic:  Handles logic related to the button clicked (or activated via keypress).
    title_set_menu:  Switches to the passed menu and updates the list of text to display.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    wakeScreen:  Called when redisplaying the already initialized screen.
    */
    
    // Declare object variables.
    private BaseActor background; // BaseActor object that will act as the background.
    private CustomLabel menuLabels[]; // Collection of menu labels.
    private CustomProgressBar progressBar; // Reference to custom progress bar object.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private ArrayList<Label> removeList; // Array of Label objects to remove from screen.
    private CustomLabel titleLabel; // Title label.
    
    // Declare regular variables.
    final private boolean initialized; // Whether screen initialized.
    private boolean loadedGame; // Whether a loaded game.
    private int menu_id; // Menu type displayed.  Set to -1 when initializing each time screen loads.
    private int menu_selector; // Menu item selected.  Base 0.
    private int menu_selector_keys; // Menu item navigated to via key presses.  Base 0.
    private String menu[]; // List of current menu selections / text.
    private int windowHeight; // Application window height.
    private int windowWidth; // Application window width.
    
    // Declare constants.
    private final int MENU_ID_MAIN = 0;
    private final int MENU_ID_OPTIONS = 1;
    private final int OPTIONS_MENU_ANIMATIONS = 0;
    private final int OPTIONS_MENU_MUSIC = 1;
    private final int OPTIONS_MENU_SOUNDS = 2;
    private final int OPTIONS_MENU_MINIMAP = 3;
    private final int OPTIONS_MENU_BACK = 4;
    private final int TITLE_MENU_MAIN = 0;
    private final int TITLE_MENU_OPTIONS = 1;
    private final int TITLE_MENU_LOAD = 2;
    private final int TITLE_MENU_SAVE = 3;
    private final int TITLE_MENU_EXIT = 4;
    
    // Game world dimensions.
    private int mapWidth; // Total map width, in pixels.
    private int mapHeight; // Total map height, in pixels.
    
    // g = Reference to base game.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    // hdg = Reference to Heroine Dusk (main) game.
    public TitleScreen(BaseGame g, int windowWidth, int windowHeight, HeroineDuskGame hdg)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Sets game world dimensions.
        // 3.  Calls the create() function to perform remaining startup logic.
        
        // Call the constructor for the BaseScreen (parent / super) class.
        super(g, windowWidth, windowHeight);
        
        // Set game world dimensions equal to those of the window.
        this.mapWidth = windowWidth;
        this.mapHeight = windowHeight;
        
        // Store application window dimensions.
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        
        // Set menu identifier to indicate performing initial display.
        this.menu_id = -1;
        
        // Store reference to main game class.
        gameHD = hdg;
        
        // Perform additional logic related to startup / create phase, including configuration and addition
        // of actors to stage.
        create();
        
        // Update as initialized.
        this.initialized  = true;
        
    }
    
    public final void create()
    {
        
        /* 
        The function occurs during the startup / create phase and accomplishes the following:
        
        
        */
        
        // 1.  Set defaults.
        this.menu_id = -1;
        this.menu_selector = 0;
        this.menu_selector_keys = 0;
        this.loadedGame = false;
        
        // 2.  Initialize arrays and array lists.
        this.menu = new String[4];
        removeList = new ArrayList<>();
        
        // 3.  Configure and add the background Actor.
        
        // Create new BaseActor for the background.
        background = new BaseActor();
        
        // Name background actor.
        background.setActorName("Background");
        
        // Assign the Texture to the background Actor.
        background.setTexture(gameHD.getAssetMgr().getImage_xRef(HeroineEnum.ImgBackgroundEnum.IMG_BACK_NIGHTSKY.getValue_Key()));
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        background.setPosition( 0, 0 );
        
        // Add the background Actor to the scene graph.
        mainStage.addActor( background );
        
        // 4.  Configure and add the label with the title.
        
        // Initialize label with title text.
        titleLabel = new CustomLabel(game.skin, "THE PERILS OF GRIZHAWK", "uiLabelStyle", 1.25f, 
          gameHD.getConfig().getTextLineHeight(), HeroineEnum.FontEnum.FONT_UI.getValue_Key());
        
        // Add label to scene graph.
        mainStage.addActor(titleLabel.displayLabelCenterX(600, viewWidthMain));
        
        // 5.  Update the menu list to contain those of the main menu.
        // Calling the function also builds / displays the menu.
        
        // Build menu list.
        title_set_menu(TITLE_MENU_MAIN);
        
    }
    
    // customLabel = Reference to the label to which to add the event.
    // label_id = Menu item number for which to add event.  Base 0.
    public void addEvent(CustomLabel customLabel, int label_id)
    {
        
        // The function adds events to the passed custom label.
        // Events include:  touchDown, touchUp, enter, and exit.
        
        InputListener labelEvent; // Events to add to passed custom label.
        
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
                    // Notes:  Occurs when user press down mouse on label.
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
                    // Notes:  Occurs when user releases mouse on label.
                    
                    // Update the selected menu item based on the label clicked.
                    menu_selector = label_id;
                    
                    // Flag to ignore next exit event.
                    ignoreNextExitEvent = true;
                    
                    // Process logic related to the label clicked.
                    titleLogic();
                    
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
                    
                    // Apply a dark shade to the label.
                    customLabel.colorLabelDark();
                    
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
                        
                        // Return label to normal color.
                        customLabel.colorLabelNormal();
                        
                }
                
            }; // End ... InputListener.
        
        // Add event to label.
        customLabel.addEvent(labelEvent);
        
    }
    
    private void buildMenu()
    {
        
        // The function builds the current menu and adds its objects to the scene graph.
        
        // Declare regular variables.
        int counter; // Used to iterate through menu items.
        float labelHeight; // Height of current label.
        int menuTop; // Y-coordinate at which to place first item in menu.
        String menuItemAdj; // Menu item, adjusted to display selection where necessary.
        
        // Set defaults.
        counter = 0;
        
        // Determine y-coordinate of top menu item.
        menuTop = gameHD.getConfig().getMenuTop();
        
        // Loop through menu items.
        for (String menuItem : menu)
        {
            
            // If selected matches current item, then...
            if (counter == menu_selector_keys)
                // Selected matches current item.
                menuItemAdj = "[ " + menuItem + " ]";
            else
                // Selected differs from current item.
                menuItemAdj = menuItem;
            
            // Initialize label with text from current menu item.
            //menuLabels[counter] = new CustomLabel(game.skin, menuItem, "uiLabelStyle", 1.25f);
            menuLabels[counter] = new CustomLabel(game.skin, menuItemAdj, "uiLabelStyle", 1.0f, 
              gameHD.getConfig().getTextLineHeight(), HeroineEnum.FontEnum.FONT_UI.getValue_Key());
            
            // Get height of current label.
            labelHeight = menuLabels[counter].getLabelHeight();
            
            // Double label height to simulate a blank line.
            labelHeight *= 1.25;
            
            // Add label to scene graph.
            mainStage.addActor(menuLabels[counter].displayLabelCenterX(menuTop - counter * labelHeight, 
              viewWidthMain));
            
            // Add event to label.
            addEvent(menuLabels[counter], counter);
            
            // Increment counter.
            counter++;
            
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
    
    // id = Identifier for menu type to show.
    private void title_set_menu(int id)
    {
        
        // The function switches to the passed menu and updates the list of text to display.
        
        // If identifier passed for same menu type, then...
        if (menu_id == id)
            // Identifier passed for same menu type.
            return;
        
        // Identifier passed for different menu type...
        
        // If displaying menu for second or later time (reset count each time screen is displayed), then...
        if (menu_id >= 0)
        
            {
            // Displaying menu for second or later time (during current display of screen).
            
            // Add current menu labels to list of actors to destroy.
            for (CustomLabel customLabel : menuLabels)
                {
                removeList.add(customLabel.getLabel());
                }
            
            // Reinitialize menu labels array.
            menuLabels = new CustomLabel[menu.length];
            }
        
        // Reset to first possible menu selection / navigation item.
        menu_selector = 0;
        menu_selector_keys = 0;
        
        // Update menu type to the one passed.
        menu_id = id;

        // Reset array containing menu options.
        menu = new String[5];
  
        // If displaying main menu, then...
        if (id == TITLE_MENU_MAIN) 
            {
            // Displaying main menu.
            ArrayRoutines.addAll(menu, (loadedGame ? "CONTINUE" : "START"), "OPTIONS", "LOAD", "SAVE", "EXIT");
            }
  
        // Otherwise, if displaying options menu, then...
        else if (id == TITLE_MENU_OPTIONS) 
            {
            // Displaying options menu.
                
            if (gameHD.getConfig().getOptions().getAnimationsOn())
                menu[0] = "ANIMATIONS ARE ON";
            else
                menu[0] = "ANIMATIONS ARE OFF";
            
            if (gameHD.getConfig().getOptions().getMusicOn())
                menu[1] = "MUSIC IS ON";
            else
                menu[1] = "MUSIC IS OFF";

            if (gameHD.getConfig().getOptions().getSfxOn())
                menu[2] = "SOUNDS ARE ON";
            else
                menu[2] = "SOUNDS ARE OFF";

            if (gameHD.getConfig().getOptions().getMinimapOn())
                menu[3] = "MINIMAP IS ON";
            else
                menu[3] = "MINIMAP IS OFF";
    
            menu[4] = "BACK";
            }
        
        // Initialize menu labels array.
        menuLabels = new CustomLabel[menu.length];
        
        // Display the menu.
        buildMenu();
        
    }
    
    private void titleLogic()
    {
        
        // The function handles logic related to the button clicked (or activated via keypress).
        
        String menuItemText; // Text to display for menu item.
        
        // If viewing main menu, then...
        if (menu_id == MENU_ID_MAIN)
            
            {
            // Viewing main menu.
        
            // Depending on selection...
            switch (menu_selector) {

                case TITLE_MENU_MAIN:

                    // User clicked start or continue button.
                    
                    // Set shop location.
                    gameHD.getShopInfo().shop_set(HeroineEnum.ShopEnum.SHOP_A_NIGHTMARE);
                    
                    // Update text next to third dialog option.
                    gameHD.getDialog().getOptions()[2].msg1 = "Wake up";
                    
                    // Switch to the dialog screen.
                    gameHD.setDialogScreen();

                    // Exit selector.
                    break;

                case TITLE_MENU_OPTIONS:

                    // User clicked options button.

                    // Update the menu list to contain those of the options.
                    // Calling the function also builds / displays the menu.
                    title_set_menu(TITLE_MENU_OPTIONS);

                    // Exit selector.
                    break;

                case TITLE_MENU_LOAD:

                    // User clicked load button.
                    
                    // Exit selector.
                    break;

                case TITLE_MENU_SAVE:

                    // User clicked save button.
                    
                    // Exit selector.
                    break;

                case TITLE_MENU_EXIT:

                    // User clicked exit button.
                    
                    // Exit the game.
                    Gdx.app.exit();

                    // Exit selector.
                    break;

                default:

                    // Unknown button type selected.
                    System.out.println("Warning:  Unknown main menu option selected!");

                    // Exit selector.
                    break;

                } // Depending on selection...
            
            } // Viewing main menu...
        
        // Otherwise, if viewing options menu, then...
        else if (menu_id == MENU_ID_OPTIONS)
        
            {
            // Viewing options menu.
                
            // Depending on selection...
            switch (menu_selector) {

                case OPTIONS_MENU_ANIMATIONS:

                    // User clicked animations button.

                    // Reverse animations flag.
                    gameHD.getConfig().getOptions().AnimationsReverse();

                    // Get base text to display.
                    menuItemText = gameHD.getConfig().getOptions().getAnimationsText();
                    
                    // If current menu item via key navigation, then...
                    if (menu_selector_keys == OPTIONS_MENU_ANIMATIONS)
                        
                        // Current menu item via key navigation.
                        // Adjust text.
                        menuItemText = "[ " + menuItemText + " ]";
                        
                    // Update and recenter label.
                    menuLabels[menu_selector].setLabelText_Center(menuItemText, gameHD.skin.getFont("uiFont"), 
                      viewWidthMain);
                    
                    // Exit selector.
                    break;

                case OPTIONS_MENU_MUSIC:

                    // User clicked music button.

                    // Reverse music flag.
                    gameHD.getConfig().getOptions().MusicReverse();

                    // Get base text to display.
                    menuItemText = gameHD.getConfig().getOptions().getMusicText();
                    
                    // If current menu item via key navigation, then...
                    if (menu_selector_keys == OPTIONS_MENU_MUSIC)
                        
                        // Current menu item via key navigation.
                        // Adjust text.
                        menuItemText = "[ " + menuItemText + " ]";
                    
                    // Update and recenter label.
                    menuLabels[menu_selector].setLabelText_Center(menuItemText, gameHD.skin.getFont("uiFont"), 
                      viewWidthMain);
                    
                    // Exit selector.
                    break;

                case OPTIONS_MENU_SOUNDS:

                    // User clicked sounds button.
                    
                    // Reverse sounds flag.
                    gameHD.getConfig().getOptions().SfxReverse();
                    
                    // Get base text to display.
                    menuItemText = gameHD.getConfig().getOptions().getSfxText();
                    
                    // If current menu item via key navigation, then...
                    if (menu_selector_keys == OPTIONS_MENU_SOUNDS)
                        
                        // Current menu item via key navigation.
                        // Adjust text.
                        menuItemText = "[ " + menuItemText + " ]";
                    
                    // Update and recenter label.
                    menuLabels[menu_selector].setLabelText_Center(menuItemText, gameHD.skin.getFont("uiFont"), 
                      viewWidthMain);

                    // Exit selector.
                    break;
                    
                case OPTIONS_MENU_MINIMAP:

                    // User clicked minimap button.
                    
                    // Reverse minimap flag.
                    gameHD.getConfig().getOptions().MinimapReverse();
                    
                    // Get base text to display.
                    menuItemText = gameHD.getConfig().getOptions().getMinimapText();
                    
                    // If current menu item via key navigation, then...
                    if (menu_selector_keys == OPTIONS_MENU_MINIMAP)
                        
                        // Current menu item via key navigation.
                        // Adjust text.
                        menuItemText = "[ " + menuItemText + " ]";
                    
                    // Update and recenter label.
                    menuLabels[menu_selector].setLabelText_Center(menuItemText, gameHD.skin.getFont("uiFont"), 
                      viewWidthMain);

                    // Exit selector.
                    break;
                    
                case OPTIONS_MENU_BACK:

                    // User clicked back button.

                    // Update the menu list to contain those of the main menu.
                    // Calling the function also builds / displays the menu.
                    title_set_menu(TITLE_MENU_MAIN);

                    // Exit selector.
                    break;

                default:

                    // Unknown options menu button selected.
                    System.out.println("Warning:  Unknown options button selected!");

                    // Exit selector.
                    break;

                } // Depending on selection...
            
            }
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt) 
    {   
        
        /*
        The function occurs during the update phase (render method) and accomplishes the following:
         */
        
        // Loop through Label objects in removal list.
        removeList.forEach((customLabel) -> {
            // Remove the Label from its Stage and parent list.
            customLabel.remove();
        });
        
        // Clear Label removal list.
        removeList.clear();
        
    }
    
    public void wakeScreen()
    {
        
        // The method gets called when redisplaying the already initialized screen.
        
        // Wake up the base screen, setting up viewports, input multiplexer, ....
        wakeBaseScreen();
        
        // Perform additional logic related to startup / create phase, including configuration and addition
        // of actors to stage.
        create();
        
    }
    
    // Handle discrete key events.
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        
        // 1.  
        
        int menu_selector_keys_prev; // Previously navigated to menu item.  Base 0.
        String menu_text_prev; // Text from previously navigated to menu item (contains brackets).
        
        // InputProcessor methods for handling discrete input.
            
        // Depending on key pressed, ...
        switch (keycode)
        {
            // If the user pressed the S key, then...
            case Keys.S:
                
                // The user pressed the S key.
            
                // Switch to the game screen.
                gameHD.setGameScreen(false);
                
                // Exit checks.
                break;
                
            // If the user pressed the up arrow key, then...
            case Keys.UP:
                
                // The user pressed the up arrow key.
                
                // Store previously navigated to menu item.
                menu_selector_keys_prev = menu_selector_keys;
                
                // Adjust navigation selection.
                menu_selector_keys--;
                
                // If past beginning of selections, then...
                if (menu_selector_keys < 0)
                    // Past beginning of selections.
                    // Adjust navigation selection.
                    menu_selector_keys = menu.length - 1;
                
                // Store text of previously navigated to menu item.
                menu_text_prev = menuLabels[menu_selector_keys_prev].getLabelText();
                
                // Remove brackets from previously navigated to menu item.
                menu_text_prev = menu_text_prev.substring(2, menu_text_prev.length() - 2);
                
                // Adjust text of menu items to reflect navigation.
                menuLabels[menu_selector_keys_prev].setLabelText_Center(menu_text_prev, 
                  gameHD.skin.getFont("uiFont"), viewWidthMain);
                menuLabels[menu_selector_keys].setLabelText_Center("[ " + 
                  menuLabels[menu_selector_keys].getLabelText() + " ]", 
                  gameHD.skin.getFont("uiFont"), viewWidthMain);
                
                // Exit checks.
                break;
                
            // If the user pressed the down arrow key, then...
            case Keys.DOWN:
                
                // The user pressed the down arrow key.
                
                // Store previously navigated to menu item.
                menu_selector_keys_prev = menu_selector_keys;
                
                // Adjust navigation selection.
                menu_selector_keys++;
                
                // If past beginning of selections, then...
                if (menu_selector_keys == menu.length)
                    // Past end of selections.
                    // Adjust navigation selection.
                    menu_selector_keys = 0;
                
                // Store text of previously navigated to menu item.
                menu_text_prev = menuLabels[menu_selector_keys_prev].getLabelText();
                
                // Remove brackets from previously navigated to menu item.
                menu_text_prev = menu_text_prev.substring(2, menu_text_prev.length() - 2);
                
                // Adjust text of menu items to reflect navigation.
                menuLabels[menu_selector_keys_prev].setLabelText_Center(menu_text_prev, 
                  gameHD.skin.getFont("uiFont"), viewWidthMain);
                menuLabels[menu_selector_keys].setLabelText_Center("[ " + 
                  menuLabels[menu_selector_keys].getLabelText() + " ]", 
                  gameHD.skin.getFont("uiFont"), viewWidthMain);
                
                // Exit checks.
                break;
                
            // If the user pressed the enter / return key, then...
            case Keys.ENTER:
                
                // The user pressed the enter / return key.
                
                // Select the item -- emulate a mouse click.
                menu_selector = menu_selector_keys;
                titleLogic();
                
            // Otherwise...
            default:
                
                // Exit checks.
                break;
                
        }
        
        // If the user pressed the S key, then...
        if (keycode == Keys.S)
        {
            // The user pressed the S key.
            
            // Play coin sound.
            gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_COIN);
            
            // Switch to the game screen.
            gameHD.setGameScreen(false);
        }
        
        // Return a value.
        return false;
        
    }
    
}
