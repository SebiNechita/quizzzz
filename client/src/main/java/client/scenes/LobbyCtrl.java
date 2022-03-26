package client.scenes;

import client.Main;
import client.game.MultiplayerGame;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.utils.Emote;
import commons.utils.GameMode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static client.utils.EmoteUtility.emoteHoverAnim;
import static client.utils.EmoteUtility.emoteUsed;

public class LobbyCtrl extends SceneCtrl {
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
    @FXML
    protected HBox emoteContainer;
    @FXML
    private Button buttonStart;
    @FXML
    private ScrollPane scrollPane;

    private Boolean ready;
    //private MultiplayerGame multiGame;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public LobbyCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
       // this.multiGame = mainCtrl.getMultiplayerGame();

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
        Main.gameMode = GameMode.MULTIPLAYER;
        main.createNewMultiplayerGame();

        buttonStart.setVisible(false);
        buttonReady.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        ready = false;

        // Ensures that the chat text scrolls automatically
        scrollPane.vvalueProperty().bind(chattextflow.heightProperty());

        chattext = new Text("Quizzzz: Welcome to the game, " + Main.USERNAME + "! " + "\n");
        chattext.setFont(Font.font("Comic Sans MS", 30));
        chattext.setFill(Color.BLUE);
        chattextflow.getChildren().add(chattext);

        // the order of below methods matters!
        main.getMultiplayerGame().join(Main.USERNAME);
        main.getMultiplayerGame().startPingThread(Main.USERNAME);
        main.getMultiplayerGame().getLobbyUpdate();

        enableListeners();
    }

    public void enableListeners() {
        for (Node node : emoteContainer.getChildren()) {
            ImageView emote = (ImageView) node;

            emote.setOnMouseEntered(event -> {
                emoteHoverAnim(emote, false).play();
            });

            emote.setOnMouseExited(event -> {
                emoteHoverAnim(emote, true).play();
            });

            emote.setOnMouseClicked(event -> {
                emoteUsed(Emote.valueOf(emote.getId()));
                showEmoji(Emote.valueOf(emote.getId()));
            });
        }
    }

    /**
     * show message when other player joins the game
     *
     * @param player who joined the lobby
     */
    public void showJoinMsg(String player) {
        chattext = new Text(player + " entered the lobby" + "\n");
        chattext.setFont(Font.font("Comic Sans MS", 30));
        chattextflow.getChildren().add(chattext);
    }

    /**
     * update the player list
     *
     * @param playerList updated player list sent by server
     */
    public void updatePlayerList(Map<String, String> playerList) {
        // clear all existing players
        textflow.getChildren().clear();
        for (String player : playerList.keySet()) {
            Text text = new Text(player + "\n");
            if (playerList.get(player).equals("false")) {
                text.setFill(Color.RED);
            } else {
                text.setFill(Color.GREEN);
            }
            text.setFont(Font.font("Comic Sans MS", 27));
            textflow.getChildren().add(text);
        }
    }

    /**
     * show the Start button
     */
    public void showStartButton() {
        buttonStart.setVisible(true);
    }

    /**
     * hide the start button
     */
    public void hideStartButton() {
        buttonStart.setVisible(false);
    }

    /**
     * method to be invoked whe start is clicked
     */
    @FXML
    public void onStartClicked() {
        // to be filled
//        Main.gameMode = GameMode.MULTIPLAYER;
//        //main.createNewMultiplayerGame();
        main.getMultiplayerGame().jumpToNextQuestion();
    }

    /**
     * Adds the emote to the chatroom
     *
     * @param emote emote to be added
     */
    public void showEmoji(Emote emote) {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("@../../img/emojis/" + emote.toString().toLowerCase() + ".png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        main.getMultiplayerGame().sendEmote(Main.USERNAME, emote.toString().toLowerCase());
    }

    /**
     * update emoji sent by other player
     *
     * @param from     sender of the emote
     * @param emoteStr the emote name
     */
    public void updateEmoji(String from, String emoteStr) {
        Text text = new Text(from + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("@../../img/emojis/" + emoteStr + ".png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * when back button is clicked. Should stop pinging thread and the long polling thread.
     */
    public void showHome() {
        chattext = new Text(Main.USERNAME + " has left the lobby!" + "\n");
        chattext.setFill(Color.RED);
        chattext.setFont(Font.font("Comic Sans MS", 30));
        textflow.getChildren().remove(playertext);
        chattextflow.getChildren().add(chattext);
        main.getMultiplayerGame().stopPingThread();
        main.getMultiplayerGame().stopLobbyUpdate();
        main.showScene(MainMenuCtrl.class);
    }

    /**
     * When pressed it makes the player from the session ready or not ready.
     */
    public void makeButtonReady() {
        if (!ready) {
            buttonReady.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            ready = true;

        } else {
            buttonReady.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            ready = false;
        }

        // send ready message to server
        main.getMultiplayerGame().sendReadyMsg(Main.USERNAME, ready);
    }

    /**
     * update ready state message in the chatbox
     *
     * @param player  who changed ready state
     * @param isReady is ready or not
     */
    public void updateReady(String player, String isReady) {
        if (isReady.equals("true")) {
            chattext = new Text(player + " is ready" + "\n");
        } else {
            chattext = new Text(player + " canceled ready" + "\n");
        }

        chattext.setFont(Font.font("Comic Sans MS", 30));
        chattextflow.getChildren().add(chattext);
    }

}
