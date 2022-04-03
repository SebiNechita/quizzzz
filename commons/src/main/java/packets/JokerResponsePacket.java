package packets;

import commons.utils.JokerType;

import java.util.Map;
import java.util.Objects;

public class JokerResponsePacket extends LobbyResponsePacket {
    private JokerType jokerType;

    /**
     * default constructor to make Jackson work properly
     */
    public JokerResponsePacket() {
    }

    /**
     * Constructor
     * @param jokerType The type of the joker used
     */
    public JokerResponsePacket(JokerType jokerType) {
        this.jokerType = jokerType;
    }

    /**
     *
     * @param type Type of request
     * @param content Content
     * @param from Starting point
     * @param jokerType The type of the joker used
     */
    public JokerResponsePacket(String type, String content, String from, JokerType jokerType) {
        super(type, content, from);
        this.jokerType = jokerType;
    }


    /**
     *
     * @param type Type of request
     * @param content Content
     * @param from Starting point
     * @param playerList The list of players in the room
     * @param jokerType The type of the joker used
     */
    public JokerResponsePacket(String type, String content, String from, Map<String, String> playerList, JokerType jokerType) {
        super(type, content, from, playerList);
        this.jokerType = jokerType;
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
     * @param o Object to be campared to this
     * @return Whether o is equal to this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JokerResponsePacket that = (JokerResponsePacket) o;
        return jokerType == that.jokerType;
    }

    /**
     * Returns the hash code of this
     * @return Hash code of this
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), jokerType);
    }

    /**
     * String representation of this
     * @return String representation of this
     */
    @Override
    public String toString() {
        return "JokerResponsePacket{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", jokerType=" + jokerType +
                ", code=" + code +
                '}';
    }
}
