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
import commons.utils.LoggerUtil;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import packets.RegisterRequestPacket;

import java.util.Locale;
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
            return new Invocation.Builder() {
                @Override
                public Invocation build(String method) {
                    return null;
                }

                @Override
                public Invocation build(String method, Entity<?> entity) {
                    return null;
                }

                @Override
                public Invocation buildGet() {
                    return null;
                }

                @Override
                public Invocation buildDelete() {
                    return null;
                }

                @Override
                public Invocation buildPost(Entity<?> entity) {
                    return null;
                }

                @Override
                public Invocation buildPut(Entity<?> entity) {
                    return null;
                }

                @Override
                public AsyncInvoker async() {
                    return null;
                }

                @Override
                public Invocation.Builder accept(String... mediaTypes) {
                    return null;
                }

                @Override
                public Invocation.Builder accept(MediaType... mediaTypes) {
                    return null;
                }

                @Override
                public Invocation.Builder acceptLanguage(Locale... locales) {
                    return null;
                }

                @Override
                public Invocation.Builder acceptLanguage(String... locales) {
                    return null;
                }

                @Override
                public Invocation.Builder acceptEncoding(String... encodings) {
                    return null;
                }

                @Override
                public Invocation.Builder cookie(Cookie cookie) {
                    return null;
                }

                @Override
                public Invocation.Builder cookie(String name, String value) {
                    return null;
                }

                @Override
                public Invocation.Builder cacheControl(CacheControl cacheControl) {
                    return null;
                }

                @Override
                public Invocation.Builder header(String name, Object value) {
                    return null;
                }

                @Override
                public Invocation.Builder headers(MultivaluedMap<String, Object> headers) {
                    return null;
                }

                @Override
                public Invocation.Builder property(String name, Object value) {
                    return null;
                }

                @Override
                public CompletionStageRxInvoker rx() {
                    return null;
                }

                @Override
                public <T extends RxInvoker> T rx(Class<T> clazz) {
                    return null;
                }

                @Override
                public Response get() {
                    return null;
                }

                @Override
                public <T> T get(Class<T> responseType) {
                    return null;
                }

                @Override
                public <T> T get(GenericType<T> responseType) {
                    return null;
                }

                @Override
                public Response put(Entity<?> entity) {
                    return null;
                }

                @Override
                public <T> T put(Entity<?> entity, Class<T> responseType) {
                    return null;
                }

                @Override
                public <T> T put(Entity<?> entity, GenericType<T> responseType) {
                    return null;
                }

                @Override
                public Response post(Entity<?> entity) {
                    return null;
                }

                @Override
                public <T> T post(Entity<?> entity, Class<T> responseType) {
                    return null;
                }

                @Override
                public <T> T post(Entity<?> entity, GenericType<T> responseType) {
                    return null;
                }

                @Override
                public Response delete() {
                    return null;
                }

                @Override
                public <T> T delete(Class<T> responseType) {
                    return null;
                }

                @Override
                public <T> T delete(GenericType<T> responseType) {
                    return null;
                }

                @Override
                public Response head() {
                    return null;
                }

                @Override
                public Response options() {
                    return null;
                }

                @Override
                public <T> T options(Class<T> responseType) {
                    return null;
                }

                @Override
                public <T> T options(GenericType<T> responseType) {
                    return null;
                }

                @Override
                public Response trace() {
                    return null;
                }

                @Override
                public <T> T trace(Class<T> responseType) {
                    return null;
                }

                @Override
                public <T> T trace(GenericType<T> responseType) {
                    return null;
                }

                @Override
                public Response method(String name) {
                    return null;
                }

                @Override
                public <T> T method(String name, Class<T> responseType) {
                    return null;
                }

                @Override
                public <T> T method(String name, GenericType<T> responseType) {
                    return null;
                }

                @Override
                public Response method(String name, Entity<?> entity) {
                    return null;
                }

                @Override
                public <T> T method(String name, Entity<?> entity, Class<T> responseType) {
                    return null;
                }

                @Override
                public <T> T method(String name, Entity<?> entity, GenericType<T> responseType) {
                    return null;
                }
            };
        }

        return getClient().target(Main.URL)
                .path(path)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("Authorization", Main.TOKEN);
    }

    public String getToken(String username, String password) {
        Response response = getClient().target(Main.URL).path("login")
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

    /**
     * Example request which checks if the server is online
     *
     * @return The response of the server
     */
    public String pingServer() {
        return requestTemplate("ping").get(String.class);
    }

    /**
     * Register a new user on server
     * @param username the name of the user
     * @param password the password of the user
     */
    public void register(String username, String password){
        Response response = getClient().target(Main.URL).path("api/registration")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(new RegisterRequestPacket(username, password), APPLICATION_JSON));

        if (response.getStatus() != 200) {
            LoggerUtil.warn("Could not create user");
        }
        else{
            LoggerUtil.warn("Created user");
        }
    }
}