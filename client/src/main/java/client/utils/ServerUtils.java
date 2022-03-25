/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import client.Main;
import commons.Game;
import commons.questions.Activity;
import commons.utils.HttpStatus;
import commons.utils.LoggerUtil;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.scene.image.Image;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.simp.stomp.StompSession;
import packets.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {
    private static final String SERVER = "http://localhost:8080/";

    private Client client;

    public ServerUtils() {
    }

    /**
     * Creates the client if it doesn't exist yet
     */
    private void initClient() {
        client = ClientBuilder.newBuilder()
                .hostnameVerifier((hostname, session) -> true)
                .sslContext(HTTPSUtil.getSSLContext())
                .build();
    }

    /**
     * Returns the client which will send the SSL context automatically for an HTTPS connection
     *
     * @return The client
     */
    private Client getClient() {
        if (client == null) {
            initClient();
        }

        return client;
    }

    /**
     * Builds a template which can be used for HTTP(S) requests
     *
     * @param path The path of the endpoint to send the request to
     * @return The template which can be used for any type of HTTP(S) request
     */
    private Invocation.Builder requestTemplate(String path) {
        if (Objects.equals(Main.TOKEN, "")) {
            LoggerUtil.warnInline("No token set, cancelling request to $HL" + Main.URL + path + "$");
            return null;
        }

        return getClient().target(Main.URL)
                .path(path)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("Authorization", Main.TOKEN);
    }

    /**
     * Tries to get a token with the given username and password
     *
     * @param username The username for which to get the token
     * @param password The password for which to get the token
     * @return The token, {@code null} if the username and/or password is invalid
     */
    public String getToken(String username, String password) {
        Response response = getClient().target(Main.URL).path("/api/user/login")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(new RegisterRequestPacket(username, password), APPLICATION_JSON));

        if (response.getStatus() == 202) {
            return (String) response.getHeaders().get("Authorization").get(0);
        } else if (response.getStatus() == 403) {
            LoggerUtil.warn("Unable to get token, invalid account:\n\t$HLUsername: " + username + "\n\tPassword: " + "*".repeat(password.length()) + "$");
        } else {
            LoggerUtil.severeInline("Unknown status $HLHTTP" + response.getStatus() + "$ given while trying to get a token");
        }
        return null;
    }

    /**
     * Retrieves an instance of Game from the server using the endpoint made for the same
     *
     * @return Instance of Game with a list of questions
     */
    public Game getGame() {
        return getRequest("api/game/create", GameResponsePacket.class).getGame();
    }

    /**
     * Uses an endpoint to retrieve the image from the Server using the path at which it is stored
     *
     * @param imagePath the path within activity-bank to access the image
     * @return loaded Image is returned
     */
    public Image getImage(String imagePath) {
        ImageResponsePacket image = getClient().target(Main.URL)
                .path("api/activity/image")
                .queryParam("imagePath", imagePath)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("Authorization", Main.TOKEN)
                .get(ImageResponsePacket.class);

        return new Image(new ByteArrayInputStream(image.getImageByte()), 240, 143, false, false);
    }

    /**
     * Tests whether the URL is valid or not
     *
     * @param url The URL to test
     * @return If the URL is valid or not
     */
    public boolean testConnection(String url) {
        Invocation invocation = getClient()
                .target(url + "/ping")
                .request("text/plain").buildGet();

        // Invoke the request
        String response;
        try {
            response = invocation.invoke(String.class);
        } catch (ProcessingException | WebApplicationException ignored) {
            return false;
        }

        return response.equals("Pong");
    }

    /**
     * Register a new user on server
     *
     * @param username the name of the user
     * @param password the password of the user
     * @return whether the registration was successful
     */
    public ResponsePacket register(String username, String password) {
        Invocation.Builder template = getClient().target(Main.URL)
                .path("api/user/register")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("Authorization", Main.TOKEN);
        return template.post(
                Entity.entity(new RegisterRequestPacket(username, password), APPLICATION_JSON),
                RegisterResponsePacket.class);
    }

    /**
     * Uses an endpoint to retrieve the list of activities from the Server
     * @return The activities stored
     */
    public List<Activity> getActivities() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/activities/list")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});
    }

    //private StompSession session = connect("ws://localhost:8080/websocket");

    //THIS IS A TEMPORARY SOLUTION FOR MAKING THE CODE COMPILE, WILL FIX WEBSOCKETS IN NEXT SPRINT
    private StompSession session = null;

    /**
     * Builds a get request
     *
     * @param path     The path of the endpoint to send the request to
     * @param response The packet which the server returns
     * @param <T>      The type of packet which the server should return
     * @return A packet containing the response of the server
     */
    @SuppressWarnings("unchecked")
    public <T extends ResponsePacket> T getRequest(String path, Class<T> response) {
        Invocation.Builder template = requestTemplate(path);
        if (template == null) {
            return (T) new ResponsePacket(HttpStatus.NotFound);
        }

        return template.get(response);
    }

    /**
     * Builds a post request
     *
     * @param path     The path of the endpoint to send the request to
     * @param request  The packet which to send to the server
     * @param response The packet which the server returns
     * @param <T>      The type of packet which the server should return
     * @param <S>      The type of packet which should be sent to the server
     * @return A packet containing the response of the server
     */
    @SuppressWarnings("unchecked")
    public <T extends ResponsePacket, S extends RequestPacket> T postRequest(String path, S request, Class<T> response) {
        Invocation.Builder template = requestTemplate(path);
        if (template == null) {
            return (T) new ResponsePacket(HttpStatus.NotFound);
        }

        return template.post(Entity.entity(request, APPLICATION_JSON), response);
    }

    /**
     * Builds a long polling get request which can be called on demand
     *
     * @param path       The path of the endpoint to send the request to
     * @param response   The packet which the server returns
     * @param onResponse The {@code run} method inside this interface will be called as soon as the server gives a response
     * @param <T>        The type of packet which the server should return
     * @return The long polling class which can be started or stopped on demand
     */
    public <T extends ResponsePacket> LongPollingRequest<T> longGetRequest(String path, Class<T> response, ServerResponse<T> onResponse) {
        LongPollingRequest<T> longPollingRequest = new ServerUtils.LongPollingRequest<>(path, response);
        longPollingRequest.setCallback(onResponse);
        return longPollingRequest;
    }

    /**
     * Builds a long polling post request which can be called on demand
     *
     * @param path       The path of the endpoint to send the request to
     * @param request    The packet which to send to the server
     * @param response   The packet which the server returns
     * @param onResponse The {@code run} method inside this interface will be called as soon as the server gives a response
     * @param <T>        The type of packet which the server should return
     * @param <S>        The type of packet which should be sent to the server
     * @return The long polling class which can be started or stopped on demand
     */
    public <T extends ResponsePacket, S extends RequestPacket> LongPollingRequest<T> longPostRequest(String path, S request, Class<T> response, ServerResponse<T> onResponse) {
        LongPollingRequest<T> longPollingRequest = new ServerUtils.LongPollingRequest<>(path, response);
        longPollingRequest.setCallback(onResponse);
        return longPollingRequest;
    }

    /**
     * Class which  handles a long polling request
     *
     * @param <T> The type of packet which the server should return
     */
    public class LongPollingRequest<T extends ResponsePacket> {
        private final String path;
        private final Class<T> response;

        private ServerResponse<T> onResponse;

        private Thread thread = null;
        private boolean interrupted = false;
        private boolean persistent = false;

        /**
         * @param path     The path of the request
         * @param response The response class of the packet
         */
        public LongPollingRequest(String path, Class<T> response) {
            this.path = path;
            this.response = response;
        }

        /**
         * Sets the callback for this long polling request
         *
         * @param onResponse The callback interface instance
         */
        public void setCallback(ServerResponse<T> onResponse) {
            this.onResponse = onResponse;
        }

        /**
         * Set whether the long polling should be persistent or not; if it should open a
         * new request as soon as the server has handled one.
         *
         * @param persistent If it should be persistent or not
         */
        public void setPersistent(boolean persistent) {
            this.persistent = persistent;
        }

        /**
         * Constructs and calls a long polling get request
         */
        public void getRequest() {
            Invocation.Builder template = requestTemplate(path);
            if (template == null) {
                return;
            }

            Invocation invocation = template.buildGet();
            poll(invocation);
        }

        /**
         * Constructs and calls a long polling post request
         *
         * @param request The packet which to send to the server
         * @param <S>     The type of packet which should be sent to the server
         */
        public <S extends RequestPacket> void postRequest(S request) {
            Invocation.Builder template = requestTemplate(path);
            if (template == null) {
                return;
            }

            Invocation invocation = template.buildPost(Entity.entity(request, APPLICATION_JSON));
            poll(invocation);
        }

        /**
         * Starts a long polling request
         *
         * @param invocation The invocation to instantiate
         */
        private void poll(Invocation invocation) {
            thread = new Thread(() -> callback(invocation.invoke(response), invocation));
            interrupted = false;
            thread.start();
        }

        /**
         * Used to call the {@code run} method of the callback interface
         *
         * @param packet The packet the server returned
         * @param source The source invocation which got called
         */
        private void callback(T packet, Invocation source) {
            if (!interrupted) {
                if (onResponse != null) {
                    onResponse.run(packet);
                }

                if (persistent) {
                    poll(source);
                }
            }
        }

        /**
         * Stops the long polling request
         */
        public void stop() {
            if (thread != null) {
                thread.interrupt();
            }

            interrupted = true;
        }
    }

    /**
     * Callback for {@link #longGetRequest(String, Class, ServerResponse)} and {@link #longPostRequest(String, RequestPacket, Class, ServerResponse)}.
     * Contains a {@code run} method which will be called when the server responds to data to a long polling request.
     *
     * @param <T> The type of packet which the server should return
     */
    public interface ServerResponse<T extends ResponsePacket> {
        /**
         * Will be called when the server response with data to a long polling request
         *
         * @param responsePacket The packet which the server returned
         */
        void run(T responsePacket);
    }


}
