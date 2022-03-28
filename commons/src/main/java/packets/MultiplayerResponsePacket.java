package packets;


import commons.Game;
import commons.utils.HttpStatus;


//import java.util.Map;

public class MultiplayerResponsePacket extends GeneralResponsePacket {

    private Game multipleGame;

    private Boolean active;

    public MultiplayerResponsePacket() {
    }

    public MultiplayerResponsePacket(HttpStatus status, Game multipleGame) {
        super(status);
        this.multipleGame = multipleGame;
    }

    /**
     * constructor
     *
     * @param httpCode
     */
    public MultiplayerResponsePacket(int httpCode) {
        super(httpCode);
    }

    /**
     * constructor
     *
     * @param status http status
     */
    public MultiplayerResponsePacket(HttpStatus status) {
        super(status);
    }

    /**
     * constructor
     *
     * @param httpCode http code
     * @param message  message
     */
    public MultiplayerResponsePacket(int httpCode, String message) {
        super(httpCode, message);
    }

    /**
     * constructor
     *
     * @param status
     * @param message
     */
    public MultiplayerResponsePacket(HttpStatus status, String message) {
        super(status, message);
    }


}
