package packets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commons.utils.HttpStatus;

import java.util.Objects;

public class ResponsePacket {
    protected int code;

    /**
     * Required for object mappers
     */
    protected ResponsePacket() {}

    /**
     * Constructor for this packet
     *
     * @param httpCode The HTTP code to send to the receiver of this packet
     */
    public ResponsePacket(int httpCode) {
        this.code = httpCode;
    }


    /**
     * Constructor for this packet
     *
     * @param status The HTTP status to send to the receiver of this packet
     */
    public ResponsePacket(HttpStatus status) {
        this.code = status.getCode();
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
        if (!(other instanceof ResponsePacket that)) return false;
        return code == that.code;
    }

    /**
     * Generates a hashcode for this packet
     *
     * @return The hashcode of this packet
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    /**
     * Translates the data of this packet into a readable format
     *
     * @return A formatted string
     */
    @Override
    public String toString() {
        return "ResponsePacket{" +
               "code=" + code + "(" + getResponseStatus() + ")" +
               '}';
    }
}
