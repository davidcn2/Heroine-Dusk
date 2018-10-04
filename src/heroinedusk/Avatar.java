package heroinedusk;

// Java imports.
import java.util.ArrayList;

public class Avatar 
{

    /*
    The class stores information and logic related to the player.
    
    Methods include:

    adjGold:  Adjusts the amount of gold the players owns by the passed amount.
    avatar_move:  Updates the (possible) player position based on a movement action.
    avatar_sleep:  Handles the player sleeping -- restores HP and MP and sets the respawn point.
    avatar_turn_left:  Handles the player turning left.
    avatar_turn_right:  Handles the player turning right.
    decrement_mp:  Decreases the magic points of the player by one (used when casting a spell, for example).
    */
    
    // Declare object variables.
    private ArrayList<HeroineEnum.SpellEnum> spellList; // List of player spells.
    
    // Declare regular variables.
    private HeroineEnum.ArmorEnum armor; // Current armor.
    private int bonus_atk; // Current bonus to player attack value.
    private int bonus_def; // Current bonus to player defense value.
    private int gold; // Current player gold.
    private int hp; // Current player hit points.
    private int max_hp; // Maximum player hit points.
    private int max_mp; // Maximum player magic points.
    private int mp; // Current player magic points.
    private boolean moved; // Whether player has moved yet.
    private HeroineEnum.FacingEnum facing; // Current direction player is facing.
    private int map_id; // Current map section in which player resides.
    private MapLocation sleeploc; // Sleep location / respawn point.
    private HeroineEnum.SpellEnum spellbook; // Current spellbook of player.
      // Contains spells in range of 0 to value.
    private HeroineEnum.WeaponEnum weapon; // Current weapon.
    private int x; // Player x-coordinate in the current map section.
    private int y; // Player y-coordinate in the current map section.
    
    public Avatar()
    {
        
        // The constructor initializes array lists and sets the starting values for the player.
        
        // Initialize array lists.
        spellList = new ArrayList<>();
        
        // Set starting values for the player.
        x = 1;
        y = 1;
        facing = HeroineEnum.FacingEnum.SOUTH;
        moved = false;
        map_id = 0;
        weapon = HeroineEnum.WeaponEnum.WEAPON_BARE_FISTS;
        armor = HeroineEnum.ArmorEnum.ARMOR_SERF_RAGS;
        hp = 5; //25;
        max_hp = 25;
        mp = 4;
        max_mp = 4;
        gold = 80000; // 0;
        bonus_atk = 0;
        bonus_def = 0;
        spellbook = HeroineEnum.SpellEnum.NO_SPELL;
        sleeploc = new MapLocation(0, 1, 1);
        
        // temp
        setSpellbook(HeroineEnum.SpellEnum.SPELL_HEAL);
        setSpellbook(HeroineEnum.SpellEnum.SPELL_BURN);
        setSpellbook(HeroineEnum.SpellEnum.SPELL_UNLOCK);
        setSpellbook(HeroineEnum.SpellEnum.SPELL_LIGHT);
        setSpellbook(HeroineEnum.SpellEnum.SPELL_FREEZE);
        setSpellbook(HeroineEnum.SpellEnum.SPELL_REFLECT);
        
    }
    
    // amount = Amount of gold to add to or subtract from player inventory.
    public void adjGold(int amount)
    {
        // The function adjusts the amount of gold the players owns by the passed amount.
        gold += amount;
    }
    
    // forwardInd = Whether moving foward.  true = Forward, false = Backward.  Examples:  true, false.
    // mazemap = Reference to active region / map information.
    // gameHD = Reference to HeroineDusk (main) game class.
    public boolean avatar_move(boolean forwardInd, MazeMap mazemap, HeroineDuskGame gameHD)
    {
        
        // The function updates the (possible) player position based on a movement action.
        // If the player can walk to the adjusted position, update the current x and y coordinates.
        
        boolean movementInd; // Whether movement occurred.
        int newX; // New x-coordinate.
        int newY; // New y-coordinate.
        
        // If moving forward, then...
        if (forwardInd)
        {
            // Moving forward / up.
            
            // Determine pending new location.
            newX = x + facing.getValue_MoveUpDx();
            newY = y + facing.getValue_MoveUpDy();
        }
        
        else
        {
            // Moving backward / down.
            
            // Determine pending new location.
            newX = x + facing.getValue_MoveDownDx();
            newY = y + facing.getValue_MoveDownDy();
        }
        
        // If proposed position within current region / map bounds, then...
        if (newX >= 0 && newY >= 0 && newX < mazemap.getRegionWidth() && newY < mazemap.getRegionHeight())
        {
        
            // Proposed position within current region / map bounds.
        
            // If proposed position walkable, then...
            if (mazemap.getImgTileEnum(newX, newY).getValue_Walkable())
            {

                // Proposed position walkable.

                // Adjust player position.
                x = newX;
                y = newY;

                // Flag movement as occurring.
                movementInd = true;

            }

            else
            {

                // Cannot walk to proposed position.

                // Play sound.
                gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_BLOCKED);

                // Flag movement as NOT occurring.
                movementInd = false;

            }
            
        } // End ... If proposed position within current region / map bounds.
        
        else
        {
            
            // Proposed position outside current region / map bounds.
            
            // Display warning.
            System.out.println("Warning:  Trying to move outside map bounds.");
            
            // Play sound.
            gameHD.getSounds().playSound(HeroineEnum.SoundEnum.SOUND_BLOCKED);
            
            // Flag movement as NOT occurring.
            movementInd = false;
            
        }
        
        // Return whether movement occurred.
        return movementInd;
        
    }
    
    public void avatar_sleep()
    {
        
        // The function handles the player sleeping -- restores HP and MP and sets the respawn point.
        
        // Restore HP and MP and set respawn point.
        hp = max_hp;
        mp = max_mp;
        // To Do:  avatar.sleeploc = [mazemap.current_id, avatar.x, avatar.y];
        
    }
    
    public void avatar_turn_left()
    {
        
        // The function handles the player turning to the left.
        
        // Adjust direction of player.
        facing = facing.getValue_TurnLeft();
        
    }
    
    public void avatar_turn_right()
    {
        
        // The function handles the player turning to the right.
        
        // Adjust direction of player.
        facing = facing.getValue_TurnRight();
        
    }
    
    public void decrement_mp()
    {
        
        // The function decreases the magic points of the player by one (used when casting a spell, for example).
        
        // Decrease player magic points by one.
        mp--;
        
    }
    
    // Getters and setters below...
    
    public HeroineEnum.ArmorEnum getArmor() {
        return armor;
    }

    public void setArmor(HeroineEnum.ArmorEnum armor) {
        this.armor = armor;
    }

    public int getBonus_atk() {
        return bonus_atk;
    }

    public void setBonus_atk(int bonus_atk) {
        this.bonus_atk = bonus_atk;
    }

    public int getBonus_def() {
        return bonus_def;
    }

    public void setBonus_def(int bonus_def) {
        this.bonus_def = bonus_def;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMax_hp() {
        return max_hp;
    }

    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }

    public boolean getHp_AtMax() {
        return hp == max_hp;
    }
    
    public int getMax_mp() {
        return max_mp;
    }

    public void setMax_mp(int max_mp) {
        this.max_mp = max_mp;
    }

    public boolean getMp_AtMax() {
        return mp == max_mp;
    }
    
    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public boolean get_HpMp_AtMax() {
        return hp == max_hp && mp == max_mp;
    }
    
    public HeroineEnum.FacingEnum getFacing() {
        return facing;
    }

    public void setFacing(HeroineEnum.FacingEnum facing) {
        this.facing = facing;
    }

    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
    
    public MapLocation getSleepLoc() {
        return sleeploc;
    }
    
    // map_id = Map section identifier.
    // x = X-coordinate within the map section.
    // y = Y-coordinate within the map section.
    public void setSleepLoc(int map_id, int x, int y) {
        this.sleeploc.setMapLocation(map_id, x, y);
    }
    
    public HeroineEnum.SpellEnum getSpellbook() {
        return spellbook;
    }

    public void setSpellbook(HeroineEnum.SpellEnum spellbook) {
        this.spellbook = spellbook;
        this.spellList.add(spellbook);
    }

    public ArrayList<HeroineEnum.SpellEnum> getSpellList() {
        return spellList;
    }
    
    public HeroineEnum.WeaponEnum getWeapon() {
        return weapon;
    }

    public void setWeapon(HeroineEnum.WeaponEnum weapon) {
        this.weapon = weapon;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
