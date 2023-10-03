package et.nate.poll.user;

import et.nate.poll.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    Page<User> getAllUsers() {
        var paging = PageRequest.of(0, 3);
        return userRepository.findAll(paging);
    }

    @GetMapping("/email/{email}")
    Optional<User> getByEmail(@PathVariable String email) {
        return Optional.of(userRepository.findByEmail(email));
    }

    @GetMapping("/exist/{id}")
    Optional<Boolean> existsById(@PathVariable Long id) {
        return Optional.of(userRepository.existsUserById(id));
    }

    @GetMapping("/exist/email/{email}")
    Optional<Boolean> existsByEmail(@PathVariable String email) {
        return Optional.of(userRepository.existsByEmail(email));
    }
}
