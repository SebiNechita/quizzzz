package client.utils;

import commons.utils.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;
import org.mockserver.socket.tls.KeyStoreFactory;
import org.mockserver.verify.VerificationTimes;

import javax.net.ssl.HttpsURLConnection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class ServerUtilsTest {
    private static ClientAndServer mockClientServer;
    private static int port = -1;

    @BeforeAll
    public static void startMockServer() {
        HttpsURLConnection.setDefaultSSLSocketFactory(new KeyStoreFactory(new MockServerLogger()).sslContext().getSocketFactory());
        port = PortFactory.findFreePort();
        mockClientServer = startClientAndServer(port);
    }

    @AfterAll
    public static void stopMockServer() {
        mockClientServer.stop();
    }

    @Test
    public void testConnectionTest() {
        mockClientServer.when(new HttpRequest().withMethod("GET").withPath("/ping"))
                .respond(new HttpResponse().withStatusCode(HttpStatus.OK.getCode()).withBody("Pong"));

        ServerUtils serverUtils = new ServerUtils();
        assertTrue(serverUtils.testConnection("https://localhost:" + port));

        mockClientServer.verify(HttpRequest.request("/ping"), VerificationTimes.once());
    }
}
