package heroinedusk;

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

public class ActionResult
{
    
    /*
    The class stores information used for multi-variable results from an action-related function.
    
    Methods include:
    
    clear:  Clears the class-level variables.
    */
    
    // Declare regular variables.
    private boolean opponentHurt; // Whether opponent hurt / damaged.  Defaults to not hurt (false).
      // Damage only occurs in combat.
    private Boolean result; // General result.
    private Integer resultNumeric; // Numeric result.  For spells, -1 = Failure, 1 = success (against an
      // object), or 0 to many for an amount (usually damage).
    private String textAction; // Action-related text.  Examples:  ATTACK!, BONE SHIELD!, BURN!, CRITICAL!, 
      // HEAL!, HP DRAIN!, MP DRAIN!, RUN!, SCORCH!, UNLOCK!, ...
    private String textResult; // Result-related text.  Examples:  n DAMAGE, MISS!, +DEF UP!, BLOCKED!, 
      // -1 MP, NO EFFECT, ABSORBED, +n HP, ...
    private String textSource; // Source-related text.  Examples:  YOU:, ENEMY:, ...
    
    public ActionResult()
    {
        
        // The constructor allows for creating an action result with empty values for all class-level variables.
        
    }
    
    // resultNumeric = Numeric result.
    // textSource = Source-related text.
    // textAction = Action-related text.
    // textResult = Result-related text.
    // opponentHurt = Whether opponent hurt / damaged.
    public ActionResult(int resultNumeric, String textAction, String textResult, String textSource, 
      boolean opponentHurt)
    {
        
        // The constructor populates the class-level variables.
        
        // Populate class-level variables.
        this.resultNumeric = resultNumeric;
        this.textAction = textAction;
        this.textResult = textResult;
        this.textSource = textSource;
        this.opponentHurt = opponentHurt;
        
    }
    
    // result = General result.
    // textSource = Source-related text.
    // textAction = Action-related text.
    // textResult = Result-related text.
    // opponentHurt = Whether opponent hurt / damaged.
    public ActionResult(boolean result, String textAction, String textResult, String textSource,
      boolean opponentHurt)
    
    {
        
        // The constructor populates the class-level variables.
        
        // Populate class-level variables.
        this.result = result;
        this.textAction = textAction;
        this.textResult = textResult;
        this.textSource = textSource;
        this.opponentHurt = opponentHurt;
        
    }
    
    // result = General result.
    // resultNumeric = Numeric result.
    // textSource = Source-related text.
    // textAction = Action-related text.
    // textResult = Result-related text.
    // opponentHurt = Whether opponent hurt / damaged.
    public ActionResult(boolean result, int resultNumeric, String textAction, String textResult, 
      String textSource, boolean opponentHurt)
    {
        
        // The constructor populates the class-level variables.
        
        // Populate class-level variables.
        this.result = result;
        this.resultNumeric = resultNumeric;
        this.textAction = textAction;
        this.textResult = textResult;
        this.textSource = textSource;
        this.opponentHurt = opponentHurt;
        
    }
    
    public void clear()
    {
        
        // The function clears the class-level variables.
        
        // Clear class-level variables.
        this.result = null;
        this.resultNumeric = null;
        this.textAction = null;
        this.textResult = null;
        this.textSource = null;
        this.opponentHurt = false;
        
    }
    
    // Getters and setters below...

    public Boolean getOpponentHurt() {
        return opponentHurt;
    }

    public void setOpponentHurt(boolean opponentHurt) {
        this.opponentHurt = opponentHurt;
    }
    
    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getResultNumeric() {
        return resultNumeric;
    }

    public void setResultNumeric(int resultNumeric) {
        this.resultNumeric = resultNumeric;
    }

    public String getTextAction() {
        return textAction;
    }
    
    public String getTextAction_Nvl() {
        if (textAction == null)
            return "";
        else
            return textAction;
    }

    public void setTextAction(String textAction) {
        this.textAction = textAction;
    }
    
    public void setTextAction(HeroineEnum.PowerActionEnum powerAction) {
        this.textAction = powerAction.getValue_Text();
    }
    
    public String getTextResult() {
        return textResult;
    }

    public String getTextResult_Nvl() {
        if (textResult == null)
            return "";
        else
            return textResult;
    }
    
    public void setTextResult(String textResult) {
        this.textResult = textResult;
    }

    public void setTextResult(HeroineEnum.PowerResultEnum powerResult) {
        this.textResult = powerResult.getValue_Text();
    }
    
    public void setTextResult_Damage(int damage) {
        this.textResult = Integer.toString(damage) + " DAMAGE";
    }
    
    public String getTextSource() {
        return textSource;
    }

    public String getTextSource_Nvl() {
        if (textSource == null)
            return "";
        else
            return textSource;
    }
    
    public void setTextSource(String textSource) {
        this.textSource = textSource;
    }
    
}
