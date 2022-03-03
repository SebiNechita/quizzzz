package packets;

import commons.utils.HttpStatus;

import java.util.Objects;

public class GeneralResponsePacket {
    private int responseCode;

    public GeneralResponsePacket() {}

    public GeneralResponsePacket(int returnCode) {
        this.responseCode = returnCode;
    }

    public GeneralResponsePacket(HttpStatus status) {
        this.responseCode = status.getCode();
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneralResponsePacket that)) return false;
        return responseCode == that.responseCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseCode);
    }

    @Override
    public String toString() {
        return "GeneralResponsePacket{" +
               "responseCode=" + responseCode +
               '}';
    }
}
