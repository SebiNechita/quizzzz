package server.api;

import commons.LeaderboardEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.LeaderboardRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class GreetingController {

    private final LeaderboardRepository repo;

    public GreetingController(LeaderboardRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "/leaderboard" })
    public List<LeaderboardEntry> getAll() {
        return repo.findAll();
    }

    @PostMapping(path = { "/leaderboard" })
    public ResponseEntity<LeaderboardEntry> add(@RequestBody LeaderboardEntry leaderboardEntry) {

        if (leaderboardEntry.points < 0
                || isNullOrEmpty(leaderboardEntry.person)) {
            return ResponseEntity.badRequest().build();
        }

        LeaderboardEntry saved = repo.save(leaderboardEntry);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping(path = "/greet/{name}")
    public String sendGreeting(@PathVariable("name") String name) {
        return "Hi, " + name + "!";
    }
}
