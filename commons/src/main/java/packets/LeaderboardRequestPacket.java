package packets;

import java.util.Objects;

public class LeaderboardRequestPacket extends RequestPacket{
    private String player;
    private int score;

    public LeaderboardRequestPacket() {
    }

    public LeaderboardRequestPacket(String player, int score) {
        this.player = player;
        this.score = score;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "LeaderboardRequestPacket{" +
                "player='" + player + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderboardRequestPacket that = (LeaderboardRequestPacket) o;
        return score == that.score && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, score);
    }
}
