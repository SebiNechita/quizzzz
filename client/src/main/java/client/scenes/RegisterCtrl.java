package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.utils.HttpStatus;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
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
            if (e.getCode() == KeyCode.DOWN) {
                password.requestFocus();
            }
        });
        password.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                registerButtonClicked();
            }
            if (e.getCode() == KeyCode.UP) {
                userName.requestFocus();
            }
            if (e.getCode() == KeyCode.DOWN) {
                confirmPassword.requestFocus();
            }
        });
        confirmPassword.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                registerButtonClicked();
            }
            if (e.getCode() == KeyCode.UP) {
                password.requestFocus();
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
    private Text errorText;

    @FXML
    private Scene scene;

    @Inject
    public RegisterCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * This method is run when Register scene is displayed
     */
    @OnShowScene
    public void OnShowScene() {
        userName.requestFocus();
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
        //the return string is null if the login is unsuccessful.
        if (result != null) {
            Main.TOKEN = result;
            main.showScene(MainMenuCtrl.class);
        } else {
            errorText.setText("Could not log in.");
        }
    }

    /**
     * Checks if the registration form is filled with correct values and if so, sends a request.
     */
    public void registerButtonClicked() {
        if (password.getText().isBlank() || userName.getText().isBlank()) {
            errorText.setText("Password or username is empty.");
            userName.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(50), Insets.EMPTY)));
            password.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(50), Insets.EMPTY)));
        } else if (!password.getText().equals(confirmPassword.getText())) {
            //the username or password is empty or only consists of whitespaces.
            errorText.setText("Passwords are not matching.");
            password.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(50), Insets.EMPTY)));
            confirmPassword.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(50), Insets.EMPTY)));
        } else {
            //the username and password are sent without any leading or trailing whitespaces.
            //If successful, logs the newly created user in.
            ResponsePacket response = server.register(userName.getText().trim(), password.getText().trim());
            if (response.getCode() == HttpStatus.Conflict.getCode()) {
                userName.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(50), Insets.EMPTY)));
                errorText.setText("User exists");
            } else if (response.getCode() == HttpStatus.Created.getCode()) {
                LoginCtrl login = main.getCtrl(LoginCtrl.class);
                login.login(userName.getText(), password.getText());
                clearFields();
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
        userName.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(50), Insets.EMPTY)));
        password.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(50), Insets.EMPTY)));
        confirmPassword.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(50), Insets.EMPTY)));
        errorText.setText("");
    }
}
