package packets;

import java.util.Objects;

public class EmoteRequestPacket extends RequestPacket {

    private String username;
    private String emoteNo;

    public EmoteRequestPacket() {
    }

    public EmoteRequestPacket(String username, String emoteNo) {
        this.username = username;
        this.emoteNo = emoteNo;
    }

    public String getUsername() {
        return username;
    }

    public String getEmoteNo() {
        return emoteNo;
    }

    @Override
    public String toString() {
        return "EmoteRequestPacket{" +
                "username='" + username + '\'' +
                ", emoteNo='" + emoteNo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmoteRequestPacket that = (EmoteRequestPacket) o;
        return Objects.equals(username, that.username) && Objects.equals(emoteNo, that.emoteNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, emoteNo);
    }
}
