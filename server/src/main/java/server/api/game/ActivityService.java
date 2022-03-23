package server.api.game;

import commons.questions.Activity;
import org.springframework.stereotype.Service;
import server.database.ActivityRepository;

import java.util.List;

@Service
public class ActivityService {
    private ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Iterable<Activity> list() {
        return activityRepository.findAll();
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public Iterable<Activity> save(List<Activity> activities) {
        return activityRepository.saveAll(activities);
    }
}
