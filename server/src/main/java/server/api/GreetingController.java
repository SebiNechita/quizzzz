package server.api;

import commons.LeaderboardEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.LeaderboardRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class GreetingController {





    @GetMapping(path = "/greet/{name}")
    public String sendGreeting(@PathVariable("name") String name) {
        return "Hi, " + name + "!";
    }
}
