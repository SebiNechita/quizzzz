package server.api.game;

import commons.Game;
import commons.questions.Activity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class CreateGameService {

    /**
     * Creates a game using noOfQuestions.
     * If it is not provided, it uses the default value of 20
     * @param noOfQuestions number of questions in the game
     * @return Game with a list of questions
     */
    public Game createGame(Integer noOfQuestions) {
        List<Activity> activities = null;
        File source = new File(
                Objects.requireNonNull(
                                getClass().getClassLoader().getResource("activity-bank/activities.json")
                        ).
                        getFile()
        );

        try {
            activities = Activity.readActivities(source);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (noOfQuestions != null) return new Game(noOfQuestions, activities);
        else return new Game(activities);
    }
}
