package packets;

import commons.Game;
import commons.utils.HttpStatus;

import java.util.Map;
import java.util.Objects;

public class JoinResponsePacket extends GeneralResponsePacket {

    private Map<String, String> playerList;
    private Game game;

    /**
     * default constructor
     */
    public JoinResponsePacket() {
    }

    /**
     * response packet for a join request.
     *
     * @param status     http status
     * @param playerList all the player list and the ready states
     */
    public JoinResponsePacket(HttpStatus status, Map<String, String> playerList) {
        super(status);
        this.playerList = playerList;
    }

    public JoinResponsePacket(Map<String, String> playerList, Game game) {
        this.playerList = playerList;
        this.game = game;
    }

    public JoinResponsePacket(int httpCode, Map<String, String> playerList, Game game) {
        super(httpCode);
        this.playerList = playerList;
        this.game = game;
    }

    public JoinResponsePacket(HttpStatus status, Map<String, String> playerList, Game game) {
        super(status);
        this.playerList = playerList;
        this.game = game;
    }

    public JoinResponsePacket(int httpCode, String message, Map<String, String> playerList, Game game) {
        super(httpCode, message);
        this.playerList = playerList;
        this.game = game;
    }

    public JoinResponsePacket(HttpStatus status, String message, Map<String, String> playerList, Game game) {
        super(status, message);
        this.playerList = playerList;
        this.game = game;
    }

    /**
     * constructor
     *
     * @param httpCode
     */
    public JoinResponsePacket(int httpCode) {
        super(httpCode);
    }

    /**
     * constructor
     *
     * @param status http status
     */
    public JoinResponsePacket(HttpStatus status) {
        super(status);
    }

    /**
     * constructor
     *
     * @param httpCode http code
     * @param message  message
     */
    public JoinResponsePacket(int httpCode, String message) {
        super(httpCode, message);
    }

    /**
     * constructor
     *
     * @param status
     * @param message
     */
    public JoinResponsePacket(HttpStatus status, String message) {
        super(status, message);
    }

    /**
     * getter for the player list
     *
     * @return playerList
     */
    public Map<String, String> getPlayerList() {
        return playerList;
    }


    public void setPlayerList(Map<String, String> playerList) {
        this.playerList = playerList;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
        JoinResponsePacket that = (JoinResponsePacket) o;
        return Objects.equals(playerList, that.playerList);
    }

    /**
     * hash method
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), playerList);
    }

    /**
     * toString method
     *
     * @return
     */
    @Override
    public String toString() {
        return "JoinResponsePacket{" +
                "playerList=" + playerList +
                '}';
    }
}
