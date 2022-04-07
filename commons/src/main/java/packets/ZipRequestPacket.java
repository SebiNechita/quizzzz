package packets;

import java.util.Arrays;

public class ZipRequestPacket extends RequestPacket {

    protected byte[] zipBytes;

    public ZipRequestPacket() {
    }

    public ZipRequestPacket(byte[] zipBytes) {
        this.zipBytes = zipBytes;
    }

    /**
     * Checks if this packet is equal to another object
     * @param o the object to check
     * @return true iff this packet has same bytes as the other object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZipRequestPacket that = (ZipRequestPacket) o;
        return Arrays.equals(zipBytes, that.zipBytes);
    }

    /**
     * Represents this packet as a string
     * @return string representation
     */
    @Override
    public String toString() {
        return "ZipRequestPacket{" +
                "zipBytes=" + Arrays.toString(zipBytes) +
                '}';
    }

    /**
     * Provides hashcode for this packet
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(zipBytes);
    }

    /**
     * Gets the bytes in this packet
     * @return the bytes
     */
    public byte[] getZipBytes() {
        return zipBytes;
    }

    /**
     * Sets the bytes of this packet
     * @param zipBytes the bytes to place in this packet
     */
    public void setZipBytes(byte[] zipBytes) {
        this.zipBytes = zipBytes;
    }
}
