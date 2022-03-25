package packets;

import java.util.Arrays;

public class ZipRequestPacket extends RequestPacket{

    protected byte[] zipBytes;

    public ZipRequestPacket() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZipRequestPacket that = (ZipRequestPacket) o;
        return Arrays.equals(zipBytes, that.zipBytes);
    }

    @Override
    public String toString() {
        return "ZipRequestPacket{" +
                "zipBytes=" + Arrays.toString(zipBytes) +
                '}';
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(zipBytes);
    }

    public ZipRequestPacket(byte[] zipBytes) {
        this.zipBytes = zipBytes;
    }

    public byte[] getZipBytes() {
        return zipBytes;
    }

    public void setZipBytes(byte[] zipBytes) {
        this.zipBytes = zipBytes;
    }
}
