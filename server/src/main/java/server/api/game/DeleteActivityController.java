package server.api.game;

import org.springframework.web.bind.annotation.*;
import packets.ActivityRequestPacket;

@RestController
@RequestMapping("api/activities")
public class DeleteActivityController {
    private DeleteActivityService deleteActivityService;

    /**
     * Constructor
     * @param deleteActivityService Service layer for this controller
     */
    public DeleteActivityController(DeleteActivityService deleteActivityService) {
        this.deleteActivityService = deleteActivityService;
    }

    /**
     * Endpoint for deleting an activity
     * @param packet The activity to be deleted
     */
    @PostMapping("/delete")
    public void delete(@RequestBody ActivityRequestPacket packet) {
        deleteActivityService.delete(packet.getId());
    }
}
