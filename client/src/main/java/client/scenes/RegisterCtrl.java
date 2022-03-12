package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.utils.HttpStatus;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import packets.ResponsePacket;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterCtrl extends SceneCtrl {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //treat Enter as clicking register button
        userName.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                registerButtonClicked();
            }
        });
        password.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                registerButtonClicked();
            }
        });
        confirmPassword.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                registerButtonClicked();
            }
        });
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
    public void showLogin() {
        clearFields();
        main.showScene(LoginCtrl.class);
    }

    /**
     * DUPLICATE FROM LoginCtrl!!!!!
     * Logs in and redirects to the Home screen if the login credentials are valid.
     */
    public void login() {
        String result = server.getToken(userName.getText(), password.getText());
        //the return string is "" if the login is unsuccessful.
        if (!result.isEmpty()) {
            Main.TOKEN = result;
            main.showScene(HomeCtrl.class);
        } else {
            error.setText("Could not log in.");
        }
    }

    /**
     * Checks if the registration form is filled with correct values and if so, sends a request.
     */
    public void registerButtonClicked() {
        if (!password.getText().equals(confirmPassword.getText())) {
            error.setText("Passwords are not matching.");
        } else if (password.getText().isBlank() || userName.getText().isBlank()) {
            //the username or password is empty or only consists of whitespaces.
            error.setText("Password or username is empty.");
        } else {
            //the username and password are sent without any leading or trailing whitespaces.
            //If successful, logs the newly created user in.
            ResponsePacket response = server.register(userName.getText().trim(), password.getText().trim());
            if (response.getCode() == HttpStatus.Conflict.getCode()) {
                error.setText("User exists");
            } else if (response.getCode() == HttpStatus.Created.getCode()) {
                clearFields();
                login();
            }
        }
    }

    /**
     * Clears input fields and error messages in this scene.
     */
    public void clearFields() {
        userName.clear();
        password.clear();
        confirmPassword.clear();
        error.setText("");
    }
}
