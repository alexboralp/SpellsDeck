/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.client;

import static java.lang.Thread.sleep;
import model.socketnet.client.ClientSocketThread;
import model.socketspellsdeck.message.IMessage;
import model.socketspellsdeck.message.Message;
import utils.Logger;
import utils.Print;
import utils.observerpattern.IObserver;
import vista.player.SpellsDeck;

/**
 *
 * @author alexander
 */
public class Administrator implements IObserver{
    
    SpellsDeck ventanaClient;
    ClientSocketThread client;
    
    public Administrator() {
        super();
    }

    public SpellsDeck getVentanaClient() {
        return ventanaClient;
    }

    public void setVentanaClient(SpellsDeck ventanaClient) {
        this.ventanaClient = ventanaClient;
        ventanaClient.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    System.out.println(client.getClient().getId() + ": Solicitando cerrar la conección");
                    ventanaClient.updateAll(new Message(IMessage.TIPO.CLOSE_CONNECTION, client.getClient().getId()));
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.Log(ex);
                }
            }
        });
    }

    public ClientSocketThread getClient() {
        return client;
    }

    public void setClient(ClientSocketThread client) {
        this.client = client;
    }
    
    @Override
    public void update(Object pMessage) {
        if (pMessage instanceof Message) {
            System.out.println("Recibí un mensaje.");
            Message message = (Message) pMessage;
        
            if (message.getTipo() == IMessage.TIPO.MESSAGE_FROM_SERVER) {
                System.out.println("El mensaje viene del server.");
                Print.print("SERVER: " + (String)message.getMessage());
            } else if (message.getTipo() == IMessage.TIPO.MESSAGE_FROM_CLIENT) {
                System.out.println("El mensaje viene del cliente.");
                if (client.isOk()) {
                    System.out.println("La conexión con el cliente está bien, enviando el mensaje al server.");
                    System.out.println(client.getClient().getId());
                    client.sendMessage(pMessage);
                } else {
                    client.stopListening();
                    client.closeComunication();
                }
            }
        }
    }
    
}
