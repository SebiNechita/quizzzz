package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.utils.LoggerUtil;
import javafx.fxml.FXML;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeCtrl extends SceneCtrl {

    @Inject
    public HomeCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.TOKEN = server.getToken("Geoff", "password");
        LoggerUtil.log(server.pingServer());
    }

    @FXML
    public void pingButtonClicked() {
        LoggerUtil.raw(server.pingServer());
    }
}
