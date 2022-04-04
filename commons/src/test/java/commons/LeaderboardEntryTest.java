package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderboardEntryTest {
    static LeaderboardEntry entry1;
    static LeaderboardEntry entry2;
    static LeaderboardEntry entry3;

    @BeforeEach
    void setUp() {
        entry1 = new LeaderboardEntry(20, "Mark");
        entry2 = new LeaderboardEntry(10, "Mark");
        entry3 = new LeaderboardEntry(20, "Mark");
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(entry1, entry1);
    }

    @Test
    void testEqualsOtherObject() {
        assertEquals(entry1, entry3);
    }

    @Test
    void testNotEqualsDifferentObject() {
        assertNotEquals(entry1, entry2);
    }

    @Test
    void setID() {
        entry1.setId(100L);
        assertEquals(100L, entry1.getId());
    }

    @Test
    void setPoints() {
        entry1.setPoints(100);
        assertEquals(100, entry1.getPoints());
    }

    @Test
    void setUsername() {
        entry1.setUsername("Papagal");
        assertEquals("Papagal", entry1.getUsername());
    }

    @Test
    void getID() {
        entry1.setId(100L);
        assertEquals(100L, entry1.getId());
    }

    @Test
    void getPoints() {
        assertEquals(20, entry1.getPoints());
    }

    @Test
    void getUsername() {
        assertEquals("Mark", entry1.getUsername());
    }

    @Test
    void testHashCode() {
        assertEquals(entry1.hashCode(), entry3.hashCode());
    }

    @Test
    void testToString() {
        String expected = "LeaderboardEntry{" +
                "id=" + entry1.getId() +
                ", points=20" +
                ", username=Mark" + '\'' +
                '}';

        assertEquals(expected, entry1.toString());
    }
}
