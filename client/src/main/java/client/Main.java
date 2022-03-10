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

import client.scenes.HomeCtrl;
import client.scenes.HomeLeaderboardCtrl;
import client.scenes.MainCtrl;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Triple;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static String URL = "https://localhost:8080/";
    public static String TOKEN = "";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Triple<HomeCtrl, Parent, String> home = FXML.load("client/scenes/Home.fxml", "Main Menu");
        Triple<HomeLeaderboardCtrl, Parent, String> homeLeaderboard = FXML.load("client/scenes/HomeLeaderboard.fxml", "Main Menu");

        MainCtrl mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, home, homeLeaderboard);
        mainCtrl.showScene(HomeCtrl.class);
    }
}
