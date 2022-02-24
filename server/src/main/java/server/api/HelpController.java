package server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class HelpController {

    @GetMapping(path = "/help")
    public String sendHelp() {

        return "How the game is played?<br>" +
                "-Singleplayer<br>" +
                " There are 20 questions and for each the player has 10 seconds to respond.<br>" +
                " At the end, the player will see the result and will also see a leaderboard in which he can see where he stands among other players. <br>" +
                "-Multiplayer<br>" +
                " The player goes to the waiting room and then a session will start. The number of players in a session is not limited.<br>" +
                " During the game, players can choose between three “JOKERS” (only once per session). These “JOKER” can improve the performance of the player.<br>" +
                " At the end of the game, he can see a leaderboard that shows the rank and points everyone has acquired.<br>";
    }
}
