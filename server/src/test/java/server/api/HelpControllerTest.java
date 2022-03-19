package server.api;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelpControllerTest {
    @Autowired
    private MockMvc mvc;

    /**
     * test case for sendHelp() method
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "Geoff")
    public void sendHelpTest() throws Exception {

        MvcResult res = mvc.perform(get("/api/help")
                .secure( true )
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String resStr = res.getResponse().getContentAsString();
        String expected = "How the game is played?<br>" +
                "-Singleplayer<br>" +
                " There are 20 questions and for each the player has 10 seconds to respond.<br>" +
                " At the end, the player will see the result and will also see a leaderboard in which he can see where he stands among other players.<br>" +
                "-Multiplayer<br>" +
                " The player goes to the waiting room and then a session will start. The number of players in a session is not limited.<br>" +
                " During the game, players can choose between three “JOKERS” (only once per session). These “JOKER” can improve the performance of the player.<br>" +
                " At the end of the game, he can see a leaderboard that shows the rank and points everyone has acquired.<br>";
        assertTrue(resStr.equals(expected));

    }
}
