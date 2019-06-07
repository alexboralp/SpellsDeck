/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author aborbon
 */
public class Logger {
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    public static void Log(Exception pEx) {
        Date date = new Date();
        //pEx.printStackTrace();
        System.out.println(dateFormat.format(date) + " " + pEx.getMessage());
    }
    
    public static void Log(String pMessage) {
        Date date = new Date();
        System.out.println(dateFormat.format(date) + " " + pMessage);
    }
    
    public static void Error(String message) {
        Date date = new Date();
        System.err.println(dateFormat.format(date) + " " + message);
    }
}
