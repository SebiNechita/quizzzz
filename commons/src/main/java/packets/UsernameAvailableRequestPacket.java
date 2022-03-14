package packets;

import java.util.Objects;

public class UsernameAvailableRequestPacket extends RequestPacket {

    private String username;

    protected UsernameAvailableRequestPacket() {}

    public UsernameAvailableRequestPacket(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsernameAvailableRequestPacket that)) return false;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "UsernameExistsRequestPacket{" +
               "username='" + username + '\'' +
               '}';
    }
}
