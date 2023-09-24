package fit;

import fit.command.Signup;

import java.util.Optional;
import java.util.UUID;

public class SignupCommandExecutor {

    private final MemberRepository repository;

    public SignupCommandExecutor(MemberRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID id, Signup command) {
        Optional<Member> member = repository.findMemberByEmail(command.email());
        if (member.isPresent()) {
            throw new DuplicateEmailException();
        }
        repository.createMember(new Member(id, command.email()));
    }
}
