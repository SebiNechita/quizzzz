package server.api.game;

import org.springframework.stereotype.Service;
import server.database.ActivityRepository;

@Service
public class DeleteActivityService {
    private ActivityRepository activityRepository;

    /**
     * Constructor
     * @param activityRepository Repository layer for this service
     */
    public DeleteActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Deletes an activity from the repository
     * @param id The id of the activity to be deleted
     */
    public void delete(String id) {
//        List<Activity> activities = activityRepository.findAll();
//        for(Activity activity : activities) {
//            if(activity.getId().equals(id)) {
//                activityRepository.delete(activity);
//            }
//        }
        // I feel this way is easier
        activityRepository.deleteById(id);
    }
}
