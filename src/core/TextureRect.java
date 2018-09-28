package core;

import java.awt.geom.Rectangle2D;

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

public class TextureRect 
{

    // The inner class stores information about a rectangle associated with a texture.
    // One use of the class involves texture splitting.
    
    // Declare regular variables.
    private int height; // Height of the region to extract.
    private int src_x; // X-coordinate of the bottom left position of the image to extract.
    private int src_y; // Y-coordinate of the bottom left position of the image to extract.
    private int width; // Width of the region to extract.
    
    // src_x = X-coordinate of the bottom left position of the image to extract.
    // src_y = Y-coordinate of the bottom left position of the image to extract.
    // width = Width of the region to extract.
    // height = Height of the region to extract.
    public TextureRect(int src_x, int src_y, int width, int height)
    {

        // The constructor stores the region information.

        // Store the region information in the class-level variables.
        this.src_x = src_x;
        this.src_y = src_y;
        this.width = width;
        this.height = height;

    }
    
    // src_x = X-coordinate of the bottom left position of the image to extract.
    // src_y = Y-coordinate of the bottom left position of the image to extract.
    // width = Width of the region to extract.
    // height = Height of the region to extract.
    // scaleFactor = Scale factor, equal to scale in Config class.
    public TextureRect(int src_x, int src_y, int width, int height, int scaleFactor)
    {

        // The constructor stores the region information.

        // Store the region information in the class-level variables.
        this.src_x = src_x * scaleFactor;
        this.src_y = src_y * scaleFactor;
        this.width = width * scaleFactor;
        this.height = height * scaleFactor;

    }

    // rect = Region information to copy from, adjusting by scale factor.
    // scaleFactor = Scale factor, equal to scale in Config class.
    public TextureRect(TextureRect rect, int scaleFactor)
    {

        // The constructor stores the region information.

        // Store the region information in the class-level variables.
        this.src_x = rect.src_x * scaleFactor;
        this.src_y = rect.src_y * scaleFactor;
        this.width = rect.width * scaleFactor;
        this.height = rect.height * scaleFactor;

    }
    
    // Getters and setters below...
    
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public Rectangle2D.Float getRect2D() {
        return new Rectangle2D.Float((float)src_x, (float)src_y, (float)width, (float)height);
    }
    
    /*
    new Rectangle2D.Float((float)rects[0].getSrc_x(), 
          (float)rects[0].getSrc_y(), (float)rects[0].getWidth(), rects[0].getHeight()
    */
    
    public int getSrc_x() {
        return src_x;
    }

    public void setSrc_x(int src_x) {
        this.src_x = src_x;
    }
    
    public int getSrc_y() {
        return src_y;
    }

    public void setSrc_y(int src_y) {
        this.src_y = src_y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
}