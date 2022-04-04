package server.api.game;

import org.springframework.web.bind.annotation.*;
import packets.ActivitiesResponsePacket;
import packets.ActivityRequestPacket;
import packets.GeneralResponsePacket;

/**
 * Controller for retrieving and updating activity
 */
@RestController
@RequestMapping("api/activities")
public class ActivityController {
    /**
     * Service layer for the ActivityController
     */
    private ActivityService activityService;

    /**
     * Constructor
     *
     * @param activityService Service layer for this controller
     */
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /**
     * Endpoint for creating game
     *
     * @return A list of activities
     */
    @GetMapping("/list")
    public ActivitiesResponsePacket list() {
        return activityService.list();
    }

    /**
     * Endpoint for editing an activity
     *
     * @param request ActivityRequestPacket
     * @return returns general response packet
     */
    @PostMapping("/edit")
    public GeneralResponsePacket edit(@RequestBody ActivityRequestPacket request) {
        return activityService.edit(request);
    }
}
