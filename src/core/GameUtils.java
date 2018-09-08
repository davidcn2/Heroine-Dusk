package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

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

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.

ArrayList supports dynamic arrays that can grow as needed.
*/

public class GameUtils
{

    /*
    The GameUtils class servers the purpose of providing extra reusable functionality.

    Texture:  Contains entire image loaded into GPU.
    TextureRegion:  Contains portion (or all) of image.  Drawing usually occurs using TextureRegion objects.
    
    // Methods include:

    frameBuilder:  Contains logic for processing a sprite sheet with images in x by y columns and rows and
                   returning the results in a single-dimensional array of TextureRegion objects.
    parseImageFiles:  Creates an animation from a set of files.
    parseSpriteSheet:  Contains logic for processing a sprite sheet with images in x by y columns and rows and
                       turning the results into an animation, which gets returned.
    */

    // filename = Name of file containing sprite sheet, with directory and suffix.  Example:  images/explosion.png
    // frameCols = Number of columns in sprite sheet.
    // frameRows = Number of rows in sprite sheet.
    private static Array<TextureRegion> frameBuilder(String fileName, int frameCols, int frameRows)
    {
        
        /*
        The function contains logic for processing a sprite sheet with images in x by y columns and rows and
        turning the results into a single-dimensional array, which gets returned.

        The TextureRegion class has a method called split that divides an image into rectangular sections,
        and returns the results in a two-dimensional array of TextureRegion objects.  The two-dimensional can
        be converted to a single Array and used in creating an Animation.  The function contains a nested for
        loop that transfers the contents of the two-dimensional array into a single-dimensional array.  The
        calling function subsequently creates the animation.
        
        The function returns a single-dimensional array (full list variety) of TextureRegion objects 
        containing sprites from the sheet.
        
        Texture:  Contains entire image loaded into GPU.
        TextureRegion:  Contains portion (or all) of image.  Drawing usually occurs using TextureRegion objects.
        */
        
        // Declare regular variables.
        int frameWidth; // Width of each sprite in the sheet -- assumes equal size.
        int frameHeight; // Height of each sprite in the sheet -- assumes equal size.
        int index; // Used to loop through frames in animation / sprites in sheet.
        
        // Declare object variables.
        TextureRegion[] frames; // Single-dimensional array of TextureRegion objects containing sprites from sheet.
        Texture t; // Texture used for sprite sheet.
        TextureRegion[][] temp; // Two-dimensional array of TextureRegion objects containing sprites from sheet.
        
        // Load image for sprite sheet to buffer.
        t = new Texture(Gdx.files.internal(fileName), true);

        // Set filter type -- controlling how pixel colors are interpolated when image is rotated or stretched.
        t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Get and store width and height of each sprite in sheet.
        frameWidth = t.getWidth() / frameCols;
        frameHeight = t.getHeight() / frameRows;

        // Divide image into rectangular sections, based on calculated (and equal) width and height of each sprite in sheet.
        temp = TextureRegion.split(t, frameWidth, frameHeight);
        frames = new TextureRegion[frameCols * frameRows];

        // Set initial frame number.
        index = 0;

        // Loop through frames in animation / sprites in sheet -- contained within two-dimensional array.

        // Loop through rows in sprite sheet.
        for (int i = 0; i < frameRows; i++)
        {
            // Loop through columns in sprite sheet.
            for (int j = 0; j < frameCols; j++)
            {
                // Copy frame from two to one-dimensional array.
                frames[index] = temp[i][j];

                // Increment frame number.
                index++;
            }
        }

        // Build and return array of frames containing sprites in sheet.
        return new Array<>(frames);
        
    }
    
    // fileDirectory = Name of directory containing image files.  Excludes trailing forward slash.  Example:  images.
    // fileNamePrefix = Filename before sprite index.  Examples:  planeGreen, planeRed, ...
    // fileNameSuffix = Filename suffix or type.  Excludes leading period.  Examples:  png, jpg, ...
    // frameCount = Number of files involved in animation.
    // frameDuration = Duration of each frame of the animation, in seconds.  Example:  0.03f.
    // mode = PlayMode of the animation.  Examples:  LOOP, LOOP_PINGPONG, LOOP_RANDOM, LOOP_REVERSED, NORMAL, REVERSED.
    public static Animation parseImageFiles(String fileDirectory, String fileNamePrefix, String fileNameSuffix,
      int frameCount, float frameDuration, PlayMode mode)
    {
        
        /*
        The function creates an animation from a set of files.
        
        The function contains logic for processing multiple image files using 
        a passed prefix and turning the results into an animation, which gets returned.
        
        Texture:  Contains entire image loaded into GPU.
        TextureRegion:  Contains portion (or all) of image.  Drawing usually occurs using TextureRegion objects.
        
        Name Format:  fileNamePrefix + N + fileNameSuffix, where 0 <= N < frameCount.
        
        Example:  planeGreen0.png, planeGreen1.png, planeGreen2.png, ...
        */
        
        // Declare regular variables.
        String fileName; // Name of current file in loop.
        String fullFilePrefix; // Full file prefix, including directory, /, and prefix.  Example:  images/planeGreen.
        String fullFileSuffix; // Full file suffix, including period (.) and suffix.  Examples:  .jpg, .png, ...
        
        // Declare objects.
        TextureRegion[] frames; // Single-dimensional array of TextureRegion objects containing sprites from files.
        Array<TextureRegion> framesArray; // Single-dimensional LibGDX Array of TextureRegion objects with images. 
        Texture tex; // Texture used for current image in loop.
        
        // Initialize frames object as a single-dimensional array with room for the passed number of frames.
        frames = new TextureRegion[frameCount];
        
        // Determine full file prefix and suffix.
        fullFilePrefix = fileDirectory + '/' + fileNamePrefix;
        fullFileSuffix = '.' + fileNameSuffix;
        
        // Loop through and add frames to array.
        for (int n = 0; n < frameCount; n++)
        {   
            
            // Determine current file in loop, based on counter and passed directory, prefix, and suffix.
            fileName = fullFilePrefix + Integer.toString(n) + fullFileSuffix;
            
            // Load current file image into buffer.
            tex = new Texture(Gdx.files.internal(fileName));
            
            // Set filter type -- controlling how pixel colors are interpolated when image is rotated or stretched.
            tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            
            // Store the texture region object in the current animation frame.
            frames[n] = new TextureRegion( tex );
            
        }
        
        // Populate LibGDX array with image frames to support animation creation.
        // framesArray = new Array<TextureRegion>(frames);
        framesArray = new Array<>(frames);
        
        // Return the Animation object.
        // return new Animation<>(frameDuration, framesArray, mode); // IntelliJ method.
        return new Animation(frameDuration, framesArray, mode); // NetBeans method.
        
    }
    
    // fileNamePrefix = Filename before sprite index, including full path.  Examples:  C:\Assets\planeGreen, C:\Assets\planeRed, ...
    // fileNameSuffix = Filename suffix or type.  Includes leading period.  Examples:  .png, .jpg, ...
    // frameCount = Number of files involved in animation.
    // frameDuration = Duration of each frame of the animation, in seconds.  Example:  0.03f.
    // mode = PlayMode of the animation.  Examples:  LOOP, LOOP_PINGPONG, LOOP_RANDOM, LOOP_REVERSED, NORMAL, REVERSED.
    public static Animation parseImageFiles(String fileNamePrefix, String fileNameSuffix,
      int frameCount, float frameDuration, PlayMode mode)
    {
        
        /*
        The function creates an animation from a set of files.
        
        The function contains logic for processing multiple image files using 
        a passed prefix and turning the results into an animation, which gets returned.
        
        Texture:  Contains entire image loaded into GPU.
        TextureRegion:  Contains portion (or all) of image.  Drawing usually occurs using TextureRegion objects.
        
        Name Format:  fileNamePrefix + N + fileNameSuffix, where 0 <= N < frameCount.
        
        Example:  planeGreen0.png, planeGreen1.png, planeGreen2.png, ...
        */
        
        // Declare regular variables.
        String fileName; // Name of current file in loop.
        
        // Declare objects.
        TextureRegion[] frames; // Single-dimensional array of TextureRegion objects containing sprites from files.
        Array<TextureRegion> framesArray; // Single-dimensional LibGDX Array of TextureRegion objects with images. 
        Texture tex; // Texture used for current image in loop.
        
        // Initialize frames object as a single-dimensional array with room for the passed number of frames.
        frames = new TextureRegion[frameCount];
        
        // Loop through and add frames to array.
        for (int n = 0; n < frameCount; n++)
        {
            
            // Determine current file in loop, based on counter and passed prefix and suffix.
            fileName = fileNamePrefix + n + fileNameSuffix;
            
            // Load current file image into buffer.
            tex = new Texture(Gdx.files.internal(fileName));
            
            // Set filter type -- controlling how pixel colors are interpolated when image is rotated or stretched.
            tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            
            // Store the texture region object in the current animation frame.
            frames[n] = new TextureRegion( tex );
            
        }
        
        // Populate LibGDX array with image frames to support animation creation.
        // framesArray = new Array<TextureRegion>(frames);
        framesArray = new Array<>(frames);
        
        // Return the Animation object.
        // return new Animation<>(frameDuration, framesArray, mode); // IntelliJ method.
        return new Animation(frameDuration, framesArray, mode); // NetBeans method.
        
    }
    
    // filename = Name of file containing sprite sheet, with directory and suffix.  Example:  images/explosion.png
    // frameCols = Number of columns in sprite sheet.
    // frameRows = Number of rows in sprite sheet.
    // frameDuration = Duration between each frame of the animation, in seconds.  Example:  0.03f.
    // mode = PlayMode of the animation.  Examples:  LOOP, LOOP_PINGPONG, LOOP_RANDOM, LOOP_REVERSED, NORMAL, REVERSED.
    @SuppressWarnings("SameParameterValue")
    public static Animation parseSpriteSheet(String fileName, int frameCols, int frameRows,
      float frameDuration, PlayMode mode)
    {

        /*
        The function builds an array of frames containing the sprites in the sheet.
        
        The function contains logic for processing a sprite sheet with images in x by y columns and rows and
        turning the results into an animation, which gets returned.

        The TextureRegion class has a method called split that divides an image into rectangular sections,
        and returns the results in a two-dimensional array of TextureRegion objects.  The two-dimensional can
        be converted to a single Array and used in creating an Animation.  The function contains a nested for
        loop that transfers the contents of the two-dimensional array into a single-dimensional array before
        creating the animation.
        
        Texture:  Contains entire image loaded into GPU.
        TextureRegion:  Contains portion (or all) of image.  Drawing usually occurs using TextureRegion objects.
        */

        Array<TextureRegion> framesArray; // Single-dimensional array (full list variety) of 
          // TextureRegion objects containing all sprites from sheet.

        // Build Array of frames containing sprites in sheet.
        // framesArray = new Array<>(frames);
        framesArray = frameBuilder(fileName, frameCols, frameRows);
        
        // Return the Animation object.
        // return new Animation<>(frameDuration, framesArray, mode); // IntelliJ method.
        return new Animation(frameDuration, framesArray, mode); // NetBeans method.

    }

    // filename = Name of file containing sprite sheet, with directory and suffix.  Example:  images/explosion.png
    // frameCols = Number of columns in sprite sheet.
    // frameRows = Number of rows in sprite sheet.
    // frameIndices = Indices of the frames in the sprite sheet to return in the animation.
    // frameDuration = Duration between each frame of the animation, in seconds.  Example:  0.03f.
    // mode = PlayMode of the animation.  Examples:  LOOP, LOOP_PINGPONG, LOOP_RANDOM, LOOP_REVERSED, NORMAL, REVERSED.
    public static Animation parseSpriteSheet(String fileName, int frameCols, int frameRows,
      int[] frameIndices, float frameDuration, PlayMode mode)
    {
        
        /*
        The function contains logic for processing a sprite sheet with images in x by y columns and rows and
        turning the results into an animation, which gets returned.  The function only builds the Animation
        using a subset of the frames, as specified in the passed array, frameIndices.

        The TextureRegion class has a method called split that divides an image into rectangular sections,
        and returns the results in a two-dimensional array of TextureRegion objects.  The two-dimensional can
        be converted to a single Array and used in creating an Animation.  The function contains a nested for
        loop that transfers the contents of the two-dimensional array into a single-dimensional array before
        creating the animation.
        
        The last step builds an Animation array containing only the frames specified in the passed array,
        frameIndices.  The single-dimensional array from the prior step acts as the source.
        
        Texture:  Contains entire image loaded into GPU.
        TextureRegion:  Contains portion (or all) of image.  Drawing usually occurs using TextureRegion objects.
        */
        
        Array<TextureRegion> frames; // Single-dimensional array (full list variety) of 
          // TextureRegion objects containing all sprites from sheet.
        Array<TextureRegion> framesArray; // Single-dimensional array containing only the frames
          // (TextureRegion objects) specified in the passed array.
        int index; // Index of TextureRegion object to copy from original array -- frames.
        
       
        // Build Array of frames containing sprites in sheet.
        // framesArray = new Array<>(frames);
        frames = frameBuilder(fileName, frameCols, frameRows);
        
        // Initialize array that will contain only frames specified in passed array.
        // framesArray = new Array<TextureRegion>();
        framesArray = new Array<>();
        
        // Loop through indices to keep.
        for (int n = 0; n < frameIndices.length; n++)
            {
            // Get index of next frame to keep.
            index = frameIndices[n];
            
            // Add frame (TextureRegion) to array.
            framesArray.add( frames.get(index) );
            }
        
        // Return the Animation object.
        // return new Animation<>(frameDuration, framesArray, mode); // IntelliJ method.
        return new Animation(frameDuration, framesArray, mode); // NetBeans method.
        
    }
    
}