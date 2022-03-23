package client.game;

import client.scenes.LobbyCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import packets.EmoteRequestPacket;
import packets.JoinRequestPacket;
import packets.JoinResponsePacket;
import packets.LobbyResponsePacket;


public class MultiplayerGame {
    private final MainCtrl main;
    private final ServerUtils server;

    public MultiplayerGame(MainCtrl main, ServerUtils server) {
        this.main = main;
        this.server = server;
    }

    /**
     * Join a player in a lobby
     *
     * @param username
     * @return JoinRequestPacket
     */
    public JoinResponsePacket join(String username) {
        return server.postRequest("api/game/join",
                new JoinRequestPacket(username),
                JoinResponsePacket.class);
    }

    public LobbyResponsePacket sendEmote(String username, String emoteNo) {
        return server.postRequest("api/game/emote",
                new EmoteRequestPacket(username, emoteNo),
                LobbyResponsePacket.class);
    }

    private class LobbyOnResponse implements ServerUtils.ServerResponse<LobbyResponsePacket> {
        @Override
        public void run(LobbyResponsePacket responsePacket) {
            if (responsePacket.getType().equals("Emote")) {
                main.getCtrl(LobbyCtrl.class).updateEmoji1(responsePacket.getFrom());
            }

        }
    }

    public void getLobbyUpdate() {
        ServerUtils.LongPollingRequest<LobbyResponsePacket> request
                = server.longGetRequest("api/game/lobbyEventListener", LobbyResponsePacket.class, new LobbyOnResponse());
        request.getRequest();
    }
}
