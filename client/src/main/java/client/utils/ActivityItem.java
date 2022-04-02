package client.utils;

import javafx.scene.image.ImageView;

import java.util.Objects;

public class ActivityItem {

    /**
     * ID of the activity
     */
    private String id;

    /**
     * view button
     */
    private final ImageView image;

    /**
     * edit button
     */
    private final ImageView edit;

    /**
     * delete button
     */
    private final ImageView delete;

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

    public ActivityItem(String id, ImageView image, ImageView edit, ImageView delete, long consumption, String source, String title, String imagePath) {
        this.id = id;
        this.image = image;
        this.edit = edit;
        this.delete = delete;
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

    public ImageView getDelete() {
        return delete;
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

    @Override
    public String toString() {
        return "ActivityItem{" +
                "id='" + id + '\'' +
                ", image=" + image +
                ", edit=" + edit +
                ", delete=" + delete +
                ", consumption=" + consumption +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityItem that = (ActivityItem) o;
        return consumption == that.consumption && Objects.equals(id, that.id) && Objects.equals(image, that.image) && Objects.equals(edit, that.edit) && Objects.equals(delete, that.delete) && Objects.equals(source, that.source) && Objects.equals(title, that.title) && Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image, edit, delete, consumption, source, title, imagePath);
    }
}
