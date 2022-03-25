package client.scenes;

import client.Main;
import client.game.MultiplayerGame;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.utils.Emote;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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

    private Boolean ready;
    private MultiplayerGame multiGame;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public LobbyCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
        this.multiGame = mainCtrl.getMultiplayerGame();
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
        buttonStart.setVisible(false);
        buttonReady.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        ready = false;

        chattext = new Text("Quizzzz: Welcome to the game, " + Main.USERNAME + "! " + "\n");
        chattext.setFont(Font.font("Comic Sans MS", 30));
        chattext.setFill(Color.BLUE);
        chattextflow.getChildren().add(chattext);

        // the order of bellow methods matters!
        multiGame.join(Main.USERNAME);
        multiGame.startPingThread(Main.USERNAME);
        multiGame.getLobbyUpdate();

        enableListners();
    }

    public void enableListners() {
        for (Node node : emoteContainer.getChildren()) {
            ImageView emote = (ImageView) node;
    /**
     * show message when other player joins the game
     *
     * @param player
     */
    public void showJoinMsg(String player) {
        chattext = new Text(player + " entered the lobby" + "\n");
        chattext.setFont(Font.font("Comic Sans MS", 30));
        chattextflow.getChildren().add(chattext);
    }

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

    public void onStartClicked() {
        // to be filled
    }

    /**
     * add the Hearts Eye emoji into the chat
     */
    public void showEmoji1() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/heart_eyes.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        //send pressed emote to server
        multiGame.sendEmote(Main.USERNAME, "1");
    }

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
     * Addes the emote to the chatroom
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
        multiGame.sendEmote(Main.USERNAME, "6");
    }

    /**
    public void showEmoji10() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/trophy.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "10");
    }
     */
     * add the Trophy emoji into the chat
    /**

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji10(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/trophy.png");
        iv.setFitHeight(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }
        iv.setFitWidth(40);

     * Show the home screen.
     */
    public void showHome() {
        chattext = new Text(Main.USERNAME + " has left the lobby!" + "\n");
        chattext.setFill(Color.RED);
        chattext.setFont(Font.font("Comic Sans MS", 30));
        textflow.getChildren().remove(playertext);
        chattextflow.getChildren().add(chattext);
        multiGame.stopPingThread();
        multiGame.stopLobbyUpdate();
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
        multiGame.sendReadyMsg(Main.USERNAME, ready);
    }

    public void updateReady(String player, String isReady) {
        if (isReady.equals("true")) {
            chattext = new Text(player + " is ready" + "\n");
        } else {
            chattext = new Text(player + " is not ready" + "\n");
        }

        chattext.setFont(Font.font("Comic Sans MS", 30));
        chattextflow.getChildren().add(chattext);
    }

}
