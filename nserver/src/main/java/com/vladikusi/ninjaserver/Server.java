package com.vladikusi.ninjaserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService pool;
    private final List<ServerThread> clients;
    private final int portNumber;
    private  boolean stop;

    Server(int portNumber) {
        this.portNumber = portNumber;
        pool = Executors.newCachedThreadPool();
        clients = new ArrayList<>();
    }

    private void runServer(){

        System.out.println("SERVER: Waiting for client");
        try{
            ServerSocket serverSocket = new ServerSocket(portNumber);
            stop = false;

            while(! stop){//do in loop to support multiple clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("SERVER: client connected");
                ServerThread st1 = new ServerThread(clientSocket);
                pool.execute(st1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        for( ServerThread st : clients) {
            st.stopServerTread();
        }
        stop = true;
        pool.shutdown();
    }

    public void activate(){
        new Thread(()->runServer()).start();
    }
}

class ServerThread extends Thread {

    private Socket socket = null;
    private  boolean stop;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try{
            stop = false;
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String fromClient;
            while(!stop){
                if((fromClient = in.readUTF()) != null) {
                    System.out.println("SERVER: recieved message - " + fromClient);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();;
        }
    }

    void stopServerTread(){
        stop = true;
    }
}
