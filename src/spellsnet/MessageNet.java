/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellsnet;

import java.util.HashMap;
import utils.Constants;

/**
 *
 * @author aborbon
 */
public class MessageNet implements Constants{
    private HashMap<String, String> values;
    private MessageType type;
    
    public MessageNet(String pData) {
        String[] msgValues = pData.split(MESSAGE_SEPARATOR);
        if (msgValues != null && msgValues.length > 0) {
            type = MessageType.valueOf(msgValues[0]);
            values = new HashMap<>();
            for (int valuesIndex = 1; valuesIndex < msgValues.length; valuesIndex++) {
                String[] keyMap = msgValues[valuesIndex].split(MESSAGE_VALUES_SEPARATOR);
                values.put(keyMap[0], keyMap[1]);
            }
        }
    }
    
    public MessageNet (MessageType pType) {
        this.type = pType;
        values = new HashMap<>();
    }
    
    public String getValue (String pKey) {
        String result = "";
        result = values.containsKey(pKey) ? values.get(pKey): result;
        return result;
    }
    
    public void addValue (String pKey, String pValue) {
        values.put(pKey, pValue);
    }
    
    public String getStringMsg() {
        String result = "";
        String comma = "";
        
        result += this.type.toString() + MESSAGE_SEPARATOR;
        
        for (String key : values.keySet()) {
            result += comma;
            result += key + MESSAGE_VALUES_SEPARATOR + values.get(key);
            comma = MESSAGE_SEPARATOR;
        }
        
        return result;
    }

    public MessageType getType() {
        return type;
    }
    
    
}
