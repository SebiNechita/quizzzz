package packets;

import commons.utils.HttpStatus;

import java.util.Objects;

public class RegisterResponsePacket {

    private int returnCode;
    private String message;

    public RegisterResponsePacket() {}

    public RegisterResponsePacket(int returnCode) {
        this.returnCode = returnCode;
        this.message = "";
    }

    public RegisterResponsePacket(HttpStatus status) {
        this.returnCode = status.getCode();
        this.message = "";
    }

    public RegisterResponsePacket(int returnCode, String message) {
        this.returnCode = returnCode;
        this.message = message;
    }

    public RegisterResponsePacket(HttpStatus status, String message) {
        this.returnCode = status.getCode();
        this.message = message;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public HttpStatus getReturnStatus() {
        return HttpStatus.getByCode(returnCode);
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public void setReturnStatus(HttpStatus status) {
        returnCode = status.getCode();
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
        return returnCode == that.returnCode && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnCode, message);
    }

    @Override
    public String toString() {
        return "RegisterResponsePacket{" +
               "returnCode=" + returnCode +
               ", message='" + message + '\'' +
               '}';
    }
}
