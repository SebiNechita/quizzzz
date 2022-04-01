/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.LeaderboardEntry;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import packets.GeneralResponsePacket;
import packets.LeaderboardResponsePacket;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MultiplayerLeaderboardCtrl extends SceneCtrl {

    private ObservableList<LeaderboardEntry> data;

    @FXML
    private TableView<LeaderboardEntry> table;
    @FXML
    private TableColumn<LeaderboardEntry, String> colUsername;
    @FXML
    private TableColumn<LeaderboardEntry, Integer> colPoints;
    @FXML
    private Text rankInfo;
    @FXML
    private Button barChartButton;
    @FXML
    private AnchorPane barChartContainer;
    @FXML
    private BarChart barChart;

    @FXML
    protected Text timeLeftText;
    @FXML
    protected AnchorPane timeLeftBar;
    @FXML
    private AnchorPane timeLeftSlider;

    protected static boolean fromMainMenu = false;

    Animation timer = null;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public MultiplayerLeaderboardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
        colUsername.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
        colPoints.setCellValueFactory(q -> new SimpleIntegerProperty(q.getValue().points).asObject());
    }

    //TODO: Get this to work
    @OnShowScene
    public void onShowScene() {
        barChartButton.setOnMouseEntered(event -> {
            smoothTransition(barChartContainer, true);
        });
        barChartButton.setOnMouseExited(event -> {
            smoothTransition(barChartContainer, false);
        });
        refresh();
        showPlayerRank();
        startTimer();
    }

    protected void startTimer() {

        timeLeftSlider = (AnchorPane) timeLeftBar.getChildren().get(0);
        timer = timerAnim(timeLeftSlider);

        timer.setOnFinished(event -> {
            main.getMultiplayerGame().showQuestion();
        });

        timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.160, 0.729, 0.901, 1), new CornerRadii(50), Insets.EMPTY)));
        timer.playFromStart();

    }

    private Animation timerAnim(AnchorPane anchorPane) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(3000));
                setInterpolator(Interpolator.LINEAR);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setPrefWidth(25 + 475 * frac);
                timeLeftText.setText("Continuing in " + (1 - frac) / 10d + "s");
            }
        };
    }

    private void initializeBarChart(LeaderboardResponsePacket packet) {
        // We have to clear it so that the same data points don't appear twice when we click on Refresh
        barChart.getData().clear();

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Leaderboard");
        barChart.setLegendVisible(false);

        List<LeaderboardEntry> leaderboard = packet.getLeaderboard();
        for (LeaderboardEntry entry : leaderboard) {
            series.getData().add(new XYChart.Data<>(entry.getUsername(), entry.getPoints()));
        }

        barChart.getData().add(series);
        series.getData().stream().filter(data -> data.getXValue().equals(Main.USERNAME)).forEach(data -> data.getNode().setStyle("-fx-background-color: GOLD"));
    }

    public void showPlayerRank() {
        GeneralResponsePacket packet = server.getRequest("api/leaderboard/" + Main.USERNAME + "/rank", GeneralResponsePacket.class);
        rankInfo.setText("Your rank is " + packet.getMessage());
    }

    /**
     * Reload the leaderboard.
     */
    public void refresh() {
        LeaderboardResponsePacket packet = server.getRequest("api/game/multiplayerleaderboard", LeaderboardResponsePacket.class);
        //If the request is unsuccessful, the response is null.
        if (packet != null && packet.getLeaderboard() != null) {
            List<LeaderboardEntry> leaderboardEntries = packet.getLeaderboard();
            data = FXCollections.observableList(leaderboardEntries);
            table.setItems(data);
            initializeBarChart(packet);
            colPoints.setSortType(TableColumn.SortType.DESCENDING);
            table.getSortOrder().add(colPoints);
            table.sort();
        }
    }

    /**
     * Show the home screen.
     */
    public void showHome() {
        if (fromMainMenu) {
            main.showScene(MainMenuCtrl.class);
        } else {
            main.showScene(EndGameCtrl.class);
        }
    }

    /**
     * Ensures that the pane is displayed/hidden smoothly
     * @param pane AnchorPane to be displayed/hidden
     * @param toShow true if the AnchorPane is to be shown; False if it is to be hidden
     */
    private void smoothTransition(AnchorPane pane, boolean toShow) {
        pane.setVisible(toShow);
        FadeTransition transition = new FadeTransition();
        transition.setNode(pane);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setDuration(Duration.millis(400));
        transition.playFromStart();
    }
}
