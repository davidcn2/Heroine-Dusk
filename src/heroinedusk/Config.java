package heroinedusk;

public class Config 
{
    
    // Declare object variables.
    private Options options; // Information from options screen, including flags for animation, music, and 
      // sound effects.
    
    // Declare regular variables.
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
        prescaleFolder = "assets/prescaled/_" + String.valueOf(prescale * 160) + "x" + 
          String.valueOf(prescale * 120) + "/";
        prescaleFolder_Backgrounds = prescaleFolder + "backgrounds";
        prescaleFolder_Enemies = prescaleFolder + "enemies";
        prescaleFolder_Interface = prescaleFolder + "interface";
        prescaleFolder_Tiles = prescaleFolder + "tiles";
        stretchToScreen = false;
        scale = prescale;
        
        // Initialize options object.
        options = new Options(true, true, true);
        
    }
    
    public int getViewHeight() {
        return viewHeight;
    }

    public int getViewWidth() {
        return viewWidth;
    }
    
}
