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


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameServiceTest {

    GameService gameService;

    /**
     * initializes gameService before each test case
     */
    @BeforeEach
    void initService() {
        gameService = new GameService();
    }

    /**
     * test case for when player leaves, should call the event handler of the long polling request
     */
    @Test
    public void onPlayerLeaveTest() {
        DeferredResult<LobbyResponsePacket> output1 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller1 = new GameController.EventCaller(output1, "Joe");
        DeferredResult<LobbyResponsePacket> output2 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller2 = new GameController.EventCaller(output2, "Kate");

        var spy = Mockito.spy(gameService);
        spy.waitForPlayerEvent(eventCaller1);
        spy.waitForPlayerEvent(eventCaller2);

        // calls the method, Ted should have already been removed by scheduled task
        spy.onPlayerLeave("Ted");

        assertTrue(gameService.getPlayerEventList().size() == 0);
        verify(spy, times(1)).trimPlayerList();
    }

    /**
     * test case for adding player to the playerList
     */
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

        verify(spy, times(1)).clearEventList("Joe");
    }

    /**
     * test case for a player joins the lobby. should return correct player list.
     */
    @Test
    public void onPlayerJoinTest() {
        DeferredResult<LobbyResponsePacket> output1 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller1 = new GameController.EventCaller(output1, "Ted");
        DeferredResult<LobbyResponsePacket> output2 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller2 = new GameController.EventCaller(output2, "Kate");

        var spy = Mockito.spy(gameService);
        spy.waitForPlayerEvent(eventCaller1);
        spy.waitForPlayerEvent(eventCaller2);
        spy.addPlayer("Ted");
        spy.addPlayer("Kate");
        spy.addPlayer("Joe");

        // calls the method
        var trimmedList = spy.onPlayerJoin("Join");
        Map<String, String> expectedList = new HashMap<>();

        // two existing eventCaller should be invoked and cleared
        assertTrue(gameService.getPlayerEventList().size() == 0);

        //assertTrue(Joe != null);
        assertTrue(trimmedList.get("Joe") != null);
        assertTrue(trimmedList.get("Ted") != null);
        assertTrue(trimmedList.get("Kate") != null);

        verify(spy, times(1)).trimPlayerList();

    }

    /**
     * case for when one of two player clicks ready. Should return correct responsepacket with correct player list
     */
    @Test
    public void onPlayerReadyTest() {
        DeferredResult<LobbyResponsePacket> output1 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller1 = new GameController.EventCaller(output1, "Ted");
        DeferredResult<LobbyResponsePacket> output2 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller2 = new GameController.EventCaller(output2, "Kate");

        var spy = Mockito.spy(gameService);
        spy.waitForPlayerEvent(eventCaller1);
        spy.waitForPlayerEvent(eventCaller2);
        spy.addPlayer("Ted");
        spy.addPlayer("Kate");

        // calls the method
        LobbyResponsePacket responsePacket = spy.onPlayerReady("Ready", "true", "Ted");

        // one of two existing eventCaller should be invoked and cleared
        assertTrue(gameService.getPlayerEventList().size() == 1);

        //assertTrue(Joe != null);
        assertEquals("Ready", responsePacket.getType());
        assertEquals("true", responsePacket.getPlayerList().get("Ted"));
        assertEquals("false", responsePacket.getPlayerList().get("Kate"));

    }

}
