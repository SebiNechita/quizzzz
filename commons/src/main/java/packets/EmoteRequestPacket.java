package packets;

import java.util.Objects;

public class EmoteRequestPacket extends RequestPacket {

    private String username;
    private String emoteStr;

    public EmoteRequestPacket() {
    }

    /**
     * constructor for EmoteRequestPacket
     *
     * @param username
     * @param emoteStr
     */
    public EmoteRequestPacket(String username, String emoteStr) {
        this.username = username;
        this.emoteStr = emoteStr;
    }

    /**
     * getter for username
     *
     * @return username
     */
    public String getUsername() {

        return username;
    }

    /**
     * getter for emoteStr
     *
     * @return emoteStr
     */
    public String getEmoteStr() {
        return emoteStr;
    }

    /**
     * to string method
     *
     * @return
     */
    @Override
    public String toString() {
        return "EmoteRequestPacket{" +
                "username='" + username + '\'' +
                ", emoteNo='" + emoteStr + '\'' +
                '}';
    }

    /**
     * equals method
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmoteRequestPacket that = (EmoteRequestPacket) o;
        return Objects.equals(username, that.username) && Objects.equals(emoteStr, that.emoteStr);
    }

    /**
     * hash method
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, emoteStr);
    }
}
