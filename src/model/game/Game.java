/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.game;

import java.util.Base64;
import java.util.StringTokenizer;
import model.encrypter.methods.EncrypterFactory;
import model.encrypter.methods.IEncrypter;
import model.game.objects.Deck;
import model.game.objects.SpellsBook;
import model.game.objects.TotalDeck;
import model.socketnet.server.ServerSocketThread;
import model.socketspellsdeck.message.IMessage;
import model.socketspellsdeck.message.Message;
import model.socketspellsdeck.message.MessageFactory;
import utils.Constants;
import utils.Logger;
import utils.OS;
import utils.ReadWriteFiles;
import utils.observerpattern.IObserver;

/**
 *
 * @author alexander
 */
public class Game implements IObserver {
    
    private final String id;
    private final ServerSocketThread players[];
    private final Deck decks[];
    private final TotalDeck totalDeck;
    private final int[] winMatches;
    private final int[] crackedCards;
    private final SpellsBook spellsBook;
    private int spellWinNumber;
    private String key;
    
    public Game(ServerSocketThread pPlayer1, ServerSocketThread pPlayer2) {
        id = pPlayer1.getClient().getId() + pPlayer2.getClient().getId();
        players = new ServerSocketThread[Constants.NUMBER_OF_PLAYERS];
        decks = new Deck[Constants.NUMBER_OF_PLAYERS];
        winMatches = new int[Constants.NUMBER_OF_PLAYERS];
        crackedCards = new int[Constants.NUMBER_OF_PLAYERS];
        players[0] = pPlayer1;
        players[1] = pPlayer2;
        players[0].addObserver(this);
        players[1].addObserver(this);
        totalDeck = new TotalDeck();
        spellsBook = new SpellsBook();
        spellsBook.readSpellsBook();
        
        restartGame();
        
        Logger.Log("Game: " + id + ", Creando el nuevo juego.");
        Logger.Log("Game: " + id + ", Creando el deck.");
        String[] fileNames = {"Card0", "Card1", "Card2", "Card3", "Card4", "Card5", "Card6"};
        String[] tipos = {EncrypterFactory.METHOD.SHA256.toString(),
                          EncrypterFactory.METHOD.MD5.toString(),
                          EncrypterFactory.METHOD.TRIPLEDES.toString(),
                          EncrypterFactory.METHOD.AES.toString(),
                          EncrypterFactory.METHOD.PLAIN.toString(),
                          EncrypterFactory.METHOD.RSA.toString(),
                          EncrypterFactory.METHOD.PGP.toString()};
        boolean ok = totalDeck.createNewDeck(fileNames, tipos);
        Logger.Log("Game: " + id + ", Deck creado sin problemas: " + ok);
    }
    
    public ServerSocketThread getPlayer(int pNumber) {
        if (pNumber > -1 && pNumber < Constants.NUMBER_OF_PLAYERS) {
            return players[pNumber];
        }
        return null;
    }

    @Override
    public void update(Object pMessage) {
        Logger.Log("Game: " + id + ", Recibido un mensaje de un jugador");
        if (pMessage instanceof Message) {
            
            Message message = (Message)pMessage;
            Logger.Log("Game: " + id + ", Cliente: " + message.getId() + ", Mensaje: " + message.getMessage().toString());
            if (message.getMessage() instanceof MessageFactory.TIPO_MENSAJE) {
                
                switch((MessageFactory.TIPO_MENSAJE)(message.getMessage())) {
                    case CARD_ENEMY_CRACKED:
                        Logger.Log("Game: " + id + ", carta descifrada por el cliente: " + message.getId());
                        int numPlayer = this.getPlayer(message.getId());
                        if (winMatches[numPlayer] < Constants.TOTAL_WIN_MATCHES) {
                            players[numPlayer].sendMessage(MessageFactory.createMessage(message.getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.CARD_ENEMY_CRACKED));
                        }
                        if (winMatches[(numPlayer + 1) % Constants.NUMBER_OF_PLAYERS] < Constants.TOTAL_WIN_MATCHES) {
                            players[(numPlayer + 1) % 2].sendMessage(MessageFactory.createMessage(message.getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.CARD_YOURS_CRACKED));
                        }
                        crackedCards[numPlayer] = crackedCards[numPlayer] + 1;
                        if (crackedCards[numPlayer] == Constants.MAX_SELECTED_CARDS_NUMBER) {
                            if (winMatches[numPlayer] < Constants.TOTAL_WIN_MATCHES) {
                                players[numPlayer].sendMessage(MessageFactory.createMessage(message.getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.WIN_ONE_MATCH));
                            }
                            if (winMatches[(numPlayer + 1) % Constants.NUMBER_OF_PLAYERS] < Constants.TOTAL_WIN_MATCHES) {
                                players[(numPlayer + 1) % Constants.NUMBER_OF_PLAYERS].sendMessage(MessageFactory.createMessage(message.getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.LOSE_ONE_MATCH));
                            }
                            winMatches[numPlayer] = winMatches[numPlayer] + 1;
                            if (winMatches[numPlayer] == 1) {
                                players[numPlayer].sendMessage(MessageFactory.createMessage(message.getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, key.substring(0, key.length() / 2)));
                                if (winMatches[(numPlayer + 1) % Constants.NUMBER_OF_PLAYERS] < Constants.TOTAL_WIN_MATCHES) {
                                    restartMatch();
                                } else {
                                    restartCrackedCards();
                                    decks[numPlayer] = null;
                                }
                            } else if (winMatches[numPlayer] == 2) {
                                players[numPlayer].sendMessage(MessageFactory.createMessage(message.getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, key));
                                if (winMatches[(numPlayer + 1) % Constants.NUMBER_OF_PLAYERS] < Constants.TOTAL_WIN_MATCHES) {
                                    restartCrackedCards();
                                    decks[(numPlayer + 1) % Constants.NUMBER_OF_PLAYERS] = null;
                                } else {
                                    restartGame();
                                }
                            }
                        }
                        break;
                    /*case FINAL_MESSAGE:
                        // TODO Lógica de cuando se recibe el mensaje final del jugador
                        break;*/
                    /*case SELECT_DECK:
                        StringTokenizer deckNumbers = new StringTokenizer(",");
                        if (deckNumbers.countTokens() == Constants.MAX_SELECTED_CARDS_NUMBER) {
                            
                        } else {
                            
                        }
                        break;*/
                    default:
                        break;
                }
                
            } else if (message.getMessage() instanceof String) {
                Logger.Log("Game: " + id + ", Mensaje: Se recibió un string");
                StringTokenizer stringTokenizer = new StringTokenizer((String)message.getMessage(), Constants.STRING_SEPARATOR);
                String instruction = stringTokenizer.nextToken();
                
                Logger.Log("Game: " + id + ", Instrucción: " + instruction);
                
                // Se mandó el DECK seleccionado
                if (MessageFactory.TIPO_MENSAJE.SELECT_DECK.toString().equals(instruction)) {
                    Logger.Log("Game: " + id + ", Recibido el deck elegido por el jugador " + message.getId());
                
                    int playerPos = getPlayer(message.getId());
                
                    if (decks[playerPos] == null) {
                    
                        Deck deck = new Deck();
                        while (stringTokenizer.hasMoreTokens()){
                            String numCardString = stringTokenizer.nextToken();
                            Logger.Log("Game: " + id + ", Siguiente Token : " + numCardString);
                            int numCard = Integer.parseInt(numCardString);
                            Logger.Log("Game: " + id + ", Carta seleccionada : " + numCard);
                            deck.addCard(totalDeck.getCard(numCard));
                        }
                        decks[playerPos] = deck;
                    }
                    
                    if (decks[(playerPos + 1) % 2] != null){
                        Logger.Log("Game: " + id + ", a iniciar el juego");
                        Logger.Log("Game: " + id + ", enviando el deck al adversario.");
                        sendMessageToPlayer(0, MessageFactory.createMessage(players[0].getClient().getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, decks[1]));
                        sendMessageToPlayer(1, MessageFactory.createMessage(players[1].getClient().getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, decks[0]));
                        Logger.Log("Game: " + id + ", enviando el aviso de iniciar el juego.");
                        sendMessageToBothPlayers(MessageFactory.createMessage("", IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.START_PLAYING));
                        //startGame();
                    } else {
                        Logger.Log("Game: " + id + ", enviando el aviso de esperar el deck del adversario.");
                        sendMessageToPlayer(playerPos, MessageFactory.createMessage(players[playerPos].getClient().getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.WAITING_FOR_SECOND_PLAYERS_DECK));
                    }
                // Se envió el mensaje final para comprobar
                } else if (MessageFactory.TIPO_MENSAJE.FINAL_MESSAGE.toString().equals(instruction)) {
                    Logger.Log("Game: " + id + ", recibí el mensaje final del jugador" + message.getId());
                    int playerPos = getPlayer(message.getId());
                    String finalMessage = ((String)message.getMessage()).substring(MessageFactory.TIPO_MENSAJE.FINAL_MESSAGE.toString().length() + 1);
                    Logger.Log("Game: " + id + ", mensaje recibido: " + finalMessage);
                    Logger.Log("Game: " + id + ", verificando el mensaje.");
                    IEncrypter enc = EncrypterFactory.getIntance(EncrypterFactory.METHOD.AES.toString());
                    enc.makeKeys(key);
                    String finalMessageDecrypted = enc.decrypt(Base64.getEncoder().encodeToString(spellsBook.getSpell(spellWinNumber)));
                    Logger.Log("Game: Mensaje final desencriptado: " + finalMessageDecrypted);
                    enc = EncrypterFactory.getIntance(EncrypterFactory.METHOD.SHA256.toString());
                    String finalMessageSHA256 = enc.encrypt(finalMessageDecrypted);
                    Logger.Log("Game: SHA256 del mensaje final desencriptado: " + finalMessageSHA256);
                    Logger.Log("Game: Comparando contra: " + Base64.getEncoder().encodeToString(spellsBook.getSpellsSHA256(spellWinNumber)));
                    
                    if (finalMessageSHA256.equals(Base64.getEncoder().encodeToString(spellsBook.getSpellsSHA256(spellWinNumber)))) {
                        Logger.Log("Game: Tenemos un ganador: " + players[playerPos].getClient().getName());
                        players[playerPos].sendMessage(MessageFactory.createMessage(message.getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.WINNER));
                        players[(playerPos + 1) % 2].sendMessage(MessageFactory.createMessage(message.getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.LOOSER));
                        
                    }
                }
                
            } /*else if (message.getMessage() instanceof Deck) {
                int playerPos = getPlayer(message.getId());
                if (decks[playerPos] != null) {
                    decks[playerPos] = (Deck)message.getMessage();
                    if (decks[(playerPos + 1) % 2] != null){
                        sendMessageToBothPlayers(MessageFactory.createMessage("", IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.START_GAME));
                        startGame();
                    } else {
                        sendMessageToPlayer(playerPos, MessageFactory.createMessage(players[playerPos].getClient().getId(), IMessage.TIPO.MESSAGE_FROM_SERVER, MessageFactory.TIPO_MENSAJE.WAITING_FOR_SECOND_PLAYERS_DECK));
                    }
                }
            }*/
        }
    }
    
    public void sendMessageToBothPlayers (Message message) {
        for (int playerPos = 0; playerPos < players.length; playerPos++) {
            message.setId(players[playerPos].getClient().getId());
            sendMessageToPlayer(playerPos, message);
        }
    }
    
    public void sendMessageToPlayer (int pPlayerPos, Message message) {
        players[pPlayerPos].sendMessage(message);
    }
    
    public void sendMessageToPlayer (String pPlayerId, Message message) {
        players[getPlayer(pPlayerId)].sendMessage(message);
    }
    
    private int getPlayer(String pId) {
        Logger.Log("Game: " + "Comparando " + players[0].getClient().getId() + " con " + pId);
        if (players[0].getClient().getId().equals(pId)) {
            Logger.Log("Game: " + "Es el jugador 1.");
            return 0;
        }
        Logger.Log("Game: " + "Es el jugador 2.");
        return 1;
    }
    
    private void restartDecks() {
        for (int pos = 0; pos < decks.length; pos++) {
            decks[pos] = null;
        }
    }
    
    private void restartCrackedCards() {
        for (int pos = 0; pos < crackedCards.length; pos++) {
            crackedCards[pos] = 0;
        }
    }
    
    private void restartMatch() {
        restartDecks();
        restartCrackedCards();
        
    }
    
    private void restartGame() {
        restartMatch();
        
        for (int pos = 0; pos < winMatches.length; pos++) {
            winMatches[pos] = 0;
        }
        
        spellWinNumber = (int)Math.floor(Math.random() * 100);
        
        Logger.Log("Game: " + id + ", Leyendo la llave ganadora.");
        String pFileTxtPath = ClassLoader.getSystemResource("model/game/keys.txt").getFile();
        if (OS.isWindows()) {
            pFileTxtPath = pFileTxtPath.substring(1);
        }
        key = ReadWriteFiles.readLineTxtFile(pFileTxtPath, spellWinNumber);
        Logger.Log("Game: " + id + ", La llave es " + key + ", número de conjuro: " + spellWinNumber);
    }
    
}
