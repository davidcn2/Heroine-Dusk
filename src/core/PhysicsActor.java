package core;

// LibGDX imports.
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

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
public class PhysicsActor extends AnimatedActor { // Extends the AnimatedActor class.

    /*
    The main purpose of the AnimatedActor class involves managing velocity and acceleration data,
    allowing movement to appear much smoother.  Instead of setting simply a constant velocity,
    the class allows for acceleration and its related effects on velocity.  Acceleration causes
    actors to slowly gain speed.  The class stores (velocity and acceleration) information using
    Vector2 objects.  The class also includes convenience functions, such as adding and calculating
    vectors.  The maxSpeed value stops the actor from gaining speed indefinitely.  The deceleration
    value controls how quickly the actor slows down when not accelerating.  Finally, a boolean
    autoAngle variable determines whether to rotate the image associated with an actor to match
    the direction of motion.
    */

    // Acceleration = How quickly an Actor gains speed.
    // Deceleration = How quickly an Actor slows down (loses speed).

    // Methods include:

    // accelerateForward:  Sets the acceleration rate to the passed value.
    // act:  Performs a time based positional update, applies acceleration to velocity based on elapsed
    //       seconds since last frame, decreases velocity when not accelerating, caps speed to maximum,
    //       moves actor based on current velocity and elapsed seconds since last frame.  If flagged,
    //       rotate image to match velocity.
    // addAccelerationAS:  Adds to the acceleration vector using the passed angle and speed.
    // addAccelerationXY:  Adds the passed x and y values to the acceleration vector.
    // addAccelerationX:  Adds the passed x value to the acceleration vector.
    // addAccelerationY:  Adds the passed y value to the acceleration vector.
    // addVelocityXY:  Adds the passed x and y values to the velocity vector.
    // addVelocityX:  Adds the passed x value to the velocity vector.
    // addVelocityY:  Adds the passed y value to the velocity vector.
    // copy:  Copies properties from the passed to the current PhysicsActor.
    // clone:  Returns a PhysicsActor with the same properties as the current.
    // getMotionAngle:  Returns the angle related to the velocity (speed) vector.
    // getSpeed:  Returns the velocity (speed).
    // setAccelerationAS:  Sets the acceleration vector using the passed angle and speed.
    // setAccelerationXY:  Sets the acceleration vector using the passed x and y values.
    // setAutoAngle:  Sets flag indicating whether to rotate image to match velocity (speed) to passed value.
    // setDeceleration:  Sets the deceleration rate to the passed value.
    // setMaxSpeed:  Sets the maximum velocity (speed) to the passed value.
    // setSpeed:  Sets the velocity (speed) to the passed value.
    // setVelocityAS:  Sets the velocity vector using the passed angle and speed.
    // setVelocityXY:  Sets the velocity vector using the passed x and y values.
    // setVelocityX:  Sets the velocity vector using the passed x value.
    // setVelocityY:  Sets the velocity vector using the passed y value.

    @SuppressWarnings("FieldMayBeFinal")
    private Vector2 velocity; // Actor velocity (speed) in x and y directions.
    @SuppressWarnings("FieldMayBeFinal")
    private Vector2 acceleration; // Actor acceleration rate in x and y directions.
    private float maxSpeed; // Maximum velocity (speed).
    private float deceleration; // Actor deceleration rate in x and y directions.
    private boolean autoAngle; // Whether to rotate image to match velocity.

    public PhysicsActor()
    {

        // The constructor of the class:

        // 1.  Initializes the velocity and acceleration vector objects.
        // 2.  Sets the maximum speed.
        // 3.  Sets the deceleration rate to zero.
        // 4.  Sets the image to NOT rotate to match velocity.

        velocity = new Vector2(); // Initialize velocity vector.
        acceleration = new Vector2(); // Initialize acceleration vector.
        maxSpeed = 9999; // Set default maximum speed.
        deceleration = 0; // Set default deceleration rate to zero.
        autoAngle = false; // Set image to NOT rotate to match velocity.

    }

    // Velocity methods...

    // vx = The x value to set the velocity vector.
    // vy = The y value to set the velocity vector.
    public void setVelocityXY(float vx, float vy)
    {
        // Set the velocity vector using the passed x and y values.
        velocity.set(vx,vy);
    }

    // vx = The x value to set the velocity vector.
    public void setVelocityX(float vx)
    {
        // Set the x portion of the velocity vector using the passed value.
        velocity.set(vx,velocity.y);
    }

    // vy = The y value to set the velocity vector.
    public void setVelocityY(float vy)
    {
        // Set the y portion of the velocity vector using the passed value.
        velocity.set(velocity.x,vy);
    }

    // vx = The x value to add to the velocity vector.
    // vy = The y value to add to the velocity vector.
    public void addVelocityXY(float vx, float vy)
    {
        // Add the passed x and y values to the velocity vector.
        velocity.add(vx,vy);
    }

    // vx = The x value to add to the velocity vector.
    public void addVelocityX(float vx)
    {
        // Add the passed x value to the velocity vector.
        velocity.add(vx,0);
    }

    // vy = The y value to add to the velocity vector.
    public void addVelocityY(float vy)
    {
        // Add the passed y value to the velocity vector.
        velocity.add(0,vy);
    }

    // angleDeg = Angle to use to set velocity vector.
    // speed = Speed to use to set velocity vector.
    public void setVelocityAS(float angleDeg, float speed)
    {

        // The method sets the velocity vector using the passed angle and speed.

        // If A represents the direction angle of the vector and M provides magnitude, then
        // calculate the x and y components of the vector using the formulae below:
        // x = M × cos(A)
        // y = M × sin(A)

        // Calculate and store x component of velocity vector based on passed angle and speed.
        velocity.x = speed * MathUtils.cosDeg(angleDeg);

        // Calculate and store y component of velocity vector based on passed angle and speed.
        velocity.y = speed * MathUtils.sinDeg(angleDeg);

    }

    public float getSpeed()
    {
        // Return the velocity (speed).
        return velocity.len();
    }

    // s = Value to which to set velocity (speed).
    public void setSpeed(float s)
    {
        // Set the velocity (speed) to the passed value.
        velocity.setLength(s);
    }

    // ms = Value to which to set the maximum velocity (speed).
    public void setMaxSpeed(float ms)
    {
        // Set the maximum velocity (speed) to the passed value.
        maxSpeed = ms;
    }

    // Acceleration / Deceleration methods...

    // ax = The x value to which to set the acceleration vector.
    // ay = The y value to which to set the acceleration vector.
    @SuppressWarnings("SameParameterValue")
    public void setAccelerationXY(float ax, float ay)
    {
        // Set the acceleration vector using the passed x and y values.
        acceleration.set(ax,ay);
    }

    // vx = The x value to add to the acceleration vector.
    // vy = The y value to add to the acceleration vector.
    public void addAccelerationXY(float ax, float ay)
    {
        // Add the passed x and y values to the acceleration vector.
        acceleration.add(ax,ay);
    }

    // vx = The x value to add to the acceleration vector.
    public void addAccelerationX(float ax)
    {
        // Add the passed x value to the acceleration vector.
        acceleration.add(ax,0);
    }

    // vy = The y value to add to the acceleration vector.
    public void addAccelerationY(float ay)
    {
        // Add the passed y value to the acceleration vector.
        acceleration.add(0,ay);
    }

    // angleDeg = Angle to use when increasing acceleration.
    // speed = Speed to use when increasing acceleration.
    @SuppressWarnings("SameParameterValue")
    public void addAccelerationAS(float angleDeg, float speed)
    {

        // The method adds to the acceleration vector using the passed angle and speed.

        // If A represents the direction angle of the vector and M provides magnitude, then
        // calculate the x and y components of the vector using the formulae below:
        // x = M × cos(A)
        // y = M × sin(A)

        // Calculate and add to acceleration vector based on passed angle and speed.
        acceleration.add(
                speed * MathUtils.cosDeg(angleDeg),
                speed * MathUtils.sinDeg(angleDeg) );

    }

    // angleDeg = Angle to use to set acceleration.
    // speed = Speed to use to set acceleration.
    public void setAccelerationAS(float angleDeg, float speed)
    {

        // The method sets the acceleration vector using the passed angle and speed.

        // If A represents the direction angle of the vector and M provides magnitude, then
        // calculate the x and y components of the vector using the formulae below:
        // x = M × cos(A)
        // y = M × sin(A)

        // Calculate and store x component of acceleration vector based on passed angle and speed.
        acceleration.x = speed * MathUtils.cosDeg(angleDeg);

        // Calculate and store y component of velocity vector based on passed angle and speed.
        acceleration.y = speed * MathUtils.sinDeg(angleDeg);

    }

    // speed = Value to which to set acceleration rate.
    public void accelerateForward(float speed)
    {
        // Set the acceleration rate, based on the passed speed and existing rotation.
        setAccelerationAS( getRotation(), speed );
    }

    public void setDeceleration(float d)
    {
        // Set the deceleration rate to the passed value.
        deceleration = d;
    }

    // Angle methods...

    private float getMotionAngle()
    {
        // Return the angle related to the velocity (speed) vector.
        return MathUtils.atan2(velocity.y, velocity.x) * MathUtils.radiansToDegrees;
    }

    // b = Whether to rotate image to match velocity (speed) to passed value.
    public void setAutoAngle(boolean b)
    {
        // Set flag indicating whether to rotate image to match velocity (speed) to passed value.
        autoAngle = b;
    }

    // Other methods...

    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {

        // The function:

        // 1.  Performs a time based positional update.
        // 2.  Applies acceleration to velocity based on elapsed seconds since last frame.
        // 3.  Decreases velocity when not accelerating.
        // 4.  Caps speed to maximum.
        // 5.  Moves actor based on current velocity and elapsed seconds since last frame.
        // 6.  If flagged, rotate image to match velocity.

        float decelerateAmount; // Amount by which to decelerate.

        // Call the act method of the Actor, which performs a time based positional update.
        super.act(dt);

        // Apply acceleration to velocity (based on elapsed seconds since last frame).
        velocity.add( acceleration.x * dt, acceleration.y * dt );

        // Decrease velocity when not accelerating.

        // If acceleration less than 0.01, then...
        if (acceleration.len() < 0.01)

        {

            // Acceleration less than 0.01.

            // Set amount to decelerate -- based on elapsed seconds since last frame.
            decelerateAmount = deceleration * dt;

            // If current speed less than deceleration amount, then...
            if ( getSpeed() < decelerateAmount )

                // Current speed less than deceleration amount.
                // Set speed to zero -- no velocity or movement.
                setSpeed(0);

            else

                // Current speed greater than or equal to deceleration amount.
                // Decrease speed by deceleration amount.
                setSpeed( getSpeed() - decelerateAmount );

        }

        // Cap speed to maximum.

        // If current greater than maximum speed, then...
        if ( getSpeed() > maxSpeed )

            // Current greater than maximum speed.
            // Set current speed to maximum.
            setSpeed(maxSpeed);

        // Apply velocity.

        // Moves current Actor (adds X and Y to current position) based on current velocity (speed)
        // and elapsed seconds since last frame.
        moveBy( velocity.x * dt, velocity.y * dt );

        // If flagged, rotate image when moving.

        // If configured to rotate image when moving and speed greater than 0.1, then...
        if (autoAngle && getSpeed() > 0.1 )

            // Configured to rotate image when moving and speed greater than 0.1.

            // Rotate image to match velocity.
            setRotation( getMotionAngle() );

    }

    // original = PhysicsActor from which to copy properties.
    public void copy(PhysicsActor original)
    {

        // The function copies properties from the passed to the current PhysicsActor.
        // Properties include:  velocity, acceleration, maximum speed, deceleration, auto angle flag, and
        // all properties of the related AnimatedActor.

        // Copy properties related to the associated AnimatedActor.
        super.copy(original);

        // Copy velocity, acceleration, maximum speed, deceleration, and auto angle flag.
        this.velocity     = new Vector2(original.velocity);
        this.acceleration = new Vector2(original.acceleration);
        this.maxSpeed     = original.maxSpeed;
        this.deceleration = original.deceleration;
        this.autoAngle    = original.autoAngle;

    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntCallSuperClone"})
    @Override
    public PhysicsActor clone()
    {

        // The function returns a PhysicsActor with the same properties as the current.

        PhysicsActor newbie; // PhysicsActor to which to copy properties.

        // Instantiate new PhysicsActor object.
        newbie = new PhysicsActor();

        // Copy properties of current (class-level) to new PhysicsActor object.
        newbie.copy( this );

        // Return the new PhysicsActor object.
        return newbie;

    }

}