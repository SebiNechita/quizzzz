package server.api.game;

import commons.Game;
import packets.GameResponsePacket;
import commons.questions.Activity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
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
    public GameResponsePacket createGame(Integer noOfQuestions) {
        List<Activity> activities = null;

        try {
            File source = new File(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("activity-bank/activities.json")).toURI()
            );
            activities = Activity.readActivities(source);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        if (noOfQuestions != null) {
            return new GameResponsePacket(new Game(noOfQuestions, activities));
        } else {
            return new GameResponsePacket(new Game(activities));
        }
    }
}
