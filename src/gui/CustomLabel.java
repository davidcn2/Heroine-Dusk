package gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import routines.UtilityRoutines;
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
    
    Example of steps to add to stage:
    
    CustomLabel mainMenu; // LibGDX Label object that will display main menu text.
    mainMenu = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2, 30.0f, "Key");
    mainMenu.addAction_FadePartial();
    mainStage.addActor(mainMenu.displayLabel(200, 200));
    
    Enumerations include:
    
    AlignEnum:  Enumerations related to text alignment.
    PosRelativeEnum:  Enumerations related to relative positioning.
    
    Methods include:
    
    addAction_Fade:  Sets up a fade effect for the label, fading out over the course of 1.0 seconds after 2.0 elapse.
    addAction_FadePartial:  Sets up a color fade effect for the label.
    addEvent:  Adds the passed event logic to the label.
    applyAction_Visible:  Depending on the passed parameter, either instantly displays or hides the label.
    centerLabel:  Centers a label -- useful when text changes.
    colorLabelDark:  Applies a dark shade to the label.
    colorLabelMedium:  Applies a medium shade to the label.
    colorLabelNormal:  Removes shading from the label.
    displayLabel:  Displays the label at the passed coordinates.
    displayLabelAlignRight:  Positions the label with its right edge at passed X and top at specified Y coordinate.
    displayLabelCenterX:  Centers the label at the passed Y coordinate.
    removeActions:  Removes all actions from the label.
    removeActor:  Removes the actor associated with the label.
    setLabelText:  Updates the text of the label, using the existing bitmap font.
    setLabelTextCenter:  Updates the text of the label and centers the revised object across the screen.
    */
    
    // Declare object variables.
    private Map<String, Action> actionMapping; // Collection of actions applied to label.
    private BitmapFont bitmapFont; // BitmapFont used for the label.
    private Label customLabel; // LibGDX Label object that will display text.
    
    // Declare regular variables.
    private float labelScale; // Scale to use when displaying label.
    private String labelStyle; // Name of style to use with label.
    private String labelText; // Text to display in label.
    private float posX; // X-coordinate for placement of the label.
    private float posY; // Y-coordinate for placement of the label.
    private float stageHeight; // Height of the stage.  Helpful for centering and relative positioning.
    private float stageWidth; // Width of the stage.  Helpful for centering and relative positioning.
    
    // Constructors below...
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // labelStyle = Name of the style to use.
    // labelScale = Scale to use when displaying label.
    // textLineHeight = Desired text line height.
    // fontSkinKey = Key to font in skin.
    public CustomLabel(Skin gameSkin, String labelText, String labelStyle, float labelScale, 
      float textLineHeight, String fontSkinKey)
    {
        
        // The constructor creates a label.
        // Example for use:  labelTitle = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2, 30.0f, "Key");
        
        // Initialize the hash map for actions.
        actionMapping = new HashMap<>();
        
        // Store values passed to function.
        this.labelText = labelText;
        this.labelStyle = labelStyle;
        this.labelScale = labelScale;
        
        // If key to font in skin passed (not null), then...
        if (fontSkinKey != null)
            bitmapFont = gameSkin.get(fontSkinKey, BitmapFont.class);
            
        // Set up Label object using passed properties.
        // Note:  Best practices include avoiding scaling -- use a high-resolution image, instead.
        customLabel = new Label( labelText, gameSkin, labelStyle ); // Add text and style to Label.
        customLabel.setFontScale(labelScale); // Make font appear larger by using setFontScale method.
        customLabel.setHeight(textLineHeight); // Set height of label.
        customLabel.layout(); // Computes and caches any information needed for drawing.
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // labelStyle = Name of the style to use.
    // textLineHeight = Desired text line height.
    // fontSkinKey = Key to font in skin.
    public CustomLabel(Skin gameSkin, String labelText, String labelStyle, float textLineHeight, 
      String fontSkinKey)
    {
        
        // The constructor creates a label.
        // Example for use:  labelTitle = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2, 30.0f);
        
        // Call original constructor using a scale of 1.0f.
        this(gameSkin, labelText, labelStyle, 1.0f, textLineHeight, fontSkinKey);
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // textLineHeight = Desired text line height.
    // fontSkinKey = Key to font in skin.
    public CustomLabel(Skin gameSkin, String labelText, float textLineHeight, String fontSkinKey)
    {
        
        // The constructor creates a label.
        // Example for use:  labelTitle = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2, 30.0f);
        
        // Call original constructor using a style of uiLabelStyle and scale of 1.0f.
        this(gameSkin, labelText, "uiLabelStyle", 1.0f, textLineHeight, fontSkinKey);
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // labelStyle = Name of the style to use.
    // labelScale = Scale to use when displaying label.
    // textLineHeight = Desired text line height.
    // align = Text alignment desired.  Maps to one of the AlignEnum values.
    // whichStage = Reference to stage to which to add label.
    // fontSkinKey = Key to font in skin.
    // elements = Provides values for posX, posY, and stageWidth, as needed, based on alignment.
    public CustomLabel(Skin gameSkin, String labelText, String labelStyle, float labelScale, 
      float textLineHeight, AlignEnum align, Stage whichStage, String fontSkinKey,
      float ... elements)
    {
        
        /*
        The constructor simplifies the addition of a label to the stage.
        Supports alignment of text.
        
        // Notes about elements...
        For left alignment, 0 = posX.
        For center alignment, 0 = posY.
        For right alignment, 0 = posX (rightmost edge), 1 = posY.
        
        posX = For left alignment, leftmost position of label.  Use null for centering (ignored).
               For right alignment, rightmost position of label.
        posY = Y-coordinate for placement of the label.
        stageWidth = Width of stage in which to center label.
        */

        // Initialize label with passed properties.
        this(gameSkin, labelText.toUpperCase(), labelStyle, labelScale, textLineHeight, fontSkinKey);
        
        // Store stage width and height.
        this.stageWidth = whichStage.getWidth();
        this.stageHeight = whichStage.getHeight();
        
        // Depending on alignment, ...
        switch (align)
        {
            case ALIGN_LEFT:
                
                // Align text to the left.
                
                // Add label to scene graph, aligning text to the left.
                whichStage.addActor(displayLabel(elements[0], elements[1]));
                
                // Exit selector.
                break;
                
            case ALIGN_CENTER:
                
                // Align text to the center.
                
                // Add label to scene graph, aligning text to the center.
                whichStage.addActor(displayLabelCenterX(elements[0], (int)this.stageWidth));
                
                // Exit selector.
                break;
                
            case ALIGN_RIGHT:
                
                // Align text to the right.
                
                // Add label to scene graph, aligning text to the right.
                whichStage.addActor(displayLabelAlignRight(elements[0], elements[1]));
                
                // Exit selector.
                break;
                
            default:
                
                // Unknown alignment type.
                
                // Display warning.
                System.out.println("Warning:  Unknown alignment!");
                
                // Exit selector.
                break;
                
        }
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // labelStyle = Name of the style to use.
    // labelScale = Scale to use when displaying label.
    // textLineHeight = Desired text line height.
    // align = Text alignment desired.  Maps to one of the AlignEnum values.
    // posRelative = Relative positioning desired.  Maps to one of the PosRelativeEnum values.
    // whichStage = Reference to stage to which to add label.
    // adjPosX = Adjustment of x-coordinate, relative to corner specified in alignRelative.
    //   Null = No relative positioning for x-coordinate.
    // adjPosY = Adjustment of y-coordinate, relative to corner specified in alignRelative.
    //   Null = No relative positioning for y-coordinate.
    // fontSkinKey = Key to font in skin.
    // elements = Provides values for posX, posY, and stageWidth, as needed, based on enum values.
    public CustomLabel(Skin gameSkin, String labelText, String labelStyle, float labelScale, 
      float textLineHeight, AlignEnum align, PosRelativeEnum posRelative, Stage whichStage, 
      Float adjPosX, Float adjPosY, String fontSkinKey, float ... elements)
    {
        
        /*
        The constructor simplifies the addition of a label to the stage.
        Support alignment of text and relative position of label.
        Centering overrides relative x positioning.
        Note that the posY refers to the bottom y-coordinte of the label, NOT the top.
        
        For lower left and right positioning...
          Relative y-position incorporates the height of the label.
          As a result, the bottom of the label aligns with the position.
        
        Notes about elements...
        
        For left alignment, 0 = posX.
        For center alignment, 0 = posY.
        For right alignment, 0 = posX (rightmost edge), 1 = posY.
        
        posX = For left alignment, leftmost position of label.  Use null for centering (ignored).
               For right alignment, rightmost position of label.
        posY = Y-coordinate for placement of the label -- the bottom, NOT the top.
        stageWidth = Width of stage in which to center label.
        */

        // Initialize label with passed properties.
        this(gameSkin, labelText.toUpperCase(), labelStyle, labelScale, textLineHeight, fontSkinKey);
        
        // Declare variables -- must happen after calling other constructor.
        float labelPosX; // X-coordinate to pass to function displaying label.
        float labelPosY; // Y-coordinate to pass to function displaying label.
        
        // Set defaults.
        labelPosX = 0;
        labelPosY = 0;
        
        // Store stage width and height.
        this.stageWidth = whichStage.getWidth();
        this.stageHeight = whichStage.getHeight();
        
        // Depending on alignment, ...
        switch (align)
        {
            case ALIGN_LEFT:
                
                // Aligning text to the left.
                
                // Set positions of x and y coordinates based on passed values.
                // As necessary, replace with relative positions.
                
                // If elements passed, then...
                if (elements.length == 2)
                    {
                    // Element passed.
                    labelPosX = elements[0];
                    labelPosY = elements[1];
                    }
                
                // Exit selector.
                break;
                
            case ALIGN_CENTER:
                
                // Aligning text to the center.
                
                // Set position of y coordinate based on passed value.
                // As necessary, replace with relative positions.
                
                // If element passed, then...
                if (elements.length == 1)
                    labelPosY = elements[0];
                
                // Exit selector.
                break;
                
            case ALIGN_RIGHT:
                
                // Aligning text to the right.
                
                // Set positions of x and y coordinates based on passed values.
                // As necessary, replace with relative positions.
                
                // If elements passed, then...
                if (elements.length == 2)
                    {
                    // Element passed.
                    labelPosX = elements[0]; // Rightmost edge.
                    labelPosY = elements[1];
                    }
                
                // Exit selector.
                break;
                
            default:
                
                // Unknown alignment type.
                
                // Exit selector.
                break;
                
        } // Depending on text alignment...
        
        // Depending on relative positioning, ...
        switch (posRelative)
        {
            case REL_POS_LOWER_LEFT:
                
                // Position relative to lower left corner.
                
                // If necessary, replace x-coordinate with relative position.
                if (adjPosX != null)
                    labelPosX = adjPosX;
                
                // If necessary, replace y-coordinate with relative position.
                if (adjPosY != null)
                    labelPosY = adjPosY;
                    
                // Exit selector.
                break;
                
            case REL_POS_LOWER_RIGHT:
                
                // Position relative to lower right corner.
                
                // If necessary, replace x-coordinate with relative position.
                if (adjPosX != null)
                    labelPosX = this.stageWidth + adjPosX;
                
                // If necessary, replace y-coordinate with relative position.
                if (adjPosY != null)
                    labelPosY = adjPosY;
                
                // Exit selector.
                break;
                
            case REL_POS_UPPER_LEFT:
                
                // Position relative to upper left corner.
                
                // If necessary, replace x-coordinate with relative position.
                if (adjPosX != null)
                    labelPosX = adjPosX;
                
                // If necessary, replace y-coordinate with relative position.
                if (adjPosY != null)
                    labelPosY = this.stageHeight - customLabel.getHeight() + adjPosY;
                
                // Exit selector.
                break;
                
            case REL_POS_UPPER_RIGHT:
                
                // Position relative to upper right corner.
                
                // If necessary, replace x-coordinate with relative position.
                if (adjPosX != null)
                    labelPosX = this.stageWidth + adjPosX;
                
                // If necessary, replace y-coordinate with relative position.
                if (adjPosY != null)
                    labelPosY = this.stageHeight - customLabel.getHeight() + adjPosY;
                
                // Exit selector.
                break;
                
            default:
                
                // No relative positioning needed.
                
                // Exit selector.
                break;
                
        } // Depending on relative positioning...
        
        //System.out.println("Label height: " + customLabel.getHeight());
        //System.out.println("Label pos x: " + labelPosX);
        //System.out.println("Label pos y: " + labelPosY);
        
        // Depending on alignment, ...
        switch (align)
        {
            case ALIGN_LEFT:
                
                // Align text to the left.
                
                // Add label to scene graph, aligning text to the left.
                whichStage.addActor(displayLabel(labelPosX, labelPosY));
                
                // Exit selector.
                break;
                
            case ALIGN_CENTER:
                
                // Align text to the center.
                
                // Add label to scene graph, aligning text to the center.
                whichStage.addActor(displayLabelCenterX(labelPosY, (int)this.stageWidth));
                
                // Exit selector.
                break;
                
            case ALIGN_RIGHT:
                
                // Align text to the right.
                
                // Add label to scene graph, aligning text to the right.
                whichStage.addActor(displayLabelAlignRight(labelPosX, labelPosY));
                
                // Exit selector.
                break;
                
            default:
                
                // Unknown alignment type.
                
                // Display warning.
                System.out.println("Warning:  Unknown alignment!");
                
                // Exit selector.
                break;
                
        } // Depending on text alignment...
        
    }
    
    // Enumerations below...
    
    // Enumerations related to text alignment.
    public enum AlignEnum 
    {
        
        ALIGN_LEFT (0), // Align to the left.
        ALIGN_CENTER (1), // Align to the center.
        ALIGN_RIGHT (2) // Align to the right.
        ; // semicolon needed when fields / methods follow

        private final int alignEnum; // Enumerations related to text alignment.
        private static final Map alignMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // alignEnum = Value to associate.
        private AlignEnum(int alignEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.alignEnum = alignEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (AlignEnum alignEnum : AlignEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                alignMap.put(alignEnum.alignEnum, alignEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.AlignEnum.ALIGN_RIGHT.getValue();
            
            // Return the numeric value for the enumeration.
            return alignEnum;
        }
        
        // align = Numeric value to convert to text.
        public static AlignEnum valueOf(int align) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (AlignEnum) alignMap.get(align);
        }
        
    }
    
    // Enumerations related to relative position.
    public enum PosRelativeEnum 
    {
        
        REL_POS_GENERAL (0), // No adjustments, place simply as indicated.
        REL_POS_UPPER_LEFT (1), // Align relative to the upper left corner.
        REL_POS_UPPER_RIGHT (2), // Align relative to the upper right corner.
        REL_POS_LOWER_LEFT (3), // Align relative to the lower left corner.
        REL_POS_LOWER_RIGHT (4) // Align relative to the lower right corner.
        ; // semicolon needed when fields / methods follow

        private final int posRelativeEnum; // Enumerations related to relative label positioning.
        private static final Map posRelativeMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // posRelativeEnum = Value to associate.
        private PosRelativeEnum(int posRelativeEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.posRelativeEnum = posRelativeEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (PosRelativeEnum posRelativeEnum : PosRelativeEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                posRelativeMap.put(posRelativeEnum.posRelativeEnum, posRelativeEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.PosRelativeEnum.ALIGN_UPPER_RIGHT.getValue();
            
            // Return the numeric value for the enumeration.
            return posRelativeEnum;
        }
        
        // posRelative = Numeric value to convert to text.
        public static PosRelativeEnum valueOf(int posRelative) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (PosRelativeEnum) posRelativeMap.get(posRelative);
        }
        
    }
    
    // Methods below...
    
    public void addAction_Fade()
    {
        
        // The function sets up a fade effect for the label.
        // Fade out over the course of 1.0 seconds after 2.0 elapse.
        // Note that the function first displays the label.
        
        // Add action to hash map.
        
        // Add fade action to hash map.
        actionMapping.putIfAbsent("Fade", 
          Actions.sequence(
            Actions.fadeIn(0.0f), 
            Actions.visible(true),
            Actions.delay(2.0f), 
            Actions.fadeOut(1.0f), 
            Actions.visible(false)
          ));
        
        // Set up fade effect for the label.
        customLabel.addAction(actionMapping.get("Fade"));
        
    }
    
    public void addAction_FadePartial()
    {
        
        // The function sets up a color fade effect for the label.
        
        // Add action to hash map.
        actionMapping.putIfAbsent("FadePartial", 
          Actions.forever(
            Actions.sequence(
              Actions.color( new Color(1f, 1f, 0f, 1f), 0.5f ),
              Actions.delay( 0.5f ),
              Actions.color( new Color(0.5f, 0.5f, 0f, 1f), 0.5f )
            )));
        
        // Set up color pause effect for the label.
        customLabel.addAction(actionMapping.get("FadePartial"));
        
    }
    
    // redFrom = Red portion of starting color value, in RGB format.
    // greenFrom = Green portion of starting color value, in RGB format.
    // blueFrom = Blue portion of starting color value, in RGB format.
    // redTo = Red portion of ending color value, in RGB format.
    // greenTo = Green portion of ending color value, in RGB format.
    // blueTo = Blue portion of ending color value, in RGB format.
    public void addAction_FadePartial(float redFrom, float greenFrom, float blueFrom, float redTo, float greenTo,
      float blueTo)
    {
        
        // The function sets up a color fade effect for the label using the passed color values.
        
        Color colorFrom; // Starting color value, in LibGDX format.
        Color colorTo; // Ending color value, in LibGDX format.
        
        // Convert colors from RGB to LibGDX format.
        colorFrom = UtilityRoutines.toRGB(redFrom, greenFrom, blueFrom);
        colorTo = UtilityRoutines.toRGB(redTo, greenTo, blueTo);
        
        // Add action to hash map.
        actionMapping.putIfAbsent("FadePartial", 
          Actions.forever(
            Actions.sequence(
              Actions.color( colorFrom, 0.5f ),
              Actions.delay( 0.5f ),
              Actions.color( colorTo, 0.5f )
            )));
        
        // Set up color pause effect for the label.
        customLabel.addAction(actionMapping.get("FadePartial"));
        
    }
    
    // labelEvent = Event logic to apply to label.
    public void addEvent(InputListener labelEvent)
    {
        // The function adds the passed event logic to the label.
        customLabel.addListener(labelEvent);
    }
    
    // visible = Whether to display label.
    public void applyAction_Visible(boolean visible)
    {
        
        // Depending on the passed parameter, the function either instantly displays or hides the label.
        
        // Add display action to hash map.
        actionMapping.putIfAbsent("Visible", 
          Actions.sequence(Actions.fadeIn(0.0f), Actions.visible(true)));
        
        // Add display action to hash map.
        actionMapping.putIfAbsent("Invisible", 
          Actions.sequence(Actions.visible(false)));
        
        // If displaying label, then...
        if (visible)
            
        {
            // Displaying label.
            
            // Display label.
            customLabel.addAction(actionMapping.get("Visible"));
        }
        
        else
            
        {
            // Hiding label.
            
            // Display label.
            customLabel.addAction(actionMapping.get("Invisible"));
        }
        
    }    
            
    // stageWidth = Width of stage in which to center label.
    public void centerLabel(float stageWidth)
    {
        
        // The function centers a label -- useful when text changes.
        // Example for use:  labelTitle.centerLabel(viewWidthMain);
        
        // Store location of label.
        this.posX = stageWidth / 2 - (customLabel.getWidth() * this.labelScale) / 2;
        
        // Set the location of the label.
        customLabel.setPosition(this.posX, this.posY);
        
    }
    
    public void colorLabelDark()
    {
        // The function applies a dark shade to the label.
        customLabel.setColor(Color.DARK_GRAY);
    }
    
    public void colorLabelMedium()
    {
        // The function applies a medium shade to the label.
        customLabel.setColor(Color.LIGHT_GRAY);
    }
    
    public void colorLabelNormal()
    {
        // The function removes shading from the label.
        customLabel.setColor(Color.WHITE);
    }
    
    // posX = X-coordinate for placement of the label.
    // posY = Y-coordinate for placement of the label.
    public final Label displayLabel(float posX, float posY)
    {
        
        // The function displays the label at the passed coordinates.
        // Example for use:  mainStage.addActor(labelTitle.displayLabel(200f, 200f));
        
        // Store location of label.
        this.posX = posX;
        this.posY = posY;
        
        // Set the location of the label.
        customLabel.setPosition(this.posX, this.posY);
        
        // Return the label.
        return customLabel;
        
    }
    
    // posX = X-coordinate for rightmost edge of the label.
    // posY = Y-coordinate for placement of the label.
    public final Label displayLabelAlignRight(float posX, float posY)
    {
        
        // The function positions the label with its right edge at the passed X and top at the specified 
        // Y coordinate.
        // Example for use:  mainStage.addActor(labelTitle.displayLabelAlignRight(384f, 300f));
        
        // Store location of label.
        this.posX = posX - Math.round(customLabel.getWidth());
        this.posY = posY;
        
        // Set the location of the label.
        customLabel.setPosition(this.posX, this.posY);
        
        // Return the label.
        return customLabel;
        
    }
    
    // posX = X-coordinate for placement of the label.
    // posY = Y-coordinate for placement of the label.
    // stageWidth = Width of stage in which to center label.
    public final Label displayLabelCenterX(float posY, float stageWidth)
    {
        
        // The function centers the label at the passed Y coordinate.
        // Example for use:  mainStage.addActor(labelTitle.displayLabelCenterX(384f, viewWidthMain));
        
        // Store y-coordinate of label.
        this.posY = posY;
        
        // Center label across stage.
        setLabelText_Center(this.labelText, stageWidth);
        
        // Return the label.
        return customLabel;
        
    }
    
    public void removeActions()
    {
        
        // The function removes all actions from the label.
        
        // Loop through actions in hash map.
        actionMapping.values().forEach((action) -> {
            // Remove action from label.
            customLabel.removeAction(action);
        });
        
        // Reinitialize the hash map for actions.
        actionMapping = new HashMap<>();
        
    }
    
    public void removeActor()    
    {
        
        // The function removes the actor associated with the label.
        
        // Remove actor.
        customLabel.remove();
        
    }
    
    // text = Text to display in label.
    // bitmapFont = BitmapFont upon which to base measurements.
    public void setLabelText(String labelText, BitmapFont bitmapFont)
    {
        
        // The function updates the text of the label, using the passed bitmap font.
        // Example for use:  labelTitle.setLabelText("Hello World", gameHD.skin.getFont("uiFont"));
        
        GlyphLayout glyphLayout; // Get a glyph layout to have access to font information related to
          // current text.
        
        // Initialize glyph layout.
        glyphLayout = new GlyphLayout();
        
        // Configure glyph layout based on current font and text.
        glyphLayout.setText(bitmapFont, labelText);
        
        // Reset width of label, based on new text.
        customLabel.setWidth(glyphLayout.width);
        
        // Store new text.
        this.labelText = labelText;
        
        // Update text of actual label.
        customLabel.setText(labelText);
        
    }
    
    // labelText = Text to display in label.
    public void setLabelText(String labelText)
    {
        
        // The function updates the text of the label, using the existing bitmap font.
        
        // Computes and caches any information needed for drawing.
        setLabelText(labelText, bitmapFont);
        
    }
    
    // text = Text to display in label.
    // bitmapFont = BitmapFont upon which to base measurements.
    // stageWidth = Width of stage in which to center label.
    public void setLabelText_Center(String labelText, BitmapFont bitmapFont, float stageWidth)
    {
        
        // The function updates the text of and centers the label, using the passed bitmap font.
        // Example for use:  labelTitle.setLabelTextCenter("Hello World", gameHD.skin.getFont("uiFont"), viewWidthMain);
        
        // Update text of the label.
        setLabelText(labelText, bitmapFont);
        
        // Center the label.
        centerLabel(stageWidth);
        
    }
    
    // labelText = Text to display in label.
    // stageWidth = Width of stage in which to center label.
    public void setLabelText_Center(String labelText, float stageWidth)
    {
        
        // The function updates the text of the label and centers the revised object across the screen.
        // The function uses the existing bitmap font.
        
        // Update and recenter label text.
        setLabelText_Center(labelText, bitmapFont, stageWidth);
        
    }
    
    // Getters and setters below...
    
    public Label getLabel()
    {
        // The function returns a reference to the label object.
        return customLabel;
    }
    
    public float getLabelHeight()
    {
        // The function returns the label height.
        
        // Return the height of the label.
        return customLabel.getHeight();
    }
    
    public String getLabelText()
    {
        // The function returns the label text.
        
        // Return the text of the label.
        return labelText;
    }
    
}
