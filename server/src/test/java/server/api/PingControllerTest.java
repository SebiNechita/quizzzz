package server.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PingControllerTest {


    @Autowired
    private MockMvc mvc;

    @Test
    public void pingTest() throws Exception {
        MvcResult res = mvc.perform(get("/ping")
                .secure( true )
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String resStr = res.getResponse().getContentAsString();
        String expected = "Pong";
        assertTrue(resStr.equals(expected));
    }


}
