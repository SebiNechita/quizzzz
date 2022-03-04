package server.api;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.UserRepository;
import server.user.User;


/**
 * This method is used to delete a user from the repository if it exists
 */

@RestController
@RequestMapping(path = "api/users/delete")
public class DeleteUserController {

    @Autowired
    private final UserRepository userRepository;

    public DeleteUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @DeleteMapping()
    public boolean deleteUserByUser(@RequestBody User user) {

        Optional<User> deleteUser =
                userRepository.findByUsername(user.getUsername());

        if (deleteUser.isPresent()) {
            userRepository.delete(deleteUser.get());
            return true;
        } else {
            return false;
        }
    }

}
