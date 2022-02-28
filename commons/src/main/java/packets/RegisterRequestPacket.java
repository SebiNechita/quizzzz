package packets;

import java.util.Objects;

public class RegisterRequestPacket {

    private String username;
    private String password;

    protected RegisterRequestPacket() {}

    public RegisterRequestPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterRequestPacket that)) return false;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "RegisterRequestPacket{" +
               "username='" + username + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
