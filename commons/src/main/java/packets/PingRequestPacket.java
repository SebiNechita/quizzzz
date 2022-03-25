package packets;

import java.util.Objects;

public class PingRequestPacket extends RequestPacket {

    private String username;

    public PingRequestPacket() {
    }

    public PingRequestPacket(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "PingRequestPacket{" +
                "username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PingRequestPacket that = (PingRequestPacket) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
