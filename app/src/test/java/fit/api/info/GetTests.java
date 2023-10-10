package fit.api.info;

import fit.AutoParameterizedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.HashMap;

import static fit.api.ApiTestLanguage.*;
import static fit.api.ApiTestLanguage.issueToken;
import static fit.api.ApiTestLanguage.signup;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetTests {

    private final TestRestTemplate client;

    public GetTests(@Autowired TestRestTemplate client) {
        this.client = client;
    }

    @AutoParameterizedTest
    void sut_returns_200_status_code(String localPart, String password) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(client, email, password);
        String token = issueToken(client, email, password);

        // Act
        ResponseEntity<Void> response = getWithToken(
                client,
                token,
                "/api/info",
                new ParameterizedTypeReference<>() {});

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @AutoParameterizedTest
    void sut_returns_email(String localPart, String password) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(client, email, password);
        String token = issueToken(client, email, password);

        // Act
        ResponseEntity<HashMap<String, Object>> response = getWithToken(
                client,
                token,
                "/api/info",
                new ParameterizedTypeReference<>() {});

        // Assert
        HashMap<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsKey("email");
        assertThat(body.get("email")).isInstanceOf(String.class);
    }

    @AutoParameterizedTest
    void sut_returns_403_status_code_no_token() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<?> response = client.exchange(
                "/api/info",
                HttpMethod.GET,
                request,
                Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }

    @AutoParameterizedTest
    void sut_returns_403_status_code_wrong_token(String token) {
        ResponseEntity<?> response = getWithToken(client, token, "/api/info", new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }
}
