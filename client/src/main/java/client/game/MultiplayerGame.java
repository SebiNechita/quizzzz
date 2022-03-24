package client.game;

import client.scenes.LobbyCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import javafx.application.Platform;
import packets.*;


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

    public GeneralResponsePacket sendEmote(String username, String emoteNo) {
        return server.postRequest("api/game/emote",
                new EmoteRequestPacket(username, emoteNo),
                GeneralResponsePacket.class);
    }

    public GeneralResponsePacket sendReadyMsg(String username, boolean isReady) {
        String isReadyStr = "false";
        if (isReady) {
            isReadyStr = "true";
        }

        return server.postRequest("api/game/ready",
                new ReadyRequestPacket(username, isReadyStr),
                GeneralResponsePacket.class);
    }

    public void getLobbyUpdate() {
        ServerUtils.LongPollingRequest<LobbyResponsePacket> request
                = server.longGetRequest("api/game/lobbyEventListener", LobbyResponsePacket.class, new LobbyOnResponse());
        // set persistent long-polling to true
        request.setPersistent(true);
        request.getRequest();
    }

    private class LobbyOnResponse implements ServerUtils.ServerResponse<LobbyResponsePacket> {
        @Override
        public void run(LobbyResponsePacket responsePacket) {
            if (responsePacket.getType().equals("Emote")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji1(responsePacket.getFrom()));
            } else if (responsePacket.getType().equals("Join")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .showJoinMsg(responsePacket.getFrom()));
            } else if (responsePacket.getType().equals("Ready")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateReady(responsePacket.getFrom(), responsePacket.getContent()));
            } else if (responsePacket.getType().equals("AllReady")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateReady(responsePacket.getFrom(), responsePacket.getContent()));
            } else if (responsePacket.getType().equals("NotAllReady")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateReady(responsePacket.getFrom(), responsePacket.getContent()));
            }

        }

    }
}



