/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.encrypter.methods;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import utils.Logger;

/**
 *
 * @author aborbon
 */
public final class TRIPLEDES extends AbsEncrypter {
    
    private SecretKeySpec secretPrivateKey;
    Cipher cipher;
    
    public TRIPLEDES() {
        super();
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.Log(ex);
            cipher = null;
        }
    }
    
    public TRIPLEDES(String pKey) {
        this();
        this.makeKeys(pKey);
    }
 
    @Override
    public void setPrivateKey(String pKey)
    {
        super.setPrivateKey(pKey);
        secretPrivateKey = new SecretKeySpec(Base64.getDecoder().decode(pKey), "DESede");
    }

    @Override
    public String encrypt(String pStrToEncrypt) {
        try
        {
            if (cipher != null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretPrivateKey);
                return Base64.getEncoder().encodeToString(cipher.doFinal(pStrToEncrypt.getBytes("UTF-8")));
            }
        }
        catch (UnsupportedEncodingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e)
        {
            Logger.Log(e);
        }
        return null;
    }

    @Override
    public String decrypt(String pStrToDecrypt) {
        try
        {
            if (cipher != null) {
                cipher.init(Cipher.DECRYPT_MODE, secretPrivateKey);
                return new String(cipher.doFinal(Base64.getDecoder().decode(pStrToDecrypt)));
            }
        }
        catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e)
        {
            Logger.Log(e);
        }
        return null;
    }

    @Override
    public void makeKeys(String pKey) {
        try {
            byte[] key = pKey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 24);
            super.setPrivateKey(Base64.getEncoder().encodeToString(key));
            secretPrivateKey = new SecretKeySpec(key, "DESede");
            this.setPublicKey(null);
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Logger.Log(e);
        }
    }
    
}