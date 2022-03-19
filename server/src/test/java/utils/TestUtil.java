package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * maps a object into JSON
 */
public class TestUtil {
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
