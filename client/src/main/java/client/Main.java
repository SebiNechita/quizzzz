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
package client;

import client.scenes.ConnectionCtrl;
import client.scenes.LoginCtrl;
import client.scenes.MainCtrl;
import client.scenes.MainMenuCtrl;
import commons.utils.GameMode;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static String USERNAME = "";
    public static String URL = "https://localhost:8080/";
    public static String TOKEN = "";
    public static GameMode gameMode;

    /**
     * Gets called when the application is started
     *
     * @param args The arguments given on startup
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Sets up and starts the JavaFX application
     *
     * @param primaryStage The stage on which all content gets shown
     */
    @Override
    public void start(Stage primaryStage) {
        boolean debug = false;
        MainCtrl mainCtrl = new MainCtrl(primaryStage);

        mainCtrl.load("client/scenes/Connection.fxml", "Connection page");
        mainCtrl.load("client/scenes/Login.fxml", "Login page");
        mainCtrl.load("client/scenes/Register.fxml", "Register page");
        mainCtrl.load("client/scenes/MainMenu.fxml", "Main Menu");
        mainCtrl.load("client/scenes/HomeLeaderboard.fxml", "Singleplayer Leaderboard");
        mainCtrl.load("client/scenes/HelpScreen.fxml", "Help page");
        mainCtrl.load("client/scenes/GameMultiChoice.fxml", "Game Screen");
        mainCtrl.load("client/scenes/Lobby.fxml","Lobby  Screen");
        mainCtrl.load("client/scenes/GameOpenQuestion.fxml", "Game Screen");

        //For testing, I skipped the Connection and Login screens
        if (debug) {
            LoginCtrl login = mainCtrl.getCtrl(LoginCtrl.class);
            login.login("Kristof", "password");
            mainCtrl.showScene(MainMenuCtrl.class);
        }
        else{
            mainCtrl.showScene(ConnectionCtrl.class);
        }

    }
}
