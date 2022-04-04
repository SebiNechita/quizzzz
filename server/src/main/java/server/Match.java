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
    //TODO: Can we create two separate maps for this? It seems like we never
    // use/update BOTH the time and the ready state of a player.
    // Actually, the ready state is only relevant for the lobby, so we don't
    // even need to store that for every match, but only for the lobby itself
    private final Map<String, Map.Entry<String, LocalDateTime>> playerMap;
    private boolean allReady;

    public Match(Game game) {
        this.game = game;
        this.scores = new LinkedList<>();
        this.playerEventList = new LinkedList<>();
        this.playerMap = new HashMap<>();
        this.allReady = false;
    }

    public boolean isAllReady() {
        return allReady;
    }

    public Game getGame() {
        return game;
    }

    public List<LeaderboardEntry> getScores() {
        return scores;
    }

    public List<GameController.EventCaller<LobbyResponsePacket>> getPlayerEventList() {
        return playerEventList;
    }

    public Map<String, Map.Entry<String, LocalDateTime>> getPlayerMap() {
        return playerMap;
    }

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
    //TODO: replace with finding the match based on the name of the player who sent the request
    public Map<String, String> trimPlayerList() {
        Map<String, String> trimmedMap = new HashMap<>();
        for (String player : playerMap.keySet()) {
            trimmedMap.put(player, playerMap.get(player).getKey());
        }
        return trimmedMap;
    }
}
