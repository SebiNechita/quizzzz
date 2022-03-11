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
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Response;
import packets.RegisterRequestPacket;
import packets.RequestPacket;
import packets.ResponsePacket;

import java.util.Objects;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private void initClient() {
        client = ClientBuilder.newBuilder()
                .hostnameVerifier((hostname, session) -> true)
                .sslContext(HTTPSUtil.getSSLContext())
                .build();
    }

    private Client client;

    private Client getClient() {
        if (client == null) {
            initClient();
        }

        return client;
    }

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
        return "";
    }

    public <T extends ResponsePacket, S extends RequestPacket> T postRequest(String path, S request, Class<T> response) {
        Invocation.Builder template = requestTemplate(path);
        if (template == null) {
            return (T) new ResponsePacket(HttpStatus.NotFound);
        }

        return template.post(Entity.entity(request, APPLICATION_JSON), response);
    }

    public <T extends ResponsePacket> T getRequest(String path, Class<T> response) {
        Invocation.Builder template = requestTemplate(path);
        if (template == null) {
            return (T) new ResponsePacket(HttpStatus.NotFound);
        }

        return template.get(response);
    }

    /**
     * test the connection with the given url
     * @param url
     * @return returns true if given url is valid
     */
    public boolean testConnection(String url) {
        Client client = getClient();
        WebTarget resourceTarget = client.target(url + "/ping");

        Invocation invocation = resourceTarget.request("text/plain")
                .buildGet();

        // Invoke the request
        String response;
        try {
            response = invocation.invoke(String.class);
        } catch (Exception e) {
            return false;
        }

        return response.equals("Pong");
    }

}
