package heroinedusk;

import com.badlogic.gdx.utils.TimeUtils;

/*
Interface (implements) vs Sub-Class (extends)...

The distinction is that implements means that you're using the elements of a Java Interface in your 
class, and extends means that you are creating a subclass of the class you are extending. You can 
only extend one class in your new class, but you can implement as many interfaces as you would like.

Interface:  A Java interface is a bit like a class, except a Java interface can only contain method
signatures and fields. An Java interface cannot contain an implementation of the methods, only the 
signature (name, parameters and exceptions) of the method. You can use interfaces in Java as a way
to achieve polymorphism.

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use 
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.
*/

public class Timer 
{
    
    /* 
    The class provides timer / delay related functionality.
    
    Methods include:
    
    checkDelay:  Uses a discrete timing mechanism to check for the (single) next time the delay finishes.
    checkTimer:  Uses a continuous timing mechanism to check for each instance the time interval finishes.
    */
    
    // Declare regular variables.
    private boolean continuousInd; // Whether continuous (true) or discrete (false).  Discrete = reset every
      // delay.
    private boolean delayInd; // Whether delay active.
    private long delayLength; // Length of delay (or between reset), in milliseconds.
    private long intervalLength; // Length of interval, in milliseconds.
    private long lastTimeDelay; // Difference, measured in milliseconds, between the current time and 
      // midnight, January 1, 1970 UTC.  Used to provide a delay during combat.
    private long lastTimeInterval; // Difference, measured in milliseconds, between the current time and 
      // midnight, January 1, 1970 UTC.
    private long sinceChange; // Milliseconds elapsed, resetting as specified in intervalLength
      // (1000 = 1 second, ...).
    
    // delayLength = Length of delay, in milliseconds.
    public Timer(long delayLength)
    {
        
        // The constructor sets the delay length.
        
        // Set value of delay length.
        this.delayLength = delayLength;
        
    }
    
    // timeLength = Length of delay or interval, in milliseconds.
    // continuousInd = Whether continuous (true) or discrete (false).  Discrete = reset every delay.
    public Timer(long timeLength, boolean continuousInd)
    {
        
        // The constructor sets the delay length and continuous indicator.
        
        // If using continuous timing mechanism, then...
        if (continuousInd)
        {
            
            // Using continuous timing mechanism.
            
            // Get difference, measured in milliseconds, between the current time and midnight, 01/01/1970 UTC.
            lastTimeInterval = TimeUtils.millis();
            
            // Set value of interval length.
            this.intervalLength = timeLength;
            
        }
        
        else
        {
            
            // Using discrete timing mechanism.
            
            // Set value of delay length.
            this.delayLength = timeLength;
            
        }
        
        // Set value of continuous indicator.
        this.continuousInd = continuousInd;
        
    }
    
    public boolean checkDelay()
    {
        
        // The function uses a discrete timing mechanism to check for the (single) next time the
        // delay finishes.  Example:  Waiting one time for 0.500 seconds after an event occurs.
        // The function returns true when the timer (delay) finishes.
        
        boolean delayFinished; // Whether delay finished.
        long delta; // Difference, measured in milliseconds, between current and last time check.
        
        // Set defaults.
        delayFinished = false;
        
        // If delay active, then...
        if ( delayInd )
        {

            // Delay active.
        
            // Get difference, measured in milliseconds, between current and last time check.
            delta = TimeUtils.timeSinceMillis( lastTimeDelay );
            
            // If specified seconds passed, then...
            if ( delta >= this.delayLength )
            {

                // Specified seconds passed.

                // Flag delay as inactive.
                delayInd = false;
                
                // Flag delay as complete.
                delayFinished = true;
                
            }
            
        }
        
        else
        {
            
            // Delay not started yet.
            
            // Flag delay as active.
            delayInd = true;

            // Store initial timer value.
            // Get difference, measured in milliseconds, between the current time and midnight, 01/01/1970 UTC.
            lastTimeDelay = TimeUtils.millis();
            
        }
        
        // Return whether delay complete.
        return delayFinished;
        
    }
    
    public boolean checkTimer()
    {
        
        // The function uses a continuous timing mechanism to check for each instance the time
        // interval finishes.  Example:  Waiting repeatedly for 1.000 seconds for a FPS clocker.
        // The function returns true when an interval finishes.
        
        boolean intervalFinished; // Whether current interval finished.
        long delta; // Difference, measured in milliseconds, between current and last time check.
        
        // Set defaults.
        intervalFinished = false;
        
        // Get difference, measured in milliseconds, between current and last time check.
        delta = TimeUtils.timeSinceMillis(lastTimeInterval);
        
        // Get difference, measured in milliseconds, between the current time and midnight, 01/01/1970 UTC.
        lastTimeInterval = TimeUtils.millis();
        
        // Update time elapsed (reset every interval).
        sinceChange += delta;
        
        // If interval elapsed, then...
        if(sinceChange >= intervalLength) 
        {

            // Interval elapsed.
            
            // Reset elapsed time.
            sinceChange = 0;
            
            // Flag current interval as complete.
            intervalFinished = true;

        }
        
        // Return whether current interval complete.
        return intervalFinished;
        
    }
    
    // Getters and setters below...
    
    public void setContinuousInd(boolean continuousInd) {
        this.continuousInd = continuousInd;
    }
    
    public void setDelayInd(boolean delayInd) {
        this.delayInd = delayInd;
    }
    
    public void setDelayLength(long delayLength) {
        this.delayLength = delayLength;
    }
    
    public void setIntervalLength(long intervalLength) {
        this.intervalLength = intervalLength;
    }
    
}
