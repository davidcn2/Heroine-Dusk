package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import core.AssetMgr;

// Java imports.
import java.util.HashMap;

public class Sounds 
{
    
    /*
    The class stores information and logic related to the music and sound elements.
    Note that the asset manager (class) actually loads the music and sounds.
    
    Methods include:

    disposeAudio:  Clears the memory related to LibGDX objects for sound.
    mapAudioHashMaps:  Populates the hash maps pointed at the sounds and music objects.
    playMusic:  Plays the passed music.
    playMusicDirect:  Plays the passed music directly via referencing the asset manager.
    playSound:  Plays the passed sound.
    */
    
    // Declare object variables.
    private HashMap<String, Music> musicMap; // Collection of music, stored as a HashMap.  Ogg format.
    private HashMap<String, Sound> soundMap; // Collection of sounds, stored as a HashMap.
    
    // Declare regular variables.
    private float audioVolume; // Volume to use with Sound and Music objects
    
    public Sounds()
    {
        
        // The constructor sets the starting audio volume.
        
        // Set starting volume to use with Sound and Music objects.
        audioVolume = 0.80f;
                
    }
    
    public void disposeAudio()
    {
        
        // The function clears the memory related to LibGDX objects for sound.
        
        // Loop through sounds in hash map.
        soundMap.values().forEach((sound) -> {
            
            // Clear memory related to current LibGDX object for sound.
            sound.dispose();
            
        });
        
        // Loop through music in hash map.
        musicMap.values().forEach((music) -> {
            
            // Clear memory related to current LibGDX object for music.
            music.dispose();
            
        });
        
    }
    
    // hdg = Reference to Heroine Dusk (main) game.
    public void mapAudioHashMaps(HeroineDuskGame hdg)
    {
        
        // The function populates the hash maps pointed at the sounds and music objects.
        
        String filePath; // (Relative) path to sound or music -- used as key in asset manager.
        String musicKey; // Key to use with hash map -- one of the MusicEnum values, as a String.
        String soundKey; // Key to use with hash map -- one of the SoundEnum values, as a String.
        
        // Initialize the hash maps.
        soundMap = new HashMap<>();
        musicMap = new HashMap<>();
        
        // Populate hash map for sounds.
        
        // Loop through sound enumerations.
        for (HeroineEnum.SoundEnum soundEnum : HeroineEnum.SoundEnum.values())
            
        {
            
            // Get key / file path for sound.
            filePath = soundEnum.getValue_FilePath();
            
            // Get enumerated value for the sound, as a String.
            soundKey = soundEnum.toString();
            
            // Add sound related to current enumeration to hash map.
            soundMap.put(soundKey, hdg.getAssetMgr().manager.get(filePath));
            
        }
        
        // Populate hash map for music.
        
        // Loop through music enumerations.
        for (HeroineEnum.MusicEnum musicEnum : HeroineEnum.MusicEnum.values())
            
        {
            
            // Get key / file path for music in ogg format.
            filePath = musicEnum.getValue_File_ogg();
            
            // Get enumerated value for the music, as a String.
            musicKey = musicEnum.toString();
            
            // Add music related to current enumeration to hash map.
            musicMap.put(musicKey, hdg.getAssetMgr().manager.get(filePath));
            
        }
        
    }
    
    // whichMusic = Name of music to play.  Corresponds to one of the enumerated values in MusicEnum.
    public void playMusic(HeroineEnum.MusicEnum whichMusic)
    {
        
        // The function plays the passed music -- matching the enumerated key value to the key in the hash map.
        
        // Play music -- looping.
        musicMap.get(whichMusic.toString()).setLooping(true);
        musicMap.get(whichMusic.toString()).setVolume(audioVolume);
        musicMap.get(whichMusic.toString()).play();
        
    }
    
    // assetMgr = Reference to the asset manager.
    // whichMusic = Name of music to play.  Corresponds to one of the enumerated values in MusicEnum.
    public void playMusicDirect(AssetMgr assetMgr, HeroineEnum.MusicEnum whichMusic)
    {
        
        // The function plays the passed music directly via referencing the asset manager.
        
        // Play music -- looping.
        // To Do:  Uncomment when ready to play background music at beginning.
        //assetMgr.getMusicOgg(whichMusic).setLooping(true);
        //assetMgr.getMusicOgg(whichMusic).setVolume(audioVolume);
        //assetMgr.getMusicOgg(whichMusic).play();
        
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