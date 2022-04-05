package client.scenes;

import client.Main;
import client.utils.AnimationUtil;
import client.utils.ColorPresets;
import client.utils.NotificationRenderer;
import client.utils.ServerUtils;
import com.google.common.util.concurrent.AtomicDouble;
import commons.utils.Emote;
import commons.utils.GameMode;
import commons.utils.JokerType;
import commons.utils.LoggerUtil;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Objects;

import static client.utils.EmoteUtility.emoteHoverAnim;
import static client.utils.EmoteUtility.emoteUsed;

public abstract class GameCtrl extends SceneCtrl {

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
    protected Button muteSound;
    @FXML
    protected Button quitButton;

    @FXML
    protected VBox jokers;
    @FXML
    private AnchorPane jokerContainer;

    @FXML
    protected Text timeLeftText;
    @FXML
    protected AnchorPane timeLeftBar;
    @FXML
    private AnchorPane timeLeftSlider;

    @FXML
    protected VBox notificationContainer;

    public NotificationRenderer notificationRenderer;

    @FXML
    protected HBox emoteContainer;

    protected AtomicDouble timeLeft = new AtomicDouble(0);
    protected double lastAnswerChange = 0;
    protected double timeMultiplier = 1d;

    protected static boolean mute = false;

    /**
     * There are three options visible to the user.
     * This variable describes the option in which the answer is visible.
     * Starts from 0
     */
    protected int answerOptionNumber;
    Animation timer = null;

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public GameCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * Gets called after scene has finished loading
     */
    protected void initialize() {
    }

    /**
     * This method is called from its subclasses when that scene is displayed
     */
    protected void onShowScene() {
        notificationRenderer = new NotificationRenderer(notificationContainer);

        timeLeftSlider = (AnchorPane) timeLeftBar.getChildren().get(0);

        pointsGainedText.setVisible(false);
        answerBonusText.setVisible(false);
        timeBonusText.setVisible(false);

        setScore(main.getGame(Main.gameMode).getScoreTotal());

        nextQuestion.setVisible(false);

        jokerContainer = (AnchorPane) jokers.getParent();
        jokerContainer.setVisible(true);
        jokers.getChildren().forEach(joker -> {
            // Hide the tooltips by default
            ((AnchorPane) joker).getChildren().get(1).setVisible(false);
        });

        emoteContainer.setVisible(false);

        notificationContainer.setVisible(Main.gameMode == GameMode.MULTIPLAYER);

        if (Main.gameMode == GameMode.MULTIPLAYER) {
            for (JokerType joker : main.getMultiplayerGame().getDisabledJokers()) {
                disableJoker(joker);
            }
        }

        setMuteButton();
        generateProgressDots();
        enableListeners();
        startTimer();
    }

    /**
     * Sets up all the listeners
     */
    private void enableListeners() {
        for (Node node : jokers.getChildren()) {
            AnchorPane joker = (AnchorPane) node;
            JokerType jokerType = JokerType.valueOf(joker.getId().toUpperCase());

            ImageView jokerImage = (ImageView) joker.getChildren().get(0);
            AnchorPane tooltip = (AnchorPane) joker.getChildren().get(1);

            jokerImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (!main.getMultiplayerGame().getDisabledJokers().contains(jokerType)) {
                    jokerUsed(jokerType, tooltip);

                    if (joker.getId().equalsIgnoreCase(JokerType.HALF_TIME.toString())) {
                        main.getMultiplayerGame().sendJokerClickedToAllClients(JokerType.HALF_TIME, this.getClass());
                    } else if (joker.getId().equalsIgnoreCase(JokerType.REMOVE_ANSWER.toString())) {
                        if (this instanceof GameMultiChoiceCtrl) {
                            ((GameMultiChoiceCtrl) this).removeWrongAnswer();
                        }
                    }
                }
            });

            jokerImage.setOnMouseExited(event -> {
                if (main.getMultiplayerGame().getDisabledJokers().contains(jokerType))
                    return;

                AnimationUtil.fadeAnim(joker, ColorPresets.white, ColorPresets.gray, 200, 10).play();
                hideJokerTooltip(tooltip);
            });

            jokerImage.setOnMouseEntered(event -> {
                if (main.getMultiplayerGame().getDisabledJokers().contains(jokerType))
                    return;

                AnimationUtil.fadeAnim(joker, ColorPresets.gray, ColorPresets.white, 200, 10).play();
                showJokerTooltip(tooltip);
            });
        }

        for (Node node : emoteContainer.getChildren()) {
            ImageView emote = (ImageView) node;

            emote.setOnMouseEntered(event -> emoteHoverAnim(emote, false).play());
            emote.setOnMouseExited(event -> emoteHoverAnim(emote, true).play());
            emote.setOnMouseClicked(event -> emoteUsed(Emote.valueOf(emote.getId())));
        }
    }

    /**
     * Gets called when a joker is used
     *
     * @param type    What type of joker has been used
     * @param tooltip The tooltip of the joker
     */
    protected void jokerUsed(JokerType type, AnchorPane tooltip) {
        disableJoker(type);
        main.getMultiplayerGame().addJokerUsed(type);
        hideJokerTooltip(tooltip);
        main.getMultiplayerGame().sendJokerNotification(Main.USERNAME, type);
    }

    /**
     * Can be used to disallow a user form using a specific joker
     *
     * @param type The joker to disable
     */
    protected void disableJoker(JokerType type) {
        disableJoker(type, false);
    }

    /**
     * Can be used to disallow a user form using a specific joker
     *
     * @param type      The joker to disable
     * @param temporary If the joker only needs to be disabled temporarily, not adding it to the jokersUsed list
     */
    protected void disableJoker(JokerType type, boolean temporary) {
        jokers.getChildren().stream()
                .filter(joker -> joker.getId().equalsIgnoreCase(type.toString()))
                .map(joker -> (AnchorPane) joker)
                .forEach(joker -> {
                    ImageView image = (ImageView) joker.getChildren().get(0);

                    AnimationUtil.fadeAnim(joker, ColorPresets.white, ColorPresets.gray, 200, 10).play();
                    ColorAdjust effect = new ColorAdjust();
                    effect.setBrightness(-0.5);
                    effect.setContrast(-0.5);
                    effect.setSaturation(-1);

                    image.setEffect(effect);

                    if (!temporary) {
                        main.getMultiplayerGame().addJokerUsed(type);
                    }
                });
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
    protected void hideJokerTooltip(AnchorPane tooltip) {
        FadeTransition transition = new FadeTransition();
        transition.setNode(tooltip);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setDuration(Duration.millis(400));
        transition.playFromStart();
        tooltip.setVisible(false);
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

        Iterator<Boolean> history = main.getGame(Main.gameMode).getQuestionHistory().iterator();

        for (int i = 0; i < 20; i++) {
            Circle circle;
            if (history.hasNext()) {
                if (history.next()) {
                    circle = generateCircle(ColorPresets.green, dropShadow);
                } else {
                    circle = generateCircle(ColorPresets.red, dropShadow);
                }
            } else {
                circle = generateCircle(ColorPresets.dark_gray, dropShadow);
            }

            children.add(circle);
        }
//FIXME:
        numberOfQuestions++;
        //if(numberofQuestions  == 10)    main.showScene(MultiLeaderboardCtrl.class);
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
        circle.setStroke(ColorPresets.black);
        circle.setRadius(11);
        circle.setCache(true);
        circle.setFill(color);

        return circle;
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
     * Resets the scene so that it is ready to show the next question
     */
    protected void initialiseNextQuestion() {
        nextQuestion.setVisible(false);
        hidePointsGained();

        if (Main.gameMode == GameMode.MULTIPLAYER) {
            for (JokerType joker : main.getMultiplayerGame().getDisabledJokers()) {
                disableJoker(joker);
            }
        }

        main.getGame(Main.gameMode).jumpToNextQuestion();
    }

    /**
     * The timer which counts down the amount of time left and also shows the correct answer after the time limit has run out
     */
    protected void startTimer() {
        LoggerUtil.log("test");
        jokerContainer.setVisible(Main.gameMode == GameMode.MULTIPLAYER);
        notificationContainer.setVisible(Main.gameMode == GameMode.MULTIPLAYER);
//        emoteContainer.setVisible(Main.gameMode == GameMode.MULTIPLAYER);
// FIXME:
        timer = AnimationUtil.timerAnim(timeLeftSlider, timeLeft, timeMultiplier, timeLeftText);
        timeMultiplier = 1d;
        timer = timerAnim(timeLeftSlider);

        timeLeftSlider.setBackground(new Background(new BackgroundFill(ColorPresets.timer_bar_regular, new CornerRadii(50), Insets.EMPTY)));
        timeMultiplier = 1d;
        timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.160, 0.729, 0.901, 1), new CornerRadii(50), Insets.EMPTY)));
//        timeMultiplier = 1d;
        timer.playFromStart();
        onTimerEnd();
    }

    protected abstract void onWaitTimerEnd();

    /**
     * The timer which counts down the amount of time left and also shows the correct answer after the time limit has run out
     */
    protected void startWaitTimer() {
        jokerContainer.setVisible(false);
        notificationContainer.setVisible(true);
        //emoteContainer.setVisible(Main.gameMode == GameMode.MULTIPLAYER);

        timer = waitTimerAnim(timeLeftSlider);

        timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.160, 0.729, 0.901, 1), new CornerRadii(50), Insets.EMPTY)));
        timer.playFromStart();
        onWaitTimerEnd();
    }


    /**
     * Reduces the total amount of time left of the timer
     *
     * @param multiplier The multiplier to reduce with
     */
    public void reduceTimer(double multiplier) {
        timeMultiplier *= multiplier;
        timeLeft.set(timer.getCurrentTime().toMillis());
        timer.stop();
        timer = AnimationUtil.timerAnim(timeLeftSlider, timeLeft, timeMultiplier, timeLeftText);

        timeLeftSlider.setBackground(new Background(new BackgroundFill(ColorPresets.timer_bar_rushed, new CornerRadii(6), Insets.EMPTY)));
        timer.playFrom(Duration.millis(timeLeft.get() * multiplier));

        onTimerEnd();
    }

    /**
     * Sets up the events for when the timer runs out
     */
    protected abstract void onTimerEnd();

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
        answerPoints = Math.min(Math.max(answerPoints, 0), 100);
        int timeBonus = (int) Math.round(lastAnswerChange * 100 * (answerPoints / 100d));
        int total = (int) (answerPoints + timeBonus * (answerPoints / 100d));

        if (Main.gameMode == GameMode.MULTIPLAYER) {
            if (main.getMultiplayerGame().isJokerActive(JokerType.DOUBLE_POINTS)) {
                total *= 2;
            }

            main.getMultiplayerGame().addToScore(total);
        }

        setScore(main.getGame(Main.gameMode).getScoreTotal());

        Paint color;
        if (answerPoints >= 90) {
            color = ColorPresets.soft_green;
        } else if (answerPoints >= 20) {
            color = ColorPresets.soft_yellow;
        } else {
            color = ColorPresets.soft_red;
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

        Animation pointsAnim = AnimationUtil.pointsAnim(total, answerPoints, timeBonus, pointsGainedText, answerBonusText, timeBonusText);
        pointsAnim.playFromStart();
    }

    /**
     * Hides point info.
     */
    protected void hidePointsGained() {
        pointsGainedText.setVisible(false);
        answerBonusText.setVisible(false);
        timeBonusText.setVisible(false);
    }
//FIXME:
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
                anchorPane.setBackground(new Background(new BackgroundFill(lerp(target.getRed(), target.getGreen(), target.getBlue(), inverted ? 1 - frac : frac), new CornerRadii(40), Insets.EMPTY)));
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
     * Animates the timer to fill up its bar
     *
     * @param anchorPane The pane which to scroll
     * @return The animation object which can be played
     */
    private Animation waitTimerAnim(AnchorPane anchorPane) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(5000));
                setInterpolator(Interpolator.LINEAR);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setPrefWidth(25 + 475 * frac);
                timeLeft = timeMultiplier * (1 - frac) / 2;
                timeLeftText.setText("Next Question in: " + (Math.round(100 * timeLeft) / 10d) + "s");
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
     * Rounds the image
     * Source: https://stackoverflow.com/a/56303884/9957954
     *
     * @param imageView imageView in which the image will be viewed
     */
    public void setRoundedImage(ImageView imageView) {
        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        imageView.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        imageView.setClip(null);
        imageView.setEffect(new DropShadow(40, Color.BLACK));
        imageView.setImage(image);
    }

    /**
     * Gets called when the mute button is pressed
     */
    @FXML
    protected void onMute() {
        String imagePath = mute ? "img/unmute.png" : "img/mute.png";
        mute = !mute;

        ImageView icon = (ImageView) muteSound.getChildrenUnmodifiable().get(0);
        icon.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath))));
    }

    /**
     * Quits from the current game session. Sets main.singleplayerGame to null and stops the timer which would otherwise continue running.
     */
    @FXML
    protected void quitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Leave Game");
        alert.setHeaderText("You are about to leave this game!");
        alert.setContentText("Are you sure you want to leave?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            LoggerUtil.infoInline(Main.USERNAME + " quit the singleplayer game!");
            main.showScene(MainMenuCtrl.class);
            timer.setOnFinished(event -> {
            });
            timer = null;
            main.quitSingleplayer();
        }
    }

    /**
     * This method is used to ensure that the mute button is in the same state in both the question types. This must be called onShowScene
     */
    protected void setMuteButton() {
        String imagePath = mute ? "img/mute.png" : "img/unmute.png";
        ImageView icon = (ImageView) muteSound.getChildrenUnmodifiable().get(0);
        icon.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath))));
    }

    /**
     * Plays a sound to the user when the answer is revealed
     *
     * @param isCorrect If the answer was correct or not
     */
    protected void playSound(boolean isCorrect) {
        if (mute) return;

        String soundFilePath = "";
        try {
            soundFilePath = Objects.requireNonNull(getClass().getResource(isCorrect ? "/sounds/correct.wav" : "/sounds/wrong.mp3")).toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Media media = new Media(soundFilePath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
}
