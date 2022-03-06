package server.api;

import commons.utils.HttpStatus;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packets.DeleteResponsePacket;
import server.database.UserRepository;
import server.user.User;



/**
 * This method is used to delete a user from the repository if it exists
 */

@RestController
@RequestMapping(path = "api/users/delete")
public class DeleteUserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DeleteUserController(UserRepository userRepository,
                                BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @DeleteMapping()
    public DeleteResponsePacket deleteUserByUser(@RequestBody User user) {

        Optional<User> deleteUser =
                userRepository.findByUsername(user.getUsername());

        if (deleteUser.isPresent()) {

            boolean isCorrectPassword = bCryptPasswordEncoder.matches(
                    user.getPassword(), deleteUser.get().getPassword()
            );

            if (isCorrectPassword) {
                userRepository.delete(deleteUser.get());
                return new DeleteResponsePacket(
                        HttpStatus.Accepted,
                        "User has been deleted!"
                );
            } else {
                return new DeleteResponsePacket(
                        HttpStatus.Forbidden,
                        "Incorrect Password. Cannot delete user!"
                );
            }

        } else {
            return new DeleteResponsePacket(
                    HttpStatus.NotFound,
                    "No user with username: " + user.getUsername()
            );
        }
    }
}
