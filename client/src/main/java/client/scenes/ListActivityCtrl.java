package client.scenes;

import client.utils.ActivityItem;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.questions.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import packets.ActivitiesResponsePacket;
import packets.ActivityRequestPacket;
import packets.GeneralResponsePacket;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Activity list controller
 */
public class ListActivityCtrl extends SceneCtrl {
    @FXML
    private TableView<ActivityItem> table;
    @FXML
    private TableColumn<ActivityItem, Long> consumption;

    @FXML
    private TableColumn<ActivityItem, String> id;

    @FXML
    private TableColumn<ActivityItem, ImageView> image;

    @FXML
    private TableColumn<ActivityItem, ImageView> edit;

    @FXML
    private TableColumn<ActivityItem, ImageView> delete;

    @FXML
    private TableColumn<ActivityItem, String> source;

    @FXML
    private TableColumn<ActivityItem, String> title;

    @FXML
    private AnchorPane anchorPane;

    private ObservableList<ActivityItem> items;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public ListActivityCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * initialize this class
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * display table and added event listenerss
     */
    @OnShowScene
    public void onShowScene() {
        this.items = convertToActivityItem(getAllActivity());

        // set up columns
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        consumption.setCellValueFactory(new PropertyValueFactory<>("consumption"));
        source.setCellValueFactory(new PropertyValueFactory<>("source"));
        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        edit.setCellValueFactory(new PropertyValueFactory<>("edit"));
        delete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        // enable cell selection
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.setItems(items);
        // add event listeners
        addImageEventListener();
        addEditEventListener();
        addDeleteEventListener();

    }

    /**
     * get all activities from database
     *
     * @return
     */
    public List<Activity> getAllActivity() {
        ActivitiesResponsePacket reponse = server.getRequest("api/activities/list", ActivitiesResponsePacket.class);

        return reponse.getActivities();
    }

    /**
     * prepare ObservableList object for the TableView
     *
     * @param list
     * @return
     */
    public ObservableList<ActivityItem> convertToActivityItem(List<Activity> list) {

        ObservableList<ActivityItem> res = FXCollections.observableArrayList();
        Image image = new Image("@../../img/view.png");
        Image edit = new Image("@../../img/edit.png");
        Image delete = new Image("@../../img/delete.png");
        for (Activity activity : list) {
            ImageView imageIcon = new ImageView(image);
            ImageView editIcon = new ImageView(edit);
            ImageView deleteIcon = new ImageView(delete);
            res.add(new ActivityItem(
                    activity.getId(),
                    imageIcon,
                    editIcon,
                    deleteIcon,
                    activity.getConsumption_in_wh(),
                    activity.getSource(),
                    activity.getTitle(),
                    activity.getImage_path()));
        }

        return res;
    }

    /**
     * add event listener to image column
     */
    public void addImageEventListener() {
        this.image.setCellFactory(tc -> {
            TableCell<ActivityItem, ImageView> cell = new TableCell<ActivityItem, ImageView>() {
                @Override
                protected void updateItem(ImageView item, boolean empty) {
                    super.updateItem(item, empty);
                    //setText(empty ? null : item);
                    if (item != null) {
                        // without this the image won't be displayed
                        setGraphic(item);
                    }
                }
            };

            //cell.setOnMouseClicked(
            final Popup popup = new Popup();
            popup.setAutoHide(true);

            cell.setOnMouseEntered(e -> {

                ActivityItem item = cell.getTableView().getItems().get(cell.getIndex());
                // clear previous content
                popup.getContent().clear();
                // create image
                Image image = server.getImage(item.getImagePath());
                popup.getContent().add(new ImageView(image));

                Stage stage = (Stage) anchorPane.getScene().getWindow();
                popup.show(stage, e.getScreenX() - 300, e.getSceneY());
            });

            cell.setOnMouseExited(e -> {
                popup.getContent().clear();
                popup.hide();
            });


            return cell;
        });
    }

    /**
     * add event listener to edit column
     */
    public void addEditEventListener() {
        this.edit.setCellFactory(tc -> {
            TableCell<ActivityItem, ImageView> cell = new TableCell<ActivityItem, ImageView>() {
                @Override
                protected void updateItem(ImageView item, boolean empty) {
                    super.updateItem(item, empty);
                    //setText(empty ? null : item);
                    if (item != null) {
                        // without this the image won't be displayed
                        setGraphic(item);
                    }
                }
            };

            cell.setOnMouseClicked(e -> {

                ActivityItem item = cell.getTableView().getItems().get(cell.getIndex());
                Activity activity = new Activity(item.getTitle(), item.getId(), item.getImagePath(), item.getConsumption(), item.getSource());

                // do something with the item...
                main.showEditActivity("client/scenes/EditActivity.fxml", "Edit Activity", activity);
            });

            return cell;
        });
    }

    /**
     * add event listener to delete column
     */
    public void addDeleteEventListener() {
        this.delete.setCellFactory(tc -> {
            TableCell<ActivityItem, ImageView> cell = new TableCell<ActivityItem, ImageView>() {
                @Override
                protected void updateItem(ImageView item, boolean empty) {
                    super.updateItem(item, empty);
                    //setText(empty ? null : item);
                    if (item != null) {
                        // without this the image won't be displayed
                        setGraphic(item);
                    }
                }
            };

            cell.setOnMouseClicked(e -> {

                ActivityItem item = cell.getTableView().getItems().get(cell.getIndex());
                Activity activity = new Activity(item.getTitle(), item.getId(), item.getImagePath(), item.getConsumption(), item.getSource());

                // show an Alert dialog box
                Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Delete '" + item.getTitle() + "' activity?",
                        ButtonType.YES, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    ActivityRequestPacket packet = new ActivityRequestPacket(item.getId());
                    server.postRequest("api/activities/delete", packet, GeneralResponsePacket.class);
                    main.showScene(ListActivityCtrl.class);
                }
            });

            return cell;
        });

    }

    /**
     * click event handler for 'Back' button
     *
     * @param actionEvent event
     */
    public void backClicked(ActionEvent actionEvent) {
        main.showScene(AdminPanelCtrl.class);
    }

}
