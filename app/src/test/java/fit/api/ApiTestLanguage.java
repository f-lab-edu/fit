package fit.api;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;
import java.util.HashMap;

public class ApiTestLanguage {

    public static ResponseEntity<Void> signup(TestRestTemplate client, String email, String password) {
        HashMap<String, String> command = new HashMap<>();
        command.put("email", email);
        command.put("password", password);
        return signup(client, command);
    }

    public static void signup(TestRestTemplate client, Email email, String password) {
        signup(client, email.toString(), password);
    }

    public static ResponseEntity<Void> signup(TestRestTemplate client, HashMap<String, String> command) {
        return client.postForEntity("/api/signup", command, void.class);
    }

    public static String issueToken(TestRestTemplate client, String email, String password) {
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
        return body != null ? body.get("token").toString() : null;
    }

    public static String issueToken(TestRestTemplate client, Email email, String password) {
        return issueToken(client, email.toString(), password);
    }

    public static <T> ResponseEntity<T> getWithToken(
            TestRestTemplate client,
            String token,
            String path,
            Class<T> responseType
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        return client.exchange(path, HttpMethod.GET, request, responseType);
    }

    public static <T> ResponseEntity<T> getWithToken(
            TestRestTemplate client,
            String token,
            String path,
            ParameterizedTypeReference<T> responseType
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        return client.exchange(path, HttpMethod.GET, request, responseType);
    }

    public static <Body, Response> ResponseEntity<Response> postWithToken(
            TestRestTemplate client,
            String token,
            String path,
            Body body,
            Class<Response> responseType
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(body, headers);
        return client.exchange(path, HttpMethod.POST, request, responseType);
    }

    public static <Body, Response> ResponseEntity<Response> postWithToken(
            TestRestTemplate client,
            String token,
            String path,
            Body body,
            ParameterizedTypeReference<Response> responseType
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(body, headers);
        return client.exchange(path, HttpMethod.POST, request, responseType);
    }
}