package server.api.game;

import commons.questions.Activity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/activities")
public class AddActivityController {
    private AddActivityService addActivityService;

    public AddActivityController(AddActivityService addActivityService) {
        this.addActivityService = addActivityService;
    }

    @GetMapping("/add")
    public void add(Activity activity) {
        addActivityService.add(activity);
    }
}
