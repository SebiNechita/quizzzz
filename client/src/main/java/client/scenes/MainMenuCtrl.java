package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.net.URL;
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
     * Starts singleplayer mode and sets current question to 0
     */
    public void showSingleplayer(){
        main.getQuestions();
        Main.currentQuestion = 0;
        main.showScene(GameMultiChoiceCtrl.class);
    }
}
