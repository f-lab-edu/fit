package fit.executor;

import fit.MemberRepository;

public class RemoveCommandExecutor {
    private final MemberRepository repository;

    public RemoveCommandExecutor(MemberRepository repository) {
        this.repository = repository;
    }

    public void execute(String email) {
        repository.deleteMemberByEmail(email);
    }
}
