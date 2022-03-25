package packets;

import java.util.Objects;

public class ReadyRequestPacket extends RequestPacket {
    private String username;
    private String isReady;

    /**
     * default constructor for Jackson to work
     */
    public ReadyRequestPacket() {
    }

    /**
     * constructor
     * @param username
     * @param isReady
     */
    public ReadyRequestPacket(String username, String isReady) {
        this.username = username;
        this.isReady = isReady;
    }

    public String getUsername() {
        return username;
    }

    public String getIsReady() {
        return isReady;
    }

    @Override
    public String toString() {
        return "ReadyRequestPacket{" +
                "username='" + username + '\'' +
                ", isReady='" + isReady + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadyRequestPacket that = (ReadyRequestPacket) o;
        return Objects.equals(username, that.username) && Objects.equals(isReady, that.isReady);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, isReady);
    }
}
