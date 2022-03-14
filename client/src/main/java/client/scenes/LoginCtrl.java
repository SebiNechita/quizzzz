package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginCtrl extends SceneCtrl {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
     * Logs in and redirects to the Home screen if the login credentials are valid.
     */
    public void login(String userName, String password) {
        String result = server.getToken(userName, password);
        //the return string is null if the login is unsuccessful.
        if (result != null) {
            Main.TOKEN = result;
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
}
