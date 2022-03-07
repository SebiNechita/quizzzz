package commons.questions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultipleChoiceQuestion extends Question{

    private List<Activity> activityList;
    private Activity answer;

    public MultipleChoiceQuestion(String question, List<Activity> activityList, Activity answer) {
        super(question);
        this.activityList = activityList;
        this.answer = answer;
    }

    /**
     * Generate a MultipleChoiceQuestion by randomly picking three activities from the list of unused activities.
     * It also randomly picks one from the 3 as an answer.
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
        Activity answer = activityList.get(
                randomGen.nextInt(activityList.size())
        );
        String question = "Which of the following activities uses " + answer.getConsumption_in_wh() + "wh of energy?";
        return new MultipleChoiceQuestion(question, activityList, answer);
    }

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

    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "question=" + getQuestion() +
                "activityList=" + activityList +
                ", answer=" + answer +
                '}';
    }
}
