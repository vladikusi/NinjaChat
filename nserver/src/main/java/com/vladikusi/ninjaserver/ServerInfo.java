package com.vladikusi.ninjaserver;

import javafx.collections.ObservableList;

public class ServerInfo {
    private static ServerController controller;
    public static ObservableList<String> names;

    public static void setServerController(ServerController serverController)
    {
        ServerInfo.controller = serverController;
    }

    public static void appendFlow(String str)
    {
        controller.appendFlow(str);
    }

    public static boolean takenName(String str)
    {
        return names.contains(str);
    }

    public static void addName(String str)
    {
        names.add(str);
    }
    
    public static void delName(String str)
    {
        names.remove(str);
    }

    public static void updateNames()
    {
        controller.updateNames();
    }

}
