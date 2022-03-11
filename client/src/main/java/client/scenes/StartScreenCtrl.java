package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.utils.LoggerUtil;
import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenCtrl extends SceneCtrl {

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    @Inject
    public StartScreenCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
        Main.TOKEN = server.getToken("Geof", "password");
        LoggerUtil.raw(server.pingServer());
    }

//    @FXML
//    public void pingButtonClicked() {
//        LoggerUtil.raw(server.pingServer());
//    }

    /**
     * Show the leaderboard.
     */
    public void showHomeLeaderboard(){
        main.showScene(HomeLeaderboardCtrl.class);
    }

    /**
     * Show the help screen.
     */
    public void showHelp(){
        main.showScene(HelpCtrl.class);
    }
}
