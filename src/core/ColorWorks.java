package core;

import com.badlogic.gdx.graphics.Color;
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
public class ColorWorks {

    // The class provides functionality related to colors, including generating a random one.

    // Methods include:

    // getColor:  Gets the current Color.
    // getRandomColor:  Generates, stores, and returns a random Color.
    // setColor:  Sets the current Color.

    private Color theColor; // A color.
    private final Color[] colorSet; // Set of colors.

    ColorWorks()
    {

        // The constructor of the class populates the array of commonly used colors.

        // Allocate memory for 17 Colors.
        colorSet = new Color[18];

        // Initialize array elements, skipping Clear.
        colorSet[0] = Color.BLACK;
        colorSet[1] = Color.BLUE;
        colorSet[2] = Color.CYAN;
        colorSet[3] = Color.DARK_GRAY;
        colorSet[4] = Color.GRAY;
        colorSet[5] = Color.GREEN;
        colorSet[6] = Color.LIGHT_GRAY;
        colorSet[7] = Color.MAGENTA;
        colorSet[8] = Color.MAROON;
        colorSet[9] = Color.NAVY;
        colorSet[10] = Color.OLIVE;
        colorSet[11] = Color.ORANGE;
        colorSet[12] = Color.PINK;
        colorSet[13] = Color.PURPLE;
        colorSet[14] = Color.RED;
        colorSet[15] = Color.TEAL;
        colorSet[16] = Color.WHITE;
        colorSet[17] = Color.YELLOW;

    }

    Color getRandomColor()
    {

        // The function generates, stores, and returns a random color.

        int colorNumber; // Random number used to determine which color to return.

        // Generate a random number used to determine which color to return.
        colorNumber = MathUtils.random(17);

        // Store the random color.
        theColor = colorSet[colorNumber];

        // Return the random color.
        return this.theColor;

    }

    public Color getColor() {
        // The function returns the Color.
        return this.theColor;
    }

    public void setColor(Color theColor) {
        // The function sets the Color.
        this.theColor = theColor;
    }

}