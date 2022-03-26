package client.game;

import client.Main;
import client.scenes.*;
import client.utils.ServerUtils;
import commons.Game;
import commons.LeaderboardEntry;
import commons.questions.MultipleChoiceQuestion;
import commons.questions.OpenQuestion;
import commons.questions.Question;
import javafx.application.Platform;
import packets.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


public class MultiplayerGame {
    private final MainCtrl main;
    private final ServerUtils server;

    private ScheduledFuture<?> pingThread;
    private ServerUtils.LongPollingRequest longPollingRequest;

    private List<Question> questions;
    private LinkedList<Boolean> questionHistory;

    private Integer currentQuestionCount;

    private Integer scoreTotal;

    public MultiplayerGame(MainCtrl main, ServerUtils server) {
        this.main = main;
        this.server = server;
        this.questionHistory = new LinkedList<>();
        this.questions = new ArrayList<>();

        Game game = server.getGame();

        List<OpenQuestion> openQuestions = game.getOpenQuestions();
        List<MultipleChoiceQuestion> multipleChoiceQuestions = game.getMultipleChoiceQuestions();

        int totalQuestions = 20;
        int currentOQ = 0;
        int currentMCQ = 0;

        for (int i = 0; i < totalQuestions; i++) {
            if (i % 5 == 0) {
                questions.add(openQuestions.get(currentOQ++));
            } else {
                questions.add(multipleChoiceQuestions.get(currentMCQ++));
            }
        }

        this.currentQuestionCount = 0;
        this.scoreTotal = 0;
    }

    public MultiplayerGame(MainCtrl mainCtrl, ServerUtils server, List<Question> questions) {
        this.main = mainCtrl;
        this.server = server;
        this.questions = questions;
        this.questionHistory = new LinkedList<>();
        this.currentQuestionCount = 0;
        this.scoreTotal = 0;
    }

    /**
     * Retrieves a list of questions and stores it.
     * @return returns the list of questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Steps to the next question and displays it.
     * Or exits if the game is over.
     */
    public void jumpToNextQuestion() {
        if (currentQuestionCount < 20) {
            showQuestion();
        } else {
//            server.postRequest("api/leaderboard", new LeaderboardEntry(this.scoreTotal, Main.USERNAME), LeaderboardResponsePacket.class);
//            main.showScene(EndGameCtrl.class);
        }
        currentQuestionCount++;
    }

    /**
     * Decides which type of question to display.
     */
    public void showQuestion() {
        if (questions.get(currentQuestionCount).getClass().equals(OpenQuestion.class)) {
            main.showScene(GameOpenQuestionCtrl.class);
        } else {
            main.showScene(GameMultiChoiceCtrl.class);
        }
    }

    /**
     * Gets the current question to be displayed from the list of questions
     * @param type Class of the question type. For example, OpenQuestion.class
     * @param <T> Should be a subclass of Question
     * @return returns a subclass of Question (OpenQuestion/MultipleChoiceQuestion)
     */
    public <T extends Question> T getCurrentQuestion(Class<T> type) {
        if (questions.get(currentQuestionCount) != null)  {
            return (T) questions.get(currentQuestionCount);
        }
        return null;
    }

    /**
     * Add a number to the current score
     * @param scoreToBeAdded the number by which score must be incremented
     */
    public void addToScore(int scoreToBeAdded) {
        this.scoreTotal += scoreToBeAdded;
    }

    /**
     * Getter for MainCtrl
     * @return mainCtrl
     */
    public MainCtrl getMainCtrl() {
        return main;
    }

    /**
     * Getter for ServerUtils
     * @return the serverUtils
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * Setter for the list of questions
     * @param questions the list of questions
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * Getter for the current question count.
     * For example, if the game is displaying the 10th question, this will be 10.
     * @return the current question count
     */
    public Integer getCurrentQuestionCount() {
        return currentQuestionCount;
    }

    /**
     * Setter for the current question count.
     * For example, if the game is displaying the 10th question, this will be 10.
     * @param currentQuestionCount the current question count
     */
    public void setCurrentQuestionCount(Integer currentQuestionCount) {
        this.currentQuestionCount = currentQuestionCount;
    }

    /**
     * Getter for the total score
     * @return the total score
     */
    public Integer getScoreTotal() {
        return scoreTotal;
    }

    /**
     * Setter for the total score
     * @param scoreTotal the total score
     */
    public void setScoreTotal(Integer scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    /**
     * Getter for the question history.
     * This contains a linked list of boolean. True if the question has been answered correctly. False otherwise.
     * For open questions, it is true if the difference between the answer and the input is < 50.
     * This is used in producing the progress bar
     * @return the question history
     */
    public LinkedList<Boolean> getQuestionHistory() {
        return questionHistory;
    }

    /**
     * Setter for the question history
     * This contains a linked list of boolean. True if the question has been answered correctly. False otherwise.
     * For open questions, it is true if the difference between the answer and the input is < 50.
     * This is used in producing the progress bar
     * @param questionHistory the question history
     */
    public void setQuestionHistory(LinkedList<Boolean> questionHistory) {
        this.questionHistory = questionHistory;
    }

    /**
     * starts the persistent ping thread, which pings the server every 0.5 second
     *
     * @param username this client's username
     */
    public void startPingThread(String username) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        pingThread = executor.scheduleAtFixedRate(() -> {
            server.postRequest("api/game/ping",
                    new PingRequestPacket(username),
                    GeneralResponsePacket.class);
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * stops the ping thread. should be called when 'back' is clicked.
     */
    public void stopPingThread() {
        pingThread.cancel(false);
    }


    /**
     * Join a player to a lobby
     *
     * @param username this client's username
     */
    public void join(String username) {

        JoinResponsePacket responsePacket = server.postRequest("api/game/join",
                new JoinRequestPacket(username),
                JoinResponsePacket.class);
        Platform.runLater(() ->
                main.getCtrl(LobbyCtrl.class)
                        .updatePlayerList(responsePacket.getPlayerList()));
    }

    /**
     * send clicked Emote to the server
     *
     * @param username this client's username
     * @param emoteStr emote name
     * @return GeneralResponsePacket
     */
    public GeneralResponsePacket sendEmote(String username, String emoteStr) {
        return server.postRequest("api/game/emote",
                new EmoteRequestPacket(username, emoteStr),
                GeneralResponsePacket.class);
    }

    /**
     * updates emote in this client according to updates sent by the server
     *
     * @param from  sender of the emote
     * @param emote emote name
     */
    private void updateEmote(String from, String emote) {
        Platform.runLater(() ->
                main.getCtrl(LobbyCtrl.class)
                        .updateEmoji(from, emote));

    }

    /**
     * send ready message to the server
     *
     * @param username this client's username
     * @param isReady  is ready or not
     */
    public void sendReadyMsg(String username, boolean isReady) {
        String isReadyStr = "false";
        if (isReady) {
            isReadyStr = "true";
        }

        LobbyResponsePacket responsePacket = server.postRequest("api/game/ready",
                new ReadyRequestPacket(username, isReadyStr),
                LobbyResponsePacket.class);

        // type equals "Ready"
        Platform.runLater(() ->
                main.getCtrl(LobbyCtrl.class)
                        .updatePlayerList(responsePacket.getPlayerList()));

        if (responsePacket.getType().equals("AllReady")) {
            Platform.runLater(() ->
                    main.getCtrl(LobbyCtrl.class)
                            .showStartButton());
        } else if (responsePacket.getType().equals("CancelAllReady")) {
            Platform.runLater(() ->
                    main.getCtrl(LobbyCtrl.class)
                            .hideStartButton());
        }

    }

    /**
     * send persistent long polling request to the server. And it should get updates from the server about other players.
     */
    public void getLobbyUpdate() {
        ServerUtils.LongPollingRequest<LobbyResponsePacket> request
                = server.longGetRequest("api/game/lobbyEventListener",
                LobbyResponsePacket.class, new LobbyOnResponse());
        // set persistent long-polling to true
        request.setPersistent(true);
        this.longPollingRequest = request;
        request.getRequest();
    }

    /**
     * stops the long polling request
     */
    public void stopLobbyUpdate() {
        this.longPollingRequest.stop();
    }

    /**
     * callback method when the client gets update from the server via the long polling request.
     */
    private class LobbyOnResponse implements ServerUtils.ServerResponse<LobbyResponsePacket> {
        @Override
        public void run(LobbyResponsePacket responsePacket) {
            if (responsePacket.getType().equals("Emote")) {
                updateEmote(responsePacket.getFrom(), responsePacket.getContent());
            } else if (responsePacket.getType().equals("Join")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
            } else if (responsePacket.getType().equals("Ready")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updateReady(responsePacket.getFrom()
                                        , responsePacket.getContent()));
            } else if (responsePacket.getType().equals("AllReady")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .showStartButton());
            } else if (responsePacket.getType().equals("CancelAllReady")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .hideStartButton());
            } else if (responsePacket.getType().equals("Leave")) {
                Platform.runLater(() ->
                        main.getCtrl(LobbyCtrl.class)
                                .updatePlayerList(responsePacket.getPlayerList()));
            }

        }

    }

}
