package client.scenes;

import client.utils.OnShowScene;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;

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
        // Ensures that the user doesn't enter whitespaces in the url
        TextFormatter<?> formatter = new TextFormatter<>((TextFormatter.Change change) -> {
            String text = change.getText();

            // if text was added, fix the text to fit the requirements
            if (!text.isEmpty()) {
                String newText = text.replaceAll("\\s|`|~|\\+|\\[|]|,|\"|;|}|\\{|'", "").toLowerCase();

                int carretPos = change.getCaretPosition() - text.length() + newText.length();
                change.setText(newText);

                // fix carret position based on difference in originally added text and fixed text
                change.selectRange(carretPos, carretPos);
            }
            return change;
        });
        url.setTextFormatter(formatter);
    }

    /**
     * Event handler for when 'Connect' button is clicked
     */
    public void connectClicked() {
        String urlStr = url.getText();
        // test if the url is valid
        boolean res = server.testConnection(urlStr);

        if (res) {
            main.showScene(LoginCtrl.class);
            url.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50");
            message.setText("");
        } else {
            message.setText("This URL is invalid");
            url.setStyle("-fx-background-color: #fc6363; -fx-background-radius: 50");
        }
    }
}
