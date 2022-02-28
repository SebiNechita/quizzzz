package server.api.registration;

import commons.utils.LoggerUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packets.RegisterRequestPacket;
import packets.RegisterResponsePacket;

@RestController
@RequestMapping(path = "api/registration")
public class UserRegistrationController {

    private final RegistrationService registrationService;

    public UserRegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public RegisterResponsePacket register(@RequestBody RegisterRequestPacket request) {
        LoggerUtil.log(request);
        return registrationService.register(request);
    }
}
