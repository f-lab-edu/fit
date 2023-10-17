package fit.api.modify;

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
public class PutTests {

    private final TestRestTemplate client;

    public PutTests(@Autowired TestRestTemplate client) {
        this.client = client;
    }

    @AutoParameterizedTest
    void sut_returns_200_status_code(String localPart,
                                     String password,
                                     String nickname,
                                     String changeNickname,
                                     String changePassword) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password, nickname);

        String token = getIssueToken(email, password);

        HashMap<String, String> modify = new HashMap<>();
        modify.put("nickname", changeNickname);
        modify.put("password", changePassword);

        // Act
        ResponseEntity<Void> response = modify(token, modify);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @AutoParameterizedTest
    void sut_returns_200_status_match_change_nickname(String localPart,
                                                      String password,
                                                      String nickname,
                                                      String changeNickname,
                                                      String changePassword) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password, nickname);

        String token = getIssueToken(email, password);

        HashMap<String, String> modify = new HashMap<>();
        modify.put("nickname", changeNickname);
        modify.put("password", changePassword);
        modify(token, modify);

        String afterToken = getIssueToken(email, changePassword);

        // Act
        ResponseEntity<HashMap<String, Object>> response = info(afterToken);

        // Assert
        HashMap<String, Object> body = response.getBody();
        String getNickname = body != null ? body.get("nickname").toString() : "";
        assertThat(getNickname).isEqualTo(changeNickname);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_new_password_missed(String localPart,
                                                            String password,
                                                            String nickname,
                                                            String changePassword) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password, nickname);

        String token = getIssueToken(email, password);

        HashMap<String, String> modify = new HashMap<>();
        modify.put("password", changePassword);

        // Act
        ResponseEntity<Void> response = modify(token, modify);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_new_nickname_missed(String localPart,
                                                            String password,
                                                            String nickname,
                                                            String changeNickname) {
        // Arrange
        String email = localPart + "@fit.com";
        signup(email, password, nickname);

        String token = getIssueToken(email, password);

        HashMap<String, String> modify = new HashMap<>();
        modify.put("nickname", changeNickname);

        // Act
        ResponseEntity<Void> response = modify(token, modify);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    private ResponseEntity<Void> modify(String token, HashMap<String, String> modify) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<HashMap<String, String>> request = new HttpEntity<>(modify, headers);

        return client.exchange(
                "/api/modify-member",
                HttpMethod.PUT,
                request,
                void.class);
    }

    private void signup(String email, String password, String nickname) {
        HashMap<String, String> command = new HashMap<>();
        command.put("email", email);
        command.put("password", password);
        command.put("nickname", nickname);
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
