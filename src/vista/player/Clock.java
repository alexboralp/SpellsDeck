/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.player;

import static java.lang.Thread.sleep;
import java.util.Date;
import javax.swing.JLabel;
import utils.Constants;
import utils.Logger;
import utils.Utils;

/**
 *
 * @author alexander
 */
public class Clock implements Runnable{
    
    JLabel clock;
    Date initialTime;
    Thread thread;
    
    public Clock (JLabel pLabel, Date pInitialTime) {
        clock = pLabel;
        initialTime = pInitialTime;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        
        long secondsLeft = 720;
        
        while(secondsLeft > 0) {
            secondsLeft = Utils.getElapsedSeconds(initialTime);
            clock.setText("Quedan " + Long.toString(secondsLeft) + " segundos.");
            try {
                sleep(Constants.THREAD_SLEEP_TIME);
            } catch (InterruptedException ex) {
                Logger.Log(ex);
            }
        }
    }
    
    
}
