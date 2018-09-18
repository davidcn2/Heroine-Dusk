package heroinedusk;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private ArrayList<CustomLabel> buttonLabels; // List of labels for buttons.
    private ArrayList<BaseActor> buttons; // List of base actors for buttons.
    private final Dialog dialog; // Reference to dialog object.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private ArrayList<Label> removeList; // Array of Label objects to remove from screen.
    private BaseActor selector; // Base actor for the selector.
    private CustomLabel titleLabel; // Label displaying dialog title.
    
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
    // dialog = Reference to dialog object.
    public DialogScreen(BaseGame g, int windowWidth, int windowHeight, HeroineDuskGame hdg, Dialog dialog)
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
        
        // Store reference to main game and dialog classes.
        gameHD = hdg;
        this.dialog = dialog;
        
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
        
        // Declare object variables.
        BaseActor button; // Base actor for a button.
        ArrayList<CustomLabel> buttonLabelCurr; // Label(s) for button message(s) for current option in loop.
        
        // Declare regular variables.
        String backgroundKey; // Key to background image in hash map (in asset manager).
        float bottomY; // Y-coordinate of bottom of first button shown vertically.
        float bottomY_Labels; // Y-coordinate of bottom of first label shown vertically.
        int buttonCount; // Number of buttons of type buy or exit.
        int buttonCounter; // Used to increment through buttons while adding them.
        float buttonHeight; // Button height.
        float buttonPosY; // Y-coordinate of bottom of current button.
        float buttonPosY_Middle; // Y-coordinate of middle of current button.
        float buttonWidth; // Button width.
        int labelCountCurr; // Number of labels for current option.
        float labelHeight; // Height (total) of label(s) for current option.
        float labelHeightAdj; // Height of a single label added to space between.
        float labelHeightSingle; // Height of a single label.
        float labelPosX; // X-coordinate of left edge of labels.
        float labelPosY; // Y-coordinate of bottom of current label.
        
        // Declare constants.
        final float buttonPosX = 12.0f; // X-coordinate of buttons.
        final float buttonDistanceVert = 18.0f; // Distance between buttons, vertically.
        final float labelDistanceVert = 11.0f; // Distance between labels, vertically.
        
        // 1.  Set defaults.
        buttonCounter = 0;
        
        // 2.  Initialize arrays and array lists.
        buttonLabels = new ArrayList<>();
        buttons = new ArrayList<>();
        removeList = new ArrayList<>();
        
        // 3.  Configure and add the background Actor.
        
        // Create new BaseActor for the background.
        background = new BaseActor();
        
        // Name background actor.
        background.setActorName("Background");
        
        // Get key to background image in hash map in asset manager.
        backgroundKey = gameHD.shopInfo.shopList.get(dialog.shop_id.getValue()).getBackground().getValue_Key();
        
        // Assign the Texture to the background Actor.
        background.setTexture(gameHD.assetMgr.getImage_xRef(backgroundKey));
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        background.setPosition( 0, 0 );
        
        // Add the background Actor to the scene graph.
        mainStage.addActor( background );
        
        // If item(s) or service(s) offered for sale, then...
        if (dialog.items_for_sale)
            
            {
            // Item(s) or service(s) offered for sale.
            
            // Render player gold-related label.
            info_render_gold();
            }
        
        System.out.println("Title: " + dialog.title);
        
        // 4.  Render the title label.
        titleLabel = new CustomLabel(game.skin, dialog.title, "uiLabelStyle", 1.0f, 
          gameHD.config.getTextLineHeight(), CustomLabel.AlignEnum.ALIGN_CENTER, 
          CustomLabel.PosRelativeEnum.REL_POS_UPPER_LEFT, mainStage, null, -12f, 0, viewWidthMain);
        
        // 5.  Render the buttons and associated labels.
        
        // Count buttons.
        buttonCount = gameHD.dialog.countButtons();
        
        // Store button height and width.
        buttonHeight = gameHD.assetMgr.getTextureRegionRect(HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY.getValue_Key()).height;
        buttonWidth = gameHD.assetMgr.getTextureRegionRect(HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY.getValue_Key()).width;
        
        // Store x-coordinate of left edge of labels.
        labelPosX = buttonWidth + buttonPosX + 24f;
        
        // Calculate y-coordinate of bottom edge of buttons.
        // Top edge = (Button height x count) + (distance between x (count - 1)).
        // Bottom edge = Top edge - button height.
        // Bottom edge = (Button height x (count - 1) + (distance between x (count - 1)).
        // aka Buttom edge = (Button height x distance between) x (count - 1).
        // LibGDX = Bottom edge.
        bottomY = (buttonHeight + buttonDistanceVert) * (buttonCount - 1) + 5f;
        
        // Loop through options to get to buttons and text.
        for (Dialog.Option option : gameHD.dialog.options)
            
        {
            
            // If buy or exit button, then...
            if (option.button == HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY || 
                option.button == HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_EXIT)
                
            {
                
                // Buy or exit button.
                
                // Create new BaseActor for the button.
                button = new BaseActor();
                
                // Name actor associated with button.
                button.setActorName("button" + option.buttonNbr);
                
                // Assign the Texture to the background Actor.
                button.setTextureRegion(gameHD.assetMgr.getTextureRegion(option.button.getValue_Key()));
                
                // Calculate bottom and middle y-coordinates of current button.
                buttonPosY = bottomY - (buttonCounter * (buttonHeight + buttonDistanceVert));
                buttonPosY_Middle = buttonPosY + (buttonHeight / 2);
                
                // Position the background with its lower left corner at the corresponding location in the screen.
                button.setPosition( buttonPosX, buttonPosY);
                
                // Add the background Actor to the scene graph.
                mainStage.addActor( button );
                
                // Add button to array list.
                buttons.add(button);
                
                // If current button selected, then...
                if (option.buttonNbr == gameHD.dialog.select_pos)
                {
                    
                    // Current button selected.
                    
                    System.out.println("Current button selected.");
                    
                    // Create new BaseActor for the selector.
                    selector = new BaseActor();
                    
                    // Name actor associated with the selector.
                    selector.setActorName("selector");
                    
                    // Assign the Texture to the selector Actor.
                    selector.setTexture(gameHD.assetMgr.getImage_xRef(HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_SELECT.getValue_Key()));
                    
                    // Position the background with its lower left corner at the corresponding location in the screen.
                    //selector.setPosition( ?, ? );
                    
                    // Add the background Actor to the scene graph.
                    //mainStage.addActor( selector );
                    
                }
                
                // Reset label count and height.
                labelCountCurr = 0;
                labelHeight = 0;
                
                // Initialize array list for button label(s) for current option.
                buttonLabelCurr = new ArrayList<>();
                
                // If first of two possible messages exists for button, then...
                if (option.msg1.length() > 0)
                    
                {
                    
                    // First of two possible messages exists for button.
                    
                    // Create button label.
                    buttonLabelCurr.add(new CustomLabel(game.skin, option.msg1.toUpperCase(), "uiLabelStyle", 1.0f, 
                      gameHD.config.getTextLineHeight()));
                    
                    // Add height of current label.
                    labelHeight += buttonLabelCurr.get(0).getLabelHeight();
                    
                    // Increment label count -- for current option / button.
                    labelCountCurr++;
                     
                }
                
                // If second of two possible messages exists for button, then...
                if (option.msg2.length() > 0)
                    
                {
                    
                    // Second of two possible messages exists for button.
                    
                    // Create button label.
                    buttonLabelCurr.add(new CustomLabel(game.skin, option.msg2.toUpperCase(), "uiLabelStyle", 1.0f, 
                      gameHD.config.getTextLineHeight()));
                    
                    // Add height of current label.
                    labelHeight += buttonLabelCurr.get(labelCountCurr).getLabelHeight();
                    
                    // Increment label count -- for current option / button.
                    labelCountCurr++;
                     
                }
                
                // If one label exists for current option / button, then...
                if (labelCountCurr == 1)
                {
                    // One label exists for current option / button.
                    
                    // Calculate y-coordinate of bottom of message (label) for current option / button.
                    bottomY_Labels = buttonPosY_Middle - labelHeight / 2;
                    
                    // Add current label to scene graph.
                    mainStage.addActor(buttonLabelCurr.get(0).displayLabel(labelPosX, bottomY_Labels));
                    
                    // Add current label to array list.
                    buttonLabels.add(buttonLabelCurr.get(0));
                    
                }
                
                // If more than one message exists for current option / button, then...
                else if (labelCountCurr > 1)
                    
                {
                    
                    // More than one message exists for current option / button.
                    
                    // Calculate y-coordinate of bottom of first message (label) for current option / button.
                    
                    // Store height of a single label.
                    labelHeightSingle = buttonLabelCurr.get(0).getLabelHeight();
                    
                    // Calculate height of a single label added to space in between.
                    labelHeightAdj = labelHeightSingle + labelDistanceVert;
                    
                    // Bottom edge = Middle of button + ((0.5 * (label count - 1)) * distance between) + (label height * (0.5 * (label count - 1)))
                    bottomY_Labels = buttonPosY_Middle + ((0.5f * (labelCountCurr - 1f)) * labelDistanceVert) + 
                      (labelHeightSingle * (0.5f * (labelCountCurr - 2f)));
                    
                    // Loop through labels.
                    for (int labelCounterCurr = 1; labelCounterCurr <= labelCountCurr; labelCounterCurr++)
                        
                    {
                        
                        // Calculate y-coordinate for current label.
                        // Position = Bottom of first label - ((counter - 1) * (label height + distance between)).
                        labelPosY = bottomY_Labels - (labelHeightAdj * (labelCounterCurr - 1));
                        
                        // Add current label to scene graph.
                        mainStage.addActor(buttonLabelCurr.get(labelCounterCurr - 1).displayLabel(labelPosX, 
                          labelPosY));
                        
                        // Add current label to array list.
                        buttonLabels.add(buttonLabelCurr.get(labelCounterCurr - 1));
                        
                    } // End ... Loop through labels.
                    
                } // End ... If more than one label.
                
                // Increment button counter.
                buttonCounter++;
                
            } // End ... If a buy or exit button...
            
        } // End ... Loop through options to get to buttons.
            
        
        
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
    
    private void info_render_gold()
    {
        
        // The function renders the player gold-related label.
        
        CustomLabel goldLabel; // Label displaying player gold.
        
        // Render the player gold-related label.
        goldLabel = new CustomLabel(game.skin, gameHD.avatar.getGold() + " Gold", "uiLabelStyle", 1.0f, 
          gameHD.config.getTextLineHeight(), CustomLabel.AlignEnum.ALIGN_RIGHT, 
          CustomLabel.PosRelativeEnum.REL_POS_LOWER_RIGHT, mainStage, -12f, 12f);
        
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