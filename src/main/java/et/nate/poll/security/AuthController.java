package et.nate.poll.security;

import et.nate.poll.shared.ErrorMessage;
import et.nate.poll.user.UserRepository;
import et.nate.poll.user.entity.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

        var token = tokenService.generateToken(authentication);

        return token;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegistrationRequest request) {

        userRepository.save(new User(null, request.email(), encoder.encode(request.password()), null, true,
                null, null, null, null, null, null, null));

        return "Success";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String >();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorMessage handle(){
        return new ErrorMessage("Email already exists");
    }
}
