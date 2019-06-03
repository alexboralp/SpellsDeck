/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.game;

import model.game.objects.Deck;
import model.socketnet.server.ServerSocketThread;
import model.socketspellsdeck.message.IMessage;
import model.socketspellsdeck.message.Message;
import model.socketspellsdeck.message.MessageFactory;
import utils.observerpattern.IObserver;

/**
 *
 * @author alexander
 */
public class Game implements IObserver {
    
    private final ServerSocketThread players[];
    private final Deck decks[];
    
    public Game(ServerSocketThread pPlayer1, ServerSocketThread pPlayer2) {
        players = new ServerSocketThread[2];
        decks = new Deck[2];
        players[0] = pPlayer1;
        players[1] = pPlayer2;
        players[0].addObserver(this);
        players[1].addObserver(this);
    }
    
    public ServerSocketThread getPlayer1() {
        return players[0];
    }
    
    public ServerSocketThread getPlayer2() {
        return players[1];
    }

    @Override
    public void update(Object pMessage) {
        if (pMessage instanceof Message) {
            Message message = (Message)pMessage;
            if (message.getMessage() instanceof MessageFactory.TIPO_MENSAJE) {
                switch((MessageFactory.TIPO_MENSAJE)(message.getMessage())) {
                    case FINAL_MESSAGE:
                        // TODO Lógica de cuando se recibe el mensaje del jugador
                        break;
                    default:
                        break;
                }
            } else if (message.getMessage() instanceof Deck) {
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
            }
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
        System.out.println("Comparando " + players[0].getClient().getId() + " con " + pId);
        if (players[0].getClient().getId().equals(pId)) {
            return 0;
        }
        return 1;
    }
    
    private void startGame() {
        // TODO: Game logic
    }
}
