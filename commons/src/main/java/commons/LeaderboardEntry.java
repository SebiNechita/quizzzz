package commons;

import packets.RequestPacket;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class LeaderboardEntry extends RequestPacket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public int points;

    public String username;

    @SuppressWarnings("unused")
    /**
     * Empty constructor
     */
    public LeaderboardEntry() {
        // for object mappers
    }

    /**
     * Constructor
     * @param points highest point of the user
     * @param username username
     */
    public LeaderboardEntry(int points, String username) {
        this.points = points;
        this.username = username;
    }

    /**
     * Getter for ID
     * @return ID
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for ID
     * @param id ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for the points
     * @return highest points of a user
     */
    public int getPoints() {
        return points;
    }

    /**
     * Setter for points
     * @param points highest points of a user
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Getter for username
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username
     * @param username username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Checks for equality
     * @param o Object with which this is to be compared
     * @return true if they are qual; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderboardEntry that = (LeaderboardEntry) o;
        return id == that.id && points == that.points && Objects.equals(username, that.username);
    }

    /**
     * Returns the hashcode of this object
     * @return hashcode of this
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, points, username);
    }

    /**
     * String representation of this
     * @return String representation of this
     */
    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "id=" + id +
                ", points=" + points +
                ", username='" + username + '\'' +
                '}';
    }
}
