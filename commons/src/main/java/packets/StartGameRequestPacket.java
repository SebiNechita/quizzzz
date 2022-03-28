package packets;

import java.util.Objects;

public class StartGameRequestPacket extends RequestPacket {
    private boolean start;
    private String username;

    public StartGameRequestPacket() {
    }

    public StartGameRequestPacket(boolean start, String username) {
        this.start = start;
        this.username = username;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isStart() {
        return start;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartGameRequestPacket that = (StartGameRequestPacket) o;
        return start == that.start && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, username);
    }

    @Override
    public String toString() {
        return "StartGameRequestPacket{" +
                "start=" + start +
                ", username='" + username + '\'' +
                '}';
    }
}
