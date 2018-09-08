package heroinedusk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import core.BaseActor;
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

public class CustomLabel
{
    
    /* 
    The class allows for easy implementation of a label.
    
    Methods include:
    
    addAction_FadePartial:  Sets up a color fade effect for the label.
    displayLabel:  Displays the label at the passed coordinates.
    */
    
    // Declare object variables.
    Map<String, Action> actionMapping; // Collection of actions applied to label.

    private Label customLabel; // LibGDX Label object that will display text.
    
    // Declare regular variables.
    private int labelScale; // Scale to use when displaying label.
    private String labelStyle; // Name of style to use with label.
    private String labelText; // Text to display in label.
    private int posX; // X-coordinate for placement of the label.
    private int posY; // Y-coordinate for placement of the label.
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // labelStyle = Name of the style to use.
    // labelScale = Scale to use when displaying label.
    public CustomLabel(Skin gameSkin, String labelText, String labelStyle, int labelScale)
    {
        
        // The constructor creates a label.
        // Example for use:  labelTitle = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2);
        
        // Initialize the hash map for actions.
        actionMapping = new HashMap<>();
        
        // Store values passed to function.
        this.labelText = labelText;
        this.labelStyle = labelStyle;
        this.labelScale = labelScale;
        
        // Set up Label object using passed properties.
        // Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.
        customLabel = new Label( labelText, gameSkin, labelStyle ); // Add text and style to Label.
        customLabel.setFontScale(labelScale); // Make font appear larger by using setFontScale method.
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // labelStyle = Name of the style to use.
    public CustomLabel(Skin gameSkin, String labelText, String labelStyle)
    {
        
        // The constructor creates a label.
        // Example for use:  labelTitle = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2);
        
        // Initialize the hash map for actions.
        actionMapping = new HashMap<>();
        
        // Store values passed to function.
        this.labelText = labelText;
        this.labelStyle = labelStyle;
        this.labelScale = 1;
        
        // Set up Label object using passed properties.
        // Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.
        customLabel = new Label( labelText, gameSkin, labelStyle ); // Add text and style to Label.
        customLabel.setFontScale(1); // Make font appear larger by using setFontScale method.
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    public CustomLabel(Skin gameSkin, String labelText)
    {
        
        // The constructor creates a label.
        // Example for use:  labelTitle = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2);
        
        // Initialize the hash map for actions.
        actionMapping = new HashMap<>();
        
        // Store values passed to function.
        this.labelText = labelText;
        this.labelStyle = "uiLabelStyle";
        this.labelScale = 1;
        
        // Set up Label object using passed properties.
        // Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.
        customLabel = new Label( labelText, gameSkin, "uiLabelStyle" ); // Add text and style to Label.
        customLabel.setFontScale(1); // Make font appear larger by using setFontScale method.
        
    }
    
    public void addAction_FadePartial()
    {
        
        // The function sets up a color fade effect for the label.
        
        // Add action to hash map.
        actionMapping.putIfAbsent("FadePartial", 
          Actions.forever(
            Actions.sequence(
              Actions.color( new Color(1, 1, 0, 1), 0.5f ),
              Actions.delay( 0.5f ),
              Actions.color( new Color(0.5f, 0.5f, 0, 1), 0.5f )
            )));
        
        // Set up color pause effect for the label.
        customLabel.addAction(actionMapping.get("FadePartial"));
        
    }
    
    // posX = X-coordinate for placement of the label.
    // posY = Y-coordinate for placement of the label.
    public Label displayLabel(int posX, int posY)
    {
        
        // The function displays the label at the passed coordinates.
        // Example for use:  mainStage.addActor(labelTitle.displayLabel(200, 200));
        
        // Store location of label.
        this.posX = posX;
        this.posY = posY;
        
        // Set the location of the label.
        customLabel.setPosition(posX, posY);
        
        // Return the label.
        return customLabel;
        
    }
    
}
