package client.scenes;

import client.utils.ServerUtils;
import commons.utils.LoggerUtil;
import javafx.fxml.FXML;
import packets.GeneralResponsePacket;
import packets.UsernameAvailableRequestPacket;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeCtrl extends SceneCtrl {

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    @Inject
    public HomeCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Server communication
     */
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

    /**
     * Show the home screen.
     */
    public void showHelp(){
        main.showScene(HelpCtrl.class);
    }
}
