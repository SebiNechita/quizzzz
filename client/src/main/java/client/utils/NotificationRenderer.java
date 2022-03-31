package client.utils;


import commons.utils.Emote;
import commons.utils.JokerType;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Utility class for rendering the notifications during the game.
 */
public class NotificationRenderer {
    private final VBox notificationContainer;
    private final Queue<AnchorPane> notifications = new LinkedList<>();
    private Animation fadingOut = null;

    /**
     * Constructs an object and automatically clears out the notification panel.
     *
     * @param notificationContainer The container which will hold all notifications
     */
    public NotificationRenderer(VBox notificationContainer) {
        this.notificationContainer = notificationContainer;
        notificationContainer.getChildren().clear();
    }

    /**
     * Adds a notification for when a user uses an emote
     *
     * @param username The username who sent the emote
     * @param emote    The emote which was sent
     */
    public void addEmoteNotification(String username, Emote emote) {
        renderNotification(generateNotification(username + " reacted with:", new Color(1, 1, 1, 1), false, emote));
    }

    /**
     * Adds a notification for when a user disconnects
     *
     * @param username The username who disconnected
     */
    public void addDisconnectNotification(String username) {
        renderNotification(generateNotification(username + " has disconnected", new Color(0.949, 0.423, 0.392, 1), false));
    }

    /**
     * Adds a notification for when a user uses a joker
     *
     * @param username The username who used the joker
     * @param type     The type of joker used
     */
    public void addJokerNotification(String username, JokerType type) {
        renderNotification(generateNotification(username + " has used a " + type.getName() + " joker!", new Color(0.541, 0.929, 1, 1), true));
    }

    /**
     * Generates a notification
     *
     * @param text      The text to show in the notification
     * @param textColor The color which the text should be
     * @param bold      If the text should be bold or not
     * @return The generated AnchorPane which contains the notification
     */
    private AnchorPane generateNotification(String text, Paint textColor, boolean bold) {
        return generateNotification(text, textColor, bold, null);
    }

    /**
     * Generates a notification
     *
     * @param text      The text to show in the notification
     * @param textColor The color which the text should be
     * @param bold      If the text should be bold or not
     * @param emote     The emote that can be shown
     * @return The generated AnchorPane which contains the notification
     */
    private AnchorPane generateNotification(String text, Paint textColor, boolean bold, Emote emote) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(200);
        anchorPane.setPrefHeight(80);
        anchorPane.setBackground(new Background(new BackgroundFill(new Color(0.231, 0.231, 0.231, 0.8), new CornerRadii(8), Insets.EMPTY)));

        Text title = new Text();
        title.setLayoutX(6);
        title.setLayoutY(22);
        title.setWrappingWidth(200);

        title.setText(text);
        title.setFill(textColor);

        title.setFont(Font.font("Comic Sans MS", bold ? FontWeight.BOLD : FontWeight.NORMAL, 18));

        anchorPane.getChildren().add(title);

        if (emote != null) {
            ImageView image = new ImageView("@../../img/emojis/" + emote.toString().toLowerCase() + ".png");
            image.setFitHeight(45);
            image.setFitWidth(45);
            image.setLayoutX(140);
            image.setLayoutY(30);

            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(0);
            image.setEffect(colorAdjust);

            anchorPane.getChildren().add(image);
        }

        return anchorPane;
    }

    /**
     * Renders a given notification in the form of an AnchorPane
     *
     * @param notification The AnchorPane to render
     */
    private void renderNotification(AnchorPane notification) {
        ObservableList<Node> children = notificationContainer.getChildren();
        children.add(notification);
        notifications.add(notification);

        if (notifications.size() > 3) {
            if (fadingOut != null) {
                fadingOut.jumpTo(Duration.millis(600));
            }

            AnchorPane target = notifications.poll();
            if (target != null) {
                fadingOut = fadeOut(target);
                fadingOut.setOnFinished(event -> {
                    children.remove(target);
                });
                fadingOut.playFromStart();
            }
        }
    }

    /**
     * Animates the notification to fade out
     *
     * @param anchorPane The AnchorPane to apply the animation to
     * @return The animation object which can be played
     */
    private Animation fadeOut(AnchorPane anchorPane) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(600));
                setInterpolator(Interpolator.EASE_BOTH);
            }

            final Color color = (Color) anchorPane.getBackground().getFills().get(0).getFill();
            final Text text = (Text) anchorPane.getChildren().get(0);
            final Color textColor = (Color) text.getFill();

            @Override
            protected void interpolate(double frac) {
                anchorPane.setBackground(new Background(new BackgroundFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1 - frac), new CornerRadii(10), Insets.EMPTY)));
                text.setFill(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 1 - frac));
            }
        };
    }
}