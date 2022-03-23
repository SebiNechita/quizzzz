package server.api.game;

import org.springframework.stereotype.Service;
import packets.GeneralResponsePacket;
import packets.LobbyResponsePacket;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GameService {

    private final List<String> playerList;
    private final List<GameController.EventCaller<GeneralResponsePacket>> playerEventList;

    public GameService() {
        this.playerList = new ArrayList<>();
        this.playerEventList = new LinkedList<>();
    }

    public void addPlayer(String player) {
        playerList.add(player);
    }

    public void removePlayer(String player) {
        playerList.remove(player);
    }

    public List<String> getPlayers() {
        return playerList;
    }

    public void waitForPlayerEvent(GameController.EventCaller<GeneralResponsePacket> eventCaller) {
        playerEventList.add(eventCaller);
    }

    public void onPlayerEvent(String type, String content, String from) {
        for (GameController.EventCaller<GeneralResponsePacket> thread : playerEventList) {
            if(type.equals("Emote")) {
                thread.run(new LobbyResponsePacket("Emote", content, from));
            }

        }
        playerEventList.clear();
    }
}
