package packets;

import commons.utils.HttpStatus;

import java.util.Arrays;

public class ImageResponsePacket extends GeneralResponsePacket{

    protected byte[] imageByte;

    /**
     * Empty constructor for jackson
     */
    public ImageResponsePacket() {}

    /**
     * Constructor
     * @param imageByte byte array for this image
     */
    public ImageResponsePacket(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    /**
     * Constructor
     * @param httpCode HTTP status code
     * @param imageByte byte aray for this image
     */
    public ImageResponsePacket(int httpCode, byte[] imageByte) {
        super(httpCode);
        this.imageByte = imageByte;
    }

    /**
     * Constructor
     * @param status HTTP status
     * @param imageByte byte array for this image
     */
    public ImageResponsePacket(HttpStatus status, byte[] imageByte) {
        super(status);
        this.imageByte = imageByte;
    }

    /**
     * Constructor
     * @param httpCode HTTP status code
     * @param message message shared in the response
     * @param imageByte byte array of the image
     */
    public ImageResponsePacket(int httpCode, String message, byte[] imageByte) {
        super(httpCode, message);
        this.imageByte = imageByte;
    }

    /**
     * Constructor
     * @param status HTTP Status
     * @param message message shared in the response
     * @param imageByte byte array of the image
     */
    public ImageResponsePacket(HttpStatus status, String message, byte[] imageByte) {
        super(status, message);
        this.imageByte = imageByte;
    }

    /**
     * Getter for byte array of the image
     * @return byte array of the image
     */
    public byte[] getImageByte() {
        return imageByte;
    }

    /**
     * Setter for the byte array of the image
     * @param imageByte byte array of the image
     */
    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    /**
     * String representation of the image
     * @return string representation of the image
     */
    @Override
    public String toString() {
        return "ImageResponsePacket{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", imageByte=" + Arrays.toString(imageByte) +
                ", code=" + code +
                '}';
    }
}
