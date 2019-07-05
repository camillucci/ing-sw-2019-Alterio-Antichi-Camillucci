package it.polimi.ingsw.view.gui.endgame;

import it.polimi.ingsw.snapshots.MatchSnapshot;
import it.polimi.ingsw.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.view.gui.Cache;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

public class EndGameController implements Ifxml<Pane> {
    @FXML ImageView firstPlayer;
    @FXML ImageView secondPlayer;
    @FXML ImageView thirdPlayer;
    @FXML ImageView fourthPlayer;
    @FXML ImageView fifthPlayer;
    @FXML Label firstPos;
    @FXML Label secondPos;
    @FXML Label thirdPos;
    @FXML Label fourthPos;
    @FXML Label fifthPos;
    @FXML Label firstScore;
    @FXML Label secondScore;
    @FXML Label thirdScore;
    @FXML Label fourthScore;
    @FXML Label fifthScore;
    @FXML private Pane rootPane;
    private EndGameData endGameData;
    private List<ImageView> characters;
    private List<Label> scores;
    private List<Label> positions;

    public void initialize(){
        characters = getCharacters();
        scores = getScores();
        positions = getPositions();
    }

    private List<ImageView> getCharacters(){
        return Arrays.asList(firstPlayer, secondPlayer, thirdPlayer, fourthPlayer, fifthPlayer);
    }

    private List<Label> getPositions() {
        return Arrays.asList(firstPos, secondPos, thirdPos, fourthPos, fifthPos);
    }

    private List<Label> getScores() {
        return Arrays.asList(firstScore, secondScore, thirdScore, fourthScore, fifthScore);
    }

    @Override
    public Pane getRoot() {
        return rootPane;
    }

    private void buildController(EndGameData endGameData) {
        this.endGameData = endGameData;
        for(int i=0; i < endGameData.matchSnapshot.getPublicPlayerSnapshot().size() + 1; i++)
            setupPlayer(i+1);
    }

    private void setupPlayer(int pos) {
        String[] player = endGameData.scoreBoard[pos-1];
        setCharacter(pos, player[0]);
        setPos(pos);
        setScore(pos, player[1]);
    }

    private void setPos(int pos){
        String text = null;
        if(pos ==1) text = "1st";
        else if (pos == 2) text = "2nd";
        else if (pos == 3) text = "3rd";
        else if (pos == 4) text = "4th";
        else if (pos == 5) text = "5th";

        positions.get(pos-1).setText(text);
    }

    private void setScore(int pos, String score)
    {
        scores.get(pos-1).setText(score);
    }

    private void setCharacter(int pos, String name)
    {
        double height = 0.20;
        double scale = 0.85;
        if (pos == 2) height*=scale;
        else if(pos == 3) height *= scale*scale;
        else if(pos > 3) height *= scale*scale*scale;

        ImageView character = characters.get(pos-1);
        character.setImage(getCarachter(name));
        character.fitHeightProperty().bind(rootPane.heightProperty().multiply(height));
    }

    private String nameToColor(String name)
    {
        for(PublicPlayerSnapshot p : endGameData.matchSnapshot.getPublicPlayerSnapshot())
            if(p.name.equals(name))
                return p.color;
        return endGameData.matchSnapshot.privatePlayerSnapshot.color;
    }

    private Image getCarachter(String name){
        return Cache.getImage("/player/" + nameToColor(name).toLowerCase() + "_character.png");
    }

    public static EndGameController getController(EndGameData endGameData){
        EndGameController ret = GUIView.getController("/view/endGame/endGame.fxml", "/view/endGame/endGame.css");
        ret.buildController(endGameData);
        return ret;
    }

    public static class EndGameData
    {
        public final String[][] scoreBoard;
        public final MatchSnapshot matchSnapshot;
        public EndGameData(String[][] scoreBoard, MatchSnapshot matchSnapshot)
        {
            this.scoreBoard = scoreBoard;
            this.matchSnapshot = matchSnapshot;
        }
    }
}
