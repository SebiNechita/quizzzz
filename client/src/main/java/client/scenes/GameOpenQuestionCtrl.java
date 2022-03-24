package client.scenes;

import client.Main;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.questions.Question;
import commons.utils.GameMode;
import commons.utils.JokerType;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

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

    private Question q;

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

        disableJoker(JokerType.REMOVE_ANSWER);

        userInput.setDisable(false);

        displayQuestion();

        enableListeners();
    }

    /**
     * Gets the current question and displays it.
     */
    private void displayQuestion() {
        q = Main.openQuestions.poll();
        setQuestion(q.getQuestion());
        setActivityImage(q.getAnswer().getImage_path());
        System.out.println(q.getAnswerInWH());
    }

    /**
     * Sets the ImageView for the open question
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
    //initialising includes loading the next question, but also cleaning up the screen
    //TODO: Create a super method for this, because the first three lines are the same
    // for both types of questions.
    protected void initialiseNextQuestion() {
        userInput.clear();
        resetTextInputColor();
        nextQuestion.setVisible(false);
        hidePointsGained();

        main.jumpToNextQuestion();


    }

    /**
     * Sets up all the listeners
     */
    private void enableListeners() {
        userInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                userInput.setText(newValue.replaceAll("\\D", ""));
            } else {
                lastAnswerChange = timeLeft;
            }
        });
    }

    /**
     * When the time's up, shows the correct answer and makes Next visible
     */
    protected void onTimerEnd(){
        timer.setOnFinished(event -> {
            showCorrectAnswer((int) q.getAnswerInWH());
            nextQuestion.setVisible(Main.gameMode == GameMode.SINGLEPLAYER);
        });
    }

    /**
     * Shows the correct answer to the user
     *
     * @param answer The correct answer (in case of multi-choice, the index of which of the options that is)
     */
    @Override
    protected void showCorrectAnswer(int answer) {
        userInput.setDisable(true);
        int difference = userInput.getText().equals("") ? 100 : Math.abs(Integer.parseInt(userInput.getText()) - answer);
        Color current = (Color) userInput.getBackground().getFills().get(0).getFill();
        if (difference <= 10) {
            fadeTextField(userInput, current, new Color(0.423, 0.941, 0.415, 1)).play();
        } else if (difference <= 80) {
            fadeTextField(userInput, current, new Color(1, 0.870, 0.380, 1)).play();
        } else {
            fadeTextField(userInput, current, new Color(0.949, 0.423, 0.392, 1)).play();
        }

        showPointsGained(100 - difference);
        Main.questionHistory.add(difference <= 50);
        generateProgressDots();
    }

    /**
     * Resets the color of the text input field for the next question
     */
    private void resetTextInputColor() {
        userInput.setBackground(new Background(new BackgroundFill(Color.color(1,1,1,1), new CornerRadii(10), Insets.EMPTY)));
    }

    /**
     * Detects when the "Next Question" button has been pressed
     */
    @FXML
    private void onNextButton() {
    }


    /**
     * Animates the input field of the user
     *
     * @param textField The input field to animate
     * @param start     The color to start from
     * @param target    The color to end with
     * @return The animation object which can be played
     */
    private Animation fadeTextField(TextField textField, Color start, Color target) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(350));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                textField.setBackground(new Background(new BackgroundFill(lerp(start.getRed(), start.getGreen(), start.getBlue(), target.getRed(), target.getGreen(), target.getBlue(), frac), new CornerRadii(50), Insets.EMPTY)));
            }
        };
    }
}
