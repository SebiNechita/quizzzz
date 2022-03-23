package packets;

import commons.utils.HttpStatus;

public class JoinResponsePacket extends GeneralResponsePacket {
    public JoinResponsePacket() {
    }

    public JoinResponsePacket(int httpCode) {
        super(httpCode);
    }

    public JoinResponsePacket(HttpStatus status) {
        super(status);
    }

    public JoinResponsePacket(int httpCode, String message) {
        super(httpCode, message);
    }

    public JoinResponsePacket(HttpStatus status, String message) {
        super(status, message);
    }
}
