package client.scenes;

import client.utils.AnimationUtil;
import client.utils.ColorPresets;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionCtrl extends SceneCtrl {

    @FXML
    private TextField url;

    @FXML
    private Text message;
    @FXML
    private Button connectButton;

    private Animation connectingAnimation;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public ConnectionCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
        // set the default url to make testing easier
        url.setText("https://localhost:8080");
    }

    /**
     * This method is run when Connection scene is displayed
     */
    @OnShowScene
    public void OnShowScene() {
        url.requestFocus();
        url.positionCaret(url.getText().length());
        addChangeListener();

    }

    /**
     * Event handler for when 'Connect' button is clicked.
     * Should set up and play the connecting animation(for this demo)
     */
    public void connectClicked() {
        connectingAnimation = AnimationUtil.connectingAnimation(url);
        connectingAnimation.play();
        connect();
    }

    /**
     * try to connect to the given URL
     */
    public void connect() {
        new Thread(() -> {
            String res = server.testConnection(url.getText());

            connectingAnimation.stop();
            url.styleProperty().unbind();
            url.getStyleClass().clear();

            Platform.runLater(() -> {
                switch (res) {
                    case "valid" -> {
                        main.showScene(LoginCtrl.class);
                        url.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.white) + "; -fx-background-radius: 50");
                        message.setText("");
                    }
                    case "URL" -> {
                        message.setText("The URL is invalid");
                        url.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.soft_red) + "; -fx-background-radius: 50");
                        AnimationUtil.shake(url).play();
                        connectButton.setDisable(true);
                    }
                    case "Server" -> {
                        message.setText("Cannot connect to this server");
                        url.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.soft_red) + "; -fx-background-radius: 50");
                        AnimationUtil.shake(url).play();
                    }
                    default -> {
                        message.setText("This is not a game server");
                        url.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.soft_red) + "; -fx-background-radius: 50");
                        AnimationUtil.shake(url).play();
                    }
                }
            });
        }).start();
    }

    /**
     * add change event listener to the URL TextField
     */
    public void addChangeListener() {
        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            boolean res = server.isValidURL(newValue);
            if (res) {
                connectButton.setDisable(false);
                message.setText("");
            } else {
                connectButton.setDisable(true);
                message.setText("This URL is invalid");
            }
            url.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50");
        };
        url.textProperty().addListener(listener);
    }

}
