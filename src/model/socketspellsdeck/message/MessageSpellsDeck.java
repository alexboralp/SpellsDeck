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
public class MessageSpellsDeck extends Message{
    
    public enum TIPO_SPELLS_DECK {
        START_DECK,
        SEND_CARD,
        FINAL_MESSAGE,
        CARD_CRACKED
    }
    
}
