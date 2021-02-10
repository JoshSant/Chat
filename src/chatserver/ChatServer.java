/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bart
 */
public class ChatServer {
    
    private boolean run = true;
    private List<ChatServerThread> serverThreads = new ArrayList<>();
    private ServerSocket servicio;
    
    public ChatServer(int port){
        try {
            servicio = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("constructor: " + ex.getLocalizedMessage());
        }
    
    }
    
    public void broadcast(String text){
        for(int i=0; i<serverThreads.size(); i++){
            if(serverThreads.get(i).getAlive()){
                serverThreads.get(i).send(text);
            } else {
                serverThreads.remove(serverThreads.get(i));
            }
        }
    }
    public boolean broadcastPrivado(String nombre, String nombre2, String texto){
        System.out.println(nombre+nombre2);
        for(int i=0; i<serverThreads.size(); i++){
            if(serverThreads.get(i).getAlive()){
                if(serverThreads.get(i).getNombre().equals(nombre)){
                    System.out.println("hola");
                    serverThreads.get(i).send("privado<"+nombre2+">>"+texto);
                }
            }
        }
        for(int i=0; i<serverThreads.size(); i++){
            if(serverThreads.get(i).getAlive()){
                if(serverThreads.get(i).getNombre().equals(nombre2)){
                    System.out.println("hola");
                    serverThreads.get(i).send("privado<"+nombre+">>"+texto);
                }
            }
        }
        return true;
    }
    
    public String clients(String nombre){
        String clientes = "Global";
        for(ChatServerThread client: serverThreads){
            if(client.getAlive()){
                if(!nombre.equals(client.getNombre())){
                    clientes = clientes +";"+ client.getNombre();
                }
            }
        }
        return clientes;
    }
    
        public void startService() {
            Thread mainThread = new Thread(){
                
                @Override
                public void run(){
                    ChatServerThread serverThread;
                    Socket servidor;
                while(run){
                    try {
                        servidor = servicio.accept();
                        serverThread = new ChatServerThread(ChatServer.this,servidor);
                        serverThreads.add(serverThread);
                        serverThread.setId(serverThreads.indexOf(serverThread));
                        serverThread.start();
                        } catch (IOException ex) {
                        System.out.println("startService: " + ex.getLocalizedMessage());
                    }
                }
                }
            };
           mainThread.start();
        }
        public static void main(String[] args) {
                ChatServer chatServer  = new ChatServer(5000);
                chatServer.startService();
        }
    
}
