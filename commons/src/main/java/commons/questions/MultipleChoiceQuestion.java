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
     * Empty Constructor for Jackson
     */
    public MultipleChoiceQuestion() {
    }

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
     * It also picks one from the 3 as an answer.
     *
     * @param unusedActivities list of unused activities
     * @return MultipleChoiceQuestion generated randomly
     */
    public static MultipleChoiceQuestion generateMultipleChoiceQuestion(List<Activity> unusedActivities) {
        Random randomGen = new Random();
        List<Activity> activityList = new ArrayList<>();
        long pivot = -1;
        if (unusedActivities.size() < 3) {
            return null;
        }
        while (activityList.size() < 3) {
            int randInt = randomGen.nextInt(unusedActivities.size());
            activityList.add(
                    unusedActivities.remove(randInt)
            );
        }
        if (randomGen.nextBoolean()) {
            return generateSelectMaximumQuestion(activityList);
        } else {
            return generateSelectByConsumptionQuestion(activityList, randomGen.nextInt(activityList.size()));
        }

    }

    /**
     * Creates a question asking for the activity using the most energy out of the three
     *
     * @param activityList the three activities
     * @return the generated question
     */
    public static MultipleChoiceQuestion generateSelectMaximumQuestion(List<Activity> activityList) {
        //TODO: Replace with some stream magic?
        Activity max = activityList.get(0);
        if (activityList.get(1).getConsumption_in_wh() > max.getConsumption_in_wh()) {
            max = activityList.get(1);
        }
        if (activityList.get(2).getConsumption_in_wh() > max.getConsumption_in_wh()) {
            max = activityList.get(2);
        }
        activityList.remove(max);
        String question = "Which of the following activities uses the most energy?";
        return new MultipleChoiceQuestion(question, activityList, max);
    }

    /**
     * Creates a question asking for which activity out of the three uses the given amount of energy.
     *
     * @param activityList the three activities
     * @param answerNo     the index of the answer activity
     * @return the generated question
     */
    public static MultipleChoiceQuestion generateSelectByConsumptionQuestion(List<Activity> activityList, int answerNo) {
        // Here, the answer is removed from the activity list because this makes it easier to find the non-answer options.
        // The answer is rather stores in the variable answer
        Activity answer = activityList.remove(answerNo);
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
     * @return the {@link Activity} that is the answer
     */
    public Activity getAnswer() {
        return answer;
    }

    /**
     * Setter for Answer
     *
     * @param answer the {@link Activity} that is the answer
     */
    public void setAnswer(Activity answer) {
        this.answer = answer;
    }

    /**
     * String representation of MultipleChoiceQuestion
     *
     * @return String representation of MultipleChoiceQuestion
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
