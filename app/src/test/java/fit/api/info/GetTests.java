package fit.api.info;

import fit.AutoParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetTests {

    private final TestRestTemplate client;

    public GetTests(@Autowired TestRestTemplate client) {
        this.client = client;
    }

    @AutoParameterizedTest
    void sut_returns_200_status_code(String localPart, String password, String nickname) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password, nickname);
        String token = issueToken(email, password);

        // Act
        ResponseEntity<HashMap<String, Object>> response = info(token);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @AutoParameterizedTest
    void sut_returns_email(String localPart, String password, String nickname) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password, nickname);
        String token = issueToken(email, password);

        // Act
        ResponseEntity<HashMap<String, Object>> response = info(token);

        // Assert
        HashMap<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).containsKey("email");
        assertThat(body).containsKey("nickname");
        assertThat(body.get("email")).isInstanceOf(String.class);
        assertThat(body.get("nickname")).isInstanceOf(String.class);
    }

    @AutoParameterizedTest
    void sut_returns_403_status_code_no_token() {
        ResponseEntity<HashMap<String, Object>> response = info("");

        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }

    @AutoParameterizedTest
    void sut_returns_403_status_code_wrong_token(String token) {
        ResponseEntity<HashMap<String, Object>> response = info(token);

        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }

    private void signup(String email, String password, String nickname) {
        HashMap<String, String> command = new HashMap<>();
        command.put("email", email);
        command.put("password", password);
        command.put("nickname", nickname);
        client.postForEntity("/api/signup", command, void.class);
    }

    private String issueToken(String email, String password) {
        HashMap<String, String> issueToken = new HashMap<>();
        issueToken.put("email", email);
        issueToken.put("password", password);

        RequestEntity<HashMap<String, String>> request = new RequestEntity<>(
                issueToken,
                HttpMethod.POST,
                URI.create("/api/issue-token"));

        ResponseEntity<HashMap<String, Object>> res = client.exchange(
                request,
                new ParameterizedTypeReference<>() {});

        HashMap<String, Object> body = res.getBody();
        return body != null ? body.get("token").toString() : "";
    }

    private ResponseEntity<HashMap<String, Object>> info(String token) {
        HttpHeaders headers = new HttpHeaders();
        if (!token.isEmpty()) {
            headers.set("Authorization", "Bearer " + token);
        }

        HttpEntity<String> request = new HttpEntity<>(headers);
        return client.exchange(
                "/api/info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {});
    }
}
