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

    /**
     * @param appUserService Service which keeps track of the user database
     */
    public RegistrationService(UserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * Registers a user if it does not yet exist
     *
     * @param request Request packet containing the user details of the user who should be registered
     * @return Response packet containing information if the registration was successful or not
     */
    public RegisterResponsePacket register(RegisterRequestPacket request) {
        try {
            return appUserService.signUpUser(new User(request.getUsername(), request.getPassword(), false, true));
        } catch (UserAlreadyExistsException e) {
            return new RegisterResponsePacket(HttpStatus.Conflict, e.getMessage());
        }
    }

    /**
     * Checks whether a user is available or not
     *
     * @param username The username of the user to check the availability or not
     * @return Response packet containing if a user is available or not
     */
    public ResponsePacket userAvailable(String username) {
        if (appUserService.userExists(username)) {
            return new ResponsePacket(HttpStatus.Conflict);
        } else {
            return new ResponsePacket(HttpStatus.OK);
        }
    }

    /**
     * Tries to delete a user specified by the received details. Only users themselves can delete their account.
     *
     * @param packet      Request packet containing the user details of the user who should be deleted
     * @param authorities The authorities of the user who sent the request
     * @return Response packet containing information if the deletion was successful or not
     */
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
