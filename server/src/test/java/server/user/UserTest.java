package server.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getAuthorities() {
        User user1 = new User("Geoff", "password", false, true);
        assertEquals(user1.getAuthorities(), Collections.singletonList(
                new SimpleGrantedAuthority(User.AppUserRole.USER.name())
        ));

        User user2 = new User("Geoff", "password", User.AppUserRole.ADMIN, false, true);
        assertEquals(user2.getAuthorities(), Collections.singletonList(
                new SimpleGrantedAuthority(User.AppUserRole.ADMIN.name())
        ));
    }

    @Test
    void getPassword() {
        User user = new User("Geoff", "password", false, true);
        assertEquals("password", user.getPassword());
    }

    @Test
    void setPassword() {
        User user = new User("Geoff", "password", false, true);
        assertEquals("password", user.getPassword());
        user.setPassword("123");
        assertEquals("123", user.getPassword());
    }

    @Test
    void getUsername() {
        User user = new User("Geoff", "password", false, true);
        assertEquals("Geoff", user.getUsername());
    }

    @Test
    void setUsername() {
        User user = new User("Geoff", "password", false, true);
        assertEquals("Geoff", user.getUsername());
        user.setUsername("jeff");
        assertEquals("jeff", user.getUsername());
    }

    @Test
    void isAccountNonExpired() {
        User user1 = new User("Geoff", "password", false, true);
        assertTrue(user1.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        User user1 = new User("Geoff", "password", false, true);
        assertTrue(user1.isAccountNonLocked());

        User user2 = new User("Geoff", "password", true, false);
        assertFalse(user2.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        User user1 = new User("Geoff", "password", false, true);
        assertTrue(user1.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        User user1 = new User("Geoff", "password", false, true);
        assertTrue(user1.isEnabled());

        User user2 = new User("Geoff", "password", false, false);
        assertFalse(user2.isEnabled());
    }

    @Test
    void testEquals() {
        User user1 = new User("Geoff", "password", false, true);
        User user2 = new User("Geoff", "password", false, true);
        User user3 = new User("DIFF", "password", false, true);
        User user4 = new User("Geoff", "DIFF", false, true);
        User user5 = new User("Geoff", "password", true, true);
        User user6 = new User("Geoff", "password", false, false);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, user4);
        assertNotEquals(user1, user5);
        assertNotEquals(user1, user6);
    }
}
