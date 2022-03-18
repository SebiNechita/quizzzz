package server.api.game;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    @MessageMapping("/quotes")
    @SendTo("/topic/quotes")
    public ResponseEntity getMessage(ResponseEntity message) {
        System.out.println(message.getStatusCode());
        return message;
    }
}
