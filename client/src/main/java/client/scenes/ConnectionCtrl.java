package client.scenes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.LeaderboardEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.text.Text;

public  class ConnectionCtrl extends SceneCtrl {

    @Inject
    public ConnectionCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    @FXML
    private TextField url;

    @FXML
    private Text message;

    /**
     * initialize scene, set the default url to localhost:8080
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set the default url to make testing easier
        url.setText("https://localhost:8080");
    }

    /**
     * event handler when 'Connect' button is clicked
     * @param actionEvent
     */
    public void connectClicked(ActionEvent actionEvent) {
        String urlStr = url.getText();
        // test if the url is valid
        boolean res = server.testConnection(urlStr);

        if(res == true) {
            // should jump to 'login' page, but we don't have one yet. So I set it to Home.
            main.showScene(HomeCtrl.class);
        } else {
            message.setText("THAT URL IS INVALID");
            url.setStyle("-fx-background-color: #fc6363");
        }
    }
}
