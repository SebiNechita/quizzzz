package server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class ServerController {

    @GetMapping(path = "/server")
    public String sendGreeting() {
        return "I am a Quizzzz server!";
    }
}
