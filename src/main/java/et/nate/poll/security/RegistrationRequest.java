package et.nate.poll.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Please insert a valid email")
        String email,
        @NotBlank(message = "Password is mandatory")
        String password
) {
}
