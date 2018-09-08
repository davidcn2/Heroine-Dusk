package heroinedusk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import core.*;
import java.util.ArrayList;
import routines.*;

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
    render:  Called when the screen should render itself.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    */
    
    // Declare object variables.
    private Config config; // Configuration information, including options.
    private CustomProgressBar progressBar; // Reference to custom progress bar object.
    
    // Declare regular variables.
    
    // Game world dimensions.
    final int mapWidth; // Total map width, in pixels.
    final int mapHeight; // Total map height, in pixels.
    
    // g = Screen object for game window.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    public GameScreen(BaseGame g, int windowWidth, int windowHeight)
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
        
        // Configure and add the actors to the stage:  background, ground, planes, and stars.
        create();
        
    }
    
    public final void create()
    {
        
        /* 
        The function occurs during the startup / create phase and accomplishes the following:
        
        
        */
        
        // 1.  Initialize configuration information, including options.
        config = new Config(mapWidth, mapHeight);
        
        // 2.  Initialize the custom progress bar.
        progressBar = new CustomProgressBar(game.skin);
        
        
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt) 
    {   
        
        /*
        The function occurs during the update phase (render method) and accomplishes the following:
        
        
        */
        
        
        
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
            game.setScreen( new GameScreen(game, config.getViewWidth(), config.getViewHeight()) );
        }
            
        // If the user pressed the Escape key, then...
        if (keycode == Keys.ESCAPE)
        {
            // The user pressed the Escape key.
            
            // Exit the game.
            Gdx.app.exit();
        }
        
        // Return a value.
        return false;
        
    }
        
}