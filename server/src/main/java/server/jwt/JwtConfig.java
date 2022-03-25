package server.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String key;
    private String tokenPrefix;
    private int tokenExpirationAfterDays;

    /**
     * Generates a secret key used for Jwt encryption
     *
     * @return The secret key
     */
    @Bean
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    /**
     * Getter for the key
     *
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * Setter for the key
     *
     * @param key The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Getter for the token prefix
     *
     * @return The token prefix
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    /**
     * Setter for the token prefix
     *
     * @param tokenPrefix The token prefix
     */
    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    /**
     * Getter for the amount of days it takes for a token to expire
     *
     * @return The amount of days
     */
    public int getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    /**
     * Setter for the amount of days it takes for a token to expire
     *
     * @param tokenExpirationAfterDays The amount of days
     */
    public void setTokenExpirationAfterDays(int tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }
}
