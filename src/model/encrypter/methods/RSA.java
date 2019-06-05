/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.encrypter.methods;

import static java.nio.charset.StandardCharsets.UTF_8;
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
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 * @author aborbon
 */
public final class RSA extends AbsEncrypter {
    
    private final int keySize = 2048;
    
    private KeyPairGenerator keyPairGenerator;
    
    private KeyPair keys;
    private PrivateKey privKey;
    private PublicKey pubKey;
    private Cipher cipher;
    
    private KeyFactory kf;
    
    private boolean ok;
    
    public RSA() {
        super();
        try {
            kf = KeyFactory.getInstance("RSA");
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize, new SecureRandom());
            cipher = Cipher.getInstance("RSA");
            ok = true;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.Log(ex);
            kf = null;
            keyPairGenerator = null;
            cipher = null;
            ok = false;
        }
    }
    
    public RSA(String pKey) {
        this();
        this.makeKeys(pKey);
    }

    @Override
    public String encrypt(String pStrToEncrypt) {
        try {
            if (ok && cipher != null) {
                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                
                return Base64.getEncoder().encodeToString(cipher.doFinal(pStrToEncrypt.getBytes(UTF_8)));
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.Log(ex);
        }
        return "";
    }

    @Override
    public String decrypt(String pStrToDecrypt) {
        try {
            if (ok && cipher != null) {
                cipher.init(Cipher.DECRYPT_MODE, privKey);
                
                return new String(cipher.doFinal(Base64.getDecoder().decode(pStrToDecrypt)));
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.Log(ex);
        }
        return "";
    }

    @Override
    public void makeKeys(String pKey) {
        if (keyPairGenerator != null) {
            keys = keyPairGenerator.genKeyPair();
            pubKey = keys.getPublic();
            privKey = keys.getPrivate();
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKey.getEncoded());
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privKey.getEncoded());
            super.setPublicKey(x509EncodedKeySpec.getEncoded());
            super.setPrivateKey(pkcs8EncodedKeySpec.getEncoded());
            ok = true;
        }
    }
    
    @Override
    public void setPrivateKey(byte[] pKey) {
        try {
            privKey = kf.generatePrivate(new PKCS8EncodedKeySpec(pKey));
            super.setPrivateKey(privKey.getEncoded());
            ok = true;
        } catch (InvalidKeySpecException ex) {
            Logger.Log(ex);
            ok = false;
        }
    }
    
    @Override
    public void setPublicKey(byte[] pKey) {
        try {
            pubKey = kf.generatePublic(new X509EncodedKeySpec(pKey));
            super.setPublicKey(pubKey.getEncoded());
            ok = true;
        } catch (InvalidKeySpecException ex) {
            Logger.Log(ex);
            ok = false;
        }
    }
    
}
