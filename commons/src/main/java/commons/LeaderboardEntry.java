package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LeaderboardEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public int points;
    public String person;

    @SuppressWarnings("unused")
    public LeaderboardEntry() {
        // for object mappers
    }

    public LeaderboardEntry(int points, String person) {
        this.points = points;
        this.person = person;
    }
}
