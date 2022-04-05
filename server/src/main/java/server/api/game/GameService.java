package server.api.game;

import commons.Game;
import commons.LeaderboardEntry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import packets.JokerRequestPacket;
import packets.JokerResponsePacket;
import packets.LobbyResponsePacket;
import packets.StartGameRequestPacket;
import server.Match;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GameService {
    private final CreateGameService createGameService;
    private final Map<String,Match> playerMatchMap;
    private final List<Match> matches;
    private Match currentMatch;


    /**
     * constructor for GameService
     *
     * @param createGameService
     */
    public GameService(CreateGameService createGameService) {
        this.createGameService = createGameService;
        this.playerMatchMap = new HashMap<>();
        this.matches = new LinkedList<>();
    }

    /**
     * Scheduled task for removing old players from every match
     */
    @Scheduled(fixedRate = 2000)
    public void checkLastPing() {
        for (Match match : matches){
            removeOldPlayers(match.getPlayerMap());
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
                removePlayer(username);
            }
        }
    }

    /**
     * notifies all other players when a player leaves/is disconnected.
     *
     * @param player player who is disconnected/left
     */
    public void onPlayerLeave(String player) {

        List<GameController.EventCaller<LobbyResponsePacket>> playerEventList = playerMatchMap.get(player).getPlayerEventList();

        var trimmedMap = playerMatchMap.get(player).trimPlayerList();

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
        currentMatch.getPlayerMap().put(player, Map.entry("false", LocalDateTime.now()));
        playerMatchMap.put(player,currentMatch);
        currentMatch.getScores().add(new LeaderboardEntry(0,player));
        return game;
    }



    /**
     * Removes a player from playerList of the match the player is in
     *
     * @param player player to be removed
     */
    public void removePlayer(String player) {
        //notifies other players
        onPlayerLeave(player);
        //gets the game associated with the user and removes them from the playermap
        playerMatchMap.get(player).getPlayerMap().remove(player);
        //also remove the information that the user is currently in a match
        playerMatchMap.remove(player);
    }

    /**
     * adds the long polling contact of the user specified in the eventCaller
     *
     * @param eventCaller eventCaller which handles the long polling reqest
     */
    public void waitForPlayerEvent(GameController.EventCaller<LobbyResponsePacket> eventCaller) {
        playerMatchMap.get(eventCaller.getUsername()).getPlayerEventList().add(eventCaller);
    }

    /**
     * send emote to other players
     *
     * @param type     should be "Emote"
     * @param emoteStr emote name
     * @param from     sender of the emote
     */
    public void onEmoteReceived(String type, String emoteStr, String from) {
        //find the long polling contacts of the game the player takes part in
        for (GameController.EventCaller<LobbyResponsePacket> thread :
                playerMatchMap.get(from).getPlayerEventList()) {
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
        for (GameController.EventCaller<LobbyResponsePacket> thread :
                playerMatchMap.get(from).getPlayerEventList()) {
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
        var trimmedMap = playerMatchMap.get(from).trimPlayerList();

        for (GameController.EventCaller<LobbyResponsePacket> thread :
                playerMatchMap.get(from).getPlayerEventList()) {
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
        //get the match the person participates in
        Match match = playerMatchMap.get(from);
        // if not all ready currently (before the request)
        if (!match.isAllReady()) {
            // update sender ready state
            match.getPlayerMap().put(from, Map.entry(content, LocalDateTime.now()));
            trimmedMap = match.trimPlayerList();
            int readyCount = 0;
            for (Map.Entry<String, LocalDateTime> value : match.getPlayerMap().values()) {
                if (value.getKey().equals("true")) {
                    readyCount += 1;
                }
            }

            //if all players are ready
            if (readyCount == currentMatch.getPlayerMap().size()) {
                currentMatch.setAllReady(true);
                for (GameController.EventCaller<LobbyResponsePacket> thread : match.getPlayerEventList()) {
                    if (!thread.getUsername().equals(from)) {
                        thread.run(new LobbyResponsePacket("AllReady", "true", from, trimmedMap));
                    }

                }
                clearEventList(from);
                return new LobbyResponsePacket("AllReady", "true", from, trimmedMap);

                // if not all players are ready
            } else {
                for (GameController.EventCaller<LobbyResponsePacket> thread : match.getPlayerEventList()) {
                    if (!thread.getUsername().equals(from)) {
                        thread.run(new LobbyResponsePacket("Ready", content, from, trimmedMap));
                    }

                }
                clearEventList(from);
                return new LobbyResponsePacket("Ready", content, from, trimmedMap);

            }

            // if all players are ready currently (before this request)
        } else {
            match.setAllReady(false);
            // update sender ready state
            match.getPlayerMap().put(from, Map.entry(content, LocalDateTime.now()));
            trimmedMap = match.trimPlayerList();
            // send ready message to other player

            for (GameController.EventCaller<LobbyResponsePacket> thread : match.getPlayerEventList()) {
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
        ListIterator<GameController.EventCaller<LobbyResponsePacket>> iter =
                playerMatchMap.get(player).getPlayerEventList().listIterator();
        while (iter.hasNext()) {
            if (!iter.next().getUsername().equals(player)) {
                iter.remove();
            }
        }
    }



    /**
     * update the last time a client ping the server in the user's game
     *
     * @param username the last pinged time of this username will be updated.
     */
    public void updatePlayerTime(String username) {
        Map<String, Map.Entry<String, LocalDateTime>> playerMap = playerMatchMap.get(username).getPlayerMap();
        //Puts an entry with the ready state and time with the name as key
        String ready = playerMap.get(username).getKey();
        playerMap.put(username, Map.entry(ready, LocalDateTime.now()));
    }

    /**
     * Returns current game, or creates a new one.
     * @return A game
     */
    public Game getGameIfExists() {
        if (currentMatch == null) {
            currentMatch = new Match(createGameService.createGame(20).getGame());
            //I used this list when checking for disconnected players in all running matches
            matches.add(currentMatch);
        }
        return currentMatch.getGame();
    }

    //used for tests
    public Match getCurrentMatch() {
        return currentMatch;
    }

    //used for tests
    public void setCurrentMatch(Match currentMatch) {
        this.currentMatch = currentMatch;
    }

    /**
     * Starts a game and informs players. Resets the game.
     * @param requestPacket The reques packet
     * @return LobbyResponsePacket
     */
    public LobbyResponsePacket onStartGame(StartGameRequestPacket requestPacket) {

        Map<String, String> trimmedMap = currentMatch.trimPlayerList();
        for (GameController.EventCaller<LobbyResponsePacket> thread : currentMatch.getPlayerEventList()) {
            thread.run(new LobbyResponsePacket("Start", "true", requestPacket.getUsername(), trimmedMap));
        }
        currentMatch = null;
        return new LobbyResponsePacket("Start", "true", requestPacket.getUsername(), trimmedMap);
    }

    /**
     * When a player uses the Half-time joker, other players get the effect from that joker
     * @param requestPacket The request packet
     * @return JokerResponsePacket
     */
    public JokerResponsePacket onJokerGame(JokerRequestPacket requestPacket) {
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerMatchMap.get(requestPacket.getUsername()).getPlayerEventList()) {
            if (requestPacket.getScene().equals("client.scenes.GameMultiChoiceCtrl")) {
                thread.run(new JokerResponsePacket("JokerMultiChoice", "true", requestPacket.getUsername(), requestPacket.getJokerType()));
            } else if (requestPacket.getScene().equals("client.scenes.GameOpenQuestionCtrl")) {
                thread.run(new JokerResponsePacket("JokerOpenQuestion", "true", requestPacket.getUsername(), requestPacket.getJokerType()));
            }

        }

        return new JokerResponsePacket("Joker", "true", requestPacket.getUsername(), requestPacket.getJokerType());
    }

    /**
     * Gets leaderboard from given player's match
     * @param username the player
     * @return the leaderboard
     */
    public List<LeaderboardEntry> getScoresByUser(String username) {
        return playerMatchMap.get(username).getScores();
    }

    /**
     * Adds given points to the given player's score
     * @param player the player whose score need to be updated
     * @param score the points to add
     */
    public void addScore(String player, int score) {
        playerMatchMap.get(player).addScore(player,score);
    }
}
