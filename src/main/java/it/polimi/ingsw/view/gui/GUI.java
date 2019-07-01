package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;

public class GUI extends Application {

    public PasswordField passwordField;
    @FXML private Text actionTarget;
    private Button handleSubmitButtonAction;
    private Stage window; // The window
    private Scene scene; // The current view of the window
    private Scene scene2;
    private StackPane layout = new StackPane(); // The layout of the scene
    private StackPane layout2 = new StackPane();
    private Button loginButton = new Button("Login");
    private Button changeSceneButton = new Button("Go to map");
    private Image adrenalineImage = new Image(getClass().getResourceAsStream("/adrenaline.jpg"));
    private Image map = new Image(getClass().getResourceAsStream("/map0.png"));
    private BackgroundImage backgroundImage;
    private BackgroundImage backgroundImage2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/root.fxml"));
        Scene scene = new Scene(root, 300, 275);

        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
        /*
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

         */
    }

    @FXML
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        actionTarget.setText("lol");
    }
}
