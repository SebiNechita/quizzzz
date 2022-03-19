package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.utils.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import packets.*;
import server.api.registration.RegistrationService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtil.asJsonString;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegistrationService service;

    /**
     * test for unauthenticated register request.
     *
     * @throws Exception
     */
    @Test
    public void registerTest()
            throws Exception {

        when(service.register(new RegisterRequestPacket("Joe", "password")))
                .thenReturn(new RegisterResponsePacket(HttpStatus.Created));

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequestPacket("Joe", "password")))
                        .secure(true)
                )
                .andExpect(status().isOk())
                // the JSON object looks like this: {"RegisterResponsePacket":{"code":201}}
                // 201 means created
                .andExpect(MockMvcResultMatchers.jsonPath("$.RegisterResponsePacket.code").value(HttpStatus.Created.getCode()));

        verify(service, times(1)).register(new RegisterRequestPacket("Joe", "password"));

    }

    /**
     * test for authenticated delete user request
     * @throws Exception
     */
    @WithMockUser(username = "Geoff")
    @Test
    public void deleteUserByUserTest() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        when(service.deleteUser(new DeleteRequestPacket("Kim", "password"), authentication.getAuthorities()))
                .thenReturn(new DeleteResponsePacket(HttpStatus.Accepted, "User has been deleted"));

        //JSON convertor
        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(delete("/api/user/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DeleteRequestPacket("Kim", "password")))
                        .secure(true)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.DeleteResponsePacket.code").value(HttpStatus.Accepted.getCode()));


        verify(service, times(1)).deleteUser(new DeleteRequestPacket("Kim", "password"), authentication.getAuthorities());

    }

    @Test
    public void availableTest()
            throws Exception {

        when(service.userAvailable("Kim"))
                .thenReturn(new ResponsePacket(HttpStatus.OK));

        mvc.perform(post("/api/user/available")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new UsernameAvailableRequestPacket("Kim")))
                        .secure(true)
                )
                .andExpect(status().isOk());

        verify(service, times(1)).userAvailable("Kim");
    }

}
