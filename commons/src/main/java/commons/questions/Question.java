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
     * Constructor for Question
     *
     * @param question String representation of question
     * @param answer Activity instance of the question
     */
    public Question(String question, Activity answer) {
        this.question = question;
        this.answer = answer;
    }

    /**
     * Getter for activity list
     * @return list of non-answer activities (specific for MultipleChoiceQuestion)
     */
    public List<Activity> getActivityList() {
        return activityList;
    }

    /**
     * Setter for activityList
     * @param activityList A list of non-answer options
     */
    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    /**
     * Getter for Answer
     * @return Activity instance of the answer
     */
    public Activity getAnswer() {
        return answer;
    }


    /**
     * Setter for Answer
     * @param answer Activity instance of the Answer
     */
    public void setAnswer(Activity answer) {
        this.answer = answer;
    }

    /**
     * Getter for energy consumed in wh
     * @return energy consumed in wh
     */
    public long getAnswerInWH() {
        return answerInWH;
    }

    /**
     * Setter for energy consumed in wh
     * @param answerInWH energy consumed in wh
     */
    public void setAnswerInWH(long answerInWH) {
        this.answerInWH = answerInWH;
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
