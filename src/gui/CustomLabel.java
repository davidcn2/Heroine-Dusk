package gui;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

// Local project imports.
import core.CoreEnum;
import routines.UtilityRoutines;

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

public class CustomLabel
{
    
    /* 
    The class allows for easy implementation of a label.
    
    Example of steps to add to stage:
    
    CustomLabel mainMenu; // LibGDX Label object that will display main menu text.
    mainMenu = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2, 30.0f, "Key");
    mainMenu.addAction_FadePartial();
    mainStage.addActor(mainMenu.displayLabel(200, 200));
    
    Methods include:
    
    addActionCustom_Center:  Adds a custom action to the hash map that displays new text that centers
      horizontally.
    addActionCustom_Center_AdjY:  Adds a custom action to the hash map that displays new text that centers
      horizontally and moves to the passed vertical position.
    addAction_Fade:  Sets up a fade effect for the label, fading out over the course of 1.0 seconds after 
      2.0 elapse.
    addAction_FadeIn:   Sets up a fade in effect for the label.
    addAction_FadeIn_Center:  Sets up a fade in effect for the label that displays new text that centers
      horizontally.
    addAction_FadeOut:  Sets up a fade out effect for the label.
    addAction_FadeOutIn_Center:  Sets up a fade out / in effect for the label, which displays centered across 
      the screen horizontally.
    addAction_FadeOutIn_Center_AdjY:  Sets up a fade out / in effect for the label, which displays centered 
      across the screen horizontally at the passed y-position.
    addAction_FadePartial:  Sets up a color fade effect for the label.
    addEvent:  Adds the passed event logic to the label.
    applyVisible:  Depending on the passed parameter, either instantly displays or hides the label.
    centerLabel:  Centers a label -- useful when text changes.
    colorLabelDark:  Applies a dark shade to the label.
    colorLabelMedium:  Applies a medium shade to the label.
    colorLabelNormal:  Removes shading from the label.
    displayLabel:  Displays the label at the passed coordinates.
    displayLabelAlignRight:  Positions the label with its right edge at passed X and top at specified Y coordinate.
    displayLabelCenterX:  Centers the label at the passed Y coordinate.
    removeActions:  Removes all actions from the label.
    removeActor:  Removes the actor associated with the label.
    setLabelStyle:  Updates the style of the label, based on the passed parameters.
    setLabelText:  Updates the text of the label, using the existing bitmap font.
    setLabelTextCenter:  Updates the text of the label and centers the revised object across the screen.
    setPosX:  Sets the x-position of the left edge of the label.
    setPosY:  Sets the y-position of the bottom edge of the label.
    setPosition:  Sets the position of the lower left corner of the label.
    */
    
    // Declare object variables.
    private Map<String, Action> actionMapping; // Collection of actions applied to label.
    private BitmapFont bitmapFont; // BitmapFont used for the label.
    private HashMap<String, Action> customActions; // Custom actions.
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
    // actorName = Actor name to use.
    // labelStyle = Name of the style to use.
    // labelScale = Scale to use when displaying label.
    // textLineHeight = Desired text line height.
    // fontSkinKey = Key to font in skin.
    public CustomLabel(Skin gameSkin, String labelText, String actorName, String labelStyle, float labelScale, 
      float textLineHeight, String fontSkinKey)
    {
        
        // The constructor creates a label with basic settings -- essentially text, style, and scale.
        // Example for use:  labelTitle = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2, 30.0f, "Key");
        
        // Initialize the hash maps.
        actionMapping = new HashMap<>();
        customActions = new HashMap<>();
        
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
        
        // Name the related actor.
        customLabel.setName(actorName);
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // actorName = Actor name to use.
    // labelStyle = Name of the style to use.
    // textLineHeight = Desired text line height.
    // fontSkinKey = Key to font in skin.
    public CustomLabel(Skin gameSkin, String labelText, String actorName, String labelStyle, 
      float textLineHeight, String fontSkinKey)
    {
        
        // The constructor creates a label with basic settings -- essentially text and style.
        // Example for use:  labelTitle = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2, 30.0f);
        
        // Call original constructor using a scale of 1.0f.
        this(gameSkin, labelText, actorName, labelStyle, 1.0f, textLineHeight, fontSkinKey);
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // actorName = Actor name to use.
    // textLineHeight = Desired text line height.
    // fontSkinKey = Key to font in skin.
    public CustomLabel(Skin gameSkin, String labelText, String actorName, float textLineHeight, 
      String fontSkinKey)
    {
        
        // The constructor creates a label with basic settings -- essentially text.
        // Example for use:  labelTitle = new CustomLabel(game.skin, "Main Menu", "uiLabelStyle", 2, 30.0f);
        
        // Call original constructor using a style of uiLabelStyle and scale of 1.0f.
        this(gameSkin, labelText, actorName, "uiLabelStyle", 1.0f, textLineHeight, fontSkinKey);
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelText = Text to display in label.
    // actorName = Actor name to use.
    // labelStyle = Name of the style to use.
    // labelScale = Scale to use when displaying label.
    // textLineHeight = Desired text line height.
    // align = Text alignment desired (horizontal).  Maps to one of the AlignEnum values.
    // whichStage = Reference to stage to which to add label.
    // fontSkinKey = Key to font in skin.
    // elements = Provides values for posX, posY, and stageWidth, as needed, based on alignment.
    public CustomLabel(Skin gameSkin, String labelText, String actorName, String labelStyle, float labelScale, 
      float textLineHeight, CoreEnum.AlignEnum align, Stage whichStage, String fontSkinKey,
      float ... elements)
    {
        
        /*
        The constructor simplifies the addition of a label to the stage.
        Supports horizontal alignment of text.
        
        // Notes about elements...
        For left alignment, 0 = posX, 1 = posY.
        For center alignment, 0 = posY.
        For right alignment, 0 = posX (rightmost edge), 1 = posY.
        
        posX = For left alignment, leftmost position of label.  Use null for centering (ignored).
               For right alignment, rightmost position of label.
        posY = Y-coordinate for placement of the label.
        stageWidth = Width of stage in which to center label.
        */

        // Initialize label with passed properties.
        this(gameSkin, labelText.toUpperCase(), actorName, labelStyle, labelScale, textLineHeight, fontSkinKey);
        
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
    // actorName = Actor name to use.
    // actorName = Actor name to use.
    // labelStyle = Name of the style to use.
    // labelScale = Scale to use when displaying label.
    // textLineHeight = Desired text line height.
    // align = Text alignment desired.  Maps to one of the AlignEnum values.
    // posRelative = Relative positioning desired.  Maps to one of the PosRelativeEnum values.
    // whichStage = Reference to stage to which to add label.
    // adjPosX = Adjustment of x-coordinate, relative to corner specified in alignRelative or posRelative.
    //   Null = No horizontal / relative positioning for x-coordinate.
    // adjPosY = Adjustment of y-coordinate, relative to corner specified in alignRelative or posRelative.
    //   Null = No horizontal / relative positioning for y-coordinate.
    // fontSkinKey = Key to font in skin.
    // elements = Provides values for posX, posY, and stageWidth, as needed, based on enum values.
    public CustomLabel(Skin gameSkin, String labelText, String actorName, String labelStyle, float labelScale, 
      float textLineHeight, CoreEnum.AlignEnum align, CoreEnum.PosRelativeEnum posRelative, 
      Stage whichStage, Float adjPosX, Float adjPosY, String fontSkinKey, float ... elements)
    {
        
        /*
        The constructor simplifies the addition of a label to the stage.
        Supports alignment and relative positioning of label.
        Centering overrides relative x positioning.
        Note that the posY refers to the bottom y-coordinate of the label, NOT the top.
        
        For upper left and right positioning...
          Relative y-position incorporates the height of the label.
          As a result, the bottom of the label aligns with the position.
        
        Notes about elements...
        
        For left alignment, 0 = posX, 1 = posY.
        For center alignment, 0 = posY.
        For right alignment, 0 = posX (rightmost edge), 1 = posY.
        
        posX = For left alignment, leftmost position of label.  Use null for centering (ignored).
               For right alignment, rightmost position of label.
        posY = Y-coordinate for placement of the label -- the bottom, NOT the top.
        stageWidth = Width of stage in which to center label.
        */

        // Initialize label with passed properties.
        this(gameSkin, labelText.toUpperCase(), actorName, labelStyle, labelScale, textLineHeight, fontSkinKey);
        
        // Declare variables -- must happen after calling other constructor.
        CoreEnum.AlignEnum alignHorz; // Horizontal alignment to use for label.
        float labelPosX; // X-coordinate to pass to function displaying label.
        float labelPosY; // Y-coordinate to pass to function displaying label.
        
        // Set defaults.
        labelPosX = 0;
        labelPosY = 0;
        
        // Store stage width and height.
        this.stageWidth = whichStage.getWidth();
        this.stageHeight = whichStage.getHeight();
        
        // If horizontal alignment passed, then...
        if (align != null)
        {
            
            // Horizontal alignment passed.
        
            // Store passed horizontal alignment.
            alignHorz = align;
            
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
                        // Elements passed.
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
                        // Elements passed.
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
            
        } // End ... If horizontal alignment passed.
        
        else
        {
        
            // Horizontal alignment NOT passed.
            
            // Default to left alignment.
            alignHorz = CoreEnum.AlignEnum.ALIGN_LEFT;
            
        }
        
        // If relative positioning passed, then...
        if (posRelative != null)
        {
            
            // Relative positioning passed.
        
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
            
        } // End ... If relative positioning passed.
        
        //System.out.println("Label height: " + customLabel.getHeight());
        //System.out.println("Label pos x: " + labelPosX);
        //System.out.println("Label pos y: " + labelPosY);
        
        // Depending on alignment, ...
        switch (alignHorz)
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
    
    // Methods below...
    
    // labelText = Text to display in label.
    public void addActionCustom_Center(String labelText)
    {
        
        // The function adds a custom action to the hash map that displays new text that centers horizontally.
        
        Action action; // Action to add to hash map.
        
        // Create action to add to hash map.
        action = new Action() {
            @Override
            public boolean act(float delta) {

                // The function changes the texture of the actor.

                // Update the label text and center horizontally.
                setLabelText_Center( labelText );
                
                // Return a value.
                return true;

            }
        };

        // Add custom action.
        customActions.put( "CENTER NEW TEXT", action );
        
    }
    
    // labelText = Text to display in label.
    // pos_y = Vertical position at which to place label.
    public void addActionCustom_Center_AdjY(String labelText, float pos_y)
    {
        
        // The function adds a custom action to the hash map that displays new text that centers horizontally
        // and moves to the passed vertical position.
        
        Action action; // Action to add to hash map.
        
        // Create action to add to hash map.
        action = new Action() {
            @Override
            public boolean act(float delta) {

                // The function changes the texture of the actor.

                // Update the label text and center horizontally.
                setLabelText_Center( labelText );
                
                // Adjust vertical position of label.
                setPosY( pos_y );
                
                // Return a value.
                return true;

            }
        };

        // Add custom action.
        customActions.put( "CENTER NEW TEXT - ADJ Y", action );
        
    }
    
    public void addAction_Fade()
    {
        
        // The function sets up a fade effect for the label.
        // Fade out over the course of 1.0 seconds after 2.0 elapse.
        // Note that the function first displays the label.
        
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
    
    // delay = Time, in seconds, before fade occurs.
    // fadeIn = Duration, in seconds, of fade.
    public void addAction_FadeIn(float delay, float fadeIn)
    {
        
        // The function sets up a fade in effect for the label.
        // Fade in over the course of "fadeIn" seconds after "delay" elapse.
        
        // Example for use:  To fade in over the course of 1.0 seconds after 
        //   2.0 elapse ... addAction_FadeIn(2.0f, 1.0f);
        
        // Add fade action to hash map.
        actionMapping.putIfAbsent("FadeIn", 
          Actions.sequence(
            Actions.delay(delay),
            Actions.alpha(0),
            Actions.visible(true),
            Actions.fadeIn(fadeIn)
          ));
        
        // Set up fade effect for the label.
        customLabel.addAction(actionMapping.get("FadeIn"));
        
    }
    
    // delay = Time, in seconds, before fade occurs.
    // fadeIn = Duration, in seconds, of fade.
    // labelText = Text to display in label.
    public void addAction_FadeIn_Center(float delay, float fadeIn, String labelText)
    {
        
        // The function sets up a fade in effect for the label that displays new text that centers
        // horizontally.
        // Fade in over the course of "fadeIn" seconds after "delay" elapse.
        
        // Example for use:  To fade in over the course of 1.0 seconds after 
        //   2.0 elapse ... addAction_FadeIn(2.0f, 1.0f, "test");
        
        // Add custom action to hash map.
        addActionCustom_Center(labelText);
        
        // Add fade action to hash map.
        actionMapping.putIfAbsent("FadeInCenter", 
          Actions.sequence(
            Actions.delay(delay),
            Actions.alpha(0),
            Actions.visible(true),
            customActions.get("CENTER NEW TEXT"),
            Actions.fadeIn(fadeIn)
          ));
        
        // Set up fade effect for the label.
        customLabel.addAction(actionMapping.get("FadeInCenter"));
        
    }
    
    // delay = Time, in seconds, before fade occurs.
    // fadeOut = Duration, in seconds, of fade.
    public void addAction_FadeOut(float delay, float fadeOut)
    {
        
        // The function sets up a fade out effect for the label.
        // Fade out over the course of "fadeOut" seconds after "delay" elapse.
        
        // Example for use:  To fade out over the course of 1.0 seconds after 
        //   2.0 elapse ... addAction_FadeOut(2.0f, 1.0f);
        
        // Add fade action to hash map.
        actionMapping.putIfAbsent("FadeOut", 
          Actions.sequence(
            Actions.fadeIn(0.0f), 
            Actions.visible(true),
            Actions.delay(delay), 
            Actions.fadeOut(fadeOut), 
            Actions.visible(false),
            Actions.color(Color.WHITE)
          ));
        
        // Set up fade effect for the label.
        customLabel.addAction(actionMapping.get("FadeOut"));
        
    }
    
    // delay = Time, in seconds, before fade occurs.
    // fadeIn = Duration, in seconds, of fade in effect.
    // fadeOut = Duration, in seconds, of fade out effect.
    // labelText = Text to display in label.
    public void addAction_FadeOutIn_Center(float delay, float fadeOut, float fadeIn, String labelText)
    {
        
        // The function sets up a fade out / in effect for the label.
        // The label displays centered across the screen horizontally.
        // Fade in over the course of "fadeIn" seconds after "delay" elapse.
        
        // Example for use:  To fade out and in over the course of 1.0 seconds each after 
        //   2.0 elapse ... addAction_FadeOutIn_Center(2.0f, 1.0f, 1.0f, "test");
        
        // Add custom action to hash map.
        addActionCustom_Center(labelText);
        
        // Add fade action to hash map.
        actionMapping.putIfAbsent("FadeOutInCenter", 
          Actions.sequence(
            Actions.delay(delay),
            Actions.fadeOut(fadeOut),
            Actions.alpha(0),
            Actions.visible(true),
            customActions.get("CENTER NEW TEXT"),
            Actions.fadeIn(fadeIn)
          ));
        
        // Set up fade effect for the label.
        customLabel.addAction(actionMapping.get("FadeOutInCenter"));
        
    }
    
    // delay = Time, in seconds, before fade occurs.
    // fadeIn = Duration, in seconds, of fade in effect.
    // fadeOut = Duration, in seconds, of fade out effect.
    // labelText = Text to display in label.
    // pos_y = Vertical position at which to place label.
    public void addAction_FadeOutIn_Center_AdjY(float delay, float fadeOut, float fadeIn, String labelText,
      float pos_y)
    {
        
        // The function sets up a fade out / in effect for the label.
        // The label appears centered across the screen horizontally and at the passed y-coordinate.
        // Fade in over the course of "fadeIn" seconds after "delay" elapse.
        
        // Example for use:  To fade out and in over the course of 1.0 seconds each after 
        //   2.0 elapse ... addAction_FadeOutIn_Center_AdjY(2.0f, 1.0f, 1.0f, "test", 40.0f);
        
        // Add custom action to hash map.
        addActionCustom_Center_AdjY(labelText, pos_y);
        
        // Add fade action to hash map.
        actionMapping.putIfAbsent("FadeOutInCenterAdjY", 
          Actions.sequence(
            Actions.delay(delay),
            Actions.fadeOut(fadeOut),
            Actions.alpha(0),
            Actions.visible(true),
            customActions.get("CENTER NEW TEXT - ADJ Y"),
            Actions.fadeIn(fadeIn)
          ));
        
        // Set up fade effect for the label.
        customLabel.addAction(actionMapping.get("FadeOutInCenterAdjY"));
        
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
    public void applyVisible(boolean visible)
    {
        
        // Depending on the passed parameter, the function either instantly displays or hides the label.
        
        // If displaying label, then...
        if (visible)
            
        {
            // Displaying label.
            
            // Display label.
            customLabel.setVisible(true);
        }
        
        else
            
        {
            // Hiding label.
            
            // Display label.
            customLabel.setVisible(false);
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
        this.posX = posX - Math.round(customLabel.getWidth() * this.labelScale);
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
    
    // x = Amount by which to move the label horizontally.
    public final void movePosX(float x)
    {
        // The method moves the label horizontally the specified amount.
        
        // Store updated x-coordinate.
        this.posX += x;
        
        // Call method in actor.
        customLabel.setX(x);
    }
    
    // y = Amount by which to move the label vertically.
    public final void movePosY(float y)
    {
        // The method moves the label vertically the specified amount.
        
        // Store updated y-coordinate.
        this.posY += y;
        
        // Call method in actor.
        customLabel.setY(y);
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
        //actionMapping = new HashMap<>();
        
        // Clear hash map for actions.
        actionMapping.clear();
        
    }
    
    public void removeActor()    
    {
        
        // The function removes the actor associated with the label.
        
        // Remove actor.
        customLabel.remove();
        
    }
    
    // gameSkin = Reference to skin used with the game.
    // labelStyle = Name of the style to use.
    public void setLabelStyle(Skin gameSkin, String labelStyle)
    {
        
        // The function updates the style of the label, based on the passed parameters.
        
        // Update class-level variable with style.
        this.labelStyle = labelStyle;
        
        // Update style of label.
        customLabel.setStyle(gameSkin.get(labelStyle, LabelStyle.class));
        
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
        
        // Update text of the label.
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
    
    // labelText = Text to display in label.
    public void setLabelText_Center(String labelText)
    {
        
        // The function updates the text of the label and centers the revised object across the screen.
        // The function uses the existing bitmap font and stage width.
        
        // Update and recenter label text.
        setLabelText_Center(labelText, bitmapFont, this.stageWidth);
        
    }
    
    // labelText = Text to display in label.
    public void setLabelText_Right(String labelText)
    {
        
        // The function updates the text (and width) of the label and keeps its right edge at the same location.
        
        float posX_Right; // Rightmost position / edge of label.
        
        // Store right edge of label.
        posX_Right = posX + (customLabel.getWidth() * this.labelScale);
        
        // Update text of the label.
        setLabelText(labelText, bitmapFont);
        
        // Set new left position of label.
        setPosX(posX_Right - Math.round(customLabel.getWidth() * this.labelScale));
        
    }
    
    // x = X-coordinate at which to place lower left corner of the label.
    // y = Y-coordinate at which to place lower left corner of the label.
    public final void setPosition(float x, float y)
    {
        // The method sets the position of the lower left corner of the label.
        
        // Call method in actor.
        customLabel.setPosition(x, y);
    }
    
    // x = X-coordinate at which to place lower left corner of the label.
    public final void setPosX(float x)
    {
        // The method sets the x-position of the left edge of the label.
        
        // Store updated x-coordinate.
        this.posX = x;
        
        // Call method in actor.
        customLabel.setX(x);
    }
    
    // y = Y-coordinate at which to place lower left corner of the label.
    public final void setPosY(float y)
    {
        // The method sets the y-position of the bottom edge of the label.
        
        // Store updated y-coordinate.
        this.posY = y;
        
        // Call method in actor.
        customLabel.setY(y);
    }
    
    // Getters and setters below...
    
    // key = Key to check for in the action map hash map.
    public boolean getActionMapKeyInd(String key) {
        
        // The function returns whether the action map hash map contains the passed key.
        
        // Return whether the action map hash map contains the passed key.
        return actionMapping.containsKey(key);
        
    }
    
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
