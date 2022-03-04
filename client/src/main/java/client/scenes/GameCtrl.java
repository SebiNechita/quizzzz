package client.scenes;

import client.utils.ServerUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public abstract class GameCtrl extends SceneCtrl {

    @FXML
    protected HBox progressBar;
    @FXML
    protected Text score;
    @FXML
    protected Text question;

    @Inject
    public GameCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    protected void initialize() {
        //----- TODO: Everything below this is temporary and for testing/displaying purposes -----
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            questionHistory.add(random.nextBoolean());
        }
        setScore(random.nextInt(10000));
    }

    protected LinkedList<Boolean> questionHistory = new LinkedList<>();
    protected void generateProgressDots() {
        ObservableList<Node> children = progressBar.getChildren();
        children.clear();

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);

        Iterator<Boolean> history = questionHistory.iterator();
        for (int i = 0; i < 20; i++) {
            Circle circle;

            if (history.hasNext()) {
                if (history.next()) {
                    circle = generateCircle(Paint.valueOf("#1ce319"), dropShadow);
                } else {
                    circle = generateCircle(Paint.valueOf("#e84343"), dropShadow);
                }
            } else {
                circle = generateCircle(Paint.valueOf("#2b2b2b"), dropShadow);
            }

            children.add(circle);
        }
    }

    private Circle generateCircle(Paint color, Effect effect) {
        Circle circle = new Circle();
        circle.setEffect(effect);
        circle.setStroke(Paint.valueOf("#000"));
        circle.setRadius(11);
        circle.setCache(true);
        circle.setFill(color);

        return circle;
    }

    protected void setScore(int score) {
        this.score.setText("Score: " + score);
    }

    protected void setQuestion(String question) {
        this.question.setText(question);
    }
}
