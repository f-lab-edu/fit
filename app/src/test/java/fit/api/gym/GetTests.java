package fit.api.gym;

import fit.AutoParameterizedTest;
import fit.api.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static fit.api.ApiTestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetTests {

    private final TestRestTemplate client;

    public GetTests(@Autowired TestRestTemplate client) {
        this.client = client;
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_not_found_gym(
            Email email,
            String password,
            long gymId
    ) {
        // Arrange
        signup(client, email, password);
        String token = issueToken(client, email, password);

        // Act
        ResponseEntity<Void> response = getWithToken(
                client,
                token,
                "/api/gyms/" + gymId,
                Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}