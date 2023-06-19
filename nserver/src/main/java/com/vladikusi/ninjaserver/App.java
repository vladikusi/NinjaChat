package com.vladikusi.ninjaserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("ServerView.fxml"));
        Parent root = loader.load();
        ServerController controller = loader.getController();
        stage.setResizable(false);
        scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> controller.shutdown());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
