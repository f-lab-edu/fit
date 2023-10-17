package fit.executor;

import fit.Member;
import fit.MemberRepository;
import fit.command.ModifyWithPasswordHash;

public class ModifyCommandExecutor {
    private final MemberRepository repository;

    public ModifyCommandExecutor(MemberRepository repository) {
        this.repository = repository;
    }

    public void execute(ModifyWithPasswordHash modifyWithPasswordHash) {
        repository.updateMember(new Member(
                null,
                modifyWithPasswordHash.email(),
                modifyWithPasswordHash.passwordHash(),
                modifyWithPasswordHash.nickname()));
    }
}
