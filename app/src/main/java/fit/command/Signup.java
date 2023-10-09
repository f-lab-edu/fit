package fit.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record Signup(
        @NotNull
        @Email
        @Size(max = 100)
        String email,
        @NotNull
        @Size(min = 10, max = 50)
        String password
) { }