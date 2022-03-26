package server.api.game;

import commons.questions.Activity;
import commons.utils.LoggerUtil;
import org.springframework.stereotype.Service;
import packets.ActivityRequestPacket;
import server.database.ActivityRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

@Service
public class AddActivityService {

    private ActivityRepository activityRepository;

    /**
     * Constructor
     * @param activityRepository Repository layer for this service
     */
    public AddActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Adds a new activity to the repository
     * @param packet The activity to be stored
     */
    public void add(ActivityRequestPacket packet) {
        String path = "extras/" + packet.getId() + ".png";
        Activity activity = new Activity(
                packet.getDescription(),
                packet.getId(),
                path,
                packet.getConsumption(),
                packet.getSource()
        );
        try {
            BufferedImage myImage = ImageIO.read(new ByteArrayInputStream(packet.getImageByte()));
            Path filePath = Path.of(Objects.requireNonNull(getClass().getResource("/activity-bank")).toExternalForm().substring(6) + "/" + path);
            File createdFile = new File(filePath.toUri());
            if (createdFile.mkdirs() && ImageIO.write(myImage, "png", createdFile)) {
                LoggerUtil.infoInline("Successfully Stored Image at: " + filePath);
            } else {
                LoggerUtil.warnInline("Image could not be stored!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        activityRepository.save(activity);
    }
}
