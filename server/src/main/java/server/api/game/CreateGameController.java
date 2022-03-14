package server.api.game;

import commons.Game;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/game/create")
public class CreateGameController {

    /**
     * Service layer for the CreateGameController
     */
    private final CreateGameService gameService;

    /**
     * Constructor
     * @param gameService Service layer for this controller
     */
    public CreateGameController(CreateGameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpoint for creating game
     * @param noOfQuestions number of questions in the game
     * @return Game with a list of questions
     */
    @GetMapping(path = "")
    public Game createGame(@RequestParam(value = "noOfQuestions", required = false) Integer noOfQuestions) {
        return gameService.createGame(noOfQuestions);
    }
}
