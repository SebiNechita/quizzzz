package client.scenes;

import client.Main;
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
        });
        password.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onLoginButtonPressed();
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
     * @param userName - the username of the user
     * @param password - the password of the user
     */
    public void login(String userName, String password) {
        String result = server.getToken(userName, password);
        //the return string is null if the login is unsuccessful.
        if (result != null) {
            Main.TOKEN = result;
            //if the result string is not empty this means that userName.getText() is valid
            Main.USERNAME = userName;
            main.showScene(MainMenuCtrl.class);
        }
        else{
            error.setText("Could not log in.");
            shake(this.userName).playFromStart();
            shake(this.password).playFromStart();
        }
    }

    /**
     * Shows the Registration scene.
     */
    public void showRegister() {
        main.showScene(RegisterCtrl.class);
    }

    public void onLoginButtonPressed(){
        login(userName.getText(), password.getText());
    }

    /**
     * Event controller for clicking on the "Back" button
     */
    public void showConnection() {
        main.showScene(ConnectionCtrl.class);
    }

    /**
     * returns a shaking Animation
     * @param node
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
