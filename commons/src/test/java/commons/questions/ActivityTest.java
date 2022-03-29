package commons.questions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    static Activity activity1;
    static Activity activity2;
    static Activity activity3;

    @BeforeEach
    void setUp(){
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

    @Test
    void readActivities() {
        assertThrows(IOException.class, () -> Activity.readActivities(new File("")));
    }

    @Test
    void setTitle() {
        activity1.setTitle("AA");
        assertEquals("AA", activity1.getTitle());
    }

    @Test
    void setId() {
        activity1.setId("12345");
        assertEquals("12345", activity1.getId());
    }

    @Test
    void setImagePath() {
        activity1.setImagePath("src/main/image.jpg");
        assertEquals("src/main/image.jpg", activity1.getImage_path());
    }

    @Test
    void setConsumptionInWH() {
        activity1.setConsumptionInWH(69);
        assertEquals(69, activity1.getConsumption_in_wh());
    }

    @Test
    void setSource() {
        activity1.setSource("www.idk.com");
        assertEquals("www.idk.com", activity1.getSource());
    }

    @Test
    void getTitle() {
        assertEquals("A", activity1.getTitle());
    }

    @Test
    void getId() {
        assertEquals("1", activity1.getId());
    }

    @Test
    void getImage_path() {
        assertEquals("B", activity1.getImage_path());
    }

    @Test
    void getConsumption_in_wh() {
        assertEquals(1, activity1.getConsumption_in_wh());
    }

    @Test
    void getSource() {
        assertEquals("C", activity1.getSource());
    }

    @Test
    void testHashCode() {
        assertEquals(activity1.hashCode(), activity3.hashCode());
    }

    @Test
    void testToString() {
        String expected = "Activity{" +
                "title='A" + '\'' +
                ", id=1" +
                ", imagePath='B" +  '\'' +
                ", consumptionInWH=1" +
                ", source='C" + '\'' +
                '}';
        assertEquals(expected, activity1.toString());
    }
}
