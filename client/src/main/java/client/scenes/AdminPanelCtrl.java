package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.utils.HttpStatus;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import packets.ResponsePacket;
import packets.ZipRequestPacket;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AdminPanelCtrl extends SceneCtrl {
    final FileChooser fileChooser;
    @FXML
    private Text noOfActivities;

    @FXML
    private VBox buttons;

    @FXML
    private Text infoText;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public AdminPanelCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
        this.fileChooser = new FileChooser();
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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
        this.fileChooser.getExtensionFilters().add(extFilter);
    }

    /**
     * Lets the user choose a ZIP and sends it to the server to import.
     * Handles the UI changes while uploading.
     */
    public void sendZip() throws IOException {
        File file = fileChooser.showOpenDialog(main.getPrimaryStage());
        if (file != null) {
            changeUIUploading();
            byte[] bytes = Files.readAllBytes(file.toPath());
            ResponsePacket responsePacket = server.postRequest("zip/",new ZipRequestPacket(bytes), ResponsePacket.class);
            if (responsePacket.getResponseStatus() == HttpStatus.OK){
                changeUIUploadSuccess();
            }
            else{
                changeUIUploadFail();
            }
        }
    }

    /**
     * Notifies user about successful upload
     */
    private void changeUIUploadSuccess() {
        changeUIUploadEnd();
        infoText.setFill(Paint.valueOf("WHITE"));
        infoText.setText("Uploaded the activities.");
    }

    /**
     * Shows the buttons
     */
    private void changeUIUploadEnd() {
        buttons.setVisible(true);
    }

    /**
     * Notifies user about failed upload
     */
    private void changeUIUploadFail() {
        infoText.setFill(Paint.valueOf("#fc6363"));
        infoText.setText("Failed to upload the activities.");
    }

    /**
     * Hides the buttons
     */
    private void changeUIUploading() {
        buttons.setVisible(false);
    }


    /**
     * Show the home screen.
     */
    public void showAddActivity() {
        main.showScene(AddActivityCtrl.class);
    }

    /**
     * Show the home screen.
     */
    public void showDeleteActivity() {
        main.showScene(DeleteActivityCtrl.class);
    }

    /**
     //     * Show the home screen.
     //     */
    public void showMainMenu() {
        main.showScene(MainMenuCtrl.class);
    }

    /**
     * Gets called when the scene is actually shown to the user
     */
    @OnShowScene
    public void onShowScene() {
        Main.noOfActivities = server.getActivities().size();
        noOfActivities.setText(Main.noOfActivities == 0 ? "X" : Integer.toString(Main.noOfActivities));
        infoText.setText("");
    }
}
