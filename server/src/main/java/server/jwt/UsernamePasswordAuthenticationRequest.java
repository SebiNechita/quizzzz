package server.jwt;

public class UsernamePasswordAuthenticationRequest {

    private String username;
    private String password;

    /**
     * Getter for the username
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter of the username
     *
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the password
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password
     *
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
