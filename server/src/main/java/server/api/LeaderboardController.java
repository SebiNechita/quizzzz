package server.api;

import commons.LeaderboardEntry;
import commons.utils.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packets.LeaderboardResponsePacket;
import server.database.LeaderboardRepository;

@RestController
@RequestMapping(path = "/api")
public class LeaderboardController {

    private final LeaderboardRepository repo;

    public LeaderboardController(LeaderboardRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "/leaderboard" })
    public LeaderboardResponsePacket getAll() {
        return new LeaderboardResponsePacket(HttpStatus.OK, repo.findAll());
    }

    @GetMapping(path = { "/leaderboard/{username}" })
    public LeaderboardEntry getScoreForUser(@PathVariable("username") String username) {
        return repo.findByUsername(username);
    }

    @PostMapping(path = { "/leaderboard" })
    public ResponseEntity<LeaderboardEntry> add(@RequestBody LeaderboardEntry leaderboardEntry) {

        if (leaderboardEntry.points < 0
                || isNullOrEmpty(leaderboardEntry.username)) {
            return ResponseEntity.badRequest().build();
        }

        LeaderboardEntry saved = repo.save(leaderboardEntry);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
