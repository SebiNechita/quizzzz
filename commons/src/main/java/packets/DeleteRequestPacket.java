package packets;

/**
 * Packet for requesting a deletion of a certain user. Either an admin needs to send this
 * request with the user's username they want to delete, or the combination of the user's
 * username and password must be given, in order for this request to be processed.
 */
public class DeleteRequestPacket extends RegisterRequestPacket {
    protected DeleteRequestPacket() {}

    public DeleteRequestPacket(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && o instanceof DeleteRequestPacket;
    }

    @Override
    public String toString() {
        return "DeleteRequestPacket{" +
               "username='" + username + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
