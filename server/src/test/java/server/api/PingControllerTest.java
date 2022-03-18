package server.api;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import utils.SSLUtil;

import javax.net.ssl.HttpsURLConnection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void pingTest() throws Exception {
        SSLUtil.turnOffSslChecking();

        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));

        assertThat(this.restTemplate.getForObject("https://localhost:" + port + "/ping",
                String.class)).contains("Pong");

        SSLUtil.turnOnSslChecking();
    }


}
