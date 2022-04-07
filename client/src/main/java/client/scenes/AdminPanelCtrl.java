package client.scenes;

import client.Main;
import client.utils.AnimationUtil;
import client.utils.ColorPresets;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.utils.HttpStatus;
import commons.utils.LoggerUtil;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import packets.ResponsePacket;
import packets.ZipRequestPacket;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AdminPanelCtrl extends SceneCtrl {
    final FileChooser fileChooser;
    private Animation uploadingAnim;
    @FXML
    private Text noOfActivities;

    @FXML
    private Text infoText;

    @FXML
    private Button sendZipButton;

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
     * Listener for the Import Zip button
     */
    public void onClickZip() {
        uploadingAnim = AnimationUtil.connectingAnimation(sendZipButton);
        uploadingAnim.play();
        sendZip();
    }

    /**
     * Lets the user choose a ZIP and sends it to the server to import.
     * Handles the UI changes while uploading.
     */
    public void sendZip() {

        File file = fileChooser.showOpenDialog(main.getPrimaryStage());
        new Thread(() -> {
            if (file != null) {
                infoText.setText("Hang Tight! We're uploading your file...");
                byte[] bytes = new byte[0];
                try {
                    bytes = Files.readAllBytes(file.toPath());
                } catch (IOException e) {
                    LoggerUtil.warnInline("Failed to read the zip at: " + file.getAbsolutePath());
                }
                ResponsePacket responsePacket = server.postRequest("zip/",new ZipRequestPacket(bytes), ResponsePacket.class);
                if (responsePacket.getResponseStatus() == HttpStatus.Created){
                    changeUIUploadSuccess();
                } else {
                    AnimationUtil.shake(sendZipButton).play();
                    changeUIUploadFail();
                }
                updateActivitiesCount();
            }
            uploadingAnim.jumpTo(Duration.ZERO); // To reset the styling of the button
            uploadingAnim.stop();
            sendZipButton.styleProperty().unbind();
            sendZipButton.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.white) + "; -fx-background-radius: 50");
        }).start();
    }

    /**
     * Notifies user about successful upload
     */
    private void changeUIUploadSuccess() {
        infoText.setFill(ColorPresets.white);
        infoText.setText("Uploaded the activities.");
    }

    /**
     * Notifies user about failed upload
     */
    private void changeUIUploadFail() {
        infoText.setFill(ColorPresets.soft_red);
        infoText.setText("Failed to upload the activities.");
    }

    /**
     * Updates activities count
     */
    private void updateActivitiesCount() {
        Main.noOfActivities = server.getActivities().size();
        noOfActivities.setText(Main.noOfActivities == 0 ? "X" : Integer.toString(Main.noOfActivities));
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
     * Show the home screen.
     */
    public void showMainMenu() {
        main.showScene(MainMenuCtrl.class);
    }

    /**
     * Gets called when the scene is actually shown to the user
     */
    @OnShowScene
    public void onShowScene() {
        updateActivitiesCount();
        sendZipButton.setStyle("-fx-background-color: " + ColorPresets.toHex(ColorPresets.white) + "; -fx-background-radius: 50");
        infoText.setText("");
    }

    public void showActivityList() {
        main.showScene(ListActivityCtrl.class);
    }
}
