package server.user;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;

    public enum AppUserRole {
        USER,
        ADMIN
    }

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private boolean isLocked;
    private boolean enabled;

    /**
     * Empty constructor required for (de)serialization into/from json
     */
    protected User() {
    }

    /**
     * Creates a new user
     *
     * @param username The username for this user
     * @param password The password for this user
     * @param isLocked If the account should be locked or not
     * @param enabled  If the account should be enabled or not
     */
    public User(String username, String password, boolean isLocked, boolean enabled) {
        this(username, password, AppUserRole.USER, isLocked, enabled);
    }

    /**
     * Creates a new user
     *
     * @param username    The username for this user
     * @param password    The password for this user
     * @param appUserRole The role of this user
     * @param isLocked    If the account should be locked or not
     * @param enabled     If the account should be enabled or not
     */
    public User(String username, String password, AppUserRole appUserRole, boolean isLocked, boolean enabled) {
        this.username = username;
        this.password = password;
        this.appUserRole = appUserRole;
        this.isLocked = isLocked;
        this.enabled = enabled;
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of just user
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the username used to authenticate the user. Cannot return
     * <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of this user
     *
     * @param username the username
     */
    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Compares this instance to another one to check if they are euqal
     *
     * @param o The other object to compare this one with
     * @return If they are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User appUser)) return false;
        return isLocked == appUser.isLocked && enabled == appUser.enabled && Objects.equals(id, appUser.id) && Objects.equals(username, appUser.username) && Objects.equals(password, appUser.password) && appUserRole == appUser.appUserRole;
    }

    /**
     * Generates a hashcode for this class
     *
     * @return The hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, appUserRole, isLocked, enabled);
    }
}
