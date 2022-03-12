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

import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Triple;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static String USERNAME = "";
    public static String URL = "https://localhost:8080/";
    public static String TOKEN = "";

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
        Triple<ConnectionCtrl, Parent, String> connection = FXML.load("client/scenes/Connection.fxml", "Connection page");
        Triple<LoginCtrl, Parent, String> login = FXML.load("client/scenes/Login.fxml", "Login page");
        Triple<RegisterCtrl, Parent, String> register = FXML.load("client/scenes/Register.fxml", "Register page");
        Triple<MainMenuCtrl, Parent, String> home = FXML.load("client/scenes/MainMenu.fxml", "Main Menu");
        Triple<SingleplayerLeaderboardCtrl, Parent, String> homeLeaderboard = FXML.load("client/scenes/HomeLeaderboard.fxml", "Singeleplayer Leaderboard");
        Triple<HelpCtrl, Parent, String> help = FXML.load("client/scenes/HelpScreen.fxml", "Help page");

        MainCtrl mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, connection, login, register, home, homeLeaderboard, help);
        mainCtrl.showScene(ConnectionCtrl.class);
    }
}
