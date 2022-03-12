package server.api.registration;

import commons.utils.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import packets.*;
import server.exceptions.UserAlreadyExistsException;
import server.user.User;
import server.user.UserService;

import java.util.Collection;

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

    public ResponsePacket userAvailable(String username) {
        if (appUserService.userExists(username)) {
            return new ResponsePacket(HttpStatus.Conflict);
        } else {
            return new ResponsePacket(HttpStatus.OK);
        }
    }

    public DeleteResponsePacket deleteUser(DeleteRequestPacket packet, Collection<? extends GrantedAuthority> authorities) {
        if (appUserService.userExists(packet.getUsername())) {
            if (appUserService.validUser(packet.getUsername(), packet.getPassword()) || authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
                User user = (User) appUserService.loadUserByUsername(packet.getUsername());
                appUserService.deleteUser(user);
                return new DeleteResponsePacket(HttpStatus.Accepted, "User has been deleted");
            } else {
                return new DeleteResponsePacket(HttpStatus.Forbidden, "Incorrect Password. Cannot delete user");
            }
        } else {
            return new DeleteResponsePacket(HttpStatus.NotFound, "No user with username: " + packet.getUsername());
        }
    }
}
