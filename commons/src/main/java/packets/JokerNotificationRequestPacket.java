package packets;

import commons.utils.JokerType;

import java.util.Objects;

public class JokerNotificationRequestPacket extends RequestPacket {
    private String username;
    private JokerType jokerType;

    public JokerNotificationRequestPacket() {
    }

    public JokerNotificationRequestPacket(String username, JokerType jokerType) {
        this.username = username;
        this.jokerType = jokerType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JokerType getJokerType() {
        return jokerType;
    }

    public void setJokerType(JokerType jokerType) {
        this.jokerType = jokerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JokerNotificationRequestPacket that = (JokerNotificationRequestPacket) o;
        return Objects.equals(username, that.username) && Objects.equals(jokerType, that.jokerType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, jokerType);
    }

    @Override
    public String toString() {
        return "JokerNotificationRequestPacket{" +
                "username='" + username + '\'' +
                ", jokerType='" + jokerType + '\'' +
                '}';
    }
}
