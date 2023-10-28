package fit.controller;

import fit.MemberView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class InfoController {

    @GetMapping("/api/info")
    public ResponseEntity<HashMap<String, Object>> info(Authentication authentication) {
        MemberView memberView = (MemberView) authentication.getPrincipal();

        HashMap<String, Object> body = new HashMap<>();
        body.put("email", memberView.email());
        return ResponseEntity.ok(body);
    }
}
