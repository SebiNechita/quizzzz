package packets;

import java.util.Arrays;
import java.util.Objects;

public class ActivityRequestPacket extends RequestPacket {

    protected long consumption;
    protected String source;
    protected String description;
    protected String id;
    protected byte[] imageByte;

    /**
     * Empty constructor
     */
    protected ActivityRequestPacket() {
    }

    /**
     * Constructor
     * @param consumption Consumption in WH
     * @param source The web source
     * @param description The title of the activity
     * @param imageByte The path to the image
     */
    public ActivityRequestPacket(long consumption, String source, String description, byte[] imageByte) {
        this.consumption = consumption;
        this.source = source;
        this.description = description;
        this.imageByte = imageByte;
    }

    /**
     * Constructor
     * @param id Id of the activity
     * @param consumption Consumption in WH
     * @param source The web source
     * @param description The title of the activity
     * @param imageByte The path to the image
     */
    public ActivityRequestPacket(String id, long consumption, String source, String description, byte[] imageByte) {
        this.consumption = consumption;
        this.source = source;
        this.description = description;
        this.id = id;
        this.imageByte = imageByte;
    }

    /**
     * Returns an image as a byte array
     * @return The array of bytes representing the image
     */
    public byte[] getImageByte() {
        return imageByte;
    }

    /**
     * Getter
     * @return The consumption of the activity
     */
    public long getConsumption() {
        return consumption;
    }

    /**
     * Getter
     * @return The source of the activity
     */
    public String getSource() {
        return source;
    }

    /**
     * Getter
     * @return The description of the activity
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
        return consumption == that.consumption && Objects.equals(source, that.source) && Objects.equals(description, that.description) && Objects.equals(id, that.id) && Arrays.equals(imageByte, that.imageByte);
    }

    /**
     * Returns the hashcode of this
     * @return The hashcode of this
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(consumption, source, description, id);
        result = 31 * result + Arrays.hashCode(imageByte);
        return result;
    }

    /**
     * Translates the data of this packet into a readable format
     *
     * @return A formatted string
     */
    @Override
    public String toString() {
        return "ActivityRequestPacket{" +
                "consumption=" + consumption +
                ", source='" + source + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", imageByte=" + Arrays.toString(imageByte) +
                '}';
    }
}
