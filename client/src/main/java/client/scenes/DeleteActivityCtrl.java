package client.scenes;

import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.utils.HttpStatus;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import packets.ActivityRequestPacket;
import packets.GeneralResponsePacket;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteActivityCtrl extends SceneCtrl {
    @FXML
    private TextField id;

    @FXML
    private Text error;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public DeleteActivityCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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

    @OnShowScene
    public void onShowScene() {
        id.requestFocus();
        id.clear();
        id.setStyle("-fx-background-color: #fff; -fx-background-radius: 50");
        error.setText("");
    }

    /**
     * Show the admin panel screen.
     */
    public void showAdminPanel() {
        main.showScene(AdminPanelCtrl.class);
    }

    /**
     * Method for clicking the delete activity button
     */
    public void clickDelete() {
        if (id.getText() == null || id.getText().equals("")) {
            id.setStyle("-fx-background-color: #fc6363; -fx-background-radius: 50");
            error.setText("Field is mandatory!");
        } else {
            String iD = id.getText();

            ActivityRequestPacket packet = new ActivityRequestPacket(iD);

            GeneralResponsePacket response = server.postRequest("api/activities/delete", packet, GeneralResponsePacket.class);
            if (response.getResponseStatus().equals(HttpStatus.NotFound)) {
                id.setStyle("-fx-background-color: #fc6363; -fx-background-radius: 50");
                error.setText("No activity with id: " + id.getText());
            } else {
                main.showScene(AdminPanelCtrl.class);
            }

        }
    }
}
