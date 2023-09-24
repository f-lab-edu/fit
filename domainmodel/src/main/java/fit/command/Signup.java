package fit.command;

import java.beans.ConstructorProperties;

public record Signup(String email, String password) {

    @ConstructorProperties({"email", "password"})
    public Signup {
    }
}
