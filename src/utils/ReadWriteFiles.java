/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
            }
            Logger.Log("ReadWriteFile: Voy a devolver: " + line);
            return line;
        } catch (IOException ex) {
            Logger.Log(ex);
        }
        return null;
    }
    
    public static void writeCompleteTxtFile(String pPathFile, String contents) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(pPathFile));
            writer.write(contents);
        }catch (IOException e) {
            Logger.Log(e);
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (IOException e) {
                Logger.Log(e);
            }
        }
    }
}
