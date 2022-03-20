package commons;

import packets.RequestPacket;

import javax.persistence.*;

@Entity
public class LeaderboardEntry extends RequestPacket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public int points;

    public String username;

    @SuppressWarnings("unused")
    public LeaderboardEntry() {
        // for object mappers
    }

    public LeaderboardEntry(int points, String username) {
        this.points = points;
        this.username = username;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}
