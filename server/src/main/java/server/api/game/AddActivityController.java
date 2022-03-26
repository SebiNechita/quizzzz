package server.api.game;

import org.springframework.web.bind.annotation.*;
import packets.ActivityRequestPacket;

@RestController
@RequestMapping("api/activities")
public class AddActivityController {
    private AddActivityService addActivityService;

    /**
     * Constructor
     * @param addActivityService Service layer for this controller
     */
    public AddActivityController(AddActivityService addActivityService) {
        this.addActivityService = addActivityService;
    }

    /**
     * Endpoint for adding an activity
     * @param packet The activity to be added
     */
    @PostMapping("/add")
    public void add(@RequestBody ActivityRequestPacket packet) {
        addActivityService.add(packet);
    }
}
