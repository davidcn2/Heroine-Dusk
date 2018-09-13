package heroinedusk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Shop 
{

    // Declare object variables.
    private ArrayList<messages> msgList; // List of message-related information.
    // messages msgList[] = new messages[4]; // List of message-related information.
    
    // Declare regular variables.
    private int whichItem; // Index of item for which to gather / return information.
    
    public Shop()
    {
        
        // The constructor initializes array lists.
        
        // Initialize ArrayList with messages.
        msgList = new ArrayList<>();
        
    }
    
    private class messages
    {
        
        // The inner class stores message-related information.
        // Information includes a type, group name, value, and one to many messages.
        // All messages include a type, group name, and at least one message.
        // Values are optional.
        
        private String message[]; // Collection of messages.
        private ArrayList<String> messageList; // Collection of messages, stored as an Arraylist.  Base 0.
        private HashMap<String, String> messageMap; // Collection of messages, stored as a HashMap.
        private String msgGroupName; // Messaging group name.
        private int msgType; // Messaging type.  Matches to a ShopEnum value.
        private Integer value; // Value associated with message group.
        
        public messages()
        {
        // Use the empty constructor when populating the ArrayList or HashMap for message text.
        // An additional constructor exists if explicitly passing a HashMap for message text.
        }
        
        // msgType = Messaging type.
        // msgGroupName = Messaging group name.
        // msg = One to many messages.
        public messages(int msgType, String msgGroupName, String ... msg)
        {
        // The constructor stores the passed type, group name, and messages in the class-level variables.
        // The constructor accepts the messages as a set of one to many parameter values.
        // The constructor accepts a list of values or simply an array for the message text.
        // The constructor copies the message text into an array.
        // Example for use:  x = new messages(3, "Axe", "Hello", "World");
        
        // Store messaging type.
        this.msgType = msgType;
        
        // Store messaging group name.
        this.msgGroupName = msgGroupName;
        
        // Initialize array -- based on passed elements.
        message = new String[msg.length];
        
        // Copy passed messages to class-level list.
        System.arraycopy(msg, 0, this.message, 0, msg.length);
        }
        
        // msgType = Messaging type.
        // msgGroupName = Messaging group name.
        // value = Value associated with message group.
        // msg = One to many messages.
        public messages(int msgType, String msgGroupName, Integer value, String ... msg)
        {
        // The constructor stores the passed type, group name, value, and messages in the class-level variables.
        // The constructor accepts the messages as a set of one to many parameter values.
        // The constructor accepts a list of values or simply an array for the message text.
        // The constructor copies the message text into an array.
        // Example for use:  x = new messages(3, "Axe", 6, "Hello", "World");
        
        // Call constructor.
        this(msgType, msgGroupName, msg);
        
        // Store value.
        this.value = value;
        }
        
        // msgType = Messaging type.
        // msgGroupName = Messaging group name.
        // msg = One to many messages, passed as a hash map.
        public messages(int msgType, String msgGroupName, HashMap<String, String> msg)
        {
        // The constructor stores the passed type, group name, and messages in the class-level variables.
        // The constructor accepts the messages as a hash map.
        // The constructor copies the message text into a hash map.
        // Example for use:  x = new messages(3, "Axe", msgMap);
        
        // Store messaging type.
        this.msgType = msgType;
        
        // Store messaging group name.
        this.msgGroupName = msgGroupName;
        
        // Initialize the hash map.
        messageMap = new HashMap<>();
        
        // Copy passed messages to class-level hash map.
        messageMap.putAll(msg);
        }
        
        // msgType = Messaging type.
        // msgGroupName = Messaging group name.
        // value = Value associated with message group.
        // msg = One to many messages, passed as a hash map.
        public messages(int msgType, String msgGroupName, Integer value, HashMap<String, String> msg)
        {
        // The constructor stores the passed type, group name, value, and messages in the class-level variables.
        // The constructor accepts the messages as a hash map.
        // The constructor copies the message text into a hash map.
        // Example for use:  x = new messages(3, "Axe", 8, msgMap);
        
        // Call constructor.
        this(msgType, msgGroupName, msg);
            
        // Store value.
        this.value = value;
        }
        
        // msgType = Messaging type.
        // msgGroupName = Messaging group name.
        // msg = One to many messages.
        public void setMessagesList(int msgType, String msgGroupName, String ... msg)
        {
        // The function stores the passed type, group name, and messages in the class-level variables.
        // The function accepts the messages as a set of one to many parameter values.
        // The function accepts a list of values or simply an array for the message text.
        // The function copies the message text into an ArrayList.
        // Example for use:  x = new messagesList(3, "Axe", "Hello", "World");
        
        // Store messaging type.
        this.msgType = msgType;
        
        // Store messaging group name.
        this.msgGroupName = msgGroupName;
        
        // Initialize ArrayList -- will support more than length elements.
        messageList = new ArrayList<>(msg.length);
        
        // Copy passed messages to class-level list.
        Collections.addAll(messageList, msg);
        }
        
        // msgType = Messaging type.
        // msgGroupName = Messaging group name.
        // value = Value associated with message group.
        // msg = One to many messages.
        public void setMessagesList(int msgType, String msgGroupName, Integer value, String ... msg)
        {
        // The function stores the passed type, group name, value, and messages in the class-level variables.
        // The function accepts the messages as a set of one to many parameter values.
        // The function accepts a list of values or simply an array for the message text.
        // The function copies the message text into an ArrayList.
        // Example for use:  x = new messagesList(3, "Axe", 7, "Hello", "World");
        
        // Call similar method.
        setMessagesList(msgType, msgGroupName, msg);
        
        // Store value.
        this.value = value;
        }
        
        // msgType = Messaging type.
        // msgGroupName = Messaging group name.
        // msg = One to many messages.  Passed as key, value pairs -- 1 = key, 2 = value, 3 = key, 4 = value, ...
        public void setMessagesMap(int msgType, String msgGroupName, String ... msg)
        {
        // The function stores the passed type, group name, and messages in the class-level variables.
        // The function accepts the messages (and keys) as a set of one to many parameter values.
        // The function accepts a list of values or simply an array for the keys and message text.
        // The function copies the message text into a hash map.
        // Example for use:  x = new messagesList(3, "Axe", "Hello", "World");
        
        int counter; // Used to loop through messages.
        int msgCount; // Number of messages passed.
            
        // Store messaging type.
        this.msgType = msgType;
        
        // Store messaging group name.
        this.msgGroupName = msgGroupName;
        
        // Initialize the hash map.
        messageMap = new HashMap<>();
        
        // Get number of messages passed.
        msgCount = msg.length;
        
        // Loop through passed message array.
        for (counter = 0; counter < msgCount; counter += 2)
            {
            // Add next pair to hash map.
            messageMap.put(msg[counter], msg[counter + 1]);
            }
        }
        
        // msgType = Messaging type.
        // msgGroupName = Messaging group name.
        // value = Value associated with message group.
        // msg = One to many messages.  Passed as key, value pairs -- 1 = key, 2 = value, 3 = key, 4 = value, ...
        public void setMessagesMap(int msgType, String msgGroupName, Integer value, String ... msg)
        {
        // The function stores the passed type, group name, value, and messages in the class-level variables.
        // The function accepts the messages (and keys) as a set of one to many parameter values.
        // The function accepts a list of values or simply an array for the keys and message text.
        // The function copies the message text into a hash map.
        // Example for use:  x = new messagesList(3, "Axe", 5, "Hello", "World");
        
        // Call similar method.
        setMessagesMap(msgType, msgGroupName, msg);
        
        // Store value.
        this.value = value;
        }
        
        // Getters and setters below...
        
        public int getMsgType() {
            return msgType;
        }
        
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
        
        public String getGroupName() {
            return msgGroupName;
        }
        
        public Integer getValue() {
            return value;
        }
        
    } // End of inner class.
    
    // msgType = Messaging type.
    // msgGroupName = Messaging group name.
    // msg = One to many messages.
    public void addMessage(int msgType, String msgGroupName, String ... msg)
    {
        
        // The function adds an item to the messaging-information list (array).
        // An item can contain one to many pieces of message text.
        // Example for use:  x.addMessage(2, "Axe", "Hello", "World");
        
        // Add the passed messaging information to the list.
        msgList.add(new messages(msgType, msgGroupName, msg));
        
    }
    
    // msgType = Messaging type.
    // msgGroupName = Messaging group name.
    // value = Value associated with message group.
    // msg = One to many messages.
    public void addMessage(int msgType, String msgGroupName, Integer value, String ... msg)
    {
        
        // The function adds an item to the messaging-information list (array).
        // An item can contain one to many pieces of message text.
        // Example for use:  x.addMessage(2, "Axe", 7, "Hello", "World");
        
        // Add the passed messaging information to the list.
        msgList.add(new messages(msgType, msgGroupName, value, msg));
        
    }
    
    // msgType = Messaging type.
    // msgGroupName = Messaging group name.
    // msg = One to many messages.
    public void addMessageList(int msgType, String msgGroupName, String ... msg)
    {
        
        // The function adds an item to the messaging-information list (ArrayList).
        // An item can contain one to many pieces of message text.
        // Example for use:  x.addMessageList(2, "Axe", "Hello", "World");
        
        messages temp; // Reference to message to add to list.
        
        // Add the passed messaging information to the list.
        temp = new messages();
        temp.setMessagesList(msgType, msgGroupName, msg);
        msgList.add(temp);
        
    }
    
    // msgType = Messaging type.
    // msgGroupName = Messaging group name.
    // msg = One to many messages, passed as a hash map.
    public void addMessageMap(int msgType, String msgGroupName, HashMap<String, String> msgMap)
    {
        
        // The function adds an item to the messaging-information list (HashMap).
        // An item can contain one to many pieces of message text.
        // Example for use:  x.addMessageMap(3, msgMap);
        
        messages temp; // Reference to message to add to list.
        
        // Add the passed messaging information to the list.
        temp = new messages(msgType, msgGroupName, msgMap);
        msgList.add(temp);
        
    }
    
    // Getters and setters below...
        
    public int getMsgType() {
        return msgList.get(whichItem).msgType;
    }

    // itemNbr = Index of item for which to gather / return information.
    public int getMsgType(int itemNbr) {
        return msgList.get(itemNbr).msgType;
    }
        
    // Handle arrays...

    // whichMessage = Array index of the message to return.  One to many messages per item.
    public String getMessage(int whichMessage) {
        return msgList.get(whichItem).getMessage(whichMessage);
    }

    // itemNbr = Index of item for which to gather / return information.
    // whichMessage = Array index of the message to return.  One to many messages per item.
    public String getMessage(int itemNbr, int whichMessage) {
        return msgList.get(itemNbr).getMessage(whichMessage);
    }

    public String[] getMessages() {
        return msgList.get(whichItem).getMessages();
    }
        
    // itemNbr = Index of item for which to gather / return information.
    public String[] getMessages(int itemNbr) {
        return msgList.get(itemNbr).getMessages();
    }

    // Handle array lists...

    // whichMessage = Array index of the message to return.  One to many messages per item.
    public String getMessageList(int whichMessage) {
        return msgList.get(whichItem).getMessageList(whichMessage);
    }
        
    // itemNbr = Index of item for which to gather / return information.
    // whichMessage = Array index of the message to return.  One to many messages per item.
    public String getMessageList(int itemNbr, int whichMessage) {
        return msgList.get(itemNbr).getMessageList(whichMessage);
    }

    public ArrayList<String> getMessagesList() {
        return msgList.get(whichItem).getMessagesList();
    }

    // itemNbr = Index of item for which to gather / return information.
    public ArrayList<String> getMessagesList(int itemNbr) {
        return msgList.get(itemNbr).getMessagesList();
    }
        
    // Handle hash maps...

    // messageKey = Key for message to return.
    public String getMessageMap(String messageKey) {
        return msgList.get(whichItem).getMessageMap(messageKey);
    }

    // itemNbr = Index of item for which to gather / return information.
    // messageKey = Key for message to return.
    public String getMessageMap(int itemNbr, String messageKey) {
        return msgList.get(itemNbr).getMessageMap(messageKey);
    }

    public HashMap<String, String> getMessagesMap() {
        return msgList.get(whichItem).getMessagesMap();
    }
        
    // itemNbr = Index of item for which to gather / return information.
    public HashMap<String, String> getMessagesMap(int itemNbr) {
        return msgList.get(itemNbr).getMessagesMap();
    }
        
    // Handle others...
    
    public Integer getValue() {
        return msgList.get(whichItem).getValue();
    }
    
    // itemNbr = Index of item for which to gather / return information.
    public Integer getValue(int itemNbr) {
        return msgList.get(itemNbr).getValue();
    }
    
    public String getGroupName() {
        return msgList.get(whichItem).getGroupName();
    }
    
    // itemNbr = Index of item for which to gather / return information.
    public String getGroupName(int itemNbr) {
        return msgList.get(itemNbr).getGroupName();
    }
    
    // whichItem = Array index of item for which to gather information.
    public void setMessagesItem(int whichItem)
    {
        // Stores the index of the item for which to gather / return information.
        this.whichItem = whichItem;
    }
     
}