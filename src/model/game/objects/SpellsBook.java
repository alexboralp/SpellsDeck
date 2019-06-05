/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.game.objects;

import java.util.Arrays;
import utils.Constants;
import utils.OS;
import utils.ReadWriteFiles;

/**
 *
 * @author aborbon
 */
public class SpellsBook {
    
    byte spells[][];
    byte spellsSHA256[][];
    
    public SpellsBook () {
        spells = new byte[Constants.SPELLS_NUMBER][Constants.SPELL_BYTE_LENGTH];
        spellsSHA256 = new byte[Constants.SPELLS_NUMBER][Constants.KEY_BYTE_LENGTH];
    }
    
    public void readSpellsBook () {
        String pFileTxtFile = ClassLoader.getSystemResource("model/game/SpellsBook.bin").getFile();
        if (OS.isWindows()) {
            pFileTxtFile = pFileTxtFile.substring(1);
        }
        byte[] array = ReadWriteFiles.readCompleteBinaryFile(pFileTxtFile);
        
        int longitudPaso = Constants.SPELL_BYTE_LENGTH + Constants.KEY_BYTE_LENGTH;
        for (int pos = 0; pos < Constants.SPELLS_NUMBER; pos ++) {
            int begin = pos * longitudPaso;
            int end1 = begin + Constants.SPELL_BYTE_LENGTH;
            int end2 = begin + Constants.SPELL_BYTE_LENGTH + Constants.KEY_BYTE_LENGTH;
            spells[pos] = Arrays.copyOfRange(array, begin, end1);
            spellsSHA256[pos] = Arrays.copyOfRange(array, end1, end2);
        }
    }
    
    public byte[] getSpell(int pSpellNumber) {
        if (pSpellNumber > -1 && pSpellNumber < Constants.SPELLS_NUMBER) {
            return spells[pSpellNumber];
        }
        return null;
    }
    
    public byte[] getSpellsSHA256(int pKeyNumber) {
        if (pKeyNumber > -1 && pKeyNumber < Constants.SPELLS_NUMBER) {
            return spellsSHA256[pKeyNumber];
        }
        return null;
    }
}
