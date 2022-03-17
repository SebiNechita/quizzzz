package client.scenes;

//import client.Main;

import client.utils.OnShowScene;
import client.utils.ServerUtils;
import packets.GameResponsePacket;
import commons.questions.Question;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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
        super.answer = answer;

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

    @OnShowScene
    public void onShowScene() {
        super.onShowScene();

        //for now we can only look at the MC questions functionality


        for (int i = 0; i < 3; i++) {
            options[i] = (AnchorPane) optionsContainer.getChildren().get(i);
        }

        retrieveMultipleChoiceQuestion();


        generateProgressDots();
        enableListeners();

        //----- TODO: Everything below this is temporary and for testing/displaying purposes -----
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removeOption(0);
            }
        }, 3000);
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

                hoverAnim(option, new Color(0.698, 0.792, 0.921, 1), false).play();
            });

            option.setOnMouseExited(event -> {
                if (locked[index] || selected != null && selected.getValue() == option)
                    return;

                hoverAnim(option, new Color(0.698, 0.792, 0.921, 1), true).play();
            });

            option.setOnMouseClicked(event -> {
                if (locked[index])
                    return;

                if (selected != null && !locked[selected.getKey()]) {
                    selectedAnim(selected.getValue(), false).play();
                }

                selected = new Pair<>(index, option);
                selectedAnim(option, true).play();

                lastAnswerChange = timeLeft;
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

        fadeOption(correct, (Color) correct.getBackground().getFills().get(0).getFill(), new Color(0.423, 0.941, 0.415, 1)).play();

        for (AnchorPane option : options) {
            if (option == correct)
                continue;

            if (selected != null && selected.getValue() != removedAnswer && option == selected.getValue()) {
                fadeOption(option, (Color) option.getBackground().getFills().get(0).getFill(), new Color(0.949, 0.423, 0.392, 1)).play();
            } else {
                fadeOption(option, (Color) option.getBackground().getFills().get(0).getFill(), new Color(0.478, 0.478, 0.478, 1)).play();
            }
        }

        boolean correctlyAnswered = selected != null && selected.getKey() == answer;
        showPointsGained(correctlyAnswered ? 100 : 0);
        questionHistory.add(correctlyAnswered);
        generateProgressDots();
    }

    /**
     * Hides point info, gets a new question, resets the options' appearance and the timer.
     */
    protected void goToNextQuestion() {
        hidePointsGained();

        //for now we can only look at the MC questions functionality
        retrieveMultipleChoiceQuestion();

        locked = new boolean[]{false, false, false};
        selected = null;

        for (AnchorPane option : options) {
            fadeOption(option, (Color) option.getBackground().getFills().get(0).getFill(), new Color(1, 1, 1, 1)).play();
        }

        startTimer();
    }
    private void setImages(Question q1,Question q2,Question q3){
        image1.setImage(server.getImage(q1.getAnswer().getImage_path()));
        image2.setImage(server.getImage(q2.getAnswer().getImage_path()));
        image3.setImage(server.getImage(q3.getAnswer().getImage_path()));
    }
    /**
     * Gets the next question.
     */
    protected void retrieveMultipleChoiceQuestion() {
        GameResponsePacket game = server.getRequest("api/game/create", GameResponsePacket.class);
        // question.setText(game.getMultipleChoiceQuestions().get(0).getQuestion());
        Question q = game.getMultipleChoiceQuestions().get(0);
        // this does not give the certainty that all answers are different
        // there might be case where 2 answers are the same or 2 answers
        // from different questions to be the same
        GameResponsePacket game2 = server.getRequest("api/game/create", GameResponsePacket.class);
        question.setText(q.getQuestion());
        int min = 50;
        int max = 100;
        //Generate random int value from 50 to 100
        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min) % 3;
        answer = random_int;
        if (random_int == 0) {
            choice1.setText(q.getAnswer().getTitle());
            choice2.setText(game2.getMultipleChoiceQuestions().get(0).getAnswer().getTitle());
            choice3.setText(game2.getMultipleChoiceQuestions().get(1).getAnswer().getTitle());
            setImages(q,game2.getMultipleChoiceQuestions().get(0),game2.getMultipleChoiceQuestions().get(1));
        } else {
            if (random_int == 1) {
                choice2.setText(q.getAnswer().getTitle());
                choice1.setText(game2.getMultipleChoiceQuestions().get(0).getAnswer().getTitle());
                choice3.setText(game2.getMultipleChoiceQuestions().get(1).getAnswer().getTitle());
                setImages(game2.getMultipleChoiceQuestions().get(0),q,game2.getMultipleChoiceQuestions().get(1));

            } else {
                choice3.setText(q.getAnswer().getTitle());
                choice1.setText(game2.getMultipleChoiceQuestions().get(0).getAnswer().getTitle());
                choice2.setText(game2.getMultipleChoiceQuestions().get(1).getAnswer().getTitle());
                setImages(game2.getMultipleChoiceQuestions().get(0),game2.getMultipleChoiceQuestions().get(1),q);

            }
        }
    }

    /**
     * Gets the next question.
     */
    protected void retrieveOpenQuestion() {
        GameResponsePacket game = server.getRequest("api/game/create", GameResponsePacket.class);
        question.setText(game.getOpenQuestions().get(0).getQuestion());
    }

    /**
     * Removes a certain option by graying it out and making it non-clickable
     *
     * @param option The index of the option to remove
     */
    private void removeOption(int option) {
        locked[option] = true;

        removedAnswer = options[option];
        fadeOption(removedAnswer, (Color) removedAnswer.getBackground().getFills().get(0).getFill(), new Color(0.478, 0.478, 0.478, 1)).play();
    }

    /**
     * Sets the image of one of the options
     *
     * @param option The option to set it for
     * @param image  The image to set
     */
    protected void setImage(int option, Image image) {
        ImageView imageView = (ImageView) options[option].getChildren().get(0);
        imageView.setImage(image);
    }

    /**
     * Sets the option's color from "hover blue" to "selected blue" (or from "hover blue" to white if inverted is false)
     * 8
     *
     * @param anchorPane The pane to animate
     * @param inverted   If the animation needs to be reversed or not
     * @return The animation object which can be played
     */
    private Animation selectedAnim(AnchorPane anchorPane, boolean inverted) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(200));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                if (inverted) {
                    anchorPane.setBackground(new Background(new BackgroundFill(lerp(0.698, 0.792, 0.921, 0.243, 0.505, 0.878, frac), new CornerRadii(10), Insets.EMPTY)));
                } else {
                    anchorPane.setBackground(new Background(new BackgroundFill(lerp(0.698, 0.792, 0.921, 1, 1, 1, frac), new CornerRadii(10), Insets.EMPTY)));
                }
            }
        };
    }

    /**
     * Animates the options the user gets
     *
     * @param anchorPane The pane to animate
     * @param start      The color to start from
     * @param target     The color to end with
     * @return The animation object which can be played
     */
    private Animation fadeOption(AnchorPane anchorPane, Color start, Color target) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(350));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setBackground(new Background(new BackgroundFill(lerp(start.getRed(), start.getGreen(), start.getBlue(), target.getRed(), target.getGreen(), target.getBlue(), frac), new CornerRadii(10), Insets.EMPTY)));
            }
        };
    }

    /**
     * Detects when the "Next Question" button has been pressed
     */
    @FXML
    private void onNextButton() {
        // make the "Next" button invisible after clicking
        nextQuestion.setVisible(false);
        // go to next question
        goToNextQuestion();
    }
}

