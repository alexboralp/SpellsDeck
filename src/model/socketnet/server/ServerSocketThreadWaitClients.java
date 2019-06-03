/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.socketnet.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import model.socketnet.Client;
import utils.Logger;
import utils.observerpattern.Observable;

/**
 *
 * @author aborbon
 */
public class ServerSocketThreadWaitClients extends Observable implements Runnable{
    
    private int port;
    private ServerSocket serverSocket;
    private boolean ok;
    Thread serverThread;

    public ServerSocketThreadWaitClients(int port) {
        ok = startSocket(port);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServersocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        closeConnection();
        this.port = port;
        ok = startSocket(port);
    }

    public boolean isOk() {
        return ok;
    }
    
    private boolean startSocket(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            return false;
        }
        return true;
    }
    
    public void startListening() {
        serverThread = new Thread(this);
        serverThread.start();
    }
    
    public void closeConnection() {
        try {
            serverThread.interrupt();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.Log(ex);
        }
    }

    @Override
    public void run() {
        if (ok) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println(socket.getRemoteSocketAddress().toString());
                    ServerSocketThread newC = new ServerSocketThread(new Client(socket.getRemoteSocketAddress().toString(), socket));
                    Thread newClient = new Thread(newC);
                    newClient.start();
                    this.updateAll(newC);
                } catch (IOException ex) {
                    Logger.Log(ex);
                }
            }
        }
    }
}
