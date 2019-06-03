/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.server;

import model.socketnet.server.ServerSocketThreadWaitClients;
import utils.Constants;
import vista.server.Server;

/**
 *
 * @author alexander
 */
public class Controller {
    
    private static Server ventanaServer;
    
    private static Administrator administrator;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

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
                java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
            
            //</editor-fold>
            
        System.out.println("Creando el socket del server...");
        int pPort = Constants.PORT; // Integer.parseInt(args[0]);
        ServerSocketThreadWaitClients sstwnc = new ServerSocketThreadWaitClients(pPort);
        
        if (sstwnc.isOk()) {
            
            System.out.println("Creando el administrador...");
            administrator = new Administrator();
            
            sstwnc.addObserver(administrator);
            
            System.out.println("En espera de nuevos clientes...");
            sstwnc.startListening();

            /* Create and display the form */
            System.out.println("Creando la interfaz gráfica...");
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    ventanaServer = new Server();
                    ventanaServer.addObserver(administrator);
                    administrator.setVentanaServer(ventanaServer);
                    ventanaServer.setVisible(true);
                }
            });
        } else {
            System.out.println("Error al crear el socket del server.");
        }
            
        //</editor-fold>
        
        //</editor-fold>
    }
}
