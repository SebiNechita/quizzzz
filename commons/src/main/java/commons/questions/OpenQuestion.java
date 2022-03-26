package commons.questions;

import java.util.List;
import java.util.Random;

public class OpenQuestion extends Question {

    /**
     * The energy consumed in WH of the activity
     */
    private long answerInWH;

    /**
     * The activity that is used to create this open question
     */
    private Activity answer;

    /**
     * Empty Constructor for Jackson
     */
    public OpenQuestion() {}

    /**
     * Constructor for OpenQuestion
     *
     * @param question   String representation of the question
     * @param answerInWH energy consumed in wh of the activity
     */
    public OpenQuestion(String question, long answerInWH) {
        super(question);
        this.answerInWH = answerInWH;
    }

    /**
     * Constructor for OpenQuestion
     *
     * @param question String representation of the question
     * @param answerInWH energy consumed in wh
     * @param answer Activity instance of this question
     */
    public OpenQuestion(String question, long answerInWH, Activity answer) {
        super(question);
        this.answer = answer;
        this.answerInWH = answerInWH;
    }

    /**
     * Getter for answerInWH
     *
     * @return answerInWH
     */
    public long getAnswerInWH() {
        return answerInWH;
    }

    /**
     * Setter for answerInWH
     *
     * @param answerInWH energy consumed in WH
     */
    public void setAnswerInWH(long answerInWH) {
        this.answerInWH = answerInWH;
    }

    /**
     * Getter for answer
     * @return the {@link Activity} that is used for this open question
     */
    public Activity getAnswer() {
        return answer;
    }

    /**
     * Setter for answer
     * @param answer the {@link Activity} that is used for this open question
     */
    public void setAnswer(Activity answer) {
        this.answer = answer;
    }

    /**
     * Randomly picks an activity from the list of activities and uses that to create an OpenQuestion
     *
     * @param unusedActivities A list that contains all the activities that hasn't been used yet
     * @return OpenQuestion created using the randomly picked activity
     */
    public static OpenQuestion generateOpenQuestion(List<Activity> unusedActivities) {
        Random randomGen = new Random();
        Activity activity = unusedActivities.remove(
                randomGen.nextInt(unusedActivities.size())
        );

        String question = "How much energy in WH does " +
                activity.getTitle() +
                " consume?";
        long answer = activity.getConsumption_in_wh();

        return new OpenQuestion(question, answer, activity);
    }

    /**
     * String representation of OpenQuestion
     * @return String representation of OpenQuestion
     */
    @Override
    public String toString() {
        return "OpenQuestion{" +
                "answerInWH=" + answerInWH +
                ", answer=" + answer +
                '}';
    }
}
