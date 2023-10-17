package fit.executor;

import fit.Member;
import fit.MemberRepository;
import fit.command.SignupWithPasswordHash;

public class SignupCommandExecutor {

    private final MemberRepository repository;

    public SignupCommandExecutor(MemberRepository repository) {
        this.repository = repository;
    }

    public void execute(SignupWithPasswordHash signupWithPasswordHash) {
        repository.createMember(new Member(
                signupWithPasswordHash.uuid(),
                signupWithPasswordHash.email(),
                signupWithPasswordHash.passwordHash(),
                signupWithPasswordHash.nickname()));
    }
}
