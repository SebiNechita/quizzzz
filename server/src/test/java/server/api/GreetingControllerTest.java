package server.api;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * test case for greet() method
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "Geoff")
    public void greetTest() throws Exception {

        MvcResult res = mvc.perform(get("/api/greet/Kim")
                .secure( true )
                .param("name", "Kim" )
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String resStr = res.getResponse().getContentAsString();
        assertTrue(resStr.equals("Hi, Kim!"));

    }
}
