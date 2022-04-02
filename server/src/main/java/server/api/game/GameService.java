package server.api.game;

import commons.Game;
import commons.LeaderboardEntry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import packets.JokerRequestPacket;
import packets.JokerResponsePacket;
import packets.LobbyResponsePacket;
import packets.StartGameRequestPacket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GameService {

    private final CreateGameService createGameService;
    private Map<String, Map.Entry<String, LocalDateTime>> currentMatchPlayerMap;
    //TODO: Can we create two separate maps for this? It seems like we never
    // use/update BOTH the time and the ready state of a player.
    // Actually, the ready state is only relevant for the lobby, so we don't
    // even need to store that for every match, but only for the lobby itself
    private final Map<String,Map<String, Map.Entry<String, LocalDateTime>>> playerMatchMap;
    private final List<Map> matches;
    private Game game;
    private final List<GameController.EventCaller<LobbyResponsePacket>> playerEventList;
    private boolean allReady;
    private List<LeaderboardEntry> scores;

    /**
     * constructor for GameService
     *
     * @param createGameService
     */
    public GameService(CreateGameService createGameService) {
        this.playerEventList = new LinkedList<>();
        this.allReady = false;
        this.createGameService = createGameService;
        this.scores = new LinkedList<>();
        this.playerMatchMap = new HashMap<>();
        this.matches = new LinkedList<>();
        //TODO: Remove this. Only using temporarily for tests to pass
        // because I changed the class structure
        this.currentMatchPlayerMap = new HashMap<>();
    }

    /**
     * Scheduled task for removing old players from every match
     */
    @Scheduled(fixedRate = 2000)
    public void checkLastPing() {
        for (Map match : matches){
            removeOldPlayers(match);
        }
    }

    /**
     * Removes players from the given match who didn't respond in 2s
     * @param playerMatchMap the match
     */
    public void removeOldPlayers(Map<String, Map.Entry<String, LocalDateTime>> playerMatchMap){
        var now = LocalDateTime.now();
        var iter = playerMatchMap.entrySet().iterator();
        while (iter.hasNext()) {
            var entry = iter.next();
            if (Duration.between(entry.getValue().getValue(), now).toMillis() > 2000) {
                String username = entry.getKey();
                iter.remove();
                onPlayerLeave(username);
                System.out.println("removed player");
            }

        }
    }

    /**
     * notifies all other players when a player leaves/is disconnected.
     *
     * @param player player who is disconnected/left
     */
    public void onPlayerLeave(String player) {

        var trimmedMap = trimPlayerList();

        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            if (!thread.getUsername().equals(player)) {
                // send join MSG to other player
                thread.run(new LobbyResponsePacket("Leave", "", player, trimmedMap));
            }
        }
        // clear all player
        playerEventList.clear();
    }

    /** Adds a new player to the playerMap with false ready state and localtime
     * and sets their score to 0
     * @param player player to be added
     * @return the game the player has joined
     */
    public Game addPlayer(String player) {
        Game game = getGameIfExists();
        currentMatchPlayerMap.put(player, Map.entry("false", LocalDateTime.now()));
        playerMatchMap.put(player,currentMatchPlayerMap);
        scores.add(new LeaderboardEntry(0,player));
        return game;
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
     * Removes a player from playerList of the match the player is in
     *
     * @param player player to be removed
     */
    public void removePlayer(String player) {
        playerMatchMap.get(player).remove(player);
        //also remove the information that the user is currently in a match
        playerMatchMap.remove(player);
    }

    /**
     * get method for playerMap
     *
     * @return playerMap
     */
    //TODO: replace with finding the match based on the name of the player who sent the request
    public Map<String, Map.Entry<String, LocalDateTime>> getPlayers() {
        return currentMatchPlayerMap;
    }

    /**
     * get method for the playerEventList
     *
     * @return playerEventList
     */
    public List<GameController.EventCaller<LobbyResponsePacket>> getPlayerEventList() {
        return this.playerEventList;
    }

    public List<LeaderboardEntry> getScores() {
        return scores;
    }

    /**
     * add EventCaller to playerEventList
     *
     * @param eventCaller eventCaller which handles the long polling reqest
     */
    public void waitForPlayerEvent(GameController.EventCaller<LobbyResponsePacket> eventCaller) {
        playerEventList.add(eventCaller);
    }

    /**
     * send emote to other players
     *
     * @param type     should be "Emote"
     * @param emoteStr emote name
     * @param from     sender of the emote
     */
    public void onEmoteReceived(String type, String emoteStr, String from) {
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            // if the user in the list is different from the emote sender
            if (type.equals("Emote") && !thread.getUsername().equals(from)) {
                thread.run(new LobbyResponsePacket("Emote", emoteStr, from));
            }
        }
        clearEventList(from);
    }

    /**
     * send joker notification to other players
     *
     * @param type     should be "JokerNotification"
     * @param jokerNotification joker type
     * @param from     sender of the emote
     */
    public void onJokerNotificationReceived(String type, String jokerNotification, String from) {
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            // if the user in the list is different from the notification sender
            if (type.equals("JokerNotification") && !thread.getUsername().equals(from)) {
                thread.run(new LobbyResponsePacket("JokerNotification", jokerNotification, from));
            }
        }
        clearEventList(from);
    }

    /**
     * sends join message to all other players in the lobby
     *
     * @param from player who just entered the lobby
     * @return updated player list
     */
    public Map<String, String> onPlayerJoin(String from) {
        var trimmedMap = trimPlayerList();

        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            if (!thread.getUsername().equals(from)) {
                // send join MSG to other player
                thread.run(new LobbyResponsePacket("Join", "", from, trimmedMap));
            }
        }
        // return player list to caller
        clearEventList(from);
        return trimmedMap;
    }

    /**
     * update ready message to all players other than the sender
     *
     * @param type    should be "Ready"
     * @param content "true" or "false"
     * @param from    player who sent this message
     * @return LobbyResponsePacket
     */
    public LobbyResponsePacket onPlayerReady(String type, String content, String from) {
        Map<String, String> trimmedMap;
        // if not all ready currently (before the request)
        if (!allReady) {
            // update sender ready state
            currentMatchPlayerMap.put(from, Map.entry(content, LocalDateTime.now()));
            trimmedMap = trimPlayerList();
            int readyCount = 0;
            for (Map.Entry<String, LocalDateTime> value : currentMatchPlayerMap.values()) {
                if (value.getKey().equals("true")) {
                    readyCount += 1;
                }
            }

            //if all players are ready
            if (readyCount == currentMatchPlayerMap.size()) {
                allReady = true;
                for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
                    if (!thread.getUsername().equals(from)) {
                        thread.run(new LobbyResponsePacket("AllReady", "true", from, trimmedMap));
                    }

                }
                clearEventList(from);
                return new LobbyResponsePacket("AllReady", "true", from, trimmedMap);

                // if not all players are ready
            } else {
                for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
                    if (!thread.getUsername().equals(from)) {
                        thread.run(new LobbyResponsePacket("Ready", content, from, trimmedMap));
                    }

                }
                clearEventList(from);
                return new LobbyResponsePacket("Ready", content, from, trimmedMap);

            }

            // if all players are ready currently (before this request)
        } else {
            allReady = false;
            // update sender ready state
            currentMatchPlayerMap.put(from, Map.entry(content, LocalDateTime.now()));
            trimmedMap = trimPlayerList();
            // send ready message to other player

            for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
                if (!thread.getUsername().equals(from)) {
                    thread.run(new LobbyResponsePacket("CancelAllReady", "false", from, trimmedMap));
                }
            }
            clearEventList(from);
            return new LobbyResponsePacket("CancelAllReady", "false", from, trimmedMap);
        }

    }

    /**
     * clear all long-polling event list except the sender
     *
     * @param player clears all eventCaller except this player
     */
    public void clearEventList(String player) {
        // clear threads except the caller it itself
        ListIterator<GameController.EventCaller<LobbyResponsePacket>> iter = playerEventList.listIterator();
        while (iter.hasNext()) {
            if (!iter.next().getUsername().equals(player)) {
                iter.remove();
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
        for (String player : this.currentMatchPlayerMap.keySet()) {
            trimmedMap.put(player, this.currentMatchPlayerMap.get(player).getKey());
        }
        return trimmedMap;
    }

    /**
     * update the last time a client ping the server
     *
     * @param username the last pinged time of this username will be updated.
     */
    public void updatePlayerTime(String username) {
        Map<String, Map.Entry<String, LocalDateTime>> playerMap = playerMatchMap.get(username);
        //Puts an entry with the ready state and time with the name as key
        String ready = playerMap.get(username).getKey();
        playerMap.put(username, Map.entry(ready, LocalDateTime.now()));
    }

    /**
     * Returns current game, or creates a new one.
     * @return A game
     */
    public Game getGameIfExists() {
        if (game == null) {
            currentMatchPlayerMap = new HashMap<>();
            //I used this list when checking for disconnected players in all running matches
            matches.add(currentMatchPlayerMap);
            game = createGameService.createGame(20).getGame();
        }
        return game;
    }

    /**
     * Starts a game and informs players. Resets the game.
     * @param requestPacket The reques packet
     * @return LobbyResponsePacket
     */
    public LobbyResponsePacket onStartGame(StartGameRequestPacket requestPacket) {
        game = null;
        Map<String, String> trimmedMap = trimPlayerList();
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            thread.run(new LobbyResponsePacket("Start", "true", requestPacket.getUsername(), trimmedMap));
        }
        return new LobbyResponsePacket("Start", "true", requestPacket.getUsername(), trimmedMap);
    }

    /**
     * When a player uses the Half-time joker, other players get the effect from that joker
     * @param requestPacket The request packet
     * @return JokerResponsePacket
     */
    public JokerResponsePacket onJokerGame(JokerRequestPacket requestPacket) {
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            if (requestPacket.getScene().equals("client.scenes.GameMultiChoiceCtrl")) {
                thread.run(new JokerResponsePacket("JokerMultiChoice", "true", requestPacket.getUsername(), requestPacket.getJokerType()));
            } else if (requestPacket.getScene().equals("client.scenes.GameOpenQuestionCtrl")) {
                thread.run(new JokerResponsePacket("JokerOpenQuestion", "true", requestPacket.getUsername(), requestPacket.getJokerType()));
            }

        }

        return new JokerResponsePacket("Joker", "true", requestPacket.getUsername(), requestPacket.getJokerType());
    }
}
