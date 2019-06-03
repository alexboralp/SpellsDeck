/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.client;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import model.game.objects.Deck;
import model.game.objects.SpellsBook;
import model.socketnet.client.ClientSocketThread;
import model.socketspellsdeck.message.IMessage;
import model.socketspellsdeck.message.Message;
import model.socketspellsdeck.message.MessageFactory;
import utils.Constants;
import utils.Logger;
import utils.Utils;
import utils.observerpattern.IObserver;
import vista.player.SpellsDeck;

/**
 *
 * @author alexander
 */
public class Administrator implements IObserver{
    
    SpellsDeck ventanaClient;
    ClientSocketThread client;
    boolean crackedCards[];
    Deck deck;
    
    public Administrator() {
        super();
        crackedCards = new boolean[Constants.MAX_SELECTED_CARDS_NUMBER];
        deck = new Deck();
        ventanaClient = null;
        restartCrackedCards();
    }
    
    public void restartCrackedCards() {
        for (int pos = 0; pos < crackedCards.length; pos++) {
            crackedCards[pos] = false;
        }
    }
    
    private void restartGame() {
        // Reiniciando las cartas crackeadas
        restartCrackedCards();
        
        // Quitando las cartas del contrario
        ventanaClient.eraseEnemyCards();
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

    public boolean[] getCrackedCards() {
        return crackedCards;
    }

    public void setCrackedCards(boolean[] crackedCards) {
        this.crackedCards = crackedCards;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
    
    
    
    @Override
    public void update(Object pMessage) {
        if (pMessage instanceof Message) {
            System.out.println("Recibí un mensaje.");
            Message message = (Message) pMessage;
        
            if (message.getTipo() == IMessage.TIPO.MESSAGE_FROM_SERVER) {
                System.out.println("El mensaje viene del server.");
                
                // Esperando que la interfaz gráfica se muestre
                while (ventanaClient == null) {
                    try {
                    sleep(Constants.THREAD_SLEEP_TIME);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Administrator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                if (message.getMessage() instanceof MessageFactory.TIPO_MENSAJE) {
                    switch ((MessageFactory.TIPO_MENSAJE)message.getMessage()) {
                        case WAITING_FOR_SECOND_PLAYER:
                            ventanaClient.print("Esperando al segundo jugador.");
                            break;
                        case WAITING_FOR_SECOND_PLAYERS_DECK:
                            ventanaClient.print("Esperando que el segundo jugador defina su deck inicial.");
                            break;
                        case START_GAME:
                            ventanaClient.print(Utils.convertToMultiline("Inicia el juego.\nSelecciona tres cartas para el juego\ny da clic en el botón 'A jugar!'"));
                            break;
                        case CARD_CRACKED:
                            ventanaClient.print("Carta decodificada.");
                            int pos = 0;
                            while (!crackedCards[pos]) {
                                pos++;
                            }
                            ventanaClient.fadeCard(pos);
                            break;
                        case SELECT_DECK:
                            ventanaClient.print(Utils.convertToMultiline("Selecciona las cartas para el juego\ny da clic en el botón 'A jugar!'"));
                            break;
                        /*case HALF_KEY:
                            ventanaClient.print("Se tiene la mitad de la llave.");
                            break;
                        case FINAL_MESSAGE:
                            ventanaClient.print("Mensaje del conjuro decodificado recibido.");
                            break;*/
                        case WIN_ONE_MATCH:
                            ventanaClient.print("Se ganó la partida.");
                            break;
                        case LOOSER_ONE_MATCH:
                            ventanaClient.print("Se perdió la partida.");
                            break;
                        case WINNER:
                            ventanaClient.print("Eres el ganador, felicidades!");
                            break;
                        case LOOSER:
                            ventanaClient.print("Perdiste el juego.");
                            break;
                        case SECOND_PLAYER_DESCONECTED:
                            ventanaClient.print("Se desconectó el segundo jugardo, ganaste el juego.");
                        default:
                            break;
        
                    }
                } else if (message.getMessage() instanceof SpellsBook) {
                    // TODO: Guardar el SpellsBook en un archivo.
                } else if (message.getMessage() instanceof String) {
                    String messageString = (String)message.getMessage();
                    
                    if (messageString.length() == Constants.KEY_BYTE_LENGTH / 2) {
                        ventanaClient.print("Se tiene la mitad de la llave.");
                        ventanaClient.writeKey(messageString);
                    } else {
                        ventanaClient.print("Se tiene la llave completa.");
                        ventanaClient.writeKey(messageString);
                    }
                }
            } else if (message.getTipo() == IMessage.TIPO.MESSAGE_FROM_CLIENT) {
                System.out.println("El mensaje viene del cliente");
                if (message.getMessage() instanceof MessageFactory.TIPO_MENSAJE) {
                    switch ((MessageFactory.TIPO_MENSAJE)message.getMessage()) {
                        case SELECT_DECK:
                            int selectedCards[] = ventanaClient.getSelectedCards();
                            
                            break;
                        default:
                            break;
                        
                    }
                }
                
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
