/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prechatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreChatServer {

    public static void main(String[] args) {
        ServerSocket servicio;
        try {
            servicio = new ServerSocket(5000);
             Socket servidor = servicio.accept();
            DataInputStream flujoE = new DataInputStream(servidor.getInputStream());
            DataOutputStream flujoS = new DataOutputStream(servidor.getOutputStream());
            flujoS.writeUTF("hola cliente");
            String valor = flujoE.readUTF();
            System.out.println("servidor recibe: "+valor);
            while(valor.compareTo("fin") != 0){
                valor = flujoE.readUTF();
                System.out.println("servidor recibe: " + valor);
            }
            flujoE.close();
            flujoS.close();
            servidor.close();
            servicio.close();
        } catch (IOException ex) {
                System.out.println("mensaje:" + ex.getMessage());
        }
    }
    
}
