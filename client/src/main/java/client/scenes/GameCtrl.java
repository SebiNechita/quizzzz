package client.scenes;

import client.Main;
import client.utils.AnimationUtil;
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

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.*;

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

    protected static List<AnchorPane> disabledJokers;
    private List<AnchorPane> touchNotUsedJokers;

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

    // Static because it has to be common between both the GameOpenQuestionCtrl and GameMultiChoiceCtrl
    protected static int numberOfQuestions = -1;
    protected AtomicDouble timeLeft = new AtomicDouble(0);
    protected double lastAnswerChange = 0;
    protected double timeMultiplier = 1d;
    protected static boolean doublePoints;
    protected static boolean doublePointsUsed;
    protected static boolean halfTime;
    protected static boolean halfTimeUsed;
    protected static boolean removeAnswer;
    protected static boolean removeAnswerUsed;

    protected static boolean jokerClicked;

    public static Map<JokerType, Boolean> jokersUsed;

    // static because the state has to be the same between both the question types
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
    @Inject
    public GameCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
        disabledJokers = new LinkedList<>();
        jokersUsed = new HashMap<>();
        jokersUsed.put(JokerType.DOUBLE_POINTS, false);
        jokersUsed.put(JokerType.REMOVE_ANSWER, false);
        jokersUsed.put(JokerType.HALF_TIME, false);
    }

    /**
     * Gets called after scene has finished loading
     */
    protected void initialize() {
        doublePoints = false;
        doublePointsUsed = false;
        halfTime = false;
        halfTimeUsed = false;
        removeAnswer = false;
        removeAnswerUsed = false;
    }

    /**
     * This method is called from its subclasses when that scene is displayed
     */
    public void onShowScene() {
        notificationRenderer = new NotificationRenderer(notificationContainer);

        timeLeftSlider = (AnchorPane) timeLeftBar.getChildren().get(0);

        pointsGainedText.setVisible(false);
        answerBonusText.setVisible(false);
        timeBonusText.setVisible(false);

        if (Main.gameMode == GameMode.SINGLEPLAYER)
            setScore(main.getSingleplayerGame().getScoreTotal());
        else setScore(main.getMultiplayerGame().getScoreTotal());

        nextQuestion.setVisible(false);

        jokerContainer = (AnchorPane) jokers.getParent();
        jokerContainer.setVisible(true);
        jokers.getChildren().forEach(joker -> {
            // Hide the tooltips by default
            ((AnchorPane) joker).getChildren().get(1).setVisible(false);
        });

        emoteContainer.setVisible(false);

        notificationContainer.setVisible(Main.gameMode == GameMode.MULTIPLAYER);

        for (Map.Entry<JokerType, Boolean> joker : jokersUsed.entrySet()) {
            if (joker.getValue()) {
                disableJoker(joker.getKey());
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
            ImageView jokerImage = (ImageView) joker.getChildren().get(0);
            AnchorPane tooltip = (AnchorPane) joker.getChildren().get(1);

            jokerImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (disabledJokers.contains(joker)) {
                    return;
                } else {
                    jokerUsed(JokerType.valueOf(joker.getId().toUpperCase()));
                    if (node == jokers.getChildren().get(0)) {
                        disableJoker(JokerType.DOUBLE_POINTS);
                        jokersUsed.replace(JokerType.DOUBLE_POINTS, true);
                        hideJokerTooltip(tooltip);
                        doublePoints = true;
                        main.getMultiplayerGame().sendJokerNotification(Main.USERNAME, JokerType.DOUBLE_POINTS);
                    } else if (node == jokers.getChildren().get(1)) {
                        disableJoker(JokerType.HALF_TIME);
                        jokersUsed.replace(JokerType.HALF_TIME, true);
                        hideJokerTooltip(tooltip);
                        halfTime = true;
                        main.getMultiplayerGame().sendJokerClickedToAllClients(JokerType.HALF_TIME, this.getClass());
                        main.getMultiplayerGame().sendJokerNotification(Main.USERNAME, JokerType.HALF_TIME);
                    } else if (node == jokers.getChildren().get(2)) {
                        disableJoker(JokerType.REMOVE_ANSWER);
                        jokersUsed.replace(JokerType.REMOVE_ANSWER, true);
                        hideJokerTooltip(tooltip);
                        removeAnswer = true;
                        main.getMultiplayerGame().sendJokerNotification(Main.USERNAME, JokerType.REMOVE_ANSWER);
                    }

                    jokerClicked = true;
                }
            });

            jokerImage.setOnMouseExited(event -> {
                if (!jokerClicked && jokersUsed.get(JokerType.valueOf(joker.getId().toUpperCase())))
                    return;

                AnimationUtil.fadeAnim(joker, new Color(1, 1, 1, 1), new Color(0.266, 0.266, 0.266, 1), 200, 10).play();
                hideJokerTooltip(tooltip);
                jokerClicked = false;
            });

            jokerImage.setOnMouseEntered(event -> {
                if (jokersUsed.get(JokerType.valueOf(joker.getId().toUpperCase())))
                    return;

                AnimationUtil.fadeAnim(joker, new Color(0.266, 0.266, 0.266, 1), new Color(1, 1, 1, 1), 200, 10).play();
                showJokerTooltip(tooltip);

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
    protected void jokerUsed(JokerType type) {
        LoggerUtil.infoInline("Clicked on the " + type + " joker.");
    }

    /**
     * Can be used to disallow a user form using a specific joker
     *
     * @param type The joker to disable
     */
    protected void disableJoker(JokerType type) {
        jokers.getChildren().stream()
                .filter(joker -> joker.getId().equalsIgnoreCase(type.toString()))
                .map(joker -> (AnchorPane) joker)
                .forEach(joker -> {
                    LoggerUtil.log(joker.getId());
                    ImageView image = (ImageView) joker.getChildren().get(0);

                    ColorAdjust effect = new ColorAdjust();
                    effect.setBrightness(-0.5);
                    effect.setContrast(-0.5);
                    effect.setSaturation(-1);

                    image.setEffect(effect);

                    disabledJokers.add(joker);
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

        Iterator<Boolean> history;
        if (Main.gameMode == GameMode.MULTIPLAYER)
            history = main.getMultiplayerGame().getQuestionHistory().iterator();
        else history = main.getSingleplayerGame().getQuestionHistory().iterator();

        if (numberOfQuestions == -1) {
            for (int i = 0; i < 20; i++) {
                Circle circle = generateCircle(Paint.valueOf("#2b2b2b"), dropShadow);
                children.add(circle);
            }
        } else {
            Circle circle;
            for (int i = numberOfQuestions; i < 20 + numberOfQuestions; i++) {
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
        numberOfQuestions++;
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
        jokerContainer.setVisible(Main.gameMode == GameMode.MULTIPLAYER);
        notificationContainer.setVisible(Main.gameMode == GameMode.MULTIPLAYER);
        //emoteContainer.setVisible(Main.gameMode == GameMode.MULTIPLAYER);

        timer = AnimationUtil.timerAnim(timeLeftSlider, timeLeft, timeMultiplier, timeLeftText);

        timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.160, 0.729, 0.901, 1), new CornerRadii(50), Insets.EMPTY)));
        timeMultiplier = 1d;
        timer.playFromStart();
        onTimerEnd();
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

        timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.925, 0.552, 0.035, 1), new CornerRadii(6), Insets.EMPTY)));
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
        if (doublePoints == true && doublePointsUsed == false && !disabledJokers.contains(JokerType.DOUBLE_POINTS)) {
            total *= 2;
            doublePointsUsed = true;
            disableJoker(JokerType.DOUBLE_POINTS);
        }
        if (Main.gameMode == GameMode.MULTIPLAYER) {
            main.getMultiplayerGame().addToScore(total);
            setScore(main.getMultiplayerGame().getScoreTotal());
        } else {
            main.getSingleplayerGame().addToScore(total);
            setScore(main.getSingleplayerGame().getScoreTotal());
        }
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

    /**
     * Rounds the image
     * Source: https://stackoverflow.com/a/56303884/9957954
     *
     * @param imageView imageView in which the image will be viewed
     */
    public void setRoundedImage(ImageView imageView) {
        Rectangle clip = new Rectangle(
                imageView.getFitWidth(), imageView.getFitHeight()
        );
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        imageView.setClip(clip);

        // snapshot the rounded image.
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        // remove the rounding clip so that our effect can show through.
        imageView.setClip(null);

        // apply a shadow effect.
        imageView.setEffect(new DropShadow(40, Color.BLACK));

        // store the rounded image in the imageView.
        imageView.setImage(image);
    }

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
        alert.setHeaderText("You're about to leave this game!");
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
