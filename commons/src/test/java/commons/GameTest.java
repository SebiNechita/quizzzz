package commons;

import commons.questions.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    static Game g1;
    static Game g2;
    static Game g3;

    static List<Activity> activityList1;
    static List<Activity> activityList2;
    static List<Activity> activityList3;

    @BeforeEach
    void setUp() {
        Activity activity1 = new Activity("A", "1", "B", 1, "C");
        Activity activity2 = new Activity("B", "1", "B", 1, "C");
        Activity activity3 = new Activity("A", "1", "B", 1, "C");

        activityList1 = new LinkedList<>(List.of(activity1, activity2, activity3));
        activityList2 = new LinkedList<>(List.of(activity1, activity3));
        activityList3 = new LinkedList<>(List.of(activity1, activity2, activity3));

        g1 = new Game(activityList1);
        g2 = new Game(activityList2);
        g3 = new Game(activityList3);
    }

    @Test
    void getMultipleChoiceQuestions() {
        assertNotNull(g1.getMultipleChoiceQuestions());
    }

    @Test
    void getOpenQuestions() {
        assertNotNull(g1.getOpenQuestions());
    }

    @Test
    void getNoOfMultipleChoiceQuestions() {
        // Here, it is 0 because the activity list runs out of activities
        assertEquals(0, g1.getNoOfMultipleChoiceQuestions());
    }

    @Test
    void getNoOfOpenQuestions() {
        // Here, it is 3 because activity list runs out of activities
        assertEquals(3, g1.getNoOfOpenQuestions());
    }

    @Test
    void getNoOfQuestions() {
        assertEquals(20, g1.getNoOfQuestions());
    }

    @Test
    void getActivities() {
        assertEquals(activityList1, g1.getActivities());
    }

    @Test
    void setActivities() {
        g1.setActivities(activityList2);
        assertEquals(activityList2, g1.getActivities());
    }

    @Test
    void testToString() {
        String expected = "Game{" +
                "multipleChoiceQuestions=" + g1.getMultipleChoiceQuestions() +
                ", openQuestions=" + g1.getOpenQuestions() +
                ", noOfQuestions=" + g1.getNoOfQuestions() +
                ", noOfMultipleChoiceQuestions=" + g1.getNoOfMultipleChoiceQuestions() +
                ", noOfOpenQuestions=" + g1.getNoOfOpenQuestions() +
                ", activities=" + g1.getActivities() +
                '}';
        assertEquals(expected, g1.toString());
    }

    @Test
    void testEquals() {
        assertNotEquals(g1, g2);
    }

    @Test
    void testHashCode() {
        assertNotEquals(g1, g2);
    }
}
