package server.api.game;

import commons.Game;
import commons.utils.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import packets.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * gets pinged by client per 0.5 second
     *
     * @param request
     * @return GeneralResponsePacket
     */
    @PostMapping("/ping")
    public GeneralResponsePacket onPing(@RequestBody PingRequestPacket request) {
        gameService.updatePlayerTime(request.getUsername());
        return new GeneralResponsePacket(HttpStatus.OK);
    }

    /**
     * long polling endpoint for updating any lobby information
     *
     * @return DeferredResult<LobbyResponsePacket>
     */
    @GetMapping("/lobbyEventListener")
    public DeferredResult<LobbyResponsePacket> playersInLobby() {
        DeferredResult<LobbyResponsePacket> output = new DeferredResult<>();
        EventCaller<LobbyResponsePacket> eventCaller = new EventCaller(output, gameService.getPlayerInSession());
        gameService.waitForPlayerEvent(eventCaller);
        return output;
    }

    /**
     * handles join request from player when he/she enters the lobby
     *
     * @param request
     * @return JoinResponsePacket
     */
    @PostMapping("/join")
    public JoinResponsePacket join(@RequestBody JoinRequestPacket request) {
        Game game = gameService.addPlayer(request.getUsername());
        Map<String, String> playerMap = gameService.onPlayerJoin(request.getUsername());
        return new JoinResponsePacket(HttpStatus.OK, playerMap, game);
    }

    /**
     * Removes the player from the game they're in
     * @return OK status
     */
    @GetMapping("/leave")
    public GeneralResponsePacket leave() {
        gameService.removePlayer(gameService.getPlayerInSession());
        return new GeneralResponsePacket(HttpStatus.OK);
    }

    /**
     * Sends the multiplayer leaderboard
     * @return the packet containing the leaderboard.
     */
    @GetMapping("/multiplayerleaderboard")
    public LeaderboardResponsePacket getMultiplayerLeaderboard (){
        return new LeaderboardResponsePacket(HttpStatus.OK,gameService.getScoresByUser(gameService.getPlayerInSession()));
    }

    /**
     * Update the score of a user
     * @param request contains the user and the points to add
     * @return a response with 200
     */
    @PostMapping("/score")
    public GeneralResponsePacket updateScore(@RequestBody LeaderboardRequestPacket request) {
        gameService.addScore(request.getPlayer(), request.getScore());
        return new GeneralResponsePacket(HttpStatus.OK);
    }

    /**
     * client sends emote to server, server then send it to other players
     *
     * @param request request for sending emote
     * @return LobbyResponsePacket
     */
    @PostMapping("/emote")
    public GeneralResponsePacket sendEmote(@RequestBody EmoteRequestPacket request) {
        gameService.onEmoteReceived("Emote",
                request.getEmoteStr(),
                request.getUsername(),
                request.getFromScene());
        return new GeneralResponsePacket(HttpStatus.OK);
    }

    /**
     * client sends joker notification to server, server then send it to other players
     *
     * @param request request for sending joker notification
     * @return LobbyResponsePacket
     */
    @PostMapping("/jokerNotification")
    public GeneralResponsePacket sendJokerNotification(@RequestBody JokerNotificationRequestPacket request) {
        gameService.onJokerNotificationReceived("JokerNotification",
                request.getJokerType().toString(),
                request.getUsername());
        return new GeneralResponsePacket(HttpStatus.OK);
    }

    /**
     * receives ready information from player and send it to other players
     *
     * @param request
     * @return LobbyResponsePacket
     */
    @PostMapping("/ready")
    public LobbyResponsePacket onReadyMsg(@RequestBody ReadyRequestPacket request) {
        return gameService.onPlayerReady("Ready", request.getIsReady(), request.getUsername());
    }

    /**
     * Event handler for giving long polling response to client
     *
     * @param <T>
     */
    public static class EventCaller<T extends ResponsePacket> {
        private final DeferredResult<T> result;
        private final String username;

        public EventCaller(DeferredResult<T> result, String username) {
            this.result = result;
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void run(T packet) {
            result.setResult(packet);
        }
    }

    /**
     * receives start from a player and sends it to other players
     * @param requestPacket The request packet
     * @return LobbyResponsePacket
     */
    @PostMapping("/start")
    public LobbyResponsePacket onStart(@RequestBody StartGameRequestPacket requestPacket) {
        return gameService.onStartGame(requestPacket);
    }

    /**
     * receives a joker from a player and sends it to other players
     * @param requestPacket The request packet
     * @return LobbyResponsePacket
     */
    @PostMapping("/joker")
    public JokerResponsePacket onJoker(@RequestBody JokerRequestPacket requestPacket) {
        return gameService.onJokerGame(requestPacket);
    }
}
