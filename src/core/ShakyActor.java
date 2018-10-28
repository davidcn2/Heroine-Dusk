package core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.security.SecureRandom;
import routines.UtilityRoutines;

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
    The main purpose of the ShakyActor class involves extending base actor
    functionality to support "shaking" -- random movement to nearby locations and
    then back to the point of origin.  Supports pausing.

    Methods include:

    act:  Performs a time based positional update and (if NOT paused) updates the elapsed time value.
    distancePoints:  Returns the distance between the two passed points.
    distanceToShakeOrigin:  Returns the distance between the passed point and the original location of 
      the actor (before "shaking").
    getBasePosX:  Returns the base x-position of the actor.
    getBasePosY:  Returns the base y-position of the actor.
    getShakeInd:  Returns whether to "shake" the actor.
    getSlideToBasePosInd:  Returns whether to slide the actor to its base position.
    nextShake:  Performs calculations for the next "shake" of the actor.
    pauseMovement:  Pauses movement of the actor.
    performInitialization:  Performs basic shared preparation steps.
    resumeMovement:  Resumes movement of the actor.
    setBasePosX:  Sets the base x-position of the actor.
    setBasePosY:  Sets the base y-position of the actor.
    setShakeInd:  Sets whether to "shake" the actor.
    setSlideToBasePosInd:  Sets whether to slide the actor to its base position.
    setViewHeight:  Sets value storing the height of the stage.  Does not impact the stage.
    setViewWidth:  Sets value storing the width of the stage.  Does not impact the stage.
    startShake:  Sets values for and initiates a "shake" operation.
    updateShakyActor:  Used to update properties of the actor.  Eases reuse.
    updateShakyActor_Center:  Used to update properties of the actor.  Uses center of stage 
      as base position.  Eases reuse.
    */
    
    // Declare object variables.
    
    
    // Declare regular variables.
    private float basePosX; // Base x-position of actor.  Actor moves to here at beginning and goes back here 
      // after "shaking".  Left edge of actor.
    private float basePosY; // Base y-position of actor.  Actor moves to here at beginning and goes back here 
      // after "shaking".  Bottom of actor.
    private float elapsedTime; // Total elapsed time the actor has existed.
    private final SecureRandom number; // Used for generating random numbers.
    private boolean pauseMovementInd; // Whether movement currently paused.
    private int shakeCount; // Number of animations to process related to "shaking" actor.
    private int shakeCounter; // Used to iterate through animations related to "shaking" actor.
    private double shakeDirectionX; // X-component of direction vector related to "shake" action.
    private double shakeDirectionY; // Y-component of direction vector related to "shake" action.
    private double shakeDistanceCurr; // Current number of pixels to move actor while "shaking".
    private int shakeDistanceMax; // Maximum number of pixels to move actor while "shaking".
    private boolean shakeInd; // Whether to "shake" actor.
    private float shakeDestX; // Destination x-position in current "shake" action.
    private float shakeDestY; // Destination y-position in current "shake" action.
    private float shakeOrigPosX; // X-position at which to start "shaking".
    private float shakeOrigPosY; // Y-position at which to start "shaking".
    private float shakeSrcX; // Source x-position in current "shake" action.
    private float shakeSrcY; // Source y-position in current "shake" action.
    private boolean slideToBasePosInd; // Whether to slide to base position.
      // Assumes offscreen, to the left, and already at y-coordinate.
    private Vector2 velocity; // Actor velocity (speed) in x and y directions.
    private Vector2 velocityShake; // Actor velocity (speed) in x and y directions, specific for "shaking".
    private int viewHeight; // Height of the stage.
    private int viewWidth; // Width of the stage.
    
    // Declare constants.
    private final float SHAKE_VELOCITY = 500f; // Velocity (speed) at which to "shake" actor.
    private final float SLIDE_VELOCITY = 2000f; // Velocity (speed) at which to slide actor to base position.
    
    // Constructors below...
    
    // viewWidth = Window width of stage.
    // viewHeight = Window height of stage.
    public ShakyActor(int viewWidth, int viewHeight)
    {
        
        // The method calls the constructor of the parent (BaseActor) to create an actor.
        // The constructor also initializes the elapsed time.
        
        // Perform common initialization steps.
        performInitialization();
        
        // Set the x and y portions of the velocity vector to the constant values used for sliding
        // the actor to its base position.
        velocity.set(SLIDE_VELOCITY, 0f);
        
        // Store view width and height.
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        
        // Start random number generator.
        number = new SecureRandom();
        
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
        
        // Perform common initialization steps.
        performInitialization();
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = slideToBasePosInd;
        
        // Set the x and y portions of the velocity vector to the constant values used for sliding
        // the actor to its base position.
        velocity.set(SLIDE_VELOCITY, 0f);
        
        // Store view width and height.
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        
        // Start random number generator.
        number = new SecureRandom();
        
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
        
        // Perform common initialization steps.
        performInitialization();
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = slideToBasePosInd;
        
        // Set the x and y portions of the velocity vector to the constant values used for sliding
        // the actor to its base position.
        velocity.set(SLIDE_VELOCITY, 0f);
        
        // Store view width and height.
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        
        // Start random number generator.
        number = new SecureRandom();
        
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
        
        // Set base position of actor.
        this.basePosX = basePosX;
        this.basePosY = basePosY;
        
        // Perform common initialization steps.
        performInitialization();
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = false;
        
        // Start random number generator.
        number = new SecureRandom();
        
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
        
        // Set base position of actor.
        this.basePosX = basePosX;
        this.basePosY = basePosY;
        
        // Perform common initialization steps.
        performInitialization();
        
        // Store whether to slide to base position.
        this.slideToBasePosInd = false;
        
        // Start random number generator.
        number = new SecureRandom();
        
    }
    
    // Methods below...
    
    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {

        // The function:

        // 1.  Performs a time based positional update.
        // When movement NOT paused...
        // 2.  Updates the elapsed time value.
        // 3.  Conditionally slides the actor to base position.
        // 4.  Conditionally "shakes" the actor.
        
        // Call the act method of the Actor, which performs a time based positional update.
        super.act(dt);

        // If movement NOT paused, then...
        if (!pauseMovementInd)
        {
            
            // Movement NOT paused.
            
            // Add seconds since last frame to elapsed time.
            elapsedTime += dt;

            // If sliding to base position, then...
            if ( slideToBasePosInd )
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

            // Otherwise, if "shaking" actor, then...
            else if (shakeInd)
            {

                // "Shake" actor.

                // See https://gamedev.stackexchange.com/questions/23447/moving-from-ax-y-to-bx1-y1-with-constant-speed.

                // Moves current Actor (adds X and Y to current position) based on constant velocity (speed)
                // and elapsed seconds since last frame.
                moveBy( velocityShake.x * dt, velocityShake.y * dt );

                // If actor at or past destination, then...
                if ( distancePoints(this.shakeSrcX, this.shakeSrcY, super.getX(), 
                    super.getY()) >= this.shakeDistanceCurr )
                {

                    // Actor at or past destination.

                    // Move actor to destination.
                    this.setX(this.shakeDestX);
                    this.setY(this.shakeDestY);

                    // Increment "shake" counter.
                    shakeCounter++; 

                    // If more "shake" animations remain, then...
                    if (shakeCounter <= shakeCount)
                    {

                        // More "shake" animations remain.

                        // Perform calculations related to next "shake".
                        nextShake(null, null);

                    }

                    // Otherwise, if in first iteration after going through all "shake" animations, then...
                    else if (shakeCounter == shakeCount + 1)
                    {

                        // In first iteration after going through all "shake" animations.
                        // Go through one more movement to return actor to original location.

                        // Return actor to original location.
                        nextShake(this.shakeOrigPosX, this.shakeOrigPosY);

                    }

                    else
                    {

                        // Actor in original location.

                        // Reset "shake" indicator and counter.
                        shakeInd = false;
                        shakeCounter = 1;

                    }

                } // End ... If actor at or past destination.

            } // End ... if "shaking" actor.
            
        } // End ... If movement NOT paused.
        
    }
    
    // posX_Src = X-coordinate of source location.
    // posY_Src = Y-coordinate of source location.
    // posX_Dest = X-coordinate of destination location.
    // posY_Dest = Y-coordinate of destionation location.
    public static double distancePoints(float posX_Src, float posY_Src, float posX_Dest, float posY_Dest)
    {
        
        // The function returns the distance between the two passed points.
        
        // Return distance between passed point and original location of actor (before "shaking").
        return Math.sqrt(Math.pow(posX_Dest - posX_Src, 2) + Math.pow(posY_Dest - posY_Src, 2));
        
    }
    
    // posX = X-coordinate of point for which to get distance to original location of actor (before "shaking").
    // posY = Y-coordinate of point for which to get distance to original location of actor (before "shaking").
    private double distanceToShakeOrigin(float posX, float posY)
    {
        
        // The function returns the distance between the passed point and the original location of the actor
        // (before "shaking").
        
        // Return distance between passed point and original location of actor (before "shaking").
        return Math.sqrt(Math.pow(posX - this.shakeOrigPosX, 2) + Math.pow(posY - this.shakeOrigPosY, 2));
        
    }
    
    // shakeDestX = X-coordinate of static location to move actor.
    // shakeDestY = Y-coordinate of static location to move actor.
    private void nextShake(Float shakeDestX, Float shakeDestY)
    {
        
        // The function performs calculations for the next "shake" of the actor.
        // Use destination when passed.
        // Otherwise, sets actor on path to within x pixels of original location in each direction.
        // x = shakeDistanceMax.
        
        double shakeDirSum; // Sum of x and y components of direction vector (using absolute values).
        double shakeDirX_Abs; // Absolute value of x component of direction vector.
        double shakeDirY_Abs; // Absolute value of y component of direction vector.
        int velocityMultipleX; // Velocity multiple in the x-direction to account for direction (left or right).
        int velocityMultipleY; // Velocity multiple in the y-direction to account for direction (up or down).
        
        // Set current "shake" source to current actor location.
        this.shakeSrcX = super.getX();
        this.shakeSrcY = super.getY();
        
        // If static destination passed, then...
        if (shakeDestX != null)
        {
            // Static destination passed.
            
            // Set current "shake" destination.
            this.shakeDestX = shakeDestX;
            this.shakeDestY = shakeDestY;
        }
        
        else
        {
            // No static destination passed.
            
            // Set current "shake" destination.
            // Position located within shakeDistanceMax pixels to left, right, above, and below original location.
            this.shakeDestX = this.shakeOrigPosX + UtilityRoutines.generateStandardRnd(number, 1, 
              this.shakeDistanceMax) - UtilityRoutines.generateStandardRnd(number, 1, this.shakeDistanceMax);
            this.shakeDestY = this.shakeOrigPosY + UtilityRoutines.generateStandardRnd(number, 1, 
              this.shakeDistanceMax) - UtilityRoutines.generateStandardRnd(number, 1, this.shakeDistanceMax);
        }
        
        // Calculate current "shake" distance.
        this.shakeDistanceCurr = distancePoints(this.shakeSrcX, this.shakeSrcY, this.shakeDestX, 
          this.shakeDestY);
        
        // If current "shake" distance is zero, then...
        if (this.shakeDistanceCurr == 0)
        {
            
            // Current "shake" distance is zero.
            
            // Set components of direction vector to zero.
            this.shakeDirectionX = 0;
            this.shakeDirectionY = 0;
            
            // Set components of velocity vector to zero.
            this.velocityShake.x = 0;
            this.velocityShake.y = 0;
            
        }
        
        else
        {
            
            // Current "shake" distance other than zero.
            
            // Calculate components of direction vector.
            this.shakeDirectionX = (this.shakeDestX - this.shakeSrcX) / this.shakeDistanceCurr;
            this.shakeDirectionY = (this.shakeDestY - this.shakeSrcY) / this.shakeDistanceCurr;
            
            // Store absolute values of components of direction vector.
            shakeDirX_Abs = Math.abs(this.shakeDirectionX);
            shakeDirY_Abs = Math.abs(this.shakeDirectionY);

            // Store sum of absolute values.
            shakeDirSum = shakeDirX_Abs + shakeDirY_Abs;

            // Determine velocity multiples.
            velocityMultipleX = this.shakeDestX > this.shakeSrcX ? 1 : -1;
            velocityMultipleY = this.shakeDestY > this.shakeSrcY ? 1 : -1;

            // Calculate proportional velocity vector.
            this.velocityShake.x = (float)(SHAKE_VELOCITY * velocityMultipleX * (shakeDirX_Abs / shakeDirSum));
            this.velocityShake.y = (float)(SHAKE_VELOCITY * velocityMultipleY * (shakeDirY_Abs / shakeDirSum));
        
        }
        
    }
    
    public void pauseMovement()
    {
        // The function pauses movement of the actor.
        this.pauseMovementInd = true;
    }
    
    private void performInitialization()
    {
        
        // The function performs basic shared preparation steps, such as setting defaults and initializing
        // arrays, array lists, hash maps, ...
        
        // Initialized elapsed time to 0.
        this.elapsedTime = 0;
        
        // Flag to not "shake" actor yet.
        this.shakeInd = false;
        
        // Initialize the velocity vectors.
        this.velocity = new Vector2();
        this.velocityShake = new Vector2();
        
        // Set number of animations to process related to "shaking" actor.
        shakeCount = 14;
        
        // Initialize "shake" counter.
        shakeCounter = 1;
        
    }
    
    public void resumeMovement()
    {
        // The function resumes movement of the actor.
        this.pauseMovementInd = false;
    }
    
    // shakeDistanceMax = Maximum number of pixels to move actor.
    public void startShake(int shakeDistance)
    {
        
        // The function sets values for and initiates a "shake" operation.
        // The actor shakes within the passed number of pixels of its original location.
        
        // Store current actor position as originating point.
        this.shakeOrigPosX = super.getX();
        this.shakeOrigPosY = super.getY();
        
        // Store maximum number of pixels to move actor with each "shake".
        this.shakeDistanceMax = shakeDistance;
        
        // Reset "shake" counter.
        this.shakeCounter = 1;
        
        // Turn on "shake" effect.
        this.shakeInd = true;
        
        // Perform "shake" calculations.
        nextShake(null, null);
        
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
    
    public boolean getShakeInd() {
        return shakeInd;
    }

    public void setShakeInd(boolean shakeInd) {
        // Do not start a "shake" movement here.
        this.shakeInd = shakeInd;
    }
    
    public boolean getSlideToBasePosInd() {
        return slideToBasePosInd;
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