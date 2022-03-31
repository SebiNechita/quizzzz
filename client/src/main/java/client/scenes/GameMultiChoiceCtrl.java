package client.scenes;

import client.Main;
import client.utils.AnimationUtil;
import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.questions.Activity;
import commons.questions.MultipleChoiceQuestion;
import commons.utils.GameMode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import javax.inject.Inject;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameMultiChoiceCtrl extends GameCtrl {
    @FXML
    private Text choice1;
    @FXML
    private Text choice2;
    @FXML
    private Text choice3;

    @FXML
    private HBox progressBar;
    @FXML
    private Text score;
    @FXML
    private Text question;

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
    private HBox optionsContainer;
    private final AnchorPane[] options = new AnchorPane[3];
    private Pair<Integer, AnchorPane> selected = null;

    private boolean[] locked = {false, false, false};
    private AnchorPane removedAnswer = null;

    @FXML
    private VBox notificationContainer;

    @FXML
    private HBox emoteContainer;

    @FXML
    private ImageView image1;
    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;


    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl    The parent class, which keeps track of all scenes
     * @param serverUtils The server utils, for communicating with the server
     */
    @Inject
    public GameMultiChoiceCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
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
     *                  the root object was not localized.
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

        for (int i = 0; i < 3; i++) {
            options[i] = (AnchorPane) optionsContainer.getChildren().get(i);
        }

        enableListeners();

        retrieveMultipleChoiceQuestion();
    }

    /**
     * Sets up all the listeners
     */
    private void enableListeners() {
        for (int i = 0; i < options.length; i++) {
            AnchorPane option = options[i];
            final int index = i;
            option.setOnMouseEntered(event -> {
                if (locked[index] || selected != null && selected.getValue() == option)
                    return;

                AnimationUtil.fadeAnim(option, new Color(1, 1, 1, 1), new Color(0.698, 0.792, 0.921, 1), 200, 40).play();
            });

            option.setOnMouseExited(event -> {
                if (locked[index] || selected != null && selected.getValue() == option)
                    return;

                AnimationUtil.fadeAnim(option, new Color(0.698, 0.792, 0.921, 1), new Color(1, 1, 1, 1), 200, 40).play();
            });

            option.setOnMouseClicked(event -> {
                if (locked[index])
                    return;

                if (selected != null && !locked[selected.getKey()]) {
                    AnimationUtil.fadeAnim(selected.getValue(), new Color(0.698, 0.792, 0.921, 1), new Color(1, 1, 1, 1), 200, 40).play();
                }

                selected = new Pair<>(index, option);
                AnimationUtil.fadeAnim(option, new Color(0.698, 0.792, 0.921, 1), new Color(0.243, 0.505, 0.878, 1), 200, 40).play();

                lastAnswerChange = timeLeft.get();
            });
        }

        AnchorPane joker = (AnchorPane) jokers.getChildren().get(2);
        ImageView jokerImage = (ImageView) joker.getChildren().get(0);
    }

    /**
     * When the time's up, shows the correct answer and makes Next visible
     */
    protected void onTimerEnd() {
        if (Main.gameMode == GameMode.MULTIPLAYER) {
            timer.setOnFinished(event -> {
                showCorrectAnswer(answerOptionNumber);
                nextQuestion.setVisible(Main.gameMode == GameMode.MULTIPLAYER);
            });
        } else {
            timer.setOnFinished(event -> {
                showCorrectAnswer(answerOptionNumber);
                nextQuestion.setVisible(Main.gameMode == GameMode.SINGLEPLAYER);
            });
        }
    }

    /**
     * Shows the correct answer to the user
     *
     * @param answer The correct answer (in case of multi-choice, the index of which of the options that is)
     */
    protected void showCorrectAnswer(int answer) {
        locked = new boolean[]{true, true, true};

        AnchorPane correct = options[answer];

        AnimationUtil.fadeAnim(correct, (Color) correct.getBackground().getFills().get(0).getFill(), new Color(0.423, 0.941, 0.415, 1), 350, 40).play();

        playSound(selected != null && selected.getValue() == correct);

        for (AnchorPane option : options) {
            if (option == correct) {
                continue;
            }

            if (selected != null && selected.getValue() != removedAnswer && option == selected.getValue()) {
                AnimationUtil.fadeAnim(option, (Color) option.getBackground().getFills().get(0).getFill(), new Color(0.949, 0.423, 0.392, 1), 350, 40).play();
            } else {
                AnimationUtil.fadeAnim(option, (Color) option.getBackground().getFills().get(0).getFill(), new Color(0.478, 0.478, 0.478, 1), 350, 40).play();
            }
        }

        boolean correctlyAnswered = selected != null && selected.getKey() == answer;
        showPointsGained(correctlyAnswered ? 100 : 0);

        if (Main.gameMode == GameMode.MULTIPLAYER) {
            main.getMultiplayerGame().getQuestionHistory().add(correctlyAnswered);
        } else {
            main.getSingleplayerGame().getQuestionHistory().add(correctlyAnswered);
        }

        generateProgressDots();
    }

    /**
     * Hides point info and next button, gets a new question, resets the options' appearance and the timer.
     */
    @FXML
    protected void initialiseNextQuestion() {
        super.initialiseNextQuestion();

        locked = new boolean[]{false, false, false};
        selected = null;

        for (AnchorPane option : options) {
            AnimationUtil.fadeAnim(option, (Color) option.getBackground().getFills().get(0).getFill(), new Color(1, 1, 1, 1), 350, 40).play();
        }
    }

    /**
     * Gets the next question.
     */
    protected void retrieveMultipleChoiceQuestion() {
        MultipleChoiceQuestion mcq;
        if (Main.gameMode == GameMode.SINGLEPLAYER) {
            mcq = main.getSingleplayerGame().getCurrentQuestion(MultipleChoiceQuestion.class);
        } else {
            mcq = main.getMultiplayerGame().getCurrentQuestion(MultipleChoiceQuestion.class);
        }
        Activity answer = mcq.getAnswer();
        Activity option2 = mcq.getActivityList().get(0); // An option that is not the answer
        Activity option3 = mcq.getActivityList().get(1); // Another option that is not the answer

        // Generates random int value from 0 to 3
        Random randomGen = new Random();
        answerOptionNumber = randomGen.nextInt(3);

        // This is to ensure that the answers are in different options and are not predictable by the user
        switch (answerOptionNumber) {
            case 0 -> setUpQuestion(mcq.getQuestion(), answer, option2, option3);
            case 1 -> setUpQuestion(mcq.getQuestion(), option2, answer, option3);
            case 2 -> setUpQuestion(mcq.getQuestion(), option2, option3, answer);
        }

    }

    /**
     * This method sets the texts and images in the options
     *
     * @param questionText the question text
     * @param option1      Activity of the text and image that'll be set in the first option
     * @param option2      Activity of the text and image that'll be set in the second option
     * @param option3      Activity of the text and image that'll be set in the third option
     */
    public void setUpQuestion(String questionText, Activity option1, Activity option2, Activity option3) {
        question.setText(questionText);

        choice1.setText(option1.getTitle());
        choice2.setText(option2.getTitle());
        choice3.setText(option3.getTitle());

        image1.setImage(server.getImage(option1.getImage_path()));
        image2.setImage(server.getImage(option2.getImage_path()));
        image3.setImage(server.getImage(option3.getImage_path()));

        setRoundedImage(image1);
        setRoundedImage(image2);
        setRoundedImage(image3);
    }

    /**
     * Removes a wrong answer
     */
    protected void removeWrongAnswer() {
        int[] wrongOptions = new int[2];
        int pos = 0;

        for (int i = 0; i < 3; ++i) {
            if (i != answerOptionNumber) {
                wrongOptions[pos++] = i;
            }
        }

        int num = new Random().nextInt(2);
        removeOption(wrongOptions[num]);
    }

    /**
     * Removes a certain option by graying it out and making it non-clickable
     *
     * @param option The index of the option to remove
     */
    private void removeOption(int option) {
        locked[option] = true;

        removedAnswer = options[option];
        AnimationUtil.fadeAnim(removedAnswer, (Color) removedAnswer.getBackground().getFills().get(0).getFill(), new Color(0.478, 0.478, 0.478, 1), 350, 40).play();
    }
}

