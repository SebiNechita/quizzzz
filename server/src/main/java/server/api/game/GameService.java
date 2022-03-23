package server.api.game;

import org.springframework.stereotype.Service;
import packets.LobbyResponsePacket;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@Service
public class GameService {

    private final List<String> playerList;
    private final List<GameController.EventCaller<LobbyResponsePacket>> playerEventList;

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

    public void waitForPlayerEvent(GameController.EventCaller<LobbyResponsePacket> eventCaller) {
        playerEventList.add(eventCaller);
    }

    public void onPlayerEvent(String type, String content, String from) {
        for (GameController.EventCaller<LobbyResponsePacket> thread : playerEventList) {
            // if the user in the list is different from the emote sender
            if(type.equals("Emote") && !thread.getUsername().equals(from)) {
                thread.run(new LobbyResponsePacket("Emote", content, from));
            } else if(type.equals("Join") && !thread.getUsername().equals(from)) {
                thread.run(new LobbyResponsePacket("Join", content, from));
            }

        }
        ListIterator<GameController.EventCaller<LobbyResponsePacket>> iter = playerEventList.listIterator();
        while(iter.hasNext()){
            if(!iter.next().getUsername().equals(from)){
                iter.remove();
            }
        }

        //playerEventList.clear();
    }
}
