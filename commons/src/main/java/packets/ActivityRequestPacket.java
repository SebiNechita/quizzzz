package packets;

import java.util.Objects;

public class ActivityRequestPacket extends RequestPacket {
    protected String image_path;
    protected long consumption;
    protected String source;
    protected String description;
    protected String id;

    /**
     * Empty constructor
     */
    protected ActivityRequestPacket() {
    }

    /**
     * Constructor
     * @param image_path The path to the image
     * @param consumption Consumption in WH
     * @param source The web source
     * @param description The title of the activity
     */
    public ActivityRequestPacket(String image_path, long consumption, String source, String description) {
        this.image_path = image_path;
        this.consumption = consumption;
        this.source = source;
        this.description = description;
        this.id = "added - " + image_path;
    }

    /**
     * Getter
     * @return The image path
     */
    public String getImage_path() {
        return image_path;
    }

    /**
     * Getter
     * @return The consumption in WH
     */
    public long getConsumption() {
        return consumption;
    }

    /**
     * Getter
     * @return The source
     */
    public String getSource() {
        return source;
    }

    /**
     * Getter
     * @return The title of the activity
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter
     * @return The id of the activity
     */
    public String getId() {
        return id;
    }

    /**
     * Equals method
     * @param o The object to be compared to this
     * @return Whether o is equal to this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityRequestPacket that = (ActivityRequestPacket) o;
        return consumption == that.consumption && Objects.equals(image_path, that.image_path) && Objects.equals(source, that.source) && Objects.equals(description, that.description) && Objects.equals(id, that.id);
    }

    /**
     * Retusrns the hashcode of this
     * @return The hjashcode of this
     */
    @Override
    public int hashCode() {
        return Objects.hash(image_path, consumption, source, description, id);
    }

    /**
     * A String representation of this
     * @return A String representation of this
     */
    @Override
    public String toString() {
        return "ActivityRequestPacket{" +
                "image_path='" + image_path + '\'' +
                ", consumption=" + consumption +
                ", source='" + source + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
