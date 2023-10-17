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

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"jwt.expiry.time=100"}
)
public class TimeoutTest {

    private final TestRestTemplate client;

    public TimeoutTest(@Autowired TestRestTemplate client) {
        this.client = client;
    }

    @AutoParameterizedTest
    void sut_returns_403_status_code_token_timeout(String localPart,
                                                   String password,
                                                   String nickname) throws InterruptedException {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password, nickname);
        String token = issueToken(email, password);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        Thread.sleep(1000);

        // Act
        ResponseEntity<Void> response = client.exchange(
                "/api/info",
                HttpMethod.GET,
                request,
                Void.class
        );

        // Assert
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
}
