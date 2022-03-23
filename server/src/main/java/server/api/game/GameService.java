package server.api.game;

import commons.utils.HttpStatus;
import org.springframework.stereotype.Service;
import packets.GeneralResponsePacket;

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

    public void onPlayerEvent() {
        for (GameController.EventCaller<GeneralResponsePacket> thread : playerEventList) {
            thread.run(new GeneralResponsePacket(HttpStatus.OK));
        }
        playerEventList.clear();
    }
}
