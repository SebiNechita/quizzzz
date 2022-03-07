package commons;

import javax.persistence.*;

@Entity
public class LeaderboardEntry {
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
}
