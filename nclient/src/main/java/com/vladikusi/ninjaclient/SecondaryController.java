package com.vladikusi.ninjaclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.*;

public class SecondaryController implements Initializable{
    @FXML
    private TextFlow chatFlow;
    @FXML
    private TextField chatField;
    @FXML
    private Label label;

    public void printFlow()
    {
        if(!ClientInfo.chatLog.isEmpty())
        {
            chatFlow.getChildren().clear();
            for (int i = 0; i < ClientInfo.chatLog.length(); ++i)
            {
                JSONObject explrObject = ClientInfo.chatLog.getJSONObject(i);
                if (explrObject.get("RoomCode").toString().equals(ClientInfo.roomCode))
                {
                    Text t1 = new Text(explrObject.get("Time").toString() + " ");
                    Text t2 = new Text(explrObject.get("Username").toString() + ": " + explrObject.get("Message"));
                    t1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    t2.setFont(Font.font("Arial", 14));
                    chatFlow.getChildren().addAll(t1, t2);
                }
            }
        }
    }

    @FXML
    private void onEnter(ActionEvent ae) throws IOException
    {
        if(!chatField.getText().isBlank())
            Client.sendMessage(chatField.getText() + '\n');
        chatField.setText("");
    }
    @FXML
    private void sendButton() throws IOException
    {
        if(!chatField.getText().isBlank())
            Client.sendMessage(chatField.getText() + '\n');
        chatField.setText("");
    }
    @FXML
    private void switchToPrimary() throws IOException {
        Client.close();
        App.setRoot("primary");
    }

    public void kickedToPrimary() throws IOException
    {
        //Потом добавить мессадж
        App.setRoot("primary");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ClientInfo.chatLog = new JSONArray();
        ClientInfo.setsController(this);
        Text t = new Text("Поздоровайтесь с чатом!\n");
        t.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        chatFlow.getChildren().addAll(t);
        label.setText("Кот комнаты:\n" + ClientInfo.roomCode);
    }
    
}

