/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bart
 */
public class ChatServerThread extends Thread {
    
    private boolean run = true;
    private int id;
    private String nombre = "";
    private final Socket servidor;
    DataInputStream flujoE;
    DataOutputStream flujoS;
    private ChatServer server;
    
    
    public String getNombre(){
        return nombre;
    }
    
    public String setNombre(String nombre){
        this.nombre = nombre;
        return nombre;
    }

    public ChatServerThread(ChatServer server , Socket servidor) {
        this.server = server;
        this.servidor = servidor;
        try {
            flujoE = new DataInputStream(servidor.getInputStream());
            flujoS = new DataOutputStream(servidor.getOutputStream());
        } catch (IOException ex) {
            System.out.println("constructor: " + ex.getLocalizedMessage());
            run = false;
        }
    }
    
    @Override
    public void run(){
        String text;
        while(run){
            try {
                text = flujoE.readUTF();
                String[] textos = text.split(";");
                if(nombre.equals("")){
                    setNombre(textos[1]);       
                    server.broadcast("Ha entrado en el chat " + textos[1]);
                }else if(text.equals("dhsfgsdhfgshdfgkhdsghsdhsfghfgsdhjg")){
                    server.broadcast(nombre + " ha salido del chat");
                }else if(text.equals("cbvhjfbhvbydfyviyvvifbvhbdfhbvfdbvf")){
                    String clientes;
                    System.out.println(nombre);
                    clientes = server.clients(nombre);
                    flujoS.writeUTF(clientes);
                }else if(textos[0].equals("Global")){
                    server.broadcast(nombre + "> " + textos[1]);
                }else{
                    server.broadcastPrivado(nombre, textos[0], textos[1]);
                }
                //flujoS.writeUTF(id + "> " + text);
                //flujoS.flush();
            } catch (IOException ex) {
                System.out.println("run: " + ex.getLocalizedMessage());
                run = false;
            }
        }
    }
    
    public boolean getAlive(){
        return run;
    }
    
    public void send(String text){
        try {
            flujoS.writeUTF(text);
            flujoS.flush();
        } catch (IOException ex) {
            System.out.println("run: " + ex.getLocalizedMessage());
            run = false;
        }
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
}
