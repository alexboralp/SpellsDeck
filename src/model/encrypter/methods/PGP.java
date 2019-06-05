/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.encrypter.methods;

import java.util.Base64;
import utils.Logger;

/**
 *
 * @author aborbon
 */
public class PGP extends AbsEncrypter {
    
    private final RSA rsa;
    private final AES aes;
    private boolean ok;
    
    public PGP() {
        super();
        rsa = new RSA();
        aes = new AES();
        ok = true;
    }

    @Override
    public String encrypt(String pStrToEncrypt) {
        try{
            if(ok){
                return aes.encrypt(pStrToEncrypt);
            }
        } catch(Exception ex) { 
            Logger.Log(ex);
        }
        return "";
    }

    @Override
    public String decrypt(String pStrToDecrypt) {
        try{
            if(ok){
                return aes.decrypt(pStrToDecrypt);
            }
        } catch(Exception ex) { 
            Logger.Log(ex);
        }
        return "";
    }

    @Override
    public void makeKeys(String pKey) {
        try {
            rsa.makeKeys(pKey);
            aes.makeKeys(generateStupidKey(16));
            String aesPrivateKeyEncrypted = rsa.encrypt(Base64.getEncoder().encodeToString(aes.getPrivateKey()));
            super.setPrivateKey(rsa.getPrivateKey());
            this.setPublicKey(Base64.getDecoder().decode(aesPrivateKeyEncrypted));
            ok = true;
        } catch(Exception ex) {
            ok = false;
        }
    }

    @Override
    public void setPrivateKey(byte[] pKey) {
        try {
            super.setPrivateKey(pKey);
            rsa.setPrivateKey(pKey);
            ok = true;
        } catch(Exception ex) {
            ok = false;
        }
    }
    
    @Override
    public void setPublicKey(byte[] pKey) {
        try {
            super.setPublicKey(pKey);
            aes.setPrivateKey(Base64.getDecoder().decode(rsa.decrypt(Base64.getEncoder().encodeToString(this.getPublicKey()))));
            ok = true;
        } catch(Exception ex) {
            ok = false;
        }
    }
}
