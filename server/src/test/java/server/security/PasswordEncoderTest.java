package server.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PasswordEncoderTest {
    private static PasswordEncoder passwordEncoder;

    @BeforeAll
    public static void init() {
        passwordEncoder = new PasswordEncoder();
    }


    @Test
    public void encoderTest() {
        assertNotNull(passwordEncoder.bCryptPasswordEncoder());
    }
}
