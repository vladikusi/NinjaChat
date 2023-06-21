package com.vladikusi.ninjaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import javafx.application.Platform;

public class Server {

    private final ExecutorService pool;
    private final List<ServerThread> clients;
    private final int portNumber;
    private ServerSocket serverSocket;
    private boolean stop;

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
                ServerThread st = new ServerThread(clientSocket);
                clients.add(st);
                pool.execute(st);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkClients()
    {
        try{
            if (!clients.isEmpty())
                for (Iterator<ServerThread> iterator = clients.iterator(); iterator.hasNext();)
                {
                    ServerThread st = iterator.next();
                    if (!ServerInfo.takenName(st.username))
                    {
                        if (st.isConnected())
                            st.kickChatter();
                        iterator.remove();
                    }
                }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendToClients()
    {
        try{
            if (!clients.isEmpty())
                for (Iterator<ServerThread> iterator = clients.iterator(); iterator.hasNext();)
                {
                    ServerThread st = iterator.next();
                    if(st.isConnected())
                        st.JSONtoClient();
                }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void stop() {
        ServerInfo.names.clear();
        checkClients();
        if (!clients.isEmpty())
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
        ServerInfo.writeJSONFile();
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
    private DataInputStream dis;
    private DataOutputStream dos;
    public boolean stop;
    public boolean firstmsg;
    private boolean exited;
    private boolean nname;
    private boolean kicked;
    private JSONObject nmsg;
    public String username;
    private String roomCode;
    private String str;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        firstmsg = true;
        username = "";
    }

    @Override
    public void run() {
        try{
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
            stop = false;
            exited = false;
            nname = false;
            nmsg = new JSONObject();
            String fromClient;
            while(!stop){
                if((fromClient = dis.readUTF()) != null) {
                    str = fromClient;
                    if (firstmsg && !ServerInfo.takenName(str))
                    {
                        roomCode = dis.readUTF();
                        nname = true;
                        username = str;
                        str = "подключился.\n";
                        System.out.println("Username: " + username);
                        firstmsg = false;                        
                    }
                    else if ((firstmsg && ServerInfo.takenName(str)))
                    {
                        dos.writeInt(0);
                        dos.writeUTF("Exit");
                        kicked = true;
                    }
                    else if (!str.equals("Exit"))
                    {
                        System.out.println("SERVER: received message - " + username + ": " + str);
                    }
                    else if (str.equals("Exit"))
                    {
                        str = "отключился.\n";
                        exited = true;
                    }
                    Platform.runLater(new Runnable(){ 
                        @Override
                        public void run() {
                            if (nname)
                            {
                                ServerInfo.addName(username);
                                nname = false;
                            }
                            if(!kicked)
                            {
                                nmsg.put("Time", LocalTime.now().format(timeFormat).toString());
                                nmsg.put("Username", username);
                                nmsg.put("Message", str);
                                nmsg.put("RoomCode", roomCode);
                                ServerInfo.appendFlow(username + "|" + roomCode + ": " + str);
                                if (!nmsg.isEmpty())
                                {
                                    ServerInfo.addJO(nmsg);
                                    nmsg = new JSONObject();
                                }
                                ServerInfo.updateAllsChat();
                            }
                            if (exited && ServerInfo.takenName(username))
                            {
                                try{
                                    dos.writeInt(0);
                                    dos.writeUTF("Exit");
                                }
                                catch(IOException e)
                                {
                                    e.printStackTrace();
                                }
                                ServerInfo.delName(username);
                            }
                            ServerInfo.updateNames();
                            ServerInfo.clearChMember();
                        }
                    });
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void JSONtoClient() throws IOException
    {
        dos.writeInt(1);
        dos.writeInt(ServerInfo.chatLog.length());
        for (int i = 0; i < ServerInfo.chatLog.length(); i++)
        {
            JSONObject explrObject = ServerInfo.chatLog.getJSONObject(i);
            System.out.println(explrObject.get("Time").toString() + " " + explrObject.get("Username").toString() + " " + explrObject.get("Message").toString());
            dos.writeUTF(explrObject.get("Time").toString());
            dos.writeUTF(explrObject.get("Username").toString());
            dos.writeUTF(explrObject.get("Message").toString());
            dos.writeUTF(explrObject.get("RoomCode").toString());
        }
    }

    public void kickChatter() throws IOException
    {
        dos.writeInt(0);
        dos.writeUTF("Exit");
    }

    public boolean isConnected() throws IOException
    {
        return socket.isConnected();
    }

    void stopServerThread(){
        stop = true;
    }
}

