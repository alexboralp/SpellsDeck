/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encrypter.methods;

/**
 *
 * @author aborbon
 */
public abstract class AbsEncrypter implements IEncrypter{
    private byte[] privateKey;
    byte[] publicKey;
    
    public AbsEncrypter() {
        privateKey = null;
        publicKey = null;
    }
    
    @Override
    public void setPrivateKey(byte[] pKey) {
        privateKey = pKey;
    }
    
    @Override
    public void setPublicKey(byte[] pKey) {
        publicKey = pKey;
    }
    
    @Override
    public abstract String encrypt(String pStrToEncrypt);
    
    @Override
    public abstract String decrypt(String pStrToDecrypt);
    
    @Override
    public abstract void makeKeys(String pKey);
    
    @Override
    public byte[] getPrivateKey(String pKey) {
        return privateKey;
    }
    
    @Override
    public byte[] getPublicKey(String pKey) {
        return publicKey;
    }
}
