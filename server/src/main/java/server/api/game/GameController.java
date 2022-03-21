package server.api.game;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game/response")
public class GameController {
    @MessageMapping("/response")
    @SendTo("/topic/response")
    public ResponseEntity getMessage(ResponseEntity message) {
        System.out.println(message.getStatusCode());
        return message;
    }
}
