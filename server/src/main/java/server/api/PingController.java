package server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class SomeController {

    /**
     * Temporary endpoint. Will probably be removed later.
     *
     * @return Body to send to the client
     */
    @GetMapping
    public String index() {
        return "Pong";
    }
}