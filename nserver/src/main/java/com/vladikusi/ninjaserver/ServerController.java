package com.vladikusi.ninjaserver;

import java.io.*;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.JSONArray;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.text.*;

public class ServerController implements Initializable{
    @FXML
    private Label chosenMember;
    @FXML
    private TextFlow servFlow;
    @FXML
    private ListView<String> members;
    public DateTimeFormatter timeFormat;

    private Server server;

    public void clearChMember()
    {
        if (members.getSelectionModel().getSelectedItem() == null)
            chosenMember.setText("Никто не выбран");
    }
    
    public void appendFlow(String str)
    {
        Text t1 = new Text(LocalTime.now().format(timeFormat) + ' ');
        Text t2 = new Text(str);
        t1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        t2.setFont(Font.font("Arial", 14)); 
        servFlow.getChildren().addAll(t1, t2);
    }

    public void updateNames()
    {
        server.checkClients();
        members.setItems(ServerInfo.names);
    }
    
    @FXML
    private void launchServer() throws IOException {
        if (server == null || !server.isActive())
        {
            server = new Server(1111);
            server.activate();
            appendFlow("Сервер запущен\n");
        }
    }

    @FXML
    private void closeServer() throws IOException {
        if (server != null) {
            if(server.isActive())
                appendFlow("Сервер выключен\n");
            server.stop();
        }
    }
    
    public void shutdown()
    {
        ServerInfo.names.removeAll();
        if (server != null) server.stop();
        Platform.exit();
    }

    @FXML
    private void kickChatter() throws IOException {
        String kicked = members.getSelectionModel().getSelectedItem();
        if (kicked != null)
        {
            ServerInfo.delName(kicked);
            server.checkClients();
            appendFlow(kicked + ": был выгнан\n");
            chosenMember.setText("Никто не выбран");
        }
    }

    @FXML
    private void TextView() throws IOException {

    }

    @FXML
    private void onListClicked() throws IOException {
        if (members.getSelectionModel().getSelectedItem() != null)
            chosenMember.setText("Выбран " + members.getSelectionModel().getSelectedItem());
    }

    public void updateAllsChat()
    {
        server.sendToClients();
    }
    public void checkClients()
    {
        server.checkClients();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ServerInfo.setServerController(this);
        ServerInfo.chatLog = new JSONArray();
        ServerInfo.readJSONFile();
        timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        servFlow.setPadding(new Insets(10));
        servFlow.setLineSpacing(5);
        ServerInfo.names = FXCollections.observableArrayList();
        members.setItems(ServerInfo.names);
    }
}
