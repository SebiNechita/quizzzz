package client.scenes;

import client.utils.OnShowScene;
import client.utils.ServerUtils;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionCtrl extends SceneCtrl {

    @FXML
    private TextField url;

    @FXML
    private Text message;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public ConnectionCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
        // set the default url to make testing easier
        url.setText("https://localhost:8080");
    }

    /**
     * This method is run when Connection scene is displayed
     */
    @OnShowScene
    public void OnShowScene() {
        url.requestFocus();
        url.positionCaret(url.getText().length());
        addChangeListener();

    }

    /**
     * Event handler for when 'Connect' button is clicked
     */
    public void connectClicked() {
        playURLAnimation();
        String urlStr = url.getText();
        // test if the url is valid
//        boolean res = server.testConnection(urlStr);
//
//        if (res) {
//            main.showScene(LoginCtrl.class);
//            url.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50");
//            message.setText("");
//        } else {
//            message.setText("Cannot connect to this server");
//            url.setStyle("-fx-background-color: #fc6363; -fx-background-radius: 50");
//            shake(url).play();
//        }
    }

    /**
     * add change event listener to the URL TextField
     */
    public void addChangeListener() {
        ChangeListener<String> listener = ((observable, oldValue, newValue) -> {
            boolean res = server.isValidURL(newValue);
            if (res) {
                message.setText("");
            } else {
                message.setText("This URL is invalid");
            }
            url.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50");
        });
        url.textProperty().addListener(listener);
    }

    public void playURLAnimation() {
        DoubleProperty startFade = new SimpleDoubleProperty(0);
        DoubleProperty endFade = new SimpleDoubleProperty(0);
        url.styleProperty().bind(Bindings.createStringBinding(() -> String.format(
                "-fx-background-color: linear-gradient(to right, #7FFFD4, #F0FFFF %f%%, #7FFFD4 );",
                startFade.get()*100
        ), startFade, endFade));


        //HBox root = new HBox(2, label, fadingLabel);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(startFade, 0)),
                new KeyFrame(Duration.seconds(1.0/2.0), new KeyValue(startFade, 0.5)),
                //new KeyFrame(Duration.seconds(2.0/3.0), new KeyValue(startFade, 0.6)),
                new KeyFrame(Duration.seconds(1), new KeyValue(startFade, 1)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * returns a shaking Animation
     *
     * @param node the node to apply this animation
     * @return shaking Animation.
     */
    protected Animation shake(TextField node) {

        TranslateTransition transition = new TranslateTransition(Duration.millis(50), node);
        transition.setFromX(0f);
        transition.setByX(10f);
        transition.setCycleCount(2);
        transition.setAutoReverse(true);

        return transition;
    }

}
