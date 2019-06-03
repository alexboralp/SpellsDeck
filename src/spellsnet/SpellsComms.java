/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellsnet;

import utils.observerpattern.IObserver;
import net.ClientNet;
import net.ServerNet;
import spellsnet.MessageNet;
import utils.*;

/**
 *
 * @author aborbon
 */
public class SpellsComms implements IObserver, Constants{
    private ClientNet client;
    
    public SpellsComms() {
        
    }
    
    public void conectarAJuego(String pHost) {
        client = new ClientNet(pHost, PORT);
        client.addObserver(this);
    }
    
    public void iniciarJuegoNuevo() {
        ServerNet.startListening(this);
    }
    
    @Override
    public void update(Object pData) {
        MessageNet msg = (MessageNet)pData;
        
        switch(msg.getType()) {
            
        }
    }
}
