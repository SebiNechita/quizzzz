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
    }

    private void enableListeners() {
        for (AnchorPane option : options) {
            option.setOnMouseEntered(event -> {
                if (selected == option)
                    return;

                hoverAnim(option, true).play();
            });

            option.setOnMouseExited(event -> {
                if (selected == option)
                    return;

                hoverAnim(option, false).play();
            });

            option.setOnMouseClicked(event -> {
                if (selected != null) {
                    selectedAnim(selected, false).play();
                }

                selected = option;
                selectedAnim(selected, true).play();
            });
        }
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
                anchorPane.setBackground(new Background(new BackgroundFill(lerp(0.698, 0.792, 0.921, 0.243, 0.505, 0.878, inverted ? 1 - frac : frac), new CornerRadii(10), Insets.EMPTY)));
            }
        };
    }

    /**
     * Lerps color from white to the given color
     *
     * @param r The normalized red to go to
     * @param g The normalized green to go to
     * @param b The normalized blue to go to
     * @param frac The time of the lerp
     * @return An interpolated color
     */
    private Color lerp(double r, double g, double b, double frac) {
        return new Color(r + ((1 - r) * frac), g + ((1 - g) * frac), b + ((1 - b) * frac), 1);
    }

    /**
     * Lerps color from a given color to a given color
     *
     * @param r1 The normalized start color's red to go to
     * @param g1 The normalized start color's green to go to
     * @param b1 The normalized start color's blue to go to
     * @param r2 The normalized end color's red to go to
     * @param g2 The normalized end color's green to go to
     * @param b2 The normalized end color's blue to go to
     * @param frac The time of the lerp
     * @return An interpolated color
     */
    private Color lerp(double r1, double g1, double b1, double r2, double g2, double b2, double frac) {
        return new Color(r2 + ((r1 - r2) * frac), g2 + ((g1 - g2) * frac), b2 + ((b1 - b2) * frac), 1);
    }
}
