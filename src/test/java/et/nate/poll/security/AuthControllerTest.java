package et.nate.poll.security;

import et.nate.poll.shared.ErrorMessage;
import et.nate.poll.user.UserRepository;
import et.nate.poll.user.entity.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class AuthControllerTest {

    @LocalServerPort
    int port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JdbcTemplate jdbcTemplate;

    String payload = """
                {
                    "email": "email@email.com",
                    "password": "1234Hello"
                }
            """;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

    @Test
    @DisplayName("given a valid email and password when register should register user")
    void givenAValidEmailAndPasswordWhenRegisterShouldRegisterUser() {

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/register")
                .then()
                .statusCode(200);

        assertTrue(userRepository.existsByEmail("email@email.com"));
    }

    @Test
    @DisplayName("given an Invalid email when register should return Error with email message")
    void givenAnInvalidEmailWhenRegisterShouldReturnErrorWithEmailMessage() {
        var payload = """
                    {
                        "email": "email.com",
                        "password": "1234Hello"
                    }
                """;

        var result = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .extract().body().as(Error.class);

        assertEquals(new Error("Please insert a valid email"), result);
        assertFalse(userRepository.existsByEmail("email.com"));
    }

    @Test
    @DisplayName("given a duplicate email when register should return error with email already exists")
    void givenADuplicateEmailWhenRegisterShouldReturnErrorWithEmailAlreadyExists() {
        createUser();
        var result = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .extract().as(ErrorMessage.class);
        assertEquals(new ErrorMessage("Email already exists"), result);
    }

    record Error(String email) {
    }

    @Test
    @DisplayName("given unauthenticated user when login should return unAuthenticated")
    void givenUnauthenticatedUserWhenLoginShouldReturnUnAuthenticated() {

        given()
                .auth()
                .basic("test@email.com", "test")
                .when()
                .post("/login")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("given authenticated user when login should return token")
    void givenAuthenticatedUserWhenLoginShouldReturnToken() {
        createUser();
        var result = given()
                .auth()
                .basic("email@email.com", "1234Hello")
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract().asString();
        assertFalse(result.isEmpty());
    }

    private void createUser() {
        userRepository.save(new User(null, "email@email.com", encoder.encode("1234Hello"), null, true,
                null, null, null, null, null, null, null));
        assertTrue(userRepository.existsByEmail("email@email.com"));
    }
}