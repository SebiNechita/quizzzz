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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.async.DeferredResult;
import packets.GameResponsePacket;
import packets.LobbyResponsePacket;
import packets.StartGameRequestPacket;
import server.Match;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameServiceTest {

    GameService gameService;

    //in some tests, there was a check for whether trimPlayerMap was called, but since
    //that method has been moved to Match, I need to create a mock match, too. -Kristof
    Match match;

    @MockBean
    CreateGameService createGameService;

    /**
     * initializes gameService before each test case
     */
    @BeforeEach
    void initService() {
        match = new Match(new Game());
        //created the mock bean and the when... statement because
        //the tests are using addPlayer which needs a game to be created -Kristof
        gameService = new GameService(createGameService);
        when(createGameService.createGame(20))
                .thenReturn(new GameResponsePacket(new Game()));
    }

    /**
     * The event callers should be removed when they are used.
     */
    @Test
    public void onPlayerLeaveTestClearEventCallers() {
        DeferredResult<LobbyResponsePacket> output1 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller1 = new GameController.EventCaller(output1, "Joe");
        DeferredResult<LobbyResponsePacket> output2 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller2 = new GameController.EventCaller(output2, "Kate");

        var spy = Mockito.spy(gameService);
        var spyMatch = Mockito.spy(match);
        //this avoids automatically creating a new match when adding the first player
        spy.setCurrentMatch(spyMatch);

        //add players to the game, but Ted doesn't have an eventcaller
        spy.addPlayer("Ted");
        spy.addPlayer("Joe");
        spy.addPlayer("Kate");
        spy.waitForPlayerEvent(eventCaller1);
        spy.waitForPlayerEvent(eventCaller2);

        // notifies Joe and Kate, Ted should have already been removed by scheduled task
        spy.onPlayerLeave("Ted");

        assertTrue(spy.getCurrentMatch().getPlayerEventList().size() == 0);
        verify(spyMatch, times(1)).trimPlayerList();
    }

    /**
     * test case for adding player to the playermap
     */
    @Test
    public void addPlayerTestPlayerMap() {
        gameService.addPlayer("Joe");

        assertTrue(gameService.getCurrentMatch().getPlayerMap().size() == 1);
        for (String player : gameService.getCurrentMatch().getPlayerMap().keySet()) {
            assertEquals("Joe", player);
            assertEquals("false", gameService.getCurrentMatch().getPlayerMap().get(player).getKey());
        }
    }

    /**
     * test case for adding player to the scores list
     */
    @Test
    public void addPlayerTestScores() {
        var spy = Mockito.spy(gameService);
        spy.addPlayer("Joe");

        List<LeaderboardEntry> expected = List.of(new LeaderboardEntry(0,"Joe"));

        assertEquals(expected,spy.getCurrentMatch().getScores());
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
        spy.addPlayer("Joe");
        spy.addPlayer("Kate");
        spy.waitForPlayerEvent(eventCaller1);
        spy.waitForPlayerEvent(eventCaller2);

        // calls the method
        spy.onEmoteReceived("Emote", "angry", "Joe", "Game");

        assertTrue(spy.getCurrentMatch().getPlayerEventList().size() == 1);
        assertEquals(eventCaller1, spy.getCurrentMatch().getPlayerEventList().get(0));

        verify(spy, times(1)).clearEventList("Joe");
    }

    /**
     * test if eventCallers other than from the joker notification sender got invoked
     */
    @Test
    public void onJokerNotificationReceivedTest() {
        DeferredResult<LobbyResponsePacket> output1 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller1 = new GameController.EventCaller(output1, "Joe");
        DeferredResult<LobbyResponsePacket> output2 = new DeferredResult<>();
        GameController.EventCaller<LobbyResponsePacket> eventCaller2 = new GameController.EventCaller(output2, "Kate");

        var spy = Mockito.spy(gameService);
        spy.addPlayer("Joe");
        spy.addPlayer("Kate");
        spy.waitForPlayerEvent(eventCaller1);
        spy.waitForPlayerEvent(eventCaller2);

        // calls the method
        spy.onJokerNotificationReceived("JokerNotification", "REMOVE_ANSWER", "Joe");

        assertTrue(spy.getCurrentMatch().getPlayerEventList().size() == 1);
        assertEquals(eventCaller1, spy.getCurrentMatch().getPlayerEventList().get(0));

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
        var spyMatch = Mockito.spy(match);
        //this avoids automatically creating a new match when adding the first player
        spy.setCurrentMatch(spyMatch);

        spy.addPlayer("Ted");
        spy.addPlayer("Kate");
        spy.addPlayer("Joe");

        spy.waitForPlayerEvent(eventCaller1);
        spy.waitForPlayerEvent(eventCaller2);

        // calls the method
        var trimmedList = spy.onPlayerJoin("Joe");
        Map<String, String> expectedList = new HashMap<>();

        // two existing eventCaller should be invoked and cleared
        assertTrue(spy.getCurrentMatch().getPlayerEventList().size() == 0);

        assertTrue(trimmedList.get("Joe") != null);
        assertTrue(trimmedList.get("Ted") != null);
        assertTrue(trimmedList.get("Kate") != null);

        verify(spyMatch, times(1)).trimPlayerList();

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
        spy.addPlayer("Ted");
        spy.addPlayer("Kate");
        spy.waitForPlayerEvent(eventCaller1);
        spy.waitForPlayerEvent(eventCaller2);

        // calls the method
        LobbyResponsePacket responsePacket = spy.onPlayerReady("Ready", "true", "Ted");

        // one of two existing eventCaller should be invoked and cleared
        assertTrue(spy.getCurrentMatch().getPlayerEventList().size() == 1);

        //assertTrue(Joe != null);
        assertEquals("Ready", responsePacket.getType());
        assertEquals("true", responsePacket.getPlayerList().get("Ted"));
        assertEquals("false", responsePacket.getPlayerList().get("Kate"));

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

        Match spyMatch = spy.getCurrentMatch();
        assertEquals(expectedMap,spyMatch.trimPlayerList());
    }

    @Test
    @WithMockUser(username = "someone")
    void getPlayerInSession() throws Exception {
        assertEquals("someone",gameService.getPlayerInSession());
    }
}
