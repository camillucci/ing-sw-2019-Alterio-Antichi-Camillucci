package it.polimi.ingsw;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application
{
    public static final IEvent<App, Stage> applicationStartedEvent = new Event<>();
    private Stage primaryStage;

    public void setScene(Scene scene){
        Platform.runLater(() -> this.primaryStage.setScene(scene));
     }

    public static void launchApp() {
        launch();
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        ((Event<App, Stage>)applicationStartedEvent).invoke(this, primaryStage);
    }

    public void show(){
        Platform.runLater(() -> this.primaryStage.show());
    }
}

