/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.socketspellsdeck.message;

/**
 *
 * @author alexander
 */
public interface IMessage {
    
    public enum TIPO {
        MESSAGE_FROM_SERVER,
        MESSAGE_FROM_CLIENT,
        CLOSE_CONNECTION
    };

    public TIPO getTipo();
    public void setTipo(TIPO pTipo);
    public Object getMessage();
    public void setMessage(Object pMessage);
}
