package routines;

public class ArrayRoutines 
{
    
    /* 
    The class offers reusable functions related to arrays and similar Java container objects.
    
    Methods include:
    
    addAll:  Adds one to many values to the passed array.  Variations allow for writing to the first or last index.
    addAll_End:  Adds one to many values to the end of the passed array.
    */
    
    // arr = Destination (int) array.
    // elements = Source (int) array.
    public static void addAll(int[] arr, int ... elements)
    {
        
        // The function allows for the addition of one to many int values to an array (passed).
        // The three dots can only be used in a method argument, and are called varargs. 
        // The three dots (varargs) allow the passing in of an array of parameters without explicitly 
        // creating the array.
        // The function places the elements at the beginning of the destination array (arr).
        // Usage:  addAll(countryId, 1, 2, 3);
        
        // If one or more elements exist in the passed elements list, then...
        if (elements != null)
            {
            // Copy the elements to the beginning of the first list (arr).
            System.arraycopy(elements, 0, arr, 0, elements.length);
            }
        
    }
    
    // arr = Destination (int) array.
    // destPos = Position at which to copy within the target (arr), base 0.  0 = First array element.
    // elements = Source (int) array.
    public static void addAll(int[] arr, String destPos, int ... elements)
    {
        
        // The function allows for the addition of one to many int values to an array (passed).
        // The three dots can only be used in a method argument, and are called varargs. 
        // The three dots (varargs) allow the passing in of an array of parameters without explicitly 
        // creating the array.
        // The function places the elements at the specified index (destPos) within the destination array (arr).
        // Usage:  addAll(countryId, 2, 1, 2, 3);
        
        int destPosition; // Position to which to copy in destination array, converted to an integer.
        
        // Convert destination position to an integer.
        destPosition = Integer.parseInt(destPos);
        
        // If one or more elements exist in the passed elements list, then...
        if (elements != null)
            {
            // Copy the elements to the specified position within the first list (arr).
            System.arraycopy(elements, 0, arr, destPosition, elements.length);
            }
        
    }
    
    // arr = Destination (int) array.
    // elements = Source (int) array.
    public static void addAll_End(int[] arr, int ... elements)
    {
        
        // The function allows for the addition of one to many int values to an array (passed).
        // The three dots can only be used in a method argument, and are called varargs. 
        // The three dots (varargs) allow the passing in of an array of parameters without explicitly 
        // creating the array.
        // The function places the elements at the end of the destination array (arr).
        // Usage:  addAll_End(countryId, 1, 2, 3);
        
        // If one or more elements exist in the passed elements list, then...
        if (elements != null)
            {
            // Copy the elements to the end of the first list (arr).
            System.arraycopy(elements, 0, arr, arr.length - 1, elements.length);
            }
        
    }
    
    // arr = Destination (Integer) array.
    // elements = Source (Integer) array.
    public static void addAll(Integer[] arr, Integer ... elements)
    {
        
        // The function allows for the addition of one to many Integer values to an array (passed).
        // The three dots can only be used in a method argument, and are called varargs. 
        // The three dots (varargs) allow the passing in of an array of parameters without explicitly 
        // creating the array.
        // The function places the elements at the beginning of the destination array (arr).
        // Usage:  addAll(countryId, 1, 2, 3);
        
        // If one or more elements exist in the passed elements list, then...
        if (elements != null)
            {
            // Copy the elements to the beginning of the first list (arr).
            System.arraycopy(elements, 0, arr, 0, elements.length);
            }
        
    }
    
    // arr = Destination (Integer) array.
    // destPos = Position at which to copy within the target (arr), base 0.  0 = First array element.
    // elements = Source (Integer) array.
    public static void addAll(Integer[] arr, int destPos, Integer ... elements)
    {
        
        // The function allows for the addition of one to many Integer values to an array (passed).
        // The three dots can only be used in a method argument, and are called varargs. 
        // The three dots (varargs) allow the passing in of an array of parameters without explicitly 
        // creating the array.
        // The function places the elements at the specified index (destPos) within the destination array (arr).
        // Usage:  addAll(countryId, 2, 1, 2, 3);
        
        // If one or more elements exist in the passed elements list, then...
        if (elements != null)
            {
            // Copy the elements to the specified position within the first list (arr).
            System.arraycopy(elements, 0, arr, destPos, elements.length);
            }
        
    }
    
    // arr = Destination (Integer) array.
    // elements = Source (Integer) array.
    public static void addAll_End(Integer[] arr, Integer ... elements)
    {
        
        // The function allows for the addition of one to many Integer values to an array (passed).
        // The three dots can only be used in a method argument, and are called varargs. 
        // The three dots (varargs) allow the passing in of an array of parameters without explicitly 
        // creating the array.
        // The function places the elements at the end of the destination array (arr).
        // Usage:  addAll_End(countryId, 1, 2, 3);
        
        // If one or more elements exist in the passed elements list, then...
        if (elements != null)
            {
            // Copy the elements to the end of the first list (arr).
            System.arraycopy(elements, 0, arr, arr.length - 1, elements.length);
            }
        
    }
    
    // arr = Destination (String) array.
    // destPos = Position at which to copy within the target (arr), base 0.  0 = First array element.
    // elements = Source (String) array.
    public static void addAll(String[] arr, String ... elements)
    {
        
        // The function allows for the addition of one to many String values to an array (passed).
        // The three dots can only be used in a method argument, and are called varargs. 
        // The three dots (varargs) allow the passing in of an array of parameters without explicitly 
        // creating the array.
        // The function places the elements at the beginning of the destination array (arr).
        // Usage:  addAll(countryName, "Mexico", "US", "Ukraine");
        
        // If one or more elements exist in the passed elements list, then...
        if (elements != null)
            {
            // Copy the elements to the beginning of the first list (arr).
            System.arraycopy(elements, 0, arr, 0, elements.length);
            }
        
    }
    
    // arr = Destination (String) array.
    // destPos = Position at which to copy within the target (arr), base 0.  0 = First array element.
    // elements = Source (String) array.
    public static void addAll(String[] arr, int destPos, String ... elements)
    {
        
        // The function allows for the addition of one to many String values to an array (passed).
        // The three dots can only be used in a method argument, and are called varargs. 
        // The three dots (varargs) allow the passing in of an array of parameters without explicitly 
        // creating the array.
        // The function places the elements at the specified index (DestPos) within the destination array (arr).
        // Usage:  addAll(countryName, 2, "Mexico", "US", "Ukraine");
        
        // If one or more elements exist in the passed elements list, then...
        if (elements != null)
            {
            // Copy the elements to the specified position (destPos) within the first list (arr).
            System.arraycopy(elements, 0, arr, destPos, elements.length);
            }
        
    }
    
    // arr = Destination (String) array.
    // destPos = Position at which to copy within the target (arr), base 0.  0 = First array element.
    // elements = Source (String) array.
    public static void addAll_End(String[] arr, String ... elements)
    {
        
        // The function allows for the addition of one to many String values to an array (passed).
        // The three dots can only be used in a method argument, and are called varargs. 
        // The three dots (varargs) allow the passing in of an array of parameters without explicitly 
        // creating the array.
        // The function places the elements at the end of the destination array (arr).
        // Usage:  addAll_End(countryName, "Mexico", "US", "Ukraine");
        
        // If one or more elements exist in the passed elements list, then...
        if (elements != null)
            {
            // Copy the elements to the end of the first list (arr).
            System.arraycopy(elements, 0, arr, arr.length - 1, elements.length);
            }
        
    }
    
}
