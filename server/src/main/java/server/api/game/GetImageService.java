package server.api.game;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class GetImageService {

    public byte[] getImage(String imagePath) {
        try {
            String path = "/activity-bank/" + imagePath;
            return Objects.requireNonNull(
                    getClass().getResourceAsStream(path)
            )
                    .readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
