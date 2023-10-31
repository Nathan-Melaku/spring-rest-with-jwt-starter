package et.nate.poll.secured;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
public class TestControllerTest {

    @LocalServerPort
    int port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("given no authentication when accessing secured endpoint should return 401")
    public void rootWhenUnAuthenticatedThen401() throws Exception {
        given()
                .get("/")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("given token from registered user when accessing secured endpoint should return successful")
    public void rootWhenRegisteredLoggedInAuthenticatedWithOAuth2ThenSuccessFull() throws Exception {

        var payload = """
                    {
                        "email": "what@email.com",
                        "password": "1234"
                    }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/register")
                .then()
                .statusCode(200);

        var token = given()
                .auth().basic("what@email.com", "1234")
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract().body().asString();

        given()
                .auth().oauth2(token)
                .when()
                .get("/")
                .then()
                .body(equalTo("SuccessFull"));
    }
}