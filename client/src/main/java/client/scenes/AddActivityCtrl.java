package client.scenes;

import client.utils.ServerUtils;
import commons.utils.LoggerUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
import packets.ImageResponsePacket;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.UUID;

public class AddActivityCtrl extends SceneCtrl {
    @FXML
    private TextField description;

    @FXML
    private TextField consumption;

    @FXML
    private TextField source;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button uploadAnImage;

    @FXML
    private Text error;

    public String filePath = null;

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
        fileChooser.setTitle("Open File Dialog");
        Stage stage = (Stage) anchorPane.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        String path = "";

        String mimetype = Files.probeContentType(file.toPath());

        if (file != null && mimetype.split("/")[0].equals("image")) {
//            Desktop desktop = Desktop.getDesktop();
//            desktop.open(file);

            path = file.getAbsolutePath();
            return path;
        }

        return null;
    }

    /**
     * Requests an image from the user and returns this image
     *
     * @param actionEvent Action event
     * @return The image file
     * @throws IOException If the file is not found
     */
    @FXML
    public File singleFileChooser(javafx.event.ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File Dialog");
        Stage stage = (Stage) anchorPane.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        String path = "";

        String mimetype = Files.probeContentType(file.toPath());

        if (file != null && mimetype.split("/")[0].equals("image")) {
//            Desktop desktop = Desktop.getDesktop();
//            desktop.open(file);

            filePath = file.getAbsolutePath();
            // put request, post mapping
            LoggerUtil.infoInline(path);

            return file;
        }

        LoggerUtil.warnInline("This is not an image file!");

        return null;
    }

    /**
     * Returns a file represented as an array of bytes
     *
     * @param file The file to be converted
     * @return An array of bytes
     * @throws IOException If the file is not found
     */
    public byte[] getBytes(File file) throws IOException {
        byte[] buffer = new byte[4096];

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);

        int read;

        while ((read = fileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, read);
        }

        fileInputStream.close();
        byteArrayOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public ImageResponsePacket byteToImage(File file) throws IOException {
        byte[] imageBytes = getBytes(file);

        return new ImageResponsePacket(imageBytes);
    }

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
        limitToNumbers();
    }

    /**
     * Show the admin panel screen.
     */
    public void showAdminPanel() {
        main.showScene(AdminPanelCtrl.class);
    }

    /**
     * get file path from file chooser
     *
     * @param e
     * @throws IOException
     */
    public void clickUploadImage(ActionEvent e) throws IOException {
        if (e.getSource().equals(uploadAnImage)) {
            filePath = singleFilePathChooser(e);
        }
    }

    /**
     * Method for clicking the add activity button
     */
    public void clickAdd() {
        if (description.getText() == null || consumption.getText() == null || source.getText() == null
                || filePath == null) {
            description.setStyle("-fx-background-color: #FF0000FF; -fx-background-radius: 50");
            consumption.setStyle("-fx-background-color: #FF0000FF; -fx-background-radius: 50");
            source.setStyle("-fx-background-color: #FF0000FF; -fx-background-radius: 50");
            error.setText("All fields are mandatory!");
            uploadAnImage.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(40), Insets.EMPTY)));
            //error.setStyle("-fx-background-color: #FF0000FF; -fx-background-radius: 50");
        } else {
            byte[] imageByteArray = null;
            try {
                FileInputStream imageStream = new FileInputStream(filePath);
                imageByteArray = imageStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ActivityRequestPacket packet = new ActivityRequestPacket(
                    UUID.randomUUID().toString(),
                    Long.parseLong(consumption.getText()),
                    source.getText(),
                    description.getText(),
                    imageByteArray
            );
            server.postRequest("api/activities/add", packet, GeneralResponsePacket.class);
            main.showScene(AdminPanelCtrl.class);
        }
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

}
