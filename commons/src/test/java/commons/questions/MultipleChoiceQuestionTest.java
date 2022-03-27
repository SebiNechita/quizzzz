package commons.questions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultipleChoiceQuestionTest {

    static Activity activity1;
    static Activity activity2;
    static Activity activity3;
    static List<Activity> activityList;
    static MultipleChoiceQuestion mcq1;
    static MultipleChoiceQuestion mcq2;
    static MultipleChoiceQuestion mcq3;


    @BeforeEach
    void setUp() {
        activity1 = new Activity("A", "1", "B", 1, "C");
        activity2 = new Activity("B", "1", "B", 1, "C");
        activity3 = new Activity("A", "1", "B", 1, "C");

        activityList = new LinkedList<>(List.of(activity1, activity2, activity3));

        mcq1 = new MultipleChoiceQuestion("q", new ArrayList<>(), activity1);
        mcq2 = new MultipleChoiceQuestion("qq", List.of(activity1, activity3), activity2);
        mcq3 = new MultipleChoiceQuestion("qq",  List.of(activity1, activity3), activity2);
    }

    @Test
    void generateMultipleChoiceQuestion() {
        MultipleChoiceQuestion mcq = MultipleChoiceQuestion.generateMultipleChoiceQuestion(activityList);
        assertNotNull(mcq);
    }

    @Test
    void getActivityList() {
        assertEquals(List.of(activity1, activity3), mcq3.getActivityList());
    }

    @Test
    void setActivityList() {
        mcq1.setActivityList(activityList);
        assertEquals(activityList, mcq1.getActivityList());
    }

    @Test
    void getAnswer() {
        assertEquals(activity2, mcq3.getAnswer());
    }

    @Test
    void setAnswer() {
        mcq3.setAnswer(activity1);
        assertEquals(activity1, mcq3.getAnswer());
    }

    @Test
    void testToString() {
        String expected = "MultipleChoiceQuestion{" +
                "question=qq" +
                "activityList=" + List.of(activity1, activity3) +
                ", answer=" + activity2 +
                '}';
        assertEquals(expected, mcq3.toString());
    }
}
