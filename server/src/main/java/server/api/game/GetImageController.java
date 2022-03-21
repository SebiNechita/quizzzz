package server.api.game;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import packets.ImageResponsePacket;

@RestController
@RequestMapping(path = "api/activity/image")
public class GetImageController {

    /**
     * Service layer for the get image controller
     */
    private final GetImageService imageService;

    /**
     * Constructor
     * @param imageService Service layer for getImageController
     */
    public GetImageController(GetImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * A Get endpoint for getting the image using the image_path
     * @param imagePath path of the image withing activity-bank
     * @return The byte array of the image is returned
     */
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ImageResponsePacket getImage(@RequestParam(value = "imagePath", required = true) String imagePath) {
        return imageService.getImage(imagePath);
    }
}
