package fit.api.signup;

import fit.AutoParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static fit.api.ApiTestLanguage.signup;
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
        ResponseEntity<Void> response = signup(client, command);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_email_missed(String password) {
        // Arrange
        HashMap<String, String> command = new HashMap<>();
        command.put("password", password);

        // Act
        ResponseEntity<Void> response = signup(client, command);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_password_missed(String localPart) {
        // Arrange
        HashMap<String, String> command = new HashMap<>();
        command.put("email", localPart + "@fit.com");

        // Act
        ResponseEntity<Void> response = signup(client, command);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_email_already_exists(
            String localPart,
            String password1,
            String password2
    ) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(client, email, password1);

        // Act
        ResponseEntity<Void> response = signup(client, email, password2);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

}
