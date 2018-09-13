package heroinedusk;

public class HeroineEnum 
{
    
    /*
    The file consolidates enumerations used in multiple classes.
    Enumerations represent special Java types used to define collections of constants.
    More precisely, a Java enum type acts as a special kind of Java class. An enum can 
    contain constants, methods, ...
    
    Enumerations include:
    
    1.  GameState:  Enumerations related to state of the game (explore, combat, information, dialog, title).
    2.  ShopEnum:  Enumerations related to shops, locations, and dialogues.
    */
    
    // Enumerations related to state of the game (explore, combat, information, dialog, title).
    public enum GameState 
    {
        
        STATE_EXPLORE (0),
        STATE_COMBAT (1),
        STATE_INFO (2),
        STATE_DIALOG (3), // Displayed upon clicking the Start button (in the main menu) or visiting a shop.
        STATE_TITLE (4) // Game starts in title state.  Displays main menu.
        ; // semicolon needed when fields / methods follow

        private final int gameState; // Game state -- one of the enumerated values.

        // gameState = Value to associate.
        private GameState(int gameState)
        {
            // The constructor sets the numeric values for each enumeration.
            this.gameState = gameState;
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.GameState.STATE_COMBAT.getValue();
            
            // Return the numeric value for the enumeration.
            return gameState;
        }
        
    }
    
    // Enumerations related to shops, locations, and dialogues.
    public enum ShopEnum 
    {
        
        SHOP_WEAPON (0), // Type for weapon shops.
        SHOP_ARMOR (1), // Type for armor shops.
        SHOP_SPELL (2), // Type for spell shops.
        SHOP_ROOM (3), // Type for other rooms / locations / buildings.
        SHOP_MESSAGE (4) // Type for messages / text.
        ; // semicolon needed when fields / methods follow

        private final int shopEnum; // Enumerations related to shops, locations, and dialogues.

        // shopEnum = Value to associate.
        private ShopEnum(int shopEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.shopEnum = shopEnum;
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.ShopEnum.SHOP_ROOM.getValue();
            
            // Return the numeric value for the enumeration.
            return shopEnum;
        }
        
    }
    
}