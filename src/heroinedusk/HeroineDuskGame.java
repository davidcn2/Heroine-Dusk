package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

// Local project imports.
import core.AssetMgr;
import core.BaseGame;
import screens.DialogScreen;
import screens.ExploreScreen;
import screens.GameScreen;
import screens.IntroScreen;
import screens.TitleScreen;

public class HeroineDuskGame extends BaseGame // Extends the BaseGame class.
{
    
    /*
    The class extends the basic functionality of a BaseGame class and creates a new GameScreen object
    with the Heroine Dusk game.  The Heroine Dusk game launches when the application starts.
    
    Methods include:
    
    create:  Sets up the skin and initializes and displays the title screen.
    createSkin:  Sets up the skin.
    dispose:  Occurs during the cleanup phase and clears objects from memory.
    disposeScreens:  Disposes of LibGDX objects in screens.
    renderDialogScreen:  Renders the dialog screen, incorporating any necessary updates.
    setDialogScreen:  Switches to (displays) the dialog screen and hides the current.
    setGameScreen:  Switches to (displays) the main game screen and hides the current.
    setIntroScreen:  Switches to (displays) the introduction screen and hides the current.
    setTitleScreen:  Switches to (displays) the title screen and hides the current.
    */
    
    // Declare object variables.
    private AssetMgr assetMgr; // Enhanced asset manager.
    private Atlas atlas; // Atlas containing all map / region information.
    private AtlasItems atlasItems; // Atlas item information.
    private Avatar avatar; // Player information.
    private final Config config; // Configuration information, including options.
    private Dialog dialog; // Contains information related to current dialog window.
    private static DialogScreen dsMain; // Reference to dialog screen.
    private static ExploreScreen esMain; // Reference to explore screen.
    private static GameScreen gsMain; // Reference to main game screen.
    private static IntroScreen isMain; // Reference to introduction screen.
    private Shops shopInfo; // Contains message-related information, mostly used for shops.
    private Sounds sounds; // Contains logic related to playing sounds and music.
    private static TitleScreen tsMain; // Reference to title screen.
    
    // Declare regular variables.
    private HeroineEnum.GameState gameState; // Game state.  See enumerated values in HeroineEnum for more details.
    private String jsonDir; // Directory containing JSON files.
    private int windowWidth; // Width to use for stages.
    private int windowHeight; // Height to use for stages.
    
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    public HeroineDuskGame(int windowWidth, int windowHeight)
    {
        
        // The constructor sets the values for the starting width and height of the application window.
        
        // Store passed in class-level variables.
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        
        // Initialize configuration information, including options.
        config = new Config(windowWidth, windowHeight);
        
        // Initialize the asset manager.
        assetMgr = new AssetMgr();
        
        // Initialize player information.
        avatar = new Avatar();
        
        // Initialize the array containing shops and other messaging information.
        shopInfo = new Shops(this);
        
        // Initialize current dialog window information.
        dialog = new Dialog();
        
        // Initialize sound and music information.
        sounds = new Sounds();
        
    }
    
    @Override
    public void create() 
    {
        
        // The function sets up the skin and initializes and displays the title screen.
        // The function is automatically called by the superclass.
        
        // Set defaults.
        gameState = HeroineEnum.GameState.STATE_TITLE;
        
        // Set up the skin.
        createSkin();
        
        // Initialize and display the introduction screen.
        //setTitleScreen();
        setIntroScreen();
        
    }
    
    private void createSkin()
    {
        
        // The function sets up the skin.
        
        // 1.  Configures skin properties for progress bar.
        // 2.  Configures skin properties for regular font.
        
        ProgressBarStyle barStyle; // Style associated with progress bar.
        Pixmap pixmap; // Used for images, such as for the progress bar.
        TextureRegionDrawable textureBar; // Drawable object to use for background of progress bar.
        BitmapFont uiFont; // Reference to BitmapFont resource providing bitmapped font and associated details.
        Label.LabelStyle uiLabelStyle; // Reference to BitmapFont resource with a color specified.
        
        // 1a.  Set up the background for the progress bar.
        
        // Create pixel map of width and height of 100 by 20, respectively.
        pixmap = new Pixmap(100, 20, Format.RGBA8888);
        
        // Set the color of the pixel map to dark gray.
        pixmap.setColor(Color.DARK_GRAY);
        
        // Fill the complete bitmap with the currently set color.
        pixmap.fill();
        
        // Since the style requires Drawable objects, convert the Pixmap to TextureRegionDrawable.
        textureBar = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        
        // Clear the pixel map from memory.
        pixmap.dispose();
        
        // Configure the style.
        barStyle = new ProgressBarStyle();
        barStyle.background = textureBar;
        
        // 1b.  Set up the pixel map for the knob portion of the progress bar.
        
        // Create pixel map of width and height of 0 by 20, respectively.
        // Use a width of 0, since no knob will display.
        pixmap = new Pixmap(0, 20, Format.RGBA8888);
        
        // Set the color of the pixel map to a shade of green.
        pixmap.setColor(Color.rgb888(136, 224, 96));
        
        // Fill the complete bitmap with the currently set color.
        pixmap.fill();
        
        // Since the style requires Drawable objects, convert the Pixmap to TextureRegionDrawable.
        textureBar = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        
        // Clear the pixel map from memory.
        pixmap.dispose();
        
        // Add the knob to the style.
        barStyle.knob = textureBar;
        
        // 1c.  Set up the pixel map for the area before the knob portion of the progress bar.
        
        // Create pixel map of width and height of 100 by 20, respectively.
        pixmap = new Pixmap(100, 20, Format.RGBA8888);
        
        // Set the color of the pixel map to a shade of green.
        pixmap.setColor(Color.rgb888(136, 224, 96));
        
        // Fill the complete bitmap with the currently set color.
        pixmap.fill();
        
        // Since the style requires Drawable objects, convert the Pixmap to TextureRegionDrawable.
        textureBar = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        
        // Clear the pixel map from memory.
        pixmap.dispose();

        // Add the knob to the style.
        barStyle.knobBefore = textureBar;
        
        // Add the progress bar style to the Skin.  Skins allow for storing resources common to multiple screens.
        skin.add("barStyle", barStyle);
        
        // 2a.  Set up the regular (ui) font.
        
        // Initialize the BitmapFont object with a FileHandle to the FNT file.
        //uiFont = new BitmapFont(Gdx.files.internal("assets/interface/Roboto.fnt"));
        uiFont = new BitmapFont(Gdx.files.internal(config.getPrescaleFolder_Interface() + "boxy_bold.fnt"));
        
        // Store text line height.
        config.setTextLineHeight(uiFont.getXHeight());
        
        /*
        Access the Texture data contained within the BitmapFont object.  Getting a reference to the
        Texture data allows actions like setting a filter to obtain a smoother appearance when scaling images.
        
        Set filter type -- controlling how pixel colors are interpolated when image is
        rotated or stretched.
        
        > Nearest:  To represent each pixel on the screen, the method uses the pixel of the texture (texel) 
        that best matches to the pixel of the screen.  This is the default filter. As this filter only uses one 
        texel for every pixel on the screen, the method applies the filter very quickly.  The result is an image 
        with “hard” borders.
        
        > Linear:  To represent each pixel on the screen, the method uses bilinear interpolation, taking 
        the four closest texels that best match with the screen pixel.  The result is smooth scaling.
        But, processing costs will also be bigger than GL_NEAREST.
        */
        uiFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        // Add the uiFont object to the Skin.  Skins allow for storing resources common to multiple screens.
        skin.add("uiFont", uiFont);
        
        // 2b.  Initialize Label style.
        
        // Create resource with the BitmapFont just produced and no color.
        uiLabelStyle = new LabelStyle(uiFont, null);
        
        // Add the LabelStyle object to the Skin.
        skin.add("uiLabelStyle", uiLabelStyle);
        
    }
    
    @Override
    public void dispose()
    {

        // The function occurs during the cleanup phase and clears objects from memory.
        
        // Dispose objects in screens from memory.
        disposeScreens();
        
        // Clear objects from memory.
        super.dispose();

    }
    
    public void disposeScreens()
    {
        
        // The function disposes of LibGDX objects in screens.
        // The function also disposes of additional LibGDX ojects, such as those related to sounds and music.
        // The function also clears memory related to the asset manager.
        
        // Dispose of LibGDX objects related to introduction screen.
        isMain.dispose();
        
        // If initialized, dispose of LibGDX objects related to explore screen.
        if (esMain != null)
            esMain.dispose();
        
        // If main game screen initialized, then...
        if (gsMain != null)
            
            // Main game screen initialized.
            // Dispose of LibGDX objects related to main game screen.
            gsMain.dispose();
        
        // If initialized, dispose of LibGDX objects related to title screen.
        if (tsMain != null)
            tsMain.dispose();
        
        // If initialized, dispose of LibGDX objects related to dialog screen.
        if (dsMain != null)
            dsMain.dispose();
        
        // Dispose of sound and music objects.
        sounds.disposeAudio();
        
        // Clear LibGDX asset manager.
        assetMgr.disposeAssetMgr();
        
    }
    
    protected void renderDialogScreen()
    {
        
        // Renders the dialog screen, incorporating any necessary updates.
        
        // Render the dialog screen.
        dsMain.render();
        
    }
    
    public void setDialogScreen()
    {
        
        // The function switches to (displays) the dialog screen and hides the current.
        
        // Switch to dialog state.
        gameState = HeroineEnum.GameState.STATE_DIALOG;
        
        // Initialize dialog screen object.
        dsMain = new DialogScreen(this, windowWidth, windowHeight, this, dialog);
        
        // Launch dialog screen.
        setScreen(dsMain);
        
    }
    
    public void setExploreScreen()
    {
        
        // The function switches to (displays) the explore screen and hides the current.
        
        // Update game state to title screen.
        gameState = HeroineEnum.GameState.STATE_EXPLORE;
        
        // If explore screen object initialized, then...
        if (esMain == null)
            {
            // Explore screen object not initialized yet.
            // Initialize explore screen object.
            esMain = new ExploreScreen(this, windowWidth, windowHeight);
            }
        else
            {
            // Explore screen object initialized already.
            // Update multiplexer and display.
            esMain.wakeScreen();
            }
            
        // Launch explore screen.
        setScreen(esMain);   
        
    }
    
    protected void setIntroScreen()
    {
        
        // The function switches to (displays) the introduction screen and hides the current.
        
        // Initialize introduction screen object.
        isMain = new IntroScreen(this, windowWidth, windowHeight);
        
        // Launch introduction screen.
        setScreen(isMain);
        
    }
    
    // resetGame = Whether to reset game.
    public void setGameScreen(boolean resetGame)
    {
        
        // The function switches to (displays) the main game screen and hides the current.
        
        // If main game screen object initialized, then...
        if (gsMain == null)
            {
            // Main game screen object not initialized yet.
            // Initialize main game screen object.
            gsMain = new GameScreen(this, windowWidth, windowHeight, this);
            }
        else
            {
            // Main game screen object initialized already.
            // Update multiplexer and display.
            gsMain.wakeScreen();
            }
        
        // Launch main game screen.
        setScreen(gsMain);
        
    }
    
    public void setTitleScreen()
    {
        
        // The function switches to (displays) the title screen and hides the current.
        
        // Update game state to title screen.
        gameState = HeroineEnum.GameState.STATE_TITLE;
        
        // If title screen object initialized, then...
        if (tsMain == null)
            {
            // Title screen object not initialized yet.
            // Initialize title screen object.
            tsMain = new TitleScreen(this, windowWidth, windowHeight, this);
            }
        else
            {
            // Title screen object initialized already.
            // Update multiplexer and display.
            tsMain.wakeScreen();
            }
            
        // Launch title screen.
        setScreen(tsMain);
        
    }
    
    // Getters and setters below...

    public AssetMgr getAssetMgr() {
        return assetMgr;
    }

    public Atlas getAtlas() {
        return atlas;
    }

    public AtlasItems getAtlasItems() {
        return atlasItems;
    }
    
    public void setAtlas(Atlas atlas) {
        this.atlas = atlas;
    }
    
    public void setAtlasItems(AtlasItems atlasItems) {
        this.atlasItems = atlasItems;
    }
    
    public Avatar getAvatar() {
        return avatar;
    }

    public Config getConfig() {
        return config;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public HeroineEnum.GameState getGameState() {
        return gameState;
    }

    public void setGameState(HeroineEnum.GameState gameState) {
        this.gameState = gameState;
    }
    
    public String getJsonDir() {
        return jsonDir;
    }

    public void setJsonDir(String jsonDir) {
        this.jsonDir = jsonDir;
    }
    
    public Shops getShopInfo() {
        return shopInfo;
    }

    public Sounds getSounds() {
        return sounds;
    }

    public void setSounds(Sounds sounds) {
        this.sounds = sounds;
    }

}