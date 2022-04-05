package server;

import commons.Game;
import commons.LeaderboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    Match match;

    @BeforeEach
    void beforeEach(){
        match = new Match(new Game());
    }

    @Test
    void addScoreTest() {
        match.getScores().add(new LeaderboardEntry(0, "A"));
        match.addScore("A",50);
        LeaderboardEntry player = new LeaderboardEntry();
        for (LeaderboardEntry l : match.getScores()){
            if (l.username.equals("A")){
                player = l;
            }
        }
        assertEquals(50,player.points);
    }
}
