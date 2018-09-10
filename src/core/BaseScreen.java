package core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.awt.Point;

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
// Implements Screen, InputProcessor, and ControllerListener interfaces from LibGDX.
public abstract class BaseScreen implements Screen, InputProcessor, ControllerListener

{

    /*
    The class uses the elements of the Screen, InputProcessor, and ControllerListener classes in LibGDX.

    Define the class as abstract so that subclasses (xyz extends BaseScreen) can implement methods in
    different ways.  The class gets defined as abstract, instead of an interface, since it provides
    code for some of its methods.  The reused methods in the current class must use the abstract
    modifier.

    Detailed class description:

    The class refactors common elements from subclasses to provide for reuse and eliminate redundancy.
    The class handles the following tasks, in common to all Screen-extending classes:

    1.  Provide a reference to the Game object that instantiated the current class.
    2.  Initialize the stage objects.
    3.  In the render method, call the act method of the stages, clear the screen, and then
        call the draw method of the stages.
    4.  Provide empty methods for all the Screen, InputProcessor, and ControllerListener interface methods.
    5.  Provide methods for pausing the game, resizing the window, center a label in a stage.

    The game-specific (sub)classes no longer need to deal with much of the infrastructure, such as
    declaring and initializing Game and Stage variables.  The subclasses must now contain only two
    methods:  create and update.  The update method in the current class contains much of the act
    methods for each stage and all of the code that actually performed rendering operations.

    Note:  The creation step occurs in the subclass.

    LibGDX methods for screen include:

    dispose:  The method should get called manually when exiting a screen.  Add logic to clear memory
    associated with certain LibGDX objects.  In LibGDX, some objects need to get disposed manually,
    as they are not managed by the garbage collector.  The dispose() method is supposed to dispose all
    of these objects -- to release their resources.
    Dispose of the following objects manually:  AssetManager, Bitmap, BitmapFont, BitmapFontCache,
    CameraGroupStrategy, DecalBatch, ETC1Data, FrameBuffer, Mesh, Model, ModelBatch, ParticleEffect,
    Pixmap, PixmapPacker, Shader, ShaderProgram, Shape, Skin, SpriteBatch, SpriteCache, Stage, Texture,
    TextureAtlas, TileAtlas, TileMapRenderer, com.badlogic.gdx.physics.box2d.World, and all bullet
    classes.
    hide:  Called when the screen is no longer the current one for a Game.
    pause:  Called when the Application is paused, usually when not active or visible.
    render:  Called when the screen should render itself.
    resize:  Adjusts the size of the Viewport objects of the stages whenever the window size changes.
             Whenever a resize event occurs, the viewport needs to be informed and updated.
    resume:  Called when the Application is resumed from a paused state, usually when it regains focus.
    show:  Called when the screen becomes the current one for a Game.
    wakeBaseScreen:  Called when redisplaying the already initialized screen.

    LibGDX methods for InputProcessor include:

    keyDown:  Called when user presses a key.
    keyTyped:  Called when user types a key.
    keyUp:  Called when user releases a key.
    mouseMoved:  Called when the mouse moved without pressing any buttons.
    scrolled:  Called when the user scrolls the mouse wheel.
    touchDown:  Called when touching the screen or pressing a mouse button.
    toughDragged:  Called when dragging a finger or the mouse.
    touchUp:  Called when lifting a finger from the screen or releasing a mouse button.

    LibGDX methods for ControllerListener include:

    connected:  Called when the gamepad connects.
    disconnected:  Called when gamepad disconnects.
    xSliderMoved:  An x-slider on the controller moved.  The sliderCode is controller specific. The
                   com.badlogic.gdx.controllers.mapping package hosts slider constants for known controllers.
    ySliderMoved:  A y-slider on the controller moved.  The sliderCode is controller specific. The
                   com.badlogic.gdx.controllers.mapping package hosts slider constants for known controllers.
    accelerometerMoved:  An accelerometer value on the controller changed.  The accelerometerCode is controller
                         specific.  The com.badlogic.gdx.controllers.mapping package hosts slider constants for
                         known controllers.  The value is a Vector3 representing the acceleration on a 3-axis
                         accelerometer in m/s^2.
    povMoved:  A POV on the controller moved. The povCode is controller specific.  The
               com.badlogic.gdx.controllers.mapping package hosts POV constants for known controllers.
    axisMoved:  An axis on the controller moved.  The axisCode is controller specific.  The axis value is in the
                range [-1, 1].  The com.badlogic.gdx.controllers.mapping package hosts axes constants for known
                controllers.
                Represents the analog stick...
                Value will range from -1 to 1 depending the stick position -- left/right, up/down.
    buttonDown:  A button on the controller was pressed.  The buttonCode is controller specific.  The
	             com.badlogic.gdx.controllers.mapping package hosts button constants for known controllers.
    buttonUp:  A button on the controller was released.  The buttonCode is controller specific.  The
	           com.badlogic.gdx.controllers.mapping package hosts button constants for known controllers.

    Custom methods include:

    centerLabelUI:  Centers the label in the specified stage.
    disposeManual:  Allows for manually clearing of LibGDX resources from memory when overriding the 
      normal dispose method.
    isPaused:  Returns the pause state of the game (true or false).
    setPaused:  Sets the pause state of the game to the passed value.
    togglePaused:  Reverses the pause state of the game (true to false, false to true).
    update:  The abstract method (defined in the subclasses) occurs during the update phase (render method)
             and contains code related to game logic.
    */

    // Protected variables and methods allow the class itself to access them, classes inside of the
    // same package to access them, and subclasses of that class to access them.

    // A final variable can only be initialized once, either via an initializer or an assignment statement.

    public AssetManager manager; // Loads and stores assets like textures, bitmap fonts, tile maps, sounds, music, ...

    public BaseGame game; // Screen object used for current window.
    // Game objects allow an application to easily have multiple screens.

    public Stage mainStage; // Stores a 2D scene graph containing the hierarchies of actors.
    // Stage handles the viewport and distributes input events.  Contains the non-UI actors.

    public Stage uiStage; // Stores a 2D scene graph containing UI actors.  Includes win text / labels.

    // A Table consists of Cell objects, laid out in rows and columns, each Cell containing an Actor.
    Table uiTable; // Table containing main menu elements.

    protected int viewHeightMain; // Window height for the main stage.
    protected int viewHeightUI; // Window height for the ui stage.
    protected int viewWidthMain; // Window width for the main stage.
    protected int viewWidthUI; // Window width for the ui stage.

    private boolean paused; // Whether game paused.

    // g = Screen object for current window.
    // windowWidth = Width to use for stages.
    // windowHeight = Height to use for stages.
    public BaseScreen(BaseGame g, int windowWidth, int windowHeight)
    {

        // The constructor of the class:

        // 1.  Stores the window width and height.
        // 2.  Stores the Screen object for the current window.
        // 3.  Scales each stage and its contents to fit the current window size.
        // 4.  Sets up the input multiplexer to receive and pass all input data to current class and stages.
        // 5.  Creates and attaches Table to UI (main menu) stage.
        // 6.  Clear active ControllerListener objects and activate the listener.

        InputMultiplexer im; // Contains a group of input processors.  The base screen and each
        // stage get added to the input multiplexer.  When input events occur, the multiplexer
        // forwards the information to each of the attached objects.

        // Set defaults.
        this.paused = false;

        // Set window size values, based on parameters.
        this.viewWidthMain = windowWidth;
        this.viewWidthUI = windowWidth;
        this.viewHeightMain = windowHeight;
        this.viewHeightUI = windowHeight;

        // Store Screen object for current window.
        this.game = g;

        // Scale each stage and its contents to fit the current window size.
        // If aspect ratio of window does not match stage, fill in extra region with solid black.
        mainStage = new Stage( new FitViewport(windowWidth, windowHeight) );
        uiStage   = new Stage( new FitViewport(windowWidth, windowHeight) );

        // An InputMultiplexer object is itself an InputProcessor that contains a list of other InputProcessors.

        // Set up input multiplexer to receive all input data and pass the information along to
        // the current class and the stages.
        im = new InputMultiplexer(this, uiStage, mainStage);
        Gdx.input.setInputProcessor( im );

        uiTable = new Table(); // Create new Table object.
        uiTable.setFillParent(true); // The method will set the size of the Table to that of the stage.
        uiStage.addActor(uiTable); // Attach Table to the stage.

    }

    // g = Screen object for current window.
    // mainWidth = Width to use for main stage.
    // mainHeight = Height to use for main stage.
    // uiWidth = Width to use for ui stage.
    // uiHeight = Height to use for ui stage.
    public BaseScreen(BaseGame g, int mainWidth, int mainHeight, int uiWidth, int uiHeight)
    {

        // The constructor of the class:

        // 1.  Stores the window width and height.
        // 2.  Stores the Screen object for the current window.
        // 3.  Scales each stage and its contents to fit the current window size.
        // 4.  Sets up the input multiplexer to receive and pass all input data to current class and stages.
        // 5.  Creates and attaches Table to UI (main menu) stage.
        // 6.  Clear active ControllerListener objects and activate the listener.

        InputMultiplexer im; // Contains a group of input processors.  The base screen and each
        // stage get added to the input multiplexer.  When input events occur, the multiplexer
        // forwards the information to each of the attached objects.

        // Set defaults.
        this.paused = false;

        // Set window size values, based on parameters.
        this.viewWidthMain = mainWidth;
        this.viewHeightMain = mainHeight;
        this.viewWidthUI = uiWidth;
        this.viewHeightUI = uiHeight;

        // Store Screen object for current window.
        this.game = g;

        // Scale each stage and its contents to fit the current window size.
        // If aspect ratio of window does not match stage, fill in extra region with solid black.
        mainStage = new Stage( new FitViewport(mainWidth, mainHeight) );
        uiStage   = new Stage( new FitViewport(uiWidth, uiHeight) );

        // An InputMultiplexer object is itself an InputProcessor that contains a list of other InputProcessors.

        // Set up input multiplexer to receive all input data and pass the information along to
        // the current class and the stages.
        im = new InputMultiplexer(this, uiStage, mainStage);
        Gdx.input.setInputProcessor( im );

        uiTable = new Table(); // Create new Table object.
        uiTable.setFillParent(true); // The method will set the size of the Table to that of the stage.
        uiStage.addActor(uiTable); // Attach Table to the stage.

    }

    // The abstract method (defined in the subclasses) occurs during the update phase (render method)
    // and contains code related to game logic.
    //
    // Sample actions might include:
    // 1.  Resets the speed of the mouse (to zero).
    // 2.  Handles key presses and mouse clicks -- adjusts x and y speed.
    // 3.  Restricts mouse to window boundaries.
    // 4.  Checks for xyz and adjusts accordingly.
    // 5.  Updates xyz and acts accordingly.
    // 6.  Centers camera on player (mouse).
    public abstract void update(float dt);

    // dt = Time span between the current and last frame in seconds.  Passed / populated automatically.
    @Override
    public void render(float dt)

    {

        /*
        The function occurs during the render phase and accomplishes the following:

        1.  Adjusts Actor positions and other properties in the UI stage.
        2.  If game not paused, adjusts Actor positions and other properties in the main stage and processes player input.
        3.  Draws the graphics.
        */

        // Call the Actor.act(float) method on each actor in the UI stage.
        // Typically called each frame.  The method also fires enter and exit events.
        // Updates the position of each Actor based on time.
        uiStage.act(dt);

        // Only pause gameplay events, not UI events.

        // If game not paused, then...
        if ( !isPaused() )
        {

            // Game active (not paused).

            // Call the Actor.act(float) method on each actor in the non-UI stage.
            // Typically called each frame.  The method also fires enter and exit events.
            // Updates the position of each Actor based on time.
            mainStage.act(dt);

            // Handle game logic -- allow processing based on player actions / input.
            update(dt);
        }

        // Draw graphics.

        // Overdraw the area with the given glClearColor.
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Clear the area using the specified buffer.  Supports multiple buffers.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stages -- containing the Actors.

        // Reposition rendering location of the main stage.
        Gdx.gl.glViewport(0, viewHeightUI - viewHeightMain, viewWidthMain, viewHeightMain );

        // Draw the main stage.
        mainStage.draw();

        // Reposition rendering location of the UI stage.
        Gdx.gl.glViewport(0,0, viewWidthUI, viewHeightUI);

        // Draw the UI stage.
        uiStage.draw();

    }

    // Pause methods follow...

    public boolean isPaused()
    {
        // The function returns the pause state of the game (true or false).
        return paused;
    }

    // b = Whether to pause game (true or false).
    public void setPaused(boolean b)
    {
        // The function sets the pause state of the game to the passed value.
        paused = b;
    }

    public void togglePaused()
    {
        // The function reverses the pause state of the game (true to false, false to true).
        paused = !paused;
    }

    // Provide methods required by Screen interface to prevent need to do so in subclasses:  resize, pause, resume, dispose, show, hide.

    // width = Current window window.
    // height = Current window height.
    @Override
    public void resize(int width, int height)
    {

        // The function adjusts the size of the Viewport objects of the stages whenever the
        // window size changes.

        // Adjust size of viewport of non-UI stage to current window width and height.
        mainStage.getViewport().update(width, height, true);

        // Adjust size of viewport of UI stage to current window width and height.
        uiStage.getViewport().update(width, height, true);

        // Recalculates the projection and view matrix of the cameras.
        mainStage.getCamera().update();
        uiStage.getCamera().update();

    }

    @Override
    public void pause()   {  }
    
    @Override
    public void resume()  {  }
    
    /*
    @Override
    public void dispose()
    {
        
        // The method occurs when removing the screen and allows for clearing of related resources from memory.
        
        // Clear LibGDX objects from memory.
        manager.dispose();
        uiStage.dispose();
        mainStage.dispose();
        //game.dispose();
        uiTable = null;
        
    }
    */

    @Override
    public void show()    {  }
    
    @Override
    public void hide()    {  }

    public void disposeManual()
    {
        
        // The method allows for manually clearing of LibGDX resources from memory when overriding the 
        // normal dispose method.

        // Clear LibGDX objects from memory.
        
        // If asset manager initialized, then...
        if (manager != null)
            manager.dispose();
        
        uiStage.dispose();
        mainStage.dispose();
        uiTable = null;
        
    }
    
    // Provide methods required by InputProcessor interface to prevent need to do so in subclasses:  keyDown, keyUp, keyTyped,
    // mouseMoved, scrolled, touchDown, touchDragged, touchUp.

    // Input.Keys constants -- https://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/Input.Keys.html.

    // keycode = Code related to the key pressed.   One of the constants in Input.Keys.
    @Override
    public boolean keyDown(int keycode)
    {
        // The function occurs when the user presses a key.

        // Return a value.
        return false;
    }

    // keycode = Code related to the key released.   One of the constants in Input.Keys.
    @Override
    public boolean keyUp(int keycode)
    {
        // The function occurs when the user releases a key.

        // Return a value.
        return false;
    }

    // keycode = Code related to the key typed.   One of the constants in Input.Keys.
    @Override
    public boolean keyTyped(char c)
    {
        // The function occurs when the user types a key.

        // Return a value.
        return false;
    }

    // screenX = Current X coordinate in the screen of the mouse.
    // screenY = Current Y coordinate in the screen of the mouse.
    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        // The function occurs when the user moves the mouse without pressing any buttons.
        // Notes:  The function does not get called on iOS.

        // Return a value.
        return false;
    }

    // amount = The scroll amount, -1 or 1, depending on the direction the user scrolled the wheel.
    @Override
    public boolean scrolled(int amount)
    {
        // The function occurs when the user scrolls the mouse wheel.
        // Notes:  The function does not get called on iOS.

        // Return a value.
        return false;
    }

    // screenX = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // screenY = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    // pointer = Pointer for the event.
    // button = Button pressed.
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        // The function occurs when the user touches the screen or presses a mouse button.
        // Notes:  The button parameter will be Input.Buttons.LEFT on iOS.

        // Return a value.
        return false;
    }

    // screenX = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // screenY = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    // pointer = Pointer for the event.
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        // The function occurs when the user drags a finger across the screen or the mouse.

        // Return a value.
        return false;
    }

    // screenX = The x coordinate where the user touched the screen, basing the origin in the upper left corner.
    // screenY = The y coordinate where the user touched the screen, basing the origin in the upper left corner.
    // pointer = Pointer for the event.
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        // The function occurs when the user lifts a finger or released a mouse button.
        // Notes:  The button parameter will be Input.Buttons.LEFT on iOS.

        // Return a value.
        return false;
    }

    // Provide methods required by ControllerListener interface to prevent need to do so in subclasses.

    // controller = Reference to the controller.
    @Override
    public void connected(Controller controller)
    {
        // The function occurs when the user connects a controller.
    }

    // controller = Reference to the controller.
    @Override
    public void disconnected(Controller controller)
    {
        // The function occurs when the user disconnects a controller.
    }

    // controller = Reference to the controller.
    // sliderCode = The sliderCode is controller specific.
    //   The com.badlogic.gdx.controllers.mapping package hosts slider constants for known controllers.
    // value = ?
    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        // The function occurs when the user moves the x-slider on the controller.
        // The function returns whether to hand the event to other listeners.

        // Return a value.
        return false;
    }

    // controller = Reference to the controller.
    // sliderCode = The sliderCode is controller specific.
    //   The com.badlogic.gdx.controllers.mapping package hosts slider constants for known controllers.
    // value = ?
    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        // The function occurs when the user moves the y-slider on the controller.
        // The function returns whether to hand the event to other listeners.

        // Return a value.
        return false;
    }

    // controller = Reference to the controller.
    // accelerometerCode = The accelerometerCode is controller specific.
    //   The com.badlogic.gdx.controllers.mapping package hosts slider constants for known controllers.
    // value = Vector3 representing the acceleration on a 3-axis accelerometer in m/s^2.
    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        // The function occurs when an accelerometer value on the controller changes.
        // The function returns whether to hand the event to other listeners.

        // Return a value.
        return false;
    }

    // controller = Reference to the controller.
    // povCode = The povCode is controller specific.
    //   The com.badlogic.gdx.controllers.mapping package hosts POV constants for known controllers.
    // value = ?
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        // The function occurs when a POV on the controller moves.
        // The function returns whether to hand the event to other listeners.

        // Return a value.
        return false;
    }

    // controller = Reference to the controller.
    // axisCode = The axis code is controller specific.
    //   The com.badlogic.gdx.controllers.mapping package hosts axes constants for known controllers.
    // value = The axis value, -1 to 1.
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        // The function occurs when an axis on the controller moves.
        // The function returns whether to hand the event to other listeners.

        // Return a value.
        return false;
    }

    // controller = Reference to the controller.
    // buttonCode = The buttonCode is controller specific.
    //   The com.badlogic.gdx.controllers.mapping package hosts button constants for known controllers.
    // value = ?
    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        // The function occurs when the user presses a button on the controller.
        // The function returns whether to hand the event to other listeners.

        // Return a value.
        return false;
    }

    // controller = Reference to the controller.
    // buttonCode = The buttonCode is controller specific.
    //   The com.badlogic.gdx.controllers.mapping package hosts button constants for known controllers.
    // value = ?
    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        // The function occurs when the user releases a button on the controller.
        // The function returns whether to hand the event to other listeners.

        // Return a value.
        return false;
    }

    // Other methods...

    public int getViewWidth() {
        return viewWidthMain;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidthMain = viewWidth;
    }

    public int getViewHeight() {
        return viewHeightMain;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeightMain = viewHeight;
    }

    // theStage = Stage in which to center label.
    // theLabel = Label to center.
    public Point centerLabelUI(Stage theStage, Label theLabel)
    {

        // The function centers the label in the specified stage and returns the x and y
        // coordinates used in its positions (based on the top left corner).

        float labelHeight; // Height of the label.
        float labelWidth; // Width of the label.
        float labelX; // New X position of the label.
        float labelY; // New Y position of the label.

        // Store label width and height.
        labelWidth = theLabel.getWidth();
        labelHeight = theLabel.getHeight();

        // Store new X and Y coordinates of the label.
        labelX = (viewHeightMain - (labelWidth * theLabel.getFontScaleX())) * 0.5f;
        labelY = (viewHeightMain - (labelHeight * theLabel.getFontScaleY())) * 0.5f;

        // Set coordinates of the label.
        theLabel.setPosition(labelX, labelY);

        // Returns the new X and Y coordinates of the label.
        return new Point((int)labelX, (int)labelY);

    }
    
    public void wakeBaseScreen()
    {

        // The method gets called when redisplaying the already initialized screen and
        // accomplishes the following:

        // 1.  Scales each stage and its contents to fit the current window size.
        // 2.  Sets up the input multiplexer to receive and pass all input data to current class and stages.
        // 3.  Creates and attaches Table to UI (main menu) stage.
        // 4.  Clear active ControllerListener objects and activate the listener.

        InputMultiplexer im; // Contains a group of input processors.  The base screen and each
        // stage get added to the input multiplexer.  When input events occur, the multiplexer
        // forwards the information to each of the attached objects.

        // Scale each stage and its contents to fit the current window size.
        // If aspect ratio of window does not match stage, fill in extra region with solid black.
        mainStage = new Stage( new FitViewport(viewWidthMain, viewHeightMain) );
        uiStage   = new Stage( new FitViewport(viewWidthUI, viewHeightUI) );

        // An InputMultiplexer object is itself an InputProcessor that contains a list of other InputProcessors.

        // Set up input multiplexer to receive all input data and pass the information along to
        // the current class and the stages.
        im = new InputMultiplexer(this, uiStage, mainStage);
        Gdx.input.setInputProcessor( im );

        uiTable = new Table(); // Create new Table object.
        uiTable.setFillParent(true); // The method will set the size of the Table to that of the stage.
        uiStage.addActor(uiTable); // Attach Table to the stage.

    }

}