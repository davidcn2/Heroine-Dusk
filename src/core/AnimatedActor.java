package core;

// LibGDX imports.
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

// Java imports.
import java.util.HashMap;

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

@SuppressWarnings("unused")
public class AnimatedActor extends BaseActor { // Extends the BaseActor class.

    /*
    The main purpose of the AnimatedActor class involves managing a set of animations,
    and selecting the correct image from the active animation.  Active refers here
    to the animation currently being rendered.

    An Animation stores a list of TextureRegions representing an animated sequence,
    e.g. for running or jumping.  Each region of an Animation is called a key frame,
    multiple key frames make up the animation.

    A HashMap data structure will get used to store animations associated with actors.
    String objects will get used as keys, and Animation objects will be the associated
    values.  For example, in a top-view adventure game, the main character might have
    four animations named north, south, east, and west.

    Methods include:

    act:  Performs a time based positional update and (if NOT paused) updates the elapsed time value.
    copy:  Copies properties from the passed to the current AnimatedActor.
    cloneActor:  Returns an AnimatedActor with the same properties as the current.
    draw:  Updates and draws the image for the active animation using a key frame based on the
           elapsed time.
    getAnimationName:  Returns the key for the active Animation object in the hash map.
    pauseAnimation:  Pauses the animation.
    removeAfterSinglePass:  Sets up an action to remove the animation from the screen after a single display.
      Computes duration.
    removeAfterSinglePassAuto:  Sets up an action to remove the animation from the screen after a single
      display.  Uses pre-computed duration.
    setActiveAnimation:  Sets the active Animation (key and object) using the passed key.
    setAnimationFrame:  Sets the specified frame of the animation to display.
    setFrameCount:  Stores the number of frames in the animation.
    setFrameDuration:  Stores the duration between frames in the animation (in seconds).
    setFrameTiming:  Stores the frame timing information (count, duration, time for a full pass).
    startAnimation:  Starts the animation (from a paused state).
    storeAnimation:  Adds an Animation object to the hash map using the specified key.
    */

    // Declare object variables.
    private Animation activeAnim; // Current (active) Animation object.
    private String activeName; // Name / key value for current (active) Animation object.
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<String,Animation> animationStorage; // HashMap data structure storing Animation
    // objects and associated keys.
    
    // Declare regular variables.
    private boolean pauseAnim; // Whether animation currently paused.
    private float elapsedTime; // Total elapsed time the animation has been playing.
    private int frameCount; // Number of frames in animation.
    private float frameDuration; // Duration (in seconds) between each animation frame.
    private float framePassRate; // Duration to run all frames in animation.
    
    public AnimatedActor()
    {

        // The constructor of the class calls the constructor of the parent (BaseActor)
        // and initializes the elapsed time, current (active) Animation object and related key,
        // and the Animation object hash map.

        super(); // Call the constructor for the BaseActor (parent / super) class.
        elapsedTime = 0; // Initialized elapsed time to 0.
        activeAnim = null; // Initialize current (active) Animation object.
        activeName = null; // Initialize key representing the current (active) Animation to no selection.
        // animationStorage = new HashMap<String,Animation>(); // Create hash map to contain Animation objects.
        animationStorage = new HashMap<>(); // Create hash map to contain Animation objects.
        pauseAnim = false; // Default animation to NOT paused.

    }

    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {

        // The function:

        // 1.  Performs a time based positional update.
        // 2.  Updates the elapsed time value -- if animation NOT paused.

        // The pause state determine whether the elapsed time increases
        // (which results in subsequent frames being displayed by the draw method).
        
        // Call the act method of the Actor, which performs a time based positional update.
        super.act( dt );

        // If animation NOT paused, then...
        if (!pauseAnim)
            // Animation NOT paused.
            // Add seconds since last frame to elapsed time.
            elapsedTime += dt;

    }
    
    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntCallSuperClone"})
    @Override
    public AnimatedActor clone()
    {

        // The function returns an AnimatedActor with the same properties as the current.

        AnimatedActor newbie; // AnimatedActor to which to copy properties.

        // Instantiate new AnimatedActor object.
        newbie = new AnimatedActor();

        // Copy properties of current (class-level) to new AnimatedActor object.
        newbie.copy( this );

        // Return the new AnimatedActor object.
        return newbie;

    }
    
    // original = AnimatedActor to copy.
    public void copy(AnimatedActor original)
    {

        // The function copies properties from the passed to the current Animated Actor, except for setting elapsed time to 0.

        // Properties include:
        // 1.  Information about the base Actor.
        // 2.  HashMap data structure storing Animation objects and keys.
        // 3.  Name / key value for current (active) Animation object.
        // 4.  Current (active) Animation object.

        // Copy properties related to the associated BaseActor.
        super.copy(original);

        // Set elapsed time to 0.
        this.elapsedTime = 0;

        // Copy reference for HashMap data structure storing Animation objects and keys.
        this.animationStorage = original.animationStorage; // sharing a reference

        // Copy name / key value for current (active) Animation object.
        this.activeName = original.activeName;

        // Copy frame count.
        this.frameCount = original.frameCount;
        
        // Copy current (active) animation object.
        this.activeAnim = this.animationStorage.get( this.activeName );

    }
    
    @Override
    public void draw(Batch batch, float parentAlpha)
    {

        // The function updates and draws the image using an animation key frame based on the elapsed time.

        // Use the getKeyFrame method of the Animation class to retrieve the correct image based
        // on the current elapsed time.
        // Set the texture region and coordinates to the the specified texture (the animation frame).
        // region.setRegion( activeAnim.getKeyFrame(elapsedTime) );
        // Texture t = ((TextureRegion)a.getKeyFrame(0)).getTexture();
        region.setRegion( (TextureRegion)activeAnim.getKeyFrame(elapsedTime) );
        
        // Set the tinting color of and draw the Actor.
        super.draw(batch, parentAlpha);

    }
    
    public String getAnimationName()
    {
        // The function returns the key for the active Animation object in the hash map.
        return activeName;
    }
    
    public void pauseAnimation()
    {
        // The function pauses the animation.
        pauseAnim = true;
    }
    
    // anim = Actor to which to add removal action.
    public void removeAfterSinglePass(AnimatedActor anim)
    {
        
        // The function sets up an action to remove the animation from the screen after a single display.
        // Computes duration.
        // Time-based:  number of frames x duration of each frame.
        
        float removalTime; // Number of seconds to allow to pass before removing actor.
        
        // Determine removal time.
        removalTime = (float)frameCount * .01f;
        
        // Set up action to remove the animation from the screen after a single display, based on time passed.
        // Allow 0.01 seconds for each frame.
        anim.addAction( Actions.sequence( Actions.delay(removalTime), Actions.removeActor() ) );
        
    }
    
    // anim = Actor to which to add removal action.
    public void removeAfterSinglePassAuto(AnimatedActor anim)
    {
        
        // The function sets up an action to remove the animation from the screen after a single display.
        // Uses pre-computed duration.
        // Time-based:  number of frames x duration of each frame.
        
        // Set up action to remove the animation from the screen after a single display, based on time passed.
        // Allow 0.01 seconds for each frame.
        anim.addAction( Actions.sequence( Actions.delay(framePassRate), Actions.removeActor() ) );
        
    }
    
    // name = Key for the Animation object to set as active in the hash map.
    public void setActiveAnimation(String name)
    {

        // The method sets the active Animation (key and object) using the passed key.
        // The method also resets the elapsed time and updates the width and height of
        // the related Actor to that of the Animation.

        Texture tex; // Texture containing first animation frame of active Animation -- after setting
        // using key.

        // If hash map contains passed key, then...
        if ( animationStorage.containsKey(name) )

        {
            // Hash map contains passed key.

            // If animation already playing, then...
            if ( name.equals(activeName) )
                // Animation already playing.
                // Exit function.
                return;
            
            // Set the active Animation key using the passed value.
            activeName = name;

            // Set the active Animation Object using the passed key.
            activeAnim = animationStorage.get(name);

            // Reset elapsed time related to animation.
            elapsedTime = 0;

            // If width and height NOT yet set, then...
            if ( getWidth() == 0 || getHeight() == 0 )
                
                {
                // Width and height NOT yet set.
                
                // Create a Texture with the first animation frame.
                // tex = activeAnim.getKeyFrame(0).getTexture();
                tex = ((TextureRegion)activeAnim.getKeyFrame(0)).getTexture();

                // Set width and height of related Actor to that of Texture.
                setWidth( tex.getWidth() );
                setHeight( tex.getHeight() );
                }
            
        }

        else

        {
            // Hash map does NOT contain passed key.

            // Display that no animation exists in the hash map with the passed name.
            System.out.println("No animation: " + name);
        }

    }
    
    // n = Animation frame to display.
    public void setAnimationFrame(int n)
    {
        // The function sets the specified frame of the animation to display.
        elapsedTime = n * activeAnim.getFrameDuration();
    }
    
    // actorFrameCount = Number of frames in animation.
    public void setFrameCount(int actorFrameCount)
    {
        // The function sets the number of frames in the animation.
        this.frameCount = actorFrameCount;
    }
    
    // actorFrameDuration = Number of seconds between frames in animation.
    public void setFrameDuration(float actorFrameDuration)
    {
        // The function sets the number of seconds between frames in the animation.
        this.frameDuration = actorFrameDuration;
    }
    
    // actorFrameCount = Number of frames in animation.
    // actorFrameDuration = Number of seconds between frames in animation.
    public void setFrameTiming(int actorFrameCount, float actorFrameDuration)
    {
        
        // The function sets the number of frames in the animation and time between frames.
        this.frameCount = actorFrameCount;
        this.frameDuration = actorFrameDuration;
        this.framePassRate = (float) actorFrameCount * actorFrameDuration;
        
    }
    
    public void startAnimation()
    {
        // The function starts the animation (from a paused state).
        pauseAnim = false;
    }
    
    // name = Name to assign to the Animation object.
    // anim = Animation object to add to the hash map.
    public void storeAnimation(String name, Animation anim)
    {

        // The method stores the passed Animation object in the hash map.
        // The method uses the passed key when adding the object to the hash map.
        // If no active animation exists, the method sets the passed one as active.

        // Add the passed Animation object to the hash map using the provided key.
        animationStorage.put(name, anim);

        // If no Animation object key set as current, then...
        if (activeName == null)

            // No animation object key set as current.
            // Set the passed Animation object as current.
            setActiveAnimation(name);

    }

    // name = Key value to associate with Animation object.
    // tex = Texture (stores a single image) to use as starting point to create Animation object.
    public void storeAnimation(@SuppressWarnings("SameParameterValue") String name, Texture tex)
    {

        // The method builds an Animation object using the passed Texture and adds the results to
        // the hash map.
        // The method uses the passed key when adding the object to the hash map.
        // If no active animation exists, the method sets the passed one as active.

        Animation anim; // Animation object created using passed Texture.
        TextureRegion reg; // Stores image (similar to a buffer from Direct-X).  Includes more
        // functionality than a Texture.  Supports storage of multiple images or animation frames.
        // The TextureRegion here will get built using the passed Texture object.

        // Create a TextureRegion object using the passed Texture.
        reg = new TextureRegion(tex);

        // Create an array of TextureRegion objects out of the TextureRegion, to support conversion to an
        // Animation.
        TextureRegion[] frames = { reg };

        // Create an Animation out of the array of TextureRegion objects, using one second as the delay
        // between frames.
        //anim = new Animation<>(1.0f, frames); // Method for IntelliJ.
        anim = new Animation(1.0f, frames); // Method for NetBeans.
        
        // Stored Animation object in hash map using passed key value.
        storeAnimation(name, anim);

        // If no Animation object key set as current, then...
        if (activeName == null)

            // No animation object key set as current.
            // Set the passed Animation object as current.
            setActiveAnimation(name);

    }

}