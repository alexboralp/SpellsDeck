/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.socketnet.server;

import model.socketnet.Client;
import java.io.IOException;
import utils.Logger;
import utils.observerpattern.Observable;

/**
 *
 * @author alexander
 */
public class ServerSocketThread extends Observable implements Runnable {
    Client client;
    boolean listening;
    Thread socketThread;
    
    public ServerSocketThread(Client pClient) {
        listening = true;
        client = pClient;
    }
    
    public void startListening() {
        if (client.isOk()) {
            socketThread = new Thread(this);
            listening = true;
            socketThread.start();
        }
        
        if (listening = true && socketThread.isInterrupted()) {
            socketThread.start();
        }
    }
    
    public void stopListening() {
        listening = false;
        socketThread.interrupt();
    }

    @Override
    public void run() {
        Object message;
        Logger.Log("ServerSocketThread: " + "Nuevo cliente recibido...");
        Logger.Log("ServerSocketThread: " + "Esperando mensajes del cliente...");
        while (listening) {
            try {
                if ((message = client.getIn().readObject()) != null) {
                    this.updateAll(message);
                }
            } catch (IOException ex) {
                listening = false;
            } catch (ClassNotFoundException ex) {
                Logger.Log(ex);
            }
            
            if (!client.isOk()) {
                listening = false;
            }
        }
        
        Logger.Log("ServerSocketThread: " + "Cerrando la conección con el cliente...");
        client.closeConnections();
        Logger.Log("ServerSocketThread: " + "Conección cerrada.");
    }
    
    public void sendMessage(Object pMessage) {
        Logger.Log("ServerSocketThread: " + "Enviando un mensaje al cliente");
        try {
            client.getOut().writeObject(pMessage);
        } catch (IOException ex) {
            Logger.Log(ex);
        }
    }

    public Client getClient() {
        return client;
    }
    
}
