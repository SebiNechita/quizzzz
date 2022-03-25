package server.api.game;

import commons.questions.Activity;
import org.springframework.stereotype.Service;
import server.database.ActivityRepository;

@Service
public class AddActivityService {
    private ActivityRepository activityRepository;

    public AddActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void add(Activity activity) {
        activityRepository.save(activity);
    }
}
