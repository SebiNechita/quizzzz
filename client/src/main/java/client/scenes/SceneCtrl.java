package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class SceneCtrl implements Initializable {
    
    protected final MainCtrl main;
    protected final ServerUtils server;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public SceneCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.main = mainCtrl;
        this.server = serverUtils;
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
    public abstract void initialize(URL location, ResourceBundle resources);
}
