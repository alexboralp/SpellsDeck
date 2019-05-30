/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import spellsnet.MessageNet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import utils.*;

/**
 *
 * @author aborbon
 */
public class ClientNet extends Observable implements Constants, Runnable{
    private Socket socket;
    DataInputStream inputReader;
    DataOutputStream outputWriter;
    private boolean isListening = false;
    
    public ClientNet(Socket pSocket) {
        this.socket = pSocket;
        initReaders();
    }
    
    public ClientNet(String pIp, int pPort) {
        try {
            socket = new Socket(pIp, pPort);
            initReaders();
        } catch (IOException ex) {
            Logger.Log(ex.getMessage());
        }
    }

    @Override
    public void run() {
        while (isListening) {
            try{
                String msgData = inputReader.readUTF();
                
                MessageNet msg = new MessageNet(msgData);
                
                Thread.sleep(THREAD_SLEEP_TIME);
            } catch (IOException | InterruptedException ex) {
                Logger.Log(ex.getMessage());
            }
        }
    }
    
    public void stop() {
        try{
            isListening = false;
            inputReader.close();
            outputWriter.close();
            socket.close();
        } catch (IOException ex) {
            Logger.Log(ex.getMessage());
        }
    }
    
    public void sendMessage(MessageNet pMsg) {
        try {
            outputWriter.writeChars(pMsg.getStringMsg());
            outputWriter.flush();
        }catch(IOException ex) {
            Logger.Log(ex.getMessage());
        }
    }
    
    private void initReaders() {
        if (socket != null) {
            try {
                isListening = true;
                inputReader = new DataInputStream(socket.getInputStream());
                outputWriter = new DataOutputStream(socket.getOutputStream());
                
                Thread socketThread = new Thread(this);
                socketThread.start();
            } catch (IOException ex) {
                Logger.Log(ex.getMessage());
            }
        }
    }
}
