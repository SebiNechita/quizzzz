package server.api.game;

import commons.utils.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import packets.*;

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
        gameService.onPlayerEvent("Join", request.getUsername(), request.getUsername());
        return new JoinResponsePacket(HttpStatus.OK);
    }

    /**
     * client sends emote to server
     *
     * @param request request for sending emote
     * @return LobbyResponsePacket
     */
    @PostMapping("/emote")
    public GeneralResponsePacket sendEmote(@RequestBody EmoteRequestPacket request) {
        gameService.onPlayerEvent("Emote", request.getEmoteNo(), request.getUsername());
        return new GeneralResponsePacket(HttpStatus.OK);
    }

    @PostMapping("/ready")
    public GeneralResponsePacket onReadyMsg(@RequestBody ReadyRequestPacket request) {
        gameService.onPlayerReady("Ready", request.getIsReady(), request.getUsername());
        return new GeneralResponsePacket(HttpStatus.OK);
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
