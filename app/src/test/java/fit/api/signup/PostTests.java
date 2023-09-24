package fit.api.signup;

import fit.AutoParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostTests {

    private final TestRestTemplate client;

    public PostTests(@Autowired TestRestTemplate client) {
        this.client = client;
    }

    @AutoParameterizedTest
    void sut_returns_200_status_code(String localPart, String password) {
        // Arrange
        HashMap<String, String> command = new HashMap<>();
        command.put("email", localPart + "@fit.com");
        command.put("password", password);

        // Act
        ResponseEntity<Void> response = client.postForEntity(
                "/api/signup",
                command,
                void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}
