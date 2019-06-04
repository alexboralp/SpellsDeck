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
    
    RSA rsa;
    AES aes;
    
    public PGP() {
        super();
        rsa = new RSA();
        aes = new AES();
    }

    @Override
    public String encrypt(String pStrToEncrypt) {
        return aes.encrypt(pStrToEncrypt);
    }

    @Override
    public String decrypt(String pStrToDecrypt) {
        return aes.decrypt(pStrToDecrypt);
    }

    @Override
    public void makeKeys(String pKey) {
        rsa.makeKeys(pKey);
        aes.makeKeys(generateStupidKey(16));
        String aesPrivateKeyEncrypted = rsa.encrypt(Base64.getEncoder().encodeToString(aes.getPrivateKey()));
        super.setPrivateKey(rsa.getPrivateKey());
        this.setPublicKey(Base64.getDecoder().decode(aesPrivateKeyEncrypted));
    }

    @Override
    public void setPrivateKey(byte[] pKey) {
        super.setPrivateKey(pKey);
        rsa.setPrivateKey(pKey);
    }
    
    @Override
    public void setPublicKey(byte[] pKey) {
        super.setPublicKey(pKey);
        aes.setPrivateKey(Base64.getDecoder().decode(rsa.decrypt(Base64.getEncoder().encodeToString(this.getPublicKey()))));
    }
}
