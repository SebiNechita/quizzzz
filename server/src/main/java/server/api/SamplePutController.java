package server.api;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;
import server.user.User;

@RestController
@RequestMapping(path = "/api/update")
public class SamplePutController {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SamplePutController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Creates a new user if a user with the given name is not found. Updates the password if it already exists.
     *
     * @param newUser
     * @return returns the updated user
     */
    @PutMapping()
    User replaceUser(@RequestBody User newUser) {
        return userRepository.findByUsername(newUser.getUsername())
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    var u = new User(newUser.getUsername(), bCryptPasswordEncoder.encode(newUser.getPassword()), false, true);
                    return userRepository.save(u);
                });
    }

}
