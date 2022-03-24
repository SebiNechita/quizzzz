package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelCtrl extends SceneCtrl {
    @FXML
    private Text noOfActivities;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public AdminPanelCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Show the home screen.
     */
    public void showAddActivity() {
        main.showScene(AddActivityCtrl.class);
    }

//    /**
//     * Show the home screen.
//     */
//    public void showEditActivity() {
//        main.showScene(EditActivityCtrl.class);
//    }

    /**
     //     * Show the home screen.
     //     */
    public void showMainMenu() {
        main.showScene(MainMenuCtrl.class);
    }

    /**
     * Gets called when the scene is actually shown to the user
     */
    @OnShowScene
    public void onShowScene() {
        Main.noOfActivities = server.getActivities().size();
        noOfActivities.setText(Main.noOfActivities == 0 ? "X" : Integer.toString(Main.noOfActivities));
    }
}
