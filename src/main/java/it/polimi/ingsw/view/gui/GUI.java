package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

public class GUI extends Application {

    private Stage window; // The window
    private Scene scene; // The current view of the window
    private Scene scene2;
    private StackPane layout = new StackPane(); // The layout of the scene
    private StackPane layout2 = new StackPane();
    private Button loginButton = new Button("Login");
    private Button changeSceneButton = new Button("Go to map");
    private Image adrenalineImage = new Image("adrenaline.jpg");
    private Image map = new Image("map10.png");
    private BackgroundImage backgroundImage;
    private BackgroundImage backgroundImage2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Adrenaline");

        backgroundImage = new BackgroundImage(adrenalineImage, NO_REPEAT, NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true));
        backgroundImage2 = new BackgroundImage(map, NO_REPEAT, NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true));

        loginButton.setOnAction(actionEvent -> AlertBox.display("Login", "Not working yet"));
        changeSceneButton.setOnAction(actionEvent -> window.setScene(scene2));

        layout.setBackground(new Background(backgroundImage));
        layout.getChildren().addAll(loginButton, changeSceneButton);
        StackPane.setAlignment(loginButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(changeSceneButton, Pos.CENTER_RIGHT);

        layout2.setBackground(new Background(backgroundImage2));

        scene = new Scene(layout, 1441, 1008);
        scene2 = new Scene(layout2, 1273, 964);
        window.setScene(scene);
        window.show();
    }
}
