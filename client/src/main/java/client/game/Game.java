package client.game;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.questions.Question;

import java.util.List;

public interface Game {
    public void jumpToNextQuestion();
    public MainCtrl getMainCtrl();
    public ServerUtils getServer();
    public Integer getScoreTotal();
    public abstract List<Boolean> getQuestionHistory();
    public abstract <T extends Question> T getCurrentQuestion(Class<T> questionType);

    public abstract void addToScore(int total);
}
