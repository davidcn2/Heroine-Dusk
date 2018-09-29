package screens;

// Local project imports.
import core.BaseActor;
import core.BaseScreen;
import gui.CustomLabel;
import heroinedusk.HeroineDuskGame;
import heroinedusk.HeroineEnum;
import heroinedusk.MazeMap;

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

public class ExploreScreen extends BaseScreen { // Extends the BaseScreen class.

    /*
    The class extends the basic functionality of a BaseScreen class and sets up the introduction screen.

    Methods include:

    ...
    */
    
    // Declare object variables.
    private BaseActor background; // BaseActor object that will act as the background.
    private CustomLabel facingLabel; // Label showing direction player is facing.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private BaseActor infoButton; // BaseActor object that will act as the information button.
    private MazeMap mazemap; // Stores data for the current active region / map.
    private ArrayList<BaseActor> tiles; // BaseActor objects associated with tiles.
    
    // Declare regular variables.
    
    // Declare constants.
    
    // hdg = Reference to Heroine Dusk (main) game.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    public ExploreScreen(HeroineDuskGame hdg, int windowWidth, int windowHeight)
    {
        
        // The constructor of the class:
        
        // 1.  Calls the constructor for the BaseScreen (parent / super) class.
        // 2.  Calls the create() function to perform remaining startup logic.
        
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
        
        1.  Sets defaults.
        ...
        */
        
        // 1.  Set defaults.
        
        // 2.  Initialize arrays and array lists.
        tiles = new ArrayList<>();
        
        // 3.  Initialize the maze map.
        mazemap = new MazeMap(gameHD);
        
        // 4.  Configure and add the background Actor.
        
        // Note:  The application uses a starting background of tempest, which the foreground objects completely hide.
        
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
        
        // 5.  Render current map location / tiles.
        
        // Store array list with base actors for tiles to display.
        tiles = mazemap.mazemap_render(gameHD.getAvatar().getX(), gameHD.getAvatar().getY(), 
          gameHD.getAvatar().getFacing());
        
        // Loop through base actors in array list.
        tiles.forEach((actor) -> {
            
            // Add the tile Actor to the scene graph.
            mainStage.addActor( actor );
        
        });
        
        // 6.  Configure and add the label with the direction the player is facing.
        
        // Initialize and add label with facing text.
        facingLabel = new CustomLabel(game.skin, gameHD.getAvatar().getFacing().toString(), "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), CustomLabel.AlignEnum.ALIGN_CENTER, 
          CustomLabel.PosRelativeEnum.REL_POS_UPPER_LEFT, mainStage, null, (float)(gameHD.getConfig().getScale() * -2), 
          HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0f);
        
        // 7.  Configure and add the information button Actor.
        
        // Create and configure new BaseActor for the information button.
        
        // To Do:  Switch to atlas to get non-selected button.
        
        // Pos X = (140 + 2) * scale factor.  2 = Offset.
        // Pos Y = (0 + 2) * scale factor.  2 = Offset.
        infoButton = new BaseActor( "info button", 
          gameHD.getAssetMgr().getImage_xRef(HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_INFO_BTN.getValue_Key()), 
          (140 + 2) * gameHD.getConfig().getScale(), 2 * gameHD.getConfig().getScale() );
        
        // Add the information button Actor to the scene graph.
        mainStage.addActor( infoButton );
        
    }
    
    @Override
    public void dispose()
    {
        
        // The method is called when removing the screen and allows for clearing of related resources 
        // from memory.
        
        // Call manual dispose method in superclass.
        super.disposeManual();
        
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
        
        // The method gets called when redisplaying the already initialized screen.
        
        // Wake up the base screen, setting up viewports, input multiplexer, ....
        wakeBaseScreen();
        
        // Configure and add the actors to the stage:  background, ground, planes, and stars.
        //create();
        
    }
    
}
