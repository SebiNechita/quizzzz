package server.api.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.async.DeferredResult;
import packets.LobbyResponsePacket;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameServiceTest {

    GameService gameService;

    @BeforeEach
    void initService() {
        gameService = new GameService();
    }

    public void onPlayerLeaveTest() {
        gameService.onPlayerLeave("Joe");
        var spy = Mockito.spy(gameService);
        spy.onPlayerLeave("Joe");
        gameService.addPlayer("Joe");
        gameService.addPlayer("Kate");
        gameService.onPlayerLeave("Joe");
        assertTrue(gameService.getPlayers().size() == 1);

        for (String player : gameService.getPlayers().keySet()) {
            assertEquals("Joe", player);
        }


    }

    @Test
    public void addPlayerTest() {
        gameService.addPlayer("Joe");

        assertTrue(gameService.getPlayers().size() == 1);
        for (String player : gameService.getPlayers().keySet()) {
            assertEquals("Joe", player);
            assertEquals("false", gameService.getPlayers().get(player).getKey());
        }
    }

    /**
     * test if eventCallers other than from the emote sender got invoked
     */
    @Test
    public void onEmoteReceivedTest() {
        DeferredResult<LobbyResponsePacket> output1 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller1 = new GameController.EventCaller(output1, "Joe");
        DeferredResult<LobbyResponsePacket> output2 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller2 = new GameController.EventCaller(output2, "Kate");

        var spy = Mockito.spy(gameService);
        spy.waitForPlayerEvent(eventCaller1);
        spy.waitForPlayerEvent(eventCaller2);

        // calls the method
        spy.onEmoteReceived("Emote", "angry", "Joe");

        assertTrue(gameService.getPlayerEventList().size() == 1);
        assertEquals(eventCaller1, gameService.getPlayerEventList().get(0));

        verify(spy,times(1)).clearEventList("Joe");
    }




}
