package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;

// Local project imports.
import core.AssetMgr;
import core.BaseActor;
import core.ShakyActor;
import gui.CustomLabel;
import routines.UtilityRoutines;
import screens.ExploreScreen;

// Java imports.
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Combat
{
    
    /*
    The class stores information and logic related to combat.
    
    Action button enabled - hash map:
    1.  In function, render_interface_end, sets value to false for enumerations, ACTION_BUTTON_ATTACK and 
        ACTION_BUTTON_RUN.
    2.  In function, render_interface_start, sets value to true for enumerations, ACTION_BUTTON_ATTACK and 
        ACTION_BUTTON_RUN.
    3.  In function, render_interface_start, for spells known by player, ...
    3A.  Displays related button -- references hash map, mapActionButtons.
    3B.  If enabled, sets color to Color.WHITE.
    3C.  If disabled, sets color to COLOR_MED_GRAY.
    4.  In function, render_interface_start, for non-spell actions, displays related button.
    5.  In function, render_interface_start, when fighting something other than an automaton, sets value to 
        false for enumeration, ACTION_BUTTON_UNLOCK.
    
    Methods include:

    fight:  Encapsulates logic related to a round of combat.
    initiate_combat:  Initializes variables related to a new combat.
    offense_finish:  Encapsulates display logic related to the last segment of the player portion
      of a round of combat
    offense_start:  Encapsulates display logic related to the first segment of the player portion
      of a round of combat.
    render_enemy:  Renders the enemy at the start of the combat.
    render_interface_end:  Renders the interface for the ending of combat, including removing the enemy.
    render_interface_start:  Renders the interface for the beginning (and duration of) combat, except for 
      the enemy.
    */
    
    // Declare object variables.
    private ActionResult actionResultEnemy; // Result from (usually last) enemy action.
    private ActionResult actionResultPlayer; // Result from (usually last) player action.
    private final Avatar avatar; // Reference to player information class.
    private Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled_Start; // Hash map containing 
      // enabled status of action buttons.  Copy of values from start of current combat.
    private MazeMap mazemap; // Stores data for the current active region / map.
    
    // Declare regular variables.
    private boolean boneshield_active; // Whether bone shield power (of enemy) active.
    private HeroineEnum.CombatPhaseEnum combatPhase; // Current combat phase.
    private HeroineEnum.EnemyEnum enemyEnum; // Type of enemy involved in combat.
    private int enemyHp; // Current number of hit points of enemy.
    private HeroineEnum.FightEnum fightEnum; // Current player action.
    private final SecureRandom number; // Used for generating random numbers.
    @SuppressWarnings("FieldMayBeFinal")
    private int selectorAdjPos; // Position adjustment related to selector.
    private boolean shakeActiveInd; // Whether shake active.
    
    // Declare constants.
    private final Color COLOR_MED_GRAY = new Color(0.50f, 0.50f, 0.50f, 1);
    private final boolean DEBUG_RUN = true; // Set to true to cause run to be successful every time.
    
    // Constructors below...
    
    // avatar = Reference to player information class.
    // mazemap = Reference to data for the current active region / map.
    // scale = Output scale factor -- multiple of 160 and 120.
    public Combat(Avatar avatar, MazeMap mazemap, int scale)
    {
        
        // The constructor initializes the combat engine and stores the parameters in their related class
        // variables.
        
        // Initialize action result.
        actionResultEnemy = new ActionResult();
        actionResultPlayer = new ActionResult();
        
        // Initialize hash maps.
        mapActionButtonEnabled_Start = new HashMap<>();
        
        // Start random number generator.
        number = new SecureRandom();
        
        // Store reference to player information class.
        this.avatar = avatar;
        
        // Store reference to current active region data class.
        this.mazemap = mazemap;
        
        // Calculate selector position adjustment.
        this.selectorAdjPos = scale * 2;
        
    }
    
    // Methods below...
    
    // enemy = ShakyActor object that acts as the enemy.
    // enemyLabel = Reference to label showing enemy type.
    // infoButton = BaseActor object that acts as the information button.
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // facingLabel = Label showing direction player is facing.
    // powerSourceLabel = Label showing the source of the power (player or object).
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // powerSourceLabel_Enemy = Label showing the source of the power (enemy).
    // powerActionLabel_Enemy = Label showing the first line -- power action (enemy).
    // powerResultLabel_Enemy = Label showing the second line -- power result (enemy).
    // renderEndBtn = Whether to update interface for buttons.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapSelectorPosX = List of x-positions to place selector -- related to buttons.
    // mapSelectorPosY = List of y-positions to place selector -- related to buttons.
    public boolean conclude_combat(ShakyActor enemy, CustomLabel enemyLabel, BaseActor infoButton, 
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, CustomLabel facingLabel, 
      CustomLabel powerSourceLabel, CustomLabel powerActionLabel, CustomLabel powerResultLabel, 
      CustomLabel powerSourceLabel_Enemy, CustomLabel powerActionLabel_Enemy, 
      CustomLabel powerResultLabel_Enemy, boolean renderEndBtn, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosX,
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosY)
    {
        
        // The function encapsulates logic related to concluding combat.
        // Combat can end, for example, after defeating an enemy or successfully fleeing.
        
        // The function returns whether to enable action buttons and switch to explore mode after 
        // function finishes.
        
        boolean enableButtons; // Whether to enable action buttons and switch to explore mode after 
          // function finishes.
        
        // Flag to enable action buttons after function finishes.
        enableButtons = true;

         // Render the interface for the ending of combat.
        render_interface_end(enemy, enemyLabel, infoButton, infoButtonSelector, hpLabel, mpLabel, 
          facingLabel, powerSourceLabel, powerActionLabel, powerResultLabel, powerSourceLabel_Enemy,
          powerActionLabel_Enemy, powerResultLabel_Enemy, renderEndBtn, mapActionButtons, 
          mapActionButtonEnabled, mapSelectorPosX, mapSelectorPosY);

        // If player has remaining magic points, then...
        if (avatar.getMp() > 0)
        {

            // Player has remaining magic points.

            // Restore action buttons associated with spells.
            ExploreScreen.restoreButtons_Spell(mapActionButtons, mapActionButtonEnabled,
              mapActionButtonEnabled_Start);

        }
        
        // Return whether to enable action buttons and switch to explore mode after function finishes.
        return enableButtons;
        
    }
    
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapSelectorPosX = List of x-positions to place selector -- related to buttons.
    // mapSelectorPosY = List of y-positions to place selector -- related to buttons.
    public boolean defense_finish(BaseActor infoButtonSelector,
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons,
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled,
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosX,
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosY)
    {
        
        /*
        The function encapsulates display logic related to the last segment of the defensive portion
        of a round of combat.  The segment includes support for:
        
        1.  Checking for and handling a victory or a defeat.
        2.  Moving to the next combat phase -- COMBAT_PHASE_INPUT -- enemy and player both still alive.
        
        The function returns whether to enable the action buttons.
        */
        
        boolean enableButtons; // Whether to enable action buttons after function finishes.
        
        // Set defaults.
        enableButtons = false;
        
        // Flag shake as inactive.
        shakeActiveInd = false;
        
        // 1.  Handle hit points reaching zero -- enemy and, or player.
        // Phases:  COMBAT_PHASE_VICTORY, COMBAT_PHASE_DEFEAT.

        // If enemy defeated (at or below zero hit points) and player still alive, then...
        if ( enemyHp <= 0 && avatar.getHp() > 0 )
        {

            // Enemy defeated -- at or below zero hit points -- and player still alive.

            System.out.println("Defense Finish:  Enemy dead.");
            
            // Perform four immediate interface updates:
            // 1.  Hide actions buttons.
            // 2.  Disable attack and run buttons.
            // 3.  Move selector to information button.
            // 4.  Hide selector.
            render_interface_end_btn(infoButtonSelector, mapActionButtons, mapActionButtonEnabled,
              mapSelectorPosX, mapSelectorPosY);
            
            // Move to victory phase.
            combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_VICTORY;

        }

        // Otherwise, if player dead, then...
        else if ( avatar.getHp() <= 0 )
        {

            // Player dead.

            System.out.println("Defense Finish:  Player dead.");
            
            // Move to defeat phase.
            combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_DEFEAT;

            // To Do:  Add defeat logic.

        }
        
        // Otherwise
        else
        {
            
            // Neither enemy nor player dead.
            
            System.out.println("Defense Finish:  Neither enemy nor player dead.");
            
            // Move to input phase.
            combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_INPUT;
            
            // Flag to enable buttons.
            enableButtons = true;
            
        }
        System.out.println("Defense finish: " + enableButtons);
        // Return whether to enable buttons.
        return enableButtons;
        
    }
    
    // powerSourceLabel_Enemy = Label showing the source of the power (enemy).
    // powerActionLabel_Enemy = Label showing the first line -- power action (enemy).
    // powerResultLabel_Enemy = Label showing the second line -- power result (enemy).
    // tileGroup = ShakyActor object that will act as the group containing the tiles.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    private void defense_start(CustomLabel powerSourceLabel_Enemy, CustomLabel powerActionLabel_Enemy, 
      CustomLabel powerResultLabel_Enemy, ShakyActor tileGroup, CustomLabel hpLabel, CustomLabel mpLabel)
    {
        
        // The function encapsulates display logic related to the first segment of the enemy portion
        // of a round of combat.  The first segment includes setting the power labels and (possibly)
        // shaking the tiles.
        
        // Note that the variables used to set the text of the labels get set in the parent function.
        
        System.out.println("Function: defense_start");
        
        // Store next combat phase.
        combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_DEFENSE;
        
        // Display results of enemy actions in power labels.
        ExploreScreen.info_update_powerResponseLines(actionResultEnemy, powerSourceLabel_Enemy, 
          powerActionLabel_Enemy, powerResultLabel_Enemy, false);
        
        // If player damaged, then...
        if (actionResultEnemy.getOpponentHurt())
        {
            
            // Player damaged.
            
            // Shake the tiles five times to indicate enemy caused damage to player.
            tileGroup.startShake(5);
            
            // Update the hit and magic points of the player.
            hpLabel.setLabelText( avatar.getHpText() );
            mpLabel.setLabelText( avatar.getMpText() );
            
            // Flag shake as active.
            shakeActiveInd = true;
            
        }
        
    }
    
    // sounds = Reference to sounds class.
    private void enemy_attack(Sounds sounds)
    {
        
        /*
        The function encapsulates non-visual logic related to the enemy attacking the player.
        The function excludes visual results.
        
        The function updates the action results related to the enemy-related labels.
        The function does not update the enemy-related labels or shake the screen.
        
        Other functions handle:
        1.  The visual results of the enemy action, including shaking the screen.
        2.  Victory conditions.
        3.  Loss conditions.
        */
        
        int attack_damage; // Amount of damage caused by enemy in current round.
        int crit_chance; // Whether enemy caused a critical hit to the player.
        int hit_chance; // Random number indicating whether enemy hit player.
        
        HeroineEnum.EnemyPowerEnum enemyPower; // Type of enemy attack.
        
        // 1.  Determine type of enemy attack.
        enemyPower = enemyEnum.getValue_RandomPower(number);
        
        // 2.  Use enemy "power" against player.
        
        // Depending on enemy power used, ...
        switch (enemyPower) {
            
            case ENEMY_POWER_ATTACK:
                
                // Enemy performs standard physical attack.
                
                // "Roll" to determine if enemy hit player -- 1 to 100.
                hit_chance = UtilityRoutines.generateStandardRnd(number, 1, 100);

                // If miss occurred, then...
                if (hit_chance <= 30)
                {

                    // Miss occurred.

                    // Set action text to ATTACK!.
                    actionResultEnemy.setTextAction(HeroineEnum.PowerActionEnum.POWER_ACTION_ATTACK);
                    
                    // Store defense result.
                    actionResultEnemy.setTextResult(HeroineEnum.PowerResultEnum.POWER_RESULT_MISS);
                    
                    // Play miss sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_MISS);

                } // End ... If miss occurred.
                
                else
                
                // Otherwise, ...
                {
                    
                    // Enemy successfully hit player.
                    
                    // Flag player as hit.
                    actionResultEnemy.setOpponentHurt(true);

                    // Randomly determine damage based on enemy minimum and maximum.
                    attack_damage = UtilityRoutines.generateStandardRnd(number, enemyEnum.getValue_AtkMin(), 
                      enemyEnum.getValue_AtkMax());

                    // Check for critical hit.
                    // A critical hit adds the minimum possible damage (ignoring bonuses) to what
                    // was already "rolled".

                    // "Roll" for critical hit.
                    crit_chance = UtilityRoutines.generateStandardRnd(number, 1, 100);

                    // If critical hit occurred, then...
                    if (crit_chance <= 5)
                    {

                        // Critical hit occurred.

                        // Add minimum possible damage to current.
                        attack_damage += enemyEnum.getValue_AtkMin();

                        // Set action text to CRITICAL!.
                        actionResultEnemy.setTextAction(HeroineEnum.PowerActionEnum.POWER_ACTION_CRITICAL);

                        // Play critical hit sound.
                        sounds.playSound(HeroineEnum.SoundEnum.SOUND_CRITICAL);

                    }

                    else
                    {

                        // Normal hit occured.

                        // Set action text to ATTACK!.
                        actionResultEnemy.setTextAction(HeroineEnum.PowerActionEnum.POWER_ACTION_ATTACK);
                        
                        // Play normal hit sound.
                        sounds.playSound(HeroineEnum.SoundEnum.SOUND_ATTACK);

                    }

                    // Allow for player armor to absorb some of the damage.
                    attack_damage -= avatar.getArmor().getValue_Def();
                    attack_damage -= avatar.getBonus_def();

                    // Ensure that at least one hit point of damage occurs.
                    if (attack_damage < 1)
                        attack_damage = 1;
                    
                    // Reduce player hit points.
                    avatar.adjHp(-attack_damage);
                    
                    // Store defense result.
                    actionResultEnemy.setTextResult_Damage(attack_damage);
                
                } // End ... If hit occurred.
                
                // Exit selector.
                break;
                
            case ENEMY_POWER_SCORCH:
                
                // Enemy performs scorch attack (another version of burn).
                
                // Set action text to SCORCH!.
                actionResultEnemy.setTextAction(HeroineEnum.PowerActionEnum.POWER_ACTION_SCORCH);
                
                // "Roll" to determine if enemy hit player -- 1 to 100.
                hit_chance = UtilityRoutines.generateStandardRnd(number, 1, 100);

                // If miss occurred, then...
                if (hit_chance <= 30)
                {

                    // Miss occurred.
                    
                    // Store defense result.
                    actionResultEnemy.setTextResult(HeroineEnum.PowerResultEnum.POWER_RESULT_MISS);

                    // Play miss sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_MISS);

                } // End ... If miss occurred.
                
                else
                
                // Otherwise, ...
                {
                    
                    // Enemy successfully hit player.
                    
                    // Play fire sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_FIRE);
                    
                    // Flag player as hit.
                    actionResultEnemy.setOpponentHurt(true);

                    // Randomly determine damage based on enemy minimum and maximum.
                    attack_damage = UtilityRoutines.generateStandardRnd(number, enemyEnum.getValue_AtkMin(), 
                      enemyEnum.getValue_AtkMax());

                    // Scorch always causes a critical hit.
                    
                    // Add minimum possible damage to current.
                    attack_damage += enemyEnum.getValue_AtkMin();
                    
                    // Allow for player armor to absorb some of the damage.
                    attack_damage -= avatar.getArmor().getValue_Def();
                    attack_damage -= avatar.getBonus_def();

                    // Ensure that at least one hit point of damage occurs.
                    if (attack_damage < 1)
                        attack_damage = 1;
                    
                    // Reduce player hit points.
                    avatar.adjHp(-attack_damage);

                    // Store defense result.
                    actionResultEnemy.setTextResult_Damage(attack_damage);
                
                } // End ... If hit occurred.
                
                // Exit selector.
                break;
                
            case ENEMY_POWER_HPDRAIN:
                
                // Enemy performs hit point drain attack.
                
                // Set action text to HP DRAIN!.
                actionResultEnemy.setTextAction(HeroineEnum.PowerActionEnum.POWER_ACTION_HP_DRAIN);
                
                // "Roll" to determine if enemy hit player -- 1 to 100.
                hit_chance = UtilityRoutines.generateStandardRnd(number, 1, 100);

                // If miss occurred, then...
                if (hit_chance <= 30)
                {

                    // Miss occurred.

                    // Store defense result.
                    actionResultEnemy.setTextResult(HeroineEnum.PowerResultEnum.POWER_RESULT_MISS);

                    // Play miss sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_MISS);

                } // End ... If miss occurred.
                
                else
                
                // Otherwise, ...
                {
                    
                    // Enemy successfully hit player.
                    
                    // Play hit point drain sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_HP_DRAIN);
                    
                    // Flag player as hit.
                    actionResultEnemy.setOpponentHurt(true);

                    // Randomly determine damage based on enemy minimum and maximum.
                    attack_damage = UtilityRoutines.generateStandardRnd(number, enemyEnum.getValue_AtkMin(), 
                      enemyEnum.getValue_AtkMax());

                    // Hit point drain never causes a critical hit.
                    
                    // Allow for player armor to absorb some of the damage.
                    attack_damage -= avatar.getArmor().getValue_Def();
                    attack_damage -= avatar.getBonus_def();

                    // Ensure that at least one hit point of damage occurs.
                    if (attack_damage < 1)
                        attack_damage = 1;
                    
                    // Reduce player hit points.
                    avatar.adjHp(-attack_damage);

                    // Add to enemy hit points.
                    enemyHp += attack_damage;
                    
                    // Store defense result.
                    actionResultEnemy.setTextResult_Damage(attack_damage);
                
                } // End ... If hit occurred.
                
                // Exit selector.
                break;
                
            case ENEMY_POWER_MPDRAIN:
                
                // power_mpdrain();
                
                // Enemy performs magic point drain attack.
                
                // Set action text to MP DRAIN!.
                actionResultEnemy.setTextAction(HeroineEnum.PowerActionEnum.POWER_ACTION_MP_DRAIN);
                
                // "Roll" to determine if enemy hit player -- 1 to 100.
                hit_chance = UtilityRoutines.generateStandardRnd(number, 1, 100);

                // If miss occurred, then...
                if (hit_chance <= 30)
                {

                    // Miss occurred.

                    // Store defense result.
                    actionResultEnemy.setTextResult(HeroineEnum.PowerResultEnum.POWER_RESULT_MISS);

                    // Play miss sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_MISS);

                } // End ... If miss occurred.
                
                else
                
                // Otherwise, ...
                {
                    
                    // Enemy successfully hit player.
                    
                    // Play magic point drain sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_MP_DRAIN);
                    
                    // Flag player as hit.
                    actionResultEnemy.setOpponentHurt(true);

                    // If player has one or more magic points, then...
                    if (avatar.getMp() > 0)
                    {
                        
                        // Player has one or more magic points.
                        
                        // Reduce player magic points by one.
                        avatar.decrement_mp();
                        
                        // Store defense result.
                        actionResultEnemy.setTextResult("-1 MP");
                        
                    }
                    
                    else
                    {
                        
                        // Player has no remaining magic points.
                        
                        // Store defense result.
                        actionResultEnemy.setTextResult("No effect");
                        
                    }
                
                } // End ... If hit occurred.
                
                // Exit selector.
                break;
                
            default:
                
                // Unknown enemy power.
                
                // Display warning.
                System.out.println("Warning:  Enemy used unknown power!");
                
                // Exit selector.
                break;
            
        } // End ... Depending on enemy power used.
        
    }
    
    // fightEnum = Enumerated value related to player fighting action.
    // sounds = Reference to sounds class.
    // avatar = Reference to player class.
    // powerSourceLabel = Label showing the source of the power (player or object).
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // buttonActor = Reference to BaseActor for the button.
    // enemy = ShakyActor object that will act as the enemy.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // damage = Damage caused by action before calling method (usually base amount).
    public boolean fight(HeroineEnum.FightEnum fightEnum, Sounds sounds, Avatar avatar, 
      CustomLabel powerSourceLabel, CustomLabel powerActionLabel, CustomLabel powerResultLabel, 
      CustomLabel hpLabel, CustomLabel mpLabel, BaseActor buttonActor, ShakyActor enemy,
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons,
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, Integer damage)
    {
        
        /*
        The function encapsulates logic related to the player action in a round of combat.
        The function excludes visual results.
        
        The function returns whether to enable the action buttons immediately.
        Enabling of action buttons immediately occurs, for example, when a spell cannot be cast.
        
        Other functions handle:
        1.  The visual results of the player action, including shaking the enemy.
        2.  Enemy action (and related display and consequences).
        3.  Victory conditions.
        4.  Loss conditions.
        */
        
        int atk_max; // Maximum damage caused by player.
        int atk_min; // Minimum damage caused by player.
        int attack_damage; // Amount of damage caused by player in current round.
        boolean boneshieldInd; // Whether bone shield deflected player attack.
        int crit_chance; // Whether player caused a critical hit to the enemy.
        boolean enableButtons; // Whether to enable action buttons after function finishes.
        int hit_chance; // Random number indicating whether player hit enemy.
        boolean proceedInd; // Whether to proceed to enemy attack and next round (or run).
        int run_chance; // Random number indicating whether player successfully ran away from enemy.
        boolean run_success; // Whether run attempt successful.
        
        // Set defaults.
        run_success = false;
        proceedInd = false;
        boneshieldInd = false;
        enableButtons = false;
        
        // Clear action results.
        actionResultEnemy.clear();
        actionResultPlayer.clear();
        
        // Set defaults for action results.
        actionResultEnemy.setTextSource("ENEMY:");
        actionResultPlayer.setTextSource("YOU:");
        
        // Start with offense action text based on player fighting action.
        actionResultPlayer.setTextAction(fightEnum.getValue_Desc());
        
        // Move to beginning of new round of combat.
        combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_INPUT;
        
        // Store current player action.
        this.fightEnum = fightEnum;
        
        // Depending on player fighting action, ...
        switch (fightEnum) {
            
            case FIGHT_ATTACK:
                
                // Player attacked enemy.
                
                // If enemy bone shield active, then...
                if (boneshield_active)
                {
                    
                    // Enemy bone shield active.
                    // Override hero action when the enemy has the bone shield up.
                    
                    // Play blocked sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_BLOCKED);
                    
                    // Flag bone shield as deflecting player attack.
                    boneshieldInd = true;
                    
                    // Populate action result with absorbed message.
                    actionResultPlayer.setTextResult(HeroineEnum.PowerResultEnum.POWER_RESULT_ABSORBED);
                    
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
                        actionResultPlayer.setTextResult(HeroineEnum.PowerResultEnum.POWER_RESULT_MISS);
                        
                        // Play miss sound.
                        sounds.playSound(HeroineEnum.SoundEnum.SOUND_MISS);
                        
                    } // End ... If miss occurred.
                    
                    // Otherwise, ... > Hit occurred.
                    else
                    {
                        
                        // Hit occurred.
                        
                        // Flag enemy as hit.
                        actionResultPlayer.setOpponentHurt(true);
                        
                        // Store minimum and maximum damage caused by player.
                        atk_min = avatar.getMinDamage();
                        atk_max = avatar.getMaxDamage();
                        
                        // Randomly determine damage based on player minimum and maximum.
                        attack_damage = UtilityRoutines.generateStandardRnd(number, atk_min, atk_max);
                        
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
                            actionResultPlayer.setTextAction(HeroineEnum.PowerActionEnum.POWER_ACTION_CRITICAL);
                            
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
                        actionResultPlayer.setTextResult_Damage(attack_damage);
                        
                    } // End ... If hit occurred.
                    
                } // End ... If enemy bone shield NOT active.
                
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
                if (run_chance <= 66 || DEBUG_RUN)
                {
                    
                    // Player successfully ran away from enemy.
                    
                    // Flag run attempt as successful.
                    run_success = true;
                    actionResultPlayer.setResult(true);

                    
                    // Store offense result.
                    actionResultPlayer.setTextResult(HeroineEnum.PowerResultEnum.POWER_RESULT_RUN_SUCCESSFUL);
                    
                }
                
                else
                {
                    
                    // Player failed to run away from enemy.
                    
                    // Flag run attempt as a failure.
                    run_success = false;
                    actionResultPlayer.setResult(false);
                    
                    // Store offense result.
                    actionResultPlayer.setTextResult(HeroineEnum.PowerResultEnum.POWER_RESULT_BLOCKED);
                    
                }
                
                // Flag to proceed to enemy attack and next round (or run).
                proceedInd = true;
                
                // Exit selector.
                break;
                
            case FIGHT_HEAL:
                
                // Player cast healing spell.
                
                // Cast heal spell.
                Spells.cast_heal(avatar, true, powerActionLabel, powerResultLabel, hpLabel, 
                  mpLabel, buttonActor, mapActionButtons, mapActionButtonEnabled, sounds, 
                  actionResultPlayer);
                
                // If player able to cast heal spell, then...
                if (actionResultPlayer.getResult())
                {
                    
                    // Player able to cast heal spell.
                    
                    // Flag to proceed to enemy attack and next round.
                    proceedInd = true;
                    
                }
                
                // Exit selector.
                break;
                
            case FIGHT_BURN:
                
                // Player cast burn spell.
                
                // Cast burn spell and return base damage.
                Spells.cast_burn(avatar, null, true, false, null, powerActionLabel, 
                  powerResultLabel, mpLabel, null, null, buttonActor, mapActionButtons,
                  mapActionButtonEnabled, number, sounds, actionResultPlayer);
                
                // If burn spell successful (fails when player has insufficient mp), then...
                if (actionResultPlayer.getResult())
                {
                    
                    // Burn spell successful.
                    
                    // Store base attack damage.
                    attack_damage = actionResultPlayer.getResultNumeric(); //damage;

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
                    actionResultPlayer.setTextResult_Damage(attack_damage);

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
                    
                } // End .... If burn spell successful.
                
                // Exit selector.
                break;
                
            case FIGHT_UNLOCK:
                
                // Player cast unlock spell.
                
                // If enemy category is automaton, then...
                if (enemyEnum.getValue_Category() == HeroineEnum.EnemyCategoryEnum.ENEMY_CATEGORY_AUTOMATON)
                {
                    
                    // Enemy category is automaton.
                    
                    // Cast unlock spell and return base damage.
                    Spells.cast_unlock(avatar, null, true, false, null, powerActionLabel, 
                      powerResultLabel, mpLabel, null, null, buttonActor, mapActionButtons,
                      mapActionButtonEnabled, number, sounds, actionResultPlayer);
                    
                    // Store base attack damage.
                    attack_damage = actionResultPlayer.getResultNumeric(); //damage;
                    
                    // Store maximum damage caused by player.
                    atk_max = avatar.getMaxDamage();
                    
                    // Add 2x maximum to damage to cause to enemy.
                    attack_damage += atk_max += atk_max;
                    
                    // Reduce enemy hit points.
                    enemyHp -= attack_damage;

                    // Store offense result.
                    actionResultPlayer.setTextResult_Damage(attack_damage);
                    
                    // Flag to proceed to enemy attack and next round.
                    proceedInd = true;
                    
                } // End ... If enemy category is automaton.
                
                else
                {
                    
                    // Enemy category other than automaton.
                    
                    // Play error sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_BLOCKED);
                    
                    // Populate action result with no target message.
                    actionResultPlayer.setResult(false);
                    actionResultPlayer.setResultNumeric(-1);
                    actionResultPlayer.setTextAction(HeroineEnum.PowerActionEnum.POWER_ACTION_NO_TARGET);
                    
                } // End ... If enemy category other than automaton.
                
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
            
        // If proceeding to next phase (offense) in current combat round, then...
        if (proceedInd)
        {
            
            // Proceed to next phase (offense) in current combat round.
            
            // Call function to handle logic related to start of offense phase.
            // The function displays the results of the player attack, shaking the enemy if hit.
            offense_start(powerSourceLabel, powerActionLabel, powerResultLabel, enemy);
            
        }
        
        else
        {
            
            // Stay in current phase (input).
            
            // Display results of player actions in power labels.
            ExploreScreen.info_update_powerResponseLines(actionResultPlayer, powerSourceLabel, 
              powerActionLabel, powerResultLabel, false);
            
            // Flag to enable action buttons after function finishes.
            enableButtons = true;
            
        }
        
        // Return whether to enable action buttons after function finishes.
        return enableButtons;
        
    }
    
    // enemyEnum = Type of enemy involved in combat.
    // enemyLabel = Reference to label showing enemy type.
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // infoButton = BaseActor object that acts as the information button.
    // facingLabel = Label showing direction player is facing.
    // regionLabel = Label showing the current region name.
    // powerSourceLabel = Label showing the source of the power (player or object).
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // powerSourceLabel_Enemy = Label showing the source of the power (enemy).
    // powerActionLabel_Enemy = Label showing the first line -- power action (enemy).
    // powerResultLabel_Enemy = Label showing the second line -- power result (enemy).
    // enemy = ShakyActor object that acts as the enemy.
    // assetMgr = Reference to the asset manager class.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapActionButtonPosX = X-coordinate of each action button.
    // mapActionButtonPosY = Y-coordinate of bottom of each action button.
    public void initiate_combat(HeroineEnum.EnemyEnum enemyEnum, CustomLabel enemyLabel, 
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, BaseActor infoButton, 
      CustomLabel facingLabel, CustomLabel regionLabel, CustomLabel powerSourceLabel, 
      CustomLabel powerActionLabel, CustomLabel powerResultLabel, CustomLabel powerSourceLabel_Enemy, 
      CustomLabel powerActionLabel_Enemy, CustomLabel powerResultLabel_Enemy, ShakyActor enemy, 
      AssetMgr assetMgr, Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosX,
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosY)
    {
        
        // The function initializes variables related to a new combat.
        
        // Set defaults.
        combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_INTRO;
        boneshield_active = false;
        shakeActiveInd = false;
        
        System.out.println("Encountered enemy of type, " + enemyEnum + ".");
        
        // Store passed values.
        this.enemyEnum = enemyEnum;
        
        // Set starting number of hit points of enemy.
        this.enemyHp = enemyEnum.getValue_HP();
        
        // Clear hash map with enabled statuses of action buttons.
        mapActionButtonEnabled_Start.clear();
        
        // Store starting enabled statuses of action buttons.
        mapActionButtonEnabled_Start.putAll(mapActionButtonEnabled);
        
        // Render starting interface.
        render_interface_start(enemyEnum, enemyLabel, infoButtonSelector, hpLabel, mpLabel, infoButton, 
          facingLabel, regionLabel, powerSourceLabel, powerActionLabel, powerResultLabel, 
          powerSourceLabel_Enemy, powerActionLabel_Enemy, powerResultLabel_Enemy, mapActionButtons, 
          mapActionButtonEnabled, mapActionButtonPosX, mapActionButtonPosY);
        
        // Render enemy.
        render_enemy(enemyEnum, enemy, assetMgr);
        
    }
    
    // enemy = ShakyActor object that acts as the enemy.
    // enemyLabel = Reference to label showing enemy type.
    // infoButton = BaseActor object that acts as the information button.
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // facingLabel = Label showing direction player is facing.
    // regionLabel = Label showing the current region name.
    // powerSourceLabel = Label showing the source of the power (player or object).
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // powerSourceLabel_Enemy = Label showing the source of the power (enemy).
    // powerActionLabel_Enemy = Label showing the first line -- power action (enemy).
    // powerResultLabel_Enemy = Label showing the second line -- power result (enemy).
    // renderEndBtn = Whether to update interface for buttons.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapSelectorPosX = List of x-positions to place selector -- related to buttons.
    // mapSelectorPosY = List of y-positions to place selector -- related to buttons.
    private void render_interface_end(ShakyActor enemy, CustomLabel enemyLabel, BaseActor infoButton,
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, CustomLabel facingLabel, 
      CustomLabel powerSourceLabel, CustomLabel powerActionLabel, CustomLabel powerResultLabel, 
      CustomLabel powerSourceLabel_Enemy, CustomLabel powerActionLabel_Enemy, 
      CustomLabel powerResultLabel_Enemy, boolean renderEndBtn, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosX,
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosY)
    {
        
        /*
        The function renders the interface for the ending of combat, including removing the enemy.
        
        1.  Fade out the enemy name.
        2.  Fade out the enemy.
        3.  (Optional) Hide action buttons.  Disable attack and run buttons.  Move selector to information 
            button.  Hide selector.
        4.  Fade out the hp and mp labels (in standard locations).
        5.  Show the information button after slight delay.
        6.  Show the facing label after slight delay.
        
        When ending combat in victory or defeat, ...
        7.  Fade out the player power labels.
        8.  Fade out the enemy power labels.
        
        When ending combat due to fleeing successfully, ...
        7.  Hide player power labels.
        8.  Hide enemy power labels.
        */
        
        // 1.  Fade out the enemy name.
        
        // Remove existing actions and fade out label over the course of 0.50 seconds.
        enemyLabel.removeActions();
        enemyLabel.addAction_FadeOut(0.00f, 0.50f);
        
        // 2.  Fade out the enemy.
        
        // Remove existing actions and fade out enemy (actor) over the course of 0.50 seconds.
        enemy.removeActions();
        enemy.addAction_FadeOut(0.00f, 0.50f);
        
        // 3.  (Optional) Hide attack, run, and other action buttons.  Update enabled status of attack and run.
        
        // If updating interface for buttons, then...
        if (renderEndBtn)
        {
            
            // Update interface for buttons.
            render_interface_end_btn(infoButtonSelector, mapActionButtons, mapActionButtonEnabled, 
              mapSelectorPosX, mapSelectorPosY);
            
        }
        
        // 4.  Fade out hp and mp labels (in standard locations).
        
        // Remove existing actions and fade out labels over the course of 0.50 seconds.
        hpLabel.removeActions();
        hpLabel.addAction_FadeOut(0.00f, 0.50f);
        mpLabel.removeActions();
        mpLabel.addAction_FadeOut(0.00f, 0.50f);
        
        // 5.  Show the information button after 0.50 seconds elapse.
        infoButton.removeActions();
        infoButton.addAction_FadeIn(0.50f, 0.00f);
        
        // Remove existing actions and fade in the information button.
        
        // 6.  Show the facing label after 0.50 seconds elapse.
        
        // Remove existing actions and show the facing label after 0.50 seconds elapse.
        facingLabel.removeActions();
        facingLabel.addAction_FadeIn(0.50f, 0.00f);
        
        // If running away from enemy successfully, then...
        if (combatPhase == HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_RUN)
        {
            
            // Running away from enemy successfully.
            
            // 7.  Hide player power labels.
            powerSourceLabel.applyVisible(false);
            powerActionLabel.applyVisible(false);
            powerResultLabel.applyVisible(false);

            // 8.  Hide enemy power labels.
            powerSourceLabel_Enemy.applyVisible(false);
            powerActionLabel_Enemy.applyVisible(false);
            powerResultLabel_Enemy.applyVisible(false);
            
        }
        
        else
        {
            
            // Victory or defeat occurred.
            
            // 7.  Fade out the player power labels.
        
            // Remove existing actions and fade out labels over the course of 1.00 seconds.
            // Wait 1.00 seconds before starting the fade.
            powerSourceLabel.removeActions();
            powerSourceLabel.addAction_FadeOut(2.00f, 1.00f);
            powerActionLabel.removeActions();
            powerActionLabel.addAction_FadeOut(2.00f, 1.00f);
            powerResultLabel.removeActions();
            powerResultLabel.addAction_FadeOut(2.00f, 1.00f);

            // 8.  Fade out the enemy power labels.

            // Remove existing actions and fade out labels over the course of 1.00 seconds.
            // Wait 1.00 seconds before starting the fade.
            powerSourceLabel_Enemy.removeActions();
            powerSourceLabel_Enemy.addAction_FadeOut(2.00f, 1.00f);
            powerActionLabel_Enemy.removeActions();
            powerActionLabel_Enemy.addAction_FadeOut(2.00f, 1.00f);
            powerResultLabel_Enemy.removeActions();
            powerResultLabel_Enemy.addAction_FadeOut(2.00f, 1.00f);
            
        } // End ... If victory or defeat occurred.
        
    }
    
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapSelectorPosX = List of x-positions to place selector -- related to buttons.
    // mapSelectorPosY = List of y-positions to place selector -- related to buttons.
    private void render_interface_end_btn(BaseActor infoButtonSelector, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons,
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled,
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosX,
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosY)
    {
        
        // The function performs display-related logic related to buttons at the conclusion of combat.
        
        // 1.  Hide attack, run, and other action buttons.
        // 2.  Disable attack and run buttons.
        // 3.  Move selector to information button.  Hide selector.
        
        HeroineEnum.ActionButtonEnum actionButtonEnum; // Enumeration value related to current action button 
          // in loop.
        Set<Map.Entry<HeroineEnum.ActionButtonEnum, BaseActor>> entrySetActionButtons; // Set view of the 
          // action buttons in the hash map.
        
        // 1.  Disable attack and run buttons.
        mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK, false);
        mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN, false);
        
        // 2.  Hide action buttons.
        
        // Store a set view of the action button mappings for the hash map.
        entrySetActionButtons = mapActionButtons.entrySet();
        
        // Loop through action buttons.
        for (Map.Entry entryActionButton : entrySetActionButtons)
        {
            
            // Store enumeration value for current action button in loop.
            actionButtonEnum = (HeroineEnum.ActionButtonEnum)entryActionButton.getKey();
            
            // Hide current button.
            mapActionButtons.get(actionButtonEnum).setVisible(false);
            
        } // End ... Loop through action buttons.
        
        // 3.  Move selector to information button.  Hide selector.
        
        // Position the selector around the information button.
        infoButtonSelector.setPosition(mapSelectorPosX.get(HeroineEnum.SelectPosEnum.BUTTON_POS_INFO), 
          mapSelectorPosY.get(HeroineEnum.SelectPosEnum.BUTTON_POS_INFO));
        
        // Hide selector.
        infoButtonSelector.setVisible(false);
        
    }
    
    // enemyEnum = Type of enemy involved in combat.
    // enemyLabel = Reference to label showing enemy type.
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // infoButton = BaseActor object that acts as the information button.
    // facingLabel = Label showing direction player is facing.
    // regionLabel = Label showing the current region name.
    // powerSourceLabel = Label showing the source of the power (player or object).
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // powerSourceLabel_Enemy = Label showing the source of the power (enemy).
    // powerActionLabel_Enemy = Label showing the first line -- power action (enemy).
    // powerResultLabel_Enemy = Label showing the second line -- power result (enemy).
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapActionButtonPosX = X-coordinate of each action button.
    // mapActionButtonPosY = Y-coordinate of bottom of each action button.
    private void render_interface_start(HeroineEnum.EnemyEnum enemyEnum, CustomLabel enemyLabel, 
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, BaseActor infoButton, 
      CustomLabel facingLabel, CustomLabel regionLabel, CustomLabel powerSourceLabel, 
      CustomLabel powerActionLabel, CustomLabel powerResultLabel, CustomLabel powerSourceLabel_Enemy, 
      CustomLabel powerActionLabel_Enemy, CustomLabel powerResultLabel_Enemy,
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosX,
      Map<HeroineEnum.ActionButtonEnum, Float> mapActionButtonPosY)
    {
        
        /*
        The function renders the interface for the beginning (and duration of) combat, except for the enemy.
        
        1.  Display enemy name, centered horizontally, slight below top of screen.
        2.  Display attack, run, and other action buttons.  Update shading.
        3.  Special case:  Automaton.
        4.  Display selector around attack button.
        5.  Display hp and mp labels (in standard locations).
        6.  Hide information button.
        7.  Hide facing label.
        8.  Hide region name label.
        9.  Remove actions from player power labels.
        10.  Displays enemy power labels.
        11.  Reset shading (to none) for power labels, to eliminate issues with fading out.
        12.  Clear power labels.
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
            
            // If current action is for a spell, then...
            if (actionButtonEnum.getValue_SpellInd())
            {
                
                // Current action is for a spell.
                
                // Store spell enumeration value related to current action button in loop.
                spellEnum = HeroineEnum.SpellEnum.valueOf(actionButtonEnum);
                
                // If player can cast spell related to current action button in loop, then...
                if (avatar.getSpellCastInd(spellEnum))
                {

                    // Player can cast spell related to current action button in loop.

                    // If player has sufficient magic points to cast spell, then...
                    if (avatar.getMp() > 0)
                    {
                        
                        // Player has sufficient magic points to cast spell.
                        
                        // Enable action button.
                        mapActionButtonEnabled.put(actionButtonEnum, true);
                        
                    }
                    
                    // Display related button -- even if disabled.
                    mapActionButtons.get(actionButtonEnum).setVisible(true);

                    // If current button enabled, then...
                    if (mapActionButtonEnabled.get(actionButtonEnum))
                    {

                        // Current button enabled.

                        // Set button to normal color.
                        mapActionButtons.get(actionButtonEnum).setColor(Color.WHITE);

                    }

                    else
                    {

                        // Current button disabled.
                        mapActionButtons.get(actionButtonEnum).setColor(COLOR_MED_GRAY);

                    }

                } // End ... If player can cast spell related to current action button in loop.
                
            } // End ... If current action is for a spell.
            
            else
            {
                
                // Current action is NOT for a spell.
                // Used for attack, run, ...
                
                // Restore shading of current button to normal (white).
                mapActionButtons.get(actionButtonEnum).setColor(Color.WHITE);
                
                // Display related button -- even if disabled.
                mapActionButtons.get(actionButtonEnum).setVisible(true);
                
            }
            
        } // End ... Loop through action buttons.
        
        // 3.  Special case:  Unlock button.
        
        // If player fighting an enemy other than an automaton, then...
        if (enemyEnum.getValue_Category() != HeroineEnum.EnemyCategoryEnum.ENEMY_CATEGORY_AUTOMATON)
        {
            
            // Player fighting something other than an automaton.
            
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
        
        // 9.  Remove actions from player power labels.
        powerSourceLabel.removeActions();
        powerActionLabel.removeActions();
        powerResultLabel.removeActions();
        
        // 10.  Display enemy power labels.
        powerSourceLabel_Enemy.applyVisible(true);
        powerActionLabel_Enemy.applyVisible(true);
        powerResultLabel_Enemy.applyVisible(true);
        
        // 11.  Reset shading (to none) for power labels, to eliminate issues with fading out.
        powerSourceLabel.getLabel().setColor(Color.WHITE);
        powerActionLabel.getLabel().setColor(Color.WHITE);
        powerResultLabel.getLabel().setColor(Color.WHITE);
        powerSourceLabel_Enemy.getLabel().setColor(Color.WHITE);
        powerActionLabel_Enemy.getLabel().setColor(Color.WHITE);
        powerResultLabel_Enemy.getLabel().setColor(Color.WHITE);
        
        // 12.  Clear power labels.
        powerSourceLabel.setLabelText("");
        powerActionLabel.setLabelText("");
        powerResultLabel.setLabelText("");
        powerSourceLabel_Enemy.setLabelText("");
        powerActionLabel_Enemy.setLabelText("");
        powerResultLabel_Enemy.setLabelText("");
        
    }
    
    // enemy = ShakyActor object that acts as the enemy.
    // enemyLabel = Reference to label showing enemy type.
    // tileGroup = ShakyActor object that will act as the group containing the tiles.
    // infoButton = BaseActor object that acts as the information button.
    // infoButtonSelector = BaseActor object that acts as the selector for the current action button.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // facingLabel = Label showing direction player is facing.
    // powerSourceLabel = Label showing the source of the power (player or object).
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // powerSourceLabel_Enemy = Label showing the source of the power (enemy).
    // powerActionLabel_Enemy = Label showing the first line -- power action (enemy).
    // powerResultLabel_Enemy = Label showing the second line -- power result (enemy).
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapSelectorPosX = List of x-positions to place selector -- related to buttons.
    // mapSelectorPosY = List of y-positions to place selector -- related to buttons.
    // sounds = Reference to sounds class.
    public boolean offense_finish(ShakyActor enemy, CustomLabel enemyLabel, ShakyActor tileGroup,
      BaseActor infoButton, BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, 
      CustomLabel facingLabel, CustomLabel powerSourceLabel, CustomLabel powerActionLabel, 
      CustomLabel powerResultLabel, CustomLabel powerSourceLabel_Enemy, CustomLabel powerActionLabel_Enemy, 
      CustomLabel powerResultLabel_Enemy, Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosX,
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosY,
      Sounds sounds)
    {
        
        /*
        The function encapsulates display logic related to the last segment of the offensive portion
        of a round of combat.  The segment includes support for:
        
        1.  Processing an enemy attack -- occurs when not running away or flee fails.
        2.  Successfully running away.
        3.  Concluding combat without a victory -- due to successfully fleeing the enemy.
        
        The function returns whether to enable the action buttons and switch to explore mode.
        */

        boolean basicFinishInd; // Whether to perform basic logic related to conclusion of combat.
        boolean enableButtons; // Whether to enable action buttons and switch to explore mode after 
          // function finishes.
        
        // Set defaults.
        basicFinishInd = false;
        enableButtons = false;
        
        // Flag shake as inactive.
        shakeActiveInd = false;
        
        System.out.println("Function: offense_finish");
        
        // 1.  Handle successful run or allow an enemy attack.
        // Phases:  COMBAT_PHASE_DEFENSE, COMBAT_PHASE_RUN.
        
        // If player successfully ran away from enemy, then...
        if (fightEnum == HeroineEnum.FightEnum.FIGHT_RUN && actionResultPlayer.getResult())
        {
            
            // Player successfully ran away from enemy.
            
            // No need to implement enemy attack.
            
            // Move to combat phase representing successful evasion of enemy.
            combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_RUN;
            
            // Flag to perform basic logic to conclude combat.
            basicFinishInd = true;
            
        }
        
        else
        {
            
            // Player either failed to run from enemy or engaged in other action.
            
            // Implement an enemy attack on the player -- updates action results for labels.
            enemy_attack(sounds);
            
            // Call function to handle logic related to start of defense phase.
            // The function displays the results of the enemy attack, shaking the tiles if player is hit.
            defense_start(powerSourceLabel_Enemy, powerActionLabel_Enemy, powerResultLabel_Enemy, 
              tileGroup, hpLabel, mpLabel);
            
        }
        
        // 2.  Handle conclusion of combat -- occurs when successful evading enemy.
        
        // If performing basic logic to conclude combat, then...
        if (basicFinishInd)
        {
            
            // Perform basic logic to conclude combat.
            enableButtons = conclude_combat(enemy, enemyLabel, infoButton, infoButtonSelector, 
              hpLabel, mpLabel, facingLabel, powerSourceLabel, powerActionLabel, powerResultLabel, 
              powerSourceLabel_Enemy, powerActionLabel_Enemy, powerResultLabel_Enemy, true, 
              mapActionButtons, mapActionButtonEnabled, mapSelectorPosX, mapSelectorPosY);
            
        }
        
        // 3.  Finish function.
        
        // Return whether to enable action buttons and switch to explore mode after function finishes.
        return enableButtons;
        
    }
    
    // powerSourceLabel = Label showing the source of the power (player or object).
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // enemy = ShakyActor object that will act as the enemy.
    private void offense_start(CustomLabel powerSourceLabel, CustomLabel powerActionLabel, 
      CustomLabel powerResultLabel, ShakyActor enemy)
    {
        
        // The function encapsulates display logic related to the first segment of the player portion
        // of a round of combat.  The first segment includes setting the power labels and (possibly)
        // shaking the enemy.
        
        // Note that the variables used to set the text of the labels get set in the parent function.
        
        // Store next combat phase.
        combatPhase = HeroineEnum.CombatPhaseEnum.COMBAT_PHASE_OFFENSE;
        
        // Display results of player actions in power labels.
        ExploreScreen.info_update_powerResponseLines(actionResultPlayer, powerSourceLabel, 
          powerActionLabel, powerResultLabel, false);
        
        // If enemy damaged, then...
        if (actionResultPlayer.getOpponentHurt())
        {
            
            // Enemy damaged.
            
            // Shake the enemy five times to indicate player caused damage.
            enemy.startShake(5);
            
            // Flag shake as active.
            shakeActiveInd = true;
            
        }
        
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
    
    public HeroineEnum.CombatPhaseEnum getCombatPhase() {
        return combatPhase;
    }
    
    public HeroineEnum.EnemyEnum getEnemyEnum() {
        return enemyEnum;
    }

    public void setEnemyEnum(HeroineEnum.EnemyEnum enemyEnum) {
        this.enemyEnum = enemyEnum;
    }
    
    public boolean isShakeActiveInd() {
        return shakeActiveInd;
    }
    
}