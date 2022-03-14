package packets;

import commons.LeaderboardEntry;
import commons.utils.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeaderboardResponsePacket extends GeneralResponsePacket {
    List<LeaderboardEntry> leaderboard;

    /**
     * Required for object mappers
     */
    protected LeaderboardResponsePacket() {
        leaderboard = new ArrayList<>();
    }

    /**
     * Constructor for this packet
     *
     * @param httpCode The HTTP code to send to the receiver of this packet
     */
    public LeaderboardResponsePacket(int httpCode) {
        super(httpCode);
    }

    /**
     * Constructor for this packet
     *
     * @param status The HTTP status to send to the receiver of this packet
     */
    public LeaderboardResponsePacket(HttpStatus status) {
        super(status);
    }

    /**
     * Constructor for this packet
     *
     * @param httpCode The HTTP code to send to the receiver of this packet
     * @param leaderboard The leaderboard to include with this packet
     */
    public LeaderboardResponsePacket(int httpCode, List<LeaderboardEntry> leaderboard) {
        super(httpCode);
        this.leaderboard = leaderboard;
    }

    /**
     * Constructor for this packet
     *
     * @param status The HTTP status to send to the receiver of this packet
     * @param leaderboard The leaderboard to include with this packet
     */
    public LeaderboardResponsePacket(HttpStatus status, List<LeaderboardEntry> leaderboard) {
        super(status);
        this.leaderboard = leaderboard;
    }

    /**
     * Constructor for this packet
     *
     * @param httpCode The HTTP code to send to the receiver of this packet
     * @param message  The message to send to the receiver of this packet
     */
    public LeaderboardResponsePacket(int httpCode, String message) {
        super(httpCode, message);
    }

    /**
     * Constructor for this packet
     *
     * @param status  The HTTP status to send to the receiver of this packet
     * @param message The message to send to the receiver of this packet
     */
    public LeaderboardResponsePacket(HttpStatus status, String message) {
        super(status, message);
    }

    /**
     * Getter for the leaderboard
     *
     * @return The leaderboard entries in a list
     */
    public List<LeaderboardEntry> getLeaderboard() {
        return leaderboard;
    }

    /**
     * Checks if this packet is equal to the object specified as a parameter
     *
     * @param other The other object to compare this with
     * @return If they are equal or not
     */
    @Override
    public boolean equals(Object other) {
        return super.equals(other) && other instanceof DeleteResponsePacket;
    }

    /**
     * Translates the data of this packet into a readable format
     *
     * @return A formatted string
     */
    @Override
    public String toString() {
        return "LeaderboardResponsePacket{" +
               "responseCode=" + getResponseStatus() + " (" + code + ")," +
               "message=" + message + "," +
               "leaderboard=" + Arrays.toString(leaderboard.toArray()) +
               '}';
    }
}
