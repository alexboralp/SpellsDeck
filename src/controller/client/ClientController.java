/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.client;

import java.io.IOException;
import java.net.Socket;
import model.game.objects.Deck;
import model.socketnet.client.ClientSocketThread;
import utils.Constants;
import utils.Logger;
import vista.player.SpellsDeck;

/**
 *
 * @author alexander
 */
public class ClientController {
    
    private static SpellsDeck ventanaClient;
    private static ClientAdministrator administrator;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {


        try {
            
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
            */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(vista.player.SpellsDeck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
            
            //</editor-fold>
            
            int pPort = Constants.PORT; // Integer.parseInt(args[0]);
            String pHost = Constants.HOST;
            
            Logger.Log("ClientController: " + "Creando el administrador...");
            administrator = new ClientAdministrator();
            
            Logger.Log("ClientController: " + "Creando la conexión con el server...");
            Socket socket = new Socket(pHost, pPort);
            
            if (socket.isConnected()) {
                Logger.Log("ClientController: " + "Creando el cliente...");
                Logger.Log("ClientController: " + "Cliente id: " + socket.getLocalSocketAddress().toString());
                model.socketnet.Client client = new model.socketnet.Client(socket.getLocalSocketAddress().toString(), socket);
                ClientSocketThread clientSocketThread = new ClientSocketThread(client);
                
                administrator.setClient(clientSocketThread);
                clientSocketThread.addObserver(administrator);
                
                clientSocketThread.startListening();
                
                Deck deck = new Deck();
                administrator.setDeck(deck);
                
                Logger.Log("ClientController: " + "Creando la interfaz gráfica...");
                /* Create and display the form */
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        ventanaClient = new SpellsDeck();
                        ventanaClient.addObserver(administrator);
                        administrator.setVentanaClient(ventanaClient);
                        ventanaClient.setVisible(true);
                    }
                });
            } else {
                Logger.Log("ClientController: " + "Error al conectar con el server");
            }
            
            //</editor-fold>
        } catch (IOException ex) {
            Logger.Log(ex);
        }
        //</editor-fold>
    }
}
