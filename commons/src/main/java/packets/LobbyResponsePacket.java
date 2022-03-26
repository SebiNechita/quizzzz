package packets;


import java.util.Map;
import java.util.Objects;

public class LobbyResponsePacket extends GeneralResponsePacket {
    private String type;
    private String content;
    private String from;
    private Map<String, String> playerList;

    /**
     * getter for type
     *
     * @return type of the response.
     */
    public String getType() {
        return type;
    }

    /**
     * getter for the content
     *
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * getter for from field, which indicates who's update it is
     *
     * @return from
     */
    public String getFrom() {
        return from;
    }

    /**
     * updated player list with ready stats
     *
     * @return playerList
     */
    public Map<String, String> getPlayerList() {
        return playerList;
    }

    /**
     * default constructor to make Jackson work properly
     */
    public LobbyResponsePacket() {
    }

    /**
     * constructor
     *
     * @param type
     * @param content
     * @param from
     */
    public LobbyResponsePacket(String type, String content, String from) {
        this.type = type;
        this.content = content;
        this.from = from;
    }

    /**
     * constructor with player list
     *
     * @param type
     * @param content
     * @param from
     * @param playerList
     */
    public LobbyResponsePacket(String type, String content, String from, Map<String, String> playerList) {
        this.type = type;
        this.content = content;
        this.from = from;
        this.playerList = playerList;
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
        if (!super.equals(o)) return false;
        LobbyResponsePacket that = (LobbyResponsePacket) o;
        return Objects.equals(type, that.type) && Objects.equals(content, that.content) && Objects.equals(from, that.from) && Objects.equals(playerList, that.playerList);
    }

    /**
     * hash method
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, content, from, playerList);
    }

    /**
     * toString method
     *
     * @return
     */
    @Override
    public String toString() {
        return "LobbyResponsePacket{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", from='" + from + '\'' +
                ", playerList=" + playerList +
                '}';
    }
}
