package client.utils;

import commons.utils.Emote;
import commons.utils.LoggerUtil;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/* --TODO: Could be changed to an AnimationUtility class and all the methods related to that can be moved in here  */
public class EmoteUtility {
    /**
     * Transitions the brightness of the given ImageView
     *
     * @param imageView The element to affect
     * @param inverted  If the animation needs to be reversed or not
     * @return The animation object which can be played
     */
    public static Animation emoteHoverAnim(ImageView imageView, boolean inverted) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(150));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            @Override
            protected void interpolate(double frac) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.3 * (inverted ? frac : 1 - frac));
                imageView.setEffect(colorAdjust);
            }
        };
    }

    /**
     * Gets called when an emote is used
     *
     * @param emote What emote has been used
     */
    public static void emoteUsed(Emote emote) {
        LoggerUtil.infoInline("Clicked on the " + emote.name() + " emote.");
    }
}
