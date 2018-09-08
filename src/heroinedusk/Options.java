package heroinedusk;

public class Options 
{
    
    private boolean animation; // Whether animations turned on.
    private boolean music; // Whether music turned on.
    private boolean sfx; // Whether sounds effects turned on.

    public Options(boolean animation, boolean music, boolean sfx)
    {
        // The constructor sets the values for the flags for animation, music, and sound effects.
        this.animation = animation;
        this.music = music;
        this.sfx = sfx;
    }
    
    public boolean getAnimationsOn() {
        return animation;
    }

    // animation = Whether animations turns on.
    public void AnimationsOn(boolean animation) {
        this.animation = animation;
    }

    public boolean getMusicOn() {
        return music;
    }

    // music = Whether music turned on.
    public void MusicOn(boolean music) {
        this.music = music;
    }

    public boolean getSfxOn() {
        return sfx;
    }

    // sfx = Whether sound effects turned on.
    public void SfxOn(boolean sfx) {
        this.sfx = sfx;
    }
    
}
