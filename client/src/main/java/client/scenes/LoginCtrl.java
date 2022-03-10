package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginCtrl extends SceneCtrl{
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private Label errorLabel;

    @Inject
    public LoginCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * Logs in and redirects to the Home screen if the login credentials are valid.
     */
    public void login(){
        String result = server.getToken(userName.getText(), password.getText());
        //the return string is "" if the login is unsuccessful.
        if (!result.isEmpty()){
            Main.TOKEN = result;
            main.showScene(HomeCtrl.class);
        }
        else{
            errorLabel.setText("Could not log in.");
        }
    }

    /**
     * Shows the Registration scene.
     */
    public void showRegister(){
        main.showScene(RegisterCtrl.class);
    }
}
