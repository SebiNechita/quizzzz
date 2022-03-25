package server.user;

import commons.utils.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import packets.RegisterResponsePacket;
import server.database.UserRepository;
import server.exceptions.UserAlreadyExistsException;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * @param userRepository The repository which to store the users in
     * @param bCryptPasswordEncoder The password encoder
     */
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' can not be found."));
    }

    /**
     * Checks if a user by username already exists or not
     *
     * @param username The username of the user
     * @return If the user already exists or not
     */
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Signs up a user
     *
     * @param user The user to sign up
     * @return A packet containing the result
     * @throws UserAlreadyExistsException Thrown if the user already exists
     */
    public RegisterResponsePacket signUpUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return new RegisterResponsePacket(HttpStatus.Created);
    }

    /**
     * Checks if the give username and password are a valid combination
     *
     * @param username The username
     * @param password The password
     * @return If they are valid or not
     */
    public boolean validUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) return false;

        return bCryptPasswordEncoder.matches(password, user.get().getPassword());
    }

    /**
     * Deletes a user from the repository
     *
     * @param user The user to delete
     */
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
