package server.api.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.questions.Activity;
import commons.utils.HttpStatus;
import commons.utils.LoggerUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import packets.ActivitiesResponsePacket;
import packets.ActivityRequestPacket;
import packets.GeneralResponsePacket;
import server.database.ActivityRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * services related to retrieving activity and updating activity
 */
@Service
public class ActivityService {
    /**
     * the repository which will be used to store activities
     */
    private ActivityRepository activityRepository;

    /**
     * Constructor
     *
     * @param activityRepository Repository layer for the service
     */
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Returns a list of all activities in the repository
     *
     * @return a list of all activities in the repository
     */
    public ActivitiesResponsePacket list() {
        return new ActivitiesResponsePacket(activityRepository.findAll());
    }

    /**
     * Stores a single activity in the repository
     *
     * @param activity The activity to store
     * @return The stored activity
     */
    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    /**
     * Stores a list of activities in the repository
     *
     * @param activities The list of activities to store
     * @return An iterable collection of stored activities
     */
    public Iterable<Activity> save(List<Activity> activities) {
        List<Activity> savedActivities = new ArrayList<>();
        for (Activity activity : activities) {
            try {
                savedActivities.add(activityRepository.saveAndFlush(activity));
            } catch (DataIntegrityViolationException e) {
                LoggerUtil.warnInline("Failed to save: \n\n" + activity);
            }
        }
        return savedActivities;
    }

    /**
     * retrieve corresponding activity by ID and update it.
     *
     * @param request ActivityRequestPacket
     * @return returns OK if operation is successful. returns 404 if the entity doesn't exist.
     */
    public GeneralResponsePacket edit(ActivityRequestPacket request) {

        Optional<Activity> optional = activityRepository.findById(request.getId());
        if (!optional.isPresent()) {
            return new GeneralResponsePacket(HttpStatus.NotFound);
        }

        Activity entity = optional.get();
        // update fields
        entity.setTitle(request.getDescription());
        entity.setConsumptionInWH(request.getConsumption());
        entity.setSource(request.getSource());
        //  overwrite image
        if (request.getImageByte() != null) {
            String path = "extras/" + request.getId() + ".png";
            writeImage(request.getImageByte(), path);
        }
        activityRepository.save(entity);

        return new GeneralResponsePacket(HttpStatus.OK);
    }

    /**
     * overwrite existing image with new one
     *
     * @param imageByteArray image byte array
     * @param path           image path
     */
    public void writeImage(byte[] imageByteArray, String path) {
        try {
            BufferedImage myImage = ImageIO.read(new ByteArrayInputStream(imageByteArray));

            URL activityBankPath = Objects.requireNonNull(getClass().getResource("/activity-bank"));
            URL filePath = new URL(activityBankPath.getProtocol(), activityBankPath.getHost(), activityBankPath.getPort(),
                    activityBankPath.getPath() + "/" + path, null);

            File createdFile = new File(filePath.toURI());

            if (ImageIO.write(myImage, "png", createdFile)) {
                LoggerUtil.infoInline("Successfully Stored Image at: " + filePath);
            } else {
                LoggerUtil.warnInline("Image could not be stored!");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }



    /**
     * Clears the repository and adds every activity from activities.json to the repository
     */
    public void updateRepository(){
        activityRepository.deleteAll();
        // read json and write to db
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<List<Activity>> typeReference = new TypeReference<List<Activity>>() {};

        InputStream inputStream = TypeReference.class.getResourceAsStream("/activity-bank/activities.json");

        if (inputStream == null) {
            LoggerUtil.warnInline("The file '/activity-bank/activities.json' does not exist in the resources directory!" +
                    "\nThe reason could be that it is included in .gitignore and hence not available in the remote repository");
            return;
        }

        try {
            List<Activity> activities = mapper.readValue(inputStream, typeReference);
            save(activities);
            LoggerUtil.infoInline("Activities Saved!");
        } catch (IOException e) {
            LoggerUtil.warnInline("Unable to save activities: " + e.getMessage());
        }
    }
}
