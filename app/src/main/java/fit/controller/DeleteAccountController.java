package fit.controller;

import fit.executor.DeleteAccountExecutor;
import fit.JwtConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteAccountController {
    private final DeleteAccountExecutor executor;
    private final JwtConfig jwtConfig;

    public DeleteAccountController(DeleteAccountExecutor executor, JwtConfig jwtConfig) {
        this.executor = executor;
        this.jwtConfig = jwtConfig;
    }

    @DeleteMapping("/api/delete-account/{email}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String email, HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        if (jwtConfig.getEmail(token).equals(email)) {
            executor.execute(email);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}