package client.scenes;

import client.utils.ServerUtils;
import commons.utils.LoggerUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import packets.ImageResponsePacket;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.ResourceBundle;

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

        if(file != null && mimetype.split("/")[0].equals("image")) {
//            Desktop desktop = Desktop.getDesktop();
//            desktop.open(file);

            path = file.getAbsolutePath();
            return path;
        }

        return null;
    }

    /**
     * Requests an image from the user and returns this image
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

        if(file != null && mimetype.split("/")[0].equals("image")) {
//            Desktop desktop = Desktop.getDesktop();
//            desktop.open(file);

            path = file.getAbsolutePath();
            // put request, post mapping
            LoggerUtil.infoInline(path);

            return file;
        }

        LoggerUtil.warnInline("This is not an image file!");

        return null;
    }

    /**
     * Returns a file represented as an array of bytes
     * @param file The file to be converted
     * @return An array of bytes
     * @throws IOException If the file is not found
     */
    public byte[] getBytes(File file) throws IOException {
        byte[] buffer = new byte[4096];

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);

        int read;

        while((read = fileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, read);
        }

        fileInputStream.close();
        byteArrayOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Constructs an ImageResponsePacket from an image file
     * @param file The image used
     * @return An ImageResponsePacket
     * @throws IOException If the file is not found
     */
    public ImageResponsePacket byteToImage(File file) throws IOException {
        byte[] imageBytes = getBytes(file);

        return new ImageResponsePacket(imageBytes);
    }

    /**
     * Writes data from an array of bytes to a new file
     * @param data An array of bytes
     * @param destination The file where the array of bytes is going to be stored
     */
    public void toFile(byte[] data, File destination) {
        try(FileOutputStream fileOutputStream = new FileOutputStream(destination)) {
            fileOutputStream.write(data);
            fileOutputStream.close();
        }
        catch (Exception e) {
            LoggerUtil.warnInline("There was an error!");
        }
    }

    /**
     * Returns a Base64 encoding of the image file
     * @param image_path A String representing the path to the image
     * @param savePath A String representing the path where the encoding is stored
     * @return A Base64 encoding of the image file
     * @throws IOException If the file is not found
     */
    public String encoded(String image_path, String savePath) throws IOException {
        FileInputStream imageStream = new FileInputStream(image_path);
        byte[] data = imageStream.readAllBytes();
        String imageString = Base64.getEncoder().encodeToString(data);

        FileWriter fileWriter = new FileWriter(savePath);

        fileWriter.write(imageString);

        fileWriter.close();
        imageStream.close();

        return imageString;
    }

    /**
     * Decodes a Base64 String back to an image file
     * @param txtPath The file where the Base64 encoding is stored
     * @param savePath The path where the new image is stored
     * @throws IOException If the file is not found
     */
    public void decodeImage(String txtPath, String savePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(txtPath);

        byte[] data = Base64.getDecoder().decode(new String(inputStream.readAllBytes()));

        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        fileOutputStream.write(data);

        fileOutputStream.close();
        inputStream.close();
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

    }

    /**
     //     * Show the home screen.
     //     */
    public void showAdminPanel() {
        main.showScene(AdminPanelCtrl.class);
    }

    public void clickUploadImage(ActionEvent e) throws IOException {
        if(e.getSource() == uploadAnImage) {
            filePath = singleFilePathChooser(e);
        }
    }

    public void clickAdd() {
        if(description.getText() == null || consumption.getText() == null || source.getText() == null
            || filePath == null) {
            description.setStyle("-fx-background-color: #FF0000FF; -fx-background-radius: 50");
            consumption.setStyle("-fx-background-color: #FF0000FF; -fx-background-radius: 50");
            source.setStyle("-fx-background-color: #FF0000FF; -fx-background-radius: 50");
            error.setText("All fields are mandatory!");
            uploadAnImage.setBackground(Color.RED);
            //error.setStyle("-fx-background-color: #FF0000FF; -fx-background-radius: 50");
        } else {

        }
    }
}
