package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.utils.GameMode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EndGameCtrl extends SceneCtrl {


    @FXML
    private Text pointsObtained;
    @FXML
    private Text textPerformance;
    @FXML
    private HBox hboxText;
    @FXML
    private Button restartButton;
    @FXML
    private Button LeaderboardButton;
    @FXML
    private Button mainmenuButton;

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
        if (Main.gameMode==GameMode.MULTIPLAYER){
            onShowSceneMultiplayer();
        }
        int score = (Main.gameMode == GameMode.MULTIPLAYER) ?
                main.getMultiplayerGame().getScoreTotal() :
                main.getSingleplayerGame().getScoreTotal();

        pointsObtained.setText(score + " points!");
        if (score < 100)
            textPerformance.setText("Poor performance, " + Main.USERNAME + "! " + "Try again!");
        else if (score > 2000)
            textPerformance.setText("Congratulations, " + Main.USERNAME + "!" + "!");
        else
            textPerformance.setText("Good game, " + Main.USERNAME + "!");
    }

    /**
     * Leaves the game and tailors the scene for multiplayer
     */
    private void onShowSceneMultiplayer() {

        restartButton.setText("Rejoin lobby");
        restartButton.setOnAction(event->{
            main.getMultiplayerGame().leave();
            main.showScene(LobbyCtrl.class);
        });
        LeaderboardButton.setOnAction(event->{
            main.showScene(MultiplayerLeaderboardCtrl.class);
        });
        mainmenuButton.setOnAction(event -> {
            main.getMultiplayerGame().leave();
            main.quitMultiplayer();
            main.showScene(MainMenuCtrl.class);
        });
    }

    /**
     * Shows the Registration scene.
     */
    public void showSinglePlayerLeaderboard() {
        SingleplayerLeaderboardCtrl.fromMainMenu = false;
        main.showScene(SingleplayerLeaderboardCtrl.class);
    }

    /**
     * Show the leaderboard.
     */
    public void showMainMenu() {
        main.quitSingleplayer();
        main.showScene(MainMenuCtrl.class);
    }

    public void showRestart() {
        Main.gameMode = GameMode.SINGLEPLAYER;
        main.createNewSingleplayerGame();
        main.getSingleplayerGame().jumpToNextQuestion();
    }

}
