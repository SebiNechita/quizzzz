package packets;

import commons.utils.JokerType;

import java.util.Objects;

public class JokerRequestPacket extends RequestPacket {
    private JokerType jokerType;
    private String username;
    private String scene;

    /**
     * Empty constructor
     */
    public JokerRequestPacket() {
    }

    /**
     * Constructor
     * @param jokerType The type of joker used
     * @param username Username of the player
     * @param scene Scene of the type of question
     */
    public JokerRequestPacket(JokerType jokerType, String username, String scene) {
        this.jokerType = jokerType;
        this.username = username;
        this.scene = scene;
    }

    /**
     *
     * @param jokerType The type of joker used
     * @param username Username of the player
     */
    public JokerRequestPacket(JokerType jokerType, String username) {
        this.jokerType = jokerType;
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
     * Getter
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter
     * @param jokerType The type of the joker
     */
    public void setJokerType(JokerType jokerType) {
        this.jokerType = jokerType;
    }

    /**
     * Setter
     * @param username The username of the player
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter
     * @return The scene
     */
    public String getScene() {
        return scene;
    }

    /**
     * Setter
     * @param scene The scene of the current question type
     */
    public void setScene(String scene) {
        this.scene = scene;
    }

    /**
     * Checks whether o is equal to this
     * @param o Object to be compared to this
     * @return Whether o is equal to this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JokerRequestPacket that = (JokerRequestPacket) o;
        return jokerType == that.jokerType && Objects.equals(username, that.username);
    }

    /**
     * Returns the hash code of this
     * @return Hash code of this
     */
    @Override
    public int hashCode() {
        return Objects.hash(jokerType, username);
    }

    /**
     * String representation of this
     * @return String representation of this
     */
    @Override
    public String toString() {
        return "JokerRequestPacket{" +
                "jokerType=" + jokerType +
                ", username='" + username + '\'' +
                '}';
    }
}
