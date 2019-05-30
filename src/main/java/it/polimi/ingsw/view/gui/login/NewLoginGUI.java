package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.App;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.Login;
import it.polimi.ingsw.view.gui.Animations;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NewLoginGUI extends Login implements Ifxml<VBox>
{
    public final IEvent<NewLoginGUI, Object> loginStarted = new Event<>();
    @FXML private VBox upperVBox;
    @FXML private HBox robotHBox;
    @FXML private Label robotLabel;
    @FXML private VBox bottomVBox;
    private String[] robotSpeech = new String[]{ "Hey, my name is :D-STRUCT-0R,", "Welcome to Adrenaline!", "Please, choose a nickname!"};
    private Timeline timeline;
    private IntroController introController;
    private NicknameController nicknameController;
    private ColorChoiceController colorChoiceController;
    private SkullChoiceController skullChoiceController;
    private MapChoiceController mapChoiceController;
    private RoomJoinController roomJoinController;
    private ImageView map;
    int colorChoiceErrorsCOunter = 0;
    private static Scene loginScene;
    public void initialize(){}

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
            nicknameController.getLoginText().requestFocus();
    }

    private void setupNickname() {
        enable();
         animation(0);
        nicknameController = NicknameController.getController();
        nicknameController.getNextButton().setOnAction(e -> disableAnd(() -> ((Event<Login, String>)nameEvent).invoke(this, nicknameController.getLoginText().getText())));
        setBottomVBox(nicknameController.getRoot());
    }

    private void notifySocketRMI(boolean socket) {
        disableAnd(() -> ((Event<Login, Boolean>)socketEvent).invoke(this, socket));
        setupNickname();
    }

    private void enable(){
        bottomVBox.setDisable(false);
    }

    private void disableAnd(Runnable function){
        bottomVBox.setDisable(true);
        (new Thread(function)).start();
    }

    private void setUpperVBox(Parent root){
        upperVBox.getChildren().clear();
        upperVBox.getChildren().add(root);
    }

    private void setBottomVBox(Parent root){
        bottomVBox.getChildren().clear();
        bottomVBox.getChildren().add(root);
    }

    @Override
    public void askConnection() {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> Platform.runLater(() ->robotSpeak("RMI or Socket?")), 1, TimeUnit.SECONDS);
        introController = IntroController.getController();
        introController.getRMIButton().setOnAction(e -> notifySocketRMI(false));
        introController.getSocketButton().setOnAction(e -> notifySocketRMI(true));
        setBottomVBox(introController.getRoot());
    }

    @Override
    public void notifyAccepted(boolean accepted) throws IOException {
        Platform.runLater(() -> {
            if (!accepted) {
                enable();
                robotSpeak("I'm sorry, this name it's not valid", () -> robotSpeak("Please, try again"));
                nicknameController.getLoginText().setText("");
                nicknameController.getLoginText().requestFocus();
            }});
    }

    @Override
    public void notifyAvailableColor(List<String> availableColors) throws IOException
    {
        Platform.runLater(() -> {
            if(colorChoiceErrorsCOunter++ > 0)
                robotSpeak("Something has gone wrong", () -> robotSpeak("Please, try again!"));
            else
                robotSpeak("Great! what's your favourite color?");
            enable();
            colorChoiceController = ColorChoiceController.getController();
            setBottomVBox(colorChoiceController.getRoot());
            for (int i = 0; i < availableColors.size(); i++) {
                final int i2 = i;
                colorChoiceController.newColorButton(availableColors.get(i2)).setOnMouseClicked(e -> disableAnd(() -> ((Event<Login, Integer>) colorEvent).invoke(this, i2)));
            }
        });
    }

    @Override
    public void notifyHost(boolean isHost) throws IOException
    {
        enable();
        if(isHost)
            Platform.runLater(this::chooseSkull);
        else
            Platform.runLater(this::prepareRoomGUI);
    }

    private void chooseSkull()
    {
        robotSpeak("You are the first in the room", () -> robotSpeak("How many skulls?"));
        skullChoiceController = SkullChoiceController.getController();
        Rectangle[] skulls = skullChoiceController.getButtons();
        for(IntegerProperty i = new SimpleIntegerProperty(4); i.get() < skulls.length;i.set(i.get()+1))
            skulls[i.get()].setOnMouseClicked(e -> disableAnd(() -> {
                        ((Event<Login, Integer>) gameLengthEvent).invoke(this, i.get());
                        Platform.runLater(this::chooseGameSize);
                    }));
        setBottomVBox(skullChoiceController.getRoot());
    }

    private void chooseGameSize()
    {
        enable();
        mapChoiceController = MapChoiceController.getController();
        ImageView[] maps = mapChoiceController.getMaps();
        for(int i=0; i < maps.length; i++){
            final int i2 = i;
            maps[i].setOnMouseClicked(e -> {
                    disableAnd(() -> ((Event<Login, Integer>) gameMapEvent).invoke(this, i2));
                    Platform.runLater(this::prepareRoomGUI);
                });
        }
        setBottomVBox(mapChoiceController.getRoot());
    }

    private void prepareRoomGUI()
    {
        robotSpeak("Waiting for other players to join");
        robotHBox.getChildren().add(loadingGif());
        roomJoinController = RoomJoinController.getController();
        setBottomVBox(roomJoinController.getRoot());
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
        ((Event<NewLoginGUI, Object>)loginStarted).invoke(this, null);
    }

    @Override
    public void onNewMessage(String message) {
        Platform.runLater(() -> parseMessage(message));
    }

    private void parseMessage(String message)
    {
        if(message.contains("joined the room"))
            playerJoined(message);
        else if(message.contains("left the room"))
            playerLeft(message);
        else
            robotSpeak(message);
    }

    String getName(String playerMessageInfo){
        int nameLen;
        for(nameLen = 0; playerMessageInfo.charAt(nameLen) != ' '; nameLen++)
            ;
        return playerMessageInfo.substring(0, nameLen);
    }

    private void playerJoined(String message)
    {
        roomJoinController.newPlayerJoined(getName(message));
    }

    private void playerLeft(String message)
    {
        roomJoinController.playerLeft(getName(message));
    }


    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        Platform.runLater(() ->{
            upperVBox.getChildren().clear();
            ImageView imageView = new ImageView(new Image("map" + matchSnapshot.gameBoardSnapshot.mapType+".png"));
            imageView.setFitWidth(loginScene.widthProperty().get());
            imageView.setFitHeight(loginScene.heightProperty().get());
            upperVBox.getChildren().add(imageView);
        });
    }

    @Override
    public VBox getRoot() {
        return upperVBox;
    }

    private static NewLoginGUI getController () throws IOException {
        return GUIView.getController("/view/login/LoginGUI.fxml");
    }

    public static NewLoginGUI createLoginScene(App app) throws IOException {
        int risY = 4323;
        int risX = 3024;
        double scale = 3.0/10;
        NewLoginGUI ret = NewLoginGUI.getController();
        loginScene = new Scene(ret.getRoot(), risY*scale, risX*scale);
        loginScene.getStylesheets().add("/view/login/LoginGUI.css");
        loginScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
                System.exit(0);
            }});
        ret.loginStarted.addEventHandler((a,b) -> app.show());
        app.setScene(loginScene);
        app.show();
        return ret;
    }
}
