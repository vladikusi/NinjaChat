package com.vladikusi.ninjaclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class PrimaryController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField ninjaField; 

    @FXML
    private void switchToSecondary() throws IOException {
        ClientInfo.username = nameField.getText().toString();
        ClientInfo.roomCode = ninjaField.getText().toString();
        if (ClientInfo.roomCode.isBlank())
            ClientInfo.roomCode = "0";
        if (!ClientInfo.username.isBlank())
        {
            Client.start(ClientInfo.username, ClientInfo.roomCode);
            App.setRoot("secondary");
        }
    }
    
    @FXML
    private void onEnter(ActionEvent ae) throws IOException
    {
        switchToSecondary();
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ClientInfo.setpController(this);
        Pattern pattern = Pattern.compile(".{0,20}");
        Pattern pattern2 = Pattern.compile(".{0,15}");
        TextFormatter<String> formatter1 = new TextFormatter<String>((UnaryOperator<TextFormatter.Change>) change -> {
        return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });
        TextFormatter<String> formatter2 = new TextFormatter<String>((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern2.matcher(change.getControlNewText()).matches() ? change : null;
        });
        nameField.setTextFormatter(formatter1);
        ninjaField.setTextFormatter(formatter2);
        ninjaField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    ninjaField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        ClientInfo.pController = this;
    }
}
