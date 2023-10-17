package fit.executor;

import fit.MemberRepository;

public class DeleteAccountExecutor {
    private final MemberRepository repository;

    public DeleteAccountExecutor(MemberRepository repository) {
        this.repository = repository;
    }

    public void execute(String email) {
        repository.deleteMemberByEmail(email);
    }
}
