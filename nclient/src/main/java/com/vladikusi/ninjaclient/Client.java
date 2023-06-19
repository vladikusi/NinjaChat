package com.vladikusi.ninjaclient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    final static int ServerPort = 1111;
    private static boolean stop;
    private static Socket s;
    private static Scanner scn;
    private static DataOutputStream dos;
    private static DataInputStream dis;

    public static void start(String Username) throws UnknownHostException, IOException
    {
        stop = false;
        InetAddress ip = InetAddress.getByName("localhost");
        s = new Socket(ip, ServerPort);
        scn = new Scanner(System.in, "UTF-8");
        dos = new DataOutputStream(s.getOutputStream());
        dis = new DataInputStream(s.getInputStream());
        dos.writeUTF(Username);
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stop) {
                    // read the message to deliver.
                    String msg = scn.nextLine();
                    try {
                       // write on the output stream
                        dos.writeUTF(msg);
                        if (msg.equals("Exit"))
                            stop = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    s.close();
                    scn.close();
                } 
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (!stop) {
                    try {
                        String msg = dis.readUTF();
                        if(msg.equals("Exit"))
                            stop = true;
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    } 
                }
                try {
                    s.close();
                    scn.close();
                } 
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        sendMessage.start();
        readMessage.start();
    }

    public static void main(String args[]) throws UnknownHostException, IOException {
        stop = false;
        Scanner scn = new Scanner(System.in, "UTF-8");

        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        DataInputStream dis = new DataInputStream(s.getInputStream());
        dos.writeUTF("Джек"); // имя
        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stop) {
                    // read the message to deliver.
                    String msg = scn.nextLine();
                    try {
                       // write on the output stream
                        dos.writeUTF(msg);
                        if (msg.equals("Exit"))
                            stop = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    s.close();
                    scn.close();
                } 
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (!stop) {
                    try {
                        String msg = dis.readUTF();
                        if(msg.equals("Exit"))
                            stop = true;
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    } 
                }
                try {
                    s.close();
                    scn.close();
                } 
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        sendMessage.start();
        readMessage.start();
    }
}