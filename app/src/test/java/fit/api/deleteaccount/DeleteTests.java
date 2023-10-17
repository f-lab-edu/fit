package fit.api.deleteaccount;

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
public class DeleteTests {

    private final TestRestTemplate client;

    public DeleteTests(@Autowired TestRestTemplate client) {
        this.client = client;
    }

    @AutoParameterizedTest
    void sut_returns_200_status_code(String localPart, String password) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password);

        String token = getIssueToken(email, password);

        // Act
        ResponseEntity<Void> response = deleteAccount(email, token);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @AutoParameterizedTest
    void sut_returns_403_status_code_if_email_missed(String localPart, String password) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password);

        String token = getIssueToken(email, password);

        // Act
        ResponseEntity<Void> response = deleteAccount("", token);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_email_not_match(String localPart, String localPart2, String password) {
        // Arrange
        String email = localPart + "@fit.com";
        String wrong_email = localPart2 + "@fit.com";
        signup(email, password);

        String token = getIssueToken(email, password);

        // Act
        ResponseEntity<Void> response = deleteAccount(wrong_email, token);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_403_status_code_after_delete_account_same_token_info(String localPart, String password) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password);

        String token = getIssueToken(email, password);

        deleteAccount(email, token);

        // Act
        ResponseEntity<HashMap<String, Object>> response = info(token);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_after_delete_account_issue_token(String localPart, String password) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password);

        String token = getIssueToken(email, password);

        deleteAccount(email, token);

        // Act
        ResponseEntity<HashMap<String, Object>> response = issueToken(email, password);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    private ResponseEntity<Void> deleteAccount(String paramEmail, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        return client.exchange(
                "/api/delete-account/" + paramEmail,
                HttpMethod.DELETE,
                request,
                void.class);
    }

    private void signup(String email, String password) {
        HashMap<String, String> command = new HashMap<>();
        command.put("email", email);
        command.put("password", password);
        client.postForEntity("/api/signup", command, void.class);
    }

    private ResponseEntity<HashMap<String, Object>> issueToken(String email, String password) {
        HashMap<String, String> issueToken = new HashMap<>();
        issueToken.put("email", email);
        issueToken.put("password", password);

        RequestEntity<HashMap<String, String>> request = new RequestEntity<>(
                issueToken,
                HttpMethod.POST,
                URI.create("/api/issue-token"));

        return client.exchange(
                request,
                new ParameterizedTypeReference<>() {});
    }

    private String getIssueToken(String email, String password) {
        ResponseEntity<HashMap<String, Object>> response = issueToken(email, password);

        HashMap<String, Object> body = response.getBody();
        return body != null ? body.get("token").toString() : "";
    }

    private ResponseEntity<HashMap<String, Object>> info(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        return client.exchange(
                "/api/info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {});
    }
}
