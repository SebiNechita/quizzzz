package commons.questions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultipleChoiceQuestion extends Question {

    /**
     * This list contains 2 activities that must be the choices displayed on the screen other than the Answer which is stored in the variable answer
     */
    private List<Activity> activityList;
    /**
     * The activity that is the answer
     */
    private Activity answer;

    /**
     * Constructor for MultipleChoiceQuestion
     *
     * @param question     String representation of the question
     * @param activityList list of 3 activities that is to be displayed as choices
     * @param answer       the activity that is the answer
     */
    public MultipleChoiceQuestion(String question, List<Activity> activityList, Activity answer) {
        super(question);
        this.activityList = activityList;
        this.answer = answer;
    }

    /**
     * Generate a MultipleChoiceQuestion by randomly picking three activities from the list of unused activities.
     * It also randomly picks one from the 3 as an answer.
     *
     * @param unusedActivities list of unused activities
     * @return MultipleChoiceQuestion generated randomly
     */
    public static MultipleChoiceQuestion generateMultipleChoiceQuestion(List<Activity> unusedActivities) {
        Random randomGen = new Random();
        List<Activity> activityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            activityList.add(
                    unusedActivities.remove(
                            randomGen.nextInt(unusedActivities.size())
                    )
            );
        }
        // Here, the answer is removed from the activity list because this makes it easier to find the non-answer options.
        // The answer is rather stores in the variable answer
        Activity answer = activityList.remove(
                randomGen.nextInt(activityList.size())
        );
        String question = "Which of the following activities uses " + answer.getConsumption_in_wh() + "wh of energy?";
        return new MultipleChoiceQuestion(question, activityList, answer);
    }

    /**
     * Getter for ActivityList
     *
     * @return the activity list
     */
    public List<Activity> getActivityList() {
        return activityList;
    }

    /**
     * Setter for ActivityList
     *
     * @param activityList the list of activities that are not the answer
     */
    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    /**
     * Getter for Answer
     *
     * @return the activity that is answer
     */
    public Activity getAnswer() {
        return answer;
    }

    /**
     * Setter for Answer
     *
     * @param answer the activity that is the answer
     */
    public void setAnswer(Activity answer) {
        this.answer = answer;
    }

    /**
     * String representation of MultipleChoiceAnswer
     *
     * @return String representation of MultipleChoiceAnswer
     */
    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "question=" + getQuestion() +
                "activityList=" + activityList +
                ", answer=" + answer +
                '}';
    }
}
