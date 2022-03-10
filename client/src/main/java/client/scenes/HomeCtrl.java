package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.utils.LoggerUtil;
import javafx.fxml.FXML;
import packets.GeneralResponsePacket;
import packets.UsernameAvailableRequestPacket;

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

    }

    @FXML
    public void pingButtonClicked() {
        LoggerUtil.raw(server.postRequest("/api/user/available", new UsernameAvailableRequestPacket("Geoff"), GeneralResponsePacket.class));
    }

    /**
     * Show the leaderboard.
     */
    public void showHomeLeaderboard(){
        main.showScene(HomeLeaderboardCtrl.class);
    }
}
