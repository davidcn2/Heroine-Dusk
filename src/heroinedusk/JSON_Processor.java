package heroinedusk;

// Local project imports.
import routines.FileRoutines;

// Jackson imports.
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

// Java imports.
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

public class JSON_Processor 
{
    
    /* 
    The class encapsulates JSON-related functionality, in particular for loading and saving atlases.
    
    Methods include:
    
    loadAtlas:  Encapsulates the reading of the atlas JSON file from the specified location.
    loadAtlasItems:  Encapsulates the reading of the atlas items JSON file from the specified location.
    readAtlas:  Encapsulates the reading of the atlas JSON file from the specified location, adding 
      exception handling.
    readAtlasItems:  Encapsulates the reading of the atlas items JSON file from the specified location, adding 
      exception handling.
    saveAtlas:  Encapsulates the saving of the atlas as a JSON file to the specified location, 
      adding exception handling.
    saveAtlasItems:  Encapsulates the saving of the atlas items as a JSON file to the specified location, 
      adding exception handling.
    writeAtlas:  Saves the atlas as a JSON file to the specified location.
    writeAtlasItems:  Saves the atlas items as a JSON file to the specified location.
    */
    
    // atlas = Contains atlas information.
    // classPath = Path to json file within class structure / package.  Example:  /json/Atlas.json.
    private void loadAtlas(Atlas atlas, String classPath) throws IOException
    {
        
        // The function encapsulates the reading of the atlas JSON file from the specified location.
        
        InputStream in; // Input stream of bytes related to passed path.
        Map<String, Object> mapJSON; // Hash map containing key / value pairs covering all atlas data -- 
        // used with JSON.
        ObjectMapper mapper; // ObjectMapper provides functionality for reading and writing the JSON.
        
        // Find resource with passed name / path.
        in = getClass().getResourceAsStream(classPath);
        
        // Initialize Jackson's serialization mapper.
        mapper = new ObjectMapper();
        
        // Convert JSON file data (loaded into byte array) to hash map.
        mapJSON = mapper.readValue(in, HashMap.class);
        
        // Load hash map using atlas functionality.
        atlas.readHashMap(mapJSON);
        
        // Close input stream.
        in.close();
        
    }

    // atlasItems = Contains atlas items information.
    // classPath = Path to json file within class structure / package.  Example:  /json/AtlasItems.json.
    // regionCount = Number of regions.
    private void loadAtlasItems(AtlasItems atlasItems, String classPath, int regionCount) throws IOException
    {
        
        // The function encapsulates the reading of the atlas items JSON file from the specified location.
        
        InputStream in; // Input stream of bytes related to passed path.
        Map<String, Object> mapJSON; // Hash map containing key / value pairs covering atlas items data -- 
        // used with JSON.
        ObjectMapper mapper; // ObjectMapper provides functionality for reading and writing the JSON.
        
        // Find resource with passed name / path.
        in = getClass().getResourceAsStream(classPath);
        
        // Initialize Jackson's serialization mapper.
        mapper = new ObjectMapper();
        
        // Convert JSON file data (loaded into byte array) to hash map.
        mapJSON = mapper.readValue(in, HashMap.class);
        
        // Load hash map using atlas items functionality.
        atlasItems.readHashMap(mapJSON, regionCount);
        
        // Close input stream.
        in.close();
        
    }

    // filename = Filename to use when reading atlas, including full path.
    // classPath = Path to json file within class structure / package.  Example:  /json/Atlas.json.
    public final Atlas readAtlas(String classPath)
    {
        
        // The function encapsulates the reading of the atlas JSON file from the specified location, 
        // adding exception handling.
        
        Atlas atlas; // Atlas to which to load data.
        
        // Initialize atlas.
        atlas = new Atlas();
        
        // Try reading the atlas information to a byte array.
        try {
            
            loadAtlas(atlas, classPath);
            
        }
        
        // Catch any io exceptions occurring when trying to read atlas from JSON file.
        catch (IOException ex) {
        
            // Display error message.
            System.out.println("Warning:  Error reading atlas from JSON file.\nMessage: " + ex.getMessage());
        
        }
        
        // Return atlas.
        return atlas;
        
    }

    // classPath = Path to json file within class structure / package.  Example:  /json/AtlasItems.json.
    // regionCount = Number of regions.
    public final AtlasItems readAtlasItems(String classPath, int regionCount)
    {
        
        // The function encapsulates the reading of the atlas items JSON file from the specified location, 
        // adding exception handling.
        
        AtlasItems atlasItems; // Contains atlas items information.
        
        // Initialize atlas items class.
        atlasItems = new AtlasItems();
        
        // Try reading the atlas item information to a byte array.
        try {
            
            loadAtlasItems(atlasItems, classPath, regionCount);
            
        }
        
        // Catch any io exceptions occurring when trying to read atlas items from JSON file.
        catch (IOException ex) {
        
            // Display error message.
            System.out.println("Warning:  Error reading atlas items from JSON file.\nMessage: " + ex.getMessage());
        
        }
        
        // Return atlas items.
        return atlasItems;
        
    }

    // atlas = Contains atlas information.
    // filename = Filename to use when saving atlas, including full path.
    public final void saveAtlas(Atlas atlas, String filename)
    {
        
        // The function encapsulates the saving of the atlas as a JSON file to the specified location, 
        // adding exception handling.
        
        // Try writing the atlas information to a JSON file.
        try {
        
            // Save atlas to JSON file.
            writeAtlas(atlas, filename);
        
        } 
        
        // Catch any io exceptions occurring when trying to write atlas to JSON file.
        catch (IOException ex) {
        
            // Display error message.
            System.out.println("Warning:  Error writing atlas to JSON file.\nMessage: " + ex.getMessage());
        
        }
        
    }
    
    // atlasItems = Contains atlas item information.
    // filename = Filename to use when saving atlas items, including full path.
    public final void saveAtlasItems(AtlasItems atlasItems, String filename)
    {
        
        // The function encapsulates the saving of the atlas item data as a JSON file to the specified 
        // location, adding exception handling.
        
        // Try writing the atlas item information to a JSON file.
        try {
        
            // Save atlas item data to JSON file.
            writeAtlasItems(atlasItems, filename);
        
        } 
        
        // Catch any io exceptions occurring when trying to write atlas item data to JSON file.
        catch (IOException ex) {
        
            // Display error message.
            System.out.println("Warning:  Error writing atlas items to JSON file.\nMessage: " + ex.getMessage());
        
        }
        
    }
    
    // atlas = Contains atlas information.
    // filename = Filename to use when saving atlas, including full path.
    private void writeAtlas(Atlas atlas, String filename) throws IOException
    {
        
        // The function saves the atlas as a JSON file to the specified location.
        
        ObjectMapper mapper; // ObjectMapper provides functionality for reading and writing the JSON.
        Map<String, Object> map; // Hash map containing all data to write to file.
        String mapJSON; // JSON text containing all information from hash map.
        
        // Initialize hash map.
        map = new HashMap<>();
        
        // Populate atlas hash map.
        atlas.populateHashMap();
        
        // Add region count to hash map.
        map.put("mapCount", atlas.mapCount);
        
        // Add region identifier list.
        map.put("mapIdentifiers", atlas.mapIdentifiers);
        
        // Add other atlas details.
        map.put("maps", atlas.mapJSON);
        
        // Initialize Jackson's serialization mapper.
        mapper = new ObjectMapper();
        
        // Order by keys.
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        
        // Convert from hash map to JSON text.
        mapJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        
        // Write the JSON text to the file.
        FileRoutines.writeUsingBufferedWriter(mapJSON, filename);
        
    }
    
    // atlasItems = Contains atlas item information.
    // filename = Filename to use when saving atlas items, including full path.
    private void writeAtlasItems(AtlasItems atlasItems, String filename) throws IOException
    {
        
        // The function saves the atlas item information as a JSON file to the specified location.
        
        ObjectMapper mapper; // ObjectMapper provides functionality for reading and writing the JSON.
        Map<String, Object> map; // Hash map containing all data to write to file.
        String mapJSON; // JSON text containing all information from hash map.
        
        // Initialize hash map.
        map = new HashMap<>();
        
        // Populate atlas items hash map.
        atlasItems.populateHashMap();
        
        // Add atlas item details.
        map.put("items", atlasItems.getMapJSON());
        
        // Initialize Jackson's serialization mapper.
        mapper = new ObjectMapper();
        
        // Order by keys.
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        
        // Convert from hash map to JSON text.
        mapJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        
        // Write the JSON text to the file.
        FileRoutines.writeUsingBufferedWriter(mapJSON, filename);
        
    }
    
}