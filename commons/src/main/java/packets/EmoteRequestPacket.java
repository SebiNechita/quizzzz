package packets;

import java.util.Objects;

public class EmoteRequestPacket extends RequestPacket {

    private String username;
    private String emoteStr;

    public EmoteRequestPacket() {
    }

    public EmoteRequestPacket(String username, String emoteStr) {
        this.username = username;
        this.emoteStr = emoteStr;
    }

    public String getUsername() {

        return username;
    }

    public String getEmoteStr() {
        return emoteStr;
    }

    @Override
    public String toString() {
        return "EmoteRequestPacket{" +
                "username='" + username + '\'' +
                ", emoteNo='" + emoteStr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmoteRequestPacket that = (EmoteRequestPacket) o;
        return Objects.equals(username, that.username) && Objects.equals(emoteStr, that.emoteStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, emoteStr);
    }
}
