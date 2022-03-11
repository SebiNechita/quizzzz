package server.api.registration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import packets.*;

@RestController
@RequestMapping(path = "api/user")
public class UserRegistrationController {

    private final RegistrationService registrationService;

    public UserRegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(path = "/register")
    public RegisterResponsePacket register(@RequestBody RegisterRequestPacket request) {
        return registrationService.register(request);
    }

    @DeleteMapping(path = "/delete")
    public DeleteResponsePacket deleteUserByUser(@RequestBody DeleteRequestPacket request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return registrationService.deleteUser(request, auth.getAuthorities());
    }

    @PostMapping(path = "/available")
    public GeneralResponsePacket available(@RequestBody UsernameAvailableRequestPacket request) {
        return registrationService.userAvailable(request.getUsername());
    }
}
