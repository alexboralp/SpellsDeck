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
public class Utils {
    
    private static Date date;
    
    public static long getElapsedSeconds (Date initialDate) {
        date = new Date();
        long elapsedTime = 720 - (date.getTime() - initialDate.getTime()) / 1000;
        return elapsedTime;
    }
    
    public static String convertToMultiline(String orig){
        return "<html>" + orig.replaceAll("\n", "<br>") + "</html>";
    }
}
