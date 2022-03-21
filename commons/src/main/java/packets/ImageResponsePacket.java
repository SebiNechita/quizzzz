package packets;

import commons.utils.HttpStatus;

import java.util.Arrays;

public class ImageResponsePacket extends GeneralResponsePacket{

    protected byte[] imageByte;

    public ImageResponsePacket() {}

    public ImageResponsePacket(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public ImageResponsePacket(int httpCode, byte[] imageByte) {
        super(httpCode);
        this.imageByte = imageByte;
    }

    public ImageResponsePacket(HttpStatus status, byte[] imageByte) {
        super(status);
        this.imageByte = imageByte;
    }

    public ImageResponsePacket(int httpCode, String message, byte[] imageByte) {
        super(httpCode, message);
        this.imageByte = imageByte;
    }

    public ImageResponsePacket(HttpStatus status, String message, byte[] imageByte) {
        super(status, message);
        this.imageByte = imageByte;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

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
