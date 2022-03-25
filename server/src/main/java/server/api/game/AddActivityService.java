package server.api.game;

import commons.questions.Activity;
import org.springframework.stereotype.Service;
import server.database.ActivityRepository;

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
     * @param activity The activity to be stored
     */
    public void add(Activity activity) {
        activityRepository.save(activity);
    }
}
