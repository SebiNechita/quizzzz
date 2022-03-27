package commons.questions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpenQuestionTest {

    static Activity activity1;
    static Activity activity2;
    static Activity activity3;
    static List<Activity> activityList;
    static OpenQuestion oq1;
    static OpenQuestion oq2;
    static OpenQuestion oq3;


    @BeforeEach
    void setUp() {
        activity1 = new Activity("A", "1", "B", 1, "C");
        activity2 = new Activity("B", "1", "B", 1, "C");
        activity3 = new Activity("A", "1", "B", 1, "C");

        activityList = new LinkedList<>(List.of(activity1, activity2, activity3));

        oq1 = new OpenQuestion("q", 1, activity1);
        oq2 = new OpenQuestion("qq", 2, activity2);
        oq3 = new OpenQuestion("qq",  2, activity2);
    }

    @Test
    void getAnswerInWH() {
        assertEquals(2, oq2.getAnswerInWH());
    }

    @Test
    void setAnswerInWH() {
        oq1.setAnswerInWH(69);
        assertEquals(69, oq1.getAnswerInWH());
    }

    @Test
    void getAnswer() {
        assertEquals(activity1, oq1.getAnswer());
    }

    @Test
    void setAnswer() {
        oq1.setAnswer(activity3);
        assertEquals(activity3, oq1.getAnswer());
    }

    @Test
    void generateOpenQuestion() {
        assertNotNull(OpenQuestion.generateOpenQuestion(activityList));
    }

    @Test
    void testToString() {
        String expected = "OpenQuestion{" +
                "answerInWH=" + 1 +
                ", answer=" + activity1 +
                '}';
    }
}

