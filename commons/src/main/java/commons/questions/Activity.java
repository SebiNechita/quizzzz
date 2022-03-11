package commons.questions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Activity {

    @SuppressWarnings("checkstyle:JavadocVariable")
    /**
     *
     */
    private String id;
    /**
     * Path to the corresponding image
     */
    private String image_path;
    /**
     * Energy consumed in WH
     */
    private long consumption_in_wh;
    /**
     * Link to source
     */
    private String source;
    /**
     * Description of the activity
     */
    private String title;

    /**
     * Empty constructor. This was required for Jackson to work.
     */
    public Activity() {
    }

    /**
     * Constructor for Activity
     *
     * @param title             description of the activity
     * @param id                unique id that can be used to find the activity
     * @param image_path        path to its corresponding image
     * @param consumption_in_wh energy consumed in wh
     * @param source            link to source
     */
    public Activity(String title, String id, String image_path, long consumption_in_wh, String source) {
        this.title = title;
        this.id = id;
        this.image_path = image_path;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    /**
     * This method reads the activities from a json file
     *
     * @param source File from which we have to read the activities
     * @return A list that contains all the activities
     */
    public static List<Activity> readActivities(File source) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                source,
                new TypeReference<List<Activity>>() {
                }
        );
    }

    /**
     * Setter for title
     *
     * @param title description of teh activity
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for id
     *
     * @param id unique identifier for the activity
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter for image path
     *
     * @param image_path path to the corresponding image
     */
    public void setImagePath(String image_path) {
        this.image_path = image_path;
    }

    /**
     * Setter for consumption_in_wh
     *
     * @param consumption_in_wh energy consumed in wh
     */
    public void setConsumptionInWH(long consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    /**
     * Setter for source
     *
     * @param source link to source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Getter for title
     *
     * @return title of the activity
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for id
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for image path
     *
     * @return image path
     */
    public String getImage_path() {
        return image_path;
    }

    /**
     * Getter for consumption_in_wh
     *
     * @return energy consumed in wh
     */
    public long getConsumption_in_wh() {
        return consumption_in_wh;
    }

    /**
     * Getter for source
     *
     * @return link to source
     */
    public String getSource() {
        return source;
    }

    /**
     * String representation of Activity
     *
     * @return the string representation of activity
     */
    @Override
    public String toString() {
        return "Activity{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", imagePath='" + image_path + '\'' +
                ", consumptionInWH=" + consumption_in_wh +
                ", source='" + source + '\'' +
                '}';
    }
}
