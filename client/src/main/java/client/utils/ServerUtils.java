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
import commons.utils.HttpStatus;
import commons.utils.LoggerUtil;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Response;
import packets.*;

import java.util.Objects;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private Client client;

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
     * Builds a post request
     *
     * @param path     The path of the endpoint to send the request to
     * @param request  The packet which the server returns
     * @param response The packet which to send to the server
     * @param <T>      The type of packet which the server should return
     * @param <S>      The type of packet which should be sent to the server
     * @return A packet containing the response of the server
     */
    public <T extends ResponsePacket, S extends RequestPacket> T postRequest(String path, S request, Class<T> response) {
        Invocation.Builder template = requestTemplate(path);
        if (template == null) {
            return (T) new ResponsePacket(HttpStatus.NotFound);
        }

        return template.post(Entity.entity(request, APPLICATION_JSON), response);
    }

    /**
     * Builds a get request
     *
     * @param path     The path of the endpoint to send the request to
     * @param response The packet which the server returns
     * @param <T>      The type of packet which the server should return
     * @return A packet containing the response of the server
     */
    public <T extends ResponsePacket> T getRequest(String path, Class<T> response) {
        Invocation.Builder template = requestTemplate(path);
        if (template == null) {
            return (T) new ResponsePacket(HttpStatus.NotFound);
        }

        return template.get(response);
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
}
