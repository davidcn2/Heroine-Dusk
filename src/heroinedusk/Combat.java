package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;

// Local project imports.
import core.AssetMgr;
import core.BaseActor;
import core.ShakyActor;
import gui.CustomLabel;
import routines.UtilityRoutines;

// Java imports.
import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;

public class Combat
{
    
    /*
    The class stores information and logic related to combat.
    
    Methods include:

    fight:  Encapsulates logic related to a round of combat.
    initiateCombat:  Initializes variables related to a new combat.
    render_enemy:  Renders the enemy at the start of the combat.
    render_interface_start:  Renders the interface for the beginning (and duration of) combat, except for 
      the enemy.
    */
    
    // Declare object variables.
    
    // Declare regular variables.
    private final Avatar avatar; // Reference to player information class.
    private boolean boneshield_active; // Whether bone shield power (of enemy) active.
    private HeroineEnum.CombatPhaseEnum combatPhase; // Current combat phase.
    private HeroineEnum.EnemyEnum enemyEnum; // Type of enemy involved in combat.
    private int enemyHp; // Current number of hit points of enemy.
    private boolean enemyHurt; // Whether enemy hurt in current round.
    private final SecureRandom number; // Used for generating random numbers.
    private String offenseAction; // Action type employed by player.
    private String offenseResult; // Result of action by player.
    private final int selectorAdjPos; // Position adjustment related to selector.
    
    // Declare constants.
    private final Color COLOR_MED_GRAY = new Color(0.50f, 0.50f, 0.50f, 1);
    private final String OFFENSE_ACTION_CRITICAL = "CRITICAL!";
    private final String OFFENSE_RESULT_MISS = "MISS!";
    private final String OFFENSE_RESULT_RUN_FAIL = "BLOCKED!";
    private final String OFFENSE_RESULT_RUN_SUCCESS = "";
    
    // Constructors below...
    
    // avatar = Reference to player information class.
    // scale = Output scale factor -- multiple of 160 and 120.
    public Combat(Avatar avatar, int scale)
    {
        
        // The constructor initializes the combat engine and stores the parameters in their related class
        // variables.
        
        // Set defaults.
        boneshield_active = false;
        enemyHurt = false;
        
        // Start random number generator.
        number = new SecureRandom();
        
        // Store reference to player information class.
        this.avatar = avatar;
        
        // Calculate selector position adjustment.
        this.selectorAdjPos = scale * 2;
        
    }
    
    // Methods below...
    
    // fightEnum = Enumerated value related to player fighting action.
    // sounds = Reference to sounds class.
    // avatar = Reference to player class.
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // buttonActor = Reference to BaseActor for the button.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // damage = Damage caused by spell before calling method (something base).
    public void fight(HeroineEnum.FightEnum fightEnum, Sounds sounds, Avatar avatar, 
      CustomLabel powerActionLabel, CustomLabel powerResultLabel, CustomLabel hpLabel, CustomLabel mpLabel, 
      BaseActor buttonActor, Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled,
      Integer damage)
    {
        
        // The function encapsulates logic related to a round of combat.
        
        int atk_max; // Maximum damage caused by player.
        int atk_min; // Minimum damage caused by player.
        int crit_chance; // Whether player caused a critical hit to the enemy.
        int attack_damage; // Amount of damage caused by player in current round.
        int heal_amount; // Number of hit points to heal.  -1 = Unable to cast heal spell.
        int hit_chance; // Random number indicating whether player hit enemy.
        boolean proceedInd; // Whether to proceed to enemy attack and next round (or run).
        int run_chance; // Random number indicating whether player successfully ran away from enemy.
        boolean run_success; // Whether run attempt successful.
        
        // Set defaults.
        offenseResult = "";
        run_success = false;
        proceedInd = false;
        
        // Get offense action text based on player fighting action.
        offenseAction = fightEnum.getValue_Desc(); 
        
        // Move to beginning of new round of combat.
        combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_INPUT;
        
        // Depending on player fighting action, ...
        switch (fightEnum) {
            
            case FIGHT_ATTACK:
                
                // Player attacked enemy.
                
                // If enemy bone shield active, then...
                if (boneshield_active)
                {
                    
                    // Enemy bone shield active.
                    // Override hero action when the enemy has the bone shield up.
                    
                    // To Do:  Add bone shield functionality.
                    
                }
                
                else
                {
                    
                    // No enemy bone shield active.
                    
                    // "Roll" to determine if player hit enemy -- 1 to 100.
                    hit_chance = UtilityRoutines.generateStandardRnd(number, 1, 100);
                    
                    // If miss occurred, then...
                    if (hit_chance <= 20)
                    {
                        
                        // Miss occurred.
                        
                        // Store offense result.
                        offenseResult = OFFENSE_RESULT_MISS;
                        
                        // Play miss sound.
                        sounds.playSound(HeroineEnum.SoundEnum.SOUND_MISS);
                        
                        System.out.println("Miss");
                        
                    } // End ... If miss occurred.
                    
                    // Otherwise, ... > Hit occurred.
                    else
                    {
                        
                        // Hit occurred.
                        
                        // Store minimum and maximum damage caused by player.
                        atk_min = avatar.getMinDamage();
                        atk_max = avatar.getMaxDamage();
                        
                        // Randomly determine damage based on player minimum and maximum.
                        attack_damage = UtilityRoutines.generateStandardRnd(number, atk_min, atk_max);
                        
                        System.out.println("Dmg: " + atk_min + " to " + atk_max);
                        System.out.println("Attack caused " + attack_damage + " damage to enemy.");
                        
                        // Check for critical hit.
                        // A critical hit adds the maximum possible damage (ignoring bonuses) to what
                        // was already "rolled".
                        
                        // "Roll" for critical hit.
                        crit_chance = UtilityRoutines.generateStandardRnd(number, 1, 100);
                        
                        // If critical hit occurred, then...
                        if (crit_chance <= 10)
                        {
                            
                            // Critical hit occurred.
                            
                            // Add maximum possible damage (ignoring bonuses) to current.
                            attack_damage += atk_max;
                            
                            // Revise offense action text.
                            offenseAction = OFFENSE_ACTION_CRITICAL;
                            
                            // Play critical hit sound.
                            sounds.playSound(HeroineEnum.SoundEnum.SOUND_CRITICAL);
                            
                        }
                        
                        else
                        {
                            
                            // Normal hit occured.
                        
                            // Play normal hit sound.
                            sounds.playSound(HeroineEnum.SoundEnum.SOUND_ATTACK);
                            
                        }
                        
                        // Reduce enemy hit points.
                        enemyHp -= attack_damage;
                        
                        // Store offense result.
                        offenseResult = Integer.toString(attack_damage) + " damage";
                        
                        // Flag enemy as hurt.
                        enemyHurt = true;
                        
                    } // End ... If hit occurred.
                    
                } // End ... If enemy bone shield active.
                
                // Flag to proceed to enemy attack and next round.
                proceedInd = true;
                
                // Exit selector.
                break;
                
            case FIGHT_RUN:
                
                // Player trying to run from enemy.
                
                // Play run sound.
                sounds.playSound(HeroineEnum.SoundEnum.SOUND_RUN);
                
                // "Roll" to determine if player successfully ran away from enemy.
                run_chance = UtilityRoutines.generateStandardRnd(number, 1, 100);
                
                // If player successfully ran away from enemy, then...
                if (run_chance <= 66)
                {
                    
                    // Player successfully ran away from enemy.
                    
                    // Flag run attempt as successful.
                    run_success = true;
                    
                    // Store offense result.
                    offenseResult = OFFENSE_RESULT_RUN_SUCCESS;
                    
                }
                
                else
                {
                    
                    // Player failed to run away from enemy.
                    
                    // Flag run attempt as a failure.
                    run_success = false;
                    
                    // Store offense result.
                    offenseResult = OFFENSE_RESULT_RUN_FAIL;
                    
                }
                
                // Flag to proceed to enemy attack and next round (or run).
                proceedInd = true;
                
                // Exit selector.
                break;
                
            case FIGHT_HEAL:
                
                // Player cast healing spell.
                
                // Cast heal spell.
                heal_amount = Spells.cast_heal(avatar, powerActionLabel, powerResultLabel, hpLabel, 
                  mpLabel, buttonActor, mapActionButtonEnabled, sounds);
                
                // If player able to cast heal spell, then...
                if (heal_amount > -1)
                {
                    
                    // Player able to cast heal spell.
                    
                    // Flag to proceed to enemy attack and next round.
                    proceedInd = true;
                    
                }
                
                // Exit selector.
                break;
                
            case FIGHT_BURN:
                
                // Player cast burn spell.
                
                // Store base attack damage.
                attack_damage = damage;
                
                // If enemy category is undead, then...
                if (enemyEnum.getValue_Category() == HeroineEnum.EnemyCategoryEnum.ENEMY_CATEGORY_UNDEAD)
                {
                    
                    // Enemy category is undead.
                    
                    // Store maximum damage caused by player.
                    atk_max = avatar.getMaxDamage();
                    
                    // Add 2x maximum to damage to cause to enemy.
                    attack_damage += atk_max += atk_max;
                    
                } // End ... If enemy category is undead.
                
                // Otherwise, if enemy category is demon, then...
                else if(enemyEnum.getValue_Category() == HeroineEnum.EnemyCategoryEnum.ENEMY_CATEGORY_DEMON)
                {
                    
                    // Enemy category is demon.
                    
                    // Store maximum damage caused by player.
                    atk_max = avatar.getMaxDamage();
                    
                    // Add 1x maximum to damage to cause to enemy.
                    attack_damage += atk_max;
                    
                } // End ... If enemy category is demon.
                
                // Reduce enemy hit points.
                enemyHp -= attack_damage;

                // Store offense result.
                offenseResult = Integer.toString(attack_damage) + " damage";

                // Flag enemy as hurt.
                enemyHurt = true;

                // If enemy bone shield is active, then...
                if (this.boneshield_active)
                {
                    
                    // Enemy bone shield is active.
                    // Burn removes enemy bone shield.
                    
                    // Turn off enemy bone shield.
                    this.boneshield_active = false;
                    
                }
                
                // Flag to proceed to enemy attack and next round.
                proceedInd = true;
                
                // Exit selector.
                break;
                
            case FIGHT_UNLOCK:
                
                // Player cast unlock spell.
                
                // Store base attack damage.
                attack_damage = damage;
                
                // If enemy category is automaton, then...
                if (enemyEnum.getValue_Category() == HeroineEnum.EnemyCategoryEnum.ENEMY_CATEGORY_AUTOMATON)
                {
                    
                    // Enemy category is automaton.
                    
                    // Store maximum damage caused by player.
                    atk_max = avatar.getMaxDamage();
                    
                    // Add 2x maximum to damage to cause to enemy.
                    attack_damage += atk_max += atk_max;
                    
                } // End ... If enemy category is automaton.
                
                // Exit selector.
                break;
                
            case FIGHT_LIGHT:
                
                // Player cast light spell.
                
                // Display message about future functionality.
                System.out.println("Future functionality:  Light spell.");
                
                // Exit selector.
                break;
                
            case FIGHT_FREEZE:
                
                // Player cast freeze spell.
                
                // Display message about future functionality.
                System.out.println("Future functionality:  Freeze spell.");
                
                // Exit selector.
                break;
                
            case FIGHT_REFLECT:
                
                // Player cast reflect spell.
                
                // Display message about future functionality.
                System.out.println("Future functionality:  Reflect spell.");
                
                // Exit selector.
                break;
                
            default:
                
                // Unknown player fighting action occurred.
                
                // Display warning.
                System.out.println("Warning:  An unknown player fighting action occurred!");
                
                // Exit selector.
                break;
                
        } // End ... Depending on player fighting action.
            
        
    }
    
    // enemyEnum = Type of enemy involved in combat.
    // enemyLabel = Reference to label showing enemy type.
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // infoButton = BaseActor object that acts as the information button.
    // facingLabel = Label showing direction player is facing.
    // regionLabel = Label showing the current region name.
    // enemy = ShakyActor object that acts as the enemy.
    // assetMgr = Reference to the asset manager class.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapActionButtonPosX = X-coordinate of each action button.
    // mapActionButtonPosY = Y-coordinate of bottom of each action button.
    public void initiate_combat(HeroineEnum.EnemyEnum enemyEnum, CustomLabel enemyLabel, 
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, BaseActor infoButton, 
      CustomLabel facingLabel, CustomLabel regionLabel, ShakyActor enemy, AssetMgr assetMgr,
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosX,
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosY)
    {
        
        // The function initializes variables related to a new combat.
        
        // Set defaults.
        combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_INTRO;
        enemyHurt = false;
        
        System.out.println("Encountered enemy of type, " + enemyEnum + ".");
        
        // Store passed values.
        this.enemyEnum = enemyEnum;
        
        // Set starting number of hit points of enemy.
        this.enemyHp = enemyEnum.getValue_HP();
        
        // Render starting interface.
        render_interface_start(enemyEnum, enemyLabel, infoButtonSelector, hpLabel, mpLabel, infoButton, 
          facingLabel, regionLabel, mapActionButtons, mapActionButtonEnabled, mapActionButtonPosX, 
          mapActionButtonPosY);
        
        // Render enemy.
        render_enemy(enemyEnum, enemy, assetMgr);
        
    }
    
    // enemyEnum = Type of enemy involved in combat.
    // enemyLabel = Reference to label showing enemy type.
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // infoButton = BaseActor object that acts as the information button.
    // facingLabel = Label showing direction player is facing.
    // regionLabel = Label showing the current region name.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapActionButtonPosX = X-coordinate of each action button.
    // mapActionButtonPosY = Y-coordinate of bottom of each action button.
    private void render_interface_start(HeroineEnum.EnemyEnum enemyEnum, CustomLabel enemyLabel, 
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, BaseActor infoButton, 
      CustomLabel facingLabel, CustomLabel regionLabel, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosX,
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosY)
    {
        
        /*
        The function renders the interface for the beginning (and duration of) combat, except for the enemy.
        
        1.  Display enemy name, centered horizontally, slight below top of screen.
        2.  Display attack, run, and other action buttons.  Update enabled status.
        3.  Update shading for magic-related action buttons.
        4.  Display selector around attack button.
        5.  Display hp and mp labels (in standard locations).
        6.  Hide information button.
        7.  Hide facing label.
        8.  Hide region name label.
        */

        HeroineEnum.ActionButtonEnum actionButtonEnum; // Enumeration value related to current action button 
          // in loop.
        Set<Map.Entry<HeroineEnum.ActionButtonEnum, BaseActor>> entrySetActionButtons; // Set view of the 
          // action buttons in the hash map.
        HeroineEnum.SpellEnum spellEnum; // Spell enumeration value related to current action button in loop.
        
        // 1.  Display enemy name, centered horizontally, slight below top of screen.
        
        // Update enemy name and recenter horizontally.
        enemyLabel.setLabelText_Center(enemyEnum.getValue_Name());
        
        // Display enemy name.
        enemyLabel.applyVisible(true);
        
        // 2.  Display action buttons.  Update enabled status.
        
        // Display action buttons.
        
        // Store a set view of the spellbook mappings for the hash map.
        entrySetActionButtons = mapActionButtons.entrySet();
        
        // Enable attack and run buttons.
        mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK, true);
        mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN, true);
        
        // Loop through action buttons.
        for (Map.Entry entryActionButton : entrySetActionButtons)
        {
            
            // Store enumeration value for current action button in loop.
            actionButtonEnum = (HeroineEnum.ActionButtonEnum)entryActionButton.getKey();
            
            // Store spell enumeration value related to current action button in loop.
            spellEnum = HeroineEnum.SpellEnum.valueOf(actionButtonEnum);

            // If related spell exists (none occurs for attack and run), then...
            if (spellEnum != null)
            {
                
                // Related spell exists.
                
                // If player can cast spell related to current action button in loop, then...
                if (avatar.getSpellCastInd(spellEnum))
                {
                    // Player can cast spell related to current action button in loop.

                    // Enable action button.
                    mapActionButtonEnabled.put(actionButtonEnum, true);

                }
                
            }
            
            // If enabled, display current action button in loop.
            if (mapActionButtonEnabled.get(actionButtonEnum))
            {
                
                // Action enabled.  Display related button.
                mapActionButtons.get(actionButtonEnum).setVisible(true);
                
            }
            
        }
        
        // 3.  Update shading for magic-related action buttons.
        
        // A.  Burn button...
        
        // If player has magic left, then...
        if (avatar.getMp() > 0)
        {
            
            // Player has magic left.
            
            // Remove shade to the button to signify currently enabled.
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setColor(Color.WHITE);
            
        }
        
        else
        {
            
            // Player has no magic left.
            
            // Apply a dark shade to the button to signify not currently enabled.
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN).setColor(COLOR_MED_GRAY);
            
            // Disable button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN, false);
            
        }
        
        // B.  Unlock button.
        
        // If player has magic left and fighting an automaton, then...
        if (avatar.getMp() > 0 && enemyEnum.getValue_Category() == 
          HeroineEnum.EnemyCategoryEnum.ENEMY_CATEGORY_AUTOMATON)
        {
            
            // Player has magic left and fighting an automaton.
            
            // Remove shade to the button to signify currently enabled.
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setColor(Color.WHITE);
            
        }
        
        else
        {
            
            // Player has no magic left or fighting something other than an automaton.
            
            // Apply a dark shade to the button to signify not currently enabled.
            mapActionButtons.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK).setColor(COLOR_MED_GRAY);
            
            // Disable button.
            mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, false);
            
        }
        
        // 4.  Display selector around attack button.
        
        // Update position of selector.
        infoButtonSelector.setX(mapActionButtonPosX.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK) - 
          selectorAdjPos);
        infoButtonSelector.setY(mapActionButtonPosY.get(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK) - 
          selectorAdjPos);
        
        // Make selector visible.
        infoButtonSelector.setVisible(true);
        
        // 5.  Display hp and mp labels (in standard locations).
        
        // Display hp and mp labels.
        hpLabel.applyVisible(true);
        mpLabel.applyVisible(true);
        
        // 6.  Hide information button.
        infoButton.setVisible(false);
        
        // 7.  Hide facing label.
        facingLabel.applyVisible(false);
        
        // 8.  Hide region name label.
        regionLabel.applyVisible(false);
        
    }
    
    // enemyEnum = Type of enemy involved in combat.
    // enemy = ShakyActor object that acts as the enemy.
    // assetMgr = Reference to the asset manager class.
    private void render_enemy(HeroineEnum.EnemyEnum enemyEnum, ShakyActor enemy, AssetMgr assetMgr)
    {
        
        // The function renders the enemy at the start of the combat.
        // The enemy starts offscreen (centered vertically and to the left) and moves to the overall center.
        
        // Update enemy actor properties.
        enemy.updateShakyActor_Center(enemyEnum.toString(), 
          assetMgr.getImage_xRef(enemyEnum.getValue_ImageEnum().getValue_Key()), true);
        
        // Show the actor.
        enemy.setVisible(true);
        
    }
    
    // Getters and setters below...
    
    public HeroineEnum.EnemyEnum getEnemyEnum() {
        return enemyEnum;
    }

    public void setEnemyEnum(HeroineEnum.EnemyEnum enemyEnum) {
        this.enemyEnum = enemyEnum;
    }
    
}