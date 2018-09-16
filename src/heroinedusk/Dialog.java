package heroinedusk;

public class Dialog 
{
    
    /*
    The class stores information and logic related to the active dialog window.
    
    Inner classes include:
    
    Option:  Store option related information (button and two messages).
    
    Methods include:

    addOption:  Adds an option to the dialog.
    */
    
    // Declare regular variables.
    protected boolean items_for_sale; // Whether dialog related to items being offered for sale.
    protected String message; // Dialog message.
    protected HeroineEnum.ShopEnum shop_id; // Identifier of current shop / location.  
      // Only meaningful when in a shop / location.  Matches to a ShopEnum value.
    protected String title; // Name of current shop / location.
    private int optionCount; // Number of options.
    protected Option[] options; // Dialog button information.
    
    public Dialog()
    {
        
        // The constructor sets defaults and initializes arrays.
        
        // Set defaults.
        optionCount = 0;
        message = "";
        title = "";
        
        // Initialize arrays.
        options = new Option[3]; 
        
        // Most shops should use the exit button as the third option.
        options[2] = new Option(HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_EXIT, "Exit", "");
        
    }
    
    // Inner classes below...
    
    public class Option
    {
        
        // The inner class stores dialog button information.
        
        // Declare regular variables.
        protected HeroineEnum.DialogButtonEnum button; // Identifier of button to display.
        protected String msg1; // First line of text to display near button.
        protected String msg2; // Second line of text to display near button.

        // button = Identifier of button to display.
        // msg1 = First line of text to display.
        // msg2 = Second line of text to display.
        public Option(HeroineEnum.DialogButtonEnum button, String msg1, String msg2)
        {
        
            // The constructor populates the class-level variables.
            
            // Set class-level variable values.
            this.button = button;
            this.msg1 = msg1;
            this.msg2 = msg2;
            
        }
        
    }
    
    // Methods below...
    
    // button = Identifier of button to display.
    // msg1 = First line of text to display.
    // msg2 = Second line of text to display.
    public void addOption(HeroineEnum.DialogButtonEnum button, String msg1, String msg2)
    {
        
        // The function adds an option to the dialog.
        
        // Add option.
        options[optionCount] = new Option(button, msg1, msg2);
        
        System.out.println("Adding option - " + optionCount + ": button: " + button + ", msg1: " + msg1 + 
          ", msg2: " + msg2);
        
        // Increment option count.
        optionCount++;
        
    }
    
    // button = Identifier of button to display.
    // msg1 = First line of text to display.
    // msg2 = Second line of text to display.
    // optionNbr = Index (base 0) of option for which to add / update information.
    public void addOption(HeroineEnum.DialogButtonEnum button, String msg1, String msg2, int optionNbr)
    {
        
        // The function adds an option to the dialog.
        
        // Add option.
        options[optionNbr] = new Option(button, msg1, msg2);
        
    }
    
}
