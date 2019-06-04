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
public abstract class AbsEncrypter implements IEncrypter{
    private byte[] privateKey;
    private byte[] publicKey;
    
    private static final char[] alfabeto = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                         'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                         '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    private static final char[] alfabetoHex = {'A', 'B', 'C', 'D', 'E', 'F',
                         '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    
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
    public byte[] getPrivateKey() {
        if (privateKey != null) {
            return privateKey;
        }
        
        return generateStupidKey().getBytes();
    }
    
    @Override
    public byte[] getPublicKey() {
        if (privateKey != null) {
            return publicKey;
        }
        
        return generateStupidKey().getBytes();
    }
    
    public static String generateStupidKey() {
        
        int longitud = (int)(16 + Math.floor(Math.random() * 4) * 16);
        String resp = "";
        
        for (int cant = 0; cant < longitud; cant++) {
            int pos = (int)(Math.floor(Math.random() * alfabeto.length));
            resp += alfabeto[pos];
        }
        
        return resp;
    }
    
    public static String generateStupidKey(int longitud) {
        
        String resp = "";
        
        for (int cant = 0; cant < longitud; cant++) {
            int pos = (int)(Math.floor(Math.random() * alfabeto.length));
            resp += alfabeto[pos];
        }
        
        return resp;
    }
    
    protected static String generateStupidKeyHex() {
        
        int longitud = (int)(16 + Math.floor(Math.random() * 4) * 16);
        String resp = "";
        
        for (int cant = 0; cant < longitud; cant++) {
            int pos = (int)(Math.floor(Math.random() * alfabetoHex.length));
            resp += alfabetoHex[pos];
        }
        
        return resp;
    }
    
    protected static String generateStupidKeyHex(int longitud) {
        
        String resp = "";
        
        for (int cant = 0; cant < longitud; cant++) {
            int pos = (int)(Math.floor(Math.random() * alfabetoHex.length));
            resp += alfabetoHex[pos];
        }
        
        return resp;
    }
}
