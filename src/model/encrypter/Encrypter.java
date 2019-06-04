/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.encrypter;

import model.encrypter.methods.EncrypterFactory;
import model.encrypter.methods.IEncrypter;

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
    public void setPrivateKey(byte[] pKey) {
        encrypter.setPrivateKey(pKey);
    }

    @Override
    public void setPublicKey(byte[] pKey) {
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
    public byte[] getPrivateKey() {
        return encrypter.getPrivateKey();
    }

    @Override
    public byte[] getPublicKey() {
        return encrypter.getPublicKey();
    }
    
    
}
