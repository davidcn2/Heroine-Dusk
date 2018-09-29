package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.audio.Sound;

// Java imports.
import java.util.HashMap;

public class Sounds 
{
    
    /*
    The class stores information and logic related to the music and sound elements.
    Note that the asset manager (class) actually loads the music and sounds.
    
    Methods include:

    disposeSounds:  Clears the memory related to LibGDX objects for sound.
    playSound:  Plays the passed sound.
    */
    
    // Declare object variables.
    private final HashMap<String, Sound> soundMap; // Collection of sounds, stored as a HashMap.
    
    // Declare regular variables.
    private float audioVolume; // Volume to use with Sound and Music objects
    
    // hdg = Reference to Heroine Dusk (main) game.
    public Sounds(HeroineDuskGame hdg)
    {
        
        // The constructor sets the starting audio volume and initializes the hash maps pointed at 
        // the sounds and music objects.
        
        Sound sound; // Reference to sound to load.
        String filePath; // (Relative) path to sound -- used as key in asset manager.
        String soundKey; // Key to use with hash map -- one of the SoundEnum values, as a String.
        
        // Set starting volume to use with Sound and Music objects.
        audioVolume = 0.80f;
        
        // Initialize the hash map.
        soundMap = new HashMap<>();
        
        // Loop through sound enumerations.
        for (HeroineEnum.SoundEnum soundEnum : HeroineEnum.SoundEnum.values())
            
        {
            
            // Get key / file path for sound.
            filePath = soundEnum.getValue_FilePath();
            
            // Get the sound.
            sound = hdg.getAssetMgr().manager.get(filePath);
            
            // Get enumerated value for the sound, as a String.
            soundKey = soundEnum.toString();
            
            // Add sound related to current enumeration to hash map.
            soundMap.put(soundKey, sound);
            
        }
                
    }
    
    public void disposeSounds()
    {
        
        // The function clears the memory related to LibGDX objects for sound.
        
        // Loop through sounds in hash map.
        for (Sound sound : soundMap.values()) {
        
            // Clear memory related to current LibGDX object for sound.
            sound.dispose();
            
        }
        
    }
    
    // whichSound = Name of sound to play.  Corresponds to one of the enumerated values in SoundEnum.
    public void playSound(HeroineEnum.SoundEnum whichSound)
    {
        
        // The function plays the passed sound -- matching the enumerated key value to the key in the hash map.
        
        // Play sound.
        soundMap.get(whichSound.toString()).play(audioVolume);
        
    }
    
    // Getters and setters below...
    
    public float getAudioVolume() {
        return audioVolume;
    }

    public void setAudioVolume(float audioVolume) {
        this.audioVolume = audioVolume;
    }
    
}