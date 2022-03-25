package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.utils.GameMode;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import packets.ResponsePacket;
import packets.ZipRequestPacket;

import java.io.IOException;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MainMenuCtrl extends SceneCtrl {

    @FXML
    private Text username;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public MainMenuCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Gets called when the scene is actually shown to the user
     */
    @OnShowScene
    public void onShowScene() {
        username.setText(Main.USERNAME.equals("") ? "???" : Main.USERNAME);
    }

    /**
     * Test method for showcasing sending a zip to the server
     * @throws IOException
     */
    public void sendZip() throws IOException {
        Path path = Paths.get("client/src/main/resources/20220311-oopp-activity-bank.zip");
        byte[] bytes = Files.readAllBytes(path);
        System.out.println(bytes.length);
        server.postRequest("zip/",new ZipRequestPacket(bytes),ResponsePacket.class);
    }

    /**
     * Show the leaderboard.
     */
    public void showHomeLeaderboard() {
        main.showScene(SingleplayerLeaderboardCtrl.class);
    }

    /**
     * Show the help screen.
     */
    public void showHelp() {
        main.showScene(HelpCtrl.class);
    }

    /**
     * Show the Logic screen.
     */
    public void showLogin() {
        main.showScene(LoginCtrl.class);
    }


    /**
     * Starts singleplayer mode and loads the first question with the screen
     */
    public void showSingleplayer(){
        Main.gameMode = GameMode.SINGLEPLAYER;
        Main.currentQuestionCount = 0;
        Main.questions = new LinkedList<>();
        Main.openQuestions = new LinkedList<>();
        Main.questionHistory = new LinkedList<>();
        Main.scoreTotal = 0;
        main.getQuestions();
        main.jumpToNextQuestion();
    }


    /**
     * Leads to the Multiplayer Lobby
     */
    public void showMultiplayer(){
        main.showScene(LobbyCtrl.class);
    }
}
