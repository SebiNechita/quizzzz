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

    /**
     * constructor for ActivityItem
     * @param id activity ID
     * @param image image icon
     * @param edit edit icon
     * @param delete delete icon
     * @param consumption energy consumption
     * @param source information source
     * @param title activity title
     * @param imagePath image path in the server
     */
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

    /**
     * getter for ID
     * @return returns ID
     */
    public String getId() {
        return id;
    }

    /**
     * getter for image icon
     * @return returns image icon
     */
    public ImageView getImage() {
        return image;
    }

    /**
     * getter for edit icon
     * @return returns edit icon
     */
    public ImageView getEdit() {
        return edit;
    }

    /**
     * getter for delete icon
     * @return returns delte icon
     */
    public ImageView getDelete() {
        return delete;
    }

    /**
     * getter for energy consumption
     * @return returns energy consumption
     */
    public long getConsumption() {
        return consumption;
    }

    /**
     * getter for information source
     * @return returns information source
     */
    public String getSource() {
        return source;
    }

    /**
     * getter for title
     * @return returns activity title
     */
    public String getTitle() {
        return title;
    }

    /**
     * getter for image path
     * @return returns image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * toString method
     * @return returns string representation of this object
     */
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

    /**
     * equals method
     * @param o object to compare to
     * @return returns true if equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityItem that = (ActivityItem) o;
        return consumption == that.consumption && Objects.equals(id, that.id) && Objects.equals(image, that.image) && Objects.equals(edit, that.edit) && Objects.equals(delete, that.delete) && Objects.equals(source, that.source) && Objects.equals(title, that.title) && Objects.equals(imagePath, that.imagePath);
    }

    /**
     * hashCode method
     * @return returns hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, image, edit, delete, consumption, source, title, imagePath);
    }
}
