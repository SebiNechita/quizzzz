package client.scenes;

import client.utils.ServerUtils;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
    private AnchorPane jokerTooltip;

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

    /**
     * Constructor for this Ctrl
     *
     * @param mainCtrl The parent class, which keeps track of all scenes
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
        super.jokerTooltip = jokerTooltip;

        super.timeLeftText = timeLeftText;
        super.timeLeftBar = timeLeftBar;

        super.notificationContainer = notificationContainer;

        super.emoteContainer = emoteContainer;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize();

        for (int i = 0; i < 3; i++) {
            options[i] = (AnchorPane) optionsContainer.getChildren().get(i);
        }

        generateProgressDots();
        enableListeners();

        //----- TODO: Everything below this is temporary and for testing/displaying purposes -----
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removeAnswer(0);
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

        showPointsGained((selected != null && selected.getKey() == answer) ? 100 : 0);
    }

    /**
     * Removes a certain answer by graying it out and making it non-clickable
     *
     * @param answer The index of the answer to remove
     */
    private void removeAnswer(int answer) {
        locked[answer] = true;

        removedAnswer = options[answer];
        fadeOption(removedAnswer, (Color) removedAnswer.getBackground().getFills().get(0).getFill(), new Color(0.478, 0.478, 0.478, 1)).play();
    }

    /**
     * Animates the option the user chose
     8
     * @param anchorPane The pane to animate
     * @param inverted If the animation needs to be reversed or not
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
     * @param start The color to start from
     * @param target The color to end with
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
        super.goToNextQuestion();
    }
}
