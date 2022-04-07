package packets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardRequestPacketTest {
    LeaderboardRequestPacket packetA;
    LeaderboardRequestPacket packetACopy;
    LeaderboardRequestPacket packetB;

    @BeforeEach
    void beforeEach(){
        packetA = new LeaderboardRequestPacket("A",500);
        packetACopy = new LeaderboardRequestPacket("A",500);
        packetB = new LeaderboardRequestPacket("B",500);
    }

    @Test
    void testToString() {
        String expected = "LeaderboardRequestPacket{" +
                "player='" + "A" + '\'' +
                ", score=" + 500 +
                '}';
        assertEquals(expected,packetA.toString());
    }

    @Test
    void testEqualsTrueSameObject() {
        assertEquals(packetA,packetA);
    }

    @Test
    void testEqualsTrueOtherObject() {
        assertEquals(packetA,packetACopy);
    }

    @Test
    void testEqualsFalse() {
        assertNotEquals(packetA,packetB);
    }
}
