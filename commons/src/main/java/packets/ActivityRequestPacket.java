package packets;

import java.util.Objects;

public class ActivityRequestPacket extends RequestPacket {
    protected String image_path;
    protected long consumption;
    protected String source;
    protected String description;
    protected String id;

    protected ActivityRequestPacket() {
    }

    public ActivityRequestPacket(String image_path, long consumption, String source, String description) {
        this.image_path = image_path;
        this.consumption = consumption;
        this.source = source;
        this.description = description;
        this.id = "added - " + image_path;
    }

    public String getImage_path() {
        return image_path;
    }

    public long getConsumption() {
        return consumption;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityRequestPacket that = (ActivityRequestPacket) o;
        return consumption == that.consumption && Objects.equals(image_path, that.image_path) && Objects.equals(source, that.source) && Objects.equals(description, that.description) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image_path, consumption, source, description, id);
    }

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
