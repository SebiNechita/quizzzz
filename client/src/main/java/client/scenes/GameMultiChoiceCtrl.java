package client.scenes;

import client.utils.ServerUtils;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
    private HBox optionsContainer;
    private AnchorPane[] options = new AnchorPane[3];
    private AnchorPane selected = null;

    private boolean locked[] = {false, false, false};

    @Inject
    public GameMultiChoiceCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);

        super.progressBar = progressBar;
        super.score = score;
        super.question = question;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize();

        for (int i = 0; i < 3; i++) {
            options[i] = (AnchorPane) optionsContainer.getChildren().get(i);
        }

        generateProgressDots();
        enableListeners();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removeAnswer(0);
            }
        }, 3000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                showCorrectAnswer(1);
            }
        }, 6000);
    }

    private void enableListeners() {
        for (int i = 0; i < options.length; i++) {
            AnchorPane option = options[i];
            final int index = i;
            option.setOnMouseEntered(event -> {
                if (locked[index] || selected == option)
                    return;

                hoverAnim(option, false).play();
            });

            option.setOnMouseExited(event -> {
                if (locked[index] || selected == option)
                    return;

                hoverAnim(option, true).play();
            });

            option.setOnMouseClicked(event -> {
                if (locked[index])
                    return;

                if (selected != null) {
                    selectedAnim(selected, false).play();
                }

                selected = option;
                selectedAnim(selected, true).play();
            });
        }
    }

    private void showCorrectAnswer(int answer) {
        locked = new boolean[]{true, true, true};

        AnchorPane correct = options[answer];

        fadeOption(correct, (Color) correct.getBackground().getFills().get(0).getFill(), new Color(0.423, 0.941, 0.415, 1)).play();

        for (AnchorPane option : options) {
            if (option == correct)
                continue;

            if (option == selected) {
                fadeOption(option, (Color) option.getBackground().getFills().get(0).getFill(), new Color(0.949, 0.423, 0.392, 1)).play();
            } else {
                fadeOption(option, (Color) option.getBackground().getFills().get(0).getFill(), new Color(0.478, 0.478, 0.478, 1)).play();
            }
        }
    }

    private void removeAnswer(int answer) {
        locked[answer] = true;

        AnchorPane option = options[answer];
        fadeOption(option, (Color) option.getBackground().getFills().get(0).getFill(), new Color(0.478, 0.478, 0.478, 1)).play();
    }

    private Animation hoverAnim(AnchorPane anchorPane, boolean inverted) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(200));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setBackground(new Background(new BackgroundFill(lerp(0.698, 0.792, 0.921, inverted ? 1 - frac : frac), new CornerRadii(10), Insets.EMPTY)));
            }
        };
    }

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
     * Lerps color from white to the given color
     *
     * @param r    The normalized red to go to
     * @param g    The normalized green to go to
     * @param b    The normalized blue to go to
     * @param frac The time of the lerp
     * @return An interpolated color
     */
    private Color lerp(double r, double g, double b, double frac) {
        frac = 1 - frac;
        return new Color(r + ((1 - r) * frac), g + ((1 - g) * frac), b + ((1 - b) * frac), 1);
    }

    /**
     * Lerps color from a given color to a given color
     *
     * @param r1   The normalized start color's red to go to
     * @param g1   The normalized start color's green to go to
     * @param b1   The normalized start color's blue to go to
     * @param r2   The normalized end color's red to go to
     * @param g2   The normalized end color's green to go to
     * @param b2   The normalized end color's blue to go to
     * @param frac The time of the lerp
     * @return An interpolated color
     */
    private Color lerp(double r1, double g1, double b1, double r2, double g2, double b2, double frac) {
        frac = 1 - frac;
        return new Color(r2 + ((r1 - r2) * frac), g2 + ((g1 - g2) * frac), b2 + ((b1 - b2) * frac), 1);
    }
}
