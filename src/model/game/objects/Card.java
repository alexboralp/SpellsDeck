/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.game.objects;

import java.io.Serializable;
import model.encrypter.methods.AbsEncrypter;
import model.encrypter.methods.EncrypterFactory;
import model.encrypter.methods.IEncrypter;
import utils.Logger;
import utils.OS;
import utils.ReadWriteFiles;

/**
 *
 * @author alexander
 */
public class Card implements Serializable {
    
    private String id;
    // Tipos posibles hasta ahora: SHA256, MD5, TRIPLEDES, AES, PLAIN, RSA, PGP
    private String tipo;
    private String imagePath;
    private String description;
    private String encryptedDescription;
    private byte[] key1;
    private byte[] key2;

    public Card() {
        this.id = "";
        this.tipo = "";
        this.imagePath = "";
        this.description = "";
        this.encryptedDescription = "";
        this.key1 = null;
        this.key2 = null;
    }

    public Card(String id, String tipo, String imagePath, String description, String encryptedDescription, byte[] key1, byte[] key2) {
        this.id = id;
        this.tipo = tipo;
        this.imagePath = imagePath;
        this.description = description;
        this.encryptedDescription = encryptedDescription;
        this.key1 = key1;
        this.key2 = key2;
    }
    
    

    public String getId() {
        return id;
    }

    public void setId(String pId) {
        this.id = pId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String pTipo) {
        this.tipo = pTipo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String pImagePath) {
        this.imagePath = pImagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        this.description = pDescription;
    }

    public String getEncryptedDescription() {
        return encryptedDescription;
    }

    public void setEncryptedDescription(String pEncryptedDescription) {
        this.encryptedDescription = pEncryptedDescription;
    }

    public byte[] getKey1() {
        return key1;
    }

    public void setKey1(byte[] pKey1) {
        this.key1 = pKey1;
    }

    public byte[] getKey2() {
        return key2;
    }

    public void setKey2(byte[] pKey2) {
        this.key2 = pKey2;
    }
    
    public boolean createCard(String pFileName, String pTipo) {
        try {
            this.id = pFileName;
            this.imagePath = ClassLoader.getSystemResource("vista/images/" + pFileName + ".jpg").getFile();
            if (OS.isWindows()) {
                this.imagePath = this.imagePath.substring(1);
            }
            String pFileTxtFile = ClassLoader.getSystemResource("vista/images/" + pFileName + ".txt").getFile();
            if (OS.isWindows()) {
                pFileTxtFile = pFileTxtFile.substring(1);
            }
            this.description = ReadWriteFiles.readCompleteTxtFile(pFileTxtFile);
            this.tipo = pTipo;
            IEncrypter ec = EncrypterFactory.getIntance(pTipo);
            ec.makeKeys(AbsEncrypter.generateStupidKey(16));
            this.encryptedDescription = ec.encrypt(this.description);
            if ((int)Math.floor(Math.random()) == 0) {
                this.key1 = ec.getPrivateKey();
                this.key2 = ec.getPublicKey();
            } else {
                this.key2 = ec.getPrivateKey();
                this.key1 = ec.getPublicKey();
            }
        } catch (Exception ex) {
            Logger.Log(ex);
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Card{" + "id=" + id + ", tipo=" + tipo + ", imagePath=" + imagePath + ", description=" + description + ", encryptedDescription=" + encryptedDescription + ", key1=" + key1 + ", key2=" + key2 + '}';
    }
    
    public String toMessageNetString() {
        return "id=" + id + ",imagePath=" + imagePath + ",description=" + description + ",encryptedDescription=" + encryptedDescription + ",key1=" + key1 + ",key2=" + key2;
    }
    
    
}
