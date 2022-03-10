package packets;

public abstract class RequestPacket {

    /**
     * Checks if this packet is equal to the object specified as a parameter
     *
     * @param other The other object to compare this with
     * @return If they are equal or not
     */
    @Override
    public abstract boolean equals(Object other);

    /**
     * Generates a hashcode for this packet
     *
     * @return The hashcode of this packet
     */
    @Override
    public abstract int hashCode();

    /**
     * Translates the data of this packet into a readable format
     *
     * @return A formatted string
     */
    @Override
    public abstract String toString();
}
