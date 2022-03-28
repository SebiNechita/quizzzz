package server.jwt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernamePasswordAuthenticationRequestTest {

    @Test
    void username() {
        UsernamePasswordAuthenticationRequest authReq = new UsernamePasswordAuthenticationRequest();
        authReq.setUsername("Geoff");
        assertEquals("Geoff", authReq.getUsername());
    }

    @Test
    void password() {
        UsernamePasswordAuthenticationRequest authReq = new UsernamePasswordAuthenticationRequest();
        authReq.setPassword("password");
        assertEquals("password", authReq.getPassword());
    }
}
