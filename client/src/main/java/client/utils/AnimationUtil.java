package client.utils;

import com.google.common.util.concurrent.AtomicDouble;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AnimationUtil {

    /**
     * Transitions from a specified start color to a specified end color
     *
     * @param anchorPane   The pane which to change the color of
     * @param start        The color to start from
     * @param end          The color to go to
     * @param millis       The time that this animation should take
     * @param cornerRadius The radius of the corner of the pane
     * @return The animation object which can be played
     */
    public static Animation fadeAnim(AnchorPane anchorPane, Color start, Color end, int millis, int cornerRadius) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(millis));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setBackground(new Background(new BackgroundFill(lerp(start.getRed(), start.getGreen(), start.getBlue(), end.getRed(), end.getGreen(), end.getBlue(), frac), new CornerRadii(cornerRadius), Insets.EMPTY)));
            }
        };
    }

    /**
     * Animates the timer to fill up its bar
     *
     * @param anchorPane     The pane which to scroll
     * @param timeLeft       The amount of time left
     * @param totalTime      The total amount of time the timer should take
     * @param timeMultiplier The multiplier of the time
     * @param timeLeftText   The text field which displays the time
     * @param textPrefix     The prefix of the text that will be shown when counting down
     * @return The animation object which can be played
     */
    public static Animation timerAnim(AnchorPane anchorPane, AtomicDouble timeLeft, double totalTime, double timeMultiplier, Text timeLeftText, String textPrefix) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(totalTime * timeMultiplier));
                setInterpolator(Interpolator.LINEAR);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setPrefWidth(25 + 475 * frac);
                timeLeft.set(timeMultiplier * (1 - frac));
                timeLeftText.setText(textPrefix + " " + (Math.round(100 * timeLeft.get()) / 10d) + "s");
            }
        };
    }

    /**
     * Animates the text to count up when the player is displayed their points
     *
     * @param totalPoints      The amount of points to show for the total text
     * @param answerPoints     The amount of points to show for the answer text
     * @param timePoints       The amount of points to show for the timer text
     * @param pointsGainedText The text field which shows the points gained in total
     * @param answerBonusText  The text field which shows the points gained for the answer
     * @param timeBonusText    The text field which shows the points gained as a time bonus
     * @return The animation object which can be played
     */
    public static Animation pointsAnim(int totalPoints, int answerPoints, int timePoints, Text pointsGainedText, Text answerBonusText, Text timeBonusText) {
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
     * Animates the input field of the user
     *
     * @param textField The input field to animate
     * @param start     The color to start from
     * @param target    The color to end with
     * @return The animation object which can be played
     */
    public static Animation fadeTextField(TextField textField, Color start, Color target) {
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
    private static Color lerp(double r1, double g1, double b1, double r2, double g2, double b2, double frac) {
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
    private static int lerp(int start, int end, double time) {
        return (int) Math.round(start + (end - start) * time);
    }
}
