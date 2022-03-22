package server.api.game;

import commons.utils.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import packets.GeneralResponsePacket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final ExecutorService games = Executors.newFixedThreadPool(4);

    @GetMapping("/lobbyData")
    public DeferredResult<GeneralResponsePacket> joinGame() {
        DeferredResult<GeneralResponsePacket> output = new DeferredResult<>();
        games.execute(() -> {
            try {
                Thread.sleep(60000);
                output.setResult(new GeneralResponsePacket(HttpStatus.ImATeapot));
            } catch (InterruptedException ignored) {}
        });

        return output;
    }
}
