package packets;

import commons.utils.JokerType;

import java.util.Objects;

public class JokerRequestPacket extends RequestPacket {
    private JokerType jokerType;
    private String username;
    private String scene;

    public JokerRequestPacket() {
    }

    public JokerRequestPacket(JokerType jokerType, String username, String scene) {
        this.jokerType = jokerType;
        this.username = username;
        this.scene = scene;
    }

    public JokerRequestPacket(JokerType jokerType, String username) {
        this.jokerType = jokerType;
        this.username = username;
    }

    public JokerType getJokerType() {
        return jokerType;
    }

    public String getUsername() {
        return username;
    }

    public void setJokerType(JokerType jokerType) {
        this.jokerType = jokerType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JokerRequestPacket that = (JokerRequestPacket) o;
        return jokerType == that.jokerType && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jokerType, username);
    }

    @Override
    public String toString() {
        return "JokerRequestPacket{" +
                "jokerType=" + jokerType +
                ", username='" + username + '\'' +
                '}';
    }
}
