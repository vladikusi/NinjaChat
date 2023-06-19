package com.vladikusi.ninjaclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class PrimaryController implements Initializable {
    @FXML
    private TextField nameField;

    @FXML
    private void switchToSecondary() throws IOException {
        ClientInfo.username = nameField.getText().toString();
        Client.start(ClientInfo.username);
        App.setRoot("secondary");
    }
    
    @FXML
    private void onEnter(ActionEvent ae)
    {
        System.out.println("Test");
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Pattern pattern = Pattern.compile(".{0,10}");
        TextFormatter<String> formatter = new TextFormatter<String>((UnaryOperator<TextFormatter.Change>) change -> {
        return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });

        nameField.setTextFormatter(formatter);
    }
}
