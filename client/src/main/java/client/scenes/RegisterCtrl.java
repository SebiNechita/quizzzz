package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.utils.LoggerUtil;
import javafx.fxml.FXML;
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

    @Inject
    public RegisterCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    public void showLogin(){
        main.showScene(LoginCtrl.class);
    }

    public void registerButtonClicked(){
        if (!password.getText().equals(confirmPassword.getText())){
            LoggerUtil.warn("Passwords are not matching.");
        }
        else{
            server.register(userName.getText(), password.getText());
        }
    }
}
