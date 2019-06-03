/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.socketnet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import utils.Logger;

/**
 *
 * @author alexander
 */
public class Client {
    private String id;
    private Socket socket;
    
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    private boolean ok;
    
    public Client(Socket pSocket) {
        socket = pSocket;
        ok = startStreams();
    }
    
    public Client(String pId, Socket pSocket) {
        this(pSocket);
        id = pId;
    }
    
    private boolean startStreams() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException ex) {
            Logger.Log(ex);
            return false;
        }
    }
    
    public void closeConnections() {
        try {
            in.close();
            out.close();
            socket.close();
            ok = false;
        } catch (IOException ex) {
            Logger.Log(ex);
            ok = false;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String pId) {
        this.id = pId;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket pSocket) {
        this.socket = pSocket;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public boolean isOk() {
        return ok && socket.isConnected();
    }
    
}
