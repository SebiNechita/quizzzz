package client.scenes;

import client.utils.ActivityItem;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.questions.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import packets.ActivitiesResponsePacket;

import javax.swing.text.TabableView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private TableColumn<ActivityItem, String> source;

    @FXML
    private TableColumn<ActivityItem, String> title;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @OnShowScene
    public void onShowScene() {
//        ObservableList<ActivityItem> items = FXCollections.observableArrayList();
        this.items = convertToActivityItem(getAllActivity());


        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        title.setCellValueFactory(new PropertyValueFactory<>("title"));

        consumption.setCellValueFactory(new PropertyValueFactory<>("consumption"));

        source.setCellValueFactory(new PropertyValueFactory<>("source"));

        image.setCellValueFactory(new PropertyValueFactory<>("image"));

        edit.setCellValueFactory(new PropertyValueFactory<>("edit"));
        // enable cell selection
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.setItems(items);
        this.image.setCellFactory(tc -> {
            TableCell<ActivityItem, ImageView> cell = new TableCell<ActivityItem, ImageView>() {
                @Override
                protected void updateItem(ImageView item, boolean empty) {
                    super.updateItem(item, empty);
                    //setText(empty ? null : item);
                    if (item != null) {
                        //setItem(item);
                        setGraphic(item);
                    }
                }
            };

            cell.setOnMouseClicked(e -> {
                if (true) {
                    ActivityItem item = cell.getTableView().getItems().get(cell.getIndex());
                    // do something with id...
                    var title = item.getTitle();

                    var test = image.getCellFactory();
                    var test1 = image.getCellValueFactory();

                    System.out.println(test1);
                }


            });
            return cell;
        });


    }

    public List<Activity> getAllActivity() {
        ActivitiesResponsePacket reponse = server.getRequest("api/activities/list", ActivitiesResponsePacket.class);

//        return reponse.getActivities().stream().limit(200).collect(Collectors.toList());
        return reponse.getActivities();
    }

    public ObservableList<ActivityItem> convertToActivityItem(List<Activity> list) {

        ObservableList<ActivityItem> res = FXCollections.observableArrayList();
        Image image = new Image("@../../img/view.png");
        Image edit = new Image("@../../img/edit.png");
        for (Activity activity : list) {
            ImageView imageIcon = new ImageView(image);
            ImageView editIcon = new ImageView(edit);

            res.add(new ActivityItem(
                    activity.getId(),
                    imageIcon,
                    editIcon,
                    activity.getConsumption_in_wh(),
                    activity.getSource(),
                    activity.getTitle()));
        }

        return res;
    }

    public void backClicked(ActionEvent actionEvent) {
        main.showScene(AdminPanelCtrl.class);
    }

    public void addClicked(ActionEvent actionEvent) {
    }
}
