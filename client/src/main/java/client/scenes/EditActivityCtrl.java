package client.scenes;

import client.utils.ServerUtils;
import commons.questions.Activity;
import commons.utils.HttpStatus;
import commons.utils.LoggerUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import packets.ActivityRequestPacket;
import packets.GeneralResponsePacket;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

/**
 * controller for EditActivity scene
 */
public class EditActivityCtrl extends SceneCtrl {
    @FXML
    private TextField description;

    @FXML
    private TextField consumption;

    @FXML
    private TextField source;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button upload;

    @FXML
    private ImageView imageView;

    @FXML
    private Text error;

    private String filePath = null;

    byte[] imageByteArray = null;

    private Activity item;


    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public EditActivityCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
        description.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DOWN) {
                consumption.requestFocus();
            }
        });
        consumption.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                description.requestFocus();
            }
            if (e.getCode() == KeyCode.DOWN) {
                source.requestFocus();
            }
        });
        source.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                consumption.requestFocus();
            }
        });
    }

    /**
     * populate the text fields with the Activity item
     */
    public void onShowScene() {
        description.requestFocus();
        description.positionCaret(source.getText().length());
        source.setText(item.getSource());
        consumption.setText(Long.toString(item.getConsumption_in_wh()));
        description.setText(item.getTitle());
        limitToNumbers();
    }

    /**
     * make consumption text field only takes numbers
     */
    public void limitToNumbers() {
        consumption.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    consumption.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * pass Activity object to this controller from ListActivityCtrl
     *
     * @param item Activity object
     */
    public void initData(Activity item) {
        this.item = item;
        Image image = server.getImage(item.getImage_path());
        imageView.setImage(image);
    }

    /**
     * display uploaded image and save the image byte array.
     *
     * @param e
     * @throws IOException
     */
    public void clickUploadImage(ActionEvent e) throws IOException {
        // save local file path
        filePath = singleFilePathChooser(e);

        if (filePath == null) {
            LoggerUtil.warnInline("No image was selected!");
        }

        try {
            // get image byte array
            FileInputStream imageStream = new FileInputStream(filePath);
            imageByteArray = imageStream.readAllBytes();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // update image view
        imageView.setImage(new Image(new ByteArrayInputStream(imageByteArray), 240, 143, false, false));
    }

    /**
     * go back to ListActivityCtrl when 'Cancel' is clicked
     *
     * @param actionEvent
     */
    public void onCancel(ActionEvent actionEvent) {
        main.showScene(ListActivityCtrl.class);
    }

    /**
     * post edit Activity request to server
     *
     * @param actionEvent
     */
    public void onOk(ActionEvent actionEvent) {
        if (description.getText() == null || description.getText().equals("") ||
                consumption.getText() == null || consumption.getText().equals("") ||
                source.getText() == null || source.getText().equals("")) {
            description.setStyle("-fx-background-color: #fc6363; -fx-background-radius: 50");
            consumption.setStyle("-fx-background-color: #fc6363; -fx-background-radius: 50");
            source.setStyle("-fx-background-color: #fc6363; -fx-background-radius: 50");
            error.setText("All fields are mandatory!");
            upload.setBackground(new Background(new BackgroundFill(Color.web("#fc6363"), new CornerRadii(40), Insets.EMPTY)));
        } else {

            ActivityRequestPacket packet = new ActivityRequestPacket(
                    item.getId(),
                    Long.parseLong(consumption.getText()),
                    source.getText(),
                    description.getText(),
                    imageByteArray
            );
            GeneralResponsePacket response = server.postRequest("api/activities/edit", packet, GeneralResponsePacket.class);
            if (response.getResponseStatus() == HttpStatus.OK) {
                main.showScene(ListActivityCtrl.class);
            } else {
                error.setText("edit operation failed.");
            }
        }
    }

    /**
     * Requests an image from the user and returns the path to the image
     *
     * @param actionEvent Action event
     * @return A string representing the path to the image
     * @throws IOException If the file is not found
     */
    @FXML
    public String singleFilePathChooser(javafx.event.ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Images Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Open File Dialog");
        Stage stage = (Stage) anchorPane.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        String path = "";

        String mimetype = Files.probeContentType(file.toPath());

        if (file != null && mimetype.split("/")[0].equals("image")) {
            path = file.getAbsolutePath();
            return path;
        }

        return null;
    }
}
