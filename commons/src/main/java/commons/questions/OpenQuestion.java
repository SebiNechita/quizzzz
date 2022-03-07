package commons.questions;

import java.util.List;
import java.util.Random;

public class OpenQuestion extends Question {

    private long answerInWH;

    public OpenQuestion(String question, long answerInWH) {
        super(question);
        this.answerInWH = answerInWH;
    }

    public long getAnswerInWH() {
        return answerInWH;
    }

    public void setAnswerInWH(long answerInWH) {
        this.answerInWH = answerInWH;
    }

    /**
     * Randomly picks an activity from the list of activities and uses that to create an OpenQuestion
     * @param unusedActivities A list that contains all the activities that hasn't been used yet
     * @return OpenQuestion created using the randomly picked activity
     */
    private static OpenQuestion generateOpenQuestion(List<Activity> unusedActivities) {
        Random randomGen = new Random();
        Activity activity = unusedActivities.remove(
                randomGen.nextInt(unusedActivities.size())
        );

        String question = "How much energy in WH does " +
                activity.getTitle() +
                " consume?";
        long answer = activity.getConsumption_in_wh();

        return new OpenQuestion(question, answer);
    }

    /**
     * This simply checks if the provided answer is correct or not
     * @param openQuestion the question that has been answered
     * @param providedAnswer the answer provided by the user
     * @return true if correct, else false
     */
    public static boolean checkAnswer(OpenQuestion openQuestion, long providedAnswer) {
        return providedAnswer == openQuestion.getAnswerInWH();
    }

    @Override
    public String toString() {
        return "OpenQuestion{" +
                "question=" + getQuestion() +
                "answerInWH=" + answerInWH +
                '}';
    }
}
