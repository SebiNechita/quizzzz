package packets;

import commons.utils.HttpStatus;

public class DeleteResponsePacket extends GeneralResponsePacket {
    /**
     * Required for object mappers
     */
    protected DeleteResponsePacket() {
    }

    /**
     * Constructor for this packet
     *
     * @param httpCode The HTTP code to send to the receiver of this packet
     */
    public DeleteResponsePacket(int httpCode) {
        super(httpCode);
    }

    /**
     * Constructor for this packet
     *
     * @param status The HTTP status to send to the receiver of this packet
     */
    public DeleteResponsePacket(HttpStatus status) {
        super(status);
    }

    /**
     * Constructor for this packet
     *
     * @param httpCode The HTTP code to send to the receiver of this packet
     * @param message  The message to send to the receiver of this packet
     */
    public DeleteResponsePacket(int httpCode, String message) {
        super(httpCode, message);
    }

    /**
     * Constructor for this packet
     *
     * @param status  The HTTP status to send to the receiver of this packet
     * @param message The message to send to the receiver of this packet
     */
    public DeleteResponsePacket(HttpStatus status, String message) {
        super(status, message);
    }

    /**
     * Checks if this packet is equal to the object specified as a parameter
     *
     * @param other The other object to compare this with
     * @return If they are equal or not
     */
    @Override
    public boolean equals(Object other) {
        return super.equals(other) && other instanceof DeleteResponsePacket;
    }

    /**
     * Translates the data of this packet into a readable format
     *
     * @return A formatted string
     */
    @Override
    public String toString() {
        return "DeleteResponsePacket{" +
               "responseCode=" + getResponseStatus() + " (" + code + ")" +
               "message=" + message +
               '}';
    }
}
