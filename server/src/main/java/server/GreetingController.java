package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class GreetingController {

    @GetMapping(path = "/greet/{name}")
    public String sendGreeting(@PathVariable("name") String name) {
        return "Hi, " + name +  "!";
    }
}
