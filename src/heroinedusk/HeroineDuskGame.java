package heroinedusk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import core.BaseGame;

public class HeroineDuskGame extends BaseGame
{
    
    // The class extends the basic functionality of a BaseGame class and creates a new GameScreen object
    // with the Heroine Dusk game.  The Heroine Dusk game launches when the application starts.
    
    // Methods include:
    
    // create:  Creates a new GameScreen object used to set to the main screen when the
    //          application starts.
    
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
        
    }
    
    @Override
    public void create() 
    {
        // The function creates a new GameScreen object used to go to the main screen
        // when the application starts.
        
        // Set up the skin.
        createSkin();

        // Create new GameScreen, which will act as the level object.
        GameScreen gs = new GameScreen(this, windowWidth, windowHeight);
        
        // Set the screen to the one just created.
        setScreen( gs );
    }
    
    private void createSkin()
    {
        
        // The function sets up the skin.
        
        ProgressBarStyle barStyle; // Style associated with progress bar.
        Pixmap pixmap; // Used for images, such as for the progress bar.
        TextureRegionDrawable textureBar; // Drawable object to use for background of progress bar.
        
        // 1.  Set up the background for the progress bar.
        
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
        
        // 2.  Set up the pixel map for the knob portion of the progress bar.
        
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
        
        // 3.  Set up the pixel map for the area before the knob portion of the progress bar.
        
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
        
    }
    
}