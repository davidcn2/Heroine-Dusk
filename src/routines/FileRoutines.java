package routines;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

public class FileRoutines 
{
    
    /* 
    The class encapsulates flat file related functionality.
    
    Methods include:
    
    writeUsingBufferedWriter:  Writes the passed text to a file, using a buffered writer.
    */
    
    // data = Data to write to file.
    // filename = Filename to use when saving atlas, including full path.
    public static void writeUsingBufferedWriter(String data, String filename) 
    {
        
        /*
        The function writes the passed text to a file, using a buffered writer.
        
        A buffered writer outputs text to a character-output stream, while buffering characters so as to 
        provide for the efficient writing of single characters, strings, and arrays.
        
        Based on:  https://www.journaldev.com/878/java-write-to-file.
        */
        
        BufferedWriter br; // Used to write from passed text (String) to buffer.
        File file; // Reference to file.
        FileWriter fr; // Used to write from buffer to file.
        
        // Set defaults.
        fr = null;
        br = null;
        
        // Create new file with the passed name.
        file = new File(filename);
        
        // Try to write passed text to buffer and then file.
        
        // First, try to write to buffer.
        try
            {
            // Initialize writer object allowing for writing to the file.
            fr = new FileWriter(file);
            
            // Initialize buffer object allowing for writing to the file.
            br = new BufferedWriter(fr);
            
            // Write line of text to buffer and file.
            br.write(data);
        }
        
        // When an error occurs...
        catch (IOException e) 
        {
            // Display error information.
            System.out.println("Error occurred writing to buffer / file:  " + e.getMessage());
        }
        
        // Upon successful completion of writing to buffer...
        finally
        {
            // Attempt to close buffer and file.
            try 
            {
                
                // If buffered writer exists, then...
                if (br != null)
                    br.close(); // Buffered writer exists.  Close buffered writer.
                
                // If file writer exists, then...
                if (fr != null)
                    fr.close(); // File writer exists.  Close file writer.
            } 
            
            // When an error occurs...
            catch (IOException e) 
            {
                // Display error information.
                System.out.println("Error occurred closing buffer / file:  " + e.getMessage());
            }
            
        }
        
    }
    
}
