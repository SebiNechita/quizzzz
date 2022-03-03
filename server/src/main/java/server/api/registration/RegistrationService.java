package server.api.registration;

import commons.utils.HttpStatus;
import org.springframework.stereotype.Service;
import packets.GeneralResponsePacket;
import packets.RegisterRequestPacket;
import packets.RegisterResponsePacket;
import server.exceptions.UserAlreadyExistsException;
import server.user.User;
import server.user.UserService;

@Service
public class RegistrationService {

    private final UserService appUserService;

    public RegistrationService(UserService appUserService) {
        this.appUserService = appUserService;
    }

    public RegisterResponsePacket register(RegisterRequestPacket request) {
        try {
            return appUserService.signUpUser(new User(request.getUsername(), request.getPassword(), false, true));
        } catch (UserAlreadyExistsException e) {
            return new RegisterResponsePacket(HttpStatus.Conflict, e.getMessage());
        }
    }

    public GeneralResponsePacket userAvailable(String username) {
        if (!appUserService.userExists(username)) {
            return new GeneralResponsePacket(HttpStatus.OK);
        } else {
            return new GeneralResponsePacket(HttpStatus.NotFound);
        }
    }
}
