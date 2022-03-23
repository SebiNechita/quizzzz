package packets;

import java.util.Objects;

public class JoinRequestPacket extends RequestPacket {



    private String username;

    public JoinRequestPacket() {
    }

    public String getUsername() {
        return username;
    }

    public JoinRequestPacket(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinRequestPacket that = (JoinRequestPacket) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "JoinRequestPacket{" +
                "username='" + username + '\'' +
                '}';
    }
}
