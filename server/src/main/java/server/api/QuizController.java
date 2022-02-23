package server.api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class QuizController {
    /**
     * Responds with the name defined in the path.
     */
    @GetMapping(path = { "/greet/{name}"})
    public String getGreeting(@PathVariable("name") String name) {
        return "Hello, " + name + "!";
    }
}
