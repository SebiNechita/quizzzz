package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyCtrl extends  SceneCtrl{
    @FXML
    private Button buttonReady;
    @FXML
    private TextFlow textflow;
    @FXML
    private TextFlow chattextflow;
    @FXML
    private Text playertext;
    @FXML
    private Text chattext;
    private Boolean ready;
    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public LobbyCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
        buttonReady.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        ready = false;
        playertext = new Text(Main.USERNAME + "\n");
        playertext.setFill(Color.RED);
        playertext.setFont(Font.font("Comic Sans MS", 27));
        textflow.getChildren().add(playertext);
        chattext = new Text("Quizzzz: Welcome to the game, " + Main.USERNAME + "\n");
        chattext.setFont(Font.font("Comic Sans MS", 30));
        chattextflow.getChildren().add(chattext);
    }



    /**
     * Show the home screen.
     */
    public void showHome() {
        chattext = new Text("Quizzzz: " + Main.USERNAME + " has left the lobby!" + "\n");
        chattext.setFont(Font.font("Comic Sans MS", 30));
        textflow.getChildren().remove(playertext);
        chattextflow.getChildren().add(chattext);
        main.showScene(MainMenuCtrl.class);
    }

    /**
     * When pressed it makes the player from the session ready or not ready.
     */
    public void makeButtonReady(){
        if(ready == false){
            buttonReady.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            playertext.setFill(Color.GREEN);
            ready = true;
        }
        else{
            buttonReady.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            playertext.setFill(Color.RED);
            ready = false;
        }
    }

}
