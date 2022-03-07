package server.user;

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

    protected User() {}

    public User(String username, String password, boolean isLocked, boolean enabled) {
        this(username, password, AppUserRole.USER, isLocked, enabled);
    }

    public User(String username, String password, AppUserRole appUserRole, boolean isLocked, boolean enabled) {
        this.username = username;
        this.password = password;
        this.appUserRole = appUserRole;
        this.isLocked = isLocked;
        this.enabled = enabled;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User appUser)) return false;
        return isLocked == appUser.isLocked && enabled == appUser.enabled && Objects.equals(id, appUser.id) && Objects.equals(username, appUser.username) && Objects.equals(password, appUser.password) && appUserRole == appUser.appUserRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, appUserRole, isLocked, enabled);
    }
}
