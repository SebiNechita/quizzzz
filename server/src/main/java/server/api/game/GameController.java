package server.api.game;

import commons.utils.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * gets pinged by client per 1 second
     *
     * @param request
     * @return GeneralResponsePacket
     */
    @PostMapping("/ping")
    public GeneralResponsePacket onPing(@RequestBody PingRequestPacket request) {
        request.getUsername();
        gameService.updatePlayerTime(request.getUsername());
        return new GeneralResponsePacket(HttpStatus.OK);
    }

    /**
     * long polling endpoint for updating any lobby information
     * @return DeferredResult<LobbyResponsePacket>
     */
    @GetMapping("/lobbyEventListener")
    public DeferredResult<LobbyResponsePacket> playersInLobby() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        DeferredResult<LobbyResponsePacket> output = new DeferredResult<>();
        EventCaller<LobbyResponsePacket> eventCaller = new EventCaller(output, username);
        gameService.waitForPlayerEvent(eventCaller);
        return output;
    }

    /**
     * handles join request from player when he/she enters the lobby
     * @param request
     * @return JoinResponsePacket
     */
    @PostMapping("/join")
    public JoinResponsePacket join(@RequestBody JoinRequestPacket request) {
        gameService.addPlayer(request.getUsername());
        Map<String, String> playerMap = gameService.onPlayerJoin(request.getUsername());
        return new JoinResponsePacket(HttpStatus.OK, playerMap);
    }

    /**
     * client sends emote to server, server then send it to other players
     * @param request request for sending emote
     * @return LobbyResponsePacket
     */
    @PostMapping("/emote")
    public GeneralResponsePacket sendEmote(@RequestBody EmoteRequestPacket request) {
        gameService.onEmoteReceived("Emote",
                request.getEmoteStr(),
                request.getUsername());
        return new GeneralResponsePacket(HttpStatus.OK);
    }

    /**
     * receives ready information from player and send it to other players
     * @param request
     * @return LobbyResponsePacket
     */
    @PostMapping("/ready")
    public LobbyResponsePacket onReadyMsg(@RequestBody ReadyRequestPacket request) {
        return gameService.onPlayerReady("Ready", request.getIsReady(), request.getUsername());
    }

    /**
     * Event handler for giving long polling reponse to client
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
}
