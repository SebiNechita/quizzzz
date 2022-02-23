package server.api;
import commons.Quote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizController {
    @GetMapping(path = { "/greet/{name}"})
    public String getGreeting(@PathVariable("name") String name) {
        return "Hello, " + name + "!";
    }

}
