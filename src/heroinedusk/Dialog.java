package heroinedusk;

// Java imports.
import java.util.ArrayList;

public class Dialog 
{
    
    /*
    The class stores information and logic related to the active dialog window.
    
    Inner classes include:
    
    Option:  Stores option related information (button and two messages).
    
    Methods include:

    addOption:  Adds an option to the dialog.
    countButtons:  Stores and returns a count of the number of buttons.
    finalize_option_nav_list:  Finishes setting up the option button navigation list.
    moveDown:  Moves down an item in the dialog menu.
    moveUp:  Moves up an item in the dialog menu.
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
    private int optionNavCount; // Number of items in option navigation list.  Base 0.
    private int optionNavCounter; // Used to increment option buttons when calling the addOption method.
    private int optionNavItem; // Currently selected item in option navigation list.  Base 0.
    private ArrayList<Integer> optionNavList; // List of option button numbers.  Generally -- 1, 2 or 1, 2, 3.
    private Option[] options; // Dialog button information.
    
    public Dialog()
    {
        
        // The constructor sets defaults and initializes arrays.
        
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
        
        // Add option to master list.
        options[optionCount] = new Option(button, optionCount, msg1, msg2);
        
        // If showing a dialog button, then...
        if (button != HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_NONE)
        {
            
            // Showing a dialog button.
            
            // Add option to navigation list.
            optionNavList.set(optionNavCounter, optionNavCounter);
            
        }
        
        // Increment option button counter.
        optionNavCounter++;
        
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
        
        // Add option to master list.
        options[optionNbr] = new Option(button, optionNbr, msg1, msg2);
        
        // If showing a dialog button, then...
        if (button != HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_NONE)
        {
            
            // Showing a dialog button.
            
            // Add option to navigation list.
            optionNavList.set(optionNbr, optionNbr);
            
        }
        
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
    
    public void finalize_option_nav_list()
    {
        
        // The function finishes setting up the option button navigation list.
        
        int itemCount; // Number of items in navigation button list.
        ArrayList<Integer> removeList; // Navigation button entries to remove.
        
        // Initialize array list.
        removeList = new ArrayList<>();
        
        // Count number of items in navigation button list, base 0.
        itemCount = optionNavList.size() - 1;
        
        // Loop through and remove unnecessary items.
        for (int counter = 0; counter <= itemCount; counter++)
        {
            
            // If order does not match value, then...
            if (counter != optionNavList.get(counter))
            {
                // Order does not match value.
                
                // Add to remove list.
                removeList.add(counter);
                
            }
            
        } // End ... Loop through and remove unnecessary items.
        
        // Loop through and remove marked items.
        removeList.forEach((item) -> {
            
            // Remove item.
            optionNavList.remove((int)item);
            
        });
        
        // Store number of items in list, base 0.
        optionNavCount = optionNavList.size() - 1;
        
        // By default, select last item -- base 0.
        optionNavItem = optionNavCount;
        
    }
    
    public int moveDown()
    {
        
        // The function moves down an item in the dialog menu.
        
        // Move to previous item in option navigation list.
        optionNavItem++;
        
        // If position outside of bounds, then...
        if (optionNavItem > optionNavCount)
        {
            
            // Position outside of bounds.
            
            // Move to first item.
            optionNavItem = 0;
            
        }
        
        // Update selector position.
        select_pos = optionNavList.get(optionNavItem);
        
        // Return new position.
        return select_pos;
        
    }
    
    public int moveUp()
    {
        
        // The function moves up an item in the dialog menu.
        
        /*
        System.out.println("Option count: " + optionCount);
        System.out.println("Button count: " + buttonCount);
        System.out.println("Nav List: " + optionNavList);
        System.out.println("Option nav item (before): " + optionNavItem);
        */

        // Move to previous item in option navigation list.
        optionNavItem--;
        
        // If position outside of bounds, then...
        if (optionNavItem < 0)
        {
            
            // Position outside of bounds.
            
            // Move to last item.
            optionNavItem = optionNavCount;
            
        }
        
        // Update selector position.
        select_pos = optionNavList.get(optionNavItem);
        
        // Return new position.
        return select_pos;
        
    }
    
    public final void resetDialog()
    {
        
        // The function resets most aspects of the dialog.
        
        // Set defaults.
        buttonCount = -1; // Not determined yet.
        optionCount = 0;
        optionNavCount = 0;
        optionNavCounter = 0;
        select_pos = 2; // Exit or similar button.
        fadeMessage = false;
        message = "";
        title = "";
        
        // Initialize arrays and array lists.
        options = new Option[3];
        optionNavList = new ArrayList<>();
        
        // Most shops should use the exit button as the third option.
        options[2] = new Option(HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_EXIT, 2, "Exit", "");
        
        // Add three option buttons to the list, each pointing to the third button.
        // Default involves only having one button, the third.
        // Reset properties of the other buttons when adding them.
        optionNavList.add(2); // Point to third button.
        optionNavList.add(2); // Point to third button.
        optionNavList.add(2); // Point to third button.
        optionNavCount = 2; // Base 0 count ... Three buttons ... Same as Two.
        optionNavItem = 2; // Last item, using base 0.
        
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
