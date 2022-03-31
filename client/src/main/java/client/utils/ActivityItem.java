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
     * path of the image in the server
     */
    private String imagePath;

    public ActivityItem(String id, ImageView image, ImageView edit, long consumption, String source, String title, String imagePath) {
        this.id = id;
        this.image = image;
        this.edit = edit;
        this.consumption = consumption;
        this.source = source;
        this.title = title;
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public ImageView getImage() {
        return image;
    }

    public ImageView getEdit() {
        return edit;
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

    public String getImagePath() {
        return imagePath;
    }
}
