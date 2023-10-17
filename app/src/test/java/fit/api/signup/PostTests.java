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
    void sut_returns_200_status_code(String localPart, String password, String nickname) {
        // Arrange
        HashMap<String, String> command = new HashMap<>();
        command.put("email", localPart + "@fit.com");
        command.put("password", password);
        command.put("nickname", nickname);

        // Act
        ResponseEntity<Void> response = client.postForEntity(
                "/api/signup",
                command,
                void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_email_missed(String password, String nickname) {
        // Arrange
        HashMap<String, String> command = new HashMap<>();
        command.put("password", password);
        command.put("nickname", nickname);

        // Act
        ResponseEntity<Void> response = client.postForEntity(
                "/api/signup",
                command,
                void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_password_missed(String localPart, String nickname) {
        // Arrange
        HashMap<String, String> command = new HashMap<>();
        command.put("email", localPart + "@fit.com");
        command.put("nickname", nickname);

        // Act
        ResponseEntity<Void> response = client.postForEntity(
                "/api/signup",
                command,
                void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_nickname_missed(String localPart, String password) {
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
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_email_already_exists(
            String localPart,
            String password1,
            String password2,
            String nickname
    ) {
        String email = localPart + "@fit.com";
        signup(email, password1, nickname);

        ResponseEntity<Void> response = signup(email, password2, nickname);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    private ResponseEntity<Void> signup(String email, String password, String nickname) {
        HashMap<String, String> command1 = new HashMap<>();
        command1.put("email", email);
        command1.put("password", password);
        command1.put("nickname", nickname);
        return client.postForEntity("/api/signup", command1, void.class);
    }
}
