package fit.controller;

import fit.JwtConfig;
import fit.command.ModifyWithPasswordHash;
import fit.executor.ModifyCommandExecutor;
import fit.query.Modify;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModifyController {

    private final JwtConfig jwtConfig;
    private final ModifyCommandExecutor executor;
    private final PasswordEncoder passwordEncoder;

    public ModifyController(JwtConfig jwtConfig, ModifyCommandExecutor executor, PasswordEncoder passwordEncoder) {
        this.jwtConfig = jwtConfig;
        this.executor = executor;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/api/modify-member")
    public ResponseEntity<Void> modify(@RequestBody Modify query, HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        if(query.password() == null || query.nickname() == null) {
            return ResponseEntity.badRequest().build();
        } else {
            ModifyWithPasswordHash modifyWithPasswordHash = new ModifyWithPasswordHash(
                    jwtConfig.getEmail(token),
                    passwordEncoder.encode(query.password()),
                    query.nickname());
            executor.execute(modifyWithPasswordHash);
            return ResponseEntity.ok().build();
        }
    }
}