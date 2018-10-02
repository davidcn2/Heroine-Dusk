package heroinedusk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class HeroineDusk {

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) 
    {
        
        // The function configures and launches the application.
        
        final int windowWidth = 960; // Starting width of application window.
        final int windowHeight = 720; // Starting height of application window.
        
        LwjglApplicationConfiguration config; // Application configuration object.
        
        // Create application configuration object.
        config = new LwjglApplicationConfiguration();

	// Change configuration settings.
	config.width = windowWidth; // Set width of application window.
	config.height = windowHeight; // Set height of application window.
	config.title = "Heroine Dusk!"; // Set title of application.
        config.forceExit = false; // Prevent default behavior of LWJGL 2 backend calling System.exit(-1).
        config.resizable = false; // Prevent maximizing and resizing of screen.
        //config.samples = 4; // Adjust sampling rate to improve anti-aliasing.
        
        // Launch game using configuration settings.
	new LwjglApplication(new HeroineDuskGame(windowWidth, windowHeight), config);
        
    }
    
}