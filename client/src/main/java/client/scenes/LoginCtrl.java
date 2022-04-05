package client.scenes;

import client.Main;
import client.utils.ColorPresets;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginCtrl extends SceneCtrl {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //treat Enter as clicking login button
        userName.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onLoginButtonPressed();
            }
            if (e.getCode() == KeyCode.DOWN) {
                password.requestFocus();
            }
        });
        password.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onLoginButtonPressed();
            }
            if (e.getCode() == KeyCode.UP) {
                userName.requestFocus();
            }
        });
    }

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private Text error;

    public LoginCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * Logs in the user with the account data
     *
     * @param username        - the username of the user
     * @param passwordEntered - the password of the user
     */
    public void login(String username, String passwordEntered) {
        String result = server.getToken(username, passwordEntered);
        //the return string is null if the login is unsuccessful.
        if (result != null) {
            Main.TOKEN = result;
            //if the result string is not empty this means that userName.getText() is valid
            Main.USERNAME = username;
            userName.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.white) + "; -fx-background-radius: 50");
            password.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.white) + "; -fx-background-radius: 50");
            error.setText("");
            main.showScene(MainMenuCtrl.class);
            userName.setText("");
            password.setText("");
        } else {
            Animation userNameShake = shake(userName);
            Animation passwordShake = shake(password);
            userNameShake.play();
            passwordShake.play();

            userName.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.soft_red) + "; -fx-background-radius: 50");
            password.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.soft_red) + "; -fx-background-radius: 50");
            error.setText("Could not log in");
        }
    }

    /**
     * This method is run when the Login scene is displayed
     */
    @OnShowScene
    public void OnShowScene() {
        userName.requestFocus();
    }

    /**
     * Shows the Registration scene.
     */
    public void showRegister() {
        main.showScene(RegisterCtrl.class);
    }

    public void onLoginButtonPressed() {
        login(userName.getText(), password.getText());
    }

    /**
     * Event controller for clicking on the "Back" button
     */
    public void showConnection() {
        main.showScene(ConnectionCtrl.class);
    }

    /**
     * Create and returns a shaking Animation for a given node
     *
     * @param node to node to apply shaking animation
     * @return shaking Animation
     */
    protected Animation shake(TextField node) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), node);
        transition.setFromX(0f);
        transition.setByX(10f);
        transition.setCycleCount(2);
        transition.setAutoReverse(true);

        return transition;
    }
}
