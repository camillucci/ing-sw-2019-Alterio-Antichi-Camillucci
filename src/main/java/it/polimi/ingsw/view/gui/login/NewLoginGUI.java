package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.view.Login;
import it.polimi.ingsw.view.gui.Animations;
import it.polimi.ingsw.view.gui.Cache;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
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
    private String[] robotSpeech = new String[]{"Awesome!", "Now choose a nickname!"};
    private Timeline timeline;
    private IntroController introController;
    private IpAddressController ipAddressController;
    private NicknameController nicknameController;
    private ColorChoiceController colorChoiceController;
    private SkullChoiceController skullChoiceController;
    private MapChoiceController mapChoiceController;
    private RoomJoinController roomJoinController;
    private ImageView map;
    private int colorChoiceErrorsCounter = 0;
    private static Scene loginScene;

    public void initialize(){}

    private void robotSpeak(String text, int millisecondsPerChar, Runnable onEnd) {
        if(timeline != null)
            timeline.stop();
        timeline = Animations.autoWriteLabel(robotLabel, text, millisecondsPerChar, onEnd);
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
        nicknameController.getLoginText().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                disableAnd(() -> {
                    String name = nicknameController.getLoginText().getText();
                    if(name.length() >= 2 && name.length() <= 16 && !name.contains(" "))
                        ((Event<Login, String>)nameEvent).invoke(this, name);
                    else
                        notifyAccepted(false);
                });
        });
        nicknameController.getNextButton().setOnAction(e -> disableAnd(() -> {
            String name = nicknameController.getLoginText().getText();
            if(name.length() >= 2 && name.length() <= 16 && !name.contains(" "))
                ((Event<Login, String>)nameEvent).invoke(this, name);
            else
                notifyAccepted(false);
        }));
        setBottomVBox(nicknameController.getRoot());
    }

    private void notifyIpAddress(String ipAddressText) {
        disableAnd(() -> ((Event<Login, String>)ipAddressEvent).invoke(this, ipAddressText));
        setupNickname();
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

    private void setBottomVBox(Parent root) {
        Platform.runLater(() -> {
            bottomVBox.getChildren().clear();
            bottomVBox.getChildren().add(root);});
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
    public void askIpAddress() {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> Platform.runLater(() ->
                robotSpeak("Hey, my name is :D-STRUCT-0R,", () -> robotSpeak("Welcome to Adrenaline!",
                        () -> robotSpeak("In order to start", () -> robotSpeak("Please enter the server's address:"))))), 1, TimeUnit.SECONDS);
        ipAddressController = IpAddressController.getController();
        ipAddressController.getIpAddressText().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER)
                notifyIpAddress(ipAddressController.getIpAddressText().getText());
        });
        ipAddressController.getNextButton().setOnAction(e -> notifyIpAddress(ipAddressController.getIpAddressText().getText()));
        setBottomVBox(ipAddressController.getRoot());
    }

    @Override
    public void notifyAccepted(boolean accepted) {
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
            if(colorChoiceErrorsCounter++ > 0)
                robotSpeak("Something has gone wrong", () -> robotSpeak("Please, try again!"));
            else
                robotSpeak("Great! Now choose a character!");
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
        robotSpeak("Where would you like to play?");
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
        ImageView imageView = new ImageView(Cache.getImage("/loading.gif"));
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
    public void disconnectedPlayerMessage(String name) {
        Platform.runLater(() -> roomJoinController.playerLeft(name));
    }

    @Override
    public void newPlayerMessage(String name) {
        Platform.runLater(() -> roomJoinController.newPlayerJoined(name));
    }

    @Override
    public void timerStartedMessage(int time) {
        String message = "Countdown is started:\n " + time + " seconds left";
        robotSpeak(message);
    }

    @Override
    public void timerTickMessage(int time) {
        String message = time + " seconds left";
        robotSpeak(message);
    }


    @Override
    public void onNewMessage(String message) {
        Platform.runLater(() -> robotSpeak(message));
    }

    private String getName(String playerMessageInfo){
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
    public VBox getRoot() {
        return upperVBox;
    }

    public static NewLoginGUI getController () {
        return GUIView.getController("/view/root.fxml");
    }
}
