package packets;

import commons.Game;
import commons.utils.HttpStatus;

import java.util.Objects;

public class GameResponsePacket extends ResponsePacket {

    private Game game;

    /**
     * Empty constructor for jackson
     */
    public GameResponsePacket() {
    }

    /**
     * Constructor for GameResponsePacker
     *
     * @param game Instance of Game that is to be transferred
     */
    public GameResponsePacket(Game game) {
        this.game = game;
    }

    /**
     * Constructor for GameResponsePacket
     *
     * @param httpCode HTTP response code
     * @param game     Instance of Game that is to be transferred
     */
    public GameResponsePacket(int httpCode, Game game) {
        super(httpCode);
        this.game = game;
    }

    /**
     * Constructor of GameResponsePacket
     *
     * @param status HTTP Status
     * @param game   Instance of Game that is to be transferred
     */
    public GameResponsePacket(HttpStatus status, Game game) {
        super(status);
        this.game = game;
    }

    /**
     * Getter for game
     *
     * @return Game that was transferred in this Response
     */
    public Game getGame() {
        return game;
    }

    /**
     * Setter for Game
     *
     * @param game Game that is to be sent
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Checks for equality between this and an Object
     *
     * @param o Object with which this is to be compared
     * @return true if equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameResponsePacket that = (GameResponsePacket) o;
        return Objects.equals(game, that.game);
    }

    /**
     * Hash code for this
     *
     * @return hash code for this
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), game);
    }

    /**
     * String representation of the GameResponsePacket Instance
     *
     * @return String representation of the GameResponsePacket Instance
     */
    @Override
    public String toString() {
        return "GameResponsePacket{" +
                "game=" + game +
                ", code=" + code +
                '}';
    }
}
