package screens;

// LibGDX imports.
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

// Local project imports.
import core.AssetMgr;
import core.BaseActor;
import core.BaseGame;
import core.BaseScreen;
import core.CoreEnum;
import gui.CustomLabel;
import heroinedusk.Dialog;
import heroinedusk.HeroineDuskGame;
import heroinedusk.HeroineEnum;

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

public class DialogScreen extends BaseScreen { // Extends the BaseScreen class.
    
    /*
    The class extends the basic functionality of a BaseScreen class and sets up a dialog screen.

    Methods include:

    addEvent:  Adds events to the passed button (BaseActor).
    create:  Calls constructor for BaseScreen and configures and adds objects in the dialog screen.
    dispose:  Called when removing the screen and allows for clearing of related resources from memory.
    info_render_gold:  Renders the player gold-related label.
    render:  Updates the screen based on the most recent information / status.
    renderButtons:  Renders the buttons, selector, and associated labels.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    wakeScreen:  Called when redisplaying the already initialized screen.
    */
    
    // Declare object variables.
    private AssetMgr assetMgr; // Enhanced asset manager.
    private BaseActor background; // BaseActor object that will act as the background.
    private ArrayList<CustomLabel> buttonLabels; // List of labels for buttons.
    private ArrayList<BaseActor> buttons; // List of base actors for buttons.
    private final Dialog dialog; // Reference to dialog object.
    private CustomLabel dialogMsgLabel; // Label displaying dialog message.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    private CustomLabel goldLabel; // Label displaying player gold.
    private ArrayList<Label> removeList; // Array of Label objects to remove from screen.
    private BaseActor selector; // Base actor for the selector.
    private CustomLabel titleLabel; // Label displaying dialog title.
    
    // Declare regular variables.
    final private boolean initialized; // Whether screen initialized.
    private ArrayList<Integer> labelCountList; // Number of labels per button.
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
        
        // Perform additional logic related to startup / create phase, including configuration and addition
        // of actors to stage.
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
        float adjPosY; // Adjustment factor (y-position) to use with relative positioning and label placement.
        String backgroundKey; // Key to background image in hash map (in asset manager).
        
        // 1.  Set defaults.
        // No defaults currently.
        
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
        backgroundKey = gameHD.getShopInfo().getShopList().get(dialog.getShop_id().getValue()).getBackground().getValue_Key();
        
        // Assign the Texture to the background Actor.
        background.setTexture(gameHD.getAssetMgr().getImage_xRef(backgroundKey));
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        background.setPosition( 0, 0 );
        
        // Add the background Actor to the scene graph.
        mainStage.addActor( background );
        
        // If item(s) or service(s) offered for sale, then...
        if (dialog.isItems_for_sale())
            
            {
            // Item(s) or service(s) offered for sale.
            
            // Render player gold-related label.
            info_render_gold();
            }
        
        // 4.  Render the title label.
        titleLabel = new CustomLabel(game.skin, dialog.getTitle(), "dialog title label", "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, mainStage, null, -12f, 
          HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0, viewWidthMain);
        
        // 5.  Render the buttons and associated labels.
        renderButtons();
        
        // 6.  Render the dialog message.
        
        // Calculate adjustment factor -- amount below top of screen for bottom of label.
        adjPosY = -(mainStage.getHeight() * 0.25f + gameHD.getConfig().getTextLineHeight());
        
        // Create and display label containing dialog message.
        dialogMsgLabel = new CustomLabel(game.skin, dialog.getMessage(), "dialog message label", 
          "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_CENTER, 
          CoreEnum.PosRelativeEnum.REL_POS_UPPER_LEFT, mainStage, null, adjPosY, 
          HeroineEnum.FontEnum.FONT_UI.getValue_Key(), 0, viewWidthMain);
        
    }
    
    // button = Reference to BaseActor for the button.
    // buttonNbr = Button number, base 0.  -1 = Selector.
    public void addEvent(BaseActor button, int buttonNbr)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Events include touchDown and touchUp.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
            {
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user touches the screen or presses a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS. 
                    // Notes:  Occurs when user press down mouse on button.
                    // Notes:  Event (touchDown) necessary to reach touchUp.
                    
                    // Return a value.
                    return true;
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // button = Button pressed.
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                {
                    
                    // The function occurs when the user lifts a finger or released a mouse button.
                    
                    // Notes:  The button parameter will be Input.Buttons.LEFT on iOS.
                    // Notes:  Occurs when user releases mouse on button.
                    
                    int buttonSelected; // Button number selected.
                    
                    // If processing an event from a regular button and NOT the selector, then...
                    if (buttonNbr > -1)
                    
                        {
                        // Processing an event from a regular button and NOT the selector.
                        
                        // Update the selected button number based on the one clicked.
                        gameHD.getDialog().setSelect_pos(buttonNbr);
                        
                        // Store the button number selected.
                        buttonSelected = buttonNbr;
                        }
                    
                    else
                        
                        {
                        // Processing an event from a regular button AND the selector.
                            
                        // Store the button number as the current selection.
                        buttonSelected = gameHD.getDialog().getSelect_pos();
                        }
                    
                    // Process logic related to the label clicked.
                    gameHD.getShopInfo().shop_act(buttonSelected);
                    
                }
                
            }; // End ... InputListener.
        
        // Add event to button.
        button.addListener(buttonEvent);
        
    }
    
    @Override
    public void dispose()
    {
        
        // The method is called when removing the screen and allows for clearing of related resources 
        // from memory.
        
        // Call manual dispose method in superclass.
        super.disposeManual();
        
    }
    
    private void info_render_gold()
    {
        
        // The function renders the player gold-related label.
        
        // Render the player gold-related label.
        goldLabel = new CustomLabel(game.skin, gameHD.getAvatar().getGold() + " Gold", "gold label", 
          "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), CoreEnum.AlignEnum.ALIGN_RIGHT, 
          CoreEnum.PosRelativeEnum.REL_POS_LOWER_RIGHT, mainStage, -12f, 12f, 
          HeroineEnum.FontEnum.FONT_UI.getValue_Key());
        
    }
    
    public void renderButtons()
    {
        
        // The function renders the buttons, selector, and associated labels.
        
        // Declare object variables.
        BaseActor button; // Base actor for a button.
        ArrayList<CustomLabel> buttonLabelCurr; // Label(s) for button message(s) for current option in loop.
        
        // Declare regular variables.
        boolean drawButton; // Whether to draw current button.
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
        final float buttonLabelDistanceHorz = 18.0f; // Distance between buttons and labels, horizontally.
        final float buttonSelectorHeight = 12.0f; // Height of one border of selector.
        final float labelDistanceVert = 11.0f; // Distance between labels, vertically.
        
        // Set defaults.
        buttonCounter = 0;
        
        // Initialize array lists.
        labelCountList = new ArrayList<>();
        
        // First, clear any existing buttons and labels, and the selector and remove them from the stage.
        
        // If one or more buttons exist, then...
        if (buttons.size() > 0)
        {
            
            // One or more buttons exist.
            // Clear existing buttons.
            buttons.forEach((currButton) -> {
                // Remove button from stage.
                currButton.remove();
            });

            // Reinitialize button list.
            buttons = new ArrayList<>();
            
        }
            
        // If one or more button labels exist, then...
        if (buttonLabels.size() > 0)
        {
            
            // One or more button labels exist.
            // Clear existing labels.
            buttonLabels.forEach((currLabel) -> {
                // Remove button label from stage.
                currLabel.removeActor();
            });

            // Reinitialize button label list.
            buttonLabels = new ArrayList<>();
            
        }
        
        // If selector exists, then...
        if (selector != null)
        {
            
            // Selector exists.
            
            // Remove selector from stage.
            selector.remove();

            // Clear selector from memory.
            selector = null;
            
        }
        
        // Count buttons.
        buttonCount = gameHD.getDialog().getOptions().length;
        
        // Store button height and width.
        buttonHeight = gameHD.getAssetMgr().getTextureRegionRect(HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY.getValue_Key()).height;
        buttonWidth = gameHD.getAssetMgr().getTextureRegionRect(HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY.getValue_Key()).width;
        
        // Store x-coordinate of left edge of labels.
        labelPosX = buttonWidth + buttonPosX + buttonLabelDistanceHorz;
        
        // Calculate y-coordinate of bottom edge of buttons.
        // Bottom edge = ((Button height x (count - 1) + (distance between x (count - 1))) + button selector edge height.
        // aka Buttom edge = ((Button height x distance between) x (count - 1)) + button selector edge height.
        // LibGDX = Bottom edge.
        bottomY = (buttonHeight + buttonDistanceVert) * (buttonCount - 1) + buttonSelectorHeight;
        
        // Loop through options to get to buttons and text.
        for (Dialog.Option option : gameHD.getDialog().getOptions())
            
        {
                
            // If buy or exit button then, then draw -- set to true.
            // Otherwise, do not draw button -- set to false.
            drawButton = option.button == HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY || 
              option.button == HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_EXIT;

            // Calculate bottom and middle y-coordinates of current button.
            buttonPosY = bottomY - (buttonCounter * (buttonHeight + buttonDistanceVert));
            buttonPosY_Middle = buttonPosY + (buttonHeight / 2);
            
            // If drawing button, then...
            if (drawButton)    
            {
                
                // Drawing button -- Buy or exit.
                    
                // Create new BaseActor for the button.
                button = new BaseActor();

                // Name actor associated with button.
                button.setActorName("button" + option.buttonNbr);

                // Assign the Texture to the button Actor.
                button.setTextureRegion(gameHD.getAssetMgr().getTextureRegion(option.button.getValue_Key()));
                
                // Position the background with its lower left corner at the corresponding location in the screen.
                button.setPosition( buttonPosX, buttonPosY);

                // Add events to current button.
                addEvent( button, option.buttonNbr );
                
                // Add the background Actor to the scene graph.
                mainStage.addActor( button );

                // Add button to array list.
                buttons.add(button);
                
                // If current button selected, then...
                if (option.buttonNbr == gameHD.getDialog().getSelect_pos())
                {

                    // Current button selected.

                    // Create new BaseActor for the selector.
                    selector = new BaseActor();

                    // Name actor associated with the selector.
                    selector.setActorName("selector");

                    // Assign the Texture to the selector Actor.
                    selector.setTexture(gameHD.getAssetMgr().getImage_xRef(HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_SELECT.getValue_Key()));

                    // Position the background with its lower left corner at the corresponding location in the screen.
                    selector.setPosition( 0, buttonPosY - buttonSelectorHeight );
                    
                    // Add events to selector.
                    addEvent( selector, -1 );

                    // Add the background Actor to the scene graph.
                    mainStage.addActor( selector );

                }
            
            } // End ... If drawing button.

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
                buttonLabelCurr.add(new CustomLabel(game.skin, option.msg1.toUpperCase(), "option one label", 
                  "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), 
                  HeroineEnum.FontEnum.FONT_UI.getValue_Key()));

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
                buttonLabelCurr.add(new CustomLabel(game.skin, option.msg2.toUpperCase(), "option two label", 
                  "uiLabelStyle", 1.0f, gameHD.getConfig().getTextLineHeight(), 
                  HeroineEnum.FontEnum.FONT_UI.getValue_Key()));

                // Add height of current label.
                labelHeight += buttonLabelCurr.get(labelCountCurr).getLabelHeight();

                // Increment label count -- for current option / button.
                labelCountCurr++;

            }

            // Store label count for current option / button.
            labelCountList.add(labelCountCurr);

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
            
        } // End ... Loop through options to get to buttons.
        
    }
    
    public void render()
    {
        
        // The function updates the screen based on the most recent information / status.
        
        // Update the player gold-related label.
        goldLabel.setLabelText(gameHD.getAvatar().getGold() + " GOLD");
        
        // Render the buttons, selector, and associated labels.
        renderButtons();
        
        // Update the dialog message label.
        dialogMsgLabel.setLabelText_Center(gameHD.getDialog().getMessage().toUpperCase(), mainStage.getWidth());
        
        // If fade effect desired, then...
        if (gameHD.getDialog().isFadeMessage())
            
        {
            
            // Fade effect desired.
            
            // Add fact action to label.
            dialogMsgLabel.addAction_Fade();
            
            // Change flag to indicate application of fade effect.
            gameHD.getDialog().setFadeMessage(false);
            
        }
        
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
        
        // Perform logic related to startup / create phase, including configuration and addition
        // of actors to stage.
        create();
        
    }
    
}