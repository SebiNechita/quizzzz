package client.scenes;

import client.utils.ServerUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class AddActivityCtrl extends SceneCtrl {
    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public AddActivityCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
     //     * Show the home screen.
     //     */
    public void showAdminPanel() {
        main.showScene(AdminPanelCtrl.class);
    }
}
