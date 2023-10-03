package et.nate.poll.security;

import et.nate.poll.user.UserRepository;
import et.nate.poll.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AuthController(TokenService tokenService, PasswordEncoder encoder, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(Authentication authentication) {
        LOGGER.debug("Token Requested For user {}", authentication.getName());
        var token = tokenService.generateToken(authentication);
        LOGGER.debug("Token generated {}", token);
        return token;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegistrationRequest request) {

        userRepository.save(new User(null, request.email(), encoder.encode(request.password()), null, true,
                null, null, null, null, null, null, null));

        return "Success";
    }
}
