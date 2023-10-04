package fit.controller;

import fit.Member;
import fit.MemberRepository;
import fit.query.IssueToken;
import jakarta.validation.Valid;
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

    public IssueTokenController(MemberRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/issue-token")
    public ResponseEntity<HashMap<String, Object>> issueToken(@Valid @RequestBody IssueToken query) {
        Optional<Member> member = repository.findMemberByEmail(query.email());

        if (member.isPresent() && passwordEncoder.matches(query.password(), member.map(Member::hashingPassword).orElse(""))) {
            HashMap<String, Object> body = new HashMap<>();
            body.put("token", "");
            return ResponseEntity.ok(body);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}