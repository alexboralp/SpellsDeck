/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.encrypter.methods;

/**
 *
 * @author alexander
 */
public class PLAIN extends AbsEncrypter {

    @Override
    public String encrypt(String pStrToEncrypt) {
        return pStrToEncrypt;
    }

    @Override
    public String decrypt(String pStrToDecrypt) {
        return pStrToDecrypt;
    }

    @Override
    public void makeKeys(String pKey) { }
    
}
