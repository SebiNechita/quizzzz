package server.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path =  "/api/client")
public class SamplePostController {

    @PostMapping()
    public void readClientData(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "message", required = false) String message) {
        System.out.println(name + ": " + message);
    }
}
