package core;

// Java imports.
import java.util.HashMap;
import java.util.Map;

public class CoreEnum 
{

    /*
    The file consolidates enumerations used in multiple classes.
    Enumerations represent special Java types used to define collections of constants.
    More precisely, a Java enum type acts as a special kind of Java class. An enum can 
    contain constants, methods, ...
    
    Enumerations include:
    
    1.  AlignEnum:  Enumerations related to horizontal alignment.
    2.  AssetKeyTypeEnum:  Enumerations related to asset key types.
    3.  PosRelativeEnum:  Enumerations related to relative position.
    */
    
    // Enumerations related to text alignment.
    public enum AlignEnum 
    {
        
        ALIGN_LEFT (0), // Align to the left.
        ALIGN_CENTER (1), // Align to the center.
        ALIGN_RIGHT (2) // Align to the right.
        ; // semicolon needed when fields / methods follow

        private final int alignEnum; // Enumerations related to horizontal alignment.
        private static final Map alignMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // alignEnum = Value to associate.
        private AlignEnum(int alignEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.alignEnum = alignEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (AlignEnum alignEnum : AlignEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                alignMap.put(alignEnum.alignEnum, alignEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.AlignEnum.ALIGN_RIGHT.getValue();
            
            // Return the numeric value for the enumeration.
            return alignEnum;
        }
        
        // align = Numeric value to convert to text.
        public static AlignEnum valueOf(int align) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (AlignEnum) alignMap.get(align);
        }
        
    }
    
    // Enumerations related to asset manager key types.
    public enum AssetKeyTypeEnum 
    {
        
        KEY_TEXTURE (0), // Key for texture.  Related (hash map) variable:  assetMapping_Textures.
        KEY_TEXTURE_REGION (1) // Key for texture region.  Related (hash map) variable:  textureRegions.
        ; // semicolon needed when fields / methods follow

        private final int assetKeyTypeEnum; // Enumerations related to asset manager key type.
        private static final Map assetKeyTypeMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // assetKeyTypeEnum = Value to associate.
        private AssetKeyTypeEnum(int assetKeyTypeEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.assetKeyTypeEnum = assetKeyTypeEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (AssetKeyTypeEnum assetKeyTypeEnum : AssetKeyTypeEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                assetKeyTypeMap.put(assetKeyTypeEnum.assetKeyTypeEnum, assetKeyTypeEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.AssetKeyTypeEnum.KEY_TEXTURE_REGION.getValue();
            
            // Return the numeric value for the enumeration.
            return assetKeyTypeEnum;
        }
        
        // assetKeyTypeEnum = Numeric value to convert to text.
        public static AssetKeyTypeEnum valueOf(int assetKeyTypeEnum) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (AssetKeyTypeEnum) assetKeyTypeMap.get(assetKeyTypeEnum);
        }
        
    }
    
    // Enumerations related to relative position.
    public enum PosRelativeEnum 
    {
        
        REL_POS_GENERAL (0), // No adjustments, place simply as indicated.
        REL_POS_UPPER_LEFT (1), // Align relative to the upper left corner.
        REL_POS_UPPER_RIGHT (2), // Align relative to the upper right corner.
        REL_POS_LOWER_LEFT (3), // Align relative to the lower left corner.
        REL_POS_LOWER_RIGHT (4) // Align relative to the lower right corner.
        ; // semicolon needed when fields / methods follow

        private final int posRelativeEnum; // Enumerations related to relative label positioning.
        private static final Map posRelativeMap = new HashMap<>(); // Hash map containing text and numbers in enumeration.
        
        // posRelativeEnum = Value to associate.
        private PosRelativeEnum(int posRelativeEnum) 
        {
            // The constructor sets the numeric values for each enumeration.
            this.posRelativeEnum = posRelativeEnum;
        }
        
        // Populate the hash map containing the text and numbers.
        static 
        {
            
            // Loop through each of the enumerated values.
            for (PosRelativeEnum posRelativeEnum : PosRelativeEnum.values()) 
            {
                // Add the current enumeration to the hash map.
                posRelativeMap.put(posRelativeEnum.posRelativeEnum, posRelativeEnum);
            }
            
        }
        
        public int getValue() 
        {
            // The function returns the numeric value for the enumeration.
            // Example for use:  int x = HeroineEnum.PosRelativeEnum.ALIGN_UPPER_RIGHT.getValue();
            
            // Return the numeric value for the enumeration.
            return posRelativeEnum;
        }
        
        // posRelative = Numeric value to convert to text.
        public static PosRelativeEnum valueOf(int posRelative) 
        {
            // The function converts the passed numeric value to its corresponding text.
            return (PosRelativeEnum) posRelativeMap.get(posRelative);
        }
        
    }
    
}
