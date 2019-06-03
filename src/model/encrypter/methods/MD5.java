/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.encrypter.methods;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import utils.Logger;

/**
 *
 * @author aborbon
 */
public class MD5 extends AbsEncrypter {

    @Override
    public String encrypt(String pStrToEncrypt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] encodedhash = digest.digest(pStrToEncrypt.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (NoSuchAlgorithmException ex) {
            Logger.Log(ex);
        }
        
        return null;
    }

    @Override
    public String decrypt(String pStrToDecrypt) {
        return null;
    }

    @Override
    public void makeKeys(String pKey) { }
    
}
