package server.api.registration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import packets.*;

@RestController
@RequestMapping(path = "api/user")
public class UserRegistrationController {

    private final RegistrationService registrationService;

    /**
     * @param registrationService The service which handles registration/deletion of users
     */
    public UserRegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Registers a user if it does not yet exist
     *
     * @param request Request packet containing the user details of the user who should be registered
     * @return Response packet containing information if the registration was successful or not
     */
    @PostMapping(path = "/register")
    public RegisterResponsePacket register(@RequestBody RegisterRequestPacket request) {
        return registrationService.register(request);
    }

    /**
     * Deletes a user if it is available
     *
     * @param request Request packet containing the user details of the user who should be deleted
     * @return Response packet containing information if the deletion was successful or not
     */
    @DeleteMapping(path = "/delete")
    public DeleteResponsePacket deleteUserByUser(@RequestBody DeleteRequestPacket request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return registrationService.deleteUser(request, auth.getAuthorities());
    }

    /**
     * Checks whether a user is available or not
     *
     * @param request The request packet containing the user details
     * @return Response packet containing if a user is available or not
     */
    @PostMapping(path = "/available")
    public ResponsePacket available(@RequestBody UsernameAvailableRequestPacket request) {
        return registrationService.userAvailable(request.getUsername());
    }
}
