package server.api.registration;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packets.GeneralResponsePacket;
import packets.RegisterRequestPacket;
import packets.RegisterResponsePacket;
import packets.UsernameExistsRequestPacket;

@RestController
@RequestMapping(path = "api/registration")
public class UserRegistrationController {

    private final RegistrationService registrationService;

    public UserRegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public RegisterResponsePacket register(@RequestBody RegisterRequestPacket request) {
        return registrationService.register(request);
    }

    @PostMapping(path = "/usernameAvailable")
    public GeneralResponsePacket register(@RequestBody UsernameExistsRequestPacket request) {
        return registrationService.userAvailable(request.getUsername());
    }
}
