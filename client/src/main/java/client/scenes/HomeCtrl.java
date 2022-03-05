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
        Main.TOKEN = server.getToken("Geof", "password");
        LoggerUtil.raw(server.pingServer());
    }

    @FXML
    public void pingButtonClicked() {
        LoggerUtil.raw(server.pingServer());
    }

    /**
     * Show the leaderboard.
     */
    public void showHomeLeaderboard(){
        main.showScene(HomeLeaderboardCtrl.class);
    }
}
