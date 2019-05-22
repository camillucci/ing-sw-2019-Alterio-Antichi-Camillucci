package it.polimi.ingsw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
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
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginGUI.fxml"));
        loginScene = new Scene(root, 300, 275);
        loginScene.getStylesheets().add("/View/LoginGUI.css");
    }
}
