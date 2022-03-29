package packets;

//import commons.utils.HttpStatus;

import java.util.List;

public class MultiplayerRequestPacket extends RequestPacket {

    //private LobbyResponsePacket lobby;

    private List<String> playerList;

    public MultiplayerRequestPacket() {
    }

    public MultiplayerRequestPacket(List<String> playerList) {
        this.playerList = playerList;
    }


    /*public MultiplayerRequestPacket( LobbyResponsePacket lobby) {
        this.lobby = lobby;
    }

    public MultiplayerRequestPacket() {
    }

    public LobbyResponsePacket getLobby(){
        return lobby;
    }*/


    /**
     * Checks if this packet is equal to the object specified as a parameter
     *
     * @param other The other object to compare this with
     * @return If they are equal or not
     */
    @Override
    public boolean equals(Object other) {
        return false;
    }

    /**
     * Generates a hashcode for this packet
     *
     * @return The hashcode of this packet
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Translates the data of this packet into a readable format
     *
     * @return A formatted string
     */
    @Override
    public String toString() {
        return null;
    }
}
