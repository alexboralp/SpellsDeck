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
            Logger.Log("ServerSocketThreadWaitForClients: " + "No puedo escuchar en el puerto: " + port + ".");
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
                    Logger.Log("ServerSocketThreadWaitClients: Nuevo cliente: " + socket.getRemoteSocketAddress().toString());
                    Client client = new Client(socket, Client.CREADOR.SERVER);
                    Logger.Log("ServerSocketThreadWaitClients: Esperando el nombre del nuevo cliente.");
                    client.setName(client.getIn().readUTF());
                    Logger.Log("ServerSocketThreadWaitClients: Nombre recibido, creando su escuchador.");
                    ServerSocketThread newC = new ServerSocketThread(client);
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
