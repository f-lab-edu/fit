package fit.api.info;

import fit.AutoParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static fit.api.ApiTestLanguage.issueToken;
import static fit.api.ApiTestLanguage.signup;
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
    void sut_returns_403_status_code_token_timeout(String localPart, String password) throws InterruptedException {
        // Arrange
        String email = localPart + "@fit.com";
        signup(client, email, password);
        String token = issueToken(client, email, password);

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
}
