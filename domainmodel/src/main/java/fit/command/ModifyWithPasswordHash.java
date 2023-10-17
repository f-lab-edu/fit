package fit.command;

public record ModifyWithPasswordHash (String email, String passwordHash, String nickname) { }