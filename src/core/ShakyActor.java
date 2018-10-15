package core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

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

public class ShakyActor extends BaseActor { // Extends the BaseActor class.
    
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
    */
    
    // Declare object variables.
    
    
    // Declare regular variables.
    private float basePosX; // Base x-position of actor.  Actor moves to here at beginning and goes back here 
      // after "shaking".  Left edge of actor.
    private float basePosY; // Base y-position of actor.  Actor moves to here at beginning and goes back here 
      // after "shaking".  Bottom of actor.
    private float elapsedTime; // Total elapsed time the actor has existed.
    private boolean slideToBasePosInd; // Whether to slide to base position.
      // Assumes offscreen, to the left, and already at y-coordinate.
    private Vector2 velocity; // Actor velocity (speed) in x and y directions.
    private int viewHeight; // Height of the stage.
    private int viewWidth; // Width of the stage.
    
    // Declare constants.
    private final float SLIDE_VELOCITY = 2000f; // Velocity (speed) at which to slide actor to base position.
    
    // Constructors below...
    
    // viewWidth = Window width of stage.
    // viewHeight = Window height of stage.
    public ShakyActor(int viewWidth, int viewHeight)
    {
        
        // The method calls the constructor of the parent (BaseActor) to create an actor.
        // The constructor also initializes the elapsed time.
        
        // Initialized elapsed time to 0.
        elapsedTime = 0;
        
        // Initialize the velocity vector.
        velocity = new Vector2();
        
        // Set the x and y portions of the velocity vector to the constant values used for sliding
        // the actor to its base position.
        velocity.set(SLIDE_VELOCITY, 0f);
        
        // Store view width and height.
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        
    }
    
    // actorName = Name of actor.
    // textureRegion = Texture region to assign to actor.
    // viewWidth = Window width of stage.
    // viewHeight = Window height of stage.
    // slideToBasePosInd = Whether to slide to base position.
    public ShakyActor(String actorName, TextureRegion textureRegion, int viewWidth, int viewHeight, 
      boolean slideToBasePosInd)
    {
        
        // The method calls the constructor of the parent (BaseActor) to create an actor with the 
        // passed name and texture region.
        // The constructor sets the starting location to be centered vertically and off the screen, 
        // to the left, by the width of the actor.
        // The constructor sets the base position such that the actor centers within the stage.
        // The constructor also initializes the elapsed time.
        
        // Call the constructor for the BaseActor (parent / super) class.
        super( actorName, textureRegion, -textureRegion.getRegionWidth(), 
          (viewHeight - textureRegion.getRegionHeight()) / 2 );
        
        // Set base position of actor.
        this.basePosX = ( viewWidth - textureRegion.getRegionWidth() ) / 2;
        this.basePosY = ( viewHeight - textureRegion.getRegionHeight() ) / 2;
        
        // Initialized elapsed time to 0.
        elapsedTime = 0;
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = slideToBasePosInd;
        
        // Initialize the velocity vector.
        velocity = new Vector2();
        
        // Set the x and y portions of the velocity vector to the constant values used for sliding
        // the actor to its base position.
        velocity.set(SLIDE_VELOCITY, 0f);
        
        // Store view width and height.
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        
    }
    
    // actorName = Name of actor.
    // texture = Texture to assign to actor.
    // viewWidth = Window width of stage.
    // viewHeight = Window height of stage.
    // slideToBasePosInd = Whether to slide to base position.
    public ShakyActor(String actorName, Texture texture, int viewWidth, int viewHeight, 
      boolean slideToBasePosInd)
    {
        
        // The method calls the constructor of the parent (BaseActor) to create an actor with the 
        // passed name and texture region.
        // The constructor sets the starting location to be centered vertically and off the screen, 
        // to the left, by the width of the actor.
        // The constructor sets the base position such that the actor centers within the stage.
        // The constructor also initializes the elapsed time.
        
        // Call the constructor for the BaseActor (parent / super) class.
        super( actorName, texture, -texture.getWidth(), 
          (viewHeight - texture.getHeight()) / 2 );
        
        // Set base position of actor.
        this.basePosX = ( viewWidth - texture.getWidth() ) / 2;
        this.basePosY = ( viewHeight - texture.getHeight() ) / 2;
        
        // Initialized elapsed time to 0.
        elapsedTime = 0;
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = slideToBasePosInd;
        
        // Initialize the velocity vector.
        velocity = new Vector2();
        
        // Set the x and y portions of the velocity vector to the constant values used for sliding
        // the actor to its base position.
        velocity.set(SLIDE_VELOCITY, 0f);
        
        // Store view width and height.
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        
    }
    
    // actorName = Name of actor.
    // textureRegion = Texture region to assign to actor.
    // basePosX:  Base x-position of actor.  Actor moves to here at beginning and goes back here after "shaking".
    // basePosY:  Base y-position of actor.  Actor moves to here at beginning and goes back here after "shaking".
    public ShakyActor(String actorName, TextureRegion textureRegion, float basePosX, float basePosY)
    {
        
        // The method calls the constructor of the parent (BaseActor) to create an actor with the 
        // passed name and texture region.
        // The constructor sets the starting and base position of the actor to the passed location.
        // The constructor also initializes the elapsed time.
        
        // Call the constructor for the BaseActor (parent / super) class.
        super( actorName, textureRegion, basePosX, basePosY );
        
        // Initialized elapsed time to 0.
        elapsedTime = 0;
        
        // Set base position of actor.
        this.basePosX = basePosX;
        this.basePosY = basePosY;
        
        // Initialize the velocity vector.
        velocity = new Vector2();
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = false;
        
    }
    
    // actorName = Name of actor.
    // texture = Texture to assign to actor.
    // basePosX:  Base x-position of actor.  Actor moves to here at beginning and goes back here after "shaking".
    // basePosY:  Base y-position of actor.  Actor moves to here at beginning and goes back here after "shaking".
    public ShakyActor(String actorName, Texture texture, float basePosX, float basePosY)
    {
        
        // The method calls the constructor of the parent (BaseActor) to create an actor with the 
        // passed name and texture.
        // The constructor sets the starting and base position of the actor to the passed location.
        // The constructor also initializes the alapsed time.
        
        // Call the constructor for the BaseActor (parent / super) class.
        super( actorName, texture, basePosX, basePosY );
        
        // Initialized elapsed time to 0.
        elapsedTime = 0;
        
        // Set base position of actor.
        this.basePosX = basePosX;
        this.basePosY = basePosY;
        
        // Initialize the velocity vector.
        velocity = new Vector2();
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = false;
        
    }
    
    // Methods below...
    
    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {

        // The function:

        // 1.  Performs a time based positional update.
        // 2.  Updates the elapsed time value.

        // Call the act method of the Actor, which performs a time based positional update.
        super.act(dt);

        // Add seconds since last frame to elapsed time.
        elapsedTime += dt;

        // If sliding to base position, then...
        if (slideToBasePosInd)
        {
            
            // Sliding to base position.
            
            // Moves current Actor (adds X and Y to current position) based on constant velocity (speed)
            // and elapsed seconds since last frame.
            moveBy( velocity.x * dt, velocity.y * dt );
            
            // If Actor at or past base position, then...
            if ( getX() >= basePosX )
            {
                
                // Actor at or past base position.
                
                // Move actor to base position.
                setX( basePosX );
                
                // Flag to stop sliding.
                slideToBasePosInd = false;
                
            } // End ... If Actor at or past base position.
            
        } // End ... If sliding to base position.

    }
    
    // actorName = Name of actor.
    // textureRegion = Texture region to assign to actor.
    public void updateShakyActor(String actorName, TextureRegion textureRegion)
    {
        
        // The function updates the actor, including the name, texture region, slide to base position 
        // indicator, and location.  The function sets the location of the actor to the base position.
        
        // Set actor name.
        super.actorName = actorName;
        
        // Assign texture region to the actor.
        super.setTextureRegion(textureRegion);
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = false;
        
        // Set the location of the actor to its base position.
        setPosition( this.basePosX, this.basePosY );
        
        // Initialized elapsed time to 0.
        elapsedTime = 0;
        
    }
    
    // actorName = Name of actor.
    // texture = Texture to assign to actor.
    public void updateShakyActor(String actorName, Texture texture)
    {
        
        // The function updates the actor, including the name, texture, slide to base position indicator, 
        // and location.  The function sets the location of the actor to the base position.
        
        // Set actor name.
        super.actorName = actorName;
        
        // Assign texture to the actor.
        super.setTexture(texture);
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = false;
        
        // Set the location of the actor to its base position.
        setPosition( this.basePosX, this.basePosY );
        
        // Initialized elapsed time to 0.
        elapsedTime = 0;
        
    }
    
    // actorName = Name of actor.
    // textureRegion = Texture region to assign to actor.
    // slideToBasePosInd = Whether to slide to base position.
    public void updateShakyActor_Center(String actorName, TextureRegion textureRegion, 
      boolean slideToBasePosInd)
    {
        
        // The function updates the actor, including the name, texture region, slide to base position
        // indicator, base position, and location.  The function uses the center of the stage as the
        // base position.  The function sets the location of the actor to the vertical center and off 
        // the screen, to the left, by its width.
        
        // Set actor name.
        super.actorName = actorName;
        
        // Assign texture region to the actor.
        super.setTextureRegion(textureRegion);
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = slideToBasePosInd;
        
        // Set base position of actor.
        this.basePosX = ( viewWidth - textureRegion.getRegionWidth() ) / 2;
        this.basePosY = ( viewHeight - textureRegion.getRegionHeight() ) / 2;
        
        // Position the actor to be centered vertically and off the screen, to the left, by its width.
        setPosition( -textureRegion.getRegionWidth(), this.basePosY );
        
        // Initialized elapsed time to 0.
        elapsedTime = 0;
        
    }
    
    // actorName = Name of actor.
    // texture = Texture to assign to actor.
    // slideToBasePosInd = Whether to slide to base position.
    public void updateShakyActor_Center(String actorName, Texture texture, boolean slideToBasePosInd)
    {
        
        // The function updates the actor, including the name, texture, slide to base position indicator,
        // base position, and location.  The function uses the center of the stage as the base position.
        // The function sets the location of the actor to the vertical center and off the screen, to the
        // left, by its width.
        
        // Set actor name.
        super.actorName = actorName;
        
        // Assign texture to the actor.
        super.setTexture(texture);
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = slideToBasePosInd;
        
        // Set base position of actor.
        this.basePosX = ( viewWidth - texture.getWidth() ) / 2;
        this.basePosY = ( viewHeight - texture.getHeight() ) / 2;
        
        // Position the actor to be centered vertically and off the screen, to the left, by its width.
        setPosition( -texture.getWidth(), this.basePosY );
        
        // Initialized elapsed time to 0.
        elapsedTime = 0;
        
    }
    
    // Getters and setters below...
    
    public float getBasePosX() {
        return basePosX;
    }

    public void setBasePosX(float basePosX) {
        this.basePosX = basePosX;
    }

    public float getBasePosY() {
        return basePosY;
    }

    public void setBasePosY(float basePosY) {
        this.basePosY = basePosY;
    }
    
    public void setSlideToBasePosInd(boolean slideToBasePosInd) {
        this.slideToBasePosInd = slideToBasePosInd;
    }
    
    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }
    
}
