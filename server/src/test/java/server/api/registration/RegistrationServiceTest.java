package server.api.registration;

import commons.utils.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import packets.*;
import server.user.User;
import server.user.UserService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationServiceTest {

    @MockBean
    private UserService userService;

    RegistrationService registrationService;

    @BeforeEach
    void initService() {
        registrationService = new RegistrationService(userService);
    }

    /**
     * test case for successful registration
     *
     * @throws Exception
     */
    @Test
    public void registerTest()
            throws Exception {
        when(userService.signUpUser(new User("Joe", "password", false, true)))
                .thenReturn(new RegisterResponsePacket(HttpStatus.Created));

        RegisterResponsePacket res = registrationService.register(new RegisterRequestPacket("Joe", "password"));

        assertEquals(HttpStatus.Created, res.getResponseStatus());

        verify(userService, times(1)).signUpUser(new User("Joe", "password", false, true));

    }

    /**
     * test case for checking non-existing user, should return OK
     *
     * @throws Exception
     */
    @Test
    public void userAvailableTest()
            throws Exception {
        when(userService.userExists("Joe"))
                .thenReturn(false);

        ResponsePacket res = registrationService.userAvailable("Joe");

        assertEquals(HttpStatus.OK, res.getResponseStatus());

        verify(userService, times(1)).userExists("Joe");

    }

    /**
     * delete an existing user by an authorized user, should return Accepted
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "George")
    public void deleteUserTest()
            throws Exception {

        when(userService.userExists("Joe"))
                .thenReturn(true);

        when(userService.validUser("Joe", "password"))
                .thenReturn(true);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        DeleteResponsePacket res = registrationService.deleteUser(new DeleteRequestPacket("Joe", "password"), authentication.getAuthorities());

        assertEquals(HttpStatus.Accepted, res.getResponseStatus());

        verify(userService, times(1)).userExists("Joe");
        verify(userService, times(1)).validUser("Joe", "password");

    }
}
