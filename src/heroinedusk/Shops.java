package heroinedusk;

import java.util.ArrayList;

public class Shops 
{
    
    /*
    The class stores information and logic related to the shops / locations / other messaging elements.
    
    Methods include:

    populateShops:  Populates the shop class.
    */
    
    // Declare object variables.
    private final HeroineDuskGame gameHD; // Reference to HeroineDusk (main) game class.
    protected ArrayList<Shop> shopList; // Contains message-related information, mostly used for shops.
    
    // Declare constants.
    private final int TYPE_VALUE_ROOM_COST = 10; // Cost associated with room / inn.
    
    // hdg = Reference to Heroine Dusk (main) game.
    public Shops(HeroineDuskGame hdg)
    {
        
        // The constructor performs initialization and populates shop / location / other messaging information.
        
        // Initialize the array containing shops and other messaging information.
        shopList = new ArrayList<>();
        
        // Populate shops.
        populateShops();
        
        // Store reference to main game class.
        gameHD = hdg;
        
    }
    
    private void populateShops()
    {
        
        // The function populates the shop class.
        // Value for messages representing an incrementing counter, one for each message.
        
        // Shops 0 to 7 = Cedar Village.
        // Shop 8 = Splash Screen (Wake Up).
        
        Shop temp; // Contains information about shop to add to array list.
        
        // Define shop.
        temp = new Shop(HeroineEnum.ImgBackgroundEnum.IMG_BACK_INTERIOR, "Cedar Arms", 
          HeroineEnum.ShopTypeEnum.SHOP_WEAPON);
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_WEAPON.getValue(), 
          HeroineEnum.WeaponEnum.WEAPON_IRON_KNIFE.getValue());
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_WEAPON.getValue(), 
          HeroineEnum.WeaponEnum.WEAPON_BRONZE_MACE.getValue());
        
        // Add shop #0.
        shopList.add(temp);
        
        // Define shop.
        temp = new Shop(HeroineEnum.ImgBackgroundEnum.IMG_BACK_INTERIOR, "Simmons Fine Clothier", 
          HeroineEnum.ShopTypeEnum.SHOP_ARMOR);
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_ARMOR.getValue(), 
          HeroineEnum.ArmorEnum.ARMOR_TRAVEL_CLOAK.getValue());
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_ARMOR.getValue(),
          HeroineEnum.ArmorEnum.ARMOR_HIDE_CUIRASS.getValue());
        
        // Add shop #1.
        shopList.add(temp);
        
        // Define shop.
        temp = new Shop(HeroineEnum.ImgBackgroundEnum.IMG_BACK_INTERIOR, "The Pilgrim Inn", 
          HeroineEnum.ShopTypeEnum.SHOP_ROOM);
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "We saw dead walking", "from the Canal Boneyard.");
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 1);
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_ROOM.getValue(), TYPE_VALUE_ROOM_COST);
        
        // Add shop #2.
        shopList.add(temp);
        
        // Define shop.
        temp = new Shop(HeroineEnum.ImgBackgroundEnum.IMG_BACK_INTERIOR, "Sage Therel", 
          HeroineEnum.ShopTypeEnum.SHOP_SPELL);
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "Fire magic is effective", "against undead and bone.");
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 1);
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_SPELL.getValue(), 
          HeroineEnum.SpellEnum.SPELL_BURN.getValue());
        
        // Add shop #3.
        shopList.add(temp);
        
        // Define shop.
        temp = new Shop(HeroineEnum.ImgBackgroundEnum.IMG_BACK_INTERIOR, "Woodsman", 
          HeroineEnum.ShopTypeEnum.SHOP_MESSAGE);
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "I'm staying right here", "until the sun comes back.");
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 1);
        
        // Add shop #4.
        shopList.add(temp);
        
        // Define shop.
        temp = new Shop(HeroineEnum.ImgBackgroundEnum.IMG_BACK_INTERIOR, "Stonegate Entrance", 
          HeroineEnum.ShopTypeEnum.SHOP_MESSAGE);
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "No one allowed in or", "out of the city.");
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "(The demo ends here.", "Thanks for playing!)");
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 1);
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 2);
        
        // Add shop #5.
        shopList.add(temp);
        
        // Define shop.
        temp = new Shop(HeroineEnum.ImgBackgroundEnum.IMG_BACK_INTERIOR, "Thomas the Fence", 
          HeroineEnum.ShopTypeEnum.SHOP_SPELL);
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "Unlock magic opens doors", "and harms automatons.");
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 1);
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_SPELL.getValue(), 
          HeroineEnum.SpellEnum.SPELL_UNLOCK.getValue());
        
        // Add shop #6.
        shopList.add(temp);
        
        // Define shop.
        temp = new Shop(HeroineEnum.ImgBackgroundEnum.IMG_BACK_INTERIOR, "Thieve's Guild", 
          HeroineEnum.ShopTypeEnum.SHOP_MESSAGE);
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "For a small fee we can", "sneak you to Stonegate.");
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "(Support indie devs!", "Full version soon!)");
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 1);
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 2);
        
        // Add shop #7.
        shopList.add(temp);
        
        // Define shop.
        temp = new Shop(HeroineEnum.ImgBackgroundEnum.IMG_BACK_TEMPEST, "A Nightmare", 
          HeroineEnum.ShopTypeEnum.SHOP_MESSAGE);
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "Darkness has overtaken", "the human realm.");
        temp.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.ARRAY_LIST, 
          "The monastery is no", "longer safe.");
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 1);
        temp.addTypeValue(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE.getValue(), 2);
        
        // Add shop #8.
        shopList.add(temp);
        
    }
    
    // slot = Slot / item number in shop.
    private void shop_act(int slot)
    {
        
        // The function performs an action based on the slot (button) clicked.
        
        Shop currShop; // Reference to current shop.
        
        // If exit button clicked, then...
        if (gameHD.dialog.options[slot].button == HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_EXIT)
            
            {
            // Exit button clicked.
                
            // Exit the shop / location / message.
            shop_exit();
            }
        
        else
            
            {
            // Something other than the exit button clicked.
                
            // Get reference to current shop.
            currShop = shopList.get(gameHD.dialog.shop_id.getValue());
            
            // Depending on shop / location type...
            switch (currShop.getShopType()) 
                
                {
                case SHOP_WEAPON:
                    
                    // Inside a weapon shop.
                    
                    // Try to purchase weapon.
                    shop_buy_weapon(HeroineEnum.WeaponEnum.valueOf(currShop.getTypeValue(slot).getValue()));
                    
                    // Exit selector.
                    break;
                    
                case SHOP_ARMOR:
                    
                    // Inside an armor shop.
                    
                    // Try to purchase armor.
                    shop_buy_armor(HeroineEnum.ArmorEnum.valueOf(currShop.getTypeValue(slot).getValue()));
                    
                    // Exit selector.
                    break;
                    
                case SHOP_SPELL:
                    
                    // Inside a spell shop.
                    
                    // Try to purchase spell.
                    shop_buy_spell(HeroineEnum.SpellEnum.valueOf(currShop.getTypeValue(slot).getValue()));
                    
                    // Exit selector.
                    break;
                    
                case SHOP_ROOM:
                    
                    // Inside a room / inn.
                    
                    // Try to purchase room for the night.
                    shop_buy_room(currShop.getTypeValue(slot).getValue());
                    
                    // Exit selector.
                    break;
                    
                default:
                    
                    // Display warning message.
                    System.out.println("Warning:  Unknown shop type!");
                    
                    // Exit selector.
                    break;
                    
                }
            
            }
        
    }
    
    // armor_id = Enumerated value for the armor to buy.  Matches to a ArmorEnum value.
    private void shop_buy_armor(HeroineEnum.ArmorEnum armor_id)
    {
        
        // The function handles logic related to purchasing an armor.
        
        int cost; // Cost associated with armor.
        
        // Get cost associated with armor.
        cost = armor_id.getValue_Gold();
        
        // If player owns sufficient gold, then...
        if (gameHD.avatar.getGold() >= cost)
            
            {
            // Player owns sufficient gold.
                
            // Subtract cost from player gold.
            gameHD.avatar.adjGold(cost);
            
            // Play purchase sound.
            gameHD.sounds.playSound(HeroineEnum.SoundEnum.SOUND_COIN);
            
            // Adjust player armor.
            gameHD.avatar.setArmor(armor_id);
            
            // Adjust dialog message.
            gameHD.dialog.message = "Bought " + armor_id.getValue_CleanText();
            
            // Rebuild dialog.
            shop_reset();
            }
        
    }
    
    // cost = Cost to stay in room for a night.
    private void shop_buy_room(int cost)
    {
        
        // The function handles logic related to staying in a room / inn for a night.
        
        // If player owns sufficient gold, then...
        if (gameHD.avatar.getGold() >= cost)
            
            {
            // Player owns sufficient gold.
                
            // Subtract cost from player gold.
            gameHD.avatar.adjGold(cost);
            
            // Play purchase sound.
            gameHD.sounds.playSound(HeroineEnum.SoundEnum.SOUND_COIN);
            
            // Adjust player spellbook.
            gameHD.avatar.avatar_sleep();
            
            // Adjust dialog message.
            gameHD.dialog.message = "You have rested";
            
            // Rebuild dialog.
            shop_reset();
            }
        
    }
    
    // armor_id = Enumerated value for the spell to buy.  Matches to a SpellEnum value.
    private void shop_buy_spell(HeroineEnum.SpellEnum spell_id)
    {
        
        // The function handles logic related to purchasing a spell.
        
        int cost; // Cost associated with spell.
        
        // Get cost associated with spell.
        cost = spell_id.getValue_Gold();
        
        // If player owns sufficient gold, then...
        if (gameHD.avatar.getGold() >= cost)
            
            {
            // Player owns sufficient gold.
                
            // Subtract cost from player gold.
            gameHD.avatar.adjGold(cost);
            
            // Play purchase sound.
            gameHD.sounds.playSound(HeroineEnum.SoundEnum.SOUND_COIN);
            
            // Adjust player spellbook.
            gameHD.avatar.setSpellbook(spell_id);
            
            // Adjust dialog message.
            gameHD.dialog.message = "Learned " + spell_id.getValue_CleanText();
            
            // Rebuild dialog.
            shop_reset();
            }
        
    }
    
    // weapon_id = Enumerated value for the weapon to buy.  Matches to a WeaponEnum value.
    private void shop_buy_weapon(HeroineEnum.WeaponEnum weapon_id)
    {
        
        // The function handles logic related to purchasing a weapon.
        
        int cost; // Cost associated with weapon.
        
        // Get cost associated with weapon.
        cost = weapon_id.getValue_Gold();
        
        // If player owns sufficient gold, then...
        if (gameHD.avatar.getGold() >= cost)
            
            {
            // Player owns sufficient gold.
                
            // Subtract cost from player gold.
            gameHD.avatar.adjGold(cost);
            
            // Play purchase sound.
            gameHD.sounds.playSound(HeroineEnum.SoundEnum.SOUND_COIN);
            
            // Adjust player weapon.
            gameHD.avatar.setWeapon(weapon_id);
            
            // Adjust dialog message.
            gameHD.dialog.message = "Bought " + weapon_id.getValue_CleanText();
            
            // Rebuild dialog.
            shop_reset();
            }
        
    }
    
    // slot = Slot / item number in shop.
    private void shop_clear_slot(int slot)
    {
        
        // The function clears the specified slot in the shop / room / location / message, ...
        
        // Clear slot.
        gameHD.dialog.addOption(HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_NONE, "", "");
        
    }
    
    private void shop_exit()
    {
        
        // The function handles logic for exiting the shop / location.
        
        // Play a click sound.
        gameHD.sounds.playSound(HeroineEnum.SoundEnum.SOUND_CLICK);
        
        // Switch to explore mode.
        gameHD.gameState = HeroineEnum.GameState.STATE_EXPLORE;
        
        // To Do:  Switch to ? screen, ...
        
    }
    
    private void shop_reset()
    {
        // The function rebuilds the dialog using the active shop identifier.
        
        // Rebuild dialog.
        shop_set(gameHD.dialog.shop_id);
    }
    
    // shop_id = Shop / location for which to display information.
    public void shop_set(HeroineEnum.ShopEnum shop_id)
    {
        
        // The function builds the dialog -- title and buttons and associated text.
        
        int counter; // Used to increment through messaging / shop types.
        HeroineEnum.ShopTypeEnum shopType; // Shop type -- maps to one of the ShopTypeEnum values.
        
        // Set defaults.
        counter = 0;
        
        // Update shop / location.
        gameHD.dialog.shop_id = shop_id;
        
        // Update shop name.
        gameHD.dialog.title = shopList.get(shop_id.getValue()).getMsgGroupName();
        
        // System.out.println("Shop " + shop_id + ": " + gameHD.dialog.title);
        
        // Loop through the type / value pairs associated with the shop.
        for (Shop.TypeValue typeValue : shopList.get(shop_id.getValue()).getTypeValueList())
        {
            
            // Convert the messaging type to the enumerated value.
            shopType = HeroineEnum.ShopTypeEnum.valueOf(typeValue.getMsgType());
            
            // Depending on the shop / location / messaging type, ...
            switch (shopType)
            {
                
                case SHOP_WEAPON:
                    
                    // Weapon shop.
                    
                    // Add weapon to shop.
                    shop_set_weapon(counter, HeroineEnum.WeaponEnum.valueOf(typeValue.getValue()));
                    
                    // Exit selector.
                    break;
                    
                case SHOP_ARMOR:
                    
                    // Armor shop.
                    
                    // Add weapon to shop.
                    shop_set_armor(counter, HeroineEnum.ArmorEnum.valueOf(typeValue.getValue()));
                    
                    // Exit selector.
                    break;
                    
                case SHOP_SPELL:
                    
                    // Spell shop.
                    
                    // Add weapon to shop.
                    shop_set_spell(counter, HeroineEnum.SpellEnum.valueOf(typeValue.getValue()));
                    
                    // Exit selector.
                    break;
                    
                case SHOP_ROOM:
                    
                    // Room.
                    
                    // Add room / inn.
                    shop_set_room(counter, typeValue.getValue());
                    
                    // Exit selector.
                    break;
                    
                case SHOP_MESSAGE:
                    
                    // Message.
                    
                    // Add message to location.
                    shop_set_message(counter, shopList.get(shop_id.getValue()).getMessageText(counter));
                    
                    // Exit selector.
                    break;
                    
                default:
                    
                    // Unknown shop / location / messaging type.
                
                    // Display error message.
                    System.out.println("Error:  Unknown shop type!");
                    
                    // Exit selector.
                    break;
                    
            } // End ... Depending on the shop / location / messaging type.
            
            // Increment counter.
            counter++;
            
        } // End ... Loop through the type / value pairs associated with the shop.
        
        // Loop through and clear any unused lines.
        while (counter < 2)
        {
            
            // Clear slot.
            shop_clear_slot(counter);
            
            // Increment counter.
            counter++;
            
        }
        
        System.out.println("Option 0, Msg 1: " + gameHD.dialog.options[0].msg1);
        System.out.println("Option 0, Msg 2: " + gameHD.dialog.options[0].msg2);
        System.out.println("Option 1, Msg 1: " + gameHD.dialog.options[1].msg1);
        System.out.println("Option 1, Msg 2: " + gameHD.dialog.options[1].msg2);
        
    }
    
    // slot = Slot / item number in shop.
    // armor_id = Armor identifier.
    private void shop_set_armor(int slot, HeroineEnum.ArmorEnum armor_id)
    {
        
        // The function handles adding an armor to a shop / location.
        
        String disable_reason; // Reason to not allow acquisition of armor.
        
        // Set defaults.
        disable_reason = "";
        
        // If player already owns armor, then...
        if (armor_id == gameHD.avatar.getArmor())
        
            {
            // Player already owns armor.
            
            // Specify disable reason.
            disable_reason = "(You own this)";
            }
        
        // Otherwise, if player armor better, then...
        else if (armor_id.getValue() < gameHD.avatar.getArmor().getValue())
            {
            // Player armor better.
            
            // Specify disable reason.
            disable_reason = "(Yours is better)";
            }
        
        // Add item to dialog for shop.
        shop_set_buy(slot, armor_id.getValue_CleanText(), armor_id.getValue_Gold(), disable_reason);
        
    }
    
    // slot = Slot / item number in shop.
    // name = Item name.
    // cost = Cost, in gold.
    // disable_reason = Reason to disable purchase option and associated text.
    private void shop_set_buy(int slot, String name, int cost, String disable_reason)
    {
        
        // The function stores dialog information for the current shop item.
        
        HeroineEnum.DialogButtonEnum button; // Button to display.
        boolean can_buy; // Whether item can be bought.
        boolean disable; // Whether a disable reason exists.
        String msg1; // First line of text to display near button.
        String msg2; // Second line of text to display near button.
        
        // Set defaults.
        can_buy = false;
        disable = false;
        
        // Specify first line of text.
        msg1 = "Buy " + name;
        
        // Specify second line of text.
        // Show the gold cost or the reason the item cannot be bought.
        
        // If item can be bought, then...
        if (disable_reason.length() == 0)
            
            {
            // Item can be bought.
            
            // Display item cost.
            msg2 = "for " + cost + " gold";
            }
        
        // Otherwise, ...
        else
        
            {
            // Item cannot be bought.
                
            // Display reason the item cannot be bought.
            msg2 = disable_reason;
            
            // Flag as disbled.
            disable = true;
            }
        
        // Display the dialog button if the item can be purchased.
        
        // If player owns sufficient gold to buy the item and no disable reason exists, then...
        if (gameHD.avatar.getGold() >= cost && !disable)
            
            {
            // Flag item as buyable.
            can_buy = true;
            }
        
        // If player can buy item, then...
        if (can_buy)
            
            {
            // Player can buy item.
                
            // Set to buy button.
            button = HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_BUY;
            }
        
        else
            
            {
            // Player cannot buy item.
                
            // Set to use no button.
            button = HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_NONE;
            }
        
        // Add option to dialog.
        gameHD.dialog.addOption(button, msg1, msg2);
        
        // Flag items for item.
        gameHD.dialog.items_for_sale = true;
        
    }
    
    // slot = Slot / item number in shop.
    // msg = Text to display.
    private void shop_set_message(int slot, String[] msg)
    {
        
        // The function handles adding messages to the dialog.
        
        // Add messages to dialog.
        gameHD.dialog.addOption(HeroineEnum.DialogButtonEnum.DIALOG_BUTTON_NONE, msg[0], msg[1]);
        
    }
    
    // slot = Slot / item number in shop.
    // room_cost = Cost to stay in room for a night.
    private void shop_set_room(int slot, int room_cost)
    {
        
        // The function handles adding dialog related to buying a room at an inn.
        
        String disable_reason; // Reason to not allow staying in room.
        
        // Set defaults.
        disable_reason = "";
        
        // If player already at maximum hit and magic points, then...
        if (gameHD.avatar.get_HpMp_AtMax())
            
            {
            // Player already at maximum hit and magic points.
            
            // Specify disable reason.
            disable_reason = "(You are well rested)";
            }
        
        // Add item to dialog for room / inn.
        shop_set_buy(slot, "Room for the night", room_cost, disable_reason);
        
    }
    
    // slot = Slot / item number in shop.
    // spell_id = Spell identifier.
    private void shop_set_spell(int slot, HeroineEnum.SpellEnum spell_id)
    {
        
        // The function handles adding a spell to a shop / location.
        
        String disable_reason; // Reason to not allow acquisition of spell.
        
        // Set defaults.
        disable_reason = "";
        
        // If player already knows spell, then...
        if (spell_id.getValue() <= gameHD.avatar.getSpellbook().getValue())
        
            {
            // Player already knows spell.
            
            // Specify disable reason.
            disable_reason = "(You know this)";
            }
        
        // Otherwise, if player lacks prior spell, then...
        else if (spell_id.getValue() > gameHD.avatar.getSpellbook().getValue() + 1)
            {
            // Player lacks prior spell.
            
            // Specify disable reason.
            disable_reason = "(Too advanced)";
            }
        
        // Add item to dialog for shop.
        shop_set_buy(slot, spell_id.getValue_CleanText(), spell_id.getValue_Gold(), disable_reason);
        
    }
    
    // slot = Slot / item number in shop.
    // weapon_id = Weapon identifier.
    private void shop_set_weapon(int slot, HeroineEnum.WeaponEnum weapon_id)
    {
        
        // The function handles adding a weapon to a shop / location.
        
        String disable_reason; // Reason to not allow acquisition of weapon.
        
        // Set defaults.
        disable_reason = "";
        
        // If player already owns weapon, then...
        if (weapon_id == gameHD.avatar.getWeapon())
        
            {
            // Player already owns weapon.
            
            // Specify disable reason.
            disable_reason = "(You own this)";
            }
        
        // Otherwise, if player weapon better, then...
        else if (weapon_id.getValue() < gameHD.avatar.getWeapon().getValue())
            {
            // Player weapon better.
            
            // Specify disable reason.
            disable_reason = "(Yours is better)";
            }
        
        // Add item to dialog for shop.
        shop_set_buy(slot, weapon_id.getValue_CleanText(), weapon_id.getValue_Gold(), disable_reason);
        
    }
    
}
