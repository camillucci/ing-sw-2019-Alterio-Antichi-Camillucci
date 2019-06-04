package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.App;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.actionHandler.ActionHandlerGUI;
import it.polimi.ingsw.view.gui.login.NewLoginGUI;
import it.polimi.ingsw.view.gui.login.NicknameController;
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
    public GUIView() throws InterruptedException, IOException {
        startupGUI();
    }

    private void startupGUI() throws InterruptedException, IOException {
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
        this.curViewElement = this.login = NewLoginGUI.createLoginScene(app);
        this.actionHandler = new ActionHandlerGUI();
        //this.actionHandler = ActionHandlerGUI.getController();
        //((ActionHandlerGUI) actionHandler).start(app);
    }

    private void setupStage(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Welcome Adrenaline!");
            //primaryStage.setResizable(false);
            primaryStage.setFullScreenExitHint("");
            //primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.setOnCloseRequest(e ->
            {
                Platform.exit();
                System.exit(0);
            });
        });
    }

    public static <V> V getController(String url) throws IOException {
       return getController(url, null);
    }

    public static <V> V getController(String fxmlUrl, String cssUrl) {
        FXMLLoader fxmlLoader = new FXMLLoader(NicknameController.class.getResource(fxmlUrl));
        try
        {
            Parent root = null;
            root = fxmlLoader.load();
            if(cssUrl != null)
                root.getStylesheets().add(cssUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (V) fxmlLoader.getController();
    }

}

