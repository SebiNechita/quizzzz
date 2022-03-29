package client.game;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;

public interface Game {
    public void jumpToNextQuestion();
    public MainCtrl getMainCtrl();
    public ServerUtils getServer();
    public Integer getScoreTotal();
}
