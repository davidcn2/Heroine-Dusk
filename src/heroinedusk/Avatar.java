package heroinedusk;

// Local project imports.
import core.BaseActor;
import gui.CustomLabel;

// Java imports.
import core.AssetMgr;
import java.util.HashMap;

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
    takeItem:  Handles giving an item to the player.
    */
    
    // Declare object variables.
    //private final ArrayList<HeroineEnum.SpellEnum> spellList; // List of player spells.
    private final HashMap<HeroineEnum.SpellEnum, Boolean> spellList; // List of player spells.
    
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
    private MapLocation mapLocation; // Current map location of player -- includes region, x, and y.
    private MapLocation mapLocationForward; // Map location of square in front of player.
      // Only updated during calls to getMapLocation_ForwardLoc().
    private final MapLocation sleeploc; // Sleep location / respawn point.
    private HeroineEnum.SpellEnum spellbook; // Current spellbook of player.
      // Contains spells in range of 0 to value.
    private HeroineEnum.WeaponEnum weapon; // Current weapon.
    private int x; // Player x-coordinate in the current map section.
    private int y; // Player y-coordinate in the current map section.
    
    public Avatar()
    {
        
        // The constructor initializes array lists and sets the starting values for the player.
        
        // Initialize hash maps.
        spellList = new HashMap<>();
        
        // Set starting values for the player.
        x = 1; //3; //1; //2; //1;
        y = 1; //2; //1; //1;
        facing = HeroineEnum.FacingEnum.SOUTH; //HeroineEnum.FacingEnum.EAST; // HeroineEnum.FacingEnum.SOUTH;
        moved = false;
        map_id = 0; //10; //0;
        weapon = HeroineEnum.WeaponEnum.WEAPON_BARE_FISTS;
        armor = HeroineEnum.ArmorEnum.ARMOR_SERF_RAGS;
        hp = 15; //25;
        max_hp = 25;
        mp = 10; //14;
        max_mp = 14;
        gold = 80000; // 0;
        bonus_atk = 0;
        bonus_def = 0;
        spellbook = HeroineEnum.SpellEnum.NO_SPELL;
        sleeploc = new MapLocation(0, 1, 1);
        mapLocation = new MapLocation(map_id, x, y);
        mapLocationForward = new MapLocation(-1, -1, -1);
        
        // temp
        setSpellbook(HeroineEnum.SpellEnum.SPELL_HEAL);
        setSpellbook(HeroineEnum.SpellEnum.SPELL_BURN);
        setSpellbook(HeroineEnum.SpellEnum.SPELL_UNLOCK);
        //setSpellbook(HeroineEnum.SpellEnum.SPELL_LIGHT);
        //setSpellbook(HeroineEnum.SpellEnum.SPELL_FREEZE);
        //setSpellbook(HeroineEnum.SpellEnum.SPELL_REFLECT);
        
    }
    
    // amount = Amount of gold to add to or subtract from player inventory.
    public void adjGold(int amount)
    {
        // The function adjusts the amount of gold the players owns by the passed amount.
        gold += amount;
    }
    
    // amount = Number of hit points to add to or subtract from player health.
    public void adjHp(int amount)
    {
        // The function adjusts the player health (hit points) by the passed amount.
        hp += amount;
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
            
            //System.out.println("Proposed tile (" + (mazemap.getImgTileEnum(newX, newY).getValue_Walkable() ? "Y" : "N") + "): " + mazemap.getImgTileEnum(newX, newY));
            //System.out.println("Proposed side tile (" + (mazemap.getImgTileEnum_Side(this, forwardInd).getValue_Walkable() ? "Y" : "N") + "): " + mazemap.getImgTileEnum_Side(this, forwardInd));
            
            // If proposed position walkable, then...
            if ( (mazemap.getImgTileEnum(newX, newY).getValue_Walkable() && 
                  mazemap.getImgTileEnum_Side(this, forwardInd) == 
                  HeroineEnum.ImgTileEnum.IMG_TILE_IGNORE_SIDE) || 
                mazemap.getImgTileEnum_Side(this, forwardInd).getValue_Walkable() )
            {

                // Proposed position walkable.

                // Adjust player position.
                x = newX;
                y = newY;
                mapLocation.setMapLocation(map_id, x, y);
                
                // Flag player as having moved.
                moved = true;
                
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
    
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    public void avatar_sleep(CustomLabel hpLabel, CustomLabel mpLabel)
    {
        
        // The function handles the player sleeping -- restores HP and MP and sets the respawn point.
        
        // Restore HP and MP and set respawn point.
        hp = max_hp;
        mp = max_mp;
        
        // If hit points label passed, then...
        if (hpLabel != null)
        {
            
            // Hit points label passed.
            
            // Update player hit points label.
            hpLabel.setLabelText( getHpText() );

            // Update player magic points label.
            mpLabel.setLabelText( getMpText() );
            
        }
        
        // Set sleep location.
        setSleepLoc( map_id, x, y );
        
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
    
    // itemEnum = Enumerated value for item to give to the player.
    // heroineWeapon = BaseActor object that acts as the player weapon.
    // weaponLabel = Label showing current player weapon.
    // heroineArmor = BaseActor object that acts as the player armor.
    // armorLabel = Label showing current player armor.
    // itemCount = Number of item to give to player.
    // assetMgr = Reference to the asset manager.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // goldLabel = Label showing player gold.
    public void takeItem(HeroineEnum.ItemEnum itemEnum, BaseActor heroineWeapon, CustomLabel weaponLabel, 
      BaseActor heroineArmor, CustomLabel armorLabel, int itemCount, AssetMgr assetMgr, CustomLabel hpLabel,
      CustomLabel mpLabel, CustomLabel goldLabel)
    {
        
        // The process handles giving an item to the player.
        // The process also updates the player images / labels in the information view on the explore screen.
        
        HeroineEnum.ArmorEnum armorEnum; // Armor found in chest.
        String armorKey; // Asset manager key related to armor texture region.
        HeroineEnum.WeaponEnum weaponEnum; // Weapon found in chest.
        String weaponKey; // Asset manager key related to weapon texture region.
        
        // Depending on the item type, ...
        switch (itemEnum.getValue_ItemType()) {

            case ITEM_TYPE_GOLD:

                // Found gold.

                // Give gold to player.
                gold += itemCount;
                
                // Update player gold label.
                goldLabel.setLabelText( getGoldText() );
                
                // Exit selector.
                break;

            case ITEM_TYPE_WEAPON:

                // Found a weapon.

                // Store enumeration for weapon found in chest.
                weaponEnum = HeroineEnum.WeaponEnum.valueOf(itemEnum.getValue_ItemName());

                // If weapon better than current one owned by player, then...
                if (weaponEnum.getValue() > weapon.getValue())
                {
                    
                    // Weapon better than current one owned by player.
                    
                    // Update weapon owned by player.
                    weapon = weaponEnum;
                    
                    // Determine asset manager key related to weapon.
                    weaponKey = weaponEnum.getValue_PlayerEnum().toString();
                    weaponKey = HeroineEnum.HeroinePlayerEnum.valueOf(weaponKey).getValue_Key();

                    // Update player weapon label.
                    weaponLabel.setLabelText( getWeaponText() );
                    
                    // Render updated player weapon.
                    heroineWeapon.setTextureRegion( assetMgr.getTextureRegion(weaponKey) );
                    
                }

                // Exit selector.
                break;

            case ITEM_TYPE_ARMOR:

                // Found an armor.

                // Store enumeration for armor found in chest.
                armorEnum = HeroineEnum.ArmorEnum.valueOf(itemEnum.getValue_ItemName());

                // If armor better than current one owned by player, then...
                if (armorEnum.getValue() > armor.getValue())
                {

                    // Armor better than current one owned by player.

                    // Update armor owned by player.
                    armor = armorEnum;

                    // Determine asset manager key related to armor.
                    armorKey = armorEnum.getValue_PlayerEnum().toString();
                    armorKey = HeroineEnum.HeroinePlayerEnum.valueOf(armorKey).getValue_Key();
                    
                    // Update player armor label.
                    armorLabel.setLabelText( getArmorText() );
                    
                    // Render updated player armor.
                    heroineArmor.setTextureRegion( assetMgr.getTextureRegion(armorKey) );

                }

                // Exit selector.
                break;

            case ITEM_TYPE_SPELL:

                // Found a spell.

                // Grant spell to player.
                setSpellbook( HeroineEnum.SpellEnum.valueOf(itemEnum.getValue_ItemName()) );
                
                // Exit selector.
                break;

            case ITEM_TYPE_MAGIC:

                // Found a magic item.

                // Adjust bonus attack, as necessary.
                bonus_atk += 
                  HeroineEnum.MagicItemEnum.valueOf(itemEnum.getValue_ItemName()).getValue_AttrAdj_BonusAtk();
                
                // Adjust bonus defense, as necessary.
                bonus_def += 
                  HeroineEnum.MagicItemEnum.valueOf(itemEnum.getValue_ItemName()).getValue_AttrAdj_BonusDef();
                
                // Adjust hit points, as necessary.
                hp +=
                  HeroineEnum.MagicItemEnum.valueOf(itemEnum.getValue_ItemName()).getValue_AttrAdj_HP();
                
                // Adjust maximum hit points, as necessary.
                max_hp +=
                  HeroineEnum.MagicItemEnum.valueOf(itemEnum.getValue_ItemName()).getValue_AttrAdj_MaxHP();
                
                // Adjust magic points, as necessary.
                mp +=
                  HeroineEnum.MagicItemEnum.valueOf(itemEnum.getValue_ItemName()).getValue_AttrAdj_MP();
                
                // Adjust maximum magic points, as necessary.
                max_mp +=
                  HeroineEnum.MagicItemEnum.valueOf(itemEnum.getValue_ItemName()).getValue_AttrAdj_MaxMP();
                
                // Update player armor label.
                armorLabel.setLabelText( getArmorText() );
                
                // Update player weapon label.
                weaponLabel.setLabelText( getWeaponText() );
                
                // Update player hit points label.
                hpLabel.setLabelText( getHpText() );
                
                // Update player magic points label.
                mpLabel.setLabelText( getMpText() );
                
                // Exit selector.
                break;

            default:

                // Invalid item type.

                // Display warning.
                System.out.println("Warning:  Found an invalid item type!");

                // Exit selector.
                break;

        }
        
    }
    
    // Getters and setters below...
    
    public HeroineEnum.ArmorEnum getArmor() {
        return armor;
    }

    public void setArmor(HeroineEnum.ArmorEnum armor) {
        this.armor = armor;
    }
    
    public String getArmorText() {
        return armor.getValue_CleanText().toUpperCase() + 
          (bonus_def > 0 ? " +" + Integer.toString(bonus_def) : "");
    }
    
    public boolean getBadlyHurtInd() {
        return hp <= (max_hp / 3);
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

    public String getGoldText() {
        return Integer.toString(gold) + " GOLD";
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
    
    public String getHpText() {
        return "HP " + Integer.toString(hp) + "/" + Integer.toString(max_hp);
    }
    
    public MapLocation getMapLocation_CurrentLoc() {
        return mapLocation;
    }
    
    public MapLocation getMapLocation_ForwardLoc() {
        
        // The function returns information about the current location in front of the player (map_id, x, y).
        
        // Depending on direction player facing, ...
        switch (facing) {
            
            case NORTH:
                
                // Facing north.
                
                // Set value of map location to return.
                mapLocationForward.setMapLocation(map_id, x, y - 1);
                
                // Exit selector.
                break;
                
            case SOUTH:
                
                // Facing south.
                
                // Set value of map location to return.
                mapLocationForward.setMapLocation(map_id, x, y + 1);
                
                // Exit selector.
                break;
                
            case EAST:
                
                // Facing east.
                
                // Set value of map location to return.
                mapLocationForward.setMapLocation(map_id, x + 1, y);
                
                // Exit selector.
                break;
                
            case WEST:
                
                // Facing west.
                
                // Set value of map location to return.
                mapLocationForward.setMapLocation(map_id, x - 1, y);
                
                // Exit selector.
                break;
                
            default:
                
                // Facing invalid direction.
            
                // Set value to return to -1 for all properties.
                mapLocationForward.setMapLocation(-1, -1, -1);
                
                // Display warning.
                System.out.println("Warning:  Player facing invalid direction while checking tile at forward location.");
                
                // Exit selector.
                break;
                
        }
        
        // Return location in front of player.
        return mapLocationForward;
        
    }
    
    public int getMaxDamage() {
        // The function returns the maximum damage caused by the player.
        return weapon.getValue_Atk_Max() + bonus_atk;
    }
    
    public int getMinDamage() {
        // The function returns the minimum damage caused by the player.
        return weapon.getValue_Atk_Min() + bonus_atk;
    }
    
    public boolean getMovedInd() {
        return moved;
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

    public String getMpText() {
        return "MP " + Integer.toString(mp) + "/" + Integer.toString(max_mp);
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
        this.mapLocation.setMapLocation(map_id, x, y);
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
        this.spellList.put(spellbook, true);
    }

    // spellEnum = Spell to check whether player knows and has magic points sufficient to cast.
    public boolean getSpellCastInd(HeroineEnum.SpellEnum spellEnum) {
        
        // The function returns whether the player knows the passed spell.
        
        boolean spellCastInd; // Whether player knows the passed spell.
          
        // Determine whether player knows spell.
        spellCastInd = getSpellInd(spellEnum);
        
        // Return whether the player knows the spell.
        return spellCastInd;
        
    }
    
    // spellEnum = Spell to check whether player knows.
    public boolean getSpellInd(HeroineEnum.SpellEnum spellEnum) {
        
        // The function returns whether the player knows the passed spell.
        
        boolean knowSpellInd; // Whether player knows the passed spell.
        
        // Set default return value.
        knowSpellInd = false;
        
        // If entry exists in hash map for spell, then...
        if (spellList.get(spellEnum) != null)
            
            // If entry indicates that player knows spell (value = true), then...
            if (spellList.get(spellEnum))
                // Entry indicates that player knows spell.
                
                // Flag as player knowing spell.
                knowSpellInd = true;
        
        // Return whether the player knows the spell.
        return knowSpellInd;
        
    }
    
    public HashMap<HeroineEnum.SpellEnum, Boolean> getSpellList() {
        return spellList;
    }
    
    public HeroineEnum.WeaponEnum getWeapon() {
        return weapon;
    }

    public void setWeapon(HeroineEnum.WeaponEnum weapon) {
        this.weapon = weapon;
    }

    public String getWeaponText() {
        return weapon.getValue_CleanText().toUpperCase() + 
          (bonus_atk > 0 ? " +" + Integer.toString(bonus_atk) : "");
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        this.mapLocation.setMapLocation(map_id, x, y);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        this.mapLocation.setMapLocation(map_id, x, y);
    }
    
}