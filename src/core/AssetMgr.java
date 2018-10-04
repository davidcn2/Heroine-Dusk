package core;

// LibGDX imports.
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Local project imports.
import gui.CustomProgressBar;
import heroinedusk.HeroineEnum;

// Java imports.
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

Abstract:  Abstract classes are similar to interfaces.  You cannot instantiate them, and they may
contain a mix of methods declared with or without an implementation. However, with abstract classes,
you can declare fields that are not static and final, and define public, protected, and private
concrete methods.

Abstract:  Abstract classes are similar to interfaces.  You cannot instantiate them, and they may
contain a mix of methods declared with or without an implementation. However, with abstract classes,
you can declare fields that are not static and final, and define public, protected, and private
concrete methods.

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.

ArrayList supports dynamic arrays that can grow as needed.
*/

public class AssetMgr
{
    
    /* 
    The class provides for expanded and simplified use of the LibGDX asset manager.
    Note:  Do NOT set up the asset manager as static, due to memory leaks / Android issues.
    
    Methods include:
    
    disposeAssetMgr:  Clears the asset manager from memory.
    getImage:  Returns the Texture from the asset manager with the passed key.
    getImage_xRef:  Returns the Texture from the asset manager based on the name in the cross reference.
    getMusicMp3:  Returns the requested music in mp3 format.
    getMusicOgg:  Returns the requested music in ogg format.
    getSound:  Returns the requested sound.
    loadResources:  Loads the current resources in the asset manager queue.
    loadTextureRegions:  loads all texture regions associated with the passed atlases.
    loadTextureRegionsDynamic:  Splits the texture with the passed key into regions.
    mapImages:  Adds cross reference (hash mapping) values to provide a simpler way of referencing
      (texture-related) items in the asset manager.
    queueImages:  Adds the passed images to the asset manager for future loading.
    */
    
    // Declare object variables.
    public AssetManager manager; // Loads and stores assets like textures, bitmap fonts, tile maps, 
      // sounds, music, ...
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, String> assetMapping_Atlases; // Cross reference between asset names and keys -- 
      // for atlases in asset manager.
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, String> assetMapping_Textures; // Cross reference between asset names and keys -- 
      // for textures in asset manager.
    private final Map<String, Rectangle2D.Float> textureRegionRects; // Contains rects related to texture regions in atlases.  Keys same as in atlas files.
    private final Map<String, TextureRegion> textureRegions; // Contains texture regions related to atlases (same keys as in the atlas files).
    
    // Declare constants.
    private final String decFormatText000 = "000"; // Text used for decimal style used to format numbers as 000.
      // Examples:  1 > 001, 2 > 002, ...
    private final DecimalFormat decimalFormat000 = new DecimalFormat(decFormatText000); // Decimal style used to
      // format numbers as 000.  Examples:  1 > 001, 2 > 002, ...
    
    public AssetMgr()
    {
        
        // The constructor creates an AssetManager and initializes hash maps.
        
        // Initialize AssetManager object.
        manager = new AssetManager();
        
        // Initialize the hash maps.
        assetMapping_Atlases = new HashMap<>();
        assetMapping_Textures = new HashMap<>();
        textureRegions = new HashMap<>();
        textureRegionRects = new HashMap<>();
        
    }
    
    // Methods below...
    
    public void disposeAssetMgr()
    {
        
        // The function clears the asset manager from memory.
        manager.dispose();
        
    }
    
    // key = Key value in asset manager for Atlas to return.
    public TextureAtlas getAtlas(String key)
    {
        
        // The function returns the Atlas from the asset manager with the passed key.
        
        // Return Atlas.
        return manager.get(key, TextureAtlas.class);
        
    }
    
    // xref_Key = Key value to use in cross reference when getting Atlas from asset manager.
    public TextureAtlas getAtlas_xRef(String xref_Key)
    {
        
        // The function returns the Atlas from the asset manager based on the name in the
        // cross reference (class-specific hash map) tied to the key passed.
        
        String xref_Value; // Value in cross reference -- matches to key in asset manager.
        
        // Determine cross reference value based on passed key.
        xref_Value = assetMapping_Atlases.get(xref_Key);
        
        // Return Atlas.
        return manager.get(xref_Value, TextureAtlas.class);
        
    }
    
    // key = Key value in asset manager for Texture to return.
    public Texture getImage(String key)
    {
        
        // The function returns the Texture from the asset manager with the passed key.
        
        // Return Texture.
        return manager.get(key, Texture.class);
        
    }
    
    // xref_Key = Key value to use in cross reference when getting Texture from asset manager.
    public Texture getImage_xRef(String xref_Key)
    {
        
        // The function returns the Texture from the asset manager based on the name in the
        // cross reference (class-specific hash map) tied to the key passed.
        
        String xref_Value; // Value in cross reference -- matches to key in asset manager.
        
        // Determine cross reference value based on passed key.
        xref_Value = assetMapping_Textures.get(xref_Key);
        
        // Return Texture.
        return manager.get(xref_Value, Texture.class);
        
    }
    
    public void loadResources()
    {
        
        // The function loads the current resources in the asset manager queue.
        
        // Load resources.
        manager.finishLoading();
        
    }
    
    // progressBar = Reference to progress bar to update while loading resources.
    public void loadResources(CustomProgressBar progressBar)
    {
        // The function displays a progress bar while loading the current resources in the asset manager queue.
        
        float progress; // Percent of loading completed.
        
        // Set defaults.
        progress = 0f;
        
        // Load resources.
        manager.finishLoading();
        
        // While asset manager loads resources, ...
        while(!manager.update())
            {
            // Get percent of loading completed.
            progress = manager.getProgress();
            
            // System.out.println("Loading ... " + progress * 100 + "%");
            
            // Update progress bar.
            progressBar.setValue(progress);
            }
        
        // If loading finished before loop could start, then manually set value of progress bar.
        if (progress == 0f)
            
            {
            // Loading occurred before loop could start.
            
            // Manually set value of progress bar to 100%.
            progressBar.setValue(1.0f);
            }
        
    }
    
    // atlasKeyList = List of atlas keys.
    public void loadTextureRegions(ArrayList<String> atlasKeyList)
    {
        
        // The function loads all texture regions associated with the passed atlases.
        // The function populates the hash maps with the texture regions and related rectangles.
        
        // Loop through atlases.
        for (String atlasKey : atlasKeyList)
        {
            
            // System.out.println("Loading texture regions for: " + atlasKey);
        
            // Loop through texture regions in atlas.
            for (AtlasRegion region : getAtlas_xRef(atlasKey).getRegions())
            {
                
                // Add current texture region to hash map.
                textureRegions.put(region.name, region);
                
                // Add rectangle for current texture region to hash map.
                textureRegionRects.put(region.name, new Rectangle2D.Float((float)region.getRegionX(), 
                  (float)region.getRegionY(), (float)region.getRegionWidth(), (float)region.getRegionHeight()));
                
            }
        
        }
        
    }
    
    // xref_Key = Key value to use in cross reference when getting Texture from asset manager.
    // rects = Regions in image (texture) to extract.  Defines how to split up the texture.
    public void loadTextureRegionsDynamic(String xref_Key, TextureRect ... rects)
    {
        
        // The function splits the texture with the passed key into regions.
        // The rects parameter defines how to split up the texture.
        
        int counter; // Used to iterate through regions being extracted.
        String key; // Key used when inserting values in hash maps.
        Texture tempTexture; // Temporary reference to Texture with passed key.
        TextureRegion tempTextureRegion; // Current texture region in split operation.
        
        // Set defaults.
        counter = 0;
        
        // Get reference to texture with passed key.
        tempTexture = getImage_xRef(xref_Key);
        
        // Loop through regions to extract.
        for (TextureRect rect : rects)
        {
            
            // Extract the current texture region.
            tempTextureRegion = new TextureRegion(tempTexture, rect.getSrc_x(), rect.getSrc_y(), 
              rect.getWidth(), rect.getHeight());
            
            // Determine key to use when inserting into hash maps.
            key = xref_Key + "_" + decimalFormat000.format(counter);
            
            // Add current texture region to hash map.
            textureRegions.put(key, tempTextureRegion);
            
            // Add rectangle for current texture region to hash map.
            textureRegionRects.put(key, rect.getRect2D());
            
            // Increment counter.
            counter++;
            
        }
        
    }
    
    // elements = Collection of items (key, value pairs) to add to hash map for atlases in asset manager.
    public void mapAtlases(String ... elements)
    {
        
        // The function adds cross reference (hash mapping) values to provide a simpler way of referencing
        // items in the asset manager.
        // The function populates the hash map specifically used for Atlas objects.
        // Pass values in the format / order:  asset name (when loading to manager), key value.
        
        // Example:  mapImages("assets/images/lasers.atlas", "lasers", "assets/images/rocks.atlas", "rocks");
        
        int elementCount; // Number of elements passed.  Divide by two to get number of pairs.
        int elementCounter; // Used to iterate through elements.
        int pairCount; // Number of hash pairs passed.
        
        // Set defaults.
        elementCounter = 0;
        
        // Count number of items passed.
        elementCount = elements.length;
        
        // Determine number of pairs.
        pairCount = elementCount / 2;
        
        // Loop through pairs.
        for (int pairCounter = 1; pairCounter <= pairCount; pairCounter++)
            {
            // Add current pair to hash map.
            assetMapping_Atlases.put(elements[elementCounter + 1], elements[elementCounter]);
            
            // Increment element counter.
            elementCounter += 2;
            }
        
    }
    
    // atlasMapList = Collection of items (key, value pairs) to add to hash map for atlases in asset manager.
    public void mapAtlases(ArrayList<String> atlasMapList)
    {
        
        // The function adds cross reference (hash mapping) values to provide a simpler way of referencing
        // atlas items in the asset manager.
        
        String [] atlasMapPairs; // List of (relative) atlas paths.
        
        // Convert from ArrayList to standard array.
        atlasMapPairs = atlasMapList.toArray(new String[atlasMapList.size()]);
        
        // Add the cross reference (hash mapping) values.
        mapAtlases(atlasMapPairs);
        
    }
    
    // elements = Collection of items (key, value pairs) to add to hash map for images in asset manager.
    public void mapImages(String ... elements)
    {
        
        // The function adds cross reference (hash mapping) values to provide a simpler way of referencing
        // items in the asset manager.
        // The function populates the hash map specifically used for images (Texture objects).
        // Pass values in the format / order:  asset name (when loading to manager), key value.
        
        // Example:  mapImages("assets/images/laser.png", "laser", "assets/images/rock.png", "rock");
        
        int elementCount; // Number of elements passed.  Divide by two to get number of pairs.
        int elementCounter; // Used to iterate through elements.
        int pairCount; // Number of hash pairs passed.
        
        // Set defaults.
        elementCounter = 0;
        
        // Count number of items passed.
        elementCount = elements.length;
        
        // Determine number of pairs.
        pairCount = elementCount / 2;
        
        // Loop through pairs.
        for (int pairCounter = 1; pairCounter <= pairCount; pairCounter++)
            {
            // Add current pair to hash map.
            assetMapping_Textures.put(elements[elementCounter + 1], elements[elementCounter]);
            
            // Increment element counter.
            elementCounter += 2;
            }
        
    }
    
    // imageMapList = Collection of items (key, value pairs) to add to hash map for images in asset manager.
    public void mapImages(ArrayList<String> imageMapList)
    {
        
        // The function adds cross reference (hash mapping) values to provide a simpler way of referencing
        // items in the asset manager.
        
        String [] imageMapPairs; // List of (relative) image paths.
        
        // Convert from ArrayList to standard array.
        imageMapPairs = imageMapList.toArray(new String[imageMapList.size()]);
        
        // Add the cross reference (hash mapping) values.
        mapImages(imageMapPairs);
        
    }
    
    // elements = List of atlases to load.  Must include full path and extension.  Example:  "assets\apples.atlas", "assets\oranges.atlas".
    public void queueAtlases(String ... elements)
    {
        
        // The function adds the passed atlases to the asset manager for future loading.
        // The atlases contain the parameters, such as how to handle scaling.
        
        // Loop through each element passed to function.
        for (String element : elements)
        {
            // Add current element in loop to queue.
            manager.load(element, TextureAtlas.class);
        }
        
    }
    
    // elements = List of atlases to load.  Must include full path and extension.  Example:  "assets\apples.atlas", "assets\oranges.atlas".
    public void queueAtlases(ArrayList<String> imagePathList)
    {
        
        // The function adds the passed atlases to the asset manager for future loading.
        // The atlases contain the parameters, such as how to handle scaling.
        
        String [] imagePath; // List of (relative) image paths.
        
        // Convert from ArrayList to standard array.
        imagePath = imagePathList.toArray(new String[imagePathList.size()]);
        
        // Add the passed atlases to the asset manager for future loading.
        queueAtlases(imagePath);
        
    }
    
    // elements = List of images to load.  Must include full path and extension.  Example:  "assets\apple.png", "assets\orange.png".
    public void queueImages(String ... elements)
    {
        
        // The function adds the passed images to the asset manager for future loading.
        
        /*
        Filter Type: Controlling how pixel colors are interpolated when image is rotated or stretched.

        > Nearest:  To represent each pixel on the screen, the method uses the pixel of the texture (texel) 
        that best matches to the pixel of the screen.  This is the default filter. As this filter only uses one 
        texel for every pixel on the screen, the method applies the filter very quickly.  The result is an image 
        with “hard” borders.

        > Linear:  To represent each pixel on the screen, the method uses bilinear interpolation, taking 
        the four closest texels that best match with the screen pixel.  The result is smooth scaling.
        But, processing costs will also be bigger than GL_NEAREST.
        
        MipMaps:
        When opting to use mipmaps, the Texture will create them at instantiation time.  MipMaps are 
        pre-calculated, optimized resized copies of the same image to save on computation time when 
        resizing the texture to fit a rectangle.  Smaller (halved) copies of the same image are 
        created and uploaded to the GPU and used for different sized geometries instead of being shrunk 
        on the fly. It adds to memory consumption but makes rendering faster.
        */
        
        TextureParameter param; // Object containing parameters to use when loading images.
        
        // Initialize parameter object.
        param = new TextureParameter();
        
        // Configure to resize using bilinear interpolation (slower, but more precise / better looking).
        param.magFilter = TextureFilter.Linear;
        param.minFilter = TextureFilter.Linear;
        
        // Configure to use MipMaps.
        param.genMipMaps = true;
        
        // Loop through each element passed to function.
        for (String element : elements)
        {
            // Add current element in loop to queue.
            manager.load(element, Texture.class, param);
        }
        
    }
    
    // elements = List of images to load.  Must include full path and extension.  Example:  "assets\apple.png", "assets\orange.png".
    public void queueImages(ArrayList<String> imagePathList)
    {
        
        // The function adds the passed images to the asset manager for future loading.
        
        String [] imagePath; // List of (relative) image paths.
        
        // Convert from ArrayList to standard array.
        imagePath = imagePathList.toArray(new String[imagePathList.size()]);
        
        // Add the passed images to the asset manager for future loading.
        queueImages(imagePath);
        
    }
    
    // elements = List of music to load.  Must include full path and extension.  Example:  "assets\symphony1.wav", "assets\symphony2.wav".
    public void queueMusic(String ... elements)
    {
        
        // The function adds the passed music to the asset manager for future loading.
        
        // Loop through each element passed to function.
        for (String element : elements)
        {
            // Add current element in loop to queue.
            manager.load(element, Music.class);
        }
        
    }
    
    // elements = List of sounds to load.  Must include full path and extension.  Example:  "assets\beep.wav", "assets\click.wav".
    public void queueSounds(String ... elements)
    {
        
        // The function adds the passed sounds to the asset manager for future loading.
        
        // Loop through each element passed to function.
        for (String element : elements)
        {
            // Add current element in loop to queue.
            manager.load(element, Sound.class);
        }
        
    }
    
    // Getters and setters below...
    
    // regionKey = Key (as relates to hash map) for Rectangle2D to return.
    public Rectangle2D.Float getTextureRegionRect(String regionKey)
    {
        
        // The function returns the rect structure related to the texture region identified by the passed key.
        
        // Return the rect structure.
        return textureRegionRects.get(regionKey);
        
    }
    
    // musicEnum = Enumerated value for the desired music.
    public Music getMusicMp3(HeroineEnum.MusicEnum musicEnum)
    {
        
        // The function returns the requested music in mp3 format.
        return manager.get(musicEnum.getValue_File_mp3(), Music.class);
        
    }
    
    // musicEnum = Enumerated value for the desired music.
    public Music getMusicOgg(HeroineEnum.MusicEnum musicEnum)
    {
        
        // The function returns the requested music in Ogg format.
        return manager.get(musicEnum.getValue_File_ogg(), Music.class);
        
    }
    
    // soundEnum = Enumerated value for the desired sound.
    public Sound getSound(HeroineEnum.SoundEnum soundEnum)
    {
        
        // The function returns the requested sound.
        return manager.get(soundEnum.getValue_FilePath(), Sound.class);
        
    }
    
    // regionKey = Key (as relates to hash map) for texture region to return.
    public TextureRegion getTextureRegion(String regionKey)
    {
        
        // The function returns the texture region in the hash map with the passed key.
        
        // Return the texture region in the hash map with the passed key.
        return textureRegions.get(regionKey);
        
    }
    
    public Map<String, Rectangle2D.Float> getTextureRegionRects() {
        return textureRegionRects;
    }

    public Map<String, TextureRegion> getTextureRegions() {
        return textureRegions;
    }
    
}