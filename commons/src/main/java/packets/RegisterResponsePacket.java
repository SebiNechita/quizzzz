package packets;

import commons.utils.HttpStatus;

import java.util.Objects;

public class RegisterResponsePacket {

    private int code;
    private String message;

    public RegisterResponsePacket() {}

    public RegisterResponsePacket(int code) {
        this.code = code;
        this.message = "";
    }

    public RegisterResponsePacket(HttpStatus status) {
        this.code = status.getCode();
        this.message = "";
    }

    public RegisterResponsePacket(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public RegisterResponsePacket(HttpStatus status, String message) {
        this.code = status.getCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterResponsePacket that)) return false;
        return code == that.code && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    @Override
    public String toString() {
        return "RegisterResponsePacket{" +
               "returnCode=" + code +
               ", message='" + message + '\'' +
               '}';
    }
}
