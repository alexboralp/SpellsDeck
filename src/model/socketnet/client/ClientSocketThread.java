/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.socketnet.client;

import java.io.EOFException;
import java.io.IOException;
import model.socketnet.Client;
import utils.Logger;
import utils.observerpattern.Observable;

/**
 *
 * @author alexander
 */
public class ClientSocketThread extends Observable implements Runnable {
    
    private Client client;
    private boolean listening;
    private Thread socketThread;

    public ClientSocketThread (Client pClient) {
        listening = true;
        client = pClient;
    }
    
    @Override
    public void run() {
        Object message;
        Logger.Log("ClientSocketThread: " + "Esperando mensajes del server");
        while (listening) {
            try {
                if ((message = client.getIn().readObject()) != null) {
                    this.updateAll(message);
                }
            } catch (EOFException ex) {
                listening = false;
            } catch (IOException | ClassNotFoundException ex) {
                Logger.Log(ex);
            }
            
            if (!client.isOk()) {
                listening = false;
            }
        }
    }
    
    public void sendMessage(Object pMessage) {
        try {
            Logger.Log("ClientSocketThread: " + "Enviando mensaje al server");
            client.getOut().writeObject(pMessage);
            client.getOut().flush();
        } catch (IOException ex) {
            Logger.Log(ex);
        }
    }
    
    public void startListening() {
        if (client.isOk()) {
            listening = true;
            socketThread = new Thread(this);
            socketThread.start();
        } else {
            Logger.Log("ClientSocketThread: " + "Hay algún error con el cliente.");
        }
    }
    
    public void stopListening() {
        listening = false;
        socketThread.interrupt();
    }
    
    public void closeComunication() {
        stopListening();
        client.closeConnections();
    }
    
    public boolean isOk() {
        return client.isOk() && listening;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
    
}
