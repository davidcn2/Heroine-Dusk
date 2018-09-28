package heroinedusk;

public class Dialog 
{
    
    /*
    The class stores information and logic related to the active dialog window.
    
    Inner classes include:
    
    Option:  Stores option related information (button and two messages).
    
    Methods include:

    addOption:  Adds an option to the dialog.
    countButtons:  Stores and returns a count of the number of buttons.
    resetDialog:  Resets most aspects of the dialog.
    */
    
    // Declare regular variables.
    protected int buttonCount; // Number of buttons of type Buy and Exit.
    private boolean fadeMessage; // Whether to fade next display of message.
    private boolean items_for_sale; // Whether dialog related to items being offered for sale.
    private String message; // Dialog message.
    private int select_pos; // Selected button -- base 0.  Examples:  0, 1, 2.
    protected Shop shop; // Reference to current shop.
    private HeroineEnum.ShopEnum shop_id; // Identifier of current shop / location.  
      // Only meaningful when in a shop / location.  Matches to a ShopEnum value.
    private String title; // Name of current shop / location.
    private int optionCount; // Number of options.
    private Option[] options; // Dialog button information.
    
    public Dialog()
    {
        
        // The constructor sets defaults and initializes arrays.
        
        // Set defaults.
        fadeMessage = false;
        message = "";
        title = "";
        
        // Reset dialog.
        resetDialog();
        
    }
    
    // Inner classes below...
    
    public class Option
    {
        
        // The inner class stores dialog button information.
        
        // Declare regular variables.
        public HeroineEnum.DialogButtonEnum button; // Identifier of button to display.
        public int buttonNbr; // Button number, base 0.
        public String msg1; // First line of text to display near button.
        public String msg2; // Second line of text to display near button.

        // button = Identifier of button to display.
        // buttonNbr = Button number, base 0.
        // msg1 = First line of text to display.
        // msg2 = Second line of text to display.
        public Option(HeroineEnum.DialogButtonEnum button, int buttonNbr, String msg1, String msg2)
        {
        
            // The constructor populates the class-level variables.
            
            // Set class-level variable values.
            this.button = button;
            this.buttonNbr = buttonNbr;
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
        options[optionCount] = new Option(button, optionCount, msg1, msg2);
        
        //System.out.println("Adding option - " + optionCount + ": button: " + button + ", msg1: " + msg1 + 
        //  ", msg2: " + msg2);
        
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
        options[optionNbr] = new Option(button, optionNbr, msg1, msg2);
        
    }
    
    public int countButtons()
    {
        
        // The function stores and returns a count of the number of buttons.
        // Buy and exit count as buttons, none does not count.
        // References DialogButtonEnum.
        
        // Reset button count.
        buttonCount = 0;
        
        // Loop through options.
        for (Option option : options)
        
        {
            
            // If button for option either buy or exit, then...
            if (option.button == HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY ||
                option.button == HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_EXIT)
                
            {
                
                // Button either buy or exit.
                // Increment button count.
                buttonCount++;
                
            }
            
        }
        
        // Return count of buttons.
        return buttonCount;
        
    }
    
    public final void resetDialog()
    {
        
        // The function resets most aspects of the dialog.
        
        // Set defaults.
        buttonCount = -1; // Not determined yet.
        optionCount = 0;
        select_pos = 2; // Exit or similar button.
        
        // Initialize arrays.
        options = new Option[3];
        
        // Most shops should use the exit button as the third option.
        options[2] = new Option(HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_EXIT, 2, "Exit", "");
        
    }
    
    // Getters and setters below...
    
    public boolean isFadeMessage() {
        return fadeMessage;
    }
    
    public void setFadeMessage(boolean fadeMessage) {
        this.fadeMessage = fadeMessage;
    }
    
    public boolean isItems_for_sale() {
        return items_for_sale;
    }
    
    public void setItems_for_sale(boolean items_for_sale) {
        this.items_for_sale = items_for_sale;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Option[] getOptions() {
        return options;
    }
    
    public int getSelect_pos() {
        return select_pos;
    }
    
    public void setSelect_pos(int select_pos) {
        this.select_pos = select_pos;
    }
    
    public HeroineEnum.ShopEnum getShop_id() {
        return shop_id;
    }
    
    public void setShop_id(HeroineEnum.ShopEnum shop_id) {
        this.shop_id = shop_id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
}
