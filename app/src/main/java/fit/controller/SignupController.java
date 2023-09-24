package fit.controller;

import fit.DuplicateEmailException;
import fit.SignupCommandExecutor;
import fit.command.Signup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class SignupController {

    private final SignupCommandExecutor executor;

    public SignupController(SignupCommandExecutor executor) {
        this.executor = executor;
    }

    @PostMapping("/api/signup")
    public ResponseEntity<Void> signup(@RequestBody Signup command) {
        if (command.email() == null || command.password() == null) {
            return ResponseEntity.badRequest().build();
        } else {
            try {
                executor.execute(UUID.randomUUID(), command);
            } catch(DuplicateEmailException exception) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
    }
}
