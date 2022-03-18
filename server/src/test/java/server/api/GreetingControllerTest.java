package server.api;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    public void greetTest() throws Exception {

        MvcResult res = mvc.perform(get("/api/greet")
                .param("name", "Kim" )
        ).andReturn();

        String resStr = res.getResponse().getContentAsString();
        System.out.println("=====================================3##########################"+resStr);
        assertTrue(resStr.equals("Hi Kim!"));

    }
}
