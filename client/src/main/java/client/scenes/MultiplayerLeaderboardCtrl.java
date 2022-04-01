package client.scenes;

import client.utils.OnShowScene;
import client.utils.ServerUtils;
import commons.LeaderboardEntry;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MultiplayerLeaderboardCtrl extends SceneCtrl{

    @FXML
    private TableView<LeaderboardEntry> table;
    @FXML
    private TableColumn<LeaderboardEntry, String> colUsername;
    @FXML
    private TableColumn<LeaderboardEntry, Integer> colPoints;
    @FXML
    private Text rankInfo;
    @FXML
    private Button barChartButton;
    @FXML
    private AnchorPane barChartContainer;
    @FXML
    private BarChart barChart;

    @FXML
    protected Text timeLeftText;
    @FXML
    private AnchorPane timeLeftBar;

    private AnchorPane timeLeftSlider;
    private double timeLeft = 0;
    private double timeMultiplier = 1d;
    Animation timer = null;

    public MultiplayerLeaderboardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        super(mainCtrl, serverUtils);
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private Animation timerAnim(AnchorPane anchorPane) {
        return new Transition() {
            {
                setCycleDuration(Duration.millis(10000 * timeMultiplier));
                setInterpolator(Interpolator.LINEAR);
            }

            @Override
            protected void interpolate(double frac) {
                anchorPane.setPrefWidth(25 + 475 * frac);
                timeLeft = timeMultiplier * (1 - frac);
                timeLeftText.setText("Time left: " + (Math.round(100 * timeLeft) / 10d) + "s");
                timeLeftSlider.setBackground(new Background(new BackgroundFill(new Color(0.160, 0.729, 0.901, 1), new CornerRadii(50), Insets.EMPTY)));
            }
        };
    }

    @OnShowScene
    public void onShowScene() {
        timeLeftSlider = (AnchorPane) timeLeftBar.getChildren().get(0);
        timeMultiplier = 1d;
        timer = timerAnim(timeLeftSlider);
        timer.playFromStart();
    }

}
