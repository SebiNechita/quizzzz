package packets;

import java.util.Objects;

public class PingRequestPacket extends RequestPacket {

    private String username;

    /**
     * default constructor
     */
    public PingRequestPacket() {
    }

    /**
     * constructor with username
     *
     * @param username whos initiating the request
     */
    public PingRequestPacket(String username) {
        this.username = username;
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
     * toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "PingRequestPacket{" +
                "username='" + username + '\'' +
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
        PingRequestPacket that = (PingRequestPacket) o;
        return Objects.equals(username, that.username);
    }

    /**
     * hash method
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
