/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encrypter;

import encrypter.methods.EncrypterFactory;
import encrypter.methods.IEncrypter;

/**
 *
 * @author aborbon
 */
public class Encrypter implements IEncrypter{
    
    IEncrypter encrypter;
    
    public Encrypter() { }
    
    public Encrypter(String method) {
        encrypter = EncrypterFactory.getIntance(method);
    }

    @Override
    public void setPrivateKey(String pKey) {
        encrypter.setPrivateKey(pKey);
    }

    @Override
    public void setPublicKey(String pKey) {
        encrypter.setPublicKey(pKey);
    }

    @Override
    public String encrypt(String pStrToEncrypt) {
        return encrypter.encrypt(pStrToEncrypt);
    }

    @Override
    public String decrypt(String pStrToDecrypt) {
        return encrypter.decrypt(pStrToDecrypt);
    }

    @Override
    public void makeKeys(String pKey) {
        encrypter.makeKeys(pKey);
    }

    @Override
    public String getPrivateKey() {
        return encrypter.getPrivateKey();
    }

    @Override
    public String getPublicKey() {
        return encrypter.getPublicKey();
    }
    
    
}
