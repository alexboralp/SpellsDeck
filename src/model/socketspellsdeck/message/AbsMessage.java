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
public abstract class AbsMessage implements IMessage, Serializable{
    
    private TIPO tipo;
    private Object message;

    public AbsMessage() { }

    public AbsMessage(TIPO tipo, String message) {
        this.tipo = tipo;
        this.message = message;
    }

    @Override
    public TIPO getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(TIPO tipo) {
        this.tipo = tipo;
    }

    @Override
    public Object getMessage() {
        return message;
    }

    @Override
    public void setMessage(Object pMessage) {
        message = pMessage;
    }
    
}
