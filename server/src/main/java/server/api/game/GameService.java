package server.api.game;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GameService {

    private final List<String> playerList;
    private final List<Thread> playerEventList;

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

    public void waitForPlayerEvent(Thread thread) {
        playerEventList.add(thread);
    }

    public void onPlayerEvent() {
        for (Thread thread : playerEventList) {
            thread.start();
        }
    }
}
