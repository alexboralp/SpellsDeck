/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.encrypter.methods;


import java.util.Base64;

/**
 *
 * @author aborbon
 */
public abstract class AbsEncrypter implements IEncrypter{
    private byte[] privateKey;
    private byte[] publicKey;
    
    public AbsEncrypter() {
        privateKey = null;
        publicKey = null;
    }
    
    @Override
    public void setPrivateKey(String pKey) {
        privateKey = Base64.getDecoder().decode(pKey);
    }
    
    @Override
    public void setPublicKey(String pKey) {
        publicKey = Base64.getDecoder().decode(pKey);
    }
    
    @Override
    public abstract String encrypt(String pStrToEncrypt);
    
    @Override
    public abstract String decrypt(String pStrToDecrypt);
    
    @Override
    public abstract void makeKeys(String pKey);
    
    @Override
    public String getPrivateKey() {
        return Base64.getEncoder().encodeToString(privateKey);
    }
    
    @Override
    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(publicKey);
    }
}
