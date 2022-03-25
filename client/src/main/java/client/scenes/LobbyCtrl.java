package client.scenes;

import client.Main;
import client.game.MultiplayerGame;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
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
        chattextflow.getChildren().add(chattext);

        multiGame.join(Main.USERNAME);
        multiGame.repeatPing(Main.USERNAME);
        multiGame.getLobbyUpdate();


    }

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

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji1(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/heart_eyes.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * add the Joy emoji into the chat
     */
    public void showEmoji2() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/joy.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "2");
    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji2(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/joy.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * add the Nerd emoji into the chat
     */
    public void showEmoji3() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/nerd.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "3");
    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji3(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/nerd.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * add the Smirking emoji into the chat
     */
    public void showEmoji4() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/smirking.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "4");
    }

    /**
     * update emoji sent by other player
     *
     * @param sender
     */
    public void updateEmoji4(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/smirking.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * add the Angry emoji into the chat
     */
    public void showEmoji5() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/annoyed.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "4");
    }

    /**
     * update emoji sent by other player
     *
     * @param sender
     */
    public void updateEmoji5(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/annoyed.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * add the Sunglasses emoji into the chat
     */
    public void showEmoji6() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/sunglasses.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "6");
    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji6(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/sunglasses.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }


    /**
     * add the Devil emoji into the chat
     */
    public void showEmoji7() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/devil.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "7");
    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji7(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/devil.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * add the Sad emoji into the chat
     */
    public void showEmoji8() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/sad.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "8");
    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji8(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/sad.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * add the Big Heart emoji into the chat
     */
    public void showEmoji9() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/love.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "9");
    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji9(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/love.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * add the Trophy emoji into the chat
     */
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
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }


    /**
     * add the Thumbs up emoji into the chat
     */
    public void showEmoji11() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/thumbup.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "11");
    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji11(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/thumbup.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }



    /**
     * add the Exhaler emoji into the chat
     */
    public void showEmoji12() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/exhaling.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "12");

    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji12(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/exhaling.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }


    /**
     * add the Crying emoji into the chat
     */
    public void showEmoji13() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/crying.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "13");
    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji13(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/crying.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
    }

    /**
     * add the Thinker emoji into the chat
     */
    public void showEmoji14() {
        Text text = new Text(Main.USERNAME + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/thinking.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
        multiGame.sendEmote(Main.USERNAME, "14");
    }

    /**
     * update emoji sent by other player
     * @param sender
     */
    public void updateEmoji14(String sender) {
        Text text = new Text(sender + ": ");
        text.setFont(Font.font("Comic Sans MS", 30));
        Text text2 = new Text("\n");
        ImageView iv = new ImageView("img/emojis/thinking.png");
        iv.setFitHeight(40);
        iv.setFitWidth(40);
        chattextflow.getChildren().addAll(text, iv, text2);
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
    public void makeButtonReady() {
        if (ready == false) {
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
