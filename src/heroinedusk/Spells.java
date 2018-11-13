package heroinedusk;

// LibGDX imports.
import com.badlogic.gdx.graphics.Color;

// Local project imports.
import core.BaseActor;
import gui.CustomLabel;
import routines.UtilityRoutines;
import screens.ExploreScreen;

// Java imports.
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Map;

/*
Interface (implements) vs Sub-Class (extends)...

The distinction is that implements means that you're using the elements of a Java Interface in your 
class, and extends means that you are creating a subclass of the class you are extending. You can 
only extend one class in your new class, but you can implement as many interfaces as you would like.

Interface:  A Java interface is a bit like a class, except a Java interface can only contain method
signatures and fields. An Java interface cannot contain an implementation of the methods, only the 
signature (name, parameters and exceptions) of the method. You can use interfaces in Java as a way
to achieve polymorphism.

Subclass: A Java subclass is a class which inherits a method or methods from a Java superclass.
A Java class may be either a subclass, a superclass, both, or neither!

Polymorphism:  Polymorphism is the ability of an object to take on many forms. The most common use 
of polymorphism in OOP occurs when a parent class reference is used to refer to a child class object.
Any Java object that can pass more than one IS-A test is considered to be polymorphic.
*/

public class Spells
{
    
    /*
    The class stores information and logic related to the player casting spells.
    
    Action button enabled - hash map:
    1.  In function, cast_burn, calls disableButtons_Spell when running out of magic points.
    2.  In function, cast_burn, when tile in front of player is a skull pile, sets value to false for 
        enumeration, ACTION_BUTTON_BURN.
    3.  In function, cast_heal, calls disableButtons_Spell when running out of magic points.
    4.  In function, cast_heal, when player reaches maximum hit points, sets value to false for enumeration, 
        ACTION_BUTTON_HEAL.
    5.  In function, cast_unlock, calls disableButtons_Spell when running out of magic points.
    6.  In function, cast_unlock, when tile in front of player is a locked door, sets value to false for 
        enumeration, ACTION_BUTTON_UNLOCK.
    
    Methods include:

    cast_burn:  Encapsulates logic related to the player casting the burn spell.
    cast_heal:  Encapsulates logic related to the player casting the heal spell.
    cast_unlock:  Encapsulates logic related to the player casting the unlock spell.
    */
    
    // Declare constants.
    private static final Color COLOR_MED_GRAY = new Color(0.50f, 0.50f, 0.50f, 1); // Disabled color.
    private static final int TILE_POS_TREASURE = 13; // Tile position of treasure (actor).
    private static final int TILE_POS_TREASURE_GROUP = 14; // Tile position of treasure (group).
    private static final int TILE_POS_CHEST = 15; // Tile position of chest.
    private static final int TILE_POS_BONE_PILE = 16; // Tile position of bone pile.
    private static final int TILE_POS_LOCK = 17; // Tile position of lock.
    
    // avatar = Reference to the player information (class).
    // atlasItems = Reference to the atlas items information (class).
    // combatInd = Whether in combat.
    // infoButtonSelected = Whether information button selected and clicked.
    // imgTileNumForward = Tile image enumeration value for location in front of player.  Null in combat.
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // mpLabel = Label showing player magic points.
    // tiles = BaseActor objects associated with tiles.
    // mazemap = Stores data for the current active region / map.
    // buttonActor = Reference to BaseActor for the button.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // number = Used for generating random numbers.
    // sounds = Reference to the sounds class.
    // actionResult = Reference to object with result information.  Allows for reuse.
    public static void cast_burn(Avatar avatar, AtlasItems atlasItems, boolean combatInd, 
      boolean infoButtonSelected, HeroineEnum.ImgTileEnum imgTileNumForward, CustomLabel powerActionLabel, 
      CustomLabel powerResultLabel, CustomLabel mpLabel, ArrayList<BaseActor> tiles, MazeMap mazemap, 
      BaseActor buttonActor, Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons,
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      SecureRandom number, Sounds sounds, ActionResult actionResult)
    {
        
        // The function encapsulates logic related to the player casting the burn spell.
        // The function uses the actionResult (object) parameter to return the results.
        
        // The numeric portion of the result indicates...
        // 1 to n = The (base) number of hit points of damage caused to an enemy.
        // -1 = Unable to cast the spell.
        // 1 = Successful burning an object (NOT AN enemy).
        
        String actionText; // Text to display related to action.
        int atk_max; // Maximum damage caused by player.
        int atk_min; // Minimum damage caused by player.
        int burn_amount; // Either amount of damage to enemy, -1 when unable to cast spell, or 1 when
          // succesfully burning an object.
        Boolean enemy_hurt; // Whether enemy hurt.  Null when not in combat or failing to cast spell.
        String mpText; // Text to display for magic points.
        String resultText; // Text to display related to result.
        boolean success; // Whether successful casting spell.
        
        // Set defaults.
        burn_amount = -1; // Signifies unable to cast burn spell.  Update later if spell cast.
        actionText = "";
        resultText = "";
        success = false;
        enemy_hurt = null;
        
        // If player lacks sufficient magic points, then...
        if (avatar.getMp() == 0)
        {

            // Player lacks sufficient magic points.
            
            // If square in front of player NOT a skull pile and NOT in combat mode, then...
            if (imgTileNumForward != HeroineEnum.ImgTileEnum.IMG_TILE_SKULL_PILE && 
                !combatInd)
            {

                // Square in front of player NOT a skull pile and NOT in combat mode.

                // Render "(NO TARGET)" message.
                ExploreScreen.info_render_no_target(null, powerActionLabel, sounds);
                
                // Store action text.
                actionText = "(NO TARGET)";
                
            }

            else
            {

                // Square in front of player is a skull pile or player in combat mode.

                // If in combat, then...
                if (combatInd)
                {

                    // In combat.

                    // Play error sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_ERROR);

                }

                else
                {

                    // NOT in combat and square in front of player is a skull pile.
                
                    // Render "INSUFFICIENT MP!" message.
                    ExploreScreen.info_render_insufficient_mp(powerActionLabel, sounds);
                    
                }
                
                // Store action text.
                actionText = "INSUFFICIENT MP!";

            } // End ... If square in front of player is a skull pile and player lacks sufficient magic points.
            
        } // End ... If player lacks sufficient magic points.

        // Otherwise, if player in combat, then...
        else if (combatInd)
        {

            // Player in combat and has sufficient magic points.
            
            // Play fire sound.
            sounds.playSound(HeroineEnum.SoundEnum.SOUND_FIRE);
            
            // Store minimum and maximum (base) damage caused by player.
            atk_min = avatar.getMinDamage();
            atk_max = avatar.getMaxDamage();
            
            // Randomly determine (base) damage based on player minimum and maximum.
            burn_amount = UtilityRoutines.generateStandardRnd(number, atk_min, atk_max);
            
            // Decrement player magic points.
            avatar.decrement_mp();
            
            // Store text to display for magic points.
            mpText = avatar.getMpText();

            // Update magic points label.
            mpLabel.setLabelText(mpText);
            
            // Store action and result text.
            actionText = "BURN!";
            resultText = "TBD - Base " + Integer.toString(burn_amount) + " damage";
            
            // Flag enemy as hurt.
            enemy_hurt = true;
            
            // Flag as successful.
            success = true;
            
            // If player has no (remaining) magic points, then...
            if (avatar.getMp() == 0)
            {
                
                // Player has no (remaining) magic points.
                
                // Disable all spell-associated buttons.
                ExploreScreen.disableButtons_Spell(mapActionButtons, mapActionButtonEnabled);
                
            } // End ... If player has no (remaining) magic points.
            
            System.out.println("Burn (base) - " + burn_amount + " hp!");
            
        } // End ... If player in combat.

        else
        {

            // Player NOT in combat and has sufficient magic points.

            // If tile in front of player is a skull pile, then...
            if (imgTileNumForward == HeroineEnum.ImgTileEnum.IMG_TILE_SKULL_PILE)
            {

                // Burn the skull pile in the tile in front of the player.

                // Play fire sound.
                sounds.playSound(HeroineEnum.SoundEnum.SOUND_FIRE);

                // Set up an action to fade out the skull pile.
                tiles.get(TILE_POS_BONE_PILE).addAction_FadeOut(0.25f, 0.75f);

                // Disable skull pile events.
                mazemap.setBonePileActiveInd(false);

                // Set skull piles in array list inactive and remove from hash map entry associated 
                // with current location.
                atlasItems.removeBonePileFirst(avatar.getMapLocation_ForwardLoc());

                // Change tile in front of player to dungeon floor.
                mazemap.setImgTileEnum_ForwardLoc(avatar, HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_CEILING);

                // Decrement player magic points.
                avatar.decrement_mp();

                // Store text to display for magic points.
                mpText = "MP " + Integer.toString(avatar.getMp()) + "/" + Integer.toString(avatar.getMax_mp());

                // Update magic points label.
                mpLabel.setLabelText(mpText);

                // Display action-related text.
                ExploreScreen.info_update_powerResponseLines("BURN!", "CLEARED PATH!", powerActionLabel, 
                  powerResultLabel, true);
                
                // If button passed and NOT in information mode, then...
                if (buttonActor != null && !infoButtonSelected)
                {
                    
                    // Button passed and NOT in information mode.
                    
                    // Set the burn button to not visible.
                    buttonActor.setVisible(false);

                    // Apply a dark shade to the burn button to signify not currently enabled.
                    buttonActor.setColor(COLOR_MED_GRAY);

                }
                
                // If player has no (remaining) magic points, then...
                if (avatar.getMp() == 0)
                {

                    // Player has no (remaining) magic points.

                    // Disable all spell-associated buttons.
                    ExploreScreen.disableButtons_Spell(mapActionButtons, mapActionButtonEnabled);

                } // End ... If player has no (remaining) magic points.
                
                else
                {
                    
                    // Player has one or more (remaining) magic points.
                    // Only need to disable current button.
                    
                    // Disable burn button.
                    mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_BURN, false);
                    
                }
                
                // Store amount to indicate successful burning of object.
                burn_amount = 1;
                
                // Store action and result text.
                actionText = "BURN!";
                resultText = "CLEARED PATH!";

                // Flag as successful.
                success = true;
                
                // TO DO:  Add avatar save.

            } // End ... If tile in front of player is a skull pile.

            else
            {

                // Tile in front of player not a burnable object.

                // Render "(NO TARGET)" message.
                ExploreScreen.info_render_no_target(null, powerActionLabel, sounds);

                // Store action text.
                actionText = "(NO TARGET)";
                
            }

        } // End ... If NOT in combat.
        
        // If action result object passed (not null), then...
        if (actionResult != null)
        {
            
            // Action result object passed (not null).
            
            // Set properties of action result object.
            actionResult.setResult(success);
            actionResult.setResultNumeric(burn_amount);
            actionResult.setTextAction(actionText);
            actionResult.setTextResult(resultText);
            
            // If enemy hurt value exists, then...
            if (enemy_hurt != null)
                actionResult.setOpponentHurt(enemy_hurt);
            
        }
        
    } // End ... cast_burn function.
    
    // avatar = Reference to the player inormation (class).
    // combatInd = Whether in combat.
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // hpLabel = Label showing player hit points.
    // mpLabel = Label showing player magic points.
    // buttonActor = Reference to BaseActor for the button.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // sounds = Reference to the sounds class.
    // actionResult = Reference to object with result information.  Allows for reuse.
    public static void cast_heal(Avatar avatar, boolean combatInd, CustomLabel powerActionLabel, 
      CustomLabel powerResultLabel, CustomLabel hpLabel, CustomLabel mpLabel, BaseActor buttonActor, 
      Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons,
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, Sounds sounds, 
      ActionResult actionResult)
    {
        
        // The function encapsulates logic related to the player casting the heal spell.
        // The function uses the actionResult (object) parameter to return the results.
        
        // The numeric portion of the result indicates...
        // 0 to n = The number of hp healed.
        // -1 = Unable to cast the spell.
        
        String actionText; // Text to display related to action.
        int heal_amount; // Number of hit points to heal.  -1 = Unable to cast heal spell.
        int hp; // Number of player hit points before healing.  Current player hit points.
        int hpMax; // Maximum number of player hit points.
        String hpText; // Text to display for hit points.
        String mpText; // Text to display for magic points.
        String resultText; // Text to display related to result.
        boolean success; // Whether successful casting spell.
        
        // Set defaults.
        heal_amount = -1; // Signifies unable to cast heal spell.  Update later if spell cast.
        actionText = "";
        resultText = "";
        success = false;
        
        // If player at maximum hit points, then...
        if (avatar.getHp_AtMax())    
        {

            // Player at maximum hit points.

            // Play error sound.
            sounds.playSound(HeroineEnum.SoundEnum.SOUND_ERROR);
            
            // Store action text.
            actionText = "HP AT MAX!";
            
            // If NOT in combat, then...
            if (!combatInd)
            {
                
                // NOT in combat.
                
                // Update power action labels.
                powerActionLabel.setLabelText("HP AT MAX!");
                
                // Display power action  labels.
                powerActionLabel.applyVisible(true);

                // Set up fade effect for power action label.
                powerActionLabel.addAction_Fade();
                
            }

        } // End ... If player at maximum hit points.

        // Otherwise, if player lacks sufficient magic points, then...
        else if (avatar.getMp() == 0)
        {

            // Player lacks sufficient magic points.

            // Store action text.
            actionText = "INSUFFICIENT MP!";
            
            // If in combat, then...
            if (combatInd)
            {
                
                // In combat.
                
                // Play error sound.
                sounds.playSound(HeroineEnum.SoundEnum.SOUND_ERROR);
                
            }
            
            else
            {
                
                // NOT in combat.
                
                // Render "INSUFFICIENT MP!" message.
                ExploreScreen.info_render_insufficient_mp(powerActionLabel, sounds);
                
            }
            
        } // End ... If player lacks sufficient magic points.

        else
        {

            // Player below maximum hit points.

            // Play heal sound.
            sounds.playSound(HeroineEnum.SoundEnum.SOUND_HEAL);

            // Store number of player hit points before healing.
            hp = avatar.getHp();
            
            // Store maximum number of player hit points.
            hpMax = avatar.getMax_hp();
            
            // Calculate number of hit points to heal.
            heal_amount = (int)Math.floor(hpMax / 2) + (int)Math.floor(Math.random() * hpMax / 2);
            heal_amount = Math.min(heal_amount, hpMax - hp);

            // Reduce magic points by one.
            avatar.decrement_mp();

            // Increase player hit points by calculated amount (locally).
            hp += heal_amount;
            
            // Update current player hit points.
            avatar.setHp(hp);
            
            // Store text to display for hit points.
            hpText = avatar.getHpText();

            // Update hit points label.
            hpLabel.setLabelText(hpText);

            // Store text to display for magic points.
            mpText = avatar.getMpText();

            // Update magic points label.
            mpLabel.setLabelText(mpText);
            
            // If player has no (remaining) magic points, then...
            if (avatar.getMp() == 0)
            {
                
                // Player has no (remaining) magic points.
                
                // Disable all spell-associated buttons.
                ExploreScreen.disableButtons_Spell(mapActionButtons, mapActionButtonEnabled);
                
            } // End ... If player has no (remaining) magic points.
            
            // Otherwise, if player now at maximum hit points, then...
            else if (avatar.getHp_AtMax())
            {

                // Player now at maximum hit points.

                // Disable heal button.
                ExploreScreen.disableButton(buttonActor, HeroineEnum.ActionButtonEnum.ACTION_BUTTON_HEAL, 
                  mapActionButtonEnabled);
                
            } // End ... If player now at maximum hit points.

            System.out.println("Heal - " + heal_amount + " hp!");

            // Store action and result text.
            actionText = "HEAL!";
            resultText = "+" + Integer.toString(heal_amount) + " HP";
            
            // If NOT in combat, then...
            if (!combatInd)
            {
                
                // NOT in combat.
                
                // Update power action and result labels.
                powerActionLabel.setLabelText("HEAL!");
                powerResultLabel.setLabelText("+" + Integer.toString(heal_amount) + " HP");

                // Display power action and result labels.
                powerActionLabel.applyVisible(true);
                powerResultLabel.applyVisible(true);

                // Set up fade effects for power action and result labels.
                powerActionLabel.addAction_Fade();
                powerResultLabel.addAction_Fade();
                
            }
            
            // Flag as successful.
            success = true;
            
        } // End ... If player at maximum hit points.
        
        // If action result object passed (not null), then...
        if (actionResult != null)
        {
            
            // Action result object passed (not null).
            
            // Set properties of action result object.
            actionResult.setResult(success);
            actionResult.setResultNumeric(heal_amount);
            actionResult.setTextAction(actionText);
            actionResult.setTextResult(resultText);
            
        }
        
    } // End ... cast_heal function.
    
    // avatar = Reference to the player information (class).
    // atlasItems = Reference to the atlas items information (class).
    // combatInd = Whether in combat.
    // infoButtonSelected = Whether information button selected and clicked.
    // imgTileNumForward = Tile image enumeration value for location in front of player.  Null in combat.
    // powerActionLabel = Label showing the first line -- power action.
    // powerResultLabel = Label showing the second line -- power result.
    // mpLabel = Label showing player magic points.
    // tiles = BaseActor objects associated with tiles.
    // mazemap = Stores data for the current active region / map.
    // buttonActor = Reference to BaseActor for the button.
    // mapActionButtons = Hash map containing BaseActor objects that act as the action buttons.
    // mapActionButtonEnabled = Hash map containing enabled status of action buttons.
    // number = Used for generating random numbers.
    // sounds = Reference to the sounds class.
    // actionResult = Reference to object with result information.  Allows for reuse.
    public static void cast_unlock(Avatar avatar, AtlasItems atlasItems, boolean combatInd, 
      boolean infoButtonSelected, HeroineEnum.ImgTileEnum imgTileNumForward, CustomLabel powerActionLabel, 
      CustomLabel powerResultLabel, CustomLabel mpLabel, ArrayList<BaseActor> tiles, MazeMap mazemap, 
      BaseActor buttonActor, Map<HeroineEnum.ActionButtonEnum, BaseActor> mapActionButtons, 
      Map<HeroineEnum.ActionButtonEnum, Boolean> mapActionButtonEnabled, 
      SecureRandom number, Sounds sounds, ActionResult actionResult)
    {
        
        // The function encapsulates logic related to the player casting the unlock spell.
        // The function uses the actionResult (object) parameter to return the results.
        
        // The numeric portion of the result indicates...
        // 1 to n = The (base) number of hit points of damage caused to an enemy.
        // -1 = Unable to cast the spell.
        // 1 = Successful unlocking an object (NOT AN enemy).
        
        String actionText; // Text to display related to action.
        int atk_max; // Maximum damage caused by player.
        int atk_min; // Minimum damage caused by player.
        Boolean enemy_hurt; // Whether enemy hurt.  Null when not in combat or failing to cast spell.
        int unlock_amount; // Either amount of damage to enemy, -1 when unable to cast spell, or 1 when
          // succesfully unlocking an object.
        String mpText; // Text to display for magic points.
        String resultText; // Text to display related to result.
        boolean success; // Whether successful casting spell.
        
        // Set defaults.
        unlock_amount = -1; // Signifies unable to cast unlock spell.  Update later if spell cast.
        actionText = "";
        resultText = "";
        success = false;
        enemy_hurt = null;
        
        // If player lacks sufficient magic points, then...
        if (avatar.getMp() == 0)
        {

            // Player lacks sufficient magic points.

            // If square in front of player NOT a locked door and NOT in combat mode, then...
            if (imgTileNumForward != HeroineEnum.ImgTileEnum.IMG_TILE_LOCKED_DOOR && !combatInd)
            {

                // Square in front of player NOT a locked door and NOT in combat mode.

                // Render "(NO TARGET)" message.
                ExploreScreen.info_render_no_target(HeroineEnum.SoundEnum.SOUND_BLOCKED, powerActionLabel, 
                  sounds);
                
                // Store action text.
                actionText = "(NO TARGET)";
                
            }

            else
            {
                
                // Player lacks sufficient magic points and one of the two...
                // 1.  NOT in combat and square in front of player is a locked door.
                // 2.  IN combat.

                // If in combat, then...
                if (combatInd)
                {

                    // In combat.

                    // Play error sound.
                    sounds.playSound(HeroineEnum.SoundEnum.SOUND_ERROR);

                }

                else
                {

                    // NOT in combat.
                
                    // Render "INSUFFICIENT MP!" message.
                    ExploreScreen.info_render_insufficient_mp(powerActionLabel, sounds);
                    
                }
                
                // Store action text.
                actionText = "INSUFFICIENT MP!";

            } // End ... If square in front of player is a locked door and player lacks sufficient magic points.

        } // End ... If player lacks sufficient magic points.

        // Otherwise, if player in combat, then...
        else if (combatInd)    
        {

            // Player in combat and has sufficient magic points.
            
            // Play unlock sound.
            sounds.playSound(HeroineEnum.SoundEnum.SOUND_UNLOCK);
            
            // Store minimum and maximum (base) damage caused by player.
            atk_min = avatar.getMinDamage();
            atk_max = avatar.getMaxDamage();
            
            // Randomly determine (base) damage based on player minimum and maximum.
            unlock_amount = UtilityRoutines.generateStandardRnd(number, atk_min, atk_max);
            
            // Decrement player magic points.
            avatar.decrement_mp();
            
            // Store text to display for magic points.
            mpText = avatar.getMpText();

            // Update magic points label.
            mpLabel.setLabelText(mpText);
            
            // Store action and result text.
            actionText = "UNLOCK!";
            resultText = "TBD - Base " + Integer.toString(unlock_amount) + " damage";
            
            // Flag enemy as hurt.
            enemy_hurt = true;
            
            // Flag as successful.
            success = true;
            
            // If player has no (remaining) magic points, then...
            if (avatar.getMp() == 0)
            {
                
                // Player has no (remaining) magic points.
                
                // Disable all spell-associated buttons.
                ExploreScreen.disableButtons_Spell(mapActionButtons, mapActionButtonEnabled);
                
            } // End ... If player has no (remaining) magic points.
            
            System.out.println("Unlock (base) - " + unlock_amount + " hp!");

        } // End ... If player in combat.

        else
        {

            // Player NOT in combat and has sufficient magic points.

            // If tile in front of player is a locked door, then...
            if (imgTileNumForward == HeroineEnum.ImgTileEnum.IMG_TILE_LOCKED_DOOR)
            {

                // Unlock the door in the tile in front of the player.

                // Play unlock sound.
                sounds.playSound(HeroineEnum.SoundEnum.SOUND_UNLOCK);
                
                // Set up an action to fade out the lock.
                tiles.get(TILE_POS_LOCK).addAction_FadeOut(0.25f, 0.75f);

                // Disable lock events.
                mazemap.setLockedDoorActiveInd(false);

                // Set locked door in array list inactive and remove from hash map entry associated 
                // with current location.
                atlasItems.removeLockedDoorFirst(avatar.getMapLocation_ForwardLoc());

                // Change tile in front of player to regular (unlocked) door.
                //mazemap.setImgTileEnum_ForwardLoc(avatar, HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_DOOR);

                // Change side tile in front of player to regular (unlocked) door.
                mazemap.setImgTileEnum_SideLoc(avatar, HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_DOOR);
                
                // Change side in tile in front of (adjacent to) player to regular (unlocked) door.
                mazemap.setImgTileEnum_SideLoc_Forward(avatar, HeroineEnum.ImgTileEnum.IMG_TILE_DUNGEON_DOOR);

                
                // Decrement player magic points.
                avatar.decrement_mp();

                // Store text to display for magic points.
                mpText = avatar.getMpText();
                
                // Update magic points label.
                mpLabel.setLabelText(mpText);

                // Display action-related text.
                ExploreScreen.info_update_powerResponseLines("UNLOCK!", "DOOR OPENED!", powerActionLabel, 
                  powerResultLabel, true);

                // If button passed and NOT in information mode, then...
                if (buttonActor != null && !infoButtonSelected)
                {
                    
                    // Button passed and NOT in information mode.
                
                    // Set the unlock button to not visible.
                    buttonActor.setVisible(false);

                    // Apply a dark shade to the unlock button to signify not currently enabled.
                    buttonActor.setColor( COLOR_MED_GRAY );
                    
                }
                
                // If player has no (remaining) magic points, then...
                if (avatar.getMp() == 0)
                {

                    // Player has no (remaining) magic points.

                    // Disable all spell-associated buttons.
                    ExploreScreen.disableButtons_Spell(mapActionButtons, mapActionButtonEnabled);

                } // End ... If player has no (remaining) magic points.
                
                else
                {
                    
                    // Player has one or more (remaining) magic points.
                    // Only need to disable current button.
                    
                    // Disable unlock button.
                    mapActionButtonEnabled.put(HeroineEnum.ActionButtonEnum.ACTION_BUTTON_UNLOCK, false);
                    
                }
                
                // Store amount to indicate successful unlocking of object.
                unlock_amount = 1;
                
                // Store action and result text.
                actionText = "UNLOCK!";
                resultText = "DOOR OPENED!";

                // Flag as successful.
                success = true;
                
                // TO DO:  Add avatar save.

            } // End ... If tile in front of player is a locked door.

            // Otherwise -- tile in front of player NOT a locked door and NOT in combat.
            else
            {

                // Tile in front of player NOT a locked door and NOT in combat.
                
                // Render "(NO TARGET)" message.
                ExploreScreen.info_render_no_target(HeroineEnum.SoundEnum.SOUND_BLOCKED, powerActionLabel,
                  sounds);
                
                // Store action text.
                actionText = "(NO TARGET)";
                
            }

        } // End ... If NOT in combat.
        
        // If action result object passed (not null), then...
        if (actionResult != null)
        {
            
            // Action result object passed (not null).
            
            // Set properties of action result object.
            actionResult.setResult(success);
            actionResult.setResultNumeric(unlock_amount);
            actionResult.setTextAction(actionText);
            actionResult.setTextResult(resultText);
            
            // If enemy hurt value exists, then...
            if (enemy_hurt != null)
                actionResult.setOpponentHurt(enemy_hurt);
            
        }
        
    } // End ... cast_unlock function.
    
}