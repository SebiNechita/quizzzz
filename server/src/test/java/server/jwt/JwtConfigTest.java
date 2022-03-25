package server.jwt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtConfigTest {

    private static JwtConfig jwtConfig;

    @BeforeAll
    public static void init() {
        jwtConfig = new JwtConfig();
    }

    @Test
    void setKey() {
        String key = "test123";
        jwtConfig.setKey(key);
        assertEquals(key, jwtConfig.getKey());
    }

    @Test
    void setTokenPrefix() {
        String prefix = "Bearer ";
        jwtConfig.setTokenPrefix(prefix);
        assertEquals(prefix, jwtConfig.getTokenPrefix());
    }

    @Test
    void setTokenExpirationAfterDays() {
        int days = 31415;
        jwtConfig.setTokenExpirationAfterDays(days);
        assertEquals(days, jwtConfig.getTokenExpirationAfterDays());
    }
}
