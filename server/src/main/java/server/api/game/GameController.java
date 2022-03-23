package server.api.game;

import commons.utils.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import packets.GeneralResponsePacket;
import packets.JoinRequestPacket;
import packets.JoinResponsePacket;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/lobbyEventListener")
    public DeferredResult<GeneralResponsePacket> playersInLobby() {
        DeferredResult<GeneralResponsePacket> output = new DeferredResult<>();
        Thread thread = new Thread(() -> {
            output.setResult(new GeneralResponsePacket(HttpStatus.OK));
        });
        gameService.waitForPlayerEvent(thread);
        return output;
    }

    @PostMapping("/join")
    public JoinResponsePacket join(@RequestBody JoinRequestPacket request) {
        gameService.addPlayer(request.getUsername());
        return new JoinResponsePacket(HttpStatus.OK);
    }
}
