package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.App;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIView extends View
{
    private final Object lock = new Object();
    private App app;
    private Stage primaryStage;
    public GUIView() throws InterruptedException {
        startupGUI();
    }

    private void startupGUI() throws InterruptedException {
        App.applicationStartedEvent.addEventHandler((app, stage) -> {
            synchronized (lock){
                this.app = app;
                this.primaryStage = stage;
                this.lock.notifyAll();
            }
        });
        synchronized (lock) {
            (new Thread(App::launchApp)).start();
            lock.wait();
        }
        setupStage();
        setupLoginScene();
    }

    private void setupLoginScene()
    {
        try
        {
            int risY = 4323;
            int risX = 3024;
            double scale = 1.3/10;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login/LoginGUI.fxml"));
            Parent root = fxmlLoader.load();
            Scene loginScene = new Scene(root, risY*scale, risX*scale);
            loginScene.getStylesheets().add("/view/login/LoginGUI.css");
            loginScene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    Platform.exit();
                }});
            LoginGUI tmp = fxmlLoader.getController();
            tmp.loginStarted.addEventHandler((a,b) -> app.show());
            this.login = tmp;
            app.setScene(loginScene);
        } catch (IOException e) {
            //todo
        }
    }

    private void setupStage(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Welcome Adrenaline!");
            primaryStage.setResizable(false);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        });
    }

}
