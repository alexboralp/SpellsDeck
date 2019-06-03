/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.client;

import java.io.IOException;
import java.net.Socket;
import model.socketnet.client.ClientSocketThread;
import utils.Logger;
import vista.player.SpellsDeck;

/**
 *
 * @author alexander
 */
public class Controller {
    
    private static SpellsDeck ventanaClient;
    
    private static Administrator administrator;
    
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
            
            int pPort = 19555; // Integer.parseInt(args[0]);
            String pHost = "localhost";
            
            System.out.println("Creando el administrador...");
            administrator = new Administrator();
            
            System.out.println("Creando la conexión con el server...");
            Socket socket = new Socket(pHost, pPort);
            
            if (socket.isConnected()) {
                System.out.println("Creando el cliente...");
                System.out.println(socket.getLocalSocketAddress().toString());
                model.socketnet.Client client = new model.socketnet.Client(socket.getLocalSocketAddress().toString(), socket);
                ClientSocketThread clientSocketThread = new ClientSocketThread(client);
                
                administrator.setClient(clientSocketThread);
                clientSocketThread.addObserver(administrator);
                
                clientSocketThread.startListening();
                
                System.out.println("Creando la interfaz gráfica...");
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
                System.err.println("Error al conectar con el server");
            }
            
            //</editor-fold>
        } catch (IOException ex) {
            Logger.Log(ex);
        }
        //</editor-fold>
    }
}