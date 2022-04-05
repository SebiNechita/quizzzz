package client.scenes;

import client.Main;
import client.utils.AnimationUtil;
import client.utils.ColorPresets;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.questions.OpenQuestion;
import commons.utils.GameMode;
import commons.utils.JokerType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOpenQuestionCtrl extends GameCtrl {
    @FXML
    private HBox progressBar;
    @FXML
    private Text score;
    @FXML
    private Text question;
    @FXML
    private Text correctAnswer;
    @FXML
    private ImageView image;

    @FXML
    private Text pointsGainedText;
    @FXML
    private Text answerBonusText;
    @FXML
    private Text timeBonusText;

    @FXML
    private Button nextQuestion;

    @FXML
    private VBox jokers;

    @FXML
    protected Text timeLeftText;
    @FXML
    protected AnchorPane timeLeftBar;

    @FXML
    private TextField userInput;

    @FXML
    private VBox notificationContainer;

    @FXML
    private HBox emoteContainer;

    private OpenQuestion oq;


    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    public GameOpenQuestionCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);

        super.progressBar = progressBar;
        super.score = score;
        super.question = question;

        super.pointsGainedText = pointsGainedText;
        super.answerBonusText = answerBonusText;
        super.timeBonusText = timeBonusText;

        super.nextQuestion = nextQuestion;

        super.jokers = jokers;

        super.timeLeftText = timeLeftText;
        super.timeLeftBar = timeLeftBar;

        super.notificationContainer = notificationContainer;

        super.emoteContainer = emoteContainer;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Gets called when the scene is shown to the user
     */
    @OnShowScene
    public void onShowScene() {
        super.onShowScene();

        userInput.setDisable(false);

        displayQuestion();

        disableJoker(JokerType.REMOVE_ANSWER, true);
        enableListeners();
    }

    /**
     * Gets the current question and displays it.
     */
    private void displayQuestion() {

        oq = main.getGame(Main.gameMode).getCurrentQuestion(OpenQuestion.class);

        setQuestion(oq.getQuestion());
        correctAnswer.setVisible(false);
        setActivityImage(oq.getAnswer().getImage_path());
        System.out.println(oq.getAnswerInWH());
    }

    /**
     * Sets the ImageView for the open question
     *
     * @param imagePath path within the activity-bank
     */
    private void setActivityImage(String imagePath) {
        this.image.setImage(server.getImage(imagePath));
        setRoundedImage(this.image);
    }

    /**
     * Hides Next button and point info and jumps to next question
     */
    @FXML
    protected void initialiseNextQuestion() {
        super.initialiseNextQuestion();

        userInput.clear();
        resetTextInputColor();
    }

    /**
     * Sets up all the listeners
     */
    private void enableListeners() {
        userInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                userInput.setText(newValue.replaceAll("\\D", ""));
            } else {
                lastAnswerChange = timeLeft.get();
            }
        });
    }

    /**
     * When the time's up, shows the correct answer and makes Next visible
     */
    protected void onTimerEnd() {
        if (Main.gameMode == GameMode.MULTIPLAYER) {
            timer.setOnFinished(event -> {
                showCorrectAnswer((int) oq.getAnswerInWH());
                startWaitTimer();
            });

        } else {
            timer.setOnFinished(event -> {
                showCorrectAnswer((int) oq.getAnswerInWH());
                nextQuestion.setVisible(Main.gameMode == GameMode.SINGLEPLAYER);
            });
        }
    }

    protected void onWaitTimerEnd() {
        timer.setOnFinished(event -> {
            main.getCtrl(GameOpenQuestionCtrl.class).initialiseNextQuestion();
        });
    }

    /**
     * Shows the correct answer to the user and updates the score
     *
     * @param answer The correct answer (in case of multi-choice, the index of which of the options that is)
     */
    @Override
    protected void showCorrectAnswer(int answer) {
        userInput.setDisable(true);
        BigInteger rawInput = new BigInteger(userInput.getText());
        int convertedInput = rawInput.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0 ? Integer.MAX_VALUE : rawInput.intValue();
        double difference = userInput.getText().equals("") ? 0.5 : Math.abs(1 - convertedInput / answer);
        correctAnswer.setVisible(true);
        correctAnswer.setText("Answer: " + answer);

        Color current = (Color) userInput.getBackground().getFills().get(0).getFill();
        if (difference <= 0.1) {
            AnimationUtil.fadeTextField(userInput, current, ColorPresets.soft_green).play();
        } else if (difference <= 0.3) {
            AnimationUtil.fadeTextField(userInput, current, ColorPresets.soft_yellow).play();
        } else {
            AnimationUtil.fadeTextField(userInput, current, ColorPresets.soft_red).play();
        }

        int points = (difference <= 0.3) ? (int) (100.0 * (1.0 - difference)) : 0;
        showPointsGained(points);

        main.getGame(Main.gameMode).getQuestionHistory().add(difference <= 0.3);
        playSound(difference <= 0.3);
        

        generateProgressDots();
    }

    /**
     * Resets the color of the text input field for the next question
     */
    private void resetTextInputColor() {
        userInput.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 1, 1), new CornerRadii(10), Insets.EMPTY)));
    }
}
