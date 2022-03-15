package client.utils;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpClassCallback.callback;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mock.Expectation;
import org.mockserver.model.RequestDefinition;

public class ServerUtilsTest {
    private static ClientAndServer mockServer;

    @BeforeAll
    public static void startMockServer() {
        mockServer = startClientAndServer(8080);


        Expectation[] expectations = new MockServerClient("localhost", 8080)
                .when(

                        request()
                                .withMethod("GET")
                                .withPath("/ping")
                )
                .respond(
                        response()
                                //.withStatusCode(401)
                                .withBody("Pong")
                );

        System.out.println(expectations);

        RequestDefinition[] requestDefinitions = new MockServerClient("localhost", 8080)
                .retrieveRecordedRequests(
                        request()
                                .withMethod("GET")
                );

        System.out.println(requestDefinitions);
    }


    @AfterAll
    public static void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void testConnectionTest(){
        ServerUtils serverUtils = new ServerUtils();
        assertEquals(true, serverUtils.testConnection("https://localhost:8080"));

    }
}
