package packets;

import java.util.Objects;

public class JoinRequestPacket extends RequestPacket {

    /**
     * player who wants to join the lobby
     */
    private String username;

    /**
     * default constructor
     */
    public JoinRequestPacket() {
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
     * constructor with the username
     *
     * @param username player who wants the join the lobby
     */
    public JoinRequestPacket(String username) {
        this.username = username;
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
        JoinRequestPacket that = (JoinRequestPacket) o;
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

    /**
     * to string method
     *
     * @return
     */
    @Override
    public String toString() {
        return "JoinRequestPacket{" +
                "username='" + username + '\'' +
                '}';
    }
}
