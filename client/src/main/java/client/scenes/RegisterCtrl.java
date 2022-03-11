package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

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
    private Text error;

    @FXML
    private Scene scene;

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
     * DUPLICATE FROM LoginCtrl!!!!!
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
            error.setText("Could not log in.");
        }
    }

    /**
     * Checks if the registration form is filled with correct values and if so, sends a request.
     */
    public void registerButtonClicked(){
        if (!password.getText().equals(confirmPassword.getText())){
            error.setText("Passwords are not matching.");
        }
        else if (password.getText().isBlank() || userName.getText().isBlank()){
            //the username or password is empty or only consists of whitespaces.
            error.setText("Password or username is empty.");
        }
        else{
            //the username and password are sent without any leading or trailing whitespaces.
            //If successful, logs the newly created user in.
            //the logger is used by the register method.
            if (server.register(userName.getText().trim(), password.getText().trim())){
                login();
            }
        }
    }

    /**
     * Controller for keypress events on Scene.
     * @param e the keypress event
     */
    public void onKeyPressed(KeyEvent e) {
        //treat Enter as clicking register button
        if (e.getCode() == KeyCode.ENTER) {
            registerButtonClicked();
        }
    }
}
