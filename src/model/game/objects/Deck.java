/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.game.objects;

/**
 *
 * @author alexander
 */
public class Deck {
    Card deck[];
    int totalCards;

    public Deck() {
        deck = new Card[3];
        totalCards = 0;
    }
    
    public boolean addCard(Card pCard) {
        if (totalCards < 3) {
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
        if (pPos > 0 && pPos < 4) {
            return deck[pPos - 1];
        }
        return null;
    }
    
}
