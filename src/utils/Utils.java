/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author aborbon
 */
public class Utils {
    public static String convertToMultiline(String orig){
        return "<html>" + orig.replaceAll("\n", "<br>") + "</html>";
    }
    
    public static String readCompleteTxtFile(String pFilePath){
        String content = "";
        try {
            content = new String ( Files.readAllBytes( Paths.get(pFilePath) ) );
        } catch (IOException e) {
            Logger.Log(e);
        }
    return content;
    }
}
