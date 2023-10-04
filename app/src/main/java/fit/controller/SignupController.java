package fit.controller;

import fit.SignupCommandExecutor;
import fit.command.Signup;
import fit.command.SignupWithPasswordHash;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class SignupController {

    private final SignupCommandExecutor executor;
    private final PasswordEncoder passwordEncoder;

    public SignupController(SignupCommandExecutor executor, PasswordEncoder passwordEncoder) {
        this.executor = executor;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody Signup command) {
        SignupWithPasswordHash signupWithPasswordHash = new SignupWithPasswordHash(
                UUID.randomUUID(),
                command.email(),
                passwordEncoder.encode(command.password()));
        executor.execute(signupWithPasswordHash);
        return ResponseEntity.ok().build();
    }
}
