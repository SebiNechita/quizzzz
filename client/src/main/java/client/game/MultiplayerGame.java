package client.game;

import client.scenes.LobbyCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import javafx.application.Platform;
import packets.*;

import java.util.concurrent.*;


public class MultiplayerGame {
    private final MainCtrl main;
    private final ServerUtils server;
    private ScheduledFuture<?> pingThread;
    private ServerUtils.LongPollingRequest longPollingRequest;

    public MultiplayerGame(MainCtrl main, ServerUtils server) {
        this.main = main;
        this.server = server;
    }

    public void startPingThread(String username) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        pingThread = executor.scheduleAtFixedRate(() -> {
            server.postRequest("api/game/ping",
                    new PingRequestPacket(username),
                    GeneralResponsePacket.class);
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void stopPingThread() {
        pingThread.cancel(false);
    }

    /**
     * repeatedly pings the server
     *
     * @param username
     */
    public void repeatPing(String username) {
        new Thread(() -> {
            while (true) {
                try {
                    server.postRequest("api/game/ping",
                            new PingRequestPacket(username),
                            GeneralResponsePacket.class);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    public GeneralResponsePacket sendEmote(String username, String emoteStr) {
        return server.postRequest("api/game/emote",
                new EmoteRequestPacket(username, emoteStr),
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
        this.longPollingRequest = request;
        request.getRequest();
    }

    public void stopLobbyUpdate() {
        this.longPollingRequest.stop();
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
            } else if (responsePacket.getType().equals("Leave")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
            }


        }

        private void updateEmote(String from, String emote) {
            Platform.runLater(() ->
                    main.getCtrl(LobbyCtrl.class)
                            .updateEmoji(from, emote));

        }
    }


}
