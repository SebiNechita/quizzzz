package commons.questions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Entity
public class Activity {
    @SuppressWarnings("checkstyle:JavadocVariable")
    /**
     * Id of the activity
     */
    @Id
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
    @Column(length = 500)
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
                new TypeReference<>() {
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
     * Setter for image_path
     *
     * @param image_path path of the image
     */
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    /**
     * Setter for energy in wh
     *
     * @param consumption_in_wh energy used by that activity in wh
     */
    public void setConsumption_in_wh(long consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    /**
     * Checks if another activity is equivalent to this.
     * @param o the other activity
     * @return true iff the other activity has same id, consumption,
     * source, path and title
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return consumption_in_wh == activity.consumption_in_wh && id.equals(activity.id) && image_path.equals(activity.image_path) && source.equals(activity.source) && title.equals(activity.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image_path, consumption_in_wh, source, title);
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
