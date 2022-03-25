package server.api.game;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import packets.LobbyResponsePacket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GameService {

    private final Map<String, Map.Entry<String, LocalDateTime>> playerMap;
    private final List<GameController.EventCaller<LobbyResponsePacket>> playerEventList;
    private boolean allReady;

    public GameService() {
        this.playerMap = new HashMap<>();
        this.playerEventList = new LinkedList<>();
        this.allReady = false;
    }

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

    private void onPlayerLeave(String player) {

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

    public void addPlayer(String player) {
        playerMap.put(player, Map.entry("false", LocalDateTime.now()));
    }

    public void removePlayer(String player) {
        playerMap.remove(player);
    }

    public Map<String, Map.Entry<String, LocalDateTime>> getPlayers() {
        return playerMap;
    }

    public void waitForPlayerEvent(GameController.EventCaller<LobbyResponsePacket> eventCaller) {
        playerEventList.add(eventCaller);
    }

    public void onEmoteReceived(String type, String emoteStr, String from) {
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            // if the user in the list is different from the emote sender
            if (type.equals("Emote") && !thread.getUsername().equals(from)) {
                thread.run(new LobbyResponsePacket("Emote", emoteStr, from));
            }
        }
        clearEventList(from);
    }

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
     * event handler on receiving player ready message
     *
     * @param type
     * @param content
     * @param from
     * @return return true if all players are ready
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
     * clear all long-polling event list except the caller
     *
     * @param player
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
     * @param username
     */
    public void updatePlayerTime(String username) {
        String ready = playerMap.get(username).getKey();
        playerMap.put(username, Map.entry(ready, LocalDateTime.now()));
    }
}
