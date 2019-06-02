/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encrypter.methods;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import utils.Logger;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 * @author aborbon
 */
public final class RSA extends AbsEncrypter {
    
    private final int keySize = 2048;
    
    KeyPairGenerator keyPairGenerator;
    
    KeyPair keys;
    PrivateKey privKey;
    PublicKey pubKey;
    Cipher cipher;
    
    KeyFactory kf;
    
    public RSA() {
        super();
        try {
            kf = KeyFactory.getInstance("RSA");
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.Log(ex.getMessage());
            kf = null;
            keyPairGenerator = null;
            cipher = null;
        }
    }
    
    public RSA(String pKey) {
        this();
        this.makeKeys(pKey);
    }

    @Override
    public String encrypt(String pStrToEncrypt) {
        try {
            if (cipher != null) {
                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            
                return Base64.getEncoder().encodeToString(cipher.doFinal(pStrToEncrypt.getBytes()));
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.Log(ex.getMessage());
        }
        return null;
    }

    @Override
    public String decrypt(String pStrToDecrypt) {
        try {
            if (cipher != null) {
                cipher.init(Cipher.DECRYPT_MODE, privKey);

                return Base64.getEncoder().encodeToString(cipher.doFinal(pStrToDecrypt.getBytes()));
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.Log(ex.getMessage());
        }
        return null;
    }

    @Override
    public void makeKeys(String pKey) {
        if (keyPairGenerator != null) {
            keys = keyPairGenerator.genKeyPair();
            pubKey = keys.getPublic();
            privKey = keys.getPrivate();
            super.setPrivateKey(Base64.getEncoder().encodeToString(privKey.getEncoded()));
            super.setPublicKey(Base64.getEncoder().encodeToString(pubKey.getEncoded()));
        }
    }
    
    @Override
    public void setPrivateKey(String pKey) {
        try {
            privKey = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pKey)));
            super.setPrivateKey(Base64.getEncoder().encodeToString(privKey.getEncoded()));
        } catch (InvalidKeySpecException ex) {
            Logger.Log(ex.getMessage());
        }
    }
    
    @Override
    public void setPublicKey(String pKey) {
        try {
            pubKey = kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(pKey)));
            super.setPublicKey(Base64.getEncoder().encodeToString(pubKey.getEncoded()));
        } catch (InvalidKeySpecException ex) {
            Logger.Log(ex.getMessage());
        }
    }
    
}
