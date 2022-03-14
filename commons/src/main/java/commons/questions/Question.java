package commons.questions;

import java.util.List;

public class Question {

    /**
     * String representation of the Question
     */
    private String question;

    /**
     * This list contains three activities that must be the choices displayed on the screen
     */
    private List<Activity> activityList;
    /**
     * The activity that is the answer
     */
    private Activity answer;

    /**
     * The energy consumed in WH of the activity
     */
    private long answerInWH;

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public Activity getAnswer() {
        return answer;
    }

    public void setAnswer(Activity answer) {
        this.answer = answer;
    }

    public long getAnswerInWH() {
        return answerInWH;
    }

    public void setAnswerInWH(long answerInWH) {
        this.answerInWH = answerInWH;
    }

    /**
     * Default Constructor for object mappers
     */
    public Question() {
    }

    /**
     * Constructor for Question
     * @param question
     */
    public Question(String question) {
        this.question = question;
    }

    /**
     * Getter for question
     * @return question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Setter for question
     * @param question String representation of the question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * String representation of Question
     * @return String representation of Question
     */
    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                '}';
    }
}
