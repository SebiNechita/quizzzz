package packets;

import commons.utils.JokerType;

import java.util.Objects;

public class JokerNotificationRequestPacket extends RequestPacket {
    private String username;
    private JokerType jokerType;

    /**
     * Empty constructor
     */
    public JokerNotificationRequestPacket() {
    }

    /**
     * Constructor
     * @param username The username of the player
     * @param jokerType The type of the joker
     */
    public JokerNotificationRequestPacket(String username, JokerType jokerType) {
        this.username = username;
        this.jokerType = jokerType;
    }

    /**
     * Getter
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter
     * @return The joker type
     */
    public JokerType getJokerType() {
        return jokerType;
    }

    /**
     * Setter
     * @param jokerType The joker type
     */
    public void setJokerType(JokerType jokerType) {
        this.jokerType = jokerType;
    }

    /**
     * Checks whether o is equal to this
     * @param o The object to be compared to this
     * @return Whether o is equal to this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JokerNotificationRequestPacket that = (JokerNotificationRequestPacket) o;
        return Objects.equals(username, that.username) && Objects.equals(jokerType, that.jokerType);
    }

    /**
     * Returns the hash code of this
     * @return The hash code of this
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, jokerType);
    }

    /**
     * String representation of this
     * @return String representation of this
     */
    @Override
    public String toString() {
        return "JokerNotificationRequestPacket{" +
                "username='" + username + '\'' +
                ", jokerType='" + jokerType + '\'' +
                '}';
    }
}
