/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.server;

import static java.lang.Thread.sleep;
import java.util.LinkedList;
import model.game.Game;
import model.socketnet.server.ServerSocketThread;
import model.socketspellsdeck.message.IMessage;
import model.socketspellsdeck.message.Message;
import model.socketspellsdeck.message.MessageFactory;
import utils.Constants;
import utils.Logger;
import utils.observerpattern.IObserver;
import vista.server.Server;

/**
 *
 * @author alexander
 */
public class ServerAdministrator implements IObserver, Runnable {
    public final int MAX_NUM_CLIENTS = 2;
    
    private Server ventanaServer;
    private final LinkedList<ServerSocketThread> clients;
    private final LinkedList<Game> games;
    
    Thread cleaner;
    
    public ServerAdministrator() {
        super();
        clients = new LinkedList<>();
        games = new LinkedList<>();
        cleaner = new Thread(this);
        cleaner.start();
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
            Logger.Log("ServerAdministrator: " + "Recibí un mensaje.");
            Message message = (Message) pMessage;
        
            if (null != message.getTipo()) {
                switch (message.getTipo()) {
                    case MESSAGE_FROM_CLIENT:
                        Logger.Log("ServerAdministrator: " + "El mensaje viene del cliente.");
                        ventanaServer.print("CLIENT: " + message.getId() + ", Mensaje: " + message.toString());
                        break;
                    case MESSAGE_FROM_SERVER:
                        Logger.Log("ServerAdministrator: " + "El mensaje viene del server.");
                        for (ServerSocketThread client : clients) {
                            System.out.println("cliente: " + client.toString());
                            if (client.getClient().isOk()) {
                                Logger.Log("ServerAdministrator: " + "El cliente está bien, voy mandando el mensaje.");
                                ventanaServer.print("Cliente " + message.getId() + ": Enviando un mensaje al cliente. Mensaje: " + message.toString());
                                client.sendMessage(pMessage);
                            }
                        }
                        break;
                    case CLOSE_CONNECTION:
                        Logger.Log("ServerAdministrator: " + "El mensaje viene del cliente solicitando cerrar la conección.");
                        Logger.Log("ServerAdministrator: " + "Cliente: " + (String)message.getMessage());
                        ventanaServer.print("Cliente " + message.getId() + ": Solicitud de cerrar la conección. Mensaje: " + message.toString());
                        for (ServerSocketThread client : clients) {
                            if (client.getClient().getId().equals((String)message.getMessage())) {
                                Logger.Log("ServerAdministrator: " + "Cerrando la conexión con el cliente.");
                                client.getClient().closeConnections();
                                ventanaServer.print("Cliente " + message.getId() + ": Conección cerrada.");
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
            Logger.Log("ServerAdministrator: " + "Recibí un cliente nuevo.");
            ServerSocketThread newClient = (ServerSocketThread)pMessage;
            addClient(newClient);
            newClient.addObserver(this);
            ventanaServer.print("Nuevo cliente, nombre: " + newClient.getClient().getName() + ", id: " + newClient.getClient().getId());
            if (clients.size() % Constants.NUMBER_OF_PLAYERS == 0) {
                Game game = new Game(clients.get(clients.size() - 2), clients.getLast());
                games.add(game);
                Logger.Log("ServerAdministrator: " + "Iniciando el juego entre " + game.getPlayer(0).getClient().getId() + " y " + game.getPlayer(1).getClient().getId());
                ventanaServer.print("Iniciando el juego entre " + game.getPlayer(0).getClient().getId() + " y " + game.getPlayer(1).getClient().getId());
                newClient.sendMessage(MessageFactory.createMessage(newClient.getClient().getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.START_GAME));
                clients.get(clients.size() - 2).sendMessage(MessageFactory.createMessage(newClient.getClient().getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.START_GAME));
            } else {
                newClient.sendMessage(MessageFactory.createMessage(newClient.getClient().getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.WAITING_FOR_SECOND_PLAYER));
            }
        }
        
    }

    @Override
    public void run() {
        while(true) {
            
            cleanGames();
            
            try {
                sleep(Constants.THREAD_BIG_SLEEP_TIME);
            } catch (InterruptedException ex) {
                Logger.Log(ex);
            }
        }
    }
    
    private void cleanGames() {
        for (Game game : games) {
            // Si la conección de alguno de los dos jugadores no está bien
            if (!(game.getPlayer(0).getClient().isOk() && game.getPlayer(1).getClient().isOk())) {
                ventanaServer.print("Cerrando el juego de " + game.getPlayer(0).getClient().getId() + " y " + game.getPlayer(1).getClient().getId());
                if (game.getPlayer(0).getClient().isOk()){
                    game.getPlayer(0).sendMessage(MessageFactory.createMessage("", IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.SECOND_PLAYER_DESCONECTED));
                } else if (game.getPlayer(1).getClient().isOk()){
                    game.getPlayer(1).sendMessage(MessageFactory.createMessage("", IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.SECOND_PLAYER_DESCONECTED));
                }
                games.remove(game);
            }
        }
    }
    
}
