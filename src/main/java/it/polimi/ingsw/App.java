package it.polimi.ingsw;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setupScenes() throws IOException {
        int risY = 4323;
        int risX = 3024;
        double scale = 1.3/10;
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginGUI.fxml"));
        loginScene = new Scene(root, risY*scale, risX*scale);
        loginScene.getStylesheets().add("/view/LoginGUI.css");
    }
}
