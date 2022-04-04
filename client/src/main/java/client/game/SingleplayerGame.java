package client.game;

import client.Main;
import client.scenes.EndGameCtrl;
import client.scenes.GameMultiChoiceCtrl;
import client.scenes.GameOpenQuestionCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Game;
import commons.LeaderboardEntry;
import commons.questions.MultipleChoiceQuestion;
import commons.questions.OpenQuestion;
import commons.questions.Question;
import packets.LeaderboardResponsePacket;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SingleplayerGame implements client.game.Game {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    private List<Question> questions;
    private LinkedList<Boolean> questionHistory;

    private Integer currentQuestionCount;

    private Integer scoreTotal;

    public SingleplayerGame(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
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

    public SingleplayerGame(MainCtrl mainCtrl, ServerUtils server, List<Question> questions) {
        this.mainCtrl = mainCtrl;
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
            server.postRequest("api/leaderboard", new LeaderboardEntry(this.scoreTotal, Main.USERNAME), LeaderboardResponsePacket.class);
            mainCtrl.showScene(EndGameCtrl.class);
        }
        currentQuestionCount++;
    }

    /**
     * Decides which type of question to display.
     */
    public void showQuestion() {
        if (questions.get(currentQuestionCount).getClass().equals(OpenQuestion.class)) {
            mainCtrl.showScene(GameOpenQuestionCtrl.class);
        } else {
            mainCtrl.showScene(GameMultiChoiceCtrl.class);
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
        return mainCtrl;
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
}
