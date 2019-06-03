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
public class SpellsBook {
    
    byte spells[][];
    byte keys[][];
    
    public SpellsBook () {
        spells = new byte[Constants.SPELLS_NUMBER][Constants.SPELL_BYTE_LENGTH];
        keys = new byte[Constants.SPELLS_NUMBER][Constants.KEY_BYTE_LENGTH];
    }
}
