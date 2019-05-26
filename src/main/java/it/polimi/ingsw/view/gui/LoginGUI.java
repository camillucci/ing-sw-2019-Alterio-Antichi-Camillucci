package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.Login;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.generics.Utils.newThread;

public class LoginGUI extends Login
{
    private static final int MAX_SKULL = 8;
    private static final int TOT_MAPS = 3;
    private static final int MAX_PLAYERS = 5;
    private int totJoined = 0;
    public final IEvent<LoginGUI, Object> loginStarted = new Event<>();
    private  ImageView [] maps = new ImageView[TOT_MAPS];
    private String[] robotSpeech = new String[]{ "Hey, my name is :D-STRUCT-0R,", "Welcome to Adrenaline!", "Please, choose a nickname!"};
    private Timeline timeline;
    private VBox joiningPlayerVBox;
    @FXML private VBox vBox;
    @FXML private HBox robotHBox;
    @FXML HBox userHBox;
    @FXML private TextField loginText;
    @FXML private Button next;
    @FXML private Label robotLabel;

    @FXML
    protected void initialize(){
        loadMaps();
        next.setOnAction(e -> {
            next.setDisable(true);
            ((Event<Login, String>)nameEvent).invoke(this, loginText.getText());
        });
        Executors.newSingleThreadScheduledExecutor().schedule(() -> animation(0), 1, TimeUnit.SECONDS);
    }

    private void robotSpeak(String text, int millisecPerCar, Runnable onEnd){
        if(timeline != null)
            timeline.stop();
        timeline = Animations.autoWriteLabel(robotLabel, text, millisecPerCar, onEnd);
    }
    private void robotSpeak(String text, Runnable onEnd){
        robotSpeak(text, 60, onEnd);
    }
    private void robotSpeak(String text){
        robotSpeak(text,  () ->{});
    }

    private void animation(int i){
        if(i < robotSpeech.length)
            robotSpeak(robotSpeech[i], () -> animation(i+1));
        else
            loginText.requestFocus();
    }

    @Override
    public void notifyAccepted(boolean accepted) {
        Platform.runLater(() -> {
            if (!accepted) {
                robotSpeak("I'm sorry, this name it's not valid", () -> robotSpeak("Please, try again"));
                loginText.setText("");
                loginText.requestFocus();
            }
        });
    }

    @Override
    public void notifyAvailableColor(List<String> availableColors) {
        Platform.runLater(() -> {
            robotSpeak("Great! what's your favourite color?");
            userHBox.getChildren().clear();
            userHBox.setSpacing(30);
            for (int i = 0; i < availableColors.size(); i++)
                userHBox.getChildren().add(newColorButton(availableColors.get(i), i));
            next.setDisable(false);
        });
    }

    private Circle newColorButton(String color, int index){
        Circle ret = new Circle(30);
        ret.getStyleClass().add("colorButton");
        ret.setStyle("-fx-fill: " + color);
        ret.setOnMouseClicked(e -> newThread(() -> {
            disableHBox();
            ((Event<Login, Integer>)colorEvent).invoke(this, index);
        }));
        return ret;
    }

    private void disableHBox()
    {
        for(Node node : userHBox.getChildren())
            node.setDisable(true);
    }

    private Rectangle newSkullButton(int i){
        Rectangle ret = new Rectangle(90, 100);
        ret.setFill(new ImagePattern(new Image("skull.png")));
        ret.getStyleClass().add("buttonSkull");
        ret.setOnMouseEntered(e -> {
            if(userHBox.getChildren().size() == MAX_SKULL && i >=4)
                for(int j=0; j <= i; j++)
                    userHBox.getChildren().get(j).getStyleClass().add("buttonSkullHover");
        });
        ret.setOnMouseExited(e -> {
            if(userHBox.getChildren().size() == MAX_SKULL && i >= 4)
                for(int j=0; j <= i; j++)
                    userHBox.getChildren().get(j).getStyleClass().remove("buttonSkullHover");
        });
        ret.setOnMouseClicked(e -> {
            if(i >=4) {
                disableHBox();
                newThread(() -> {
                    ((Event<Login, Integer>) gameLengthEvent).invoke(this, i);
                    chooseGameSize();
                });
            }
        });
        return ret;
    }

    private void chooseSkull(){
        robotSpeak("You are the first in the room", () -> robotSpeak("How many skulls?"));
        userHBox.getChildren().clear();
        userHBox.setSpacing(0);
        for (int i = 0; i < MAX_SKULL; i++)
            userHBox.getChildren().add(newSkullButton(i));
    }

    private void loadMaps(){
        for(IntegerProperty i = new SimpleIntegerProperty(0); i.get() < TOT_MAPS; i.set(i.get()+1)) {
            maps[i.get()] = newMap(i.get());
            maps[i.get()].setOnMouseClicked(e -> {
                disableHBox();
                newThread(() -> ((Event<Login, Integer>) gameMapEvent).invoke(this, i.get()));
                prepareRoomGUI();
            });
        }
    }

    private void chooseGameSize()
    {
        Platform.runLater(() -> {
            userHBox.setSpacing(40);
            robotSpeak("Great! now choose the map");
            userHBox.getChildren().clear();
            HBox imageBox = new HBox();
            userHBox.getChildren().add(imageBox);
            Polygon nextButton = nextMapButton();
            userHBox.getChildren().add(nextButton);
            imageBox.getChildren().add(maps[0]);
            IntegerProperty i = new SimpleIntegerProperty(0);
            nextButton.setOnMouseClicked(e -> {
                i.set(i.get() == TOT_MAPS - 1 ? 0 : i.get() + 1);
                imageBox.getChildren().clear();
                imageBox.getChildren().add(maps[i.get()]);
            });
        });
    }

    private Polygon nextMapButton(){
        final double scale = 0.5;
        Polygon triangle = new Polygon(scale*80, scale*40, 0, scale*80, 0, 0);
        triangle.getStyleClass().add("arrowButton");
        triangle.getStyleClass().add("button");
        return triangle;
    }

    private ImageView newMap(int i)
    {
        final String partialUrl = "map";
        String url = partialUrl + i +".png";
        ImageView ret = new ImageView(new Image(url));
        ret.setFitWidth(700);
        ret.setFitHeight(536);
        ret.getStyleClass().add("mapImage");
        return ret;
    }

    @Override
    public void notifyHost(boolean isHost)  {
        if(isHost)
            Platform.runLater(this::chooseSkull);
        else
            Platform.runLater(this::prepareRoomGUI);
    }

    private void prepareRoomGUI()
    {
        prepareJoining();
        prepareLoading();
    }

    private void prepareLoading(){
        Node node = robotHBox.getChildren().get(1);
        robotHBox.getChildren().remove(1);
        ImageView loadingGif = loadingGif();
        robotHBox.getChildren().add(loadingGif);
        robotHBox.getChildren().add(node);
        robotSpeak("Waiting for other players to join");
    }

    private void prepareJoining()
    {
        vBox.getChildren().remove(1);
        joiningPlayerVBox = new VBox();
        joiningPlayerVBox.setSpacing(10);
        vBox.getChildren().add(joiningPlayerVBox);
        for(int i=0; i < MAX_PLAYERS; i++)
            joiningPlayerVBox.getChildren().add(getRow());
    }

    private HBox getRow()
    {
        HBox ret = new HBox();
        ret.setSpacing(40);
        ret.setAlignment(Pos.CENTER);
        Rectangle rectangle = new Rectangle(40,40);
        rectangle.getStyleClass().add("rectangle");
        Label playerNameLabel = new Label("");
        playerNameLabel.getStyleClass().add("newPlayerLabel");
        playerNameLabel.setMinWidth(550);
        ret.getChildren().add(rectangle);
        ret.getChildren().add(playerNameLabel);
        return ret;
    }

    private ImageView loadingGif(){
        ImageView imageView = new ImageView(new Image("loading.gif"));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        return imageView;
    }

    @Override
    public void login()
    {
        newThread( () -> ((Event<LoginGUI, Object>)loginStarted).invoke(this, null));
    }

    @Override
    public void onNewMessage(String message) {
        Platform.runLater(() -> parseMessage(message));
    }

    private void countdownMessage(String message){

    }
    private void timerStopMessage(String message){

    }
    private void parseMessage(String message)
    {
        if(message.contains("seconds left\n"))
            countdownMessage(message);
        else if (message.contains("Countdown stopped\\n"))
            timerStopMessage(message);
        else if(message.contains("joined the room"))
            playerJoined(message);
    }

    private void playerJoined(String message)
    {
        int nameLen;
        for(nameLen = 0; message.charAt(nameLen) != ' '; nameLen++)
            ;
        String name = message.substring(0, nameLen -1);
        HBox hBox = (HBox)joiningPlayerVBox.getChildren().get(totJoined++);
        hBox.getChildren().get(0).getStyleClass().remove("rectangle");
        hBox.getChildren().get(0).getStyleClass().add("joinedRectangle");
        Label playerLabel = (Label) hBox.getChildren().get(1);
        playerLabel.setText(name);
    }

    private void playerLeft(String name){

    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        //TODO
    }
}
