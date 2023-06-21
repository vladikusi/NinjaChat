package com.vladikusi.ninjaserver;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServerInfo {
    private static FileWriter fout;
    private static ServerController controller;
    public static ObservableList<String> names;

    public static JSONArray chatLog;

    public static void setServerController(ServerController serverController)
    {
        ServerInfo.controller = serverController;
    }

    public static void appendFlow(String str)
    {
        controller.appendFlow(str);
    }

    public static void declareJSON()
    {
        chatLog = new JSONArray();
    }
    
    public static void readJSONFile()
    {
        String filename = LocalDate.now().toString() + ".json";
        String loc = new String(filename);
        File file = new File(loc);
        try {
            
            String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
            JSONObject json = new JSONObject(content);
            chatLog = json.getJSONArray("chatLog");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeJSONFile()
    {
        JSONObject log = new JSONObject();
        log.put("chatLog", chatLog);
        String filename = LocalDate.now().toString() + ".json";
        try {
            fout = new FileWriter(filename, false);
            
            fout.write(log.toString());
            fout.flush();
            fout.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void addJO(JSONObject chato)
    {
        chatLog.put(chato);
    }

    public static boolean takenName(String str)
    {
        return (names != null)? names.contains(str): false;
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

    public static void clearChMember()
    {
        controller.clearChMember();
    }

    public static void updateAllsChat()
    {
        controller.updateAllsChat();
    }

    public static void checkClients()
    {
        controller.checkClients();
    }

}
