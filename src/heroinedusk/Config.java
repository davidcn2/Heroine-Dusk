package heroinedusk;

// Java imports.
import routines.ArrayRoutines;

public class Config 
{
    
    /*
    Possible Scale Values:
    
    1 = 160 x 120.
    2 = 320 x 240.
    3 = 480 x 360.
    4 = 640 x 480.
    5 = 800 x 600.
    6 = 960 x 720.
    7 = 1120 x 840.
    8 = 1200 x 960.
    */
    
    // Declare object variables.
    private Options options; // Information from options screen, including flags for animation, music, and 
      // sound effects.
    
    // Declare regular variables.
    private int menuTop[]; // Y-coordinate positions at which to place first menu item in title screen.
    private int menuTopCurr; // Current y-coordinate at which to place first menu item in title screen --
      // based on scale factor.
    private int prescale; // Prescale factor.  Multiply by 160 and 120 (for width and height, respectively) 
      // to determine which set of prescaled images to use.  Example:  4 = 640 x 480.
    private String prescaleFolder; // Prefix for folders associated with prescale factor.
    private String prescaleFolder_Backgrounds; // Folder to use for backgrounds, based on prescale factor.
    private String prescaleFolder_Enemies; // Folder to use for enemies, based on prescale factor.
    private String prescaleFolder_Interface; // Folder to use for interface, based on prescale factor.
    private String prescaleFolder_Tiles; // Folder to use for tiles, based on prescale factor.
    private int scale; // Output scale factor -- multiple of 160 and 120.
      // If stretchToScreen = true, determines based on window size.
      // If stretchToScreen = false, uses value in prescale.
    private boolean stretchToScreen; // Whether to stretch images to view / window dimensions.
      // true = Manually scaled images, false = Prescaled images.
    private float textLineHeight; // Height of each line of text.
    private int viewHeight; // Window height.  Also maintained in BaseScreen.
    private int viewWidth; // Window width.  Also maintained in BaseScreen.
    
    // mapWidth = Total map width, in pixels.  View and map width remain equal in Heroine Dusk.
    // mapHeight = Total map height, in pixels.  View and map height remain equal in Heroine Dusk.
    public Config(int mapWidth, int mapHeight)
    {
        
        // Requires passing in of map width and height meeting one of the available prescaled options.
        
        // Prescale options:
        // 5 = 800 x 600.
        // 6 = 960 x 720.
        // 8 = 1280 x 960.
        
        // Set defaults.
        viewWidth = mapWidth;
        viewHeight = mapHeight;
        prescale = mapWidth / 160;
        prescaleFolder = "assets/prescaled/_" + String.format ("%04d", (prescale * 160)) + "x" + 
          String.valueOf(prescale * 120) + "/";
        prescaleFolder_Backgrounds = prescaleFolder + "backgrounds/";
        prescaleFolder_Enemies = prescaleFolder + "enemies/";
        prescaleFolder_Interface = prescaleFolder + "interface/";
        prescaleFolder_Tiles = prescaleFolder + "tiles/";
        stretchToScreen = false; // Using prescaled images.
        scale = prescale;
        
        // Initialize arrays.
        menuTop = new int[8];
        
        // Initialize options object.
        options = new Options(true, true, true, false);
        
        // Populate array with y-positions at which to place first item in tile screen menu (depending on scale).
        ArrayRoutines.addAll(menuTop, 0, 0, 0, 0, 0, 374, 0, 0);
        
        // Determine current y-position at which to place top of menu on title screen.
        menuTopCurr = menuTop[prescale - 1];
        
    }
    
    // Getters and setters below...
    
    public int getMenuTop() {
        return menuTopCurr;
    }
    
    public Options getOptions() {
        return options;
    }
    
    public String getPrescaleFolder_Backgrounds() {
        return prescaleFolder_Backgrounds;
    }

    public String getPrescaleFolder_Enemies() {
        return prescaleFolder_Enemies;
    }

    public String getPrescaleFolder_Interface() {
        return prescaleFolder_Interface;
    }

    public String getPrescaleFolder_Tiles() {
        return prescaleFolder_Tiles;
    }
    
    public int getScale() {
        return scale;
    }
    
    public boolean getStretchToScreen() {
        return stretchToScreen;
    }
    
    public float getTextLineHeight() {
        return textLineHeight;
    }
    
    public int getViewHeight() {
        return viewHeight;
    }

    public int getViewWidth() {
        return viewWidth;
    }
    
    public void setTextLineHeight(float textLineHeight) {
        this.textLineHeight = textLineHeight;
    }
    
}
