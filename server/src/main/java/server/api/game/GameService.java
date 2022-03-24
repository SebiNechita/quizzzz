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

    public void onPlayerEvent(String type, String content, String from) {
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            // if the user in the list is different from the emote sender
            if (type.equals("Emote") && !thread.getUsername().equals(from)) {
                thread.run(new LobbyResponsePacket("Emote", content, from));
            } else if (type.equals("Join") && !thread.getUsername().equals(from)) {
                thread.run(new LobbyResponsePacket("Join", content, from));
            }
        }

    }

    public void onPlayerReady(String type, String content, String from) {

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
                for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
                    thread.run(new LobbyResponsePacket("AllReady", "true", from));
                }
            } else {
                for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
                    thread.run(new LobbyResponsePacket("Ready", content, from));
                }
            }

            // if all ready currently
        } else {
            // update sender ready state
            playerMap.put(from, content);
            // send ready message to other player
            for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
                thread.run(new LobbyResponsePacket("NotAllReady", "false", from));
            }

        }

        // clear threads except the caller it itself
        ListIterator<GameController.EventCaller<LobbyResponsePacket>> iter = playerEventList.listIterator();
        while (iter.hasNext()) {
            if (!iter.next().getUsername().equals(from)) {
                iter.remove();
            }
        }

    }

}
