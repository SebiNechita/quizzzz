package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.utils.GameMode;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class EndGameCtrl extends SceneCtrl {


    @FXML
    private Text pointsObtained;
    @FXML
    private Text textPerformance;
    @FXML
    private HBox hboxText;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public EndGameCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Gets called when the scene is actually shown to the user
     */
    @OnShowScene
    public void onShowScene() {
        pointsObtained.setText(Main.scoreTotal + " points!");
        if (Main.scoreTotal < 100)
            textPerformance.setText("Poor performance, " + Main.USERNAME + "! " + "Try again!");
        else if (Main.scoreTotal > 2000)
            textPerformance.setText("Congratulation, " + Main.USERNAME + "!" + "!");
        else
            textPerformance.setText("Good game, " + Main.USERNAME + "!");
        textPerformance.setTextAlignment(TextAlignment.CENTER);
        hboxText.setAlignment(Pos.CENTER);
    }

    /**
     * Shows the Registration scene.
     */
    public void showSinglePlayerLeaderboard() {
        main.showScene(SingleplayerLeaderboardCtrl.class);
    }

    /**
     * Show the leaderboard.
     */
    public void showMainMenu() {
        main.showScene(MainMenuCtrl.class);
    }

    public void showRestart() {
        Main.gameMode = GameMode.SINGLEPLAYER;
        Main.currentQuestionCount = 0;
        Main.questions = new LinkedList<>();
        Main.openQuestions = new LinkedList<>();
        Main.questionHistory = new LinkedList<>();
        Main.scoreTotal = 0;
        main.getQuestions();
        main.jumpToNextQuestion();
    }

}
