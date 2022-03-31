package client.utils;

import javafx.scene.image.ImageView;

public class ActivityItem {

    /**
     * ID of the activity
     */
    private String id;

    /**
     * view button
     */
    private ImageView image;

    /**
     * edit button
     */
    private final ImageView edit;

    /**
     * Energy consumed in WH
     */
    private long consumption;

    /**
     * Link to source
     */
    private String source;

    /**
     * Description of the activity
     */
    private String title;

    /**
     * constructor for ActivityItem
     *
     * @param id
     * @param image
     * @param consumption
     * @param source
     * @param title
     */
    public ActivityItem(String id, ImageView image, ImageView edit, long consumption, String source, String title) {
        this.id = id;
        this.image = image;
        this.edit = edit;
        this.consumption = consumption;
        this.source = source;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public ImageView getEdit() {
        return edit;
    }

    public ImageView getImage() {
        return image;
    }

    public long getConsumption() {
        return consumption;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }
}
