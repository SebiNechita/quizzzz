package server.api.game;

import commons.questions.Activity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param activity The activity to be added
     */
    @PostMapping("/add")
    public void add(Activity activity) {
        addActivityService.add(activity);
    }
}
