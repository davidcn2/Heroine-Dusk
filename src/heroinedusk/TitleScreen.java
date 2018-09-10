package heroinedusk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import core.AssetMgr;
import core.BaseActor;
import core.BaseGame;
import core.BaseScreen;
import routines.ArrayRoutines;

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

    create:  Calls constructor for BaseScreen and configures and adds objects in the title screen.
    dispose:  Called when removing the screen and allows for clearing of related resources from memory.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    wakeScreen:  Called when redisplaying the already initialized screen.
    */
    
    // Declare object variables.
    private AssetMgr assetMgr; // Enhanced asset manager.
    private BaseActor background; // BaseActor object that will act as the background.
    private CustomLabel menuLabels[]; // Collection of menu labels.
    private CustomProgressBar progressBar; // Reference to custom progress bar object.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    
    // Declare regular variables.
    final private boolean initialized; // Whether screen initialized.
    private boolean loadedGame; // Whether a loaded game.
    private int menu_id; // Menu type displayed.
    private int menu_selector; // Menu item selected.  Base 0.
    private String menu[]; // List of current menu selections / text.
    private int windowHeight; // Application window height.
    private int windowWidth; // Application window width.
    
    // Declare constants.
    private final int TITLE_MENU_MAIN = 0;
    private final int TITLE_MENU_OPTIONS = 1;
    
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
        
        // Store reference to main game class.
        gameHD = hdg;
        
        // Configure and add the actors to the stage:  background, ground, planes, and stars.
        create();
        
        // Update as initialized.
        this.initialized  = true;
        
    }
    
    // customLabel = Reference to the label to which to add the event.
    // label_id = Menu item number for which to add event.  Base 0.
    public void addEvent(CustomLabel customLabel, int label_id)
    {
        
        InputListener labelEvent; // Event to add to passed label.
        
        // Craft event logic to add to passed label.
        labelEvent = new InputListener()
            {
                
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                // Occurs when user press down mouse on label.
                // Event (touchDown) necessary to reach touchUp.
                    
                // Return a value.
                return true;
                }
                
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                {
                    System.out.println("touchUp");
                    menu_selector = label_id;
                    titleLogic();
                }
                
            };
        
        // Add event to label.
        customLabel.addEvent(labelEvent);
        
    }
    
    private void buildMenu()
    {
        
        // The function builds the current menu and adds its objects to the scene graph.
        
        // Declare regular variables.
        int counter; // Used to iterate through menu items.
        int menuTop; // Y-coordinate at which to place first item in menu.
        
        // Set defaults.
        counter = 0;
        
        // Determine y-coordinate of top menu item.
        menuTop = gameHD.config.getMenuTop();
        
        // Loop through menu items.
        for (String menuItem : menu)
        {
            // Initialize label with text from current menu item.
            menuLabels[counter] = new CustomLabel(game.skin, menuItem, "uiLabelStyle", 1.25f);
            
            // Add label to scene graph.
            mainStage.addActor(menuLabels[counter].displayLabelCenterX(menuTop - counter * 60, viewWidthMain));
            
            // Add event to label.
            addEvent(menuLabels[counter], counter);
            
            // Increment counter.
            counter++;
        }
        
    }
    
    public final void create()
    {
        
        /* 
        The function occurs during the startup / create phase and accomplishes the following:
        
        
        */
        
        // Declare object variables.
        Texture backgroundTex; // Texture used when loading image for background.
        
        // 1.  Set defaults.
        this.menu_id = -1;
        this.loadedGame = false;
        
        // 2.  Initialize arrays.
        this.menu = new String[4];
        
        // 3.  Update the menu list to contain those of the main menu.
        title_set_menu(TITLE_MENU_MAIN);
        
        // If NOT initialized yet, then...
        if (initialized == false)
        
        {
            // NOT initialized yet.
            
            // 4.  Initialize the asset manager.
            assetMgr = new AssetMgr();

            // 5.  Initialize the custom progress bar.
            progressBar = new CustomProgressBar(game.skin);

            // 6.  Load assets.
            loadAssets();
        }
        
        // 7.  Configure and add the background Actor.
        
        // Create new BaseActor for the background.
        background = new BaseActor();
        
        // Name background actor.
        background.setActorName("Background");
        
        // Assign the Texture to the background Actor.
        background.setTexture(assetMgr.getImage_xRef("title"));
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        background.setPosition( 0, 0 );
        
        // Add the background Actor to the scene graph.
        mainStage.addActor( background );
        
        // 8.  Configure and add the objects related to the main menu.
        
        /*
        CustomLabel instructions; // LibGDX Label object that will display main menu text.
        instructions = new CustomLabel(game.skin, "Start", "uiLabelStyle", 1);
        instructions.addEvent();
        mainStage.addActor(instructions.displayLabelCenterX(menuTop, viewWidthMain));
        */
        buildMenu();
        
        
    }
    
    @Override
    public void dispose()
    {
        
        // The method is called when removing the screen and allows for clearing of related resources 
        // from memory.

        // Call manual dispose method in superclass.
        super.disposeManual();
        
        // Clear LibGDX objects in other classes.
        assetMgr.disposeAssetMgr();
        
    }
    
    private void loadAssets()
    {
        
        // The function loads the assets based on the current size of the application window.
        // Nothing occurs if assets already loaded for the current size.
        
        // Declare constants.
        final String imageKey = "title"; // Key to associate with image.
        
        // Declare regular variables.
        String imagePath; // Path to image to load.
        
        // If using a manually scaled size, then...
        if (gameHD.config.getStretchToScreen())
            
            {
            // Using a manually scaled size.
            
            // Specify image path.
            imagePath = "assets/backgrounds/title.png";
            }
        
        else
            
            {
            // Using a prescaled size.
            
            // Specify image path.
            imagePath = gameHD.config.getPrescaleFolder_Backgrounds() + "title.png";
            }
        
        // 1.  Display progress bar in center of screen -- update when loading assets.
        mainStage.addActor(progressBar.displayBarCenter(viewWidthMain, viewHeightMain));
        
        // 2.  Queue images.
        assetMgr.queueImages(imagePath);
        assetMgr.mapImages(imagePath, imageKey);
        
        // 3.  Load resources and update progress bar.
        assetMgr.loadResources(progressBar);
        
        // 4.  Hide the progress bar.
        progressBar.hideBar();
        
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
        
        // Reset to first possible menu selection.
        menu_selector = 0;
  
        // Update menu type to the one passed.
        menu_id = id;

        // Reset array containing menu options.
        menu = new String[4];
  
        // If displaying main menu, then...
        if (id == TITLE_MENU_MAIN) 
            {
            // Displaying main menu.
            ArrayRoutines.addAll(menu, (loadedGame ? "Continue" : "Start"), "Options", "Load", "Save");
            }
  
        // Otherwise, if displaying options menu, then...
        else if (id == TITLE_MENU_OPTIONS) 
            {
            // Displaying options menu.
                
            if (gameHD.config.options.getAnimationsOn())
                menu[0] = "Animations are on";
            else
                menu[0] = "Animations are off";
            
            if (gameHD.config.options.getMusicOn())
                menu[1] = "Music is on";
            else
                menu[1] = "Music is off";

            if (gameHD.config.options.getSfxOn())
                menu[2] = "Sounds are on";
            else
                menu[2] = "Sounds are off";

            if (gameHD.config.options.getMinimapOn())
                menu[3] = "Sounds are on";
            else
                menu[3] = "Sounds are off";
    
            menu[4] = "Back";
            }
        
        // Initialize menu labels array.
        menuLabels = new CustomLabel[menu.length];
        
    }
    
    private void titleLogic()
    {
        System.out.println("Selected label " + menu_selector + ": " + menu[menu_selector]);
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt) 
    {   
        
        /*
        The function occurs during the update phase (render method) and accomplishes the following:
        
        
        */
        
        
    }
    
    public void wakeScreen()
    {
        
        // The method gets called when redisplaying the already initialized screen.
        
        // Wake up the base screen, setting up viewports, input multiplexer, ....
        wakeBaseScreen();
        
        // Configure and add the actors to the stage:  background, ground, planes, and stars.
        create();
        
    }
    
    // Handle discrete key events.
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        
        // 1.  
        
        // InputProcessor methods for handling discrete input.
            
        // If the user pressed the S key, then...
        if (keycode == Keys.S)
        {
            // The user pressed the S key.
            
            // Switch to the game screen.
            gameHD.setGameScreen();
        }
        
        // Return a value.
        return false;
        
    }
    
}
