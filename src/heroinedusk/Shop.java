package heroinedusk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Shop 
{

    /*
    The class stores information about a shop / location / other messaging element.
    
    Inner classes include:
    
    Messages:  Encapsulates messaging information -- type and zero to many messages.
    TypeValue:  Encapsulate a type and value pair.
    
    Methods include:
    
    addMessage:  Adds an item to the messaging-information list -- msgList.  Supports arrays, array lists, and hash maps.
    addMessageMap:  Adds an item to the messaging-information list -- msgList -- copying from and to a hash map.
    addTypeValue:  Adds a type / value pair.
    getBackground:  Gets the background value associated with the shop / location / other messaging element.
    getMessages:  Gets the specified messaging-information item from the list -- msgList.
    getMessagesList:  Gets the entire list of messaging-information items.
    getMsgGroupName:  Gets the messaging group name.  Usually identifies the shop or location.
    getTypeValue:  Gets the specified type / value pair.
    getTypeValueList:  Gets all of the type / value pairs.
    setBackground:  Sets the background value associated with the shop / location / other messaging element.
    setMsgGroupName:  Sets the messaging group name.  Usually identifies the shop or location.
    setValue_TypeValueList:  Updates the value for the specified type / value pair.
    */
    
    // Declare regular variables.
    private HeroineEnum.ImgBackgroundEnum background; // Background type.
    private String msgGroupName; // Messaging group name.
    private HeroineEnum.ShopTypeEnum shopType; // Overall shop / location / message type.
    
    // Declare list variables.
    private ArrayList<Messages> msgList; // List of message-related information.
    private ArrayList<TypeValue> typeValueList; // Collection of type / value pairs.
    
    // background = Background type.
    // msgGroupName = Messaging group name.
    // shopType = Overall shop / location / message type.
    public Shop(HeroineEnum.ImgBackgroundEnum background, String msgGroupName, HeroineEnum.ShopTypeEnum shopType)
    {
    
        // The constructor sets the required values (group name and background type) and performs initializations.
        
        // Set required values.
        this.background = background;
        this.msgGroupName = msgGroupName;
        this.shopType = shopType;
        
        // Initialize arrays and array lists.
        msgList = new ArrayList<>();
        typeValueList = new ArrayList<>();
        
    }
    
    // Inner classes below...
    
    public class Messages
    {
        
        // The inner class stores message-related information.
        
        // Declare regular variables.
        private final HeroineEnum.ShopTypeEnum msgType; // Messaging type.  Matches to a ShopTypeEnum value.  Examples:  SHOP_MESSAGE, ...
        
        // Declare list variables.
        private String message[]; // Collection of messages.
        private ArrayList<String> messageList; // Collection of messages, stored as an Arraylist.  Base 0.
        private HashMap<String, String> messageMap; // Collection of messages, stored as a HashMap.
        
        /*
        msgType = Messaging type.  Matches to a ShopTypeEnum value.  Examples:  SHOP_MESSAGE, ...
        listType = List type (standard array, array list, or hash map) in which to store messages  
          Matches to a ListEnum value.  Examples:  STD_ARRAY, ARRAY_LIST, HASH_MAP.
        msg = One to many messages.  For hash maps --> Passed as key, value pairs -- 1 = key, 2 = value, 3 = key, 4 = value, ...
        */
        public Messages(HeroineEnum.ShopTypeEnum msgType, HeroineEnum.ListEnum listType, String ... msg)
        {
            
            // The constructor stores the messages type and messages.
            // The list type parameter determines the type of list object to use for storing the messages.
            // The function accepts a list of values or simply an array for the messaging text (and keys for hash maps).
            // Example for use:  x = new Messages(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.STD_ARRAY, "Hello", "World");
            
            // Store messaging type.
            this.msgType = msgType;
            
            // Depending on how storing messages (list type), ...
            switch (listType)
            {
                
                case STD_ARRAY:
                
                    // Store messages in a standard array.
                
                    // Initialize array -- based on passed elements.
                    message = new String[msg.length];

                    // Copy passed messages to class-level list.
                    System.arraycopy(msg, 0, this.message, 0, msg.length);
                    
                    // Exit selector.
                    break;
                    
                case ARRAY_LIST:
                    
                    // Store messages in an array list.
                    // Bonus:  Stores sames messages in standard array.

                    // Initialize ArrayList -- will support more than length elements.
                    messageList = new ArrayList<>(msg.length);

                    // Copy passed messages to class-level list.
                    Collections.addAll(messageList, msg);

                    // Initialize standard array to hold messages.
                    message = new String[messageList.size()];

                    // Convert from array list to array, storing in new variable, messages.
                    messageList.toArray(message);
                    
                    // Exit selector.
                    break;
                    
                case HASH_MAP:
                    
                    // Store messages in a hash map.
            
                    int counter; // Used to loop through messages.
                    int msgCount; // Number of messages passed.

                    // Initialize the hash map.
                    messageMap = new HashMap<>();

                    // Get number of messages passed.
                    msgCount = msg.length;

                    // Loop through passed message array.
                    for (counter = 0; counter < msgCount; counter += 2)
                        {
                        // Add next pair to hash map.
                        // Counter = key, counter + 1 = value (message text).
                        messageMap.put(msg[counter], msg[counter + 1]);
                        }
                    
                    // Exit selector.
                    break;
                    
                default:
                    
                    // Exit selector.
                    break;
                    
            } // End ... Depending on how storing messages (list type).
            
        } // End of constructor.
        
        /*
        msgType = Messaging type.  Matches to a ShopTypeEnum value.  Examples:  SHOP_MESSAGE, ...
        msg = One to many messages, passed as a hash map.
        */
        public Messages(HeroineEnum.ShopTypeEnum msgType, HashMap<String, String> msg)
        {
            
            // The constructor stores the messages type and messages.
            // The constructor accepts the messages as a hash map.
            // The constructor copies the message text into a hash map.
            // Example for use:  x = new Messages(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, msgMap);
            
            // Store messaging type.
            this.msgType = msgType;
            
            // Initialize the hash map.
            messageMap = new HashMap<>();

            // Copy passed messages to class-level hash map.
            messageMap.putAll(msg);
                
        }
        
        // Getters and setters below...
        
        // Handle arrays...
        
        // whichMessage = Array index of the message to return.
        public String getMessage(int whichMessage) {
            return message[whichMessage];
        }
        
        public String[] getMessages() {
            return message;
        }
        
        // Handle array lists...
        
        // whichMessage = Array index of the message to return.
        public String getMessageList(int whichMessage) {
            return messageList.get(whichMessage);
        }
        
        public ArrayList<String> getMessagesList() {
            return messageList;
        }
        
        // Handle hash maps...
        
        // messageKey = Key for message to return.
        public String getMessageMap(String messageKey) {
            return messageMap.get(messageKey);
        }
        
        public HashMap<String, String> getMessagesMap() {
            return messageMap;
        }
        
        // Handle others...
        
        public HeroineEnum.ShopTypeEnum getMsgType() {
            return msgType;
        }
        
    }
    
    public class TypeValue
    {
        
        // The inner class stores type / value pairs.
        // For messages, the value is (usually) an incrementing counter, one for each message.
        
        // Declare regular variables.
        private final int msgType; // Type.  Matches to a ShopTypeEnum value.  Examples:  SHOP_WEAPON, SHOP_ARMOR, SHOP_ROOM, ...
        private Integer value; // Value.
        
        // msgType = Type.  Examples:  SHOP_WEAPON, SHOP_ARMOR, SHOP_ROOM, ...
        // value = Value.
        public TypeValue(int msgType, Integer value)
        {
            
            // The constructor sets the type and value.
            this.msgType = msgType;
            this.value = value;
            
        }
        
        // Getters and setters below...
        
        public int getMsgType() {
            return msgType;
        }
        
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
        
    }
    
    // Methods below...
    
    /*
    msgType = Messaging type.  Matches to a ShopTypeEnum value.  Examples:  SHOP_MESSAGE, ...
    listType = List type (standard array, array list, or hash map) in which to store messages  
      Matches to a ListEnum value.  Examples:  STD_ARRAY, ARRAY_LIST, HASH_MAP.
    msg = One to many messages.  For hash maps --> Passed as key, value pairs -- 1 = key, 2 = value, 3 = key, 4 = value, ...
    */
    public void addMessage(HeroineEnum.ShopTypeEnum msgType, HeroineEnum.ListEnum listType, String ... msg)
    {
        
        // The function adds an item to the messaging-information list (array, array list, or hash map).
        // An item contains a type and zero to many pieces of message text.
        // Example for use:  x.addMessage(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, HeroineEnum.ListEnum.STD_ARRAY, "Hello", "World");

        // Add the passed messaging information to the list.
        msgList.add(new Messages(msgType, listType, msg));
        
    }
    
    // msgType = Messaging type.  Matches to a ShopTypeEnum value.  Examples:  SHOP_MESSAGE, ...
    // msg = One to many messages, passed as a hash map.
    public void addMessageMap(HeroineEnum.ShopTypeEnum msgType, HashMap<String, String> msgMap)
    {
        
        // The function adds an item to the messaging-information list (HashMap).
        // An item contains a type and zero to many pieces of message text.
        // Example for use:  x.addMessageMap(HeroineEnum.ShopTypeEnum.SHOP_MESSAGE, msgMap);
        
        // Add the passed messaging information to the list.
        msgList.add(new Messages(msgType, msgMap));
        
    }
    
    // msgType = Type.  Examples:  SHOP_WEAPON, SHOP_ARMOR, SHOP_ROOM, ...
    // value = Value.
    public void addTypeValue(int msgType, Integer value)
    {
        
        // The function adds a type / value pair.
        
        // Add type / value pair.
        typeValueList.add(new TypeValue(msgType, value));
        
    }
            
    // Getters and setters below...
    
    public HeroineEnum.ImgBackgroundEnum getBackground() {
        return background;
    }

    public void setBackground(HeroineEnum.ImgBackgroundEnum background) {
        this.background = background;
    }
    
    public Messages getMessages(int whichItem) {
        // Example for use:  y = x.getMessages(0).getMsgType();
        return msgList.get(whichItem);
    }
    
    public ArrayList<Messages> getMessagesList() {
        return msgList;
    }
    
    public String[] getMessageText(int whichItem) {
        // Returns the messages (text) related to the specified item as a standard array.
        // Example for use -- stores as standard array:  String[] x = shopInfo.get(shop_id).getMessageText(counter);
        // Example for use -- stores as single String:  String y = String.join(" ", Arrays.asList(shopInfo.get(shop_id).getMessageText(counter)));
        return msgList.get(whichItem).getMessages();
    }
    
    public String getMsgGroupName() {
        return msgGroupName;
    }

    public void setMsgGroupName(String msgGroupName) {
        this.msgGroupName = msgGroupName;
    }
    
    public HeroineEnum.ShopTypeEnum getShopType() {
        return shopType;
    }

    public void setShopType(HeroineEnum.ShopTypeEnum shopType) {
        this.shopType = shopType;
    }
    
    // whichItem = Index of desired type / value pair in array list.
    public TypeValue getTypeValue(int whichItem) {
        // Example for use:  y = x.getTypeValue(0).getMsgType();
        return typeValueList.get(whichItem);
    }
    
    public ArrayList<TypeValue> getTypeValueList() {
        return typeValueList;
    }
    
    // whichItem = Index of desired type / value pair in array list.
    // value = Value to assign to specified item in array list.
    public void setValue_TypeValueList(int whichItem, int value) {
        typeValueList.get(whichItem).value = value;
    }
    
}
