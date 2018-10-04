package screens;

// LibGDX imports.
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

// Local project imports.
import core.BaseActor;
import core.BaseScreen;
import core.TextureRect;
import gui.CustomLabel;
import gui.CustomProgressBar;
import heroinedusk.HeroineDuskGame;
import heroinedusk.HeroineEnum;
import heroinedusk.JSON_Processor;

// Java imports.
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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

public class IntroScreen extends BaseScreen { // Extends the BaseScreen class.

    /*
    The class extends the basic functionality of a BaseScreen class and sets up the introduction screen.

    Methods include:

    addEvent:  Adds events to the passed button (BaseActor).  Used with the start button.
    create:  Calls constructor for BaseScreen and configures and adds objects in the introduction screen.
    dispose:  Called when removing the screen and allows for clearing of related resources from memory.
    postLoad:  Performs actions after the loading of assets.
    queueAssets:  Queues the assets for loading, based on the current size of the application window.
    update:  Occurs during the update phase (render method) and contains code related to game logic.
    */
    
    // Declare object variables.
    private ArrayList<String> atlasKeyList; // List of atlas keys.
    private BaseActor background; // BaseActor object that will act as the background.
    private CustomLabel createdByLabel1; // Created by label - first line.
    private CustomLabel createdByLabel2; // Created by label - second line.
    private CustomLabel musicByLabel; // Music by label.
    private CustomProgressBar progressBar; // Reference to custom progress bar object.
    private BaseActor startButton; // BaseActor object that will act as the start button.
    private CustomLabel startLabel; // Start label.
    private CustomLabel titleLabel; // Title label.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    
    // Declare regular variables.
    private boolean stillLoading; // Whether still loading (assets).
    
    // hdg = Reference to Heroine Dusk (main) game.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    public IntroScreen(HeroineDuskGame hdg, int windowWidth, int windowHeight)
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
        2.  Configures and adds assets needed for introduction screen -- background, title, and music.
        3.  Initializes the custom progress bar.
        4.  Queues assets -- actually loading occurs in the update() function.
        5.  Configures and adds the labels with the created by text.
        6.  Configures and adds the labels with the music by text.
        7.  Loads atlas information, including regions and items.
        */
        
        String jsonDir; // Directory containing JSON files.
        String jsonFullPath; // Full path to JSON file.
        String parentDir; // Top-level directory containing packages.
        JSON_Processor json; // Handles JSON functionality.
        
        // 1.  Set defaults.
        stillLoading = true;
        
        // 2.  Configure and add assets needed for introduction screen -- background, title, and music.
        
        // 2A.  Configure and add the background Actor.
        
        // Create new BaseActor for the background.
        background = new BaseActor();
        
        // Name background actor.
        background.setActorName("Background");
        
        // If using a manually scaled size, then...
        if (gameHD.getConfig().getStretchToScreen())
            
            {
            // Using a manually scaled size.
        
            // Assign the Texture to the background Actor.
            background.setTexture( new Texture(Gdx.files.internal("assets/backgrounds/nightsky.png")) );
            }
        
        else
            
            {
            // Using a prescaled size.
                
            // Assign the Texture to the background Actor.
            background.setTexture( new Texture(Gdx.files.internal(gameHD.getConfig().getPrescaleFolder_Backgrounds() + 
              "nightsky.png")) );
            }
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        background.setPosition( 0, 0 );
        
        // Add the background Actor to the scene graph.
        mainStage.addActor( background );
        
        // 2B.  Configure and add the label with the title.
        
        // Initialize label with title text.
        titleLabel = new CustomLabel(game.skin, "THE PERILS OF GRIZHAWK", "uiLabelStyle", 1.25f, 
          gameHD.getConfig().getTextLineHeight(), HeroineEnum.FontEnum.FONT_UI.getValue_Key());
        
        // Add label to scene graph.
        mainStage.addActor(titleLabel.displayLabelCenterX(600, viewWidthMain));
        
        // 2C.  Load and play the background music.
        
        // Queue just the starting music.
        gameHD.getAssetMgr().queueMusic(HeroineEnum.MusicEnum.M31.getValue_File_ogg());
        
        // Load just the starting music.
        gameHD.getAssetMgr().loadResources();
        
        // Play (loop) starting background music.
        gameHD.getSounds().playMusicDirect(gameHD.getAssetMgr(), HeroineEnum.MusicEnum.M31);
        
        // 3.  Initialize the custom progress bar.
        progressBar = new CustomProgressBar(game.skin);
        
        // 4.  Queue assets for loading.
        queueAssets();
        
        // 5.  Configure and add the labels with the created by text.
        
        // Initialize label with first line of created by text.
        createdByLabel1 = new CustomLabel(game.skin, "BASED ON - HEROINE DUSK", "uiLabelStyle", 0.8f, 
          gameHD.getConfig().getTextLineHeight(), HeroineEnum.FontEnum.FONT_UI.getValue_Key());
        
        // Apply medium shade to label.
        createdByLabel1.colorLabelMedium();
        
        // Add label to scene graph.
        mainStage.addActor(createdByLabel1.displayLabelCenterX(310, viewWidthMain));
        
        // Initialize label with second line of created by text.
        createdByLabel2 = new CustomLabel(game.skin, "(CREATED BY CLINT BELLANGER)", "uiLabelStyle", 0.8f, 
          gameHD.getConfig().getTextLineHeight(), HeroineEnum.FontEnum.FONT_UI.getValue_Key());
        
        // Apply medium shade to label.
        createdByLabel2.colorLabelMedium();
        
        // Add label to scene graph.
        mainStage.addActor(createdByLabel2.displayLabelCenterX(280, viewWidthMain));
        
        // 6.  Configure and add the label with the music by text.
        
        // Initialize label with first line of created by text.
        musicByLabel = new CustomLabel(game.skin, "FEATURES MUSIC BY YUBATAKE", "uiLabelStyle", 0.8f, 
          gameHD.getConfig().getTextLineHeight(), HeroineEnum.FontEnum.FONT_UI.getValue_Key());
        
        // Apply medium shade to label.
        musicByLabel.colorLabelMedium();
        
        // Add label to scene graph.
        mainStage.addActor(musicByLabel.displayLabelCenterX(200, viewWidthMain));
        
        // 7.  Load atlas information, including regions and items.
        
        // Store path of top-level directory containing packages.
        parentDir = (new File("/")).getAbsolutePath();
        
        // Store path of directory containing JSON files.
        jsonDir = parentDir + "src\\json\\";
        
        // Store JSON directory in game class.
        gameHD.setJsonDir(jsonDir);
        
        // Store full path to JSON file with atlas information.
        jsonFullPath = (jsonDir + "Atlas.json").replaceAll("\\\\", "/").replaceAll(" ","%20");
        
        // Initialize the JSON processor.
        json = new JSON_Processor();
        
        // Read the atlas from a file.
        gameHD.setAtlas(json.readAtlas(jsonFullPath));
        
        // Store full path to JSON file with atlas item information.
        jsonFullPath = (jsonDir + "AtlasItems.json").replaceAll("\\\\", "/").replaceAll(" ","%20");
        
        // Read the atlas items from a file.
        gameHD.setAtlasItems(json.readAtlasItems(jsonFullPath, gameHD.getAtlas().getMapCount()));
        
    }
    
    // button = Reference to BaseActor for the button.
    public void addEvent(BaseActor button)
    {
        
        // The function adds events to the passed button (BaseActor).
        // Events include touchDown, touchUp, enter, and exit.
        
        InputListener buttonEvent; // Events to add to passed button (BaseActor).
        
        // Craft event logic to add to passed button (BaseActor).
        buttonEvent = new InputListener()
            {
                
                boolean ignoreNextExitEvent; // Whether to ignore next exit event (used with touchUp / exit).
                
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
                    
                    // Flag to ignore next exit event.
                    ignoreNextExitEvent = true;
                    
                    // Switch to the title screen.
                    gameHD.setTitleScreen();
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // fromActor = Reference to actor losing focus.
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
                {
                    
                    // The function occurs when the mouse cursor or a finger touch is moved over the actor.
                    
                    // Notes:  On the desktop, the event occurs even when no mouse buttons are pressed 
                    // (pointer will be -1).
                    // Notes:  Occurs when mouse cursor or finger touch is moved over the label.
                    
                    // Apply a dark shade to the start button.
                    startButton.setColor(Color.LIGHT_GRAY);
                    
                }
                
                // event = Event for actor input: touch, mouse, keyboard, and scroll.
                // x = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
                // y = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
                // pointer = Pointer for the event.
                // toActor = Reference to actor gaining focus.
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
                {
                    
                    // The function occurs when the mouse cursor or a finger touch is moved out of the actor.
                    
                    // Notes:  On the desktop, the event occurs even when no mouse buttons are pressed 
                    // (pointer will be -1).
                    // Notes:  Occurs when mouse cursor or finger touch is moved out of the label.
                    
                    // If ignoring next exit event, then...
                    if (ignoreNextExitEvent)
                        
                        // Ignoring next exit event.
                        
                        // Flag to process next exit event.
                        ignoreNextExitEvent = false;
                    
                    // Otherwise, ...
                    else
                        
                        // Process exit event.
                        
                        // Return start button to normal color.
                        startButton.setColor(Color.WHITE);
                        
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
    
    private void postLoad()
    {
        
        /*
        The function performs actions after the loading of assets.
        
        Operations include:
        
        1.  Allocate space in arrays.
        2.  Loads texture regions related to atlases.
        3.  Configures and adds the start button Actor.
        4.  Configures and adds the label next to the start button.
        5.  Populate hash maps related to sounds and music.
        6.  Split tile regions for use with explore screen.
        7.  Hides the progress bar.
        */
        
        int counter; // Used to count through regions in current tile -- for splitting.
        String key; // Image key for the current tile in the loop -- used with the asset manager hash map.
        float posX; // X-coordinate at which to place start label.
        TextureRect[] rects; // List of regions in tiles to extract -- x, y, width, height.
        String startButtonKey; // Key in asset manager for texture region for start button.
        
        // 1.  Allocate space in arrays.
        rects = new TextureRect[HeroineEnum.TileRegionEnum.values().length];
        
        // 2.  Load texture regions related to atlases.
        gameHD.getAssetMgr().loadTextureRegions(atlasKeyList);
        
        // 3.  Configure and add the start button Actor.
        
        // Create new BaseActor for the start button.
        startButton = new BaseActor();
        
        // Name background actor.
        startButton.setActorName("Start Button");
        
        // Store asset manager key for start button.
        startButtonKey = HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK.getValue_Key();
        
        // Assign the Texture to the start button Actor.
        startButton.setTextureRegion( gameHD.getAssetMgr().getTextureRegion(startButtonKey) );
        
        // Position the start button close to the lower left corner of the screen.
        startButton.setPosition( 50, 50 );
        
        // Scale the start button to half original size.
        //startButton.setScaleX( 0.50f );
        //startButton.setScaleY( 0.50f );
        startButton.setWidth( startButton.getWidth() * 0.50f );
        startButton.setHeight( startButton.getHeight() * 0.50f );
        
        // Add events to start button.
        addEvent( startButton );
        
        // Add the start button Actor to the scene graph.
        mainStage.addActor( startButton );
        
        // 4.  Configure and add the label next to the start button.
        
        // Initialize label with start text.
        startLabel = new CustomLabel(game.skin, "START", "uiLabelStyle", 1.0f, 
          gameHD.getConfig().getTextLineHeight(), HeroineEnum.FontEnum.FONT_UI.getValue_Key());
        
        // Add effect to label.
        startLabel.addAction_FadePartial( 222f, 238f, 214f, 43f, 70f, 30f );
        
        // Calculate x-coordinate at which to place start label.
        posX = (startButton.getX() + startButton.getWidth()) * 1.1f;
        
        // Add label to scene graph.
        mainStage.addActor(startLabel.displayLabel( posX, startButton.getY()) );
        
        // 5.  Populate hash maps related to sounds and music.
        gameHD.getSounds().mapAudioHashMaps( gameHD );
        
        // 6.  Split tile regions for use with explore screen.
        
        // Loop through tile image enumerated values.
        for (HeroineEnum.ImgTileEnum imgTileEnum : HeroineEnum.ImgTileEnum.values())
        {
            
            // Reset counter.
            counter = 0;
            
            // If NOT the placeholder, then...
            if (imgTileEnum != HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE)
            {
                
                // NOT the placeholder.
                
                // Store the image key for the current tile.
                key = imgTileEnum.getValue_Key();

                // Clear region information array.
                Arrays.fill(rects, null);

                // Loop through tile region enumerated values.
                for (HeroineEnum.TileRegionEnum tileRegionEnum : HeroineEnum.TileRegionEnum.values())
                {

                    // Load one piece of region information for the current tile - x, y, width, height.
                    // Apply scale factor to get proper region.
                    rects[counter] = new TextureRect(tileRegionEnum.getValue_TextureRect(), gameHD.getConfig().getScale());

                    // Increment counter.
                    counter++;
                }

                // Split tile texture regions.
                gameHD.getAssetMgr().loadTextureRegionsDynamic(key, rects);
                
            }
              
        } // End ... Loop through tile image enumerated values.
        
        // 7.  Hide the progress bar.
        progressBar.hideBar();
        
    }
    
    private void queueAssets()
    {
        
        /*
        The function queues the assets for loading, based on the current size of the application window.
        
        The following actions occur:
        
        1.  Displays progress bar in center of screen -- update when loading assets.
        2.  Stores values in load lists.
        3.  Queues images.
        4.  Queues atlases.
        5.  Queues sounds.
        6.  Queues music (ogg format).
        */
        
        // Declare object variables.
        ArrayList<String> atlasMapList; // List of atlas paths and keys (path, key, path, key, ...) for later 
          // addition to hash map.
        ArrayList<String> atlasPathList; // List of paths to atlases to load.
        ArrayList<String> imageMapList; // List of image paths and keys (path, key, path, key, ...) for later 
          // addition to hash map.
        ArrayList<String> imagePathList; // List of paths to images to load.
        
        // Declare regular variables.
        String atlasPath; // Path to atlas to load.
        String imagePath; // Path to image to load.
        
        // Initialize array list.
        atlasKeyList = new ArrayList<>();
        atlasMapList = new ArrayList<>();
        atlasPathList = new ArrayList<>();
        imageMapList = new ArrayList<>();
        imagePathList = new ArrayList<>();
        
        // 1.  Display progress bar in center of screen -- update when loading assets.
        uiStage.addActor(progressBar.displayBarCenterHorz(viewWidthMain, 175));
        
        // 2.  Store values in load lists.
        
        // If using a manually scaled size, then...
        if (gameHD.getConfig().getStretchToScreen())
            
            {
            // Using a manually scaled size.
            
            // Loop through background image enumerations.
            for (HeroineEnum.ImgBackgroundEnum imgEnum : HeroineEnum.ImgBackgroundEnum.values())
                
                {
                // Specify image path.
                imagePath = "assets/backgrounds/" + imgEnum.getValue_File();

                // Add to lists.
                imageMapList.add(imagePath);
                imageMapList.add(imgEnum.getValue_Key());
                imagePathList.add(imagePath);
                }
            
            // Loop through tile image enumerations.
            for (HeroineEnum.ImgTileEnum imgEnum : HeroineEnum.ImgTileEnum.values())
                
                {
                // Specify image path.
                imagePath = "assets/tiles/" + imgEnum.getValue_File();

                // Add to lists.
                imageMapList.add(imagePath);
                imageMapList.add(imgEnum.getValue_Key());
                imagePathList.add(imagePath);
                }
            
            // Remove placeholder related to tile image enumerations.
            imageMapList.remove("assets/tiles/" + HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE.getValue_File());
            imageMapList.remove(HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE.getValue_Key());
            imagePathList.remove("assets/tiles/" + HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE.getValue_File());
            
            // Loop through interface image enumerations.
            for (HeroineEnum.ImgInterfaceEnum imgEnum : HeroineEnum.ImgInterfaceEnum.values())
                
                {
                // Specify image path.
                imagePath = "assets/interface/" + imgEnum.getValue_File();
                
                // Specify atlas path.
                atlasPath = "assets/interface/" + imgEnum.getValue_AtlasFile();
                
                // If atlas exists then, ...
                if (atlasPath.length() > 0)
                    {
                    // Atlas exists.
                    
                    // Add to atlas lists.
                    atlasKeyList.add(imgEnum.getValue_AtlasKey());
                    atlasMapList.add(atlasPath);
                    atlasMapList.add(imgEnum.getValue_AtlasKey());
                    atlasPathList.add(atlasPath);
                    }
                
                else
                    {
                    // No atlas exists.
                        
                    // Add to standard texture lists.
                    imageMapList.add(imagePath);
                    imageMapList.add(imgEnum.getValue_Key());
                    imagePathList.add(imagePath);
                    }
                    
                }
            
            }
        
        else
            
            {
            // Using a prescaled size.
            
            // Loop through background image enumerations.
            for (HeroineEnum.ImgBackgroundEnum imgEnum : HeroineEnum.ImgBackgroundEnum.values())
                
                {
                // Specify image path.
                imagePath = gameHD.getConfig().getPrescaleFolder_Backgrounds() + imgEnum.getValue_File();

                // Add to lists.
                imageMapList.add(imagePath);
                imageMapList.add(imgEnum.getValue_Key());
                imagePathList.add(imagePath);
                }
            
            // Loop through tile image enumerations.
            for (HeroineEnum.ImgTileEnum imgEnum : HeroineEnum.ImgTileEnum.values())
                
                {
                // Specify image path.
                imagePath = gameHD.getConfig().getPrescaleFolder_Tiles() + imgEnum.getValue_File();

                // Add to lists.
                imageMapList.add(imagePath);
                imageMapList.add(imgEnum.getValue_Key());
                imagePathList.add(imagePath);
                }
            
            // Remove placeholder related to tile image enumerations.
            imageMapList.remove(gameHD.getConfig().getPrescaleFolder_Tiles() + HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE.getValue_File());
            imageMapList.remove(HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE.getValue_Key());
            imagePathList.remove(gameHD.getConfig().getPrescaleFolder_Tiles() + HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE.getValue_File());
            
            // Loop through interface image enumerations.
            for (HeroineEnum.ImgInterfaceEnum imgEnum : HeroineEnum.ImgInterfaceEnum.values())
                
                {
                // Specify image path.
                imagePath = gameHD.getConfig().getPrescaleFolder_Interface() + imgEnum.getValue_File();
                
                // Specify atlas path.
                atlasPath = gameHD.getConfig().getPrescaleFolder_Interface() + imgEnum.getValue_AtlasFile();
                
                // If atlas exists then, ...
                // Note:  Use imgEnum.getValue_AtlasFile().length() to avoid all iterations "having" an atlas.
                if (imgEnum.getValue_AtlasFile().length() > 0)
                    {
                    // Atlas exists.
                    
                    // Add to atlas list.
                    atlasKeyList.add(imgEnum.getValue_AtlasKey());
                    atlasMapList.add(atlasPath);
                    atlasMapList.add(imgEnum.getValue_AtlasKey());
                    atlasPathList.add(atlasPath);
                    }
                
                else
                    {
                    // No atlas exists.
                    
                    // Add to standard texture lists.
                    imageMapList.add(imagePath);
                    imageMapList.add(imgEnum.getValue_Key());
                    imagePathList.add(imagePath);
                    }
                
                }
                
            }
        
        // 3.  Queue images.
        gameHD.getAssetMgr().queueImages(imagePathList);
        gameHD.getAssetMgr().mapImages(imageMapList);
        
        // 4.  Queue atlases.
        gameHD.getAssetMgr().queueAtlases(atlasPathList);
        gameHD.getAssetMgr().mapAtlases(atlasMapList);
        
        // 5.  Queue sounds.
        
        // Loop through sounds (via enumerations).
        for (HeroineEnum.SoundEnum sound : HeroineEnum.SoundEnum.values()) {
        
            // Add sound to queue.
            gameHD.getAssetMgr().queueSounds(sound.getValue_FilePath());
            
        }
        
        // 6.  Queue music (ogg format).
        
        // Loop through music (via enumerations).
        for (HeroineEnum.MusicEnum music : HeroineEnum.MusicEnum.values()) {
            
            // Add music to queue.
            gameHD.getAssetMgr().queueMusic(music.getValue_File_ogg());
            
        }
        
    }
    
    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void update(float dt) 
    {   
        
        /*
        The function occurs during the update phase (render method).
        
        The following operations occur:
        
        (While loading assets and asset manager needs to load resources)
        1.  Continues loading.
        2.  Updates progress bar with load status.
        3.  Display load status.
        
        (Upon completion loading assets -- just once)
        1.  Updates progress bar to show 100% completion.
        2.  Performs post load operations.
        3.  Flags as finished loading.
        */
        
        float progress; // Percent of loading completed.
        
        // If still loading, then...
        if (stillLoading)
        {
            
            // Still loading.
            
            // While asset manager loads resources, ...
            if(gameHD.getAssetMgr().manager.update())
            {
                // Finished loading assets.
                
                // Update progress bar to show 100%.
                progressBar.setValue(1.0f);
                
                // Perform post-load processes.
                postLoad();
                
                // Flag as finished loading.
                stillLoading = false;
            }
            
            else
            {
                // Still loading...
                
                // Get percent of loading completed.
                progress = gameHD.getAssetMgr().manager.getProgress();
                
                // Display load status.
                System.out.println("Loading Status ... Asset " + 
                  gameHD.getAssetMgr().manager.getAssetNames().size + " ... " + progress * 100 + "%");

                // Update progress bar.
                progressBar.setValue(progress);
            }
            
        }
        
    }
    
}
