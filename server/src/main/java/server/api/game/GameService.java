package server.api.game;

import commons.Game;
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
    private final Map<String, Map.Entry<String, LocalDateTime>> playerMap;
    private Game game;
    private final List<GameController.EventCaller<LobbyResponsePacket>> playerEventList;
    private boolean allReady;

    /**
     * constructor for GameService
     *
     * @param createGameService
     */
    public GameService(CreateGameService createGameService) {
        this.playerMap = new HashMap<>();
        this.playerEventList = new LinkedList<>();
        this.allReady = false;
        this.createGameService = createGameService;
    }

    /**
     * a scheduled task for removing disconnected player from the player list
     */
    @Scheduled(fixedRate = 2000)
    public void checkLastPing() {
        var now = LocalDateTime.now();

        var iter = playerMap.entrySet().iterator();
        while (iter.hasNext()) {
            var entry = iter.next();
            System.out.println(Duration.between(entry.getValue().getValue(), now).toMillis());
            if (Duration.between(entry.getValue().getValue(), now).toMillis() > 2000) {
                String username = entry.getKey();
                iter.remove();
                onPlayerLeave(username);
                System.out.println("removed player");
            }

        }

        System.out.println("Working");
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

    /**
     * method for adding a new player to the playerMap with false ready state and localtime
     *
     * @param player player to be added
     */
    public void addPlayer(String player) {
        playerMap.put(player, Map.entry("false", LocalDateTime.now()));
    }

    /**
     * method for removing a player from playerList
     *
     * @param player player to be removed
     */
    public void removePlayer(String player) {
        playerMap.remove(player);
    }

    /**
     * get method for playerMap
     *
     * @return playerMap
     */
    public Map<String, Map.Entry<String, LocalDateTime>> getPlayers() {
        return playerMap;
    }

    /**
     * get method for the playerEventList
     *
     * @return playerEventList
     */
    public List<GameController.EventCaller<LobbyResponsePacket>> getPlayerEventList() {
        return this.playerEventList;
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
            playerMap.put(from, Map.entry(content, LocalDateTime.now()));
            trimmedMap = trimPlayerList();
            int readyCount = 0;
            for (Map.Entry<String, LocalDateTime> value : playerMap.values()) {
                if (value.getKey().equals("true")) {
                    readyCount += 1;
                }
            }

            //if all players are ready
            if (readyCount == playerMap.size()) {
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
            playerMap.put(from, Map.entry(content, LocalDateTime.now()));
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
    public Map<String, String> trimPlayerList() {
        Map<String, String> trimmedMap = new HashMap<>();
        for (String player : this.playerMap.keySet()) {
            trimmedMap.put(player, this.playerMap.get(player).getKey());
        }
        return trimmedMap;
    }

    /**
     * update the last time a client ping the server
     *
     * @param username the last pinged time of this username will be updated.
     */
    public void updatePlayerTime(String username) {
        String ready = playerMap.get(username).getKey();
        playerMap.put(username, Map.entry(ready, LocalDateTime.now()));
    }

    /**
     * Return a game
     * @return A game
     */
    public Game getGameIfExists() {
        if (game == null) {
            game = createGameService.createGame(20).getGame();
        }
        return game;
    }

    /**
     * Starts a game when a player presses "start" and informs other players
     * @param requestPacket The reques packet
     * @return LobbyResponsePacket
     */
    public LobbyResponsePacket onStartGame(StartGameRequestPacket requestPacket) {
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
