package it.polimi.ingsw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application
{
    private Scene loginScene;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        setupScenes();
        primaryStage.setTitle("Welcome Adrenaline!");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void setupScenes() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginGUI.fxml"));
        loginScene = new Scene(root, 300, 275);
        loginScene.getStylesheets().add("/view/LoginGUI.css");
    }
}
