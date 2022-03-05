package client.scenes;

import client.utils.ServerUtils;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.*;

public abstract class GameCtrl extends SceneCtrl {

    private enum GameMode {
        SINGLEPLAYER,
        MULTIPLAYER
    }
    private GameMode gameMode;

    @FXML
    protected HBox progressBar;
    @FXML
    protected Text score;
    @FXML
    protected Text question;

    @FXML
    protected Text pointsGainedText;
    @FXML
    protected Text answerBonusText;
    @FXML
    protected Text timeBonusText;

    @FXML
    protected Button nextQuestion;

    @FXML
    protected Text timeLeftText;
    @FXML
    protected AnchorPane timeLeftBar;
    private AnchorPane timeLeftSlider;

    protected double timeLeft = 0;
    protected double lastAnswerChange = 0;

    @Inject
    public GameCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    protected void initialize() {
        timeLeftSlider = (AnchorPane) timeLeftBar.getChildren().get(0);

        pointsGainedText.setVisible(false);
        answerBonusText.setVisible(false);
        timeBonusText.setVisible(false);

        nextQuestion.setVisible(false);

        //----- TODO: Everything below this is temporary and for testing/displaying purposes -----
        gameMode = GameMode.SINGLEPLAYER;
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            questionHistory.add(random.nextBoolean());
        }
        setScore(random.nextInt(10000));
        startTimer();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                reduceTimer(0.5d);
            }
        }, 2000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                reduceTimer(0.5d);
            }
        }, 4000);
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

    Animation timer = null;
    protected void startTimer() {
        timer = timerAnim(timeLeftSlider);

        timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.160, 0.729, 0.901, 1), new CornerRadii(6), Insets.EMPTY)));
        timeMultiplier = 1d;
        timer.playFromStart();
        onTimerEnd();
    }

    protected double timeMultiplier = 1d;
    protected void reduceTimer(double multiplier) {
        timeMultiplier *= multiplier;
        double timeLeft = timer.getCurrentTime().toMillis();
        timer.stop();
        timer = timerAnim(timeLeftSlider);

        timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.925, 0.552, 0.035, 1), new CornerRadii(6), Insets.EMPTY)));
        timer.playFrom(Duration.millis(timeLeft * multiplier));

        onTimerEnd();
    }

    private void onTimerEnd() {
        timer.setOnFinished(event -> {
            showCorrectAnswer(1);

            if (gameMode == GameMode.SINGLEPLAYER) {
                nextQuestion.setVisible(true);
            } else {

            }
        });
    }

    private Animation timerAnim(AnchorPane anchorPane) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(10000 * timeMultiplier));
                setInterpolator(Interpolator.LINEAR);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setPrefWidth(25 + 475 * frac);
                timeLeft = timeMultiplier * (1 - frac);
                timeLeftText.setText("Time left: " + (Math.round(100 * timeLeft) / 10d) + "s");
            }
        };
    }

    protected abstract void showCorrectAnswer(int answer);

    protected void showPointsGained(int answerPoints) {
        int timeBonus = (int) Math.round(lastAnswerChange * 100 * (answerPoints / 100d));
        int total = (int) (answerPoints + timeBonus * (answerPoints / 100d));

        Paint color;
        if (answerPoints >= 90) {
            color = Paint.valueOf("#6cf06a");
        } else if (answerPoints >= 20) {
            color = Paint.valueOf("#ffde61");
        } else {
            color = Paint.valueOf("#f26c64");
        }

        pointsGainedText.setText("You gained " + "0".repeat(Integer.toString(total).length()) + " points");
        pointsGainedText.setFill(color);
        pointsGainedText.setVisible(true);

        answerBonusText.setText("+" + "0".repeat(Integer.toString(answerPoints).length()) + " for answering");
        answerBonusText.setFill(color);
        answerBonusText.setVisible(true);

        timeBonusText.setText("+" + "0".repeat(Integer.toString(timeBonus).length()) + " time bonus");
        timeBonusText.setFill(color);
        timeBonusText.setVisible(true);

        Animation pointsAnim = pointsAnim(total, answerPoints, timeBonus);
        pointsAnim.playFromStart();
    }

    private Animation pointsAnim(int totalPoints, int answerPoints, int timePoints) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
                setInterpolator(new Interpolator() {
                    @Override
                    protected double curve(double t) {
                        return t == 1 ? 1 : 1 - Math.pow(2, -10 * t);
                    }
                });
            }

            @Override
            protected void interpolate(double frac) {
                pointsGainedText.setText("You gained " + lerp(0, totalPoints, frac) + " points");
                answerBonusText.setText("+" + lerp(0, answerPoints, frac) + " for answering");
                timeBonusText.setText("+" + lerp(0, timePoints, frac) + " time bonus");
            }
        };
    }

    /**
     * Lerps color from white to the given color
     *
     * @param r    The normalized red to go to
     * @param g    The normalized green to go to
     * @param b    The normalized blue to go to
     * @param frac The time of the lerp
     * @return An interpolated color
     */
    protected Color lerp(double r, double g, double b, double frac) {
        frac = 1 - frac;
        return new Color(r + ((1 - r) * frac), g + ((1 - g) * frac), b + ((1 - b) * frac), 1);
    }

    /**
     * Lerps color from a given color to a given color
     *
     * @param r1   The normalized red to start from
     * @param g1   The normalized green to start from
     * @param b1   The normalized blue to start from
     * @param r2   The normalized red to go to
     * @param g2   The normalized green to go to
     * @param b2   The normalized  blue to go to
     * @param frac The time of the lerp
     * @return An interpolated color
     */
    protected Color lerp(double r1, double g1, double b1, double r2, double g2, double b2, double frac) {
        frac = 1 - frac;
        return new Color(r2 + ((r1 - r2) * frac), g2 + ((g1 - g2) * frac), b2 + ((b1 - b2) * frac), 1);
    }

    /**
     * Lerps an integer from a given value to a given value
     * @param start The integer to start form
     * @param end The integer to end at
     * @param time The time of the lerp
     * @return An interpolated integer
     */
    private int lerp(int start, int end, double time) {
        return (int) Math.round(start + (end - start) * time);
    }
}
