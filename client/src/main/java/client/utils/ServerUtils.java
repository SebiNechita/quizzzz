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
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

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

    /**
     * Example request which checks if the server is online
     *
     * @return The response of the server
     */
    public String pingServer() {
        return getClient().target(Main.URL)
                .path("ping")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);
    }
}