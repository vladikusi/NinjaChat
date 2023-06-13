package com.vladikusi.ninjaserver;

import java.io.*;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.collections.*;
import javafx.event.ActionEvent;
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
    private ListView members;
    private ObservableList<String> names;
    
    @FXML
    private void launchServer() throws IOException {

    }

    @FXML
    private void closeServer() throws IOException {

    }
    
    @FXML
    public void exitApplication(ActionEvent event)
    {
        
    }

    @FXML
    private void kickChatter() throws IOException {
        
    }

    @FXML
    private void TextView() throws IOException {

    }

    @FXML
    private void scrollText() throws IOException {

    }

    @FXML
    private void onListClicked() throws IOException {
        chosenMember.setText("Выбран " + members.getSelectionModel().getSelectedItem());
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        List<Text> txts = new ArrayList<Text>();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        for (int i = 0; i < 10; ++i)
        {
            Text t;
            t = new Text(LocalTime.now().format(timeFormat));
            t.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            txts.add(t);
            switch(i % 4)
            {
                case 0:
                    t = new Text(" Петя зашёл на сервер\n");
                    break;
                case 1: t = new Text(" Ваня зашёл на сервер\n");
                    break;
                case 2: t = new Text(" Ваня вышел из сервера\n");
                    break;
                case 3: t = new Text(" Петя вышел из сервера\n");
                    break;
                default: t = new Text("Дефолт\n");
                    break;
            }
            t.setFont(Font.font("Arial", 14));
            txts.add(t);
        }
        servFlow.setPadding(new Insets(10));
        servFlow.setLineSpacing(5);
        servFlow.getChildren().addAll(txts);
        names = FXCollections.observableArrayList("IVAN", "PETYA", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14");
        chosenMember.setText("Выбран " + names.get(0));
        members.setItems(names);
    }
}
