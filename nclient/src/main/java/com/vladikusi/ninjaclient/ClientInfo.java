package com.vladikusi.ninjaclient;

import java.io.IOException;

import org.json.JSONArray;

public class ClientInfo {
    public static String username;
    public static String roomCode;
    public static PrimaryController pController;
    public static SecondaryController sController;
    public static JSONArray chatLog;

    public static void setpController(PrimaryController pController)
    {
        ClientInfo.pController = pController;
    }
    public static void setsController(SecondaryController sController)
    {
        ClientInfo.sController = sController;
    }

    public static void leaveToPrimary() throws IOException
    {
        sController.kickedToPrimary();
    }

    public static void shutdown() throws IOException
    {
        if (ClientInfo.username != null)
            Client.close();
    }
    
    public static void printFlow()
    {
        sController.printFlow();
    }
}
