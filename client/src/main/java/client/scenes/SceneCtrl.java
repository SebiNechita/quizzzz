package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class SceneCtrl implements Initializable {
    protected final MainCtrl main;
    protected final ServerUtils server;

    @Inject
    public SceneCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.main = mainCtrl;
        this.server = serverUtils;
    }

    public abstract void initialize(URL location, ResourceBundle resources);
}
