package heroinedusk;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import core.AssetMgr;
import core.BaseActor;
import core.BaseGame;
import core.BaseScreen;
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

public class DialogScreen extends BaseScreen { // Extends the BaseScreen class.
    
    /*
    The class extends the basic functionality of a BaseScreen class and sets up a dialog screen.

    Methods include:

    create:  Calls constructor for BaseScreen and configures and adds objects in the dialog screen.
    dispose:  Called when removing the screen and allows for clearing of related resources from memory.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    wakeScreen:  Called when redisplaying the already initialized screen.
    */
    
    // Declare object variables.
    private AssetMgr assetMgr; // Enhanced asset manager.
    private BaseActor background; // BaseActor object that will act as the background.
    private CustomProgressBar progressBar; // Reference to custom progress bar object.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private ArrayList<Label> removeList; // Array of Label objects to remove from screen.
    
    // Declare regular variables.
    final private boolean initialized; // Whether screen initialized.
    private int windowHeight; // Application window height.
    private int windowWidth; // Application window width.
    
    // Game world dimensions.
    private int mapWidth; // Total map width, in pixels.
    private int mapHeight; // Total map height, in pixels.
    
    // g = Reference to base game.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    // hdg = Reference to Heroine Dusk (main) game.
    public DialogScreen(BaseGame g, int windowWidth, int windowHeight, HeroineDuskGame hdg)
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
    
    public final void create()
    {
        
        /* 
        The function occurs during the startup / create phase and accomplishes the following:
        
        
        */
        
        String backgroundKey; // Key to background image in hash map (in asset manager).
        
        // 1.  Set defaults.
        
        // 2.  Initialize arrays and array lists.
        removeList = new ArrayList<>();
        
        // 3.  Configure and add the background Actor.
        
        // Create new BaseActor for the background.
        background = new BaseActor();
        
        // Name background actor.
        background.setActorName("Background");
        
        // Get key to background image in hash map in asset manager.
        backgroundKey = gameHD.shopInfo.shopList.get(gameHD.dialog.shop_id.getValue()).getBackground().getValue_Key();
        
        // Assign the Texture to the background Actor.
        background.setTexture(gameHD.assetMgr.getImage_xRef(backgroundKey));
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        background.setPosition( 0, 0 );
        
        // Add the background Actor to the scene graph.
        mainStage.addActor( background );
        
        System.out.println("Title: " + gameHD.dialog.title);
        
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
        
        // Configure and add the actors to the stage:  background, ground, planes, and stars.
        create();
        
    }
    
}