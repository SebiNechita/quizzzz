package server.user;

import commons.utils.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import packets.RegisterResponsePacket;
import server.database.AppUserRepository;
import server.exceptions.UserAlreadyExistsException;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' can not be found."));
    }

    public RegisterResponsePacket signUpUser(User appUser) throws UserAlreadyExistsException {
        if (appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));

        appUserRepository.save(appUser);

        return new RegisterResponsePacket(HttpStatus.Created);
    }

    public boolean validUser(User appUser) {
        Optional<User> user = appUserRepository.findByUsername(appUser.getUsername());

        if (user.isEmpty()) return false;

        return bCryptPasswordEncoder.matches(appUser.getPassword(), user.get().getPassword());
    }

}
