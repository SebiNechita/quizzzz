package server.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class GreetingController {
    @GetMapping(path = "/greet/{name}")
    public String sendGreeting(@PathVariable("name") String name) {
        return "Hi, " + name + "!";
    }
}
