package gui;

// LibGDX imports.
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

// Java imports.
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

public class CustomProgressBar 
{
    
    /* 
    The class allows for easy implementation of a progress bar.
    
    Methods include:
    
    addAction_Fade:  Sets up a fade to nothing effect to use later with the progress bar.
    displayBar:  Displays the progress bar at the passed coordinates.  Used when first displaying the bar.
    displayBarCenter:  Displays the progress bar in the center of the application window.
    displayBarCenterHorz:  Displays the progress bar in the center of the application window.
    hideBar:  Uses a fading action to hide the progress bar.
    setBarHeight:  Sets the height of the progress bar.
    setBarWidth:  Sets the width of the progress bar.
    setValue:  Sets the value of the progress bar.
    */
    
    // Declare object variables.
    private Map<String, Action> actionMapping; // Collection of actions applied to progress bar.
    private final ProgressBar progressBar; // Reference to the LibGDX progress bar.
    
    // Declare regular variables.
    private int barHeight; // Height of the bar.
    private int barWidth; // Width of the bar.
    private float barValue; // Value of the bar (between minimum and maximum of 0.0f and 1.0f).
    private int barX; // X-coordinate for placement of the bar.
    private int barY; // Y-coordinate for placement of the bar.
    private boolean vertical; // Whether to display a vertical (true) or horizontal (false) progress bar.
    
    // gameSkin = Reference to skin used with the game.
    public CustomProgressBar(Skin gameSkin)
    {
        
        // The constructor creates a progress bar.
        // Example for use:  progressBar = new CustomProgressBar(game.skin);
        
        // Set defaults.
        vertical = false;
        barWidth = 100;
        barHeight = 20;
        
        // Initialize the hash map for actions.
        actionMapping = new HashMap<>();
        
        // 1.  Minimum = 0.0f.
        // 2.  Maximum = 1.0f.
        // 3.  Step Size = 0.01f.
        // 4.  Vertical flag -- default to horizontal (false).
        // 5.  Reference to the skin -- game.skin.
        // 6.  Name of bar style to reference within skin.
        progressBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, gameSkin, "barStyle");
        
        // 7.  Specify the starting value.
        progressBar.setValue(0.0f);
        
        // 8.  Specify animation duration.
        progressBar.setAnimateDuration(0.25f);
        
        // 9.  Adds a fade action.
        addAction_Fade();
        
    }
    
    private void addAction_Fade()
    {
        
        // The function sets up a fade to nothing effect to use later with the progress bar.
        
        // Add action to hash map.
        actionMapping.putIfAbsent("Fade", 
            Actions.sequence(
              Actions.fadeOut(0.5f), 
              Actions.visible(false)
            ));
        
    }
    
    // barX = X-coordinate at which to place the progress bar.
    // barY = Y-coordinate at which to place the progress bar.
    public ProgressBar displayBar(int barX, int barY)
    {
        
        // The function displays the progress bar at the passed coordinates.
        // Used when first displaying the bar.
        // Example for use:  mainStage.addActor(progressBar.displayBar(200, 200));
        
        // Store location of progress bar.
        this.barX = barX;
        this.barY = barY;
        
        // Set the location and dimensions of the progress bar.
        progressBar.setBounds(barX, barY, barWidth, barHeight);
        
        // Return the progress bar.
        return progressBar;
        
    }
    
    // windowWidth = Width of the application window.
    // windowHeight = Height of the application window.
    public ProgressBar displayBarCenter(int windowWidth, int windowHeight)
    {
        
        // The function displays the progress bar in the center of the application window.
        // Example for use (1):  mainStage.addActor(progressBar.displayBarCenter(800, 600));
        // Example for use (2):  mainStage.addActor(progressBar.displayBarCenter(config.getViewWidth(), config.getViewHeight()));
        
        // Store location of progress bar -- using the center of the application window.
        barX = windowWidth / 2 - barWidth / 2;
        barY = windowHeight / 2 - barHeight / 2;
        
        // Set the location and dimensions of the progress bar.
        progressBar.setBounds(barX, barY, barWidth, barHeight);
        
        // Return the progress bar.
        return progressBar;
        
    }
    
    // windowWidth = Width of the application window.
    // barY = Y-coordinate for placement of the bar.
    public ProgressBar displayBarCenterHorz(int windowWidth, int barY)
    {
        
        // The function displays the progress bar in the center of the application window.
        // Example for use (1):  mainStage.addActor(progressBar.displayBarCenter(800, 600));
        // Example for use (2):  mainStage.addActor(progressBar.displayBarCenter(config.getViewWidth(), config.getViewHeight()));
        
        // Store x-coordinate of progress bar -- using the center of the application window.
        barX = windowWidth / 2 - barWidth / 2;
        
        // Set the location and dimensions of the progress bar.
        progressBar.setBounds(barX, barY, barWidth, barHeight);
        
        // Return the progress bar.
        return progressBar;
        
    }
    
    public void hideBar()
    {
        
        // The function uses a fading action to hide the progress bar.
        
        // Implement fading action to hide the progress bar.
        progressBar.addAction(actionMapping.get("Fade"));
        
    }
    
    // Getters and setters below...
    
    // barHeight = Height to which to set progress bar.
    public void setBarHeight(int barHeight) {
        this.barHeight = barHeight;
    }

    // barWidth = Width to which to set progress bar.
    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }
    
    // barValue = Value to which to set progress bar.
    public void setValue(float barValue)
    {
        
        // The function sets the value of the progress bar and updates its display.
        
        // Update bar value.
        this.barValue = barValue;
        
        // Update value of the actual progress bar.
        progressBar.setValue(barValue);
        
    }
    
}
