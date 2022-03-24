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

import client.game.SingleplayerGame;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.utils.LoggerUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MainCtrl {

    private final Stage primaryStage;
    private final ServerUtils serverUtils;
    private SingleplayerGame singleplayerGame;

    private final HashMap<Class<?>, SceneCtrl> ctrlClasses = new HashMap<>();
    private final HashMap<Class<?>, Pair<Scene, String>> scenes = new HashMap<>();

    /**
     * Constructs a new MainCtrl instance
     *
     * @param primaryStage The primary stage which will contain every scene that will be shown to the user
     */
    public MainCtrl(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.serverUtils = new ServerUtils();
    }

    /**
     * Creates a new SingleplayerGame
     */
    public void createNewSingleplayerGame() {
        this.singleplayerGame = new SingleplayerGame(this, serverUtils);
    }

    /**
     * getter for the current SingleplayerGame
     * @return the current SingleplayerGame
     */
    public SingleplayerGame getSingleplayerGame() {
        return singleplayerGame;
    }

    /**
     * Loads and initializes a scene
     *
     * @param path  The path of the FXML file which contains the graphics of the scene
     * @param title The title that the window must have when this scene is shown
     * @param <T>   The type of the SceneCtrl
     */
    public <T extends SceneCtrl> void load(String path, String title) {
        try {
            URL scene = getClass().getClassLoader().getResource(path);
            FXMLLoader loader = new FXMLLoader(scene, null, null, new ControllerFactory(), StandardCharsets.UTF_8);

            Parent parent = loader.load();
            T ctrl = loader.getController();

            initialize(ctrl, parent, title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Will be run when the window is shown to the user, and initializes all the scenes
     *
     * @param ctrl   The Ctrl that should be initialized
     * @param parent The parent (the scene) that should be initialized
     * @param title  The title for that scene
     * @param <T>    The type of the SceneCtrl
     */
    private <T extends SceneCtrl> void initialize(T ctrl, Parent parent, String title) {
        ctrlClasses.put(ctrl.getClass(), ctrl);
        scenes.put(ctrl.getClass(), new Pair<>(new Scene(parent), title));
    }

    /**
     * Gets the Ctrl object of the specified scene.
     *
     * @param c   The class to get the instance for
     * @param <T> The type of the SceneCtrl class
     * @return The Ctrl instance
     */
    public <T extends SceneCtrl> T getCtrl(Class<T> c) {
        return c.cast(ctrlClasses.get(c));
    }

    /**
     * Can be used to show a scene by giving the Ctrl class of that scene.
     * <p>Any method annotated with the {@link OnShowScene} annotation will be called as soon as
     * the scene is shown to the user.</p>
     *
     * @param c   The SceneCtrl of the class that should be shown
     * @param <T> The type of the SceneCtrl class
     */
    public <T extends SceneCtrl> void showScene(Class<T> c) {
        if (!primaryStage.isShowing())
            primaryStage.show();

        Pair<Scene, String> pair = scenes.get(c);
        primaryStage.setScene(pair.getKey());
        primaryStage.setTitle(pair.getValue());

        try {
            // Runs every method in the SceneCtrl with the "OnShowScene" annotation
            for (Method method : c.getDeclaredMethods()) {
                if (method.isAnnotationPresent(OnShowScene.class)) {
                    method.invoke(ctrlClasses.get(c));
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    /**
     * Utility class which will try to get the constructor of a SceneCtrl class, and
     * return a new instance of it if it exists.
     */
    private class ControllerFactory implements Callback<Class<?>, Object> {
        /**
         * The <code>call</code> method is called when required, and is given a
         * single argument of type P, with a requirement that an object of type R
         * is returned.
         *
         * @param param The single argument upon which the returned value should be
         *              determined.
         * @return An object of type R that may be determined based on the provided
         * parameter value.
         */
        @Override
        public Object call(Class<?> param) {
            try {
                return param.getDeclaredConstructor(MainCtrl.class, ServerUtils.class).newInstance(MainCtrl.this, serverUtils);
            } catch (ReflectiveOperationException e) {
                LoggerUtil.severe("The SceneCtrl $HL" + param.getSimpleName() + "$ doesn't have its constructor setup correctly.");
//                e.printStackTrace();
                return null;
            }
        }
    }
}
