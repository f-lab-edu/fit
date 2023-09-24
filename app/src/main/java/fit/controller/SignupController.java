package fit.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {

    @PostMapping("/api/signup")
    public void signup() {
    }
}
