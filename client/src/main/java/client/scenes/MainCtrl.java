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

import client.utils.OnShowScene;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MainCtrl {

    private Stage primaryStage;

    private final HashMap<Class<?>, SceneCtrl> ctrlClasses = new HashMap<>();
    private final HashMap<Class<?>, Pair<Scene, String>> scenes = new HashMap<>();

    /**
     * Will be run when the window is shown to the user, and initializes all the scenes
     *
     * @param primaryStage The main stage which will show evey scene to the user
     * @param page The page(s) which should be initialized
     */
    @SafeVarargs
    public final void initialize(Stage primaryStage, Triple<? extends SceneCtrl, Parent, String>... page) {
        this.primaryStage = primaryStage;

        for (Triple<? extends SceneCtrl, Parent, String> triple : page) {
            ctrlClasses.put(triple.getLeft().getClass(), triple.getLeft());
            scenes.put(triple.getLeft().getClass(), new Pair<>(new Scene(triple.getMiddle()), triple.getRight()));
        }

        primaryStage.show();
    }

    /**
     * Can be used to show a scene by giving the Ctrl class of that scene.
     * <p>Any method annotated with {@link OnShowScene} annotation will be called as soon as
     * the scene is shown to the user.</p>
     *
     * @param c The SceneCtrl of the class that should be shown
     * @param <T> The type of the SceneCtrl class
     */
    public <T extends SceneCtrl> void showScene(Class<T> c) {
        Pair<Scene, String> pair = scenes.get(c);
        primaryStage.setScene(pair.getKey());
        primaryStage.setTitle(pair.getValue());

        try {
            // Runs every method in the scene with the "OnShowScene" annotation
            for (Method method : c.getDeclaredMethods()) {
                if (method.isAnnotationPresent(OnShowScene.class)) {
                    method.invoke(ctrlClasses.get(c));
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {}
    }
}
