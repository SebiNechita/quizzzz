package packets;

import commons.utils.HttpStatus;

import java.util.Map;
import java.util.Objects;

public class JoinResponsePacket extends GeneralResponsePacket {
    private Map<String, String> playerList;

    public JoinResponsePacket() {
    }

    public JoinResponsePacket(HttpStatus status, Map<String, String> playerList) {
        super(status);
        this.playerList = playerList;
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

    public Map<String, String> getPlayerList() {
        return playerList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JoinResponsePacket that = (JoinResponsePacket) o;
        return Objects.equals(playerList, that.playerList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), playerList);
    }

    @Override
    public String toString() {
        return "JoinResponsePacket{" +
                "playerList=" + playerList +
                '}';
    }
}
