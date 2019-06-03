/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.socketspellsdeck.message;

import java.io.Serializable;

/**
 *
 * @author alexander
 */
public class Message extends AbsMessage implements Serializable {
    
    private String id;

    public Message() {
        super();
    }

    public Message(TIPO tipo, String message) {
        super(tipo, message);
    }
    
    public Message(TIPO tipo, String message, String pId) {
        super(tipo, message);
        id = pId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
}
