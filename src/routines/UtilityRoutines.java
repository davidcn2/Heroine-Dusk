package routines;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;

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

public class UtilityRoutines 
{
    
    /* 
    The class includes utility / helper functions.
    
    Methods include:
    
    toRGB:  Converts the passed combination of red, green, and blue values from RGB to LibGDX format.
    */
    
    public UtilityRoutines()
    {
        
    }
    
    // r = Red portion of color (in RGB format).
    // g = Green portion of color (in RGB format).
    // b = Blue portion of color (in RGB format).
    public static Color toRGB(float r, float g, float b) 
    {
        
        // The function converts the passed combination of red, green, and blue values from RGB to LibGDX format.
        
        float blue; // Blue portion of color, in LibGDX format.
        float green; // Green portion of color, in LibGDX format.
        float red; // Red portion of color, in LibGDX format.
        
        // Convert to LibGDX format.
        red = r / 255.0f;
        green = g / 255.0f;
        blue = b / 255.0f;
        
        // Return the LibGDX (combined) color value.
        return new Color(red, green, blue, 1f);
 
    }
    
}
