package server.api.game;

import commons.Game;
import commons.questions.Activity;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "api/game/create")
public class CreateGameController {

    @GetMapping(path = "")
    public Game createGame(@RequestParam(value = "noOfQuestions", required = false) Integer noOfQuestions) {
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
