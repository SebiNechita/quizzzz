package client.scenes;

import client.utils.ServerUtils;
import commons.utils.Emote;
import commons.utils.GameMode;
import commons.utils.JokerType;
import commons.utils.LoggerUtil;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.*;

public abstract class GameCtrl extends SceneCtrl {

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
    protected VBox jokers;
    private AnchorPane jokerContainer;

    @FXML
    protected Text timeLeftText;
    @FXML
    protected AnchorPane timeLeftBar;
    private AnchorPane timeLeftSlider;

    @FXML
    protected VBox notificationContainer;
    private NotificationRenderer notificationRenderer;

    @FXML
    protected HBox emoteContainer;

    protected double timeLeft = 0;
    protected double lastAnswerChange = 0;
    protected double timeMultiplier = 1d;

    protected LinkedList<Boolean> questionHistory = new LinkedList<>();

    Animation timer = null;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    @Inject
    public GameCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * Gets called after scene has finished loading
     */
    protected void initialize() {
        notificationRenderer = new NotificationRenderer();

        timeLeftSlider = (AnchorPane) timeLeftBar.getChildren().get(0);

        pointsGainedText.setVisible(false);
        answerBonusText.setVisible(false);
        timeBonusText.setVisible(false);

        nextQuestion.setVisible(false);

        jokerContainer = (AnchorPane) jokers.getParent();
        jokerContainer.setVisible(false);
        jokers.getChildren().forEach(joker -> {
            // Hide the tooltips by default
            ((AnchorPane) joker).getChildren().get(1).setVisible(false);
        });

        emoteContainer.setVisible(false);

        notificationContainer.setVisible(gameMode == GameMode.MULTIPLAYER);

        enableListeners();

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

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (random.nextBoolean()) {
                        notificationRenderer.addEmoteNotification(String.valueOf(random.nextInt()), Emote.values()[random.nextInt(5)]);
                    } else {
                        if (random.nextBoolean()) {
                            notificationRenderer.addDisconnectNotification(String.valueOf(random.nextInt()));
                        } else {
                            notificationRenderer.addJokerNotification(String.valueOf(random.nextInt()), JokerType.values()[random.nextInt(3)]);
                        }
                    }
                });
            }
        }, 1000, 400);
    }

    /**
     * Sets up all the listeners
     */
    private void enableListeners() {
        for (Node node : jokers.getChildren()) {
            AnchorPane joker = (AnchorPane) node;
            ImageView jokerImage = (ImageView) joker.getChildren().get(0);
            AnchorPane tooltip = (AnchorPane) joker.getChildren().get(1);

            jokerImage.setOnMouseEntered(event -> {
                hoverAnim(joker, new Color(0.266, 0.266, 0.266, 1), new Color(1, 1, 1, 1)).play();
                showJokerTooltip(tooltip);
            });

            jokerImage.setOnMouseExited(event -> {
                hoverAnim(joker, new Color(1, 1, 1, 1), new Color(0.266, 0.266, 0.266, 1)).play();
                hideJokerTooltip(tooltip);
            });

            jokerImage.setOnMouseClicked(event -> {
                jokerUsed(JokerType.valueOf(joker.getId().toUpperCase()));
            });
        }

        for (Node node : emoteContainer.getChildren()) {
            ImageView emote = (ImageView) node;

            emote.setOnMouseEntered(event -> {
                emoteHoverAnim(emote, false).play();
            });

            emote.setOnMouseExited(event -> {
                emoteHoverAnim(emote, true).play();
            });

            emote.setOnMouseClicked(event -> {
                emoteUsed(Emote.valueOf(emote.getId()));
            });
        }
    }

    /**
     * Gets called when a joker is used
     *
     * @param type What type of joker has been used
     */
    private void jokerUsed(JokerType type) {
        LoggerUtil.infoInline("Clicked on the " + type + " joker.");
    }

    /**
     * Shows the given tooltip
     *
     * @param tooltip The tooltip to show
     */
    private void showJokerTooltip(AnchorPane tooltip) {
        tooltip.setVisible(true);
        FadeTransition transition = new FadeTransition();
        transition.setNode(tooltip);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setDuration(Duration.millis(400));
        transition.playFromStart();
    }

    /**
     * Hides the given tooltip
     *
     * @param tooltip The tooltip to hide
     */
    private void hideJokerTooltip(AnchorPane tooltip) {
        FadeTransition transition = new FadeTransition();
        transition.setNode(tooltip);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setDuration(Duration.millis(400));
        transition.playFromStart();
        tooltip.setVisible(false);
    }

    /**
     * Gets called when an emote is used
     *
     * @param emote What emote has been used
     */
    private void emoteUsed(Emote emote) {
        LoggerUtil.infoInline("Clicked on the " + emote.name() + " emote.");
    }

    /**
     * Generates the colors (or lack thereof) of the progress dots at the top of the screen
     */
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

    /**
     * Helper method for {@link #generateProgressDots}, which generates the dots itself
     *
     * @param color  The color for the circle
     * @param effect The effect which the circle must have
     * @return The generated circle
     */
    private Circle generateCircle(Paint color, Effect effect) {
        Circle circle = new Circle();
        circle.setEffect(effect);
        circle.setStroke(Paint.valueOf("#000"));
        circle.setRadius(11);
        circle.setCache(true);
        circle.setFill(color);

        return circle;
    }

    /**
     * Resets everything and loads the next question
     */
    protected void goToNextQuestion() {
    }

    /**
     * Sets the score shown to the user in the top right of the screen
     *
     * @param score The score to set
     */
    protected void setScore(int score) {
        this.score.setText("Score: " + score);
    }

    /**
     * Sets the question that is shown to the user
     *
     * @param question The question to show
     */
    protected void setQuestion(String question) {
        this.question.setText(question);
    }

    /**
     * The timer which counts down the amount of time left and also shows the correct answer after the time limit has run out
     */
    protected void startTimer() {
        jokerContainer.setVisible(gameMode == GameMode.MULTIPLAYER);
        notificationContainer.setVisible(gameMode == GameMode.MULTIPLAYER);
        emoteContainer.setVisible(gameMode == GameMode.MULTIPLAYER);

        timer = timerAnim(timeLeftSlider);

        timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.160, 0.729, 0.901, 1), new CornerRadii(6), Insets.EMPTY)));
        timeMultiplier = 1d;
        timer.playFromStart();
        onTimerEnd();
    }

    /**
     * Reduces the total amount of time left of the timer
     *
     * @param multiplier The multiplier to reduce with
     */
    protected void reduceTimer(double multiplier) {
        timeMultiplier *= multiplier;
        double timeLeft = timer.getCurrentTime().toMillis();
        timer.stop();
        timer = timerAnim(timeLeftSlider);

        timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.925, 0.552, 0.035, 1), new CornerRadii(6), Insets.EMPTY)));
        timer.playFrom(Duration.millis(timeLeft * multiplier));

        onTimerEnd();
    }

    /**
     * Sets up the events for when the timer runs out
     */
    private void onTimerEnd() {
        timer.setOnFinished(event -> {
            showCorrectAnswer(1);

            nextQuestion.setVisible(gameMode == GameMode.SINGLEPLAYER);
        });
    }

    /**
     * Shows the correct answer to the user
     *
     * @param answer The correct answer (in case of multi-choice, the index of which of the options that is)
     */
    protected abstract void showCorrectAnswer(int answer);

    /**
     * Shows the amount of points a player has gained after answering
     *
     * @param answerPoints The amount of points the player got for answering <p>0 <b>or</b> 100 for multi-choice, number between 0-100 for open</p>
     */
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

    /**
     * Hides point info.
     */
    protected void hidePointsGained(){
        pointsGainedText.setVisible(false);
        answerBonusText.setVisible(false);
        timeBonusText.setVisible(false);
    }

    /**
     * Transitions from white to the target color
     *
     * @param anchorPane The pane which to change the color of
     * @param target     The color to go to
     * @param inverted   If the transition needs to be inverted or not
     * @return The animation object which can be played
     */
    protected Animation hoverAnim(AnchorPane anchorPane, Color target, boolean inverted) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(200));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setBackground(new Background(new BackgroundFill(lerp(target.getRed(), target.getGreen(), target.getBlue(), inverted ? 1 - frac : frac), new CornerRadii(10), Insets.EMPTY)));
            }
        };
    }

    /**
     * Transitions from a specified start color to a specified end color
     *
     * @param anchorPane The pane which to change the color of
     * @param start      The color to start from
     * @param end        The color to go to
     * @return The animation object which can be played
     */
    protected Animation hoverAnim(AnchorPane anchorPane, Color start, Color end) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(200));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setBackground(new Background(new BackgroundFill(lerp(start.getRed(), start.getGreen(), start.getBlue(), end.getRed(), end.getGreen(), end.getBlue(), frac), new CornerRadii(10), Insets.EMPTY)));
            }
        };
    }


    /**
     * Transitions the brightness of the given ImageView
     *
     * @param imageView The element to affect
     * @param inverted  If the animation needs to be reversed or not
     * @return The animation object which can be played
     */
    protected Animation emoteHoverAnim(ImageView imageView, boolean inverted) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(150));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.3 * (inverted ? frac : 1 - frac));
                imageView.setEffect(colorAdjust);
            }
        };
    }

    /**
     * Animates the timer to fill up its bar
     *
     * @param anchorPane The pane which to scroll
     * @return The animation object which can be played
     */
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

    /**
     * Animates the text to count up when the player is displayed their points
     *
     * @param totalPoints  The amount of points to show for the total text
     * @param answerPoints The amount of points to show for the answer text
     * @param timePoints   The amount of points to show for the timer text
     * @return The animation object which can be played
     */
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
     *
     * @param start The integer to start form
     * @param end   The integer to end at
     * @param time  The time of the lerp
     * @return An interpolated integer
     */
    private int lerp(int start, int end, double time) {
        return (int) Math.round(start + (end - start) * time);
    }

    /**
     * Utility class for rendering the notifications during the game.
     */
    private class NotificationRenderer {
        private final Queue<AnchorPane> notifications = new LinkedList<>();
        private Animation fadingOut = null;

        /**
         * Constructs an object and automatically clears out the notification panel.
         */
        private NotificationRenderer() {
            notificationContainer.getChildren().clear();
        }

        /**
         * Adds a notification for when a user uses an emote
         *
         * @param username The username who sent the emote
         * @param emote    The emote which was sent
         */
        private void addEmoteNotification(String username, Emote emote) {
            renderNotification(generateNotification(username + " reacted with:", new Color(1, 1, 1, 1), false, emote));
        }

        /**
         * Adds a notification for when a user disconnects
         *
         * @param username The username who disconnected
         */
        private void addDisconnectNotification(String username) {
            renderNotification(generateNotification(username + " has disconnected", new Color(0.949, 0.423, 0.392, 1), false));
        }

        /**
         * Adds a notification for when a user uses a joker
         *
         * @param username The username who used the joker
         * @param type     The type of joker used
         */
        private void addJokerNotification(String username, JokerType type) {
            renderNotification(generateNotification(username + " has used a " + type.getName() + " joker!", new Color(0.541, 0.929, 1, 1), true));
        }

        /**
         * Generates a notification
         *
         * @param text      The text to show in the notification
         * @param textColor The color which the text should be
         * @param bold      If the text should be bold or not
         * @return The generated AnchorPane which contains the notification
         */
        private AnchorPane generateNotification(String text, Paint textColor, boolean bold) {
            return generateNotification(text, textColor, bold, null);
        }

        /**
         * Generates a notification
         *
         * @param text      The text to show in the notification
         * @param textColor The color which the text should be
         * @param bold      If the text should be bold or not
         * @param emote     The emote that can be shown
         * @return The generated AnchorPane which contains the notification
         */
        private AnchorPane generateNotification(String text, Paint textColor, boolean bold, Emote emote) {
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefWidth(200);
            anchorPane.setPrefHeight(80);
            anchorPane.setBackground(new Background(new BackgroundFill(new Color(0.231, 0.231, 0.231, 0.8), new CornerRadii(8), Insets.EMPTY)));

            Text title = new Text();
            title.setLayoutX(6);
            title.setLayoutY(22);
            title.setWrappingWidth(200);

            title.setText(text);
            title.setFill(textColor);

            title.setFont(Font.font("Comic Sans MS", bold ? FontWeight.BOLD : FontWeight.NORMAL, 18));

            anchorPane.getChildren().add(title);

            if (emote != null) {
                ImageView image = new ImageView("@../../img/emojis/" + emote.toString().toLowerCase() + ".png");
                image.setFitHeight(45);
                image.setFitWidth(45);
                image.setLayoutX(140);
                image.setLayoutY(30);

                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(0);
                image.setEffect(colorAdjust);

                anchorPane.getChildren().add(image);
            }

            return anchorPane;
        }

        /**
         * Renders a given notification in the form of an AnchorPane
         *
         * @param notification The AnchorPane to render
         */
        private void renderNotification(AnchorPane notification) {
            ObservableList<Node> children = notificationContainer.getChildren();
            children.add(notification);
            notifications.add(notification);

            if (notifications.size() > 3) {
                if (fadingOut != null) {
                    fadingOut.jumpTo(Duration.millis(600));
                }

                AnchorPane target = notifications.poll();
                if (target != null) {
                    fadingOut = fadeOut(target);
                    fadingOut.setOnFinished(event -> {
                        children.remove(target);
                    });
                    fadingOut.playFromStart();
                }
            }
        }

        /**
         * Animates the notification to fade out
         *
         * @param anchorPane The AnchorPane to apply the animation to
         * @return The animation object which can be played
         */
        private Animation fadeOut(AnchorPane anchorPane) {
            return new Transition() {
                {
                    setCycleDuration(Duration.millis(600));
                    setInterpolator(Interpolator.EASE_BOTH);
                }

                final Color color = (Color) anchorPane.getBackground().getFills().get(0).getFill();
                final Text text = (Text) anchorPane.getChildren().get(0);
                final Color textColor = (Color) text.getFill();

                @Override
                protected void interpolate(double frac) {
                    anchorPane.setBackground(new Background(new BackgroundFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1 - frac), new CornerRadii(10), Insets.EMPTY)));
                    text.setFill(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 1 - frac));
                }
            };
        }
    }
}
