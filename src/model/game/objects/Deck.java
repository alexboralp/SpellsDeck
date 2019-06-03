/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.game.objects;

import java.io.Serializable;
import utils.Constants;

/**
 *
 * @author alexander
 */
public class Deck implements Serializable {
    Card deck[];
    int totalCards;

    public Deck() {
        deck = new Card[Constants.MAX_SELECTED_CARDS_NUMBER];
        totalCards = 0;
    }
    
    public boolean addCard(Card pCard) {
        if (totalCards < Constants.MAX_SELECTED_CARDS_NUMBER) {
            deck[totalCards] = pCard;
            totalCards++;
            return true;
        }
        return false;
    }
    
    public void removeCards() {
        for (int pos = 0; pos < deck.length; pos++) {
            deck[pos] = null;
            totalCards = 0;
        }
    }
    
    public Card getCard(int pPos) {
        if (pPos > -1 && pPos < Constants.MAX_SELECTED_CARDS_NUMBER) {
            return deck[pPos];
        }
        return null;
    }
    
}
