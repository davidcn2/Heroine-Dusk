package heroinedusk;

// Local project imports.
import com.badlogic.gdx.graphics.Color;
import core.AssetMgr;
import core.BaseActor;
import core.ShakyActor;
import gui.CustomLabel;

// Java imports.
import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;

public class Combat
{
    
    /*
    The class stores information and logic related to combat.
    
    Methods include:

    xyz:  
    */
    
    // Declare object variables.
    
    // Declare regular variables.
    private final Avatar avatar; // Reference to player information class.
    private HeroineEnum.CombatPhaseEnum combatPhase; // Current combat phase.
    private HeroineEnum.EnemyEnum enemyEnum; // Type of enemy involved in combat.
    private final SecureRandom number; // Used for generating random numbers.
    private final int selectorAdjPos; // Position adjustment related to selector.
    
    // Declare constants.
    private final Color COLOR_MED_GRAY = new Color(0.50f, 0.50f, 0.50f, 1);
    
    // avatar = Reference to player information class.
    // scale = Output scale factor -- multiple of 160 and 120.
    public Combat(Avatar avatar, int scale)
    {
        
        // The constructor initializes the combat engine and stores the parameters in their related class
        // variables.
        
        // Start random number generator.
        number = new SecureRandom();
        
        // Store reference to player information class.
        this.avatar = avatar;
        
        // Calculate selector position adjustment.
        this.selectorAdjPos = scale * 2;
        
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
        
        System.out.println("Encountered enemy of type, " + enemyEnum + ".");
        
        // Store passed values.
        this.enemyEnum = enemyEnum;
        
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