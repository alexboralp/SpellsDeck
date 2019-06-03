/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.game.objects;

/**
 *
 * @author alexander
 */
public class Card {
    public enum TIPO {
        SHA256, MD5, TRIPLEDES, AES, PLAIN, RSA, PGP
    }
    
    private String id;
    private String tipo;
    private String imagePath;
    private String description;
    private String encryptedDescription;
    private String key1;
    private String key2;

    public Card() {
        this.id = "";
        this.tipo = "";
        this.imagePath = "";
        this.description = "";
        this.encryptedDescription = "";
        this.key1 = "";
        this.key2 = "";
    }

    public Card(String id, String tipo, String imagePath, String description, String encryptedDescription, String key1, String key2) {
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

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEncryptedDescription() {
        return encryptedDescription;
    }

    public void setEncryptedDescription(String encryptedDescription) {
        this.encryptedDescription = encryptedDescription;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    @Override
    public String toString() {
        return "Card{" + "id=" + id + ", tipo=" + tipo + ", imagePath=" + imagePath + ", description=" + description + ", encryptedDescription=" + encryptedDescription + ", key1=" + key1 + ", key2=" + key2 + '}';
    }
    
    public String toMessageNetString() {
        return "id=" + id + ",imagePath=" + imagePath + ",description=" + description + ",encryptedDescription=" + encryptedDescription + ",key1=" + key1 + ",key2=" + key2;
    }
    
    
}
