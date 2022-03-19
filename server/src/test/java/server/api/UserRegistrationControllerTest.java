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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import packets.RegisterRequestPacket;
import packets.RegisterResponsePacket;
import packets.ResponsePacket;
import packets.UsernameAvailableRequestPacket;
import server.api.registration.RegistrationService;

import static org.mockito.Mockito.*;
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
     * test for register method
     *
     * @throws Exception
     */
    @Test
    public void registerTest()
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();


        when(service.register(new RegisterRequestPacket("Joe", "password")))
                .thenReturn(new RegisterResponsePacket(HttpStatus.Created));

        mvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new RegisterRequestPacket("Joe", "password")))
                        .secure(true)
                )
                .andExpect(status().isOk())
                // the JSON object looks like this: {"RegisterResponsePacket":{"code":201}}
                // 201 means created
                .andExpect(MockMvcResultMatchers.jsonPath("$.RegisterResponsePacket.code").value("201"))
                .andReturn();

        verify(service, times(1)).register(new RegisterRequestPacket("Joe", "password"));

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
