package server.api.game;

import commons.utils.HttpStatus;
import commons.utils.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import packets.GeneralResponsePacket;
import packets.JoinRequestPacket;
import packets.JoinResponsePacket;
import packets.RegisterRequestPacket;
import server.user.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final ExecutorService games = Executors.newFixedThreadPool(4);

    @Autowired
    private List<String> playerList;

    @GetMapping("/lobbyData")
    public DeferredResult<GeneralResponsePacket> joinGame() {
        DeferredResult<GeneralResponsePacket> output = new DeferredResult<>();
        games.execute(() -> {
            try {
                Thread.sleep(60000);
                output.setResult(new GeneralResponsePacket(HttpStatus.ImATeapot));
            } catch (InterruptedException ignored) {
            }
        });

        return output;
    }

    @PostMapping("/join")
    public JoinResponsePacket join(@RequestBody JoinRequestPacket request) {
        playerList.add(request.getUsername());
        return new JoinResponsePacket(HttpStatus.OK);
    }
}
