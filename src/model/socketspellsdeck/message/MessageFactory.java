/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.socketspellsdeck.message;

import model.socketspellsdeck.message.IMessage.TIPO;

/**
 *
 * @author alexander
 */
public class MessageFactory{
    
    public enum TIPO_MENSAJE {
        WAITING_FOR_SECOND_PLAYER,
        WAITING_FOR_SECOND_PLAYERS_DECK,
        SELECT_DECK,
        ENEMY_DECK,
        START_GAME,
        START_PLAYING,
        CARD_ENEMY_CRACKED,
        CARD_YOURS_CRACKED,
        // HALF_KEY,
        FINAL_MESSAGE,
        WIN_ONE_MATCH,
        LOSE_ONE_MATCH,
        WINNER,
        LOOSER,
        SECOND_PLAYER_DESCONECTED,
        WRONG_NUMBER_OF_CARDS,
        SAVE_FINAL_MESSAGE
        //RESTART_MATCH
        
        //TWO_PLAYERS_READY,
        //START_DECK,
        //ALL_SET,
        
        
        
        
    }
    
    /*public MessageFactory() {
        super();
    }*/
    
    public static Message createMessage(String pId, TIPO pTipo, Object pTipoMensaje) {
        return new Message(pId, pTipo, pTipoMensaje);
    }
    
}
