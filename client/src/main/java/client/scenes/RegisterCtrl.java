package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterCtrl extends SceneCtrl{
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private TextField confirmPassword;

    @FXML
    private Label errorLabel;

    @Inject
    public RegisterCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * Shows the login screen.
     */
    public void showLogin(){
        main.showScene(LoginCtrl.class);
    }

    /**
     * Checks if the registration form is filled with correct values and if so, sends a request.
     */
    public void registerButtonClicked(){
        if (!password.getText().equals(confirmPassword.getText())){
            errorLabel.setText("Passwords are not matching.");
        }
        else if (password.getText().isBlank() || userName.getText().isBlank()){
            //the username or password is empty or only consists of whitespaces.
            errorLabel.setText("Password or username is empty.");
        }
        else{
            //the username and password are sent without any leading or trailing whitespaces.
            //If successful, returns to login.
            //the logger is used by the register method.
            if (server.register(userName.getText().trim(), password.getText().trim())){
                showLogin();
            }
        }
    }
}
