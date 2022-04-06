package packets;

import java.util.Objects;

public class EmoteRequestPacket extends RequestPacket {

    private String username;
    private String emoteStr;
    private String fromScene;

    public EmoteRequestPacket() {
    }

    /**
     * constructor for EmoteRequestPacket
     *
     * @param username user sending the emote
     * @param emoteStr emote type
     * @param fromScene from which scene the emote request is sent. Can be lobby/game
     */
    public EmoteRequestPacket(String username, String emoteStr, String fromScene) {
        this.username = username;
        this.emoteStr = emoteStr;
        this.fromScene = fromScene;
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
     * getter for emoteStr
     *
     * @return emoteStr
     */
    public String getEmoteStr() {
        return emoteStr;
    }

    /**
     * Setter for username
     * @param username the user sending the emote
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter for emoteStr
     * @param emoteStr type of emote
     */
    public void setEmoteStr(String emoteStr) {
        this.emoteStr = emoteStr;
    }

    /**
     * Getter for fromScene
     * @return scene from which the emote is being sent
     */
    public String getFromScene() {
        return fromScene;
    }

    /**
     * Setter for fromScene
     * @param fromScene the scene from which the emote is being sent
     */
    public void setFromScene(String fromScene) {
        this.fromScene = fromScene;
    }

    /**
     * String representation of the packet
     * @return String representation of the packet
     */
    @Override
    public String toString() {
        return "EmoteRequestPacket{" +
                "username='" + username + '\'' +
                ", emoteStr='" + emoteStr + '\'' +
                ", fromScene='" + fromScene + '\'' +
                '}';
    }

    /**
     * Checks for equality
     * @param o object with which the comparison is to be made
     * @return true if equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmoteRequestPacket that = (EmoteRequestPacket) o;
        return Objects.equals(username, that.username) && Objects.equals(emoteStr, that.emoteStr) && Objects.equals(fromScene, that.fromScene);
    }

    /**
     * Hash function for this
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, emoteStr, fromScene);
    }
}
