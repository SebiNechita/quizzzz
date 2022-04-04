package client.utils;

import javafx.scene.paint.Color;

/**
 * Utility class which keeps track of all the colors
 */
public final class ColorPresets {
    public static final Color white = new Color(1, 1, 1, 1);
    public static final Color light_gray = new Color(0.478, 0.478, 0.478, 1);
    public static final Color gray = new Color(0.266, 0.266, 0.266, 1);
    public static final Color dark_gray = new Color(0.168, 0.168, 0.168, 1);
    public static final Color black = new Color(0, 0, 0, 1);

    public static final Color red = new Color(0.909, 0.262, 0.262, 1);
    public static final Color green = new Color(0.109, 0.890, 0.098, 1);

    public static final Color soft_red = new Color(0.949, 0.423, 0.392, 1);
    public static final Color soft_yellow = new Color(1, 0.870, 0.380, 1);
    public static final Color soft_green = new Color(0.423, 0.941, 0.415, 1);
    public static final Color soft_blue = new Color(0.278, 0.776, 0.921, 1);

    public static final Color hover = new Color(0.698, 0.792, 0.921, 1);
    public static final Color selected = new Color(0.243, 0.505, 0.878, 1);

    public static final Color timer_bar_regular = new Color(0.160, 0.729, 0.901, 1);
    public static final Color timer_bar_rushed = new Color(0.925, 0.552, 0.035, 1);

    /**
     * Converts a JavaFX color to its hex
     *
     * @param color The color to convert
     * @return A color in hex format
     */
    public static String toHex(Color color) {
        int r = (int) Math.round(color.getRed() * 255.0);
        int g = (int) Math.round(color.getGreen() * 255.0);
        int b = (int) Math.round(color.getBlue() * 255.0);
        int o = (int) Math.round(color.getOpacity() * 255.0);
        return String.format("#%02x%02x%02x%02x", r, g, b, o);
    }
}
