package server.api.game;

import commons.Game;
import commons.LeaderboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.async.DeferredResult;
import packets.GameResponsePacket;
import packets.LobbyResponsePacket;
import packets.StartGameRequestPacket;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameServiceTest {

    GameService gameService;

    @MockBean
    CreateGameService createGameService;

    /**
     * initializes gameService before each test case
     */
    @BeforeEach
    void initService() {
        //created the mock bean and the when... statement because
        //the tests are using addPlayer which needs a game to be created
        gameService = new GameService(createGameService);
        when(createGameService.createGame(20))
                .thenReturn(new GameResponsePacket(new Game()));
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
        var trimmedList = spy.onPlayerJoin("Joe");
        Map<String, String> expectedList = new HashMap<>();

        // two existing eventCaller should be invoked and cleared
        assertTrue(gameService.getPlayerEventList().size() == 0);

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

    @Test
    void addScoreTest() {
        gameService.addPlayer("A");
        gameService.addScore("A",50);
        LeaderboardEntry player = new LeaderboardEntry();
        for (LeaderboardEntry l : gameService.getScores()){
            if (l.username.equals("A")){
                player = l;
            }
        }
        assertEquals(50,player.points);
    }

    @Test
    void onStartGameNewPlayerMapTest() {
        GameService spy = Mockito.spy(gameService);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Joe", "false");

        spy.addPlayer("Ted");
        spy.addPlayer("Kate");
        spy.onStartGame(new StartGameRequestPacket(true,"Ted"));
        spy.addPlayer("Joe");
        assertEquals(expectedMap,spy.trimPlayerList());
    }
}
