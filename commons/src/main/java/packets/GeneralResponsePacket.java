package packets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import commons.utils.HttpStatus;

import java.util.Objects;

@JsonTypeInfo(include=JsonTypeInfo.As.WRAPPER_OBJECT, use=JsonTypeInfo.Id.NAME)
public class GeneralResponsePacket extends ResponsePacket {
    protected int code;
    protected String message;

    /**
     * Required for object mappers
     */
    protected GeneralResponsePacket() {}

    /**
     * Constructor for this packet
     *
     * @param httpCode The HTTP code to send to the receiver of this packet
     */
    public GeneralResponsePacket(int httpCode) {
        this.code = httpCode;
    }

    /**
     * Constructor for this packet
     *
     * @param status The HTTP status to send to the receiver of this packet
     */
    public GeneralResponsePacket(HttpStatus status) {
        this.code = status.getCode();
    }

    /**
     * Constructor for this packet
     *
     * @param httpCode The HTTP code to send to the receiver of this packet
     * @param message The message to send to the receiver of this packet
     */
    public GeneralResponsePacket(int httpCode, String message) {
        this.code = httpCode;
        this.message = message;
    }

    /**
     * Constructor for this packet
     *
     * @param status The HTTP status to send to the receiver of this packet
     * @param message The message to send to the receiver of this packet
     */
    public GeneralResponsePacket(HttpStatus status, String message) {
        this.code = status.getCode();
        this.message = message;
    }

    /**
     * Getter for the HTTP code
     *
     * @return The raw HTTP code of this packet
     */
    public int getCode() {
        return code;
    }

    /**
     * Getter for the HTTP status
     *
     * @return The HTTP status of this packet
     */
    @JsonIgnore
    public HttpStatus getResponseStatus() {
        return HttpStatus.getByCode(code);
    }

    /**
     * Checks if this packet is equal to the object specified as a parameter
     *
     * @param other The other object to compare this with
     * @return If they are equal or not
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof GeneralResponsePacket that)) return false;
        return code == that.code && Objects.equals(message, that.message);
    }

    /**
     * Generates a hashcode for this packet
     *
     * @return The hashcode of this packet
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    /**
     * Translates the data of this packet into a readable format
     *
     * @return A formatted string
     */
    @Override
    public String toString() {
        return "GeneralResponsePacket{" +
               "code=" + code + "(" + getResponseStatus() + ")" +
               ", message='" + message + '\'' +
               '}';
    }
}
