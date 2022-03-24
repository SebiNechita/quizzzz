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

    @GetMapping("/lobbyEventListener")
    public DeferredResult<LobbyResponsePacket> playersInLobby() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        DeferredResult<LobbyResponsePacket> output = new DeferredResult<>();
        EventCaller<LobbyResponsePacket> eventCaller = new EventCaller(output, username);
        gameService.waitForPlayerEvent(eventCaller);
        return output;
    }

    @PostMapping("/join")
    public JoinResponsePacket join(@RequestBody JoinRequestPacket request) {
        gameService.addPlayer(request.getUsername());
        Map<String, String> playerMap = gameService.onPlayerJoin(request.getUsername());
        return new JoinResponsePacket(HttpStatus.OK, playerMap);
    }

    /**
     * client sends emote to server
     *
     * @param request request for sending emote
     * @return LobbyResponsePacket
     */
    @PostMapping("/emote")
    public GeneralResponsePacket sendEmote(@RequestBody EmoteRequestPacket request) {
        gameService.onEmoteReceived("Emote", request.getEmoteNo(), request.getUsername());
        return new GeneralResponsePacket(HttpStatus.OK);
    }

    @PostMapping("/ready")
    public LobbyResponsePacket onReadyMsg(@RequestBody ReadyRequestPacket request) {
        return gameService.onPlayerReady("Ready", request.getIsReady(), request.getUsername());
    }

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
