package server;

import commons.Game;
import commons.LeaderboardEntry;
import packets.LobbyResponsePacket;
import server.api.game.GameController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Match {
    private final Game game;
    private final List<LeaderboardEntry> scores;
    private final List<GameController.EventCaller<LobbyResponsePacket>> playerEventList;
    private final Map<String, Map.Entry<String, LocalDateTime>> playerMap;
    private boolean allReady;

    public Match(Game game) {
        this.game = game;
        this.scores = new LinkedList<>();
        this.playerEventList = new LinkedList<>();
        this.playerMap = new HashMap<>();
        this.allReady = false;
    }

    /**
     * Tells whether every player in this match is ready
     * @return true iff every player in this match is ready
     */
    public boolean isAllReady() {
        return allReady;
    }

    /**
     * Gets the game associated with this match
     * @return the game associated with this match
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the scores of users in this match
     * @return a leaderboard with scores of users in this match
     */
    public List<LeaderboardEntry> getScores() {
        return scores;
    }

    /**
     * Gets the event callers for this match
     * @return the list of event callers for this match
     */
    public List<GameController.EventCaller<LobbyResponsePacket>> getPlayerEventList() {
        return playerEventList;
    }

    /**
     * Gets the players, their ready state and ping time for this match
     * @return the map containing players' ready state and ping time for this match
     */
    public Map<String, Map.Entry<String, LocalDateTime>> getPlayerMap() {
        return playerMap;
    }

    /**
     * Asserts whether every player in this game is ready
     * @param allReady whether every player in this game is ready
     */
    public void setAllReady(boolean allReady) {
        this.allReady = allReady;
    }

    /**
     * Adds the specified amount of points to the player's score
     * @param player the player to update the score of
     * @param score the number of points to add
     */
    public void addScore(String player, int score){
        //I used a list of leaderboardentries because that is
        // what's returned in a LeaderboardResponsePacket.
        for (LeaderboardEntry l : scores){
            if (l.username.equals(player)){
                l.points += score;
            }
        }
    }

    /**
     * trim the playerList (removes the LocalDateTime field from the Map)
     *
     * @return the trimmed player list to be sent to the client
     */
    public Map<String, String> trimPlayerList() {
        Map<String, String> trimmedMap = new HashMap<>();
        for (String player : playerMap.keySet()) {
            trimmedMap.put(player, playerMap.get(player).getKey());
        }
        return trimmedMap;
    }
}
