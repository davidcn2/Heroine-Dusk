package routines;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
    
    bitwiseList:  Returns a list of numbers that add up to the passed value, based on bitwise operations.
    toRGB:  Converts the passed combination of red, green, and blue values from RGB to LibGDX format.
    */
    
    // Array of numbers to check in bitwise list.
    private static final Integer[] BITWISE_LIST =  new Integer[]{ 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024,
      2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 
      33554432, 67108864, 134217728, 268435456, 536870912, 1073741824};
    
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
    
    // value = Value to reach.
    public static ArrayList<Integer> bitwiseList(int value)
    {
        
        // The function returns a list of numbers that add up to the passed value, based on bitwise operations.
        
        ArrayList<Integer> temp; // List of numbers that add up to the passed value.
        
        // Initialize array list.
        temp = new ArrayList<>();
        
        // If value is 0, then
        if (value == 0)
        {
            // Value is 0.
            
            // Add 0 to array list.
            temp.add(value);
        }
        
        else
        {
            // Value greater than 0.
            
            // If 1 a bitwise value, then...
            if ((value & 1) == 1)
                temp.add(1);
            
            // If value greater than 1, then...
            if (value > 1)
            {
                
                // Value greater than 1.
                
                // Loop through numbers to check.
                for (int check : BITWISE_LIST)
                {
                    
                    // If current number a bitwise value, then...
                    if ((value & check) == check)
                        temp.add(check);
                    
                }
                
            } // End ... Value greater than 1.
            
        } // End ... Value greater than 0.
        
        // Return the array list.
        return temp;
        
    }
    
    // start = First integer in random range.
    // end = Last integer in random range.
    public static int generateStandardRnd(SecureRandom number, int start, int end)
    {
        
        // The function returns a random number in the passed range.
        
        int range; // Difference between start and end.
        
        // Calculate difference between start and end.
        range = end - start;
        
        // Return random number in passed range.
        return (number.nextInt(range + 1) + start);
        
    }
    
}