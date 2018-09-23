package heroinedusk;

import java.util.HashMap;
import java.util.Map;

public class HeroineEnum 
{
    
    /*
    The file consolidates enumerations used in multiple classes.
    Enumerations represent special Java types used to define collections of constants.
    More precisely, a Java enum type acts as a special kind of Java class. An enum can 
    contain constants, methods, ...
    
    Enumerations include:
    
    1.  ActionButtonEnum:  Enumerations related to action buttons.
    2.  ArmorEnum:  Enumerations related to armors.
    3.  DialogButtonEnum:  Enumerations related to dialog buttons.
    4.  EnemyCategoryEnum:  Enumerations related to enemy categories.
    5.  EnemyEnum:  Enumerations related to enemies.
    6.  FacingEnum:  Enumerations related to direction player is facing.
    7.  FontEnum:  Enumerations related to fonts.
    8.  GameState:  Enumerations related to state of the game (explore, combat, information, dialog, title).
    9.  ImgBackgroundEnum:  Enumerations related to background images.
    10.  ImgInterfaceEnum:  Enumerations related to interface images -- except fonts.
    11.  ListEnum:  Enumerations related to list types.
    12.  MusicEnum:  Enumerations related to music.
    13.  ShopEnum:  Enumerations related to shops / locations.
    14.  ShopTypeEnum:  Enumerations related to shop / location types.
    15.  SoundsEnum:  Enumerations related to sounds.
    16.  SpellEnum:  Enumerations related to spells.  Also used for spellbook.
    17:  WeaponEnum:  Enumerations related to weapons.
    */
    
    // Enumerations related to action buttons.
    public enum ActionButtonEnum 
    {
        
        ACTION_BUTTON_ATTACK (0, "action_buttons0"), // Attack button.
        ACTION_BUTTON_RUN (1, "action_buttons1"), // Run button.
        ACTION_BUTTON_HEAL (2, "action_buttons2"), // Heal button.
        ACTION_BUTTON_BURN (3, "action_buttons3"), // Burn button.
        ACTION_BUTTON_UNLOCK (4, "action_buttons4"), // Unlock button.
        ACTION_BUTTON_LIGHT (5, "action_buttons5"), // Light button.
        ACTION_BUTTON_FREEZE (6, "action_buttons6"), // Freeze button.
        ACTION_BUTTON_REFLECT (7, "action_buttons7") // Reflect button.
        ; // semicolon needed when fields / methods follow

        private final int actionButtonEnum; // Enumerations related to action buttons.
        private final String actionButtonKey; // Key associated with texture -- used with asset manager and textureRegions hash map.
        private static final Map actionButtonMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // actionButtonEnum = Value to associate.
        // actionButtonKey = Key associated with texture -- used with asset manager and textureRegions hash map.
        private ActionButtonEnum(int actionButtonEnum, String actionButtonKey) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.actionButtonEnum = actionButtonEnum;
            this.actionButtonKey = actionButtonKey;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (ActionButtonEnum actionButtonEnum : ActionButtonEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                actionButtonMap.put(actionButtonEnum.actionButtonEnum, actionButtonEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT.getValue();
            
            // Return the numeric value for the enumeration.
            return actionButtonEnum;
        }
        
        public String getValue_Key() 
        {
            // The function returns the key associated with the texture.
            // Example for use:  String x = HeroineEnum.ActionButtonEnum.ACTION_BUTTON_REFLECT.getValue_Key();
            
            // Return the key associated with the texture.
            return actionButtonKey;
        }
        
        // actionButton = Numeric value to convert to text.
        public static ActionButtonEnum valueOf(int actionButton) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (ActionButtonEnum) actionButtonMap.get(actionButton);
        }
        
    }
    
    // Enumerations related to armors.
    public enum ArmorEnum
    {
        
        NO_ARMOR (0, "No Armor", 0, 0), // No armor.
        ARMOR_SERF_RAGS (1, "Serf Rags", 2, 0), // Serf rags.
        ARMOR_TRAVEL_CLOAK (2, "Travel Cloak", 4, 50), // Travel cloak.
        ARMOR_HIDE_CUIRASS (3, "Hide Cuirass", 6, 200), // Hide cuirass.
        ARMOR_RIVET_LEATHER (4, "Rivet Leather", 8, 1000), // Rivet leather.
        ARMOR_CHAIN_MAILLE (5, "Chain Maille", 10, 5000), // Chain maille.
        ARMOR_PLATE_ARMOR (6, "Plate Armor", 12, 20000), // Plate armor.
        ARMOR_WYVERN_SCALE (7, "Wyvern Scale", 14, 100000) // Wyvern scale.
        ; // semicolon needed when fields / methods follow

        private final int def; // Defense value for the armor.
        private final int gold; // Value of the weapon, in gold.
        private final int armorEnum; // Enumerations related to armors.
        private static final Map armorMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        private final String armorText; // Clean text for the armor.
        
        // armorEnum = Value to associate.
        // armorText = Clean text to associate.
        // def = Defense value to associate.
        // gold = Weapon value, in gold, to associate.
        private ArmorEnum(int armorEnum, String armorText, int def, int gold) 
        {
            // The constructor sets the values for each enumeration.
            this.armorEnum = armorEnum;
            this.armorText = armorText;
            this.def = def;
            this.gold = gold;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (ArmorEnum armorEnum : ArmorEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                armorMap.put(armorEnum.armorEnum, armorEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.ArmorEnum.TRAVEL_CLOAK.getValue();
            
            // Return the numeric value for the enumeration.
            return armorEnum;
        }
        
        public int getValue_Def() 
        {
            // The function returns the defense value for the enumeration.
            // Example for use:  int x = HeroineEnum.ArmorEnum.TRAVEL_CLOAK.getValue_Def();
            
            // Return the gold value for the enumeration.
            return def;
        }
        
        public int getValue_Gold() 
        {
            // The function returns the gold value for the enumeration.
            // Example for use:  int x = HeroineEnum.ArmorEnum.TRAVEL_CLOAK.getValue_Gold();
            
            // Return the gold value for the enumeration.
            return gold;
        }
        
        public String getValue_CleanText() 
        {
            // The function returns the clean text value for the enumeration.
            // Example for use:  String x = HeroineEnum.ArmorEnum.TRAVEL_CLOAK.getValue_CleanText();
            
            // Return the clean text value for the enumeration.
            return armorText;
        }
        
        // armor = Numeric value to convert to text.
        public static ArmorEnum valueOf(int armor) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (ArmorEnum) armorMap.get(armor);
        }
        
    }
    
    // Enumerations related to dialog buttons.
    public enum DialogButtonEnum 
    {
        
        DIALOG_BUTTON_NONE (0, ""), // No button.
        DIALOG_BUTTON_BUY (1, "dialog_buttonO"), // Buy button.
        DIALOG_BUTTON_EXIT (2, "dialog_buttonX") // Exit button.
        ; // semicolon needed when fields / methods follow

        private final int dialogButtonEnum; // Enumerations related to dialog buttons.
        private final String dialogButtonKey; // Key associated with texture -- used with asset manager and textureRegions hash map.
        private static final Map dialogButtonMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // dialogButtonEnum = Value to associate.
        // dialogButtonKey = Key associated with texture -- used with asset manager and textureRegions hash map.
        private DialogButtonEnum(int dialogButtonEnum, String dialogButtonKey) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.dialogButtonEnum = dialogButtonEnum;
            this.dialogButtonKey = dialogButtonKey;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (DialogButtonEnum dialogButtonEnum : DialogButtonEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                dialogButtonMap.put(dialogButtonEnum.dialogButtonEnum, dialogButtonEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY.getValue();
            
            // Return the numeric value for the enumeration.
            return dialogButtonEnum;
        }
        
        public String getValue_Key() 
        {
            // The function returns the key associated with the texture.
            // Example for use:  String x = HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY.getValue_Key();
            
            // Return the key associated with the texture.
            return dialogButtonKey;
        }
        
        // dialogButton = Numeric value to convert to text.
        public static DialogButtonEnum valueOf(int dialogButton) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (DialogButtonEnum) dialogButtonMap.get(dialogButton);
        }
        
    }
    
    // Enumerations related to enemy categories.
    public enum EnemyCategoryEnum 
    {
        
        ENEMY_CATEGORY_SHADOW (0), // Shadow enemy types.
        ENEMY_CATEGORY_DEMON (1), // Demon enemy types.
        ENEMY_CATEGORY_UNDEAD (2), // Undead enemy types.
        ENEMY_CATEGORY_AUTOMATON (3) // Automaton enemy types.
        ; // semicolon needed when fields / methods follow

        private final int enemyCategoryEnum; // Enumerations related to enemy categories.
        private static final Map enemyCategoryMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // enemyCategoryEnum = Value to associate.
        private EnemyCategoryEnum(int enemyCategoryEnum) 
        {
            // The constructor sets the values for each enumeration.
            this.enemyCategoryEnum = enemyCategoryEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (EnemyCategoryEnum enemyCategoryEnum : EnemyCategoryEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                enemyCategoryMap.put(enemyCategoryEnum.enemyCategoryEnum, enemyCategoryEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = AtlasEnum.EnemyCategoryEnum.ENEMY_CATEGORY_DEMON.getValue();
            
            // Return the numeric value for the enumeration.
            return enemyCategoryEnum;
        }
        
        // enemyCategory = Numeric value to convert to text.
        public static EnemyCategoryEnum valueOf(int enemyCategory) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (EnemyCategoryEnum) enemyCategoryMap.get(enemyCategory);
        }
        
    }
    
    // Enumerations related to music.
    public enum EnemyEnum
    {
        ENEMY_SHADOW_TENDRILS (0, 2, 5, EnemyCategoryEnum.ENEMY_CATEGORY_SHADOW, 6, "Shadow Tendrils", 1, 
          2, "shadow_tendrils.png", new EnemyPowerEnum[]{EnemyPowerEnum.ENEMY_POWER_ATTACK}), // Shadow tendrils.
        ENEMY_IMP (1, 2, 6, EnemyCategoryEnum.ENEMY_CATEGORY_DEMON, 7, "Imp", 1, 3, "imp.png",
          new EnemyPowerEnum[]{EnemyPowerEnum.ENEMY_POWER_ATTACK, EnemyPowerEnum.ENEMY_POWER_ATTACK, 
              EnemyPowerEnum.ENEMY_POWER_SCORCH}), // Imp.
        ENEMY_SHADOW_SOUL (2, 3, 8, EnemyCategoryEnum.ENEMY_CATEGORY_SHADOW, 8, "Shadow Soul", 
          2, 4, "shadow_soul.png", new EnemyPowerEnum[]{EnemyPowerEnum.ENEMY_POWER_ATTACK, 
              EnemyPowerEnum.ENEMY_POWER_ATTACK, EnemyPowerEnum.ENEMY_POWER_MPDRAIN}), // Shadow soul
        ENEMY_ZOMBIE (3, 4, 10, EnemyCategoryEnum.ENEMY_CATEGORY_UNDEAD, 12, "Zombie", 
          3, 6, "zombie.png", new EnemyPowerEnum[]{EnemyPowerEnum.ENEMY_POWER_ATTACK, 
              EnemyPowerEnum.ENEMY_POWER_ATTACK, EnemyPowerEnum.ENEMY_POWER_HPDRAIN}), // Zombie.
        ENEMY_SKELETON (4, 6, 12, EnemyCategoryEnum.ENEMY_CATEGORY_UNDEAD, 18, "Skeleton", 
          5, 8, "skeleton.png", new EnemyPowerEnum[]{EnemyPowerEnum.ENEMY_POWER_ATTACK}), // Skeleton.
        ENEMY_DRUID (5, 7, 14, EnemyCategoryEnum.ENEMY_CATEGORY_DEMON, 16, "Druid", 
          7, 12, "druid.png", new EnemyPowerEnum[]{EnemyPowerEnum.ENEMY_POWER_ATTACK, 
              EnemyPowerEnum.ENEMY_POWER_SCORCH, EnemyPowerEnum.ENEMY_POWER_HPDRAIN, 
              EnemyPowerEnum.ENEMY_POWER_MPDRAIN}), // Druid.
        ENEMY_MIMIC (6, 10, 16, EnemyCategoryEnum.ENEMY_CATEGORY_AUTOMATON, 30, "Mimic", 
          16, 25, "mimic.png", new EnemyPowerEnum[]{EnemyPowerEnum.ENEMY_POWER_ATTACK}), // Mimic.
        ENEMY_DEATH_SPEAKER (7, 8, 15, EnemyCategoryEnum.ENEMY_CATEGORY_DEMON, 84, "Death Speaker", 
          225, 275, "death_speaker.png", new EnemyPowerEnum[]{EnemyPowerEnum.ENEMY_POWER_ATTACK, 
              EnemyPowerEnum.ENEMY_POWER_SCORCH}) // Death speaker.
        ; // semicolon needed when fields / methods follow
        
        private final int enemyAtkMax; // Enemy maximum attack value.
        private final int enemyAtkMin; // Enemy minimum attack value.
        private final EnemyCategoryEnum enemyCategory; // Enemy category.
        private final int enemyEnum; // Enumerations related to enemies.
        private final int enemyHP; // Enemy hit points.
        private static final Map enemyMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        private final String enemyName; // Enemy name.
        private final int enemyGoldMax; // Enemy maximum gold value.
        private final int enemyGoldMin; // Enemy minimum gold value.
        private final String imageFile; // Image filename (just name and extension, no path).
        private final EnemyPowerEnum[] powers; // Enemy powers.
        
        // enemyEnum = Enumerations related to enemies.
        // enemyAtkMin = Enemy minimum attack value.
        // enemyAtkMax = Enemy maximum attack value.
        // enemyCategory = Enemy category.
        // enemyHP = Enemy hit points.
        // enemyName = Enemy name.
        // enemyGoldMin = Enemy minimum gold value.
        // enemyGoldMax = Enemy maximum gold value.
        // imageFile = Image filename (just name and extension, no path).
        // powers = List of enemy powers.
        private EnemyEnum(int enemyEnum, int enemyAtkMin, int enemyAtkMax, EnemyCategoryEnum enemyCategory, 
          int enemyHP, String enemyName, int enemyGoldMin, int enemyGoldMax, String imageFile, 
          EnemyPowerEnum[] powers) 
        {
            // The constructor sets the values for each enumeration.
            this.enemyEnum = enemyEnum;
            this.enemyAtkMin = enemyAtkMin;
            this.enemyAtkMax = enemyAtkMax;
            this.enemyCategory = enemyCategory;
            this.enemyHP = enemyHP;
            this.enemyName = enemyName;
            this.enemyGoldMin = enemyGoldMin;
            this.enemyGoldMax = enemyGoldMax;
            this.imageFile = imageFile;
            this.powers = powers;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (EnemyEnum enemyEnum : EnemyEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                enemyMap.put(enemyEnum.enemyEnum, enemyEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = AtlasEnum.EnemyEnum.ENEMY_SHADOW_TENDRILS.getValue();
            
            // Return the numeric value for the enumeration.
            return enemyEnum;
        }
        
        public int getValue_AtkMax() 
        {
            // The function returns the maximum attack value for the enemy.
            // Example for use:  int x = AtlasEnum.EnemyEnum.ENEMY_SHADOW_TENDRILS.getValue_AtkMax();
            
            // Return the maximum attack value for the enemy.
            return enemyAtkMax;
        }
        
        public int getValue_AtkMin() 
        {
            // The function returns the minimum attack value for the enemy.
            // Example for use:  int x = AtlasEnum.EnemyEnum.ENEMY_SHADOW_TENDRILS.getValue_AtkMin();
            
            // Return the minimum attack value for the enemy.
            return enemyAtkMin;
        }
        
        public EnemyCategoryEnum getValue_Category() 
        {
            // The function returns the category for the enemy.
            // Example for use:  MusicEnum x = AtlasEnum.EnemyEnum.ENEMY_SHADOW_TENDRILS.getValue_Category();
            
            // Return the category for the enemy.
            return enemyCategory;
        }
        
        public int getValue_GoldMax() 
        {
            // The function returns the maximum gold value for the enemy.
            // Example for use:  int x = AtlasEnum.EnemyEnum.ENEMY_SHADOW_TENDRILS.getValue_GoldMax();
            
            // Return the maximum gold value for the enemy.
            return enemyGoldMax;
        }
        
        public int getValue_GoldMin() 
        {
            // The function returns the minimum gold value for the enemy.
            // Example for use:  int x = AtlasEnum.EnemyEnum.ENEMY_SHADOW_TENDRILS.getValue_GoldMin();
            
            // Return the minimum gold value for the enemy.
            return enemyGoldMin;
        }
        
        public int getValue_HP() 
        {
            // The function returns the hit points for the enemy.
            // Example for use:  int x = AtlasEnum.EnemyEnum.ENEMY_SHADOW_TENDRILS.getValue_HP();
            
            // Return the hit points for the enemy.
            return enemyHP;
        }
        
        public String getValue_Name() 
        {
            // The function returns the enemy name.
            // Example for use:  String x = AtlasEnum.EnemyEnum.ENEMY_SHADOW_TENDRILS.getValue_Name();
            
            // Return the enemy name.
            return enemyName;
        }
        
        public String getValue_ImageFile() 
        {
            // The function returns the image filename (just name and extension, no path).
            // Example for use:  String x = AtlasEnum.EnemyEnum.ENEMY_SHADOW_TENDRILS.getValue_ImageFile();
            
            // Return the image filename.
            return imageFile;
        }
        
        // enemy = Numeric value to convert to text.
        public static EnemyEnum valueOf(int enemy) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (EnemyEnum) enemyMap.get(enemy);
        }
        
    }
    
    // Enumerations related to enemy powers.
    public enum EnemyPowerEnum 
    {
        
        ENEMY_POWER_ATTACK (0), // Power -- attack.
        ENEMY_POWER_SCORCH (1), // Power -- scorch.
        ENEMY_POWER_HPDRAIN (2), // Power -- hit point drain.
        ENEMY_POWER_MPDRAIN (3) // Power - magic point drain.
        ; // semicolon needed when fields / methods follow

        private final int enemyPowerEnum; // Enumerations related to enemy powers.
        private static final Map enemyPowerMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // enemyPowerEnum = Value to associate.
        private EnemyPowerEnum(int enemyPowerEnum) 
        {
            // The constructor sets the values for each enumeration.
            this.enemyPowerEnum = enemyPowerEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (EnemyPowerEnum enemyPowerEnum : EnemyPowerEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                enemyPowerMap.put(enemyPowerEnum.enemyPowerEnum, enemyPowerEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = AtlasEnum.EnemyPowerEnum.ENEMY_POWER_SCORCH.getValue();
            
            // Return the numeric value for the enumeration.
            return enemyPowerEnum;
        }
        
        // enemyPower = Numeric value to convert to text.
        public static EnemyPowerEnum valueOf(int enemyPower) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (EnemyPowerEnum) enemyPowerMap.get(enemyPower);
        }
        
    }
    
    // Enumerations related to direction player is facing.
    public enum FacingEnum 
    {
        
        NORTH (0), // Player is facing north.
        SOUTH (1), // Player is facing south.
        EAST (2), // Player is facing east.
        WEST (3) // Player is facing west.
        ; // semicolon needed when fields / methods follow

        private final int facingEnum; // Enumerations related to direction player is facing.
        private static final Map facingMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // facingEnum = Value to associate.
        private FacingEnum(int facingEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.facingEnum = facingEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (FacingEnum facingEnum : FacingEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                facingMap.put(facingEnum.facingEnum, facingEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.FacingEnum.NORTH.getValue();
            
            // Return the numeric value for the enumeration.
            return facingEnum;
        }
        
        // facing = Numeric value to convert to text.
        public static FacingEnum valueOf(int facing) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (FacingEnum) facingMap.get(facing);
        }
        
    }
    
    // Enumerations related to fonts.
    public enum FontEnum 
    {
        
        FONT_UI (0, "uiFont"), // Regular font.
        FONT_RED (1, "uiFontRed") // Red font.
        ; // semicolon needed when fields / methods follow

        private final int fontEnum; // Enumerations related to dialog buttons.
        private final String fontSkinKey; // Key associated with font -- used with Skin.
        private static final Map fontMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // fontEnum = Value to associate.
        // fontSkinKey = Key associated with font -- used with Skin.
        private FontEnum(int fontEnum, String fontSkinKey) 
        {
            // The constructor sets the values for each enumeration.
            this.fontEnum = fontEnum;
            this.fontSkinKey = fontSkinKey;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (FontEnum fontEnum : FontEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                fontMap.put(fontEnum.fontEnum, fontEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.FontEnum.FONT_UI.getValue();
            
            // Return the numeric value for the enumeration.
            return fontEnum;
        }
        
        public String getValue_Key() 
        {
            // The function returns the key associated with the font.
            // Example for use:  String x = HeroineEnum.FontEnum.FONT_UI.getValue_Key();
            
            // Return the key associated with the font.
            return fontSkinKey;
        }
        
        // font = Numeric value to convert to text.
        public static FontEnum valueOf(int font) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (FontEnum) fontMap.get(font);
        }
        
    }
    
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
        private static final Map gameStateMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // gameState = Value to associate.
        private GameState(int gameState)
        {
            // The constructor sets the numeric values for each enumeration.
            this.gameState = gameState;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (GameState gameStateEnum : GameState.values()) 
            {
                // Add the current enumeration to the hash map.
                gameStateMap.put(gameStateEnum.gameState, gameStateEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.GameState.STATE_COMBAT.getValue();
            
            // Return the numeric value for the enumeration.
            return gameState;
        }
        
        // facing = Numeric value to convert to text.
        public static GameState valueOf(int gameState) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (GameState) gameStateMap.get(gameState);
        }
        
    }
    
    // Enumerations related to background images.
    public enum ImgBackgroundEnum 
    {
        
        IMG_BACK_BLACK (0, "black.png", "black", false), // Black background.
        IMG_BACK_NIGHTSKY (1, "nightsky.png", "nightsky", true), // Night sky background.
        IMG_BACK_TEMPEST (2, "tempest.png", "tempest", false), // Tempest background.
        IMG_BACK_INTERIOR (3, "interior.png", "interior", false), // Interior background.
        IMG_BACK_TITLE (4, "title.png", "title", false) // Title (menu) background.
        ; // semicolon needed when fields / methods follow

        private final boolean loadFirst; // Whether to load before other objects.  Used with title screen.
        private final int imgBackgroundEnum; // Enumerations related to background images.
        private final String imgFile; // Filename (just name and extension, no path).
        private final String imgKey; // Key associated with image -- used with asset manager hash map.
        private static final Map imgBackgroundMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // imgBackgroundEnum = Value to associate.
        // imgFile = Filename (just name and extension, no path).
        // imgKey = Key associated with image -- used with asset manager hash map.
        // loadFirst = Whether to load before other objects.  Used with title screen.
        private ImgBackgroundEnum(int imgBackgroundEnum, String imgFile, String imgKey, boolean loadFirst) 
        {
            // The constructor sets the values for each enumeration.
            this.imgBackgroundEnum = imgBackgroundEnum;
            this.imgFile = imgFile;
            this.imgKey = imgKey;
            this.loadFirst = loadFirst;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (ImgBackgroundEnum imgBackgroundEnum : ImgBackgroundEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                imgBackgroundMap.put(imgBackgroundEnum.imgBackgroundEnum, imgBackgroundEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.ImgBackgroundEnum.IMG_BACK_TEMPEST.getValue();
            
            // Return the numeric value for the enumeration.
            return imgBackgroundEnum;
        }
        
        public String getValue_File() 
        {
            // The function returns the filename (just name and extension, no path).
            // Example for use:  String x = HeroineEnum.ImgBackgroundEnum.IMG_BACK_TEMPEST.getValue_File();
            
            // Return the filename (just name and extension, no path).
            return imgFile;
        }
        
        public String getValue_Key() 
        {
            // The function returns the image key -- used with the asset manager hash map.
            // Example for use:  String x = HeroineEnum.ImgBackgroundEnum.IMG_BACK_TEMPEST.getValue_Key();
            
            // Return the key.
            return imgKey;
        }
        
        public boolean getValue_LoadFirst() 
        {
            // The function returns whether to load before other assets -- used with introduction screen.
            // Example for use:  boolean x = HeroineEnum.ImgBackgroundEnum.IMG_BACK_TEMPEST.getValue_LoadFirst();
            
            // Return the flag.
            return loadFirst;
        }
        
        // imgBackground = Numeric value to convert to text.
        public static ImgBackgroundEnum valueOf(int imgBackground) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (ImgBackgroundEnum) imgBackgroundMap.get(imgBackground);
        }
        
    }
    
    // Enumerations related to interface images -- except fonts.
    public enum ImgInterfaceEnum 
    {
        
        IMG_INTERFACE_ACTION_BTN (0, "action_buttons.png", "action_buttons.pack", "action_btn", "action_btn_atlas"), // Action buttons.
        IMG_INTERFACE_DIALOG_BTN (1, "dialog_buttons.png", "dialog_buttons.atlas", "dialog_btn", "dialog_btn_atlas"), // Dialog buttons.
        IMG_INTERFACE_HEROINE (2, "heroine.png", "", "heroine", ""), // Heroine -- character pictures.
        IMG_INTERFACE_INFO_BTN (3, "info_button.png", "", "info_btn", ""), // Information button.
        IMG_INTERFACE_MINIMAP (4, "minimap.png", "", "minimap", ""), // Minimap icons.
        IMG_INTERFACE_MINIMAP_CURSOR (4, "minimap_cursor.png", "", "minimap_cursor", ""), // Minimap cursor.
        IMG_INTERFACE_SELECT (4, "select.png", "", "select", "") // Button selection.
        ; // semicolon needed when fields / methods follow

        private final int imgInterfaceEnum; // Enumerations related to interface images.
        private final String imgAtlasFile; // Filename (just name and extension, no path) for atlas.
        private final String imgAtlasKey; // Key associated with atlas -- used with asset manager hash map.
        private final String imgFile; // Filename (just name and extension, no path).
        private final String imgKey; // Key associated with image -- used with asset manager hash map.
        private static final Map imgInterfaceMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // imgInterfaceEnum = Value to associate.
        // imgFile = Filename (just name and extension, no path).
        // imgAtlasFile = Filename (just name and extension, no path) for atlas.
        // imgKey = Key associated with image -- used with asset manager hash map.
        // imgAtlasKey = Key associated with atlas -- used with asset manager hash map.
        private ImgInterfaceEnum(int imgInterfaceEnum, String imgFile, String imgAtlasFile, String imgKey, 
          String imgAtlasKey) 
        {
            // The constructor sets the values for each enumeration.
            this.imgInterfaceEnum = imgInterfaceEnum;
            this.imgFile = imgFile;
            this.imgAtlasFile = imgAtlasFile;
            this.imgKey = imgKey;
            this.imgAtlasKey = imgAtlasKey;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (ImgInterfaceEnum imgInterfaceEnum : ImgInterfaceEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                imgInterfaceMap.put(imgInterfaceEnum.imgInterfaceEnum, imgInterfaceEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.ImgInterfaceEnum.IMG_INTERFACE_DIALOG_BTN.getValue();
            
            // Return the numeric value for the enumeration.
            return imgInterfaceEnum;
        }
        
        public String getValue_AtlasFile() 
        {
            // The function returns the filename (just name and extension, no path).
            // Example for use:  String x = HeroineEnum.ImgBackgroundEnum.IMG_INTERFACE_DIALOG_BTN.getValue_AtlasFile();
            
            // Return the filename (just name and extension, no path).
            return imgAtlasFile;
        }
        
        public String getValue_AtlasKey() 
        {
            // The function returns the atlas key -- used with the asset manager hash map.
            // Example for use:  String x = HeroineEnum.ImgBackgroundEnum.IMG_INTERFACE_DIALOG_BTN.getValue_AtlasKey();
            
            // Return the atlas key.
            return imgAtlasKey;
        }
        
        public String getValue_File() 
        {
            // The function returns the filename (just name and extension, no path).
            // Example for use:  String x = HeroineEnum.ImgBackgroundEnum.IMG_INTERFACE_DIALOG_BTN.getValue_File();
            
            // Return the filename (just name and extension, no path).
            return imgFile;
        }
        
        public String getValue_Key() 
        {
            // The function returns the image key -- used with the asset manager hash map.
            // Example for use:  String x = HeroineEnum.ImgBackgroundEnum.IMG_INTERFACE_DIALOG_BTN.getValue_Key();
            
            // Return the image key.
            return imgKey;
        }
        
        // imgInterface = Numeric value to convert to text.
        public static ImgInterfaceEnum valueOf(int imgInterface) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (ImgInterfaceEnum) imgInterfaceMap.get(imgInterface);
        }
        
    }
    
    // Enumerations related to list types.
    public enum ListEnum 
    {
        
        STD_ARRAY (0), // Type for standard arrays.
        ARRAY_LIST (1), // Type for array lists.
        HASH_MAP (2) // Type for hash maps.
        ; // semicolon needed when fields / methods follow

        private final int listEnum; // Enumerations related to list types.
        private static final Map listMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // listEnum = Value to associate.
        private ListEnum(int listEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.listEnum = listEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (ListEnum listEnum : ListEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                listMap.put(listEnum.listEnum, listEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.ListEnum.ARRAY_LIST.getValue();
            
            // Return the numeric value for the enumeration.
            return listEnum;
        }
        
        // listType = Numeric value to convert to text.
        public static ListEnum valueOf(int listType) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (ListEnum) listMap.get(listType);
        }
        
    }
    
    // Enumerations related to music.
    public enum MusicEnum 
    {
        
        MUSIC_ELEGY_DM (0, "elegy_dm.mp3", "elegy_dm.ogg"), // Elegy DM music.
        MUSIC_HAPLY (1, "haply.mp3", "haply.ogg"), // Haply music.
        MUSIC_KAWARAYU (2, "kawarayu.mp3", "kawarayu.ogg"), // Kawarayu music.
        M31 (3, "m31.mp3", "m31.ogg") // M31 music.
        ; // semicolon needed when fields / methods follow

        private final int musicEnum; // Enumerations related to music.
        private final String mp3File; // Filename (just name and extension, no path) in mp3 format.
        private final String oggFile; // Filename (just name and extension, no path) in ogg format.
        private static final Map musicMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // musicEnum = Value to associate.
        // mp3File = Filename (just name and extension, no path) in mp3 format.
        // oggFile = Filename (just name and extension, no path) in ogg format.
        private MusicEnum(int musicEnum, String mp3File, String oggFile) 
        {
            // The constructor sets the values for each enumeration.
            this.musicEnum = musicEnum;
            this.mp3File = mp3File;
            this.oggFile = oggFile;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (MusicEnum musicEnum : MusicEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                musicMap.put(musicEnum.musicEnum, musicEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = AtlasEnum.MusicEnum.MUSIC_HAPLY.getValue();
            
            // Return the numeric value for the enumeration.
            return musicEnum;
        }
        
        public String getValue_File_mp3() 
        {
            // The function returns the mp3 filename (just name and extension, no path).
            // Example for use:  String x = AtlasEnum.MusicEnum.MUSIC_HAPLY.getValue_File_mp3();
            
            // Return the mp3 filename (just name and extension, no path).
            return mp3File;
        }
        
        public String getValue_File_ogg() 
        {
            // The function returns the ogg filename (just name and extension, no path).
            // Example for use:  String x = AtlasEnum.MusicEnum.MUSIC_HAPLY.getValue_File_ogg();
            
            // Return the ogg filename (just name and extension, no path).
            return oggFile;
        }
        
        // music = Numeric value to convert to text.
        public static MusicEnum valueOf(int music) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (MusicEnum) musicMap.get(music);
        }
        
    }
    
    // Enumerations related to shops / locations.
    public enum ShopEnum 
    {
        
        SHOP_CEDAR_ARMS (0), // Cedar Arms
        SHOP_SIMMONS_CLOTHIER (1), // Simmons Fine Clothier
        SHOP_PILGRIM_INN (2), // The Pilgrim Inn
        SHOP_SAGE_THEREL (3), // Sage Therel
        SHOP_WOODSMAN (4), // Woodsman
        SHOP_STONEGATE_ENTRANCE (5), // Stonegate Entrance
        SHOP_THOMAS_THE_FENCE (6), // Thomas the Fence
        SHOP_THIEVES_GUILD (7), // Thieve's Guild
        SHOP_A_NIGHTMARE (8) // A Nightmare
        ; // semicolon needed when fields / methods follow

        private final int shopEnum; // Enumerations related to shops / locations.
        private static final Map shopMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.

        // shopEnum = Value to associate.
        private ShopEnum(int shopEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.shopEnum = shopEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (ShopEnum shop : ShopEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                shopMap.put(shop.shopEnum, shop);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.ShopEnum.SHOP_A_NIGHTMARE.getValue();
            
            // Return the numeric value for the enumeration.
            return shopEnum;
        }
        
        // shop = Numeric value to convert to text.
        public static ShopEnum valueOf(int shop) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (ShopEnum) shopMap.get(shop);
        }
        
    }
    
    // Enumerations related to shops, locations, and dialogues.
    public enum ShopTypeEnum 
    {
        
        SHOP_WEAPON (0), // Type for weapon shops.
        SHOP_ARMOR (1), // Type for armor shops.
        SHOP_SPELL (2), // Type for spell shops.
        SHOP_ROOM (3), // Type for rooms (inns).
        SHOP_MESSAGE (4) // Type for messages / text.
        ; // semicolon needed when fields / methods follow

        private final int shopTypeEnum; // Enumerations related to shops, locations, and dialogues.
        private static final Map shopTypeMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // shopTypeEnum = Value to associate.
        private ShopTypeEnum(int shopTypeEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.shopTypeEnum = shopTypeEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (ShopTypeEnum shopType : ShopTypeEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                shopTypeMap.put(shopType.shopTypeEnum, shopType);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.ShopTypeEnum.SHOP_ROOM.getValue();
            
            // Return the numeric value for the enumeration.
            return shopTypeEnum;
        }
        
        // shopType = Numeric value to convert to text.
        public static ShopTypeEnum valueOf(int shopType) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (ShopTypeEnum) shopTypeMap.get(shopType);
        }
        
    }
    
    // Enumerations related to sounds.
    public enum SoundEnum 
    {
        
        SOUND_ATTACK (0, "sounds/attack.wav"), // Attack-related sound.
        SOUND_BLOCKED (1, "sounds/blocked.wav"), // Attack blocked sound.
        SOUND_BONESHIELD (2, "sounds/boneshield.wav"), // Sound related to attack from Bone Shield boss.
        SOUND_CLICK (3, "sounds/click.wav"), // Sound related to mouse-click.
        SOUND_COIN (4, "sounds/coin.wav"), // Sound related to picking up one or more coin(s).
        SOUND_CRITICAL (5, "sounds/critical.wav"), // Sound related to a critical hit.
        SOUND_DEFEAT (6, "sounds/defeat.wav"), // Sound related to a defeat.
        SOUND_FIRE (7, "sounds/fire.wav"), // Sound related to a fire spell.
        SOUND_HEAL (8, "sounds/heal.wav"), // Sound related to a heal spell.
        SOUND_HP_DRAIN (9, "sounds/hpdrain.wav"), // Sound related to a heal spell.
        SOUND_MISS (10, "sounds/miss.wav"), // Sound related to a miss.
        SOUND_MP_DRAIN (11, "sounds/mpdrain.wav"), // Sound related to a magic point drain attack.
        SOUND_RUN (12, "sounds/run.wav"), // Sound related to a run action (fleeing combat).
        SOUND_UNLOCK (13, "sounds/unlock.wav") // Sound related to unlocking an object.
        ; // semicolon needed when fields / methods follow

        private final int soundEnum; // Enumerations related to sounds.
        private final String soundFilePath; // Relative path to the sound file.
        private static final Map soundMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // soundEnum = Value to associate.
        // soundFilePath = Relative path to the sound file.
        private SoundEnum(int soundEnum, String soundFilePath) 
        {
            // The constructor sets the values for each enumeration.
            this.soundEnum = soundEnum;
            this.soundFilePath = soundFilePath;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (SoundEnum soundEnum : SoundEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                soundMap.put(soundEnum.soundEnum, soundEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.SoundEnum.SOUND_RUN.getValue();
            
            // Return the numeric value for the enumeration.
            return soundEnum;
        }
        
        public String getValue_FilePath() 
        {
            // The function returns the relative file path value for the enumeration.
            // Example for use:  String x = HeroineEnum.ArmorEnum.SOUND_RUN.getValue_FilePath();
            
            // Return the relative file path value for the enumeration.
            return soundFilePath;
        }
        
        // sound = Numeric value to convert to text.
        public static SoundEnum valueOf(int sound) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (SoundEnum) soundMap.get(sound);
        }
        
    }
    
    // Enumerations related to spells.
    public enum SpellEnum 
    {
        
        NO_SPELL (0, "No Spell", 0), // No spell.
        SPELL_HEAL (1, "Heal", 0), // Heal spell.
        SPELL_BURN (2, "Burn", 100), // Burn spell.
        SPELL_UNLOCK (3, "Unlock", 500), // Unlock spell.
        SPELL_LIGHT (4, "Light", 2500), // Light spell.
        SPELL_FREEZE (5, "Freeze", 10000), // Freeze spell.
        SPELL_REFLECT (6, "Reflect", 50000) // Reflect spell.
        ; // semicolon needed when fields / methods follow

        private final int gold; // Value of the weapon, in gold.
        private final int spellEnum; // Enumerations related to spells.
        private static final Map spellMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        private final String spellText; // Clean text for the spell.
        
        // spellEnum = Value to associate.
        // spellText = Clean text to associate.
        // gold = Weapon value, in gold, to associate.
        private SpellEnum(int spellEnum, String spellText, int gold) 
        {
            // The constructor sets the values for each enumeration.
            this.spellEnum = spellEnum;
            this.spellText = spellText;
            this.gold = gold;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (SpellEnum spellEnum : SpellEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                spellMap.put(spellEnum.spellEnum, spellEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.SpellEnum.SPELL_LIGHT.getValue();
            
            // Return the numeric value for the enumeration.
            return spellEnum;
        }
        
        public int getValue_Gold() 
        {
            // The function returns the gold value for the enumeration.
            // Example for use:  int x = HeroineEnum.SpellEnum.SPELL_LIGHT.getValue_Gold();
            
            // Return the gold value for the enumeration.
            return gold;
        }
        
        public String getValue_CleanText() 
        {
            // The function returns the clean text value for the enumeration.
            // Example for use:  String x = HeroineEnum.SpellEnum.SPELL_LIGHT.getValue_CleanText();
            
            // Return the clean text value for the enumeration.
            return spellText;
        }
        
        // spell = Numeric value to convert to text.
        public static SpellEnum valueOf(int spell) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (SpellEnum) spellMap.get(spell);
        }
        
    }
    
    // Enumerations related to weapons.
    public enum WeaponEnum 
    {
        
        WEAPON_BARE_FISTS (0, "Bare Fists", 1, 4, 0), // Bare fists.
        WEAPON_WOOD_STICK (1, "Wood Stick", 2, 6, 0), // Wood stick.
        WEAPON_IRON_KNIFE (2, "Iron Knife", 3, 8, 50), // Iron knife.
        WEAPON_BRONZE_MACE (3, "Bronze Mace", 4, 10, 200), // Bronze mace.
        WEAPON_STEEL_SWORD (4, "Steel Sword", 5, 12, 1000), // Steel sword.
        WEAPON_WAR_HAMMER (5, "War Hammer", 6, 14, 5000), // War hammer.
        WEAPON_BATTLE_AXE (6, "Battle Axe", 7, 16, 20000), // Battle axe.
        WEAPON_GREAT_SWORD (7, "Great Sword", 8, 18, 100000) // Great sword.
        ; // semicolon needed when fields / methods follow

        private final int atk_max; // Maximum attack value for the weapon.
        private final int atk_min; // Minimum attack value for the weapon.
        private final int gold; // Value of the weapon, in gold.
        private final int weaponEnum; // Enumerations related to weapons.
        private static final Map weaponMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        private final String weaponText; // Clean text for the weapon.
        
        // weaponEnum = Value to associate.
        // weaponText = Clean text to associate.
        // atk_min = Minimum attack value to associate.
        // atk_max = Maximum attack value to associate.
        // gold = Weapon value, in gold, to associate.
        private WeaponEnum(int weaponEnum, String weaponText, int atk_min, int atk_max, int gold) 
        {
            // The constructor sets the values for each enumeration.
            this.weaponEnum = weaponEnum;
            this.weaponText = weaponText;
            this.atk_min = atk_min;
            this.atk_max = atk_max;
            this.gold = gold;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (WeaponEnum weaponEnum : WeaponEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                weaponMap.put(weaponEnum.weaponEnum, weaponEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.WeaponEnum.IRON_KNIFE.getValue();
            
            // Return the numeric value for the enumeration.
            return weaponEnum;
        }
        
        public int getValue_Atk_Max() 
        {
            // The function returns the maximum attack value for the enumeration.
            // Example for use:  int x = HeroineEnum.WeaponEnum.IRON_KNIFE.getValue_Atk_Max();
            
            // Return the maximum attack value for the enumeration.
            return atk_max;
        }
        
        public int getValue_Atk_Min() 
        {
            // The function returns the minimum attack value for the enumeration.
            // Example for use:  int x = HeroineEnum.WeaponEnum.IRON_KNIFE.getValue_Atk_Min();
            
            // Return the minimum attack value for the enumeration.
            return atk_min;
        }
        
        public int getValue_Gold() 
        {
            // The function returns the gold value for the enumeration.
            // Example for use:  int x = HeroineEnum.WeaponEnum.IRON_KNIFE.getValue_Gold();
            
            // Return the gold value for the enumeration.
            return gold;
        }
        
        public String getValue_CleanText() 
        {
            // The function returns the clean text value for the enumeration.
            // Example for use:  String x = HeroineEnum.WeaponEnum.IRON_KNIFE.getValue_CleanText();
            
            // Return the clean text value for the enumeration.
            return weaponText;
        }
        
        // weapon = Numeric value to convert to text.
        public static WeaponEnum valueOf(int weapon) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (WeaponEnum) weaponMap.get(weapon);
        }
        
    }
    
}