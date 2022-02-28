package client.scenes;

import client.utils.ServerUtils;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class GameCtrl extends SceneCtrl {

    @Inject
    public GameCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
