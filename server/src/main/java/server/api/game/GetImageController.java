package server.api.game;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/activity/image")
public class GetImageController {

    private final GetImageService imageService;

    public GetImageController(GetImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(
            path = "",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImage(@RequestParam(value = "imagePath", required = true) String imagePath) {
        return imageService.getImage(imagePath);
    }
}
