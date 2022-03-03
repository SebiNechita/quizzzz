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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;

public class MainCtrl {

    private Stage primaryStage;

    private final HashMap<Class<?>, SceneCtrl> ctrlClasses = new HashMap<>();
    private final HashMap<Class<?>, Pair<Scene, String>> scenes = new HashMap<>();

    @SafeVarargs
    public final void initialize(Stage primaryStage, Triple<? extends SceneCtrl, Parent, String>... page) {
        this.primaryStage = primaryStage;

        for (Triple<? extends SceneCtrl, Parent, String> triple : page) {
            ctrlClasses.put(triple.getLeft().getClass(), triple.getLeft());
            scenes.put(triple.getLeft().getClass(), new Pair<>(new Scene(triple.getMiddle()), triple.getRight()));
        }

        primaryStage.show();
    }

    public <T extends SceneCtrl> T getCtrl(Class<T> c) {
        return c.cast(ctrlClasses.get(c));
    }

    public <T extends SceneCtrl> void showScene(Class<T> c) {
        Pair<Scene, String> pair = scenes.get(c);
        primaryStage.setScene(pair.getKey());
        primaryStage.setTitle(pair.getValue());
    }
}