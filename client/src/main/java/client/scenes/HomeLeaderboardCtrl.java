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

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.LeaderboardEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import packets.LeaderboardResponsePacket;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeLeaderboardCtrl extends SceneCtrl {

    private ObservableList<LeaderboardEntry> data;

    @FXML
    private TableView<LeaderboardEntry> table;
    @FXML
    private TableColumn<LeaderboardEntry, String> colUsername;
    @FXML
    private TableColumn<LeaderboardEntry, String> colPoints;

    @Inject
    public HomeLeaderboardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colUsername.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
        colPoints.setCellValueFactory(q -> new SimpleStringProperty(Integer.toString(q.getValue().points)));
    }

    /**
     * Reload the leaderboard.
     */
    public void refresh() {
        LeaderboardResponsePacket packet = server.getRequest("api/leaderboard", LeaderboardResponsePacket.class);
        //If the request is unsuccessful, the response is null.
        if (packet != null && packet.getLeaderboard() != null) {
            List<LeaderboardEntry> leaderboardEntries = packet.getLeaderboard();
            data = FXCollections.observableList(leaderboardEntries);
            table.setItems(data);
        }
    }

    /**
     * Show the home screen.
     */
    public void showHome(){
        main.showScene(HomeCtrl.class);
    }
}
