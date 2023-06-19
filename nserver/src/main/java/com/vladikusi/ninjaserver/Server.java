package com.vladikusi.ninjaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;

public class Server {

    private final ExecutorService pool;
    private final List<ServerThread> clients;
    private final int portNumber;
    private ServerSocket serverSocket;
    private boolean stop;
    private String str;

    Server(int portNumber) {
        this.portNumber = portNumber;
        pool = Executors.newCachedThreadPool();
        clients = new ArrayList<>();
        stop = false;
    }

    private void runServer(){
        System.out.println("SERVER: Waiting for client");
        try{
            serverSocket = new ServerSocket(portNumber);
            while(!stop) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("SERVER: client connected");
                ServerThread st1 = new ServerThread(clientSocket);
                pool.execute(st1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        for(ServerThread st : clients) {
            st.stopServerThread();
        }
        stop = true;
        pool.shutdown();
        try
        {
            serverSocket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void activate(){
        new Thread(()->runServer()).start();
    }
    public boolean isActive()
    {
        return !stop;
    }
}

class ServerThread extends Thread {
    private Socket socket = null;
    private boolean stop;
    private boolean firstmsg;
    private boolean exited;
    public String username;
    private String str;

    public ServerThread(Socket socket) {
        this.socket = socket;
        firstmsg = true;
        username = "";
    }

    @Override
    public void run() {
        try{
            stop = false;
            exited = false;
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String fromClient;
            while(!stop){
                if((fromClient = in.readUTF()) != null) {
                    str = fromClient;
                    if (firstmsg && !ServerInfo.takenName(str))
                    {
                        username = str;
                        ServerInfo.addName(str);
                        str = "подключился.";
                        System.out.println("Username: " + username);
                        firstmsg = false;
                    }
                    else if (firstmsg && ServerInfo.takenName(str) || !firstmsg && !ServerInfo.takenName(username))
                    {
                        out.writeUTF("Exit");
                        exited = true;
                    }
                    else if (!str.equals("Exit"))
                    {
                        System.out.println("SERVER: received message - " + username + ": " + str);
                    }
                    else if (str.equals("Exit"))
                    {
                        ServerInfo.delName(username);
                        str = "отключился.";
                    }
                    Platform.runLater(new Runnable(){ 
                        @Override
                        public void run() {
                            if (!exited)
                                ServerInfo.appendFlow(username + ": " + str);
                            ServerInfo.updateNames();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void stopServerThread(){
        stop = true;
    }
}
