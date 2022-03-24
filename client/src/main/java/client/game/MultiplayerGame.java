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
     * Join a player to a lobby
     *
     * @param username
     */
    public void join(String username) {

        JoinResponsePacket responsePacket = server.postRequest("api/game/join",
                new JoinRequestPacket(username),
                JoinResponsePacket.class);
        Platform.runLater(() ->
                main.getCtrl(LobbyCtrl.class)
                        .updatePlayerList(responsePacket.getPlayerList()));
    }

    public GeneralResponsePacket sendEmote(String username, String emoteNo) {
        return server.postRequest("api/game/emote",
                new EmoteRequestPacket(username, emoteNo),
                GeneralResponsePacket.class);
    }

    public void sendReadyMsg(String username, boolean isReady) {
        String isReadyStr = "false";
        if (isReady) {
            isReadyStr = "true";
        }

        LobbyResponsePacket responsePacket = server.postRequest("api/game/ready",
                new ReadyRequestPacket(username, isReadyStr),
                LobbyResponsePacket.class);

        // type equals "Ready"
        Platform.runLater(() ->
                main.getCtrl(LobbyCtrl.class)
                        .updatePlayerList(responsePacket.getPlayerList()));

        if (responsePacket.getType().equals("AllReady")) {
            Platform.runLater(() ->
                    main.getCtrl(LobbyCtrl.class)
                            .showStartButton());
        } else if (responsePacket.getType().equals("CancelAllReady")) {
            Platform.runLater(() ->
                    main.getCtrl(LobbyCtrl.class)
                            .hideStartButton());
        }

    }

    public void getLobbyUpdate() {
        ServerUtils.LongPollingRequest<LobbyResponsePacket> request
                = server.longGetRequest("api/game/lobbyEventListener",
                LobbyResponsePacket.class, new LobbyOnResponse());
        // set persistent long-polling to true
        request.setPersistent(true);
        request.getRequest();
    }

    private class LobbyOnResponse implements ServerUtils.ServerResponse<LobbyResponsePacket> {
        @Override
        public void run(LobbyResponsePacket responsePacket) {
            if (responsePacket.getType().equals("Emote")) {
                updateEmote(responsePacket.getFrom(), responsePacket.getContent());
            } else if (responsePacket.getType().equals("Join")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
            } else if (responsePacket.getType().equals("Ready")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
            } else if (responsePacket.getType().equals("AllReady")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .showStartButton());
            } else if (responsePacket.getType().equals("CancelAllReady")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .hideStartButton());
            }


        }

        private void updateEmote(String from, String emoteNo) {
            if (emoteNo.equals("1")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji1(from));
            } else if (emoteNo.equals("2")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji2(from));
            } else if (emoteNo.equals("3")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji3(from));
            } else if (emoteNo.equals("4")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji4(from));
            } else if (emoteNo.equals("5")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji5(from));
            } else if (emoteNo.equals("6")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji6(from));
            } else if (emoteNo.equals("7")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji7(from));
            } else if (emoteNo.equals("8")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji8(from));
            } else if (emoteNo.equals("9")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji9(from));
            } else if (emoteNo.equals("10")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji10(from));
            } else if (emoteNo.equals("11")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji11(from));
            } else if (emoteNo.equals("12")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji12(from));
            } else if (emoteNo.equals("13")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji13(from));
            } else if (emoteNo.equals("14")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateEmoji14(from));
            }
        }

    }
}



