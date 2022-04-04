package server.api;

import commons.LeaderboardEntry;
import commons.utils.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import packets.GeneralResponsePacket;
import packets.LeaderboardResponsePacket;
import server.database.LeaderboardRepository;

@RestController
@RequestMapping(path = "/api")
public class LeaderboardController {

    private final LeaderboardRepository repo;

    public LeaderboardController(LeaderboardRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = {"/leaderboard"})
    public LeaderboardResponsePacket getAll() {
        return new LeaderboardResponsePacket(HttpStatus.OK, repo.findAll());
    }

    @GetMapping(path = {"/leaderboard/{username}"})
    public LeaderboardEntry getScoreForUser(@PathVariable("username") String username) {
        return repo.findByUsername(username);
    }

    /**
     * Gets the number of people who have greater scores than the given user
     *
     * @param username the user
     * @return the rank of the user if the user is on the leaderboard, 404 otherwise
     */
    @GetMapping(path = {"/leaderboard/{username}/rank"})
    public GeneralResponsePacket getRankForUser(@PathVariable("username") String username) {
        LeaderboardEntry leaderboardEntry = repo.findByUsername(username);
        if (leaderboardEntry == null) {
            return new GeneralResponsePacket(HttpStatus.NotFound);
        }
        int userScore = leaderboardEntry.points;
        int numberOfBetterScores = (int) repo.findAll().stream()
                .filter(e -> e.points > userScore)
                .map(e -> e.points)
                .distinct()
                .count();
        return new GeneralResponsePacket(HttpStatus.OK, Integer.toString(numberOfBetterScores + 1));
    }

    @PostMapping(path = {"/leaderboard"})
    public LeaderboardResponsePacket add(@RequestBody LeaderboardEntry leaderboardEntry) {
        if (leaderboardEntry.points < 0 || isNullOrEmpty(leaderboardEntry.username)) {
            return new LeaderboardResponsePacket(HttpStatus.BadRequest);
        }

        String tokenUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!leaderboardEntry.username.equals(tokenUsername)) {
            return new LeaderboardResponsePacket(HttpStatus.Forbidden);
        }

        if (repo.findByUsername(leaderboardEntry.username) == null) {
            repo.save(leaderboardEntry);
        } else {
            updateLeaderboard(leaderboardEntry);
        }

        return new LeaderboardResponsePacket(HttpStatus.OK, repo.findAll());
    }

    /**
     * Updates the leaderboard database with the specified points
     * (if it's a higher score than the score currently stored in the database)
     *
     * @param leaderboardEntry contains the username and the proposed number of points
     */
    private void updateLeaderboard(LeaderboardEntry leaderboardEntry) {
        String user = leaderboardEntry.username;
        int newScore = leaderboardEntry.points;
        int oldScore = repo.findByUsername(leaderboardEntry.username).points;

        if (newScore > oldScore) {
            repo.updateScoreForUser(user, newScore);
        }
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
