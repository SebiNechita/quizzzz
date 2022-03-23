package packets;

import commons.utils.HttpStatus;

import java.util.Objects;

public class LobbyResponsePacket extends ResponsePacket {
    String type;
    String content;
    String from;

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }

    public LobbyResponsePacket() {
    }

    public LobbyResponsePacket(String type, String content, String from) {
        this.type = type;
        this.content = content;
        this.from = from;
    }

    public LobbyResponsePacket(int httpCode, String type, String content, String from) {
        super(httpCode);
        this.type = type;
        this.content = content;
        this.from = from;
    }

    public LobbyResponsePacket(HttpStatus status, String type, String content, String from) {
        super(status);
        this.type = type;
        this.content = content;
        this.from = from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LobbyResponsePacket that = (LobbyResponsePacket) o;
        return Objects.equals(type, that.type) && Objects.equals(content, that.content) && Objects.equals(from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, content, from);
    }

    @Override
    public String toString() {
        return "LobbyResponsePacket{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
