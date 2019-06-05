/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.client;

import static java.lang.Thread.sleep;
import model.encrypter.methods.EncrypterFactory;
import model.encrypter.methods.IEncrypter;
import model.game.objects.Card;
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
public class ClientAdministrator implements IObserver, Runnable{
    
    SpellsDeck ventanaClient;
    ClientSocketThread client;
    boolean crackedEnemyCards[];
    boolean crackedMyCards[];
    int[] myCards;
    Deck deck;
    String[] encrypterMethods;
    Thread gameThread;
    private boolean jugar;
    
    public ClientAdministrator() {
        super();
        crackedEnemyCards = new boolean[Constants.MAX_SELECTED_CARDS_NUMBER];
        crackedMyCards = new boolean[Constants.MAX_SELECTED_CARDS_NUMBER];
        deck = new Deck();
        ventanaClient = null;
        myCards = new int[Constants.MAX_SELECTED_CARDS_NUMBER];
        restartCrackedCards();
        encrypterMethods = new String[EncrypterFactory.METHOD.values().length];
        // Obtiene todos los métodos como un arreglo de Strings
        int posMethod = 0;
        for (EncrypterFactory.METHOD method : EncrypterFactory.METHOD.values()) {
            encrypterMethods[posMethod] = method.toString();
            posMethod++;
        }
    }
    
    private void restartCrackedCards() {
        Logger.Log("ClientAdministrator: " + "Reiniciando las cartas desencriptadas.");
        for (int pos = 0; pos < Constants.MAX_SELECTED_CARDS_NUMBER; pos++) {
            crackedEnemyCards[pos] = false;
        }
        for (int pos = 0; pos < Constants.MAX_SELECTED_CARDS_NUMBER; pos++) {
            crackedMyCards[pos] = false;
        }
    }
    
    private void restartGame() {
        // Reiniciando las cartas crackeadas
        restartCrackedCards();
        
        jugar = false;
        
        // Quitando las cartas del contrario
        //ventanaClient.eraseEnemyCards();
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
                    Logger.Log("ClientAdministrator: " + client.getClient().getId() + ": Solicitando cerrar la conección");
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

    public boolean[] getCrackedEnemyCards() {
        return crackedEnemyCards;
    }

    public void setCrackedEnemyCards(boolean[] crackedEnemyCards) {
        this.crackedEnemyCards = crackedEnemyCards;
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
            
            Message message = (Message) pMessage;
            Logger.Log("ClientAdministrator: " + "Recibí un mensaje del cliente: " + ((Message) message).getId());
        
            if (message.getTipo() == IMessage.TIPO.MESSAGE_FROM_SERVER) {
                Logger.Log("ClientAdministrator: " + "El mensaje viene del server.");
                
                // Esperando que la interfaz gráfica se muestre
                while (ventanaClient == null) {
                    try {
                    sleep(Constants.THREAD_SLEEP_TIME);
                    } catch (InterruptedException ex) {
                        Logger.Log(ex);
                    }
                }
                
                if (message.getMessage() instanceof MessageFactory.TIPO_MENSAJE) {
                    switch ((MessageFactory.TIPO_MENSAJE)message.getMessage()) {
                        case WAITING_FOR_SECOND_PLAYER:
                            Logger.Log("ClientAdministrator: " + "Esperando al segundo jugador.");
                            ventanaClient.print("Esperando al segundo jugador.");
                            jugar = false;
                            break;
                        case WAITING_FOR_SECOND_PLAYERS_DECK:
                            Logger.Log("ClientAdministrator: " + "Esperando que el segundo jugador defina su deck inicial.");
                            ventanaClient.print(Utils.convertToMultiline("Esperando que el segundo\njugador defina su\ndeck inicial."));
                            jugar = false;
                            break;
                        case START_GAME:
                            Logger.Log("ClientAdministrator: " + "Inicia el juego. Selecciona tres cartas para el juego y da clic en el botón 'A jugar!'");
                            ventanaClient.print(Utils.convertToMultiline("Inicia el juego.\nSelecciona tres cartas\npara el juego y\nda clic en el botón\n'A jugar!'"));
                            jugar = false;
                            break;
                        case START_PLAYING:
                            Logger.Log("ClientAdministrator: " + "Inicia el juego. Tratando de desencriptar las cartas del contrario.");
                            ventanaClient.print(Utils.convertToMultiline("Inicia el juego.\nTratando de desencriptar\nlas cartas del contrario."));
                            gameThread = new Thread(this);
                            gameThread.start();
                            jugar = true;
                            break;
                        case CARD_ENEMY_CRACKED:
                            if (jugar) {
                                Logger.Log("ClientAdministrator: " + "Carta del enemigo decodificada.");
                                ventanaClient.print("Carta decodificada.");
                                int posEnemy = 0;
                                while (crackedEnemyCards[posEnemy]) {
                                    posEnemy++;
                                }
                                crackedEnemyCards[posEnemy] = true;
                                ventanaClient.fadeEnemyCard(posEnemy);
                            }
                            break;
                        case CARD_YOURS_CRACKED:
                            if (jugar) {
                                Logger.Log("ClientAdministrator: " + "Carta tuya decodificada.");
                                ventanaClient.print("Carta decodificada.");
                                int posMy = 0;
                                while (crackedMyCards[posMy]) {
                                    posMy++;
                                }
                                crackedMyCards[posMy] = true;
                                ventanaClient.fadeMyCard(posMy);
                            }
                            break;
                        case SELECT_DECK:
                            Logger.Log("ClientAdministrator: " + "Selecciona las cartas para el juego y da clic en el botón 'A jugar!'");
                            ventanaClient.print(Utils.convertToMultiline("Selecciona las cartas\npara el juego y\nda clic en el botón\n'A jugar!'"));
                            jugar = false;
                            break;
                        /*case HALF_KEY:
                            ventanaClient.print("Se tiene la mitad de la llave.");
                            break;
                        case FINAL_MESSAGE:
                            ventanaClient.print("Mensaje del conjuro decodificado recibido.");
                            break;*/
                        case WIN_ONE_MATCH:
                            Logger.Log("ClientAdministrator: " + "Se ganó la partida.");
                            ventanaClient.print("Se ganó la partida.\nSelecciona el deck\npara la siguiente\npartida.");
                            ventanaClient.winMatch();
                            gameThread.interrupt();
                            restartGame();
                            break;
                        case LOSE_ONE_MATCH:
                            Logger.Log("ClientAdministrator: " + "Se perdió la partida.");
                            ventanaClient.print("Se perdió la partida.\nSelecciona el deck\npara la siguiente\npartida.");
                            ventanaClient.loseMatch();
                            gameThread.interrupt();
                            restartGame();
                            break;
                        case WINNER:
                            Logger.Log("ClientAdministrator: " + "Eres el ganador, felicidades!");
                            ventanaClient.print("Eres el ganador,\nfelicidades!");
                            jugar = false;
                            break;
                        case LOOSER:
                            Logger.Log("ClientAdministrator: " + "Perdiste el juego.");
                            ventanaClient.print("Perdiste el juego.");
                            jugar = false;
                            break;
                        case SECOND_PLAYER_DESCONECTED:
                            Logger.Log("ClientAdministrator: " + "Se desconectó el segundo jugador, ¡ganaste el juego!.");
                            ventanaClient.print(Utils.convertToMultiline("Se desconectó el\nsegundo jugador,\n¡ganaste el juego!."));
                            jugar = false;
                            break;
                        case WRONG_NUMBER_OF_CARDS:
                            Logger.Log("ClientAdministrator: " + "El número de cartas seleccionado es incorrecto.");
                            ventanaClient.print(Utils.convertToMultiline("El número de cartas\nseleccionado es incorrecto."));
                            jugar = false;
                            break;
                        default:
                            break;
        
                    }
                } else if (message.getMessage() instanceof SpellsBook) {
                    // TODO: Guardar el SpellsBook en un archivo.
                } else if (message.getMessage() instanceof String) {
                    String messageString = (String)message.getMessage();
                    
                    if (messageString.length() == Constants.KEY_BYTE_LENGTH / 2) {
                        Logger.Log("ClientAdministrator: " + "Se tiene la mitad de la llave.");
                        ventanaClient.print("Se tiene la mitad de la llave.");
                        ventanaClient.writeKey(messageString);
                    } else {
                        Logger.Log("ClientAdministrator: " + "Se tiene la llave completa.");
                        ventanaClient.print("Se tiene la llave completa.");
                        ventanaClient.writeKey(messageString);
                    }
                } else if (message.getMessage() instanceof Deck) {
                    Logger.Log("ClientAdministrator: " + "Se recibió el deck enemigo.");
                    deck = (Deck)message.getMessage();
                    int[] selectedCards = new int[Constants.MAX_SELECTED_CARDS_NUMBER];
                    Logger.Log("ClientAdministrator: " + "Cartas seleccionadas: ");
                    for (int pos = 0; pos < Constants.MAX_SELECTED_CARDS_NUMBER; pos++) {
                        selectedCards[pos] = Integer.parseInt(deck.getCard(pos).getId().substring(4));
                        Logger.Log("ClientAdministrator: " + selectedCards[pos]);
                    }
                    ventanaClient.setEnemySelectedCards(selectedCards);
                    Logger.Log("ClientAdministrator: " + "Esperando la señal del server para iniciar.");
                    ventanaClient.print(Utils.convertToMultiline("Se recibió el deck enemigo.\nEsperando la señal del\nserver para iniciar."));
                }
            } else if (message.getTipo() == IMessage.TIPO.MESSAGE_FROM_CLIENT) {
                Logger.Log("ClientAdministrator: " + "El mensaje viene del cliente");
                if (message.getMessage() instanceof String) {
                    if (((String)message.getMessage()).contains(MessageFactory.TIPO_MENSAJE.SELECT_DECK.toString())) {
                        myCards = ventanaClient.getSelectedCards();
                        Logger.Log("ClientAdministrator: " + "Mis cartas seleccionadas: " + myCards[0] + ", " + myCards[1] + ", " + myCards[2]);
                    }
                }
                
                if (client.isOk()) {
                    Logger.Log("ClientAdministrator: " + "La conexión con el server está bien, enviando el mensaje al server.");
                    Logger.Log("ClientAdministrator: " + "Server: " + client.getClient().getId());
                    message.setId(client.getClient().getId());
                    client.sendMessage(message);
                } else {
                    client.stopListening();
                    client.closeComunication();
                }
            }
        }
    }

    @Override
    public void run() {
        if (jugar) {
            Logger.Log("ClientAdministrator: " + "Iniciando la desencriptación de las cartas.");
            for (int numCard = 0; numCard < Constants.MAX_SELECTED_CARDS_NUMBER; numCard++) {
                Logger.Log("ClientAdministrator: " + "Carta " + numCard);
                Card currentCard = deck.getCard(numCard);
                Logger.Log("ClientAdministrator: " + "Método de encriptación de la carta " + currentCard.getTipo());
                int startMethod = myCards[numCard];
                boolean cracked = false;
                int intentos = 0;
                while (!cracked && intentos < encrypterMethods.length) {
                    Logger.Log("ClientAdministrator: " + "Intentando con el método " + encrypterMethods[startMethod]);
                    IEncrypter enc = EncrypterFactory.getIntance(encrypterMethods[startMethod]);
                    try {
                        enc.setPrivateKey(currentCard.getKey1());
                        enc.setPublicKey(currentCard.getKey2());
                        if (enc.encrypt(currentCard.getDescription()).equals(currentCard.getEncryptedDescription())) {
                            Logger.Log("ClientAdministrator: " + "Desencriptado. ");
                            client.sendMessage(MessageFactory.createMessage(client.getClient().getId(), IMessage.TIPO.MESSAGE_FROM_CLIENT, MessageFactory.TIPO_MENSAJE.CARD_ENEMY_CRACKED));
                            cracked = true;
                        }
                    }catch(Exception ex) {
                        Logger.Log(ex);
                    }
                    try{
                        if (!cracked) {
                            enc.setPrivateKey(currentCard.getKey2());
                            enc.setPublicKey(currentCard.getKey1());
                            if (enc.encrypt(currentCard.getDescription()).equals(currentCard.getEncryptedDescription())) {
                                Logger.Log("ClientAdministrator: " + "Desencriptado. ");
                                client.sendMessage(MessageFactory.createMessage(client.getClient().getId(), IMessage.TIPO.MESSAGE_FROM_CLIENT, MessageFactory.TIPO_MENSAJE.CARD_ENEMY_CRACKED));
                                cracked = true;
                            }
                        }
                    }catch(Exception ex) {
                        Logger.Log(ex);
                    }
                    intentos++;
                    startMethod = (startMethod + 1) % encrypterMethods.length;
                    if (!jugar){
                        break;
                    }
                }
                if (intentos == encrypterMethods.length) {
                    Logger.Error("ClientAdministrator: " + "No se pudo desencriptar la carta :" + currentCard.getId() + " que tenía el método: " + currentCard.getTipo());
                }
                if (!jugar){
                    break;
                }
            }
        }
    }
    
}
