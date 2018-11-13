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
    // scale = Output scale factor -- multiple of 160 and 120.
    public Combat(Avatar avatar, int scale)
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
        
        // Calculate selector position adjustment.
        this.selectorAdjPos = scale * 2;
        
    }
    
    // Methods below...
    
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
        The function encapsulates logic related to a round of combat.
        The function contains logic to handle player actions, other than related visual results.
        
        The function returns whether to enable the action buttons immediately.
        
        Other functions handle:
        1.  The visual results of the player action.
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
                        
                        System.out.println("Miss");
                        
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
            
            // Call function to handle logic related to offense phase.
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
    // enemy = ShakyActor object that acts as the enemy.
    // assetMgr = Reference to the asset manager class.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapActionButtonPosX = X-coordinate of each action button.
    // mapActionButtonPosY = Y-coordinate of bottom of each action button.
    public void initiate_combat(HeroineEnum.EnemyEnum enemyEnum, CustomLabel enemyLabel, 
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, BaseActor infoButton, 
      CustomLabel facingLabel, CustomLabel regionLabel, CustomLabel powerSourceLabel, 
      CustomLabel powerActionLabel, CustomLabel powerResultLabel, ShakyActor enemy, AssetMgr assetMgr,
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
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
          facingLabel, regionLabel, powerSourceLabel, powerActionLabel, powerResultLabel, mapActionButtons, 
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
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapSelectorPosX = List of x-positions to place selector -- related to buttons.
    // mapSelectorPosY = List of y-positions to place selector -- related to buttons.
    private void render_interface_end(ShakyActor enemy, CustomLabel enemyLabel, BaseActor infoButton,
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, CustomLabel facingLabel, 
      CustomLabel powerSourceLabel, CustomLabel powerActionLabel, CustomLabel powerResultLabel, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosX,
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosY)
    {
        
        /*
        The function renders the interface for the ending of combat, including removing the enemy.
        
        1.  Fade out the enemy name.
        2.  Fade out the enemy.
        3.  Hide attack, run, and other action buttons.  Update enabled status of attack and run.
        4.  Move selector to information button.  Hide selector.
        5.  Fade out the hp and mp labels (in standard locations).
        6.  Show the information button after slight delay.
        7.  Show the facing label after slight delay.
        8.  Fade out the player power labels.
        9.  To Do:  Fade out the enemy power labels.
        */

        HeroineEnum.ActionButtonEnum actionButtonEnum; // Enumeration value related to current action button 
          // in loop.
        Set<Map.Entry<HeroineEnum.ActionButtonEnum, BaseActor>> entrySetActionButtons; // Set view of the 
          // action buttons in the hash map.
        
        // 1.  Fade out the enemy name.
        
        // Remove existing actions and fade out label over the course of 0.50 seconds.
        enemyLabel.removeActions();
        enemyLabel.addAction_FadeOut(0.00f, 0.50f);
        
        // 2.  Fade out the enemy.
        
        // Remove existing actions and fade out enemy (actor) over the course of 0.50 seconds.
        enemy.removeActions();
        enemy.addAction_FadeOut(0.00f, 0.50f);
        
        // 3.  Hide attack, run, and other action buttons.  Update enabled status of attack and run.
        
        // Hide action buttons.
        
        // Store a set view of the spellbook mappings for the hash map.
        entrySetActionButtons = mapActionButtons.entrySet();
        
        // Disable attack and run buttons.
        mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_ATTACK, false);
        mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_RUN, false);
        
        // Loop through action buttons.
        for (Map.Entry entryActionButton : entrySetActionButtons)
        {
            
            // Store enumeration value for current action button in loop.
            actionButtonEnum = (HeroineEnum.ActionButtonEnum)entryActionButton.getKey();
            
            // Return button to normal color.
            //mapActionButtons.get(actionButtonEnum).setColor(Color.WHITE);
            
            // Hide current button.
            mapActionButtons.get(actionButtonEnum).setVisible(false);
            
        } // End ... Loop through action buttons.
        
        // 4.  Move selector to information button.  Hide selector.
        
        // Position the selector around the information button.
        infoButtonSelector.setPosition(mapSelectorPosX.get(HeroineEnum.SelectPosEnum.BUTTON_POS_INFO), 
          mapSelectorPosY.get(HeroineEnum.SelectPosEnum.BUTTON_POS_INFO));
        
        // Hide selector.
        infoButtonSelector.setVisible(false);
        
        // 5.  Fade out hp and mp labels (in standard locations).
        
        // Remove existing actions and fade out labels over the course of 0.50 seconds.
        hpLabel.removeActions();
        hpLabel.addAction_FadeOut(0.00f, 0.50f);
        mpLabel.removeActions();
        mpLabel.addAction_FadeOut(0.00f, 0.50f);
        
        // 6.  Show the information button after 0.50 seconds elapse.
        infoButton.removeActions();
        infoButton.addAction_FadeIn(0.50f, 0.00f);
        
        // Remove existing actions and fade in the information button.
        
        // 7.  Show the facing label after 0.50 seconds elapse.
        
        // Remove existing actions and show the facing label after 0.50 seconds elapse.
        facingLabel.removeActions();
        facingLabel.addAction_FadeIn(0.50f, 0.00f);
        
        // 8.  Fade out the player power labels.
        
        // Remove existing actions and fade out labels over the course of 1.00 seconds.
        // Wait 0.50 seconds before starting the fade.
        powerSourceLabel.removeActions();
        powerSourceLabel.addAction_FadeOut(1.00f, 1.00f);
        powerActionLabel.removeActions();
        powerActionLabel.addAction_FadeOut(1.00f, 1.00f);
        powerResultLabel.removeActions();
        powerResultLabel.addAction_FadeOut(1.00f, 1.00f);
        
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
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapActionButtonPosX = X-coordinate of each action button.
    // mapActionButtonPosY = Y-coordinate of bottom of each action button.
    private void render_interface_start(HeroineEnum.EnemyEnum enemyEnum, CustomLabel enemyLabel, 
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, BaseActor infoButton, 
      CustomLabel facingLabel, CustomLabel regionLabel, 
      CustomLabel powerSourceLabel, CustomLabel powerActionLabel, CustomLabel powerResultLabel,
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
        
    }
    
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
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // mapSelectorPosX = List of x-positions to place selector -- related to buttons.
    // mapSelectorPosY = List of y-positions to place selector -- related to buttons.
    public boolean offense_finish(ShakyActor enemy, CustomLabel enemyLabel, BaseActor infoButton,
      BaseActor infoButtonSelector, CustomLabel hpLabel, CustomLabel mpLabel, CustomLabel facingLabel, 
      CustomLabel powerSourceLabel, CustomLabel powerActionLabel, 
      CustomLabel powerResultLabel, Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosX,
      HashMap<HeroineEnum.SelectPosEnum, Float> mapSelectorPosY)
    {
        
        /*
        The function encapsulates display logic related to the last segment of the player portion
        of a round of combat.  The segment includes support for:
        
        1.  Checking for and handling a victory,
        2.  Successfully running away.
        3.  Moving to the next combat phase (enemy still alive or failed to run away from enemy).
        
        The function returns whether to enable the action buttons and switch to explore mode.
        */

        boolean basicFinishInd; // Whether to perform basic logic related to conclusion of combat.
        boolean enableButtons; // Whether to enable action buttons after function finishes.
        
        // Set defaults.
        basicFinishInd = false;
        enableButtons = false;
        
        // Flag shake as inactive.
        shakeActiveInd = false;
        
        // If enemy defeated (at or below zero hit points), then...
        if ( enemyHp <= 0 )
        {
            
            // Enemy defeated -- at or below zero hit points.
            
            // Render the interface for the ending of combat.
            render_interface_end(enemy, enemyLabel, infoButton, infoButtonSelector, hpLabel, mpLabel, 
              facingLabel, powerSourceLabel, powerActionLabel, powerResultLabel, 
              mapActionButtons, mapActionButtonEnabled, mapSelectorPosX, mapSelectorPosY);
            
            // Flag to enable action buttons after function finishes.
            enableButtons = true;
            
            // Flag to perform basic logic to conclude combat.
            basicFinishInd = true;
            
        }
        
        // Otherwise, if successful running away from enemy, then...
        else if ( fightEnum == HeroineEnum.FightEnum.FIGHT_RUN && actionResultPlayer.getResult() )
        {
            
            // Successful running away from enemy.
            
            // Render the interface for the ending of combat.
            render_interface_end(enemy, enemyLabel, infoButton, infoButtonSelector, hpLabel, mpLabel, 
              facingLabel, powerSourceLabel, powerActionLabel, powerResultLabel, 
              mapActionButtons, mapActionButtonEnabled, mapSelectorPosX, mapSelectorPosY);
            
            // Flag to enable action buttons after function finishes.
            enableButtons = true;
            
            // Flag to perform basic logic to conclude combat.
            basicFinishInd = true;
            
        }
        
        // Otherwise, ...
        else
        {
            
            // Otherwise, move to next combat phase.
            
            // Move to next combat phase.
            
            
        }
        
        // If performing basic logic to conclude combat, then...
        if (basicFinishInd)
        {
            
            // Perform basic logic to conclude combat.
            
            // If player has remaining magic points, then...
            if (avatar.getMp() > 0)
            {
                
                // Player has remaining magic points.
                
                // Restore action buttons associated with spells.
                ExploreScreen.restoreButtons_Spell(mapActionButtons, mapActionButtonEnabled,
                  mapActionButtonEnabled_Start);
                
            }
            
        }
        
        // Return whether to enable action buttons.
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