package et.nate.poll.security;

public record RegistrationRequest(
        String email,
        String password
) {
}
