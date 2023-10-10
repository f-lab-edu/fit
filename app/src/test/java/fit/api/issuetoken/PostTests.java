package fit.api.issuetoken;

import fit.AutoParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
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
        String email = localPart + "@fit.com";

        signup(client, email, password);

        HashMap<String, String> issueToken = new HashMap<>();
        issueToken.put("email", email);
        issueToken.put("password", password);

        // Act
        ResponseEntity<Void> response = client.postForEntity(
                "/api/issue-token",
                issueToken,
                void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @AutoParameterizedTest
    void sut_returns_token(String localPart, String password) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(client, email, password);

        // Act
        ResponseEntity<HashMap<String, Object>> response = issueToken(email, password);

        // Assert
        HashMap<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsKey("token");
        assertThat(body.get("token")).isInstanceOf(String.class);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_no_exist_member(String localPart, String password) {
        String email = localPart + "@fit.com";

        ResponseEntity<?> response = issueToken(email, password);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_wrong_password(String localPart, String password, String wrongPassword) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(client, email, password);

        // Act
        ResponseEntity<?> response = issueToken(email, wrongPassword);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    private static RequestEntity<HashMap<String, String>> createIssueTokenRequest(String email, String password) {
        HashMap<String, String> issueToken = new HashMap<>();
        issueToken.put("email", email);
        issueToken.put("password", password);
        return new RequestEntity<>(
                issueToken,
                HttpMethod.POST,
                URI.create("/api/issue-token"));
    }

    private ResponseEntity<HashMap<String, Object>> issueToken(String email, String password) {
        RequestEntity<HashMap<String, String>> request = createIssueTokenRequest(email, password);
        return client.exchange(
                request,
                new ParameterizedTypeReference<>() {
                });
    }
}