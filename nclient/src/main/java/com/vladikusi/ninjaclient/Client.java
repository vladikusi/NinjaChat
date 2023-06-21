package com.vladikusi.ninjaclient;

import java.io.*;
import java.net.*;

import org.json.JSONObject;

import javafx.application.Platform;

public class Client {
    final static int ServerPort = 1111;
    public static boolean stop;
    private static boolean fnmsg;
    private static Socket s;
    private static DataOutputStream dos;
    private static DataInputStream dis;
    private static boolean kicked;
    public static String message;
    private static int actionID;
    private static JSONObject nmsg;


    public static void start(String Username, String roomCode) throws UnknownHostException, IOException
    {
        nmsg = new JSONObject();
        fnmsg = false;
        stop = false;
        kicked = false;
        InetAddress ip = InetAddress.getByName("localhost");
        s = new Socket(ip, ServerPort);
        dos = new DataOutputStream(s.getOutputStream());
        dis = new DataInputStream(s.getInputStream());
        dos.writeUTF(Username);
        dos.writeUTF(roomCode);
        new Thread(() -> {
            String msg;
            try{
                while (!stop) {
                    actionID = dis.readInt();
                    switch (actionID)
                    {
                        case 0:
                            if ((msg = dis.readUTF()) != null)
                            {
                                if(msg.equals("Exit"))
                                {
                                    kicked = true;
                                    stop = true;
                                }
                            }
                            break;
                        case 1:
                            getJson();
                            break;
                        default: 
                            break;
                        }
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run()
                        {
                            if (actionID == 1)
                            {
                                ClientInfo.printFlow();
                            }
                            if (fnmsg)
                            {
                                ClientInfo.chatLog.clear();
                                fnmsg = false;
                            }
                        }
                    });
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            stop();
        }, "GettingMessage").start();
    }

    public static void sendMessage(String str) throws IOException
    {
        dos.writeUTF(str);
    }

    public static void getJson() throws IOException
    {
        fnmsg = false;
        nmsg = new JSONObject();
        int N = dis.readInt();
        for (int i = 0; i < N; i++)
        {
            nmsg.put("Time", dis.readUTF());
            nmsg.put("Username", dis.readUTF());
            nmsg.put("Message", dis.readUTF());
            nmsg.put("RoomCode", dis.readUTF());
            if (ClientInfo.chatLog != null)
                ClientInfo.chatLog.put(nmsg);
            nmsg = new JSONObject();
            fnmsg = true;
        }
    }

    public static void stop() {
        stop = true;
        ClientInfo.username = null;
        try {
            if(kicked)
                ClientInfo.leaveToPrimary();
            s.close();
            dos.close();
            dis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void close()
    {
        try {
            dos.writeUTF("Exit");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

