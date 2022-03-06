package packets;

import commons.utils.HttpStatus;

public class DeleteResponsePacket extends RegisterResponsePacket{

    public DeleteResponsePacket() {
    }

    public DeleteResponsePacket(int code) {
        super(code);
    }

    public DeleteResponsePacket(HttpStatus status) {
        super(status);
    }

    public DeleteResponsePacket(int code, String message) {
        super(code, message);
    }

    public DeleteResponsePacket(HttpStatus status, String message) {
        super(status, message);
    }
}
