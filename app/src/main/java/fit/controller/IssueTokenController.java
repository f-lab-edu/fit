package fit.controller;

import fit.JwtConfig;
import fit.Member;
import fit.MemberRepository;
import fit.query.IssueToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
public class IssueTokenController {

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public IssueTokenController(MemberRepository repository,
                                PasswordEncoder passwordEncoder,
                                JwtConfig jwtConfig) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/api/issue-token")
    public ResponseEntity<HashMap<String, Object>> issueToken(@RequestBody IssueToken query) {
        Optional<Member> member = repository.findMemberByEmail(query.email());

        if (member.map(x -> passwordEncoder.matches(query.password(), x.passwordHash())).orElse(false)) {
            HashMap<String, Object> body = new HashMap<>();
            body.put("token", jwtConfig.createToken(member.get().id(), member.get().email()));
            return ResponseEntity.ok(body);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}