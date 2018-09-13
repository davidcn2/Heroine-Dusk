package heroinedusk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import core.*;

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

public class GameScreen extends BaseScreen // Extends the BaseScreen class.
{

    /* 
    The class extends the basic functionality of a BaseScreen class and stores information
    about the game -- background, player, key, coins, door, and wall and related properties
    (map height and width, timers, speeds, ...).
    
    Methods include:
    
    create:  Sets defaults / lists.  Configures and adds the Actors to the stage.  Sets up TiledMap.
    dispose:  Called when removing the screen and allows for clearing of related resources from memory.
    recreateDisplay:  Called when redisplaying the already initialized screen.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    wakeScreen:  Called when redisplaying the already initialized screen.
    */
    
    // Declare object variables.
    private AssetMgr assetMgr; // Enhanced asset manager.
    // private Config config; // Configuration information, including options.
    private CustomProgressBar progressBar; // Reference to custom progress bar object.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    
    // Declare regular variables.
    private float timeElapsed; // Number of seconds elapsed since game started.
    
    // Game world dimensions.
    private int mapWidth; // Total map width, in pixels.
    private int mapHeight; // Total map height, in pixels.
    private int windowHeight; // Application window height.
    private int windowWidth; // Application window width.
    
    // g = Reference to base game.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    // hdg = Reference to Heroine Dusk (main) game.
    public GameScreen(BaseGame g, int windowWidth, int windowHeight, HeroineDuskGame hdg)
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
        
    }
    
    public final void create()
    {
        
        /* 
        The function occurs during the startup / create phase and accomplishes the following:
        
        
        */
        
        // 1.  Initialize configuration information, including options.
        //config = new Config(mapWidth, mapHeight);
        
        // 2.  Initialize the asset manager.
        assetMgr = new AssetMgr();
        
        // 3.  Initialize the custom progress bar.
        progressBar = new CustomProgressBar(game.skin);
        
        // 4.  Load assets.
        
        // 4a.  Display progress bar in center of screen -- update when loading assets.
        mainStage.addActor(progressBar.displayBarCenter(viewWidthMain, viewHeightMain));
        
        // 4b.  Queue images.
        assetMgr.queueImages("assets/interface/heroine.png");
        assetMgr.mapImages("assets/interface/heroine.png", "heroine");
        
        // 4c.  Load resources and update progress bar.
        assetMgr.loadResources(progressBar);
        
        // 4d.  Hide the progress bar.
        progressBar.hideBar();
        
        
        // Temporary...
        recreateDisplay();
        
        /*
        CustomLabel instructions; // LibGDX Label object that will display main menu text.
        instructions = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2.0f);
        instructions.addAction_FadePartial();
        mainStage.addActor(instructions.displayLabel(200, 200));
        */
        
        /*
        BaseActor testActor;
        Texture testTex;
        testActor = new BaseActor();
        testTex = assetMgr.getImage_xRef("heroine");
        testTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        testActor.setTexture(testTex);
        testActor.setPosition(50, 50);
        mainStage.addActor(testActor);
        */
        
        /*
        // Set up Label object that will display main menu instructions.
        // Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.
        instructions = new Label( "This is a test", game.skin, "uiLabelStyle" ); // Add text and style to Label.
        instructions.setFontScale(2); // Make font appear larger by using setFontScale method.
        instructions.setPosition(300, 250); // Set coordinates of the Label.
        
        // Set up color pause effect for main menu instructions text.
        instructions.addAction(
          Actions.forever(
            Actions.sequence(
              Actions.color( new Color(1, 1, 0, 1), 0.5f ),
              Actions.delay( 0.5f ),
              Actions.color( new Color(0.5f, 0.5f, 0, 1), 0.5f )
            )));
        */
        
        //mainStage.addActor( instructions ); // Add main menu instructions Label to the scene graph.
        //uiStage.addActor( instructions ); // Add main menu instructions Label to the scene graph.
        //System.out.println("Instructions");
        
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
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt) 
    {   
        
        /*
        The function occurs during the update phase (render method) and accomplishes the following:
        
        1.  Updates the time elapsed value.
        */
        
        // Update time elapsed value.
        timeElapsed += dt;
        
    }

    private void recreateDisplay()
    {
        
        // The method get called to add back the actors, music, and other elements related to
        // the current screen when switching from another.  Assumes earlier construction.
        
        CustomLabel instructions; // LibGDX Label object that will display main menu text.
        instructions = new CustomLabel(game.skin, "GAME SCREEN", "uiLabelStyle", 2);
        //instructions.addAction_FadePartial();
        mainStage.addActor(instructions.displayLabel(200, 400));
        
        BaseActor testActor;
        Texture testTex;
        testActor = new BaseActor();
        testTex = assetMgr.getImage_xRef("heroine");
        testTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        testActor.setTexture(testTex);
        testActor.setPosition(50, 50);
        mainStage.addActor(testActor);
        
    }
    
    public void wakeScreen()
    {
        
        // The method gets called when redisplaying the already initialized screen.
        
        // Wake up the base screen, setting up viewports, input multiplexer, ....
        wakeBaseScreen();
        
        // Add back actors, music, and other elements to the current screen.
        recreateDisplay();
        
    }
    
    // Handle discrete key events.
    
    // keycode = Code for key pressed.
    @Override
    public boolean keyDown(int keycode)
    {
        
        // The function gets called when the user presses a key.
        
        // 1.  Pauses the game when pressing the P key.
        // 2.  Resets the game when pressing the R key.
        // 4.  Exits the game when pressing the Escape key.
        
        // InputProcessor methods for handling discrete input.
        
        // If the user pressed the P key, then...
        if (keycode == Keys.P)
        {
            // The user pressed the P key.
            
            // Pause the game.
            togglePaused();
        }

        // If the user pressed the R key, then...
        if (keycode == Keys.R)
        {
            // The user pressed the R key.
            
            // Reset the game.
            //game.setScreen( new TitleScreen(game, viewWidthMain, viewHeightMain) );
        }
           
        // If the user pressed the S key, then...
        if (keycode == Keys.S)
        {
            // The user pressed the S key.
            
            // Switch to the title screen.
            gameHD.setTitleScreen();
        }
        
        // If the user pressed the Escape key, then...
        if (keycode == Keys.ESCAPE)
        {
            // The user pressed the Escape key.
            
            // Dispose of screen related LibGDX objects.
            gameHD.disposeScreens();
            
            // Exit the game.
            Gdx.app.exit();
        }
        
        // Return a value.
        return false;
        
    }
        
}