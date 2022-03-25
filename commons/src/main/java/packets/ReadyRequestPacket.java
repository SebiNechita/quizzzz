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
     *
     * @param username who is sending the ready state
     * @param isReady  ready or not
     */
    public ReadyRequestPacket(String username, String isReady) {
        this.username = username;
        this.isReady = isReady;
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
     * getter for isReady
     *
     * @return isReady
     */
    public String getIsReady() {
        return isReady;
    }

    /**
     * toString method
     *
     * @return
     */
    @Override
    public String toString() {
        return "ReadyRequestPacket{" +
                "username='" + username + '\'' +
                ", isReady='" + isReady + '\'' +
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
        ReadyRequestPacket that = (ReadyRequestPacket) o;
        return Objects.equals(username, that.username) && Objects.equals(isReady, that.isReady);
    }

    /**
     * hash method
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, isReady);
    }
}
