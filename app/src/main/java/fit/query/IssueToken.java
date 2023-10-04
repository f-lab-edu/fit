package fit.query;

import jakarta.validation.constraints.NotNull;

public record IssueToken(
        @NotNull
        String email,
        @NotNull
        String password
){

}
