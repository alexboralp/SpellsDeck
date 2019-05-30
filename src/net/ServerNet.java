/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import utils.*;

/**
 *
 * @author aborbon
 */
public class ServerNet implements Constants, Runnable{
    private static ServerNet server;
    private List<ClientNet> clients;
    private ServerSocket serverSocket ;
    private boolean isListening;
    IObserver observer;
    
    private ServerNet(IObserver pObserver) throws Exception{
        clients = new ArrayList<>();
        serverSocket = new ServerSocket(PORT);
        isListening = true;
        this.observer = pObserver;
    }
    
    public synchronized static void startListening(IObserver observer) {
        try {
            if (server == null) {
                server = new ServerNet(observer);
            }
            
            ServerSocket serverSocket = new ServerSocket(PORT);
            Thread listenThread = new Thread(server);
            listenThread.start();
        } catch (IOException ex) {
            Logger.Log(ex.getMessage());
        } catch (Exception ex) {
            Logger.Log(ex.getMessage());
        }
    }
    
    @Override
    public void run () {
        while (isListening) {
            try {
                Socket newSocket = serverSocket.accept();
                ClientNet cliente = new ClientNet(newSocket);
                clients.add(cliente);
                
                Thread.sleep(THREAD_SLEEP_TIME);
                
            } catch (IOException | InterruptedException ex) {
                Logger.Log(ex.getMessage());
            }
        }
    }
    
    public void stopListening () {
        isListening = false;
    }
}
