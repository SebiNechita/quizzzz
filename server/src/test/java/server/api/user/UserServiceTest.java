package server.api;

import commons.utils.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import packets.RegisterResponsePacket;
import server.database.UserRepository;
import server.user.User;
import server.user.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    UserService userService;

    @BeforeEach
    void initService() {

        userService = new UserService(userRepository, new BCryptPasswordEncoder());
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * test case for finding an existing User
     *
     * @throws Exception
     */
    @Test
    public void findByUserNameTest() throws Exception {
        when(userRepository.findByUsername("Joe"))
                .thenReturn(Optional.of(new User("Joe", "password", false, true)));

        UserDetails res = userService.loadUserByUsername("Joe");

        assertEquals("Joe", res.getUsername());
        assertEquals("password", res.getPassword());

        verify(userRepository, times(1)).findByUsername("Joe");
    }

    /**
     * test case for an existing user, should return true
     *
     * @throws Exception
     */
    @Test
    public void userExistsTest() throws Exception {
        when(userRepository.findByUsername("Joe"))
                .thenReturn(Optional.of(new User("Joe", "password", false, true)));

        boolean res = userService.userExists("Joe");

        assertTrue(res);

        verify(userRepository, times(1)).findByUsername("Joe");
    }

    /**
     * test case for signing up non-existing user, should return Created
     *
     * @throws Exception
     */
    @Test
    public void signUpUserTest() throws Exception {
        when(userRepository.findByUsername("Joe"))
                .thenReturn(Optional.empty());
        when(userRepository.save(any()))
                .thenReturn(new User("Joe", "password", false, true));

        RegisterResponsePacket res = userService.signUpUser(new User("Joe", "password", false, true));

        assertEquals(HttpStatus.Created, res.getResponseStatus());

        verify(userRepository, times(1)).findByUsername("Joe");
        verify(userRepository, times(1)).save(any());
    }

    /**
     * test case for validating an existing user, should return true
     *
     * @throws Exception
     */
    @Test
    public void validUserTest() throws Exception {
        when(userRepository.findByUsername("Joe"))
                .thenReturn(Optional.of(new User("Joe", bCryptPasswordEncoder.encode("password"), false, true)));

        boolean res = userService.validUser("Joe", "password");
        assertTrue(res);

        verify(userRepository, times(1)).findByUsername("Joe");
    }

    /**
     * test case for deleting a existing user, should be successful
     * @throws Exception
     */
    @Test
    public void deleteUserTest() throws Exception {
        userService.deleteUser(new User("Joe", "password", false, true));

        verify(userRepository,times(1)).delete(new User("Joe", "password", false, true));
    }
}
