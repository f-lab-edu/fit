package fit;

import fit.command.Signup;
import java.util.UUID;

public class SignupCommandExecutor {

    private final MemberRepository repository;

    public SignupCommandExecutor(MemberRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID id, Signup command) {
        repository.createMember(new Member(id, command.email()));
    }
}
