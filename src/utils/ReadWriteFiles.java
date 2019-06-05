/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author alexander
 */
public class ReadWriteFiles {
    
    
    public static String readCompleteTxtFile(String pFilePath){
        return new String ( readCompleteBinaryFile(pFilePath) );
    }
    
    public static byte[] readCompleteBinaryFile(String pFilePath) {
        try {
            return Files.readAllBytes(Paths.get(pFilePath));
        } catch (IOException ex) {
            Logger.Log(ex);
        }
        return null;
    }
    
    public static String readLineTxtFile(String pFilePath, int pLineNumber) {
        try {
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(pFilePath));
            int i = 0;
            while (i < pLineNumber && (line = br.readLine()) != null) {
                i++;
                Logger.Log("ReadWriteFile: Línea leída de las keys.txt: " + line);
            }
            Logger.Log("ReadWriteFile: Voy a devolver: " + line);
            return line;
        } catch (IOException ex) {
            Logger.Log(ex);
        }
        return null;
    }
}
