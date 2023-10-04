package fit.api.signup;

import fit.AutoParameterizedTest;
import fit.SecurityConfig;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_email_missed(String password) {
        // Arrange
        HashMap<String, String> command = new HashMap<>();
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
    void sut_returns_400_status_code_if_password_missed(String localPart) {
        // Arrange
        HashMap<String, String> command = new HashMap<>();
        command.put("email", localPart + "@fit.com");

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
            String password2
    ) {
        String email = localPart + "@fit.com";
        signup(email, password1);

        ResponseEntity<Void> response = signup(email, password2);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_hashing_password(String localPart) {
        // Arrange
        PasswordEncoder passwordEncoder = new SecurityConfig().passwordEncoder();

        // Act
        String hashingPassword = passwordEncoder.encode(localPart);

        // Assert
        Assertions.assertThat(passwordEncoder.matches(localPart, hashingPassword)).isTrue();
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_not_email_type(String localPart, String password) {
        String email = localPart;

        ResponseEntity<Void> response = signup(email, password);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_email_over_100_size(String localPart, String password) {
        String email = localPart.repeat(10).substring(0,100) + "@fit.com";

        ResponseEntity<Void> response = signup(email, password);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_password_min_10_size(String localPart, String localPart2) {
        String email = localPart + "@fit.com";
        String password = localPart2.substring(0, 5);

        ResponseEntity<Void> response = signup(email, password);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_password_over_50_size(String localPart, String localPart2) {
        String email = localPart + "@fit.com";
        String password = localPart2.repeat(2).substring(0, 55);

        ResponseEntity<Void> response = signup(email, password);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    private ResponseEntity<Void> signup(String email, String password) {
        HashMap<String, String> command1 = new HashMap<>();
        command1.put("email", email);
        command1.put("password", password);
        return client.postForEntity("/api/signup", command1, void.class);
    }
}
