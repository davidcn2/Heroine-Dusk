package core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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

public abstract class BaseGame extends Game { // Extends the Game class from LibGDX.

    /*
    Define the class as abstract so that subclasses (xyz extends BaseScreen) can implement methods in
    different ways.  The class gets defined as abstract, instead of an interface, since it provides
    code for some of its methods.  The reused methods in the current class must use the abstract
    modifier.

    Detailed class description:

    The class extends the basic functionality of a Game class to support reusable resources with Skin
    objects.  The Skin object allows the creation of common UI elements when initializing the Game
    class.  The Skin object stores the information in a data structure that can get accessed by the
    Screen objects at a later time.

    LibGDX methods for screen include:

    create:  The function occurs during the startup / create phase.
    dispose: The function occurs during the cleanup phase and clears objects from memory.

    The class extends the basic functionality of a Game class.
    The Game abstract class provides an implementation of ApplicationListener, along with some helper
    methods to set and handle Screen rendering.  The Game class simplifies handling of multiple
    screens.
    */

    public Skin skin; // Used to store resources common to multiple screens.

    public BaseGame()
    {

        // The constructor of the class:

        // 1.  Initializes the Skin object.

        // Initialize the Skin object.
        skin = new Skin();

    }

    @Override
    public abstract void create();

    @Override
    public void dispose()
    {

        /*
        The function occurs during the cleanup phase and clears objects from memory.
        */

        // Clear objects from memory.
        skin.dispose();
        super.dispose();

    }

}