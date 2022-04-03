package server.api.game;

import commons.utils.HttpStatus;
import org.springframework.web.bind.annotation.*;
import packets.ActivityRequestPacket;
import packets.GeneralResponsePacket;

@RestController
@RequestMapping("api/activities")
public class DeleteActivityController {
    private DeleteActivityService deleteActivityService;

    /**
     * Constructor
     *
     * @param deleteActivityService Service layer for this controller
     */
    public DeleteActivityController(DeleteActivityService deleteActivityService) {
        this.deleteActivityService = deleteActivityService;
    }

    /**
     * Endpoint for deleting an activity
     *
     * @param packet The activity to be deleted
     * @return A response packet with HTTP Status Found, if delete was successful; HTTP Status Not Found otherwise
     */
    @PostMapping("/delete")
    public GeneralResponsePacket delete(@RequestBody ActivityRequestPacket packet) {
        if (!deleteActivityService.delete(packet.getId())) {
            return new GeneralResponsePacket(HttpStatus.NotFound);
        } else {
            return new GeneralResponsePacket(HttpStatus.Found);
        }
    }
}
