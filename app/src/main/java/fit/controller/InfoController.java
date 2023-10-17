package fit.controller;

import fit.JwtConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class InfoController {

    private final JwtConfig jwtConfig;

    public InfoController(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @GetMapping("/api/info")
    public ResponseEntity<HashMap<String, Object>> info(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        HashMap<String, Object> body = new HashMap<>();
        body.put("email", jwtConfig.getEmail(token));
        body.put("nickname", jwtConfig.getNickname(token));
        return ResponseEntity.ok(body);
    }
}