package heroinedusk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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

import core.BaseGame;

public class HeroineDuskGame extends BaseGame // Extends the BaseGame class.
{
    
    /*
    The class extends the basic functionality of a BaseGame class and creates a new GameScreen object
    with the Heroine Dusk game.  The Heroine Dusk game launches when the application starts.
    
    Methods include:
    
    create:  sets up the skin and initializes and displays the title screen.
    createSkin:  Sets up the skin.
    disposeScreens:  Disposes of LibGDX objects in screens.
    setGameScreen:  Switches to (displays) the main game screen and hides the current.
    setTitleScreen:  Switches to (displays) the title screen and hides the current.
    */
    
    // Declare object variables.
    protected final Config config; // Configuration information, including options.
    private static GameScreen gsMain; // Reference to main game screen.
    private static TitleScreen tsMain; // Reference to title screen.
    
    // Declare regular variables.
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
        
    }
    
    @Override
    public void create() 
    {
        
        // The function sets up the skin and initializes and displays the title screen.
        
        // Set up the skin.
        createSkin();
        
        // Initialize and display the title screen.
        setTitleScreen();
        
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
        uiFont = new BitmapFont(Gdx.files.internal("assets/interface/Roboto.fnt"));
        
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
        
        // Create resource with the BitmapFont just produced and a color.
        uiLabelStyle = new LabelStyle(uiFont, Color.valueOf("DEEED6"));
        
        // Add the LabelStyle object to the Skin.
        skin.add("uiLabelStyle", uiLabelStyle);
        
    }
    
    protected void disposeScreens()
    {
        
        // The function disposes of LibGDX objects in screens.
        gsMain.dispose();
        tsMain.dispose();
        
    }
    
    protected void setGameScreen()
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
    
    protected void setTitleScreen()
    {
        
        // The function switches to (displays) the title screen and hides the current.
        
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
    
}