package fit.command;

import java.util.UUID;

public record SignupWithPasswordHash (UUID uuid, String email, String passwordHash) { }
