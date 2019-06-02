/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellsnet;

import net.ServerNet;

/**
 *
 * @author alexander
 */
public class Server {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        ServerNet serverNet = new serverNet();
        if (observableSocket.isOk()) {
            System.out.println("En espera de clientes...");
            observableSocket.startListening();
        } else {
            System.out.println("Error al iniciar el socket...");
        }
        
        observableSocket.closeConnection();
    }
    
}
