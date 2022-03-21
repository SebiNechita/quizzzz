package commons.questions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {
    static Activity activity1;
    static Activity activity2;
    static Activity activity3;

    @BeforeAll
    static void beforeEach(){
        activity1 = new Activity("A", "1", "B", 1, "C");
        activity2 = new Activity("B", "1", "B", 1, "C");
        activity3 = new Activity("A", "1", "B", 1, "C");
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(activity1,activity1);
    }

    @Test
    void testEqualsOtherObject() {
        assertEquals(activity1,activity3);
    }

    @Test
    void testNotEqualsDifferentTitle() {
        assertNotEquals(activity1,activity2);
    }
}
