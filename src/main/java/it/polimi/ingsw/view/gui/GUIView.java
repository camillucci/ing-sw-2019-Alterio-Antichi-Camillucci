package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.App;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.Login;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.actionhandler.ActionHandlerGUI;
import it.polimi.ingsw.view.gui.endGame.EndGameController;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIView extends View
{
    private final Object lock = new Object();
    private App app;
    private Stage primaryStage;
    private Scene rootScene;
    private static final Logger logger = Logger.getLogger("GUIView");

    public GUIView() throws InterruptedException, IOException {
        startupGUI();
        System.setProperty("prism.order", "sw");
    }

    private void setupStage(){
        Platform.runLater(() -> {
            primaryStage.setTitle("Welcome Adrenaline!");
            primaryStage.setWidth(1000);
            primaryStage.setHeight(700);
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
        NewLoginGUI loginGUI = createLogin(app);
        ActionHandlerGUI actionHandlerGUI = createActionHandler(loginGUI);
        actionHandlerGUI.matchEndedEvent.addEventHandler((a, endGameData) -> onMatchEnded(endGameData));
        buildView(loginGUI, actionHandlerGUI);
    }

    private void onMatchEnded(EndGameController.EndGameData endGameData) {
        EndGameController endGameController = EndGameController.getController(endGameData);
        this.rootScene.setRoot(endGameController.getRoot());
    }

    private ActionHandlerGUI createActionHandler(Login login)
    {
        ActionHandlerGUI tmp = ActionHandlerGUI.getController();
        tmp.getRoot().minWidthProperty().bind(rootScene.widthProperty());
        tmp.getRoot().maxWidthProperty().bind(rootScene.widthProperty());
        tmp.getRoot().minHeightProperty().bind(rootScene.heightProperty());
        tmp.getRoot().maxHeightProperty().bind(rootScene.heightProperty());
        login.loginCompletedEvent.addEventHandler((a, b) -> rootScene.setRoot(tmp.getRoot()));
        return tmp;
    }

    public NewLoginGUI createLogin(App app) throws IOException
    {
        NewLoginGUI tmp = NewLoginGUI.getController();
        this.rootScene = new Scene(tmp.getRoot());
        rootScene.getStylesheets().add("/view/root.css");
        rootScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
                System.exit(0);
            }});
        tmp.loginStarted.addEventHandler((a,b) -> app.show());
        app.setScene(rootScene);
        app.show();
        return tmp;
    }

    public static <V> V getController(String url) {
       return getController(url, null);
    }

    public static <V> V getController(String fxmlUrl, String cssUrl) {
        FXMLLoader fxmlLoader = new FXMLLoader(NicknameController.class.getResource(fxmlUrl));
        try
        {
            Parent root;
            root = fxmlLoader.load();
            if(cssUrl != null)
                root.getStylesheets().add(cssUrl);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return fxmlLoader.getController();
    }
}
