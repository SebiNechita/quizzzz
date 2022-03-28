package server.api.game;

import commons.Game;
import packets.GameResponsePacket;
import commons.questions.Activity;
import org.springframework.stereotype.Service;
import server.database.ActivityRepository;

import java.util.List;

@Service
public class CreateGameService {
    private final ActivityRepository repository;

    public CreateGameService(ActivityRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a game using noOfQuestions.
     * If it is not provided, it uses the default value of 20
     * @param noOfQuestions number of questions in the game
     * @return Game with a list of questions
     */
    public GameResponsePacket createGame(Integer noOfQuestions) {
        List<Activity> activities = null;

        try {
            activities = repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (noOfQuestions != null) {
            return new GameResponsePacket(new Game(noOfQuestions, activities));
        } else {
            return new GameResponsePacket(new Game(activities));
        }
    }
}
