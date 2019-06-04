/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.encrypter.methods;

/**
 *
 * @author aborbon
 */
public interface IEncrypter {
    public void setPrivateKey(byte[] pKey);
    public void setPublicKey(byte[] pKey);
    public String encrypt(String pStrToEncrypt);
    public String decrypt(String pStrToDecrypt);
    public void makeKeys(String pKey);
    public byte[] getPrivateKey();
    public byte[] getPublicKey();
}
