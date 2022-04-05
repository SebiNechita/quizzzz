package client.game;

import commons.questions.Question;

import java.util.List;

public interface Game {
    void jumpToNextQuestion();

    Integer getScoreTotal();

    List<Boolean> getQuestionHistory();

    <T extends Question> T getCurrentQuestion(Class<T> questionType);

    void addToScore(int total);
}
