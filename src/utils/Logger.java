/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author aborbon
 */
public class Logger {
    
    public static void Log(Exception pEx) {
        //pEx.printStackTrace();
        System.out.println(pEx.getMessage());
    }
    
    public static void Log(String pMessage) {
        System.out.println(pMessage);
    }
    
    public static void Error(String message) {
        System.err.println(message);
    }
}
