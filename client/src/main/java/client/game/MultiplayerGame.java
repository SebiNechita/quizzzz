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

    /**
     * starts the persistent ping thread, which pings the server every 0.5 second
     *
     * @param username this client's username
     */
    public void startPingThread(String username) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        pingThread = executor.scheduleAtFixedRate(() -> {
            server.postRequest("api/game/ping",
                    new PingRequestPacket(username),
                    GeneralResponsePacket.class);
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * stops the ping thread. should be called when 'back' is clicked.
     */
    public void stopPingThread() {
        pingThread.cancel(false);
    }


    /**
     * Join a player to a lobby
     *
     * @param username this client's username
     */
    public void join(String username) {

        JoinResponsePacket responsePacket = server.postRequest("api/game/join",
                new JoinRequestPacket(username),
                JoinResponsePacket.class);
        Platform.runLater(() ->
                main.getCtrl(LobbyCtrl.class)
                        .updatePlayerList(responsePacket.getPlayerList()));
    }

    /**
     * send clicked Emote to the server
     *
     * @param username this client's username
     * @param emoteStr emote name
     * @return GeneralResponsePacket
     */
    public GeneralResponsePacket sendEmote(String username, String emoteStr) {
        return server.postRequest("api/game/emote",
                new EmoteRequestPacket(username, emoteStr),
                GeneralResponsePacket.class);
    }

    /**
     * updates emote in this client according to updates sent by the server
     *
     * @param from  sender of the emote
     * @param emote emote name
     */
    private void updateEmote(String from, String emote) {
        Platform.runLater(() ->
                main.getCtrl(LobbyCtrl.class)
                        .updateEmoji(from, emote));

    }

    /**
     * send ready message to the server
     *
     * @param username this client's username
     * @param isReady  is ready or not
     */
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

    /**
     * send persistent long polling request to the server. And it should get updates from the server about other players.
     */
    public void getLobbyUpdate() {
        ServerUtils.LongPollingRequest<LobbyResponsePacket> request
                = server.longGetRequest("api/game/lobbyEventListener",
                LobbyResponsePacket.class, new LobbyOnResponse());
        // set persistent long-polling to true
        request.setPersistent(true);
        this.longPollingRequest = request;
        request.getRequest();
    }

    /**
     * stops the long polling request
     */
    public void stopLobbyUpdate() {
        this.longPollingRequest.stop();
    }

    /**
     * callback method when the client gets update from the server via the long polling request.
     */
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
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateReady(responsePacket.getFrom()
                                        , responsePacket.getContent()));
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

    }

}
