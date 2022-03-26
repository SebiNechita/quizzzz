package server.api.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.utils.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.request.async.DeferredResult;
import packets.*;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GameService gameService;

    /**
     * test case for getting pinged by client
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "someone")
    public void onPingTest()
            throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(post("/api/game/ping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PingRequestPacket("Joe")))
                        .secure(true)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.GeneralResponsePacket.code")
                        .value(HttpStatus.OK.getCode()));

        verify(gameService, times(1))
                .updatePlayerTime("Joe");
    }

    // not sure how to test this endpoint, might try it later
    @WithMockUser(username = "someone")
    public void playersInLobbyTest()
            throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(get("/api/game/lobbyEventListener")
                        .secure(true)
                )
                .andExpect(status().isOk());


        verify(gameService, times(1))
                .waitForPlayerEvent(new GameController.EventCaller(new DeferredResult<>(), "someone"));

    }

    /**
     * test case for someone joining the lobby. should return the expected playerMap
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "someone")
    public void joinTest()
            throws Exception {

        Map<String, String> playerMap = new HashMap<>();
        playerMap.put("Joe", "false");

        when(gameService.onPlayerJoin("Joe"))
                .thenReturn(playerMap);

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(post("/api/game/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new JoinRequestPacket("Joe")))
                        .secure(true)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.JoinResponsePacket.code")
                        .value(HttpStatus.OK.getCode()))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.JoinResponsePacket.playerList")
                        .value(Matchers.hasKey("Joe")))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.JoinResponsePacket.playerList")
                        .value(Matchers.hasValue("false")));

        verify(gameService, times(1))
                .onPlayerJoin("Joe");

    }

    /**
     * test case for receiving emote, should invoke correct service method with correct parameters
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "someone")
    public void sendEmoteTest()
            throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(post("/api/game/emote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EmoteRequestPacket("Joe", "angry")))
                        .secure(true)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.GeneralResponsePacket.code")
                        .value(HttpStatus.OK.getCode()));

        verify(gameService, times(1))
                .onEmoteReceived("Emote", "angry", "Joe");
    }

    /**
     * tests for when a player sent a ready request, should invoke onPlayerReady() with correct parameters
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "someone")
    public void onReadyMsgTest()
            throws Exception {

        Map<String, String> playerMap = new HashMap<>();
        playerMap.put("Joe", "true");

        when(gameService.onPlayerReady("Ready", "true", "Joe"))
                .thenReturn(new LobbyResponsePacket("Ready", "true", "Joe", playerMap));

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(post("/api/game/ready")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReadyRequestPacket("Joe","true")))
                        .secure(true)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.LobbyResponsePacket.type")
                        .value("Ready"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.LobbyResponsePacket.content")
                        .value("true"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.LobbyResponsePacket.from")
                        .value("Joe"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.LobbyResponsePacket.playerList")
                        .value(Matchers.hasKey("Joe")))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.LobbyResponsePacket.playerList")
                        .value(Matchers.hasValue("true")));

        verify(gameService, times(1))
                .onPlayerReady("Ready", "true", "Joe");

    }


}
