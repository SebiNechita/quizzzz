package server.api.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.questions.Activity;
import commons.utils.LoggerUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import packets.ActivitiesResponsePacket;
import server.database.ActivityRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {
    /**
     * the repository which will be used to store activities
     */
    private ActivityRepository activityRepository;

    /**
     * Constructor
     * @param activityRepository Repository layer for the service
     */
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Returns a list of all activities in the repository
     * @return a list of all activities in the repository
     */
    public ActivitiesResponsePacket list() {
        return new ActivitiesResponsePacket(activityRepository.findAll());
    }

    /**
     * Stores a single activity in the repository
     * @param activity The activity to store
     * @return The stored activity
     */
    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    /**
     * Stores a list of activities in the repository
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
