package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

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

    public void showConnection() {
        main.showScene(ConnectionCtrl.class);
    }
}
