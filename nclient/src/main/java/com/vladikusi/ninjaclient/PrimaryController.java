package com.vladikusi.ninjaclient;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        Client.main(null);
        App.setRoot("secondary");
    }
}
