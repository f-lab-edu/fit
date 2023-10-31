package fit.api.gym;

import fit.AutoParameterizedTest;
import fit.GymCategory;
import fit.GymView;
import fit.api.Email;
import fit.command.RegisterGymRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static fit.api.ApiTestLanguage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostTests {

    private final TestRestTemplate client;

    public PostTests(@Autowired TestRestTemplate client) {
        this.client = client;
    }

    @AutoParameterizedTest
    void sut_returns_200_status_code(
            Email email,
            String password,
            RegisterGymRequest command
    ) {
        // Arrange
        signup(client, email, password);
        String token = issueToken(client, email, password);

        // Act
        ResponseEntity<Void> response = postWithToken(
                client,
                token,
                "/api/gyms/register-gym",
                command,
                Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @AutoParameterizedTest
    void sut_returns_id_of_new_gym(
            Email email,
            String password,
            RegisterGymRequest command
    ) {
        // Arrange
        signup(client, email, password);
        String token = issueToken(client, email, password);

        // Act
        ResponseEntity<Map<String, Object>> response = postWithToken(
                client,
                token,
                "/api/gyms/register-gym",
                command,
                new ParameterizedTypeReference<>() {});

        // Assert
        assertThat(response.getBody()).containsKey("id");
    }

    @AutoParameterizedTest
    void sut_creates_new_gym(
            Email email,
            String password,
            RegisterGymRequest command
    ) {
        // Arrange
        signup(client, email, password);
        String token = issueToken(client, email, password);

        // Act
        Object gymId = registerGym(command, token);

        // Assert
        ResponseEntity<GymView> response = getGym(token, gymId);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @AutoParameterizedTest
    void sut_correctly_sets_properties_of_new_gym(
            Email email,
            String password,
            RegisterGymRequest command
    ) {
        // Arrange
        signup(client, email, password);
        String token = issueToken(client, email, password);

        // Act
        Object gymId = registerGym(command, token);

        // Assert
        ResponseEntity<GymView> response = getGym(token, gymId);
        GymView actual = response.getBody();
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo((int) gymId);
        assertThat(actual.displayName()).isEqualTo(command.displayName());
        assertThat(actual.region()).isEqualTo(command.region());
        assertThat(actual.phone()).isEqualTo(command.phone());
        assertThat(actual.address()).isEqualTo(command.address());
        assertThat(actual.category()).isEqualTo(command.category());
        assertThat(actual.owner().email()).isEqualTo(email.toString());
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_display_name_missed(
            Email email,
            String password,
            String region,
            String phone,
            String address,
            GymCategory category
    ) {
        // Arrange
        signup(client, email, password);
        String token = issueToken(client, email, password);
        RegisterGymRequest command = new RegisterGymRequest(null, region, phone, address, category);

        // Act
        ResponseEntity<Void> response = postWithToken(
                client,
                token,
                "/api/gyms/register-gym",
                command,
                Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_region_missed(
            Email email,
            String password,
            String displayName,
            String phone,
            String address,
            GymCategory category
    ) {
        // Arrange
        signup(client, email, password);
        String token = issueToken(client, email, password);
        RegisterGymRequest command = new RegisterGymRequest(displayName, null, phone, address, category);

        // Act
        ResponseEntity<Void> response = postWithToken(
                client,
                token,
                "/api/gyms/register-gym",
                command,
                Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_address_missed(
            Email email,
            String password,
            String displayName,
            String region,
            String phone,
            GymCategory category
    ) {
        // Arrange
        signup(client, email, password);
        String token = issueToken(client, email, password);
        RegisterGymRequest command = new RegisterGymRequest(displayName, region, phone, null, category);

        // Act
        ResponseEntity<Void> response = postWithToken(
                client,
                token,
                "/api/gyms/register-gym",
                command,
                Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest
    void sut_returns_400_status_code_if_category_missed(
            Email email,
            String password,
            String displayName,
            String region,
            String phone,
            String address
    ) {
        // Arrange
        signup(client, email, password);
        String token = issueToken(client, email, password);
        RegisterGymRequest command = new RegisterGymRequest(displayName, region, phone, address, null);

        // Act
        ResponseEntity<Void> response = postWithToken(
                client,
                token,
                "/api/gyms/register-gym",
                command,
                Void.class);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @AutoParameterizedTest()
    void sut_returns_400_status_code_if_category_not_match_type(
            String displayName,
            String region,
            String phone,
            String address,
            String category
    ) {
        assertThrows(IllegalArgumentException.class,
                () -> new RegisterGymRequest(
                        displayName,
                        region,
                        phone,
                        address,
                        GymCategory.valueOf(category)));
    }

    private Object registerGym(RegisterGymRequest command, String token) {
        ResponseEntity<Map<String, Object>> commandResponse = postWithToken(
                client,
                token,
                "/api/gyms/register-gym",
                command,
                new ParameterizedTypeReference<>() {});
        //noinspection DataFlowIssue
        return commandResponse.getBody().get("id");
    }

    private ResponseEntity<GymView> getGym(String token, Object gymId) {
        return getWithToken(
                client,
                token,
                "/api/gyms/" + gymId,
                GymView.class);
    }
}