/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.server;

import java.util.LinkedList;
import model.socketnet.server.ServerSocketThread;
import model.socketspellsdeck.message.Message;
import utils.Print;
import utils.observerpattern.IObserver;
import vista.server.Server;

/**
 *
 * @author alexander
 */
public class Administrator implements IObserver {
    
    private Server ventanaServer;
    private final LinkedList<ServerSocketThread> clients;
    
    public Administrator() {
        super();
        clients = new LinkedList<>();
    }

    public Server getVentanaServer() {
        return ventanaServer;
    }

    public void setVentanaServer(Server ventanaServer) {
        this.ventanaServer = ventanaServer;
    }
    
    public void addClient(ServerSocketThread pNewClient) {
        clients.add(pNewClient);
        
        for (ServerSocketThread client : clients) {
            if (!client.getClient().isOk()) {
                clients.remove(client);
            }
        }
    }
    
    @Override
    public void update(Object pMessage) {
        if (pMessage instanceof Message) {
            System.out.println("Recibí un mensaje.");
            Message message = (Message) pMessage;
        
            if (null != message.getTipo()) {
                switch (message.getTipo()) {
                    case MESSAGE_FROM_CLIENT:
                        System.out.println("El mensaje viene del cliente.");
                        Print.print("CLIENT: " + (String)message.getMessage());
                        break;
                    case MESSAGE_FROM_SERVER:
                        System.out.println("El mensaje viene del server.");
                        for (ServerSocketThread client : clients) {
                            System.out.println("cliente: " + client.toString());
                            if (client.getClient().isOk()) {
                                System.out.println("El cliente está bien, voy mandando el mensaje.");
                                client.sendMessage(pMessage);
                            }
                        }
                        break;
                    case CLOSE_CONNECTION:
                        System.out.println("El mensaje viene del cliente solicitando cerrar la conección.");
                        System.out.println("Cliente: " + (String)message.getMessage());
                        for (ServerSocketThread client : clients) {
                            if (client.getClient().getId().equals((String)message.getMessage())) {
                                System.out.println("Cerrando la conexión con el cliente.");
                                client.getClient().closeConnections();
                            }
                            if (!client.getClient().isOk()) {
                                client.stopListening();
                                client.getClient().closeConnections();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } else if (pMessage instanceof ServerSocketThread) {
            System.out.println("Recibí un cliente nuevo.");
            ServerSocketThread newClient = (ServerSocketThread)pMessage;
            addClient(newClient);
            newClient.addObserver(this);
        }
        
    }
    
}
