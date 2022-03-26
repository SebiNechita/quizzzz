package server.api.game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packets.ActivitiesResponsePacket;

@RestController
@RequestMapping("api/activities")
public class ActivityController {
    /**
     * Service layer for the ActivityController
     */
    private ActivityService activityService;

    /**
     * Constructor
     * @param activityService Service layer for this controller
     */
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * Endpoint for creating game
     * @return A list of activities
     */
    @GetMapping("/list")
    public ActivitiesResponsePacket list() {
        return activityService.list();
    }
}
