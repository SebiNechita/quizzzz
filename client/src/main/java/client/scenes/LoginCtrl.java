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
    public static void login() {
        String result = server.getToken(userName.getText(), password.getText());
        //the return string is null if the login is unsuccessful.
        if (result != null) {
            Main.TOKEN = result;
            //if the result string is not empty this means that userName.getText() is valid
            Main.USERNAME = userName.getText();
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

    /**
     * Shows the Connection scene.
     */
    public void showConnection(){
        main.showScene(ConnectionCtrl.class);
    }
}
