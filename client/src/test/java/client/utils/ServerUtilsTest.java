package client.utils;

import client.Main;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.utils.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.socket.PortFactory;
import org.mockserver.socket.tls.KeyStoreFactory;
import org.mockserver.verify.VerificationTimes;
import packets.RegisterResponsePacket;

import javax.net.ssl.HttpsURLConnection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

// this SSUtil is from this answer bellow. it's purpose is to turn off SSL validation when testing.
// https://stackoverflow.com/questions/23504819/how-to-disable-ssl-certificate-checking-with-spring-resttemplate
public class ServerUtilsTest {
    private static ClientAndServer mockClientServer;
    private static int port = -1;

    /**
     * set up some variables before tests
     */
    @BeforeAll
    public static void startMockServer() {
        HttpsURLConnection.setDefaultSSLSocketFactory(new KeyStoreFactory(new MockServerLogger()).sslContext().getSocketFactory());
        port = PortFactory.findFreePort();
        mockClientServer = startClientAndServer(port);
        Main.URL = "https://localhost:" + port;
        Main.TOKEN = "mock token";
    }

    /**
     * stops the mock server after tests
     */
    @AfterAll
    public static void stopMockServer() {
        mockClientServer.stop();
    }

    /**
     * test case for a successful server connection
     */
    @Test
    public void testConnectionTest() {
        mockClientServer.when(new HttpRequest().withMethod("GET").withPath("/ping"))
                .respond(new HttpResponse().withStatusCode(HttpStatus.OK.getCode()).withBody("Pong"));

        ServerUtils serverUtils = new ServerUtils();
        assertTrue(serverUtils.testConnection("https://localhost:" + port).equals("true"));

        mockClientServer.verify(HttpRequest.request("/ping"), VerificationTimes.once());
    }

    /**
     * test case for a successful get request
     *
     * @throws Exception
     */
    @Test
    public void getRequestTest() throws Exception {
        mockClientServer.when(
                        request()
                                .withMethod("GET")
                                .withPath("/api/random")
                )

                .respond(response()
                        .withStatusCode(HttpStatus.OK.getCode()));

        ServerUtils serverUtils = new ServerUtils();

        try {
            serverUtils.getRequest("/api/random", RegisterResponsePacket.class);
        } catch (Exception e) {
            System.out.println("failed");
        }

        mockClientServer.verify(request("/api/random"), VerificationTimes.once());

    }

    /**
     * test case for a successful registration
     *
     * @throws Exception
     */
    @Test
    public void registerTest() throws Exception {
        ObjectMapper om = new ObjectMapper();
        String packet = om.writeValueAsString(new RegisterResponsePacket(HttpStatus.Created));

        mockClientServer.when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/user/register"))

                .respond(
                        response()
                                .withStatusCode(HttpStatus.OK.getCode())
                                .withBody(json(packet, MediaType.APPLICATION_JSON_UTF_8)));

        ServerUtils serverUtils = new ServerUtils();
        serverUtils.register("Joe", "password");

        mockClientServer.verify(request("/api/user/register"), VerificationTimes.once());


    }
}
