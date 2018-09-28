package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Intersector;
import java.util.ArrayList;

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
public class BaseActor extends Group // Extends the Group class from LibGDX.
{

    /* The class extends the basic functionality of an Actor class in LibGDX.

    Methods include:

    act:  Overrides the parent / super class act method.  Calls the act method of the
          Actor (parent / super) class.  Updates Actor position based on time.
    cloneActor:  Returns a BaseActor with the same properties as the current.
    copy:  Copies properties from the passed to the current BaseActor.
    destroy:  Removes the BaseActor from its Stage and parent list (as necessary).
    draw:  Sets the tinting color of and draws the Actor.
    getBoundingPolygon:  Sets the position and rotation of the bounding polygon to that of the Actor.
    getBoundingRectangle:  Sets the properties of the bounding rectangle related to the texture region.
    getTintColor:  Gets the tint color of the Actor.
    moveToOrigin:  Centers a small within a larger rectangle, using the borders of the current and target BaseActor objects.
    overlaps:  Determines whether the bounding polygon for the passed Actor intersects (significantly)
               with that of the current.  Moves current Actor minimum amount to avoid intersection.
    setActorName:  Sets the Actor name to the passed value.
    setAdditionalDetails:  Performs additional operations for the constructor that would cause
                           overridable method call errors.
    setEllipseBoundary:  Sets the properties of the bounding polygon related to the texture region.
                         The polygon uses an ellipse with the dimensions of the texture region as
                         the drivers for the width and height.
    setOriginCenter:  Sets the origin of BaseActor to center of associated image, in order for rotations to appear correctly.
    setParentList:  Sets reference to an ArrayList to which the Actor has been added.
    setRandomTintColor:  Sets the tint color of the Actor to a random color.
    setRectangleBoundary:  Sets the properties of the bounding polygon related to the texture region.
                           The polygon uses a rectangle with the dimensions of the texture region as
                           the bounding polygon shape.
    setTexture:  Sets the properties of a texture.
    setTintColor:  Sets the tint color of the Actor.
    setTintColorToDefault:  Sets the tint color of the Actor to the default.
    */

    @SuppressWarnings({"FieldCanBeLocal"})
    private String actorName; // Name of actor.

    private ArrayList<? extends BaseActor> parentList; // Stores a reference to an ArrayList to which the Actor has been added.
    TextureRegion region; // Stores image (similar to a buffer from Direct-X).  Includes more
    // functionality than a Texture.  Supports storage of multiple images or animation frames.
    // Stores coordinates (u, v), that determine which rectangular subarea of the Texture to use.
    private Polygon boundingPolygon; // Encapsulates a 2D polygon defined by its vertices.
    // A polygon can be translated and rotated.
    @SuppressWarnings("FieldMayBeFinal")
    private Rectangle boundingRectangle; // Encapsulates a 2D rectangle defined by its corner point in the
    // bottom left and its extents in x (width) and y (height).  The object will contain the X and Y
    // coordinates and height and width of the texture region.
    private Color tintColor; // Color to tint the Actor.
    private final ColorWorks colorEngine; // Contains color related functionality.

    public BaseActor()
    {

        /*
        The constructor of the class calls the constructor of the parent (Actor),
        creates texture region and polygon objects, initializes the color engine,
        and sets the tint color to the default.

        Java notes:

        1.  A constructor does not have a return type.
        2.  The name of the constructor must be the same as the name of the class.
        3.  Unlike methods, constructors are not considered members of a class.
        4.  A constructor is called automatically when a new instance of an object is created.
        */

        super(); // Call the constructor for the Actor (parent / super) class.
        region = new TextureRegion(); // Create a TextureRegion object.
        boundingPolygon = null; // Initialize an empty bounding polygon.
        boundingRectangle = new Rectangle(); // Create a 2D rectangle to reflect position and borders of image.
        //noinspection MoveFieldAssignmentToInitializer
        parentList = null; // Initialize an empty list of references to ArrayList containing Actor.

        // Initialize color engine object.
        colorEngine = new ColorWorks();

        // Set additional defaults.
        setAdditionalDefaults();

    }

    // actorName = Name of actor.
    // filename = Full path to file containing image to load.
    // x = X-coordinate at which to place lower left corner of the actor.
    // y = Y-coordinate at which to place lower left corner of the actor.
    public BaseActor(String actorName, String filename, float x, float y)
    {
        
        // The constructor creates a base actor, loading the image from a file, based on the passed properties.
        
        // Call basic logic related to a new base actor.
        this();
        
        // Name actor.
        this.actorName = actorName;
        
        // Assign texture to actor.
        setTexture( new Texture(Gdx.files.internal(filename)) );
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        setPosition( x, y );
        
    }
    
    // actorName = Name of actor.
    // texture = Texture to assign to actor.
    // x = X-coordinate at which to place lower left corner of the actor.
    // y = Y-coordinate at which to place lower left corner of the actor.
    public BaseActor(String actorName, Texture texture, float x, float y)
    {
        
        // The constructor creates a base actor using a texture based on the passed properties.
        
        // Call basic logic related to a new base actor.
        this();
        
        // Name actor.
        this.actorName = actorName;
        
        // Assign texture to actor.
        setTexture( texture );
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        setPosition( x, y );
        
    }
    
    // actorName = Name of actor.
    // textureRegion = Texture region to assign to actor.
    // x = X-coordinate at which to place lower left corner of the actor.
    // y = Y-coordinate at which to place lower left corner of the actor.
    public BaseActor(String actorName, TextureRegion textureRegion, float x, float y)
    {
        
        // The constructor creates a base actor using a texture based on the passed properties.
        
        // Call basic logic related to a new base actor.
        this();
        
        // Name actor.
        this.actorName = actorName;
        
        // Assign texture region to actor.
        setTextureRegion( textureRegion );
        
        // Position the background with its lower left corner at the corresponding location in the screen.
        setPosition( x, y );
        
    }
    
    private void setAdditionalDefaults()
    {

        // The function performs additional operations for the constructor that would cause
        // overridable method call errors.

        // Set the Color used to tint the Actor to the default.
        setTintColorToDefault();
    }

    // pl = Reference to an ArrayList to which the Actor has been added.
    public void setParentList(ArrayList<? extends BaseActor> pl)
    {
        // Set reference to an ArrayList to which the Actor has been added.
        parentList = pl;
    }

    // x = X-coordinate at which to place lower left corner of the actor.
    // y = Y-coordinate at which to place lower left corner of the actor.
    @Override
    public final void setPosition(float x, float y)
    {
        // The method sets the position of the lower left corner of the actor.
        // The method exists to facilitate its use in the constructor.
        
        // Call method in superclass.
        super.setPosition(x, y);
    }
    
    public void destroy()
    {

        // The function removes the BaseActor from its Stage and parent list (as necessary).

        // Remove Actor from Stage.
        remove();

        // If parent list exists, then...
        if (parentList != null)
            // Parent list exists.
            // Remove current BaseActor from parent list.
            parentList.remove(this);

    }

    public void setOriginCenter()
    {

        // The function sets the origin of the BaseActor to the center of its associated image, in order for rotations to
        // appear correctly.

        // If width of BaseActor set (automatic when applying image), then...
        if ( getWidth() > 0 )

            // Width of BaseActor set.

            // Set origin of the BaseActor to the center of its associated image.
            setOrigin( getWidth() / 2, getHeight() / 2 );

        else

            // Width of BaseActor NOT set.

            // Display warning message.
            System.err.println("error: actor size not set");

    }

    // target = Target BaseActor to center within the current -- based on rectangular regions.
    public void moveToOrigin(BaseActor target)
    {

        // The function centers a smaller (the target) within a larger (the current) rectangle,
        // using the borders of the current and target BaseActor objects.
        // Origin = Position used for rotating and scaling operations.
        // Position... getX() = X position of the actor's left edge, getY() = Returns the Y position of the actor's bottom edge.

        // Set position of target BaseActor to center within current.
        this.setPosition(
                target.getX() + target.getOriginX() - this.getOriginX(),
                target.getY() + target.getOriginY() - this.getOriginY() );

    }

    // t = Texture (stores a single image).
    public final void setTexture(Texture t)
    {

        // The function sets the properties of a texture.

        int h; // Height of the passed texture.
        int w; // Width of the passed texture.

        // Set texture properties.
        w = t.getWidth(); // Get the width of the passed texture.
        h = t.getHeight(); // Get the height of the passed texture.
        setWidth( w ); // Set the width of the texture region to that of the passed texture.
        setHeight( h ); // Set the height of the texture region to that of the passed texture.
        region.setRegion( t ); // Set the texture region and coordinates to the size of the specified texture.

        // Set default color the Actor will be tinted when drawn.
        tintColor = getColor();

    }

    // t = TextureRegion (stores a single image).
    public final void setTextureRegion(TextureRegion t)
    {

        // The function sets the properties of a texture.

        int h; // Height of the passed texture region.
        int w; // Width of the passed texture region.

        // Set texture properties.
        w = t.getRegionWidth(); // Get the width of the passed texture.
        h = t.getRegionHeight(); // Get the height of the passed texture.
        setWidth( w ); // Set the width of the texture region to that of the passed texture.
        setHeight( h ); // Set the height of the texture region to that of the passed texture.
        region.setRegion( t ); // Set the texture region and coordinates to the size of the specified texture.

        // Set default color the Actor will be tinted when drawn.
        tintColor = getColor();

    }
    
    public void setRectangleBoundary()
    {

        // The function sets the properties of the bounding polygon related to the texture region.
        // The polygon uses a rectangle with the dimensions of the texture region as the bounding
        // polygon shape.

        float h; // Height of the Actor.
        float w; // Width of the actor.

        // Set the properties of the bounding rectangle related to the texture region.
        // Use the properties of the texture region.

        // Store Actor dimensions.
        w = getWidth(); // Store width of the Actor.
        h = getHeight(); // Store height of the Actor.

        // Set up vertices of the Actor (rectangle / polygon).
        float [] vertices = {0,0, w,0, w,h, 0,h}; // Array of vertices related to the bounding polygon.

        // Set the properties of the bounding polygon (rectangular shape) related to the texture region.
        boundingPolygon = new Polygon(vertices);

        // Set the position of the top left corner of the polygon to that of the texture region.
        boundingPolygon.setOrigin( getOriginX(), getOriginY() );

    }

    public void setEllipseBoundary()
    {

        // The function sets the properties of the bounding polygon related to the texture region.
        // The polygon uses an ellipse with the dimensions of the texture region as the drivers
        // for the width and height.

        // The method contains a loop to generate a set of n equally spaced values for t in the
        // interval [0, 6.28], then calculates the corresponding x and y coordinates, and stores
        // them in an array used to initialize the polygon.  Changing the method to use a value of
        // 4 for n would generate a diamond shape.  Using a value of 8 for n generates an octagon
        // shape.  As the method uses larger values for n, the generated shape gets smoother.

        float h; // Height of the Actor.
        float t; // Values ranging from 0 to 6.28 (pi x 2), used to generate vertices in ellipse.
        float[] vertices; // Array of vertices related to the bounding polygon.
        float w; // Width of the Actor.
        final int n; // Number of vertices.

        n = 12; // Set number of vertices.
        w = getWidth(); // Store width of the Actor.
        h = getHeight(); // Store height of the Actor.
        vertices = new float[n * 2]; // Initialize the array of vertices.

        // Loop through and set the vertices of the bounding polygon.
        for (int i = 0; i < n; i++)
        {

            // Generate ellipse, letting x = cos(t) and y = sin(t),
            // where t ranges from 0 to pi x 2 (about 6.28).
            // x = w/2 * cos(t) + w/2
            // y = h/2 * sin(t) + h/2

            // Store next driver value for ellipse.
            t = 6.28f * i / n;

            // Generate X-coordinate for current vertex in ellipse.
            vertices[i * 2] = w / 2 * MathUtils.cos(t) + w / 2;

            // Generate Y-coordinate for current vertex in ellipse.
            vertices[i * 2 + 1] = h / 2 * MathUtils.sin(t) + h / 2;

        }

        // Set the properties of the bounding polygon (shape of an ellipse) related to the texture region.
        boundingPolygon = new Polygon(vertices);

        // Set the position of the top left corner of the polygon to that of the texture region.
        boundingPolygon.setOrigin( getOriginX(), getOriginY() );

    }

    public Polygon getBoundingPolygon()
    {

        // The function sets the position and rotation of the bounding polygon to that of the Actor.
        // The function returns the bounding polygon.

        // Set the position (x and y coordinates of the bottom left corner) of the bounding polygon to
        // that of the Actor.
        boundingPolygon.setPosition( getX(), getY() );

        // Set rotation of the bounding polygon to that of the Actor.
        boundingPolygon.setRotation( getRotation() );

        // Return the bounding polygon.
        return boundingPolygon;

    }

    public Rectangle getBoundingRectangle()
    {

        // The function sets the properties of the bounding rectangle related to the texture region.
        // The function returns the bounding rectangle.

        // Set the properties of the bounding rectangle related to the texture region.
        // Use the properties of the texture region.
        boundingRectangle.set( getX(), getY(), getWidth(), getHeight() );

        // Return the bounding rectangle.
        return boundingRectangle;

    }

    // other = Other Actor to check for collision detection.
    // resolve = Whether to move the other Actor along the minimum translation vector to prevent overlap.
    public boolean overlaps(BaseActor other, @SuppressWarnings("SameParameterValue") boolean resolve)
    {

        // The function determines whether the bounding polygon for the passed Actor intersects (significantly)
        // with that of the current.  Moves current Actor minimum amount to avoid intersection.
        // The function returns whether a significant overlap occurs.

        boolean polyOverlap; // Whether Actors overlap.
        final float significant; // Minimum significant penetration depth -- length of the minimum translation
        // vector (MTV), which is the smallest vector along which an intersecting shape can get moved to
        // be separate from the other shape.

        Intersector.MinimumTranslationVector mtv; // Minimum magnitude vector required to push the
        // polygon defined by verts1 out of the collision with the polygon defined by verts2.
        Polygon poly1; // Reference to bounding polygon for current Actor.
        Polygon poly2; // Reference to bounding polygon for passed Actor.

        // Set defaults.
        significant = 0.5f;

        // Store bounding polygons for current and passed Actors.
        poly1 = this.getBoundingPolygon();
        poly2 = other.getBoundingPolygon();

        // If polygons are intersecting, then...
        if (poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))

        {
            /*
            Polygons are intersecting.
            Checks whether specified counter-clockwise wound convex polygons overlap.
            If polygons overlap, obtains a Minimum Translation Vector indicating the
            minimum magnitude vector required to push the polygon p1 out of collision
            with polygon p2.
            Minimum Translation Vector indicates the minimum magnitude vector required
            to push the polygon defined by verts1 out of the collision with the polygon
            defined by verts2.
            */

            // Instantiate a minimum transaction vector object.
            mtv = new Intersector.MinimumTranslationVector();

            // Obtain a minimum translation vector indicating the minimum magnitude vector
            // required to push the current Actor (polygon) out of the collision with the
            // other.
            polyOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

            // If polygons intersect and resolve parameter flagged, then...
            if (polyOverlap && resolve)

            {
                // Polygons intersect and resolve parameter flagged.

                // Moves current Actor (adds X and Y to current position) based on minimum
                // translation vector -- to resolve and avoid intersection with the minimum
                // necessary movement.
                this.moveBy( mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth );
            }

            // Compare penetration depth to minimum significant value.
            // The penetration depth is the length of the minimum translation vector (MTV), which is the
            // smallest vector along which an intersecting shape can get moved to be separated from the
            // other shape.

            // If polygon intersection more than minimum significant value, then return polygons as
            // intersecting.  Otherwise, return polygons as not intersecting.
            polyOverlap = (polyOverlap && (mtv.depth > significant));
        }

        else

        {
            // Polygons are not intersecting.
            polyOverlap = false;
        }

        // Return whether polygons are intersecting.
        return polyOverlap;

    }

    // dt = Time in seconds since the last frame.  Also called delta.
    @Override
    public void act(float dt)
    {

        // The function calls the act method of the Actor, which performs
        // a time based positional update.

        // Calls the act method of the Actor (parent / super) class.
        // Updates the actor based on time.
        super.act ( dt );

    }

    // A Batch is used to draw 2D rectangles that reference a texture (region).
    // The class will batch the drawing commands and optimize them for processing by the GPU.

    // batch = Sprite used to draw image, dependent on and referencing a texture (buffer).
    // parentAlpha = Alpha / transparency to use when drawing image.
    @Override
    public void draw(Batch batch, float parentAlpha)
    {

        // The function sets the tinting color of and draws the Actor (when visible).

        // System.out.println("\nActor:  " + actorName);

        // Set the color used to tint images when they are added to the Batch.
        // Default:  Color.WHITE.

        // Set the Color values (red, green, blue, and alpha / transparency) of the
        // Batch object to equal those of the tintColor class variable.
        batch.setColor(tintColor.r, tintColor.g, tintColor.b, tintColor.a);

        // If the Actor is visible, then...
        if ( isVisible() )

            // Actor is visible.
            // Draw the texture, taking into account its position,
            // origin (center of rotation), width and height, scaling factors, and
            // rotation angle.
            batch.draw( region, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );

        // Call the constructor of the parent class to redraw the group.
        super.draw(batch, parentAlpha);

    }
    
    // actorName = Name to give Actor.
    public void setActorName(String actorName)
    {
        // The function sets the Actor name to the passed value.
        this.actorName = actorName;
    }

    // Color related methods...

    public Color getTintColor()
    {
        // The function returns the Color used to tint the Actor.
        return tintColor;
    }

    // tintColor = Color to tint the Actor.
    public void setTintColor(Color tintColor)
    {
        // The function sets the Color used to tint the Actor.
        this.tintColor = tintColor;
    }

    // red = Red portion of Color to tint the Actor.
    // green = Green portion of Color to tint the Actor.
    // blue = Blue portion of Color to tint the Actor.
    // alpha = Alpha portion of Color to tint the Actor.
    public void setTintColor(float red, float green, float blue, float alpha)
    {
        // The function sets the Color used to tint the Actor used the passed values for
        // red, green, blue, and alpha.
        this.tintColor = new com.badlogic.gdx.graphics.Color(red, green, blue, alpha);
    }

    @SuppressWarnings("WeakerAccess")
    public void setTintColorToDefault()
    {
        // The function sets the Color used to tint the Actor to the default.
        this.tintColor = getColor();
    }

    public void setRandomTintColor()
    {
        // The function sets a random tint Color for the Actor.
        this.tintColor = colorEngine.getRandomColor();
    }

    // Copy related methods...

    // original = Actor from which to copy properties.
    void copy(BaseActor original)
    {

        // The function copies properties from the passed to the current BaseActor.
        // Properties include:  image texture (buffer), bounding polygon, position, origin, width,
        // height, tint color, and visibility status.

        // If image texture (buffer) exists in passed Actor, then...
        if (original.region.getTexture() != null )

            // Image texture (buffer) exists in passed Actor.

            // Copy image texture (buffer) from passed to current Actor.
            this.region = new TextureRegion( original.region );

        // If bounding polygon exists in passed Actor, then...
        if (original.boundingPolygon != null)
        {
            // Bounding polygon exists in passed Actor.

            // Create bounding polygon in current with vertices in bounding polygon of passed Actor.
            this.boundingPolygon = new Polygon( original.boundingPolygon.getVertices() );

            // Set origin of bounding polygon to that of passed Actor.
            this.boundingPolygon.setOrigin( original.getOriginX(), original.getOriginY() );
        }

        // Set position of current to that of passed Actor (based on bottom left corner).
        this.setPosition( original.getX(), original.getY() );

        // Copy properties indicating top left corner of passed to current Actor.
        this.setOriginX( original.getOriginX() );
        this.setOriginY( original.getOriginY() );

        // Copy width and height from passed to current Actor.
        this.setWidth( original.getWidth() );
        this.setHeight( original.getHeight() );

        // Copy visibility flag from passed to current Actor.
        this.setVisible( original.isVisible() );

        // Copy tinting color from passed to current Actor.
        //this.tintColor = new Color();
        //this.tintColor.r = original.getColor().r;
        //this.tintColor.g = original.getColor().g;
        //this.tintColor.b = original.getColor().b;
        //this.tintColor.a = original.getColor().a;
        this.setTintColor( original.getTintColor() );

    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntCallSuperClone", "CloneDeclaresCloneNotSupported"})
    @Override
    public BaseActor clone()
    {

        // The function returns a BaseActor with the same properties as the current.

        BaseActor newbie; // BaseActor to which to copy properties.

        // Instantiate new BaseActor object.
        newbie = new BaseActor();

        // Copy properties of current (class-level) to new BaseActor object.
        newbie.copy( this );

        // Return the new BaseActor object.
        return newbie;

    }

}