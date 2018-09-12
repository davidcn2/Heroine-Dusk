package heroinedusk;

public class Options 
{
    
    private boolean animation; // Whether animations turned on.
    private boolean minimap; // Whether minimap turned on.
    private boolean music; // Whether music turned on.
    private boolean sfx; // Whether sounds effects turned on.

    // animation = Whether animations turned on.
    // music = Whether music turned on.
    // sfx = Whether sound effects turned on.
    // minimap = Whether minimap turned on.
    public Options(boolean animation, boolean music, boolean sfx, boolean minimap)
    {
        // The constructor sets the values for the flags for animation, music, and sound effects.
        this.animation = animation;
        this.music = music;
        this.sfx = sfx;
        this.minimap = minimap;
    }
    
    // Getters and setters below...
    
    public boolean getAnimationsOn() {
        return animation;
    }

    // animation = Whether animations turns on.
    public void AnimationsOn(boolean animation) {
        this.animation = animation;
    }

    public void AnimationsReverse()
    {
        animation = !animation;
    }

    public String getAnimationsText() {
        return animation ? "ANIMATIONS ARE ON" : "ANIMATIONS ARE OFF";
    }
    
    public boolean getMusicOn() {
        return music;
    }

    // music = Whether music turned on.
    public void MusicOn(boolean music) {
        this.music = music;
    }

    public void MusicReverse()
    {
        music = !music;
    }
    
    public String getMusicText() {
        return music ? "MUSIC IS ON" : "MUSIC IS OFF";
    }
    
    public boolean getSfxOn() {
        return sfx;
    }

    // sfx = Whether sound effects turned on.
    public void SfxOn(boolean sfx) {
        this.sfx = sfx;
    }
    
    public void SfxReverse()
    {
        sfx = !sfx;
    }

    public String getSfxText() {
        return sfx ? "SOUNDS ARE ON" : "SOUNDS ARE OFF";
    }
    
    public boolean getMinimapOn() {
        return minimap;
    }

    // minimap = Whether minimap turns on.
    public void MinimapOn(boolean minimap) {
        this.minimap = minimap;
    }
    
    public void MinimapReverse()
    {
        minimap = !minimap;
    }
    
    public String getMinimapText() {
        return minimap ? "MINIMAP IS ON" : "MINIMAP IS OFF";
    }
    
}
