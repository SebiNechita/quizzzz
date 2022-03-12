package server.api.game;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class GetImageService {

    /**
     * Service layer for the getImagerController. It retrieves the image from the activity-bank using the image path
     * @param imagePath path of the image within the activity-bank
     * @return The byte array of the image
     */
    public byte[] getImage(String imagePath) {
        try {
            String path = "/activity-bank/" + imagePath;
            return Objects.requireNonNull(getClass().getResourceAsStream(path)).readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
