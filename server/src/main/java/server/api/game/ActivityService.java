package server.api.game;

import commons.questions.Activity;
import commons.utils.LoggerUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import packets.ActivitiesResponsePacket;
import server.database.ActivityRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {
    /**
     * the repository which will be used to stoore activities
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
}
