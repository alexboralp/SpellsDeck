/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.game.objects;

import utils.Constants;

/**
 *
 * @author aborbon
 */
public class TotalDeck {
    Card cards[];
    int totalCards;

    public TotalDeck() {
        cards = new Card[Constants.TOTAL_CARDS];
        totalCards = 0;
    }
    
    public boolean addCard(Card pCard) {
        if (totalCards < Constants.TOTAL_CARDS) {
            cards[totalCards] = pCard;
            totalCards++;
            return true;
        }
        return false;
    }
    
    public void removeCards() {
        for (int pos = 0; pos < cards.length; pos++) {
            cards[pos] = null;
            totalCards = 0;
        }
    }
    
    public Card getCard(int pPos) {
        if (pPos > -1 && pPos < Constants.TOTAL_CARDS) {
            return cards[pPos];
        }
        return null;
    }
    
    public boolean createNewDeck (String[] pFileNames, String[] pTipos) {
        boolean ok = true;
        if (Constants.TOTAL_CARDS == pFileNames.length && pFileNames.length == pTipos.length) {
            for (int pos = 0; pos < pFileNames.length; pos++) {
                cards[pos] = new Card();
                ok = ok && cards[pos].createCard(pFileNames[pos], pTipos[pos]);
            }
        }
        return ok;
    }
}
