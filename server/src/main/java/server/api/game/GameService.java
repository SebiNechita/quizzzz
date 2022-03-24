package server.api.game;

import org.springframework.stereotype.Service;
import packets.LobbyResponsePacket;

import java.util.*;

@Service
public class GameService {

    private final Map<String, String> playerMap;
    private final List<GameController.EventCaller<LobbyResponsePacket>> playerEventList;
    private boolean allReady;

    public GameService() {
        this.playerMap = new HashMap<>();
        this.playerEventList = new LinkedList<>();
        this.allReady = false;
    }

    public void addPlayer(String player) {
        playerMap.put(player, "false");
    }

    public void removePlayer(String player) {
        playerMap.remove(player);
    }

    public Map<String, String> getPlayers() {
        return playerMap;
    }

    public void waitForPlayerEvent(GameController.EventCaller<LobbyResponsePacket> eventCaller) {
        playerEventList.add(eventCaller);
    }

    public void onEmoteReceived(String type, String emoteNo, String from) {
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            // if the user in the list is different from the emote sender
            if (type.equals("Emote") && !thread.getUsername().equals(from)) {
                thread.run(new LobbyResponsePacket("Emote", emoteNo, from));
            }
        }
        clearEventList(from);
    }

    public Map<String, String> onPlayerJoin(String from) {
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            if (!thread.getUsername().equals(from)) {
                // send join MSG to other player
                thread.run(new LobbyResponsePacket("Join", "", from, playerMap));
            }
        }
        // return player list to caller
        clearEventList(from);
        return playerMap;
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
        // if not all ready currently
        if (!allReady) {
            // update sender ready state
            playerMap.put(from, content);
            int readyCount = 0;
            for (String value : playerMap.values()) {
                if (value.equals("true")) {
                    readyCount += 1;
                }
            }
            //if all players are ready
            if (readyCount == playerMap.size()) {
                allReady = true;
                for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
                    if (!thread.getUsername().equals(from)) {
                        thread.run(new LobbyResponsePacket("AllReady", "true", from, playerMap));
                    }

                }
                clearEventList(from);
                return new LobbyResponsePacket("AllReady", "true", from, playerMap);

                // if not all players are ready
            } else {
                for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
                    if (!thread.getUsername().equals(from)) {
                        thread.run(new LobbyResponsePacket("Ready", content, from, playerMap));
                    }

                }
                clearEventList(from);
                return new LobbyResponsePacket("Ready", content, from, playerMap);

            }

            // if all ready currently
        } else {
            allReady = false;
            // update sender ready state
            playerMap.put(from, content);
            // send ready message to other player

            for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
                if (!thread.getUsername().equals(from)) {
                    thread.run(new LobbyResponsePacket("CancelAllReady", "false", from, playerMap));
                }
            }
            clearEventList(from);
            return new LobbyResponsePacket("CancelAllReady", "false", from, playerMap);

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

}
