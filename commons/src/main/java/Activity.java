import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Activity {

    @SuppressWarnings("checkstyle:JavadocVariable")
    private String id;
    private String image_path;
    private long consumption_in_wh;
    private String source;
    private String title;

    public Activity() {}

    public Activity(String title, String id, String image_path, long consumption_in_wh, String source) {
        this.title = title;
        this.id = id;
        this.image_path = image_path;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    /**
     * This method reads the activities from a json file
     * @param source File from which we have to read the activities
     * @return A list that contains all the activities
     */
    public static List<Activity> readActivities(File source) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                source,
                new TypeReference<List<Activity>>() {}
        );
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImagePath(String image_path) {
        this.image_path = image_path;
    }

    public void setConsumptionInWH(long consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getImage_path() {
        return image_path;
    }

    public long getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public String getSource() {
        return source;
    }

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
