package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
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

    @Inject
    public RegisterCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    public void showLogin(){
        main.showScene(LoginCtrl.class);
    }

    public void registerButtonClicked(){
        server.register(userName.getText(), password.getText());
    }
}
